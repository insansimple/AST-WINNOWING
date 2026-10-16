/*
 * Program   : MatrixMultiplication
 * Deskripsi : Mengalikan dua buah matriks. Dilengkapi pengecekan syarat
 *             perkalian (kolom A = baris B), perkalian dengan skalar,
 *             transpose matriks, input dari pengguna, tampilan matriks
 *             yang rapi, dan menu sederhana.
 */
import java.util.Scanner;

public class MatrixMultiplication {

    // Logika inti: mengalikan matriks mat1 (rb x cb) dengan mat2 (cb x ck)
    public int[][] kali(int[][] mat1, int[][] mat2) {
        int rb = mat1.length;
        int cb = mat1[0].length;
        int ck = mat2[0].length;
        int[][] luaran = new int[rb][ck];
        for (int r = 0; r < rb; r++) {
            for (int c = 0; c < ck; c++) {
                for (int x = 0; x < cb; x++) {
                    luaran[r][c] += mat1[r][x] * mat2[x][c];
                }
            }
        }
        return luaran;
    }

    // Mengecek syarat perkalian: jumlah kolom mat1 harus sama dengan baris mat2
    public boolean dapatDikali(int[][] mat1, int[][] mat2) {
        return mat1[0].length == mat2.length;
    }

    // Mengalikan setiap elemen matriks dengan sebuah bilangan (skalar)
    public int[][] kaliSkalar(int[][] mat, int pengali) {
        int[][] luaran = new int[mat.length][mat[0].length];
        for (int r = 0; r < mat.length; r++) {
            for (int c = 0; c < mat[0].length; c++) {
                luaran[r][c] = mat[r][c] * pengali;
            }
        }
        return luaran;
    }

    // Membuat transpose matriks (baris jadi kolom)
    public int[][] transpos(int[][] mat) {
        int[][] luaran = new int[mat[0].length][mat.length];
        for (int r = 0; r < mat.length; r++) {
            for (int c = 0; c < mat[0].length; c++) {
                luaran[c][r] = mat[r][c];
            }
        }
        return luaran;
    }

    // Menampilkan matriks ke layar
    public static void cetakMatriks(String label, int[][] mat) {
        System.out.println(label);
        for (int r = 0; r < mat.length; r++) {
            System.out.print("| ");
            for (int c = 0; c < mat[r].length; c++) {
                System.out.print(mat[r][c] + "\t");
            }
            System.out.println("|");
        }
    }

    // Membaca bilangan bulat dengan validasi sederhana
    public static int ambilAngka(Scanner scanner, String teks) {
        System.out.print(teks);
        while (!scanner.hasNextInt()) {
            System.out.print("Input harus angka bulat, ulangi: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    // Membaca ukuran (harus lebih dari 0)
    public static int ambilDimensi(Scanner scanner, String teks) {
        int val = 0;
        while (val <= 0) {
            val = ambilAngka(scanner, teks);
            if (val <= 0) {
                System.out.println("Ukuran harus lebih dari 0!");
            }
        }
        return val;
    }

    // Mengisi matriks dari input pengguna
    public static int[][] inputMatriks(Scanner scanner, String label, int baris, int kolom) {
        int[][] mat = new int[baris][kolom];
        System.out.println("\nIsi matriks " + label + ":");
        for (int r = 0; r < baris; r++) {
            for (int c = 0; c < kolom; c++) {
                mat[r][c] = ambilAngka(scanner, label + "[" + r + "][" + c + "] = ");
            }
        }
        return mat;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MatrixMultiplication ops = new MatrixMultiplication();

        // Membaca ukuran matriks A dan B
        System.out.println("=== Ukuran Matriks A ===");
        int bA = ambilDimensi(scanner, "Jumlah baris A : ");
        int kA = ambilDimensi(scanner, "Jumlah kolom A : ");

        System.out.println("=== Ukuran Matriks B ===");
        int bB = ambilDimensi(scanner, "Jumlah baris B : ");
        int kB = ambilDimensi(scanner, "Jumlah kolom B : ");

        int[][] mA = inputMatriks(scanner, "A", bA, kA);
        int[][] mB = inputMatriks(scanner, "B", bB, kB);

        int menu;
        do {
            System.out.println("\n===== PROGRAM OPERASI MATRIKS =====");
            System.out.println("1. Perkalian matriks (A x B)");
            System.out.println("2. Perkalian dengan skalar");
            System.out.println("3. Transpose matriks");
            System.out.println("4. Tampilkan matriks A dan B");
            System.out.println("0. Keluar");
            menu = ambilAngka(scanner, "Pilih menu: ");

            switch (menu) {
                case 1:
                    if (ops.dapatDikali(mA, mB)) {
                        System.out.println();
                        cetakMatriks("Hasil A x B:", ops.kali(mA, mB));
                    } else {
                        System.out.println("Tidak bisa dikalikan: kolom A (" + kA
                                + ") harus sama dengan baris B (" + bB + ")!");
                    }
                    break;
                case 2:
                    int pengali = ambilAngka(scanner, "Masukkan nilai skalar : ");
                    System.out.println();
                    cetakMatriks("Skalar x A:", ops.kaliSkalar(mA, pengali));
                    cetakMatriks("Skalar x B:", ops.kaliSkalar(mB, pengali));
                    break;
                case 3:
                    System.out.println();
                    cetakMatriks("Transpose A:", ops.transpos(mA));
                    cetakMatriks("Transpose B:", ops.transpos(mB));
                    break;
                case 4:
                    System.out.println();
                    cetakMatriks("Matriks A:", mA);
                    cetakMatriks("Matriks B:", mB);
                    break;
                case 0:
                    System.out.println("Terima kasih, program selesai.");
                    break;
                default:
                    System.out.println("Menu tidak tersedia!");
            }
        } while (menu != 0);

        scanner.close();
    }
}