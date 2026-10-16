/*
 * Program   : MatrixAddition
 * Deskripsi : Menjumlahkan dua buah matriks berukuran sama. Dilengkapi
 *             pengurangan matriks, pengecekan ukuran, input dari pengguna,
 *             tampilan matriks yang rapi, dan menu sederhana.
 */
import java.util.Scanner;

public class MatrixAddition {

    // Logika inti: menjumlahkan dua matriks yang ukurannya sama
    public int[][] jumlahkan(int[][] mx1, int[][] mx2) {
        int jmlBaris = mx1.length;
        int jmlKolom = mx1[0].length;
        int[][] keluaran = new int[jmlBaris][jmlKolom];
        for (int r = 0; r < jmlBaris; r++) {
            for (int c = 0; c < jmlKolom; c++) {
                keluaran[r][c] = mx1[r][c] + mx2[r][c];
            }
        }
        return keluaran;
    }

    // Mengurangkan matriks a dengan matriks b
    public int[][] kurangkan(int[][] mx1, int[][] mx2) {
        int jmlBaris = mx1.length;
        int jmlKolom = mx1[0].length;
        int[][] keluaran = new int[jmlBaris][jmlKolom];
        for (int r = 0; r < jmlBaris; r++) {
            for (int c = 0; c < jmlKolom; c++) {
                keluaran[r][c] = mx1[r][c] - mx2[r][c];
            }
        }
        return keluaran;
    }

    // Mengecek apakah ukuran dua matriks sama
    public boolean cekUkuran(int[][] mx1, int[][] mx2) {
        return mx1.length == mx2.length && mx1[0].length == mx2[0].length;
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
        MatrixAddition pengolah = new MatrixAddition();

        // Membaca ukuran matriks (kedua matriks berukuran sama)
        int nBaris = ambilDimensi(pemindai, "Masukkan jumlah baris : ");
        int nKolom = ambilDimensi(pemindai, "Masukkan jumlah kolom : ");

        int[][] mtxP = isiData(pemindai, "A", nBaris, nKolom);
        int[][] mtxQ = isiData(pemindai, "B", nBaris, nKolom);

        int opsi;
        do {
            System.out.print("\n===== PROGRAM OPERASI MATRIKS =====");
            System.out.print(System.lineSeparator());
            System.out.print("1. Penjumlahan (A + B)");
            System.out.print(System.lineSeparator());
            System.out.print("2. Pengurangan (A - B)");
            System.out.print(System.lineSeparator());
            System.out.print("3. Tampilkan matriks A dan B");
            System.out.print(System.lineSeparator());
            System.out.print("0. Keluar");
            System.out.print(System.lineSeparator());
            opsi = ambilInteger(pemindai, "Pilih menu: ");

            switch (opsi) {
                case 1:
                    if (pengolah.cekUkuran(mtxP, mtxQ)) {
                        System.out.print(System.lineSeparator());
                        cetakMatriks("Hasil A + B:", pengolah.jumlahkan(mtxP, mtxQ));
                    } else {
                        System.out.print("Ukuran matriks berbeda!");
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 2:
                    if (pengolah.cekUkuran(mtxP, mtxQ)) {
                        System.out.print(System.lineSeparator());
                        cetakMatriks("Hasil A - B:", pengolah.kurangkan(mtxP, mtxQ));
                    } else {
                        System.out.print("Ukuran matriks berbeda!");
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 3:
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