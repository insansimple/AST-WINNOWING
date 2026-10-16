/*
 * Program   : MatrixMultiplication
 * Deskripsi : Mengalikan dua buah matriks. Dilengkapi pengecekan syarat
 *             perkalian (kolom A = baris B), perkalian dengan skalar,
 *             transpose matriks, input dari pengguna, tampilan matriks
 *             yang rapi, dan menu sederhana.
 */
import java.util.Scanner;

public class MatrixMultiplication {

    // Logika inti: mengalikan matriks a (r1 x c1) dengan b (c1 x c2)
    public int[][] kalikanMtx(int[][] mx1, int[][] mx2) {
        int bar1 = mx1.length;
        int kol1 = mx1[0].length;
        int kol2 = mx2[0].length;
        int[][] luaran = new int[bar1][kol2];
        for (int r = 0; r < bar1; r++) {
            for (int c = 0; c < kol2; c++) {
                for (int t = 0; t < kol1; t++) {
                    luaran[r][c] += mx1[r][t] * mx2[t][c];
                }
            }
        }
        return luaran;
    }

    // Mengecek syarat perkalian: jumlah kolom A harus sama dengan baris B
    public boolean layakKali(int[][] mx1, int[][] mx2) {
        return mx1[0].length == mx2.length;
    }

    // Mengalikan setiap elemen matriks dengan sebuah bilangan (skalar)
    public int[][] skalarkan(int[][] mtx, int faktor) {
        int[][] keluaran = new int[mtx.length][mtx[0].length];
        for (int r = 0; r < mtx.length; r++) {
            for (int c = 0; c < mtx[0].length; c++) {
                keluaran[r][c] = mtx[r][c] * faktor;
            }
        }
        return keluaran;
    }

    // Membuat transpose matriks (baris jadi kolom)
    public int[][] putar(int[][] mtx) {
        int[][] keluaran = new int[mtx[0].length][mtx.length];
        for (int r = 0; r < mtx.length; r++) {
            for (int c = 0; c < mtx[0].length; c++) {
                keluaran[c][r] = mtx[r][c];
            }
        }
        return keluaran;
    }

    // Menampilkan matriks ke layar
    public static void cetakMatriks(String label, int[][] mtx) {
        System.out.print(label);
        System.out.print(System.lineSeparator());
        for (int r = 0; r < mtx.length; r++) {
            System.out.print("| ");
            for (int c = 0; c < mtx[r].length; c++) {
                System.out.print(new StringBuilder().append(mtx[r][c]).append("\t").toString());
            }
            System.out.print("|");
            System.out.print(System.lineSeparator());
        }
    }

    // Membaca bilangan bulat dengan validasi sederhana
    public static int ambilInteger(Scanner pemindai, String teks) {
        System.out.print(teks);
        while (!pemindai.hasNextInt()) {
            System.out.print("Input harus angka bulat, ulangi: ");
            pemindai.next();
        }
        return pemindai.nextInt();
    }

    // Membaca ukuran (harus lebih dari 0)
    public static int ambilDimensi(Scanner pemindai, String teks) {
        int angka = 0;
        while (angka <= 0) {
            angka = ambilInteger(pemindai, teks);
            if (angka <= 0) {
                System.out.print("Ukuran harus lebih dari 0!");
                System.out.print(System.lineSeparator());
            }
        }
        return angka;
    }

    // Mengisi matriks dari input pengguna
    public static int[][] isiData(Scanner pemindai, String label, int jmlBaris, int jmlKolom) {
        int[][] mtx = new int[jmlBaris][jmlKolom];
        System.out.print(new StringBuilder().append("\nIsi matriks ").append(label).append(":").toString());
        System.out.print(System.lineSeparator());
        for (int r = 0; r < jmlBaris; r++) {
            for (int c = 0; c < jmlKolom; c++) {
                mtx[r][c] = ambilInteger(pemindai, new StringBuilder().append(label).append("[").append(r).append("][").append(c).append("] = ").toString());
            }
        }
        return mtx;
    }

    public static void main(String[] args) {
        Scanner pemindai = new Scanner(System.in);
        MatrixMultiplication pengolah = new MatrixMultiplication();

        // Membaca ukuran matriks A dan B
        System.out.print("=== Ukuran Matriks A ===");
        System.out.print(System.lineSeparator());
        int nBarisP = ambilDimensi(pemindai, "Jumlah baris A : ");
        int nKolomP = ambilDimensi(pemindai, "Jumlah kolom A : ");

        System.out.print("=== Ukuran Matriks B ===");
        System.out.print(System.lineSeparator());
        int nBarisQ = ambilDimensi(pemindai, "Jumlah baris B : ");
        int nKolomQ = ambilDimensi(pemindai, "Jumlah kolom B : ");

        int[][] mtxP = isiData(pemindai, "A", nBarisP, nKolomP);
        int[][] mtxQ = isiData(pemindai, "B", nBarisQ, nKolomQ);

        int opsi;
        do {
            System.out.print("\n===== PROGRAM OPERASI MATRIKS =====");
            System.out.print(System.lineSeparator());
            System.out.print("1. Perkalian matriks (A x B)");
            System.out.print(System.lineSeparator());
            System.out.print("2. Perkalian dengan skalar");
            System.out.print(System.lineSeparator());
            System.out.print("3. Transpose matriks");
            System.out.print(System.lineSeparator());
            System.out.print("4. Tampilkan matriks A dan B");
            System.out.print(System.lineSeparator());
            System.out.print("0. Keluar");
            System.out.print(System.lineSeparator());
            opsi = ambilInteger(pemindai, "Pilih menu: ");

            switch (opsi) {
                case 1:
                    if (pengolah.layakKali(mtxP, mtxQ)) {
                        System.out.print(System.lineSeparator());
                        cetakMatriks("Hasil A x B:", pengolah.kalikanMtx(mtxP, mtxQ));
                    } else {
                        System.out.print(new StringBuilder().append("Tidak bisa dikalikan: kolom A (").append(nKolomP)
                                .append(") harus sama dengan baris B (").append(nBarisQ).append(")!").toString());
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 2:
                    int faktorSkalar = ambilInteger(pemindai, "Masukkan nilai skalar : ");
                    System.out.print(System.lineSeparator());
                    cetakMatriks("Skalar x A:", pengolah.skalarkan(mtxP, faktorSkalar));
                    cetakMatriks("Skalar x B:", pengolah.skalarkan(mtxQ, faktorSkalar));
                    break;
                case 3:
                    System.out.print(System.lineSeparator());
                    cetakMatriks("Transpose A:", pengolah.putar(mtxP));
                    cetakMatriks("Transpose B:", pengolah.putar(mtxQ));
                    break;
                case 4:
                    System.out.print(System.lineSeparator());
                    cetakMatriks("Matriks A:", mtxP);
                    cetakMatriks("Matriks B:", mtxQ);
                    break;
                case 0:
                    System.out.print("Terima kasih, program selesai.");
                    System.out.print(System.lineSeparator());
                    break;
                default:
                    System.out.print("Menu tidak tersedia!");
                    System.out.print(System.lineSeparator());
            }
        } while (opsi != 0);

        pemindai.close();
    }
}