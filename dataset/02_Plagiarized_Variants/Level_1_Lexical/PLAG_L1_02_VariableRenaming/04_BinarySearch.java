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
    private int cacahIterasi = 0;

    // Logika inti: pencarian iteratif, mengembalikan indeks atau -1
    public int cari(int[] larik, int kunci) {
        cacahIterasi = 0;
        int kiri = 0;
        int kanan = larik.length - 1;
        while (kiri <= kanan) {
            cacahIterasi++;
            int tengah = kiri + (kanan - kiri) / 2;
            if (larik[tengah] == kunci) {
                return tengah;
            }
            if (larik[tengah] < kunci) {
                kiri = tengah + 1;
            } else {
                kanan = tengah - 1;
            }
        }
        return -1;
    }

    // Versi rekursif sebagai pembanding
    public int cariRekursif(int[] larik, int kunci, int kiri, int kanan) {
        if (kiri > kanan) {
            return -1;
        }
        int tengah = kiri + (kanan - kiri) / 2;
        if (larik[tengah] == kunci) {
            return tengah;
        }
        if (larik[tengah] < kunci) {
            return cariRekursif(larik, kunci, tengah + 1, kanan);
        }
        return cariRekursif(larik, kunci, kiri, tengah - 1);
    }

    public int getCacahIterasi() {
        return cacahIterasi;
    }

    // Mengurutkan array dengan bubble sort (syarat binary search)
    public static void sortirArray(int[] larik) {
        int panjang = larik.length;
        for (int p = 0; p < panjang - 1; p++) {
            for (int q = 0; q < panjang - p - 1; q++) {
                if (larik[q] > larik[q + 1]) {
                    int tmp = larik[q];
                    larik[q] = larik[q + 1];
                    larik[q + 1] = tmp;
                }
            }
        }
    }

    // Menampilkan isi array ke layar
    public static void cetakArray(int[] larik) {
        System.out.print("Isi array : [");
        for (int pos = 0; pos < larik.length; pos++) {
            System.out.print(larik[pos]);
            if (pos < larik.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BinarySearch mesin = new BinarySearch();

        // Membaca jumlah data
        int banyak = 0;
        while (banyak <= 0) {
            banyak = ambilAngka(scanner, "Masukkan jumlah data: ");
            if (banyak <= 0) {
                System.out.println("Jumlah data harus lebih dari 0!");
            }
        }

        // Mengisi array
        int[] elemen = new int[banyak];
        for (int pos = 0; pos < banyak; pos++) {
            elemen[pos] = ambilAngka(scanner, "Data ke-" + (pos + 1) + " : ");
        }

        // Binary search hanya bisa untuk data terurut
        sortirArray(elemen);
        System.out.println("\nData sudah diurutkan otomatis.");

        int menu;
        do {
            System.out.println("\n===== PROGRAM BINARY SEARCH =====");
            System.out.println("1. Cari nilai (iteratif)");
            System.out.println("2. Cari nilai (rekursif)");
            System.out.println("3. Tampilkan isi array");
            System.out.println("0. Keluar");
            menu = ambilAngka(scanner, "Pilih menu: ");

            switch (menu) {
                case 1:
                    int kunci1 = ambilAngka(scanner, "Nilai yang dicari: ");
                    int ketemu1 = mesin.cari(elemen, kunci1);
                    if (ketemu1 == -1) {
                        System.out.println("Nilai " + kunci1 + " tidak ditemukan.");
                    } else {
                        System.out.println("Nilai " + kunci1 + " ditemukan di indeks "
                                + ketemu1);
                    }
                    System.out.println("Jumlah langkah : " + mesin.getCacahIterasi());
                    break;
                case 2:
                    int kunci2 = ambilAngka(scanner, "Nilai yang dicari: ");
                    int ketemu2 = mesin.cariRekursif(elemen, kunci2, 0, elemen.length - 1);
                    if (ketemu2 == -1) {
                        System.out.println("Nilai " + kunci2 + " tidak ditemukan.");
                    } else {
                        System.out.println("Nilai " + kunci2 + " ditemukan di indeks "
                                + ketemu2);
                    }
                    break;
                case 3:
                    cetakArray(elemen);
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