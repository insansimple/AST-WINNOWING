/*
 * Program   : MatrixTranspose
 * Deskripsi : Operasi transposisi pada matriks M x N. Baris pada matriks
 *             asli menjadi kolom pada matriks hasil, sehingga ukuran
 *             hasil menjadi N x M. Elemen A[i][j] menjadi T[j][i].
 */
import java.util.Scanner;

public class MatrixTranspose {

    // Menampilkan matriks dalam bentuk tabel
    public static void tampilkanMatriks(int[][] matriks) {
        for (int i = 0; i < matriks.length; i++) {
            for (int j = 0; j < matriks[i].length; j++) {
                System.out.printf("%5d", matriks[i][j]);
            }
            System.out.println();
        }
    }

    // Membuat matriks transpose: hasil[j][i] = asli[i][j]
    public static int[][] transpose(int[][] asli) {
        int baris = asli.length;
        int kolom = asli[0].length;

        // ukuran hasil dibalik: kolom x baris
        int[][] hasil = new int[kolom][baris];

        for (int i = 0; i < baris; i++) {
            for (int j = 0; j < kolom; j++) {
                hasil[j][i] = asli[i][j];
            }
        }
        return hasil;
    }

    // Mengisi matriks dari input pengguna
    public static int[][] isiMatriks(Scanner input, int baris, int kolom) {
        int[][] matriks = new int[baris][kolom];
        for (int i = 0; i < baris; i++) {
            for (int j = 0; j < kolom; j++) {
                System.out.print("Elemen [" + (i + 1) + "][" + (j + 1) + "] : ");
                matriks[i][j] = input.nextInt();
            }
        }
        return matriks;
    }

    // Mengisi matriks otomatis dengan angka berurutan (untuk contoh)
    public static int[][] isiOtomatis(int baris, int kolom) {
        int[][] matriks = new int[baris][kolom];
        int angka = 1;
        for (int i = 0; i < baris; i++) {
            for (int j = 0; j < kolom; j++) {
                matriks[i][j] = angka;
                angka++;
            }
        }
        return matriks;
    }

    // Memeriksa apakah matriks simetris (A sama dengan transpose-nya)
    public static boolean isSimetris(int[][] matriks) {
        // hanya matriks persegi yang bisa simetris
        if (matriks.length != matriks[0].length) {
            return false;
        }
        for (int i = 0; i < matriks.length; i++) {
            for (int j = 0; j < matriks[i].length; j++) {
                if (matriks[i][j] != matriks[j][i]) {
                    return false;
                }
            }
        }
        return true;
    }

    // Menampilkan matriks asli, hasil transpose, dan keterangan ukuran
    public static void prosesDanTampilkan(int[][] matriks) {
        int[][] hasil = transpose(matriks);

        System.out.println("\nMatriks asli (" + matriks.length + " x " + matriks[0].length + "):");
        tampilkanMatriks(matriks);

        System.out.println("\nMatriks transpose (" + hasil.length + " x " + hasil[0].length + "):");
        tampilkanMatriks(hasil);

        if (isSimetris(matriks)) {
            System.out.println("\nKeterangan: matriks ini simetris.");
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Contoh pemakaian dengan data otomatis
        System.out.println("=== CONTOH 1: Matriks 2 x 3 ===");
        prosesDanTampilkan(isiOtomatis(2, 3));

        System.out.println("\n=== CONTOH 2: Matriks 3 x 3 ===");
        prosesDanTampilkan(isiOtomatis(3, 3));

        // Input dari pengguna
        System.out.println("\n=== INPUT MANUAL ===");
        int m;
        int n;

        do {
            System.out.print("Masukkan jumlah baris (M)  : ");
            m = input.nextInt();
            System.out.print("Masukkan jumlah kolom (N)  : ");
            n = input.nextInt();
            if (m < 1 || n < 1) {
                System.out.println("Ukuran harus minimal 1, coba lagi!\n");
            }
        } while (m < 1 || n < 1);

        int[][] matriksUser = isiMatriks(input, m, n);
        prosesDanTampilkan(matriksUser);

        System.out.println("\nProgram selesai.");
        input.close();
    }
}