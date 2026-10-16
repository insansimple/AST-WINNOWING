/*
 * Program   : MatrixAddition
 * Deskripsi : Menjumlahkan dua buah matriks berukuran sama. Dilengkapi
 *             pengurangan matriks, pengecekan ukuran, input dari pengguna,
 *             tampilan matriks yang rapi, dan menu sederhana.
 * Modifikasi: Obfuscation Pointer & Variable Aliasing serta modifikasi
 *             penulisan increment/decrement pointer.
 */
import java.util.Scanner;

public class MatrixAddition {

    // Logika inti: menjumlahkan dua matriks dengan aliasing array state 2D
    public int[][] add(int[][] a, int[][] b) {
        int[][] matA = a; // Variable aliasing
        int[][] matB = b; // Variable aliasing

        int rows = matA.length;
        int cols = matA[0].length;
        int[][] sumGrid = new int[rows][cols];

        int rowPtr = 0;
        while (rowPtr < rows) {
            int colPtr = 0;
            while (colPtr < cols) {
                sumGrid[rowPtr][colPtr] = matA[rowPtr][colPtr] + matB[rowPtr][colPtr];
                colPtr += 1; // modifikasi increment pointer
            }
            rowPtr += 1; // modifikasi increment pointer
        }
        return sumGrid;
    }

    // Mengurangkan matriks a dengan matriks b dengan aliasing array
    public int[][] subtract(int[][] a, int[][] b) {
        int[][] matA = a; // Variable aliasing
        int[][] matB = b; // Variable aliasing

        int rows = matA.length;
        int cols = matA[0].length;
        int[][] diffGrid = new int[rows][cols];

        int rowPtr = 0;
        while (rowPtr < rows) {
            int colPtr = 0;
            while (colPtr < cols) {
                diffGrid[rowPtr][colPtr] = matA[rowPtr][colPtr] - matB[rowPtr][colPtr];
                colPtr += 1; // modifikasi increment pointer
            }
            rowPtr += 1; // modifikasi increment pointer
        }
        return diffGrid;
    }

    // Mengecek apakah ukuran dua matriks sama
    public boolean ukuranSama(int[][] a, int[][] b) {
        int[][] refA = a;
        int[][] refB = b;
        return refA.length == refB.length && refA[0].length == refB[0].length;
    }

    // Menampilkan matriks ke layar dengan aliasing referensi
    public static void tampilkanMatriks(String judul, int[][] m) {
        int[][] gridRef = m; // Variable aliasing
        System.out.println(judul);

        int rPtr = 0;
        while (rPtr < gridRef.length) {
            System.out.print("| ");
            int cPtr = 0;
            while (cPtr < gridRef[rPtr].length) {
                System.out.print(gridRef[rPtr][cPtr] + "\t");
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

    // Mengisi matriks dari input pengguna dengan aliasing grid
    public static int[][] isiMatriks(Scanner input, String nama, int baris, int kolom) {
        int[][] storageGrid = new int[baris][kolom];
        System.out.println("\nIsi matriks " + nama + ":");

        int rIdx = 0;
        while (rIdx < baris) {
            int cIdx = 0;
            while (cIdx < kolom) {
                storageGrid[rIdx][cIdx] = bacaAngka(input, nama + "[" + rIdx + "][" + cIdx + "] = ");
                cIdx += 1; // modifikasi increment pointer
            }
            rIdx += 1; // modifikasi increment pointer
        }
        return storageGrid;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        MatrixAddition kalkulator = new MatrixAddition();

        // Membaca ukuran matriks (kedua matriks berukuran sama)
        int baris = bacaUkuran(input, "Masukkan jumlah baris : ");
        int kolom = bacaUkuran(input, "Masukkan jumlah kolom : ");

        int[][] matriksA = isiMatriks(input, "A", baris, kolom);
        int[][] matriksB = isiMatriks(input, "B", baris, kolom);

        int pilihan;
        do {
            System.out.println("\n===== PROGRAM OPERASI MATRIKS =====");
            System.out.println("1. Penjumlahan (A + B)");
            System.out.println("2. Pengurangan (A - B)");
            System.out.println("3. Tampilkan matriks A dan B");
            System.out.println("0. Keluar");
            pilihan = bacaAngka(input, "Pilih menu: ");

            switch (pilihan) {
                case 1:
                    if (kalkulator.ukuranSama(matriksA, matriksB)) {
                        System.out.println();
                        tampilkanMatriks("Hasil A + B:", kalkulator.add(matriksA, matriksB));
                    } else {
                        System.out.println("Ukuran matriks berbeda!");
                    }
                    break;
                case 2:
                    if (kalkulator.ukuranSama(matriksA, matriksB)) {
                        System.out.println();
                        tampilkanMatriks("Hasil A - B:", kalkulator.subtract(matriksA, matriksB));
                    } else {
                        System.out.println("Ukuran matriks berbeda!");
                    }
                    break;
                case 3:
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