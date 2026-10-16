/*
 * Program   : LinearSearch
 * Deskripsi : Mencari sebuah nilai di dalam array menggunakan algoritma
 *             Linear Search. Dilengkapi pencarian semua kemunculan,
 *             penghitungan jumlah langkah, input dari pengguna,
 *             dan menu sederhana.
 */
import java.util.Scanner;

public class LinearSearch {

    // Inner static class wrapper untuk koleksi data pencarian
    public static class DataSet {
        private int[] elements;

        public DataSet(int[] elements) {
            this.elements = elements;
        }

        public int size() {
            return elements.length;
        }

        public int get(int index) {
            return elements[index];
        }

        public int[] toArray() {
            return elements;
        }
    }

    // Inner static class wrapper untuk kueri pencarian nilai
    public static class SearchQuery {
        private int target;

        public SearchQuery(int target) {
            this.target = target;
        }

        public int getTarget() {
            return target;
        }

        public boolean matches(int value) {
            return this.target == value;
        }
    }

    // Logika inti: mencari target, mengembalikan indeks pertama atau -1
    public static int search(int[] arr, int target) {
        DataSet dataset = new DataSet(arr);
        SearchQuery query = new SearchQuery(target);
        for (int i = 0; i < dataset.size(); i++) {
            if (query.matches(dataset.get(i))) {
                return i;
            }
        }
        return -1;
    }

    // Menghitung berapa kali target muncul di dalam array
    public static int hitungKemunculan(int[] arr, int target) {
        DataSet dataset = new DataSet(arr);
        SearchQuery query = new SearchQuery(target);
        int jumlah = 0;
        for (int i = 0; i < dataset.size(); i++) {
            if (query.matches(dataset.get(i))) {
                jumlah++;
            }
        }
        return jumlah;
    }

    // Menampilkan semua indeks tempat target ditemukan
    public static void tampilkanSemuaIndeks(int[] arr, int target) {
        DataSet dataset = new DataSet(arr);
        SearchQuery query = new SearchQuery(target);
        boolean ditemukan = false;
        System.out.print("Indeks kemunculan " + query.getTarget() + " : ");
        for (int i = 0; i < dataset.size(); i++) {
            if (query.matches(dataset.get(i))) {
                System.out.print(i + " ");
                ditemukan = true;
            }
        }
        if (!ditemukan) {
            System.out.print("(tidak ada)");
        }
        System.out.println();
    }

    // Menghitung jumlah langkah pencarian sampai target ditemukan
    public static int hitungLangkah(int[] arr, int target) {
        DataSet dataset = new DataSet(arr);
        SearchQuery query = new SearchQuery(target);
        int langkah = 0;
        for (int i = 0; i < dataset.size(); i++) {
            langkah++;
            if (query.matches(dataset.get(i))) {
                break;
            }
        }
        return langkah;
    }

    // Menampilkan isi array ke layar
    public static void tampilkanArray(int[] arr) {
        DataSet dataset = new DataSet(arr);
        System.out.print("Isi array : [");
        for (int i = 0; i < dataset.size(); i++) {
            System.out.print(dataset.get(i));
            if (i < dataset.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
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

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Membaca jumlah data
        int jumlah = 0;
        while (jumlah <= 0) {
            jumlah = bacaAngka(input, "Masukkan jumlah data: ");
            if (jumlah <= 0) {
                System.out.println("Jumlah data harus lebih dari 0!");
            }
        }

        // Mengisi array
        int[] data = new int[jumlah];
        DataSet dataContainer = new DataSet(data);
        for (int i = 0; i < jumlah; i++) {
            dataContainer.toArray()[i] = bacaAngka(input, "Data ke-" + (i + 1) + " : ");
        }

        int pilihan = 0;
        do {
            System.out.println("\n===== PROGRAM LINEAR SEARCH =====");
            System.out.println("1. Cari satu nilai (indeks pertama)");
            System.out.println("2. Cari semua kemunculan");
            System.out.println("3. Tampilkan isi array");
            System.out.println("0. Keluar");
            pilihan = bacaAngka(input, "Pilih menu: ");

            switch (pilihan) {
                case 1:
                    int target1 = bacaAngka(input, "Nilai yang dicari: ");
                    int hasil = search(dataContainer.toArray(), target1);
                    if (hasil == -1) {
                        System.out.println("Nilai " + target1 + " tidak ditemukan.");
                    } else {
                        System.out.println("Nilai " + target1 + " ditemukan di indeks "
                                + hasil);
                    }
                    System.out.println("Jumlah langkah : " + hitungLangkah(dataContainer.toArray(), target1));
                    break;
                case 2:
                    int target2 = bacaAngka(input, "Nilai yang dicari: ");
                    System.out.println("Jumlah kemunculan : "
                            + hitungKemunculan(dataContainer.toArray(), target2));
                    tampilkanSemuaIndeks(dataContainer.toArray(), target2);
                    break;
                case 3:
                    tampilkanArray(dataContainer.toArray());
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