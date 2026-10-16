/*
 * Program   : LinearSearch
 * Deskripsi : Mencari sebuah nilai di dalam array menggunakan algoritma
 *             Linear Search. Dilengkapi pencarian semua kemunculan,
 *             penghitungan jumlah langkah, input dari pengguna,
 *             dan menu sederhana.
 */
import java.util.Scanner;

public class LinearSearch {

    // Logika inti: mencari target, mengembalikan indeks pertama atau -1
    public static int cariPertama(int[] larik, int sasaran) {
        for (int k = 0; k < larik.length; k++) {
            if (larik[k] == sasaran) {
                return k;
            }
        }
        return -1;
    }

    // Menghitung berapa kali target muncul di dalam array
    public static int cacahMuncul(int[] larik, int sasaran) {
        int total = 0;
        for (int k = 0; k < larik.length; k++) {
            if (larik[k] == sasaran) {
                total++;
            }
        }
        return total;
    }

    // Menampilkan semua indeks tempat target ditemukan
    public static void cetakIndeksSemua(int[] larik, int sasaran) {
        boolean adaHasil = false;
        System.out.print(new StringBuilder().append("Indeks kemunculan ").append(sasaran).append(" : ").toString());
        for (int k = 0; k < larik.length; k++) {
            if (larik[k] == sasaran) {
                System.out.print(new StringBuilder().append(k).append(" ").toString());
                adaHasil = true;
            }
        }
        if (!adaHasil) {
            System.out.print("(tidak ada)");
        }
        System.out.print(System.lineSeparator());
    }

    // Menghitung jumlah langkah pencarian sampai target ditemukan
    public static int cacahLangkah(int[] larik, int sasaran) {
        int tahap = 0;
        for (int k = 0; k < larik.length; k++) {
            tahap++;
            if (larik[k] == sasaran) {
                break;
            }
        }
        return tahap;
    }

    // Menampilkan isi array ke layar
    public static void cetakIsi(int[] larik) {
        System.out.print("Isi array : [");
        for (int k = 0; k < larik.length; k++) {
            System.out.print(larik[k]);
            if (k < larik.length - 1) {
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
        for (int k = 0; k < banyak; k++) {
            kumpulan[k] = ambilInteger(pemindai, new StringBuilder().append("Data ke-").append(k + 1).append(" : ").toString());
        }

        int opsi;
        do {
            System.out.print("\n===== PROGRAM LINEAR SEARCH =====");
            System.out.print(System.lineSeparator());
            System.out.print("1. Cari satu nilai (indeks pertama)");
            System.out.print(System.lineSeparator());
            System.out.print("2. Cari semua kemunculan");
            System.out.print(System.lineSeparator());
            System.out.print("3. Tampilkan isi array");
            System.out.print(System.lineSeparator());
            System.out.print("0. Keluar");
            System.out.print(System.lineSeparator());
            opsi = ambilInteger(pemindai, "Pilih menu: ");

            switch (opsi) {
                case 1:
                    int sasaran1 = ambilInteger(pemindai, "Nilai yang dicari: ");
                    int temuan = cariPertama(kumpulan, sasaran1);
                    if (temuan == -1) {
                        System.out.print(new StringBuilder().append("Nilai ").append(sasaran1).append(" tidak ditemukan.").toString());
                        System.out.print(System.lineSeparator());
                    } else {
                        System.out.print(new StringBuilder().append("Nilai ").append(sasaran1).append(" ditemukan di indeks ")
                                .append(temuan).toString());
                        System.out.print(System.lineSeparator());
                    }
                    System.out.print(new StringBuilder().append("Jumlah langkah : ").append(cacahLangkah(kumpulan, sasaran1)).toString());
                    System.out.print(System.lineSeparator());
                    break;
                case 2:
                    int sasaran2 = ambilInteger(pemindai, "Nilai yang dicari: ");
                    System.out.print(new StringBuilder().append("Jumlah kemunculan : ")
                            .append(cacahMuncul(kumpulan, sasaran2)).toString());
                    System.out.print(System.lineSeparator());
                    cetakIndeksSemua(kumpulan, sasaran2);
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