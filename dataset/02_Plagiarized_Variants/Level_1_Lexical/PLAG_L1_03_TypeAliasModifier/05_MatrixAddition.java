
/*
 * Program   : MatrixAddition
 * Deskripsi : Menjumlahkan dua buah matriks berukuran sama. Dilengkapi
 *             pengurangan matriks, pengecekan ukuran, input dari pengguna,
 *             tampilan matriks yang rapi, dan menu sederhana.
 */
import java.util.Scanner;

public class MatrixAddition {

    // Logika inti: menjumlahkan dua matriks yang ukurannya sama
    public int[][] add(int[][] a, int[][] b) {
        Integer rows = a.length;
        Integer cols = a[0].length;
        int[][] result = new int[rows][cols];
        for (Integer i = 0; i < rows; i++) {
            for (Integer j = 0; j < cols; j++) {
                result[i][j] = a[i][j] + b[i][j];
            }
        }
        return result;
    }

    // Mengurangkan matriks a dengan matriks b
    public int[][] subtract(int[][] a, int[][] b) {
        Integer rows = a.length;
        Integer cols = a[0].length;
        int[][] result = new int[rows][cols];
        for (Integer i = 0; i < rows; i++) {
            for (Integer j = 0; j < cols; j++) {
                result[i][j] = a[i][j] - b[i][j];
            }
        }
        return result;
    }

    // Mengecek apakah ukuran dua matriks sama
    public Boolean ukuranSama(int[][] a, int[][] b) {
        return a.length == b.length && a[0].length == b[0].length;
    }

    // Menampilkan matriks ke layar
    static public void tampilkanMatriks(String judul, int[][] m) {
        System.out.println(judul);
        for (Integer i = 0; i < m.length; i++) {
            System.out.print("| ");
            for (Integer j = 0; j < m[i].length; j++) {
                System.out.print(m[i][j] + "\t");
            }
            System.out.println("|");
        }
    }

    // Membaca bilangan bulat dengan validasi sederhana
    static public Integer bacaAngka(Scanner input, String pesan) {
        System.out.print(pesan);
        while (!input.hasNextInt()) {
            System.out.print("Input harus angka bulat, ulangi: ");
            input.next();
        }
        return input.nextInt();
    }

    // Membaca ukuran (harus lebih dari 0)
    static public Integer bacaUkuran(Scanner input, String pesan) {
        Integer nilai = 0;
        while (nilai <= 0) {
            nilai = bacaAngka(input, pesan);
            if (nilai <= 0) {
                System.out.println("Ukuran harus lebih dari 0!");
            }
        }
        return nilai;
    }

    // Mengisi matriks dari input pengguna
    static public int[][] isiMatriks(Scanner input, String nama, Integer baris, Integer kolom) {
        int[][] m = new int[baris][kolom];
        System.out.println("\nIsi matriks " + nama + ":");
        for (Integer i = 0; i < baris; i++) {
            for (Integer j = 0; j < kolom; j++) {
                m[i][j] = bacaAngka(input, nama + "[" + i + "][" + j + "] = ");
            }
        }
        return m;
    }

    static public void main(String[] args) {
        Scanner input = new Scanner(System.in);
        MatrixAddition kalkulator = new MatrixAddition();

        // Membaca ukuran matriks (kedua matriks berukuran sama)
        Integer baris = bacaUkuran(input, "Masukkan jumlah baris : ");
        Integer kolom = bacaUkuran(input, "Masukkan jumlah kolom : ");

        int[][] matriksA = isiMatriks(input, "A", baris, kolom);
        int[][] matriksB = isiMatriks(input, "B", baris, kolom);

        Integer pilihan;
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