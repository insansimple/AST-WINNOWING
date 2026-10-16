/*
 * Program   : MatrixMultiplication
 * Deskripsi : Mengalikan dua buah matriks. Dilengkapi pengecekan syarat
 *             perkalian (kolom A = baris B), perkalian dengan skalar,
 *             transpose matriks, input dari pengguna, tampilan matriks
 *             yang rapi, dan menu sederhana.
 */
import java.util.Scanner;

public class MatrixMultiplication {

    // Inner static class wrapper untuk enkapsulasi struktur data matriks
    public static class MatrixHolder {
        private int[][] grid;
        private int rows;
        private int cols;

        public MatrixHolder(int[][] grid) {
            this.grid = grid;
            this.rows = grid.length;
            this.cols = grid.length > 0 ? grid[0].length : 0;
        }

        public MatrixHolder(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;
            this.grid = new int[rows][cols];
        }

        public int getRows() {
            return rows;
        }

        public int getCols() {
            return cols;
        }

        public int get(int r, int c) {
            return grid[r][c];
        }

        public void set(int r, int c, int val) {
            grid[r][c] = val;
        }

        public void addValue(int r, int c, int val) {
            grid[r][c] += val;
        }

        public int[][] rawGrid() {
            return grid;
        }
    }

    // Logika inti: mengalikan matriks a (r1 x c1) dengan b (c1 x c2)
    public int[][] multiply(int[][] a, int[][] b) {
        MatrixHolder holderA = new MatrixHolder(a);
        MatrixHolder holderB = new MatrixHolder(b);
        int r1 = holderA.getRows();
        int c1 = holderA.getCols();
        int c2 = holderB.getCols();
        MatrixHolder product = new MatrixHolder(r1, c2);

        for (int i = 0; i < r1; i++) {
            for (int j = 0; j < c2; j++) {
                for (int k = 0; k < c1; k++) {
                    product.addValue(i, j, holderA.get(i, k) * holderB.get(k, j));
                }
            }
        }
        return product.rawGrid();
    }

    // Mengecek syarat perkalian: jumlah kolom A harus sama dengan baris B
    public boolean bisaDikalikan(int[][] a, int[][] b) {
        MatrixHolder holderA = new MatrixHolder(a);
        MatrixHolder holderB = new MatrixHolder(b);
        return holderA.getCols() == holderB.getRows();
    }

    // Mengalikan setiap elemen matriks dengan sebuah bilangan (skalar)
    public int[][] kaliSkalar(int[][] m, int skalar) {
        MatrixHolder holder = new MatrixHolder(m);
        MatrixHolder hasil = new MatrixHolder(holder.getRows(), holder.getCols());
        for (int i = 0; i < holder.getRows(); i++) {
            for (int j = 0; j < holder.getCols(); j++) {
                hasil.set(i, j, holder.get(i, j) * skalar);
            }
        }
        return hasil.rawGrid();
    }

    // Membuat transpose matriks (baris jadi kolom)
    public int[][] transpose(int[][] m) {
        MatrixHolder holder = new MatrixHolder(m);
        MatrixHolder hasil = new MatrixHolder(holder.getCols(), holder.getRows());
        for (int i = 0; i < holder.getRows(); i++) {
            for (int j = 0; j < holder.getCols(); j++) {
                hasil.set(j, i, holder.get(i, j));
            }
        }
        return hasil.rawGrid();
    }

    // Menampilkan matriks ke layar
    public static void tampilkanMatriks(String judul, int[][] m) {
        MatrixHolder holder = new MatrixHolder(m);
        System.out.println(judul);
        for (int i = 0; i < holder.getRows(); i++) {
            System.out.print("| ");
            for (int j = 0; j < holder.getCols(); j++) {
                System.out.print(holder.get(i, j) + "\t");
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
        MatrixHolder holder = new MatrixHolder(baris, kolom);
        System.out.println("\nIsi matriks " + nama + ":");
        for (int i = 0; i < baris; i++) {
            for (int j = 0; j < kolom; j++) {
                holder.set(i, j, bacaAngka(input, nama + "[" + i + "][" + j + "] = "));
            }
        }
        return holder.rawGrid();
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

        int pilihan = 0;
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