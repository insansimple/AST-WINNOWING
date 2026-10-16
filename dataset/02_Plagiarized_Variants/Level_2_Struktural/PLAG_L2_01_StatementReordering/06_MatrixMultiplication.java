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
    public int[][] multiply(int[][] a, int[][] b) {
        int c2 = b[0].length;
        int c1 = a[0].length;
        int r1 = a.length;
        int[][] product = new int[r1][c2];
        for (int i = 0; i < r1; i++) {
            for (int j = 0; j < c2; j++) {
                for (int k = 0; k < c1; k++) {
                    product[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return product;
    }

    // Mengecek syarat perkalian: jumlah kolom A harus sama dengan baris B
    public boolean bisaDikalikan(int[][] a, int[][] b) {
        return a[0].length == b.length;
    }

    // Mengalikan setiap elemen matriks dengan sebuah bilangan (skalar)
    public int[][] kaliSkalar(int[][] m, int skalar) {
        int[][] hasil = new int[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                hasil[i][j] = m[i][j] * skalar;
            }
        }
        return hasil;
    }

    // Membuat transpose matriks (baris jadi kolom)
    public int[][] transpose(int[][] m) {
        int[][] hasil = new int[m[0].length][m.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                hasil[j][i] = m[i][j];
            }
        }
        return hasil;
    }

    // Menampilkan matriks ke layar
    public static void tampilkanMatriks(String judul, int[][] m) {
        System.out.println(judul);
        for (int i = 0; i < m.length; i++) {
            System.out.print("| ");
            for (int j = 0; j < m[i].length; j++) {
                System.out.print(m[i][j] + "\t");
            }
            System.out.println("|");
        }
    }

    // Membaca bilangan bulat dengan validasi sederhana
    public static int bacaAngka(Scanner input, String pesan) {
        System.out.print(pesan);
        while (!input.hasNextInt()) {
            System.out.print("Input harus angka bulat, ulangi: ");
            input.next();
        }
        return input.nextInt();
    }

    // Membaca ukuran (harus lebih dari 0)
    public static int bacaUkuran(Scanner input, String pesan) {
        int nilai = 0;
        while (nilai <= 0) {
            nilai = bacaAngka(input, pesan);
            if (nilai <= 0) {
                System.out.println("Ukuran harus lebih dari 0!");
            }
        }
        return nilai;
    }

    // Mengisi matriks dari input pengguna
    public static int[][] isiMatriks(Scanner input, String nama, int baris, int kolom) {
        int[][] m = new int[baris][kolom];
        System.out.println("\nIsi matriks " + nama + ":");
        for (int i = 0; i < baris; i++) {
            for (int j = 0; j < kolom; j++) {
                m[i][j] = bacaAngka(input, nama + "[" + i + "][" + j + "] = ");
            }
        }
        return m;
    }

    public static void main(String[] args) {
        MatrixMultiplication kalkulator = new MatrixMultiplication();
        Scanner input = new Scanner(System.in);

        // Membaca ukuran matriks A dan B
        System.out.println("=== Ukuran Matriks A ===");
        int barisA = bacaUkuran(input, "Jumlah baris A : ");
        int kolomA = bacaUkuran(input, "Jumlah kolom A : ");

        System.out.println("=== Ukuran Matriks B ===");
        int barisB = bacaUkuran(input, "Jumlah baris B : ");
        int kolomB = bacaUkuran(input, "Jumlah kolom B : ");

        int[][] matriksA = isiMatriks(input, "A", barisA, kolomA);
        int[][] matriksB = isiMatriks(input, "B", barisB, kolomB);

        int pilihan;
        do {
            System.out.println("\n===== PROGRAM OPERASI MATRIKS =====");
            System.out.println("1. Perkalian matriks (A x B)");
            System.out.println("2. Perkalian dengan skalar");
            System.out.println("3. Transpose matriks");
            System.out.println("4. Tampilkan matriks A dan B");
            System.out.println("0. Keluar");
            pilihan = bacaAngka(input, "Pilih menu: ");

            switch (pilihan) {
                case 1:
                    if (kalkulator.bisaDikalikan(matriksA, matriksB)) {
                        System.out.println();
                        tampilkanMatriks("Hasil A x B:", kalkulator.multiply(matriksA, matriksB));
                    } else {
                        System.out.println("Tidak bisa dikalikan: kolom A (" + kolomA
                                + ") harus sama dengan baris B (" + barisB + ")!");
                    }
                    break;
                case 2:
                    int skalar = bacaAngka(input, "Masukkan nilai skalar : ");
                    System.out.println();
                    tampilkanMatriks("Skalar x A:", kalkulator.kaliSkalar(matriksA, skalar));
                    tampilkanMatriks("Skalar x B:", kalkulator.kaliSkalar(matriksB, skalar));
                    break;
                case 3:
                    System.out.println();
                    tampilkanMatriks("Transpose A:", kalkulator.transpose(matriksA));
                    tampilkanMatriks("Transpose B:", kalkulator.transpose(matriksB));
                    break;
                case 4:
                    System.out.println();
                    tampilkanMatriks("Matriks A:", matriksA);
                    tampilkanMatriks("Matriks B:", matriksB);
                    break;
                case 0:
                    System.out.println("Terima kasih, program selesai.");
                    break;
                default:
                    System.out.println("Menu tidak tersedia!");
            }
        } while (pilihan != 0);

        input.close();
    }
}