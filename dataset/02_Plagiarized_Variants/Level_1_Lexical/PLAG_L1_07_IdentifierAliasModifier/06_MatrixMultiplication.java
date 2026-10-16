/*
 * Program   : MatrixMultiplication
 * Deskripsi : Mengalikan dua buah matriks. Dilengkapi pengecekan syarat
 *             perkalian (kolom A = baris B), perkalian dengan skalar,
 *             transpose matriks, input dari pengguna, tampilan matriks
 *             yang rapi, dan menu sederhana.
 * Modifikasi: Obfuscation Pointer & Variable Aliasing serta modifikasi
 *             penulisan increment/decrement pointer.
 */
import java.util.Scanner;

public class MatrixMultiplication {

    // Logika inti: mengalikan matriks dengan aliasing referensi dan penunjuk perkalian
    public int[][] multiply(int[][] a, int[][] b) {
        int[][] matLeft = a;  // Variable aliasing
        int[][] matRight = b; // Variable aliasing

        int r1 = matLeft.length;
        int c1 = matLeft[0].length;
        int c2 = matRight[0].length;
        int[][] multOutput = new int[r1][c2];

        int rowIdx = 0;
        while (rowIdx < r1) {
            int colIdx = 0;
            while (colIdx < c2) {
                int dotIdx = 0;
                while (dotIdx < c1) {
                    multOutput[rowIdx][colIdx] += matLeft[rowIdx][dotIdx] * matRight[dotIdx][colIdx];
                    dotIdx += 1; // modifikasi increment pointer
                }
                colIdx += 1; // modifikasi increment pointer
            }
            rowIdx += 1; // modifikasi increment pointer
        }
        return multOutput;
    }

    // Mengecek syarat perkalian: jumlah kolom A harus sama dengan baris B
    public boolean bisaDikalikan(int[][] a, int[][] b) {
        int[][] leftRef = a;
        int[][] rightRef = b;
        return leftRef[0].length == rightRef.length;
    }

    // Mengalikan setiap elemen matriks dengan skalar dengan aliasing buffer
    public int[][] kaliSkalar(int[][] m, int skalar) {
        int[][] sourceGrid = m; // Variable aliasing
        int[][] scaledResult = new int[sourceGrid.length][sourceGrid[0].length];

        int r = 0;
        while (r < sourceGrid.length) {
            int c = 0;
            while (c < sourceGrid[0].length) {
                scaledResult[r][c] = sourceGrid[r][c] * skalar;
                c += 1; // modifikasi increment pointer
            }
            r += 1; // modifikasi increment pointer
        }
        return scaledResult;
    }

    // Membuat transpose matriks dengan aliasing grid
    public int[][] transpose(int[][] m) {
        int[][] sourceGrid = m; // Variable aliasing
        int[][] transposedGrid = new int[sourceGrid[0].length][sourceGrid.length];

        int r = 0;
        while (r < sourceGrid.length) {
            int c = 0;
            while (c < sourceGrid[0].length) {
                transposedGrid[c][r] = sourceGrid[r][c];
                c += 1; // modifikasi increment pointer
            }
            r += 1; // modifikasi increment pointer
        }
        return transposedGrid;
    }

    // Menampilkan matriks ke layar dengan aliasing tampilan
    public static void tampilkanMatriks(String judul, int[][] m) {
        int[][] displayGrid = m; // Variable aliasing
        System.out.println(judul);

        int rPtr = 0;
        while (rPtr < displayGrid.length) {
            System.out.print("| ");
            int cPtr = 0;
            while (cPtr < displayGrid[rPtr].length) {
                System.out.print(displayGrid[rPtr][cPtr] + "\t");
                cPtr += 1; // modifikasi increment pointer
            }
            System.out.println("|");
            rPtr += 1; // modifikasi increment pointer
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
        int[][] entryMatrix = new int[baris][kolom];
        System.out.println("\nIsi matriks " + nama + ":");

        int r = 0;
        while (r < baris) {
            int c = 0;
            while (c < kolom) {
                entryMatrix[r][c] = bacaAngka(input, nama + "[" + r + "][" + c + "] = ");
                c += 1; // modifikasi increment pointer
            }
            r += 1; // modifikasi increment pointer
        }
        return entryMatrix;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        MatrixMultiplication kalkulator = new MatrixMultiplication();

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