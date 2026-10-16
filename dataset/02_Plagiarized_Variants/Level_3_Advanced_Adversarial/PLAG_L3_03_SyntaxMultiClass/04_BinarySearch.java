/*
 * Program   : BinarySearch
 * Deskripsi : Mencari sebuah nilai di dalam array yang sudah terurut
 *             menggunakan algoritma Binary Search. Dilengkapi versi
 *             rekursif, penghitungan jumlah langkah, pengurutan data
 *             sebelum pencarian, input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner;

public class BinarySearch {

    // Inner static class wrapper untuk rentang interval pencarian
    public static class SearchInterval {
        private int low;
        private int high;

        public SearchInterval(int low, int high) {
            this.low = low;
            this.high = high;
        }

        public int getLow() {
            return low;
        }

        public int getHigh() {
            return high;
        }

        public void setLow(int low) {
            this.low = low;
        }

        public void setHigh(int high) {
            this.high = high;
        }

        public boolean isValid() {
            return low <= high;
        }

        public int calculateMid() {
            return low + (high - low) / 2;
        }
    }

    // Inner static class wrapper untuk koleksi data array terurut
    public static class SortedArray {
        private int[] elements;

        public SortedArray(int[] elements) {
            this.elements = elements;
        }

        public int size() {
            return elements.length;
        }

        public int get(int index) {
            return elements[index];
        }

        public void set(int index, int val) {
            elements[index] = val;
        }

        public int[] rawArray() {
            return elements;
        }
    }

    // Menyimpan jumlah langkah pencarian terakhir (untuk analisis)
    private int jumlahLangkah = 0;

    // Logika inti: pencarian iteratif, mengembalikan indeks atau -1
    public int search(int[] arr, int target) {
        jumlahLangkah = 0;
        SortedArray array = new SortedArray(arr);
        SearchInterval interval = new SearchInterval(0, array.size() - 1);

        while (interval.isValid()) {
            jumlahLangkah++;
            int mid = interval.calculateMid();
            if (array.get(mid) == target) {
                return mid;
            }
            if (array.get(mid) < target) {
                interval.setLow(mid + 1);
            } else {
                interval.setHigh(mid - 1);
            }
        }
        return -1;
    }

    // Versi rekursif sebagai pembanding
    public int searchRekursif(int[] arr, int target, int low, int high) {
        SearchInterval interval = new SearchInterval(low, high);
        if (!interval.isValid()) {
            return -1;
        }
        SortedArray array = new SortedArray(arr);
        int mid = interval.calculateMid();
        if (array.get(mid) == target) {
            return mid;
        }
        if (array.get(mid) < target) {
            return searchRekursif(arr, target, mid + 1, interval.getHigh());
        }
        return searchRekursif(arr, target, interval.getLow(), mid - 1);
    }

    public int getJumlahLangkah() {
        return jumlahLangkah;
    }

    // Mengurutkan array dengan bubble sort (syarat binary search)
    public static void urutkan(int[] arr) {
        SortedArray array = new SortedArray(arr);
        int n = array.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (array.get(j) > array.get(j + 1)) {
                    int temp = array.get(j);
                    array.set(j, array.get(j + 1));
                    array.set(j + 1, temp);
                }
            }
        }
    }

    // Menampilkan isi array ke layar
    public static void tampilkanArray(int[] arr) {
        SortedArray array = new SortedArray(arr);
        System.out.print("Isi array : [");
        for (int i = 0; i < array.size(); i++) {
            System.out.print(array.get(i));
            if (i < array.size() - 1) {
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
        BinarySearch pencari = new BinarySearch();

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
        SortedArray dataHolder = new SortedArray(data);
        for (int i = 0; i < jumlah; i++) {
            dataHolder.set(i, bacaAngka(input, "Data ke-" + (i + 1) + " : "));
        }

        // Binary search hanya bisa untuk data terurut
        urutkan(dataHolder.rawArray());
        System.out.println("\nData sudah diurutkan otomatis.");

        int pilihan = 0;
        do {
            System.out.println("\n===== PROGRAM BINARY SEARCH =====");
            System.out.println("1. Cari nilai (iteratif)");
            System.out.println("2. Cari nilai (rekursif)");
            System.out.println("3. Tampilkan isi array");
            System.out.println("0. Keluar");
            pilihan = bacaAngka(input, "Pilih menu: ");

            switch (pilihan) {
                case 1:
                    int target1 = bacaAngka(input, "Nilai yang dicari: ");
                    int hasil1 = pencari.search(dataHolder.rawArray(), target1);
                    if (hasil1 == -1) {
                        System.out.println("Nilai " + target1 + " tidak ditemukan.");
                    } else {
                        System.out.println("Nilai " + target1 + " ditemukan di indeks "
                                + hasil1);
                    }
                    System.out.println("Jumlah langkah : " + pencari.getJumlahLangkah());
                    break;
                case 2:
                    int target2 = bacaAngka(input, "Nilai yang dicari: ");
                    int hasil2 = pencari.searchRekursif(dataHolder.rawArray(), target2, 0, dataHolder.size() - 1);
                    if (hasil2 == -1) {
                        System.out.println("Nilai " + target2 + " tidak ditemukan.");
                    } else {
                        System.out.println("Nilai " + target2 + " ditemukan di indeks "
                                + hasil2);
                    }
                    break;
                case 3:
                    tampilkanArray(dataHolder.rawArray());
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