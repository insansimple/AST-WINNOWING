/*
 * Program   : MatrixAddition
 * Deskripsi : Menjumlahkan dua buah matriks berukuran sama. Dilengkapi
 *             pengurangan matriks, pengecekan ukuran, input dari pengguna,
 *             tampilan matriks yang rapi, dan menu sederhana.
 */
import java.util.Scanner;

public class MatrixAddition {

    // Inner static class wrapper untuk enkapsulasi struktur data matriks 2 dimensi
    public static class MatrixGrid {
        private int[][] data;
        private int rows;
        private int cols;

        public MatrixGrid(int[][] data) {
            this.data = data;
            this.rows = data.length;
            this.cols = data.length > 0 ? data[0].length : 0;
        }

        public MatrixGrid(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;
            this.data = new int[rows][cols];
        }

        public int getRows() {
            return rows;
        }

        public int getCols() {
            return cols;
        }

        public int get(int r, int c) {
            return data[r][c];
        }

        public void set(int r, int c, int val) {
            data[r][c] = val;
        }

        public int[][] rawGrid() {
            return data;
        }

        public boolean hasSameDimensions(MatrixGrid other) {
            return this.rows == other.rows && this.cols == other.cols;
        }
    }

    // Logika inti: menjumlahkan dua matriks yang ukurannya sama
    public int[][] add(int[][] a, int[][] b) {
        MatrixGrid gridA = new MatrixGrid(a);
        MatrixGrid gridB = new MatrixGrid(b);
        MatrixGrid result = new MatrixGrid(gridA.getRows(), gridA.getCols());

        for (int i = 0; i < gridA.getRows(); i++) {
            for (int j = 0; j < gridA.getCols(); j++) {
                result.set(i, j, gridA.get(i, j) + gridB.get(i, j));
            }
        }
        return result.rawGrid();
    }

    // Mengurangkan matriks a dengan matriks b
    public int[][] subtract(int[][] a, int[][] b) {
        MatrixGrid gridA = new MatrixGrid(a);
        MatrixGrid gridB = new MatrixGrid(b);
        MatrixGrid result = new MatrixGrid(gridA.getRows(), gridA.getCols());

        for (int i = 0; i < gridA.getRows(); i++) {
            for (int j = 0; j < gridA.getCols(); j++) {
                result.set(i, j, gridA.get(i, j) - gridB.get(i, j));
            }
        }
        return result.rawGrid();
    }

    // Mengecek apakah ukuran dua matriks sama
    public boolean ukuranSama(int[][] a, int[][] b) {
        MatrixGrid gridA = new MatrixGrid(a);
        MatrixGrid gridB = new MatrixGrid(b);
        return gridA.hasSameDimensions(gridB);
    }

    // Menampilkan matriks ke layar
    public static void tampilkanMatriks(String judul, int[][] m) {
        MatrixGrid grid = new MatrixGrid(m);
        System.out.println(judul);
        for (int i = 0; i < grid.getRows(); i++) {
            System.out.print("| ");
            for (int j = 0; j < grid.getCols(); j++) {
                System.out.print(grid.get(i, j) + "\t");
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
        MatrixGrid grid = new MatrixGrid(baris, kolom);
        System.out.println("\nIsi matriks " + nama + ":");
        for (int i = 0; i < baris; i++) {
            for (int j = 0; j < kolom; j++) {
                grid.set(i, j, bacaAngka(input, nama + "[" + i + "][" + j + "] = "));
            }
        }
        return grid.rawGrid();
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        MatrixAddition kalkulator = new MatrixAddition();

        // Membaca ukuran matriks (kedua matriks berukuran sama)
        int baris = bacaUkuran(input, "Masukkan jumlah baris : ");
        int kolom = bacaUkuran(input, "Masukkan jumlah kolom : ");

        int[][] matriksA = isiMatriks(input, "A", baris, kolom);
        int[][] matriksB = isiMatriks(input, "B", baris, kolom);

        int pilihan = 0;
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