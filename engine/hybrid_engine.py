import os
import sys
from typing import Dict, Any, Optional

# Pastikan path engine lokal terdaftar di sys.path
current_dir = os.path.dirname(os.path.abspath(__file__))
if current_dir not in sys.path:
    sys.path.insert(0, current_dir)

# Import engine dari modul masing-masing
try:
    from winnowing_engine import StandaloneWinnowingEngine
    from java_ast_engine import StandaloneJavaASTEngine, JavaASTNode, JavaLexer
except ImportError:
    from publikasi.engine.winnowing_engine import StandaloneWinnowingEngine
    from publikasi.engine.java_ast_engine import StandaloneJavaASTEngine, JavaASTNode, JavaLexer


# ==============================================================================
# PENGATURAN PARAMETER DEFAULT HYBRID ENGINE (Ubah di sini agar mudah dikoreksi)
# ==============================================================================
DEFAULT_K = StandaloneWinnowingEngine.DEFAULT_K
DEFAULT_W = StandaloneWinnowingEngine.DEFAULT_W
DEFAULT_LOWER_THRESHOLD = 0.15              # Ambang batas bawah Fast-Exit: <= 15% langsung Non-Plagiat (Skip AST)
DEFAULT_UPPER_THRESHOLD = 0.70              # Ambang batas atas Fast-Exit: >= 70% langsung Plagiat (Skip AST)
DEFAULT_AST_THRESHOLD = StandaloneJavaASTEngine.DEFAULT_THRESHOLD
DEFAULT_WEIGHT_STRUCTURAL = StandaloneJavaASTEngine.DEFAULT_WEIGHT_STRUCTURAL
DEFAULT_WEIGHT_SUBTREE = StandaloneJavaASTEngine.DEFAULT_WEIGHT_SUBTREE
# ==============================================================================


class StandaloneHybridEngine:
    """
    Engine Deteksi Plagiarisme Java Hybrid Bertahap (Multi-Stage Pipeline)
    Menggunakan Skema 3-Zona Adaptif (Fast-Exit Winnowing + Deep Structural AST):
    
    1. Zona 1 - Fast-Exit Non-Plagiat (winnowing_sim <= lower_threshold, misal <= 15%):
       - Kode terbukti sangat berbeda secara tekstual.
       - Eksekusi AST di-SKIP total untuk menghemat komputasi.
       - Kesimpulan: Non-Plagiat (Fast-Exit).
       
    2. Zona 2 - Fast-Exit Plagiat (winnowing_sim >= upper_threshold, misal >= 70%):
       - Kode terbukti memiliki kemiripan tekstual sangat tinggi (Exact / Type-1 Plagiarism).
       - Eksekusi AST di-SKIP total karena status plagiat sudah pasti.
       - Kesimpulan: Plagiat (Fast-Exit).
       
    3. Zona 3 - Zona Abu-abu / Ambigu (lower_threshold < winnowing_sim < upper_threshold):
       - Kode berada pada area batas (kemungkinan Type-2 / Type-3 Plagiarism dengan identifier
         diganti atau struktur fungsi direfaktorisasi).
       - Eksekusi AST Java dijalankan secara mendalam menggunakan kombinasi bobot:
         Skor AST = (weight_structural * Structural_Sim) + (weight_subtree * Subtree_FP_Sim)
       - Ditentukan berdasarkan ambang batas `ast_threshold` (default: 0.65).
    """

    # Akses parameter default dari level modul
    DEFAULT_K = DEFAULT_K
    DEFAULT_W = DEFAULT_W
    DEFAULT_LOWER_THRESHOLD = DEFAULT_LOWER_THRESHOLD
    DEFAULT_UPPER_THRESHOLD = DEFAULT_UPPER_THRESHOLD
    DEFAULT_AST_THRESHOLD = DEFAULT_AST_THRESHOLD
    DEFAULT_WEIGHT_STRUCTURAL = DEFAULT_WEIGHT_STRUCTURAL
    DEFAULT_WEIGHT_SUBTREE = DEFAULT_WEIGHT_SUBTREE

    def __init__(
        self,
        k: int = DEFAULT_K,
        w: int = DEFAULT_W,
        winnowing_lower_threshold: float = DEFAULT_LOWER_THRESHOLD,
        winnowing_upper_threshold: float = DEFAULT_UPPER_THRESHOLD,
        ast_threshold: float = DEFAULT_AST_THRESHOLD,
        weight_structural: float = DEFAULT_WEIGHT_STRUCTURAL,
        weight_subtree: float = DEFAULT_WEIGHT_SUBTREE
    ):
        """
        Inisialisasi Engine Hybrid dengan parameter optimal hasil Grid Search Tuning:
        
        :param k: Ukuran k-gram untuk Winnowing (default: 7, hasil tuning optimal Rank #1)
        :param w: Ukuran sliding window untuk Winnowing (default: 8, hasil tuning optimal Rank #1)
        :param winnowing_lower_threshold: Batas ambang bawah (default: 0.15 / 15%). Jika winnowing <= 0.15, SKIP AST (Fast-Exit Non-Plagiat).
        :param winnowing_upper_threshold: Batas ambang atas (default: 0.70 / 70%). Jika winnowing >= 0.70, SKIP AST (Fast-Exit Plagiat).
        :param ast_threshold: Batas ambang AST penentu plagiarisme di zona abu-abu 15%-70% (default: 0.65 / 65%).
        :param weight_structural: Bobot kemiripan struktural AST (default: 0.70).
        :param weight_subtree: Bobot kemiripan subtree fingerprint AST (default: 0.30).
        """
        self.k = k
        self.w = w
        self.winnowing_lower_threshold = winnowing_lower_threshold
        self.winnowing_upper_threshold = winnowing_upper_threshold
        self.ast_threshold = ast_threshold
        self.weight_structural = weight_structural
        self.weight_subtree = weight_subtree

        # Inisialisasi sub-engine dengan parameter yang terkonfigurasi
        self.winnowing_engine = StandaloneWinnowingEngine(k=self.k, w=self.w)
        self.ast_engine = StandaloneJavaASTEngine(
            weight_structural=self.weight_structural,
            weight_subtree=self.weight_subtree,
            threshold=self.ast_threshold
        )

    def calculate_similarity(
        self,
        code1: str,
        code2: str,
        k: Optional[int] = None,
        w: Optional[int] = None,
        winnowing_lower_threshold: Optional[float] = None,
        winnowing_upper_threshold: Optional[float] = None,
        ast_threshold: Optional[float] = None,
        weight_structural: Optional[float] = None,
        weight_subtree: Optional[float] = None
    ) -> float:
        """
        Menghitung kesamaan final secara bertahap menggunakan skema 3 zona adaptif.
        """
        result = self.detect(
            code1,
            code2,
            k=k,
            w=w,
            winnowing_lower_threshold=winnowing_lower_threshold,
            winnowing_upper_threshold=winnowing_upper_threshold,
            ast_threshold=ast_threshold,
            weight_structural=weight_structural,
            weight_subtree=weight_subtree
        )
        return result["similarity"]

    def detect(
        self,
        code1: str,
        code2: str,
        k: Optional[int] = None,
        w: Optional[int] = None,
        winnowing_lower_threshold: Optional[float] = None,
        winnowing_upper_threshold: Optional[float] = None,
        ast_threshold: Optional[float] = None,
        threshold: Optional[float] = None,
        weight_structural: Optional[float] = None,
        weight_subtree: Optional[float] = None
    ) -> Dict[str, Any]:
        """
        Mendeteksi plagiarisme antar dua kode Java dengan skema 3 zona bertahap (Winnowing -> AST).
        - winn_sim <= lower_threshold (<= 15%): jangan panggil AST (Fast Exit - Non Plagiat)
        - winn_sim >= upper_threshold (>= 70%): jangan panggil AST (Fast Exit - Plagiat)
        - 15% < winn_sim < 70% (Grey Zone): panggil AST untuk verifikasi struktural mendalam
        """
        k_val = k if k is not None else self.k
        w_val = w if w is not None else self.w
        lower_th = winnowing_lower_threshold if winnowing_lower_threshold is not None else self.winnowing_lower_threshold
        upper_th = winnowing_upper_threshold if winnowing_upper_threshold is not None else self.winnowing_upper_threshold
        a_th = ast_threshold if ast_threshold is not None else (
            threshold if threshold is not None else self.ast_threshold
        )
        w_s = weight_structural if weight_structural is not None else self.weight_structural
        w_f = weight_subtree if weight_subtree is not None else self.weight_subtree

        # -------------------------------------------------------------
        # TAHAP 1: PENGUJIAN WINNOWING
        # -------------------------------------------------------------
        if self.winnowing_engine.k == k_val and self.winnowing_engine.w == w_val:
            winn_engine = self.winnowing_engine
        else:
            winn_engine = StandaloneWinnowingEngine(k=k_val, w=w_val)

        winn_sim = winn_engine.calculate_similarity(code1, code2)

        if winn_sim <= lower_th:
            # Skor <= ambang bawah (misal <= 15%) -> SKIP AST (Non-plagiat jelas)
            ast_executed = False
            ast_sim = 0.0
            final_sim = winn_sim
            is_plagiarized = False
            decision_stage = "Stage 1: Winnowing Low (AST Skipped)"
            decision_zone = f"Low <= {lower_th*100:.0f}% (AST Skipped)"
            stage = 1
            structural_sim = 0.0
            fp_sim = 0.0
        elif winn_sim >= upper_th:
            # Skor >= ambang atas (misal >= 70%) -> SKIP AST (Plagiat jelas)
            ast_executed = False
            ast_sim = 0.0
            final_sim = winn_sim
            is_plagiarized = True
            decision_stage = "Stage 1: Winnowing High (AST Skipped)"
            decision_zone = f"High >= {upper_th*100:.0f}% (AST Skipped)"
            stage = 1
            structural_sim = 0.0
            fp_sim = 0.0
        else:
            # ---------------------------------------------------------
            # TAHAP 2: ZONA AMBIGU (15% < winn_sim < 70%) -> PANGGIL AST
            # ---------------------------------------------------------
            ast_executed = True
            ast_result = self.ast_engine.detect(
                code1,
                code2,
                threshold=a_th,
                weight_structural=w_s,
                weight_subtree=w_f
            )
            structural_sim = ast_result.get("structural_similarity", 0.0)
            fp_sim = ast_result.get("fingerprint_similarity", 0.0)
            ast_sim = ast_result.get("similarity", w_s * structural_sim + w_f * fp_sim)

            final_sim = max(winn_sim, ast_sim)
            is_plagiarized = final_sim >= a_th
            decision_stage = "Stage 2: AST Verification"
            decision_zone = f"Grey Zone {lower_th*100:.0f}%-{upper_th*100:.0f}% (AST Analyzed)"
            stage = 2

        return {
            "similarity": final_sim,
            "winnowing_similarity": winn_sim,
            "ast_similarity": ast_sim,
            "ast_executed": ast_executed,
            "stage": stage,
            "decision_stage": decision_stage,
            "decision_zone": decision_zone,
            "is_plagiarized": is_plagiarized,
            "threshold": a_th if ast_executed else (upper_th if is_plagiarized else lower_th),
            "k": k_val,
            "w": w_val,
            "winnowing_lower_threshold": lower_th,
            "winnowing_upper_threshold": upper_th,
            "ast_threshold": a_th,
            "weight_structural": w_s,
            "weight_subtree": w_f,
            "structural_similarity": structural_sim,
            "fingerprint_similarity": fp_sim
        }


# Alias class untuk fleksibilitas
HybridEngine = StandaloneHybridEngine


if __name__ == "__main__":
    print("=" * 70)
    print("   STANDALONE HYBRID ENGINE (WINNOWING + AST ADAPTIF 3-ZONA)")
    print("=" * 70)
    engine = StandaloneHybridEngine()
    print(f"Konfigurasi Parameter Default:")
    print(f"  - k-gram (k)                   : {engine.k}")
    print(f"  - Window Size (w)              : {engine.w}")
    print(f"  - Fast-Exit Bawah (<= {engine.winnowing_lower_threshold*100:.0f}%)    : Skip AST -> Non-Plagiat")
    print(f"  - Fast-Exit Atas  (>= {engine.winnowing_upper_threshold*100:.0f}%)    : Skip AST -> Plagiat")
    print(f"  - Grey Zone Threshold          : {engine.ast_threshold*100:.0f}%")
    print(f"  - AST Structural Weight        : {engine.weight_structural*100:.0f}%")
    print(f"  - AST Subtree FP Weight        : {engine.weight_subtree*100:.0f}%")
    print("=" * 70)
