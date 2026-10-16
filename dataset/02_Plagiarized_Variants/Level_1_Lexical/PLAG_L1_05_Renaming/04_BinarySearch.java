/*
 * Program   : BinarySearch
 * Deskripsi : Mencari sebuah nilai di dalam array yang sudah terurut
 *             menggunakan algoritma Binary Search. Dilengkapi versi
 *             rekursif, penghitungan jumlah langkah, pengurutan data
 *             sebelum pencarian, input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner;

public class BinarySearch {

    // Menyimpan jumlah langkah pencarian terakhir (untuk analisis)
    private int totalTahap = 0;

    // Logika inti: pencarian iteratif, mengembalikan indeks atau -1
    public int telusuriIter(int[] larik, int sasaran) {
        totalTahap = 0;
        int kiri = 0;
        int kanan = larik.length - 1;
        while (kiri <= kanan) {
            totalTahap++;
            int tengah = kiri + (kanan - kiri) / 2;
            if (larik[tengah] == sasaran) {
                return tengah;
            }
            if (larik[tengah] < sasaran) {
                kiri = tengah + 1;
            } else {
                kanan = tengah - 1;
            }
        }
        return -1;
    }

    // Versi rekursif sebagai pembanding
    public int telusuriRek(int[] larik, int sasaran, int kiri, int kanan) {
        if (kiri > kanan) {
            return -1;
        }
        int tengah = kiri + (kanan - kiri) / 2;
        if (larik[tengah] == sasaran) {
            return tengah;
        }
        if (larik[tengah] < sasaran) {
            return telusuriRek(larik, sasaran, tengah + 1, kanan);
        }
        return telusuriRek(larik, sasaran, kiri, tengah - 1);
    }

    public int ambilTahap() {
        return totalTahap;
    }

    // Mengurutkan array dengan bubble sort (syarat binary search)
    public static void aturUrutan(int[] larik) {
        int panjang = larik.length;
        for (int p = 0; p < panjang - 1; p++) {
            for (int q = 0; q < panjang - p - 1; q++) {
                if (larik[q] > larik[q + 1]) {
                    int sementara = larik[q];
                    larik[q] = larik[q + 1];
                    larik[q + 1] = sementara;
                }
            }
        }
    }

    // Menampilkan isi array ke layar
    public static void cetakIsi(int[] larik) {
        System.out.print("Isi array : [");
        for (int p = 0; p < larik.length; p++) {
            System.out.print(larik[p]);
            if (p < larik.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.print("]");
        System.out.print(System.lineSeparator());
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

    public static void main(String[] args) {
        Scanner pemindai = new Scanner(System.in);
        BinarySearch penelusur = new BinarySearch();

        // Membaca jumlah data
        int banyak = 0;
        while (banyak <= 0) {
            banyak = ambilInteger(pemindai, "Masukkan jumlah data: ");
            if (banyak <= 0) {
                System.out.print("Jumlah data harus lebih dari 0!");
                System.out.print(System.lineSeparator());
            }
        }

        // Mengisi array
        int[] kumpulan = new int[banyak];
        for (int p = 0; p < banyak; p++) {
            kumpulan[p] = ambilInteger(pemindai, new StringBuilder().append("Data ke-").append(p + 1).append(" : ").toString());
        }

        // Binary search hanya bisa untuk data terurut
        aturUrutan(kumpulan);
        System.out.print("\nData sudah diurutkan otomatis.");
        System.out.print(System.lineSeparator());

        int opsi;
        do {
            System.out.print("\n===== PROGRAM BINARY SEARCH =====");
            System.out.print(System.lineSeparator());
            System.out.print("1. Cari nilai (iteratif)");
            System.out.print(System.lineSeparator());
            System.out.print("2. Cari nilai (rekursif)");
            System.out.print(System.lineSeparator());
            System.out.print("3. Tampilkan isi array");
            System.out.print(System.lineSeparator());
            System.out.print("0. Keluar");
            System.out.print(System.lineSeparator());
            opsi = ambilInteger(pemindai, "Pilih menu: ");

            switch (opsi) {
                case 1:
                    int sasaran1 = ambilInteger(pemindai, "Nilai yang dicari: ");
                    int temuan1 = penelusur.telusuriIter(kumpulan, sasaran1);
                    if (temuan1 == -1) {
                        System.out.print(new StringBuilder().append("Nilai ").append(sasaran1).append(" tidak ditemukan.").toString());
                        System.out.print(System.lineSeparator());
                    } else {
                        System.out.print(new StringBuilder().append("Nilai ").append(sasaran1).append(" ditemukan di indeks ")
                                .append(temuan1).toString());
                        System.out.print(System.lineSeparator());
                    }
                    System.out.print(new StringBuilder().append("Jumlah langkah : ").append(penelusur.ambilTahap()).toString());
                    System.out.print(System.lineSeparator());
                    break;
                case 2:
                    int sasaran2 = ambilInteger(pemindai, "Nilai yang dicari: ");
                    int temuan2 = penelusur.telusuriRek(kumpulan, sasaran2, 0, kumpulan.length - 1);
                    if (temuan2 == -1) {
                        System.out.print(new StringBuilder().append("Nilai ").append(sasaran2).append(" tidak ditemukan.").toString());
                        System.out.print(System.lineSeparator());
                    } else {
                        System.out.print(new StringBuilder().append("Nilai ").append(sasaran2).append(" ditemukan di indeks ")
                                .append(temuan2).toString());
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 3:
                    cetakIsi(kumpulan);
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