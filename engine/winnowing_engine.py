import re
from typing import Set, List, Dict, Any, Tuple

# ==============================================================================
# PENGATURAN PARAMETER DEFAULT WINNOWING ENGINE (Ubah di sini agar mudah dikoreksi)
# ==============================================================================
DEFAULT_K = 7                               # Ukuran k-gram (hasil tuning optimal Grid Search Rank #1)
DEFAULT_W = 8                               # Ukuran sliding window (hasil tuning optimal Grid Search Rank #1)
DEFAULT_THRESHOLD = 0.50                    # Ambang batas default deteksi plagiarisme
# ==============================================================================


class StandaloneWinnowingEngine:
    """
    Engine Winnowing Plagiarism Detector Mandiri (Khusus Java & Bahasa Pemrograman Umum)
    Menggunakan:
    1. Preprocessing (penghapusan komentar Java // dan /* */, normalisasi whitespace, lowercase)
    2. K-gram generation
    3. Rolling hash (Rabin-Karp)
    4. Window-based fingerprint selection (Winnowing algorithm)
    5. Jaccard similarity metric
    """

    DEFAULT_K = DEFAULT_K
    DEFAULT_W = DEFAULT_W
    DEFAULT_THRESHOLD = DEFAULT_THRESHOLD

    def __init__(self, k: int = DEFAULT_K, w: int = DEFAULT_W):
        self.k = k
        self.w = w
        self.base = 257          # Bilangan prima basis rolling hash
        self.mod = 10**9 + 7     # Bilangan prima modulus besar

    def remove_comments(self, code: str) -> str:
        """Menghapus komentar single-line (//) dan multi-line (/* */) pada kode Java."""
        # 1. Hapus multi-line comments
        code_no_multi = re.sub(r'/\*[\s\S]*?\*/', '', code)
        # 2. Hapus single-line comments
        code_no_comments = re.sub(r'//.*', '', code_no_multi)
        return code_no_comments

    def preprocess(self, code: str) -> str:
        """
        Preprocessing teks kode Java:
        - Hapus komentar
        - Hapus semua spasi, tab, newline
        - Ubah ke lowercase
        """
        code_clean = self.remove_comments(code)
        # Hapus seluruh karakter whitespace
        code_no_spaces = re.sub(r'\s+', '', code_clean)
        return code_no_spaces.lower()

    def generate_kgrams(self, text: str) -> List[str]:
        """Membuat k-gram dari string teks."""
        if len(text) < self.k:
            return []
        return [text[i : i + self.k] for i in range(len(text) - self.k + 1)]

    def rolling_hash(self, text: str) -> List[int]:
        """
        Menghasilkan deretan hash untuk setiap k-gram menggunakan Rabin-Karp Rolling Hash.
        """
        if len(text) < self.k:
            return []

        hashes = []
        # Hash pertama untuk k karakter pertama
        hash_val = 0
        for i in range(self.k):
            hash_val = (hash_val * self.base + ord(text[i])) % self.mod
        hashes.append(hash_val)

        # Nilai base^(k-1) % mod
        power = 1
        for _ in range(self.k - 1):
            power = (power * self.base) % self.mod

        # Rolling hash untuk karakter berikutnya
        for i in range(1, len(text) - self.k + 1):
            old_char = ord(text[i - 1])
            new_char = ord(text[i + self.k - 1])

            # Geser jendela hash: ((H_prev - old * power) * base + new) % mod
            hash_val = (hash_val - old_char * power) % self.mod
            hash_val = (hash_val * self.base + new_char) % self.mod
            hash_val = (hash_val + self.mod) % self.mod
            hashes.append(hash_val)

        return hashes

    def select_fingerprints(self, hashes: List[int]) -> Set[int]:
        """
        Memilih fingerprint minimum dari setiap sliding window berukuran w.
        """
        fingerprints = set()
        n = len(hashes)

        if n == 0:
            return fingerprints

        if n < self.w:
            fingerprints.add(min(hashes))
            return fingerprints

        for i in range(n - self.w + 1):
            window = hashes[i : i + self.w]
            min_val = min(window)
            fingerprints.add(min_val)

        return fingerprints

    def get_fingerprints(self, code: str) -> Set[int]:
        """Pipeline lengkap ekstraksi fingerprint dari kode sumber Java."""
        preprocessed = self.preprocess(code)
        hashes = self.rolling_hash(preprocessed)
        return self.select_fingerprints(hashes)

    def get_fingerprints_from_preprocessed(self, preprocessed: str) -> Set[int]:
        """Ekstraksi fingerprint dari teks kode yang sudah melalui tahap preprocessing."""
        hashes = self.rolling_hash(preprocessed)
        return self.select_fingerprints(hashes)

    def calculate_similarity_from_fingerprints(self, fp1: Set[int], fp2: Set[int]) -> float:
        """Menghitung Jaccard similarity antara dua himpunan fingerprint."""
        if not fp1 and not fp2:
            return 0.0
        union = fp1.union(fp2)
        if not union:
            return 0.0
        intersection = fp1.intersection(fp2)
        return len(intersection) / len(union)

    def calculate_similarity(self, code1: str, code2: str) -> float:
        """Menghitung skor kesamaan Winnowing antara dua kode Java [0.0 - 1.0]."""
        fp1 = self.get_fingerprints(code1)
        fp2 = self.get_fingerprints(code2)
        return self.calculate_similarity_from_fingerprints(fp1, fp2)

    def detect(self, code1: str, code2: str, threshold: float = DEFAULT_THRESHOLD) -> Dict[str, Any]:
        """
        Mendeteksi plagiarisme dan mengembalikan detail komparasi.
        """
        fp1 = self.get_fingerprints(code1)
        fp2 = self.get_fingerprints(code2)
        similarity = self.calculate_similarity_from_fingerprints(fp1, fp2)
        
        return {
            "similarity": similarity,
            "is_plagiarized": similarity >= threshold,
            "threshold": threshold,
            "fingerprints_count_1": len(fp1),
            "fingerprints_count_2": len(fp2),
            "shared_fingerprints": len(fp1.intersection(fp2))
        }
