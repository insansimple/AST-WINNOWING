/*
 * Program   : LinearSearch
 * Deskripsi : Mencari sebuah nilai di dalam array menggunakan algoritma
 *             Linear Search. Dilengkapi pencarian semua kemunculan,
 *             penghitungan jumlah langkah, input dari pengguna,
 *             dan menu sederhana.
 */
import java.util.Scanner;

public class LinearSearch {

    // Logika inti: mencari kunci, mengembalikan indeks pertama atau -1
    public static int cari(int[] larik, int kunci) {
        for (int pos = 0; pos < larik.length; pos++) {
            if (larik[pos] == kunci) {
                return pos;
            }
        }
        return -1;
    }

    // Menghitung berapa kali kunci muncul di dalam array
    public static int hitungMuncul(int[] larik, int kunci) {
        int cacah = 0;
        for (int pos = 0; pos < larik.length; pos++) {
            if (larik[pos] == kunci) {
                cacah++;
            }
        }
        return cacah;
    }

    // Menampilkan semua indeks tempat kunci ditemukan
    public static void cetakSemuaIndeks(int[] larik, int kunci) {
        boolean ketemu = false;
        System.out.print("Indeks kemunculan " + kunci + " : ");
        for (int pos = 0; pos < larik.length; pos++) {
            if (larik[pos] == kunci) {
                System.out.print(pos + " ");
                ketemu = true;
            }
        }
        if (!ketemu) {
            System.out.print("(tidak ada)");
        }
        System.out.println();
    }

    // Menghitung jumlah langkah pencarian sampai kunci ditemukan
    public static int hitungIterasi(int[] larik, int kunci) {
        int iterasi = 0;
        for (int pos = 0; pos < larik.length; pos++) {
            iterasi++;
            if (larik[pos] == kunci) {
                break;
            }
        }
        return iterasi;
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

        int menu;
        do {
            System.out.println("\n===== PROGRAM LINEAR SEARCH =====");
            System.out.println("1. Cari satu nilai (indeks pertama)");
            System.out.println("2. Cari semua kemunculan");
            System.out.println("3. Tampilkan isi array");
            System.out.println("0. Keluar");
            menu = ambilAngka(scanner, "Pilih menu: ");

            switch (menu) {
                case 1:
                    int kunci1 = ambilAngka(scanner, "Nilai yang dicari: ");
                    int posisi = cari(elemen, kunci1);
                    if (posisi == -1) {
                        System.out.println("Nilai " + kunci1 + " tidak ditemukan.");
                    } else {
                        System.out.println("Nilai " + kunci1 + " ditemukan di indeks "
                                + posisi);
                    }
                    System.out.println("Jumlah langkah : " + hitungIterasi(elemen, kunci1));
                    break;
                case 2:
                    int kunci2 = ambilAngka(scanner, "Nilai yang dicari: ");
                    System.out.println("Jumlah kemunculan : "
                            + hitungMuncul(elemen, kunci2));
                    cetakSemuaIndeks(elemen, kunci2);
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