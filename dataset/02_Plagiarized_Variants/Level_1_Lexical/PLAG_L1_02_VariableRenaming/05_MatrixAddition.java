
/*
 * Program   : MatrixAddition
 * Deskripsi : Menjumlahkan dua buah matriks berukuran sama. Dilengkapi
 *             pengurangan matriks, pengecekan ukuran, input dari pengguna,
 *             tampilan matriks yang rapi, dan menu sederhana.
 */
import java.util.Scanner;

public class MatrixAddition {

    // Logika inti: menjumlahkan dua matriks yang ukurannya sama
    public int[][] tambah(int[][] mat1, int[][] mat2) {
        int baris = mat1.length;
        int kolom = mat1[0].length;
        int[][] output = new int[baris][kolom];
        for (int r = 0; r < baris; r++) {
            for (int c = 0; c < kolom; c++) {
                output[r][c] = mat1[r][c] + mat2[r][c];
            }
        }
        return output;
    }

    // Mengurangkan matriks mat1 dengan matriks mat2
    public int[][] kurangi(int[][] mat1, int[][] mat2) {
        int baris = mat1.length;
        int kolom = mat1[0].length;
        int[][] output = new int[baris][kolom];
        for (int r = 0; r < baris; r++) {
            for (int c = 0; c < kolom; c++) {
                output[r][c] = mat1[r][c] - mat2[r][c];
            }
        }
        return output;
    }

    // Mengecek apakah ukuran dua matriks sama
    public boolean dimensiSama(int[][] mat1, int[][] mat2) {
        return mat1.length == mat2.length && mat1[0].length == mat2[0].length;
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
        MatrixAddition ops = new MatrixAddition();

        // Membaca ukuran matriks (kedua matriks berukuran sama)
        int jmlBaris = ambilDimensi(scanner, "Masukkan jumlah baris : ");
        int jmlKolom = ambilDimensi(scanner, "Masukkan jumlah kolom : ");

        int[][] mA = inputMatriks(scanner, "A", jmlBaris, jmlKolom);
        int[][] mB = inputMatriks(scanner, "B", jmlBaris, jmlKolom);

        int menu;
        do {
            System.out.println("\n===== PROGRAM OPERASI MATRIKS =====");
            System.out.println("1. Penjumlahan (A + B)");
            System.out.println("2. Pengurangan (A - B)");
            System.out.println("3. Tampilkan matriks A dan B");
            System.out.println("0. Keluar");
            menu = ambilAngka(scanner, "Pilih menu: ");

            switch (menu) {
                case 1:
                    if (ops.dimensiSama(mA, mB)) {
                        System.out.println();
                        cetakMatriks("Hasil A + B:", ops.tambah(mA, mB));
                    } else {
                        System.out.println("Ukuran matriks berbeda!");
                    }
                    break;
                case 2:
                    if (ops.dimensiSama(mA, mB)) {
                        System.out.println();
                        cetakMatriks("Hasil A - B:", ops.kurangi(mA, mB));
                    } else {
                        System.out.println("Ukuran matriks berbeda!");
                    }
                    break;
                case 3:
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