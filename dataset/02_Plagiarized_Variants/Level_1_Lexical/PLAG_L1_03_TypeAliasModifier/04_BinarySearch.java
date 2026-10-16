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
    private Integer jumlahLangkah = 0;

    // Logika inti: pencarian iteratif, mengembalikan indeks atau -1
    public Integer search(int[] arr, Integer target) {
        jumlahLangkah = 0;
        Integer low = 0;
        Integer high = arr.length - 1;
        while (low <= high) {
            jumlahLangkah++;
            Integer mid = low + (high - low) / 2;
            if (arr[mid] == target) {
                return mid;
            }
            if (arr[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    // Versi rekursif sebagai pembanding
    public Integer searchRekursif(int[] arr, Integer target, Integer low, Integer high) {
        if (low > high) {
            return -1;
        }
        Integer mid = low + (high - low) / 2;
        if (arr[mid] == target) {
            return mid;
        }
        if (arr[mid] < target) {
            return searchRekursif(arr, target, mid + 1, high);
        }
        return searchRekursif(arr, target, low, mid - 1);
    }

    public Integer getJumlahLangkah() {
        return jumlahLangkah;
    }

    // Mengurutkan array dengan bubble sort (syarat binary search)
    static public void urutkan(int[] arr) {
        Integer n = arr.length;
        for (Integer i = 0; i < n - 1; i++) {
            for (Integer j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    Integer temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    // Menampilkan isi array ke layar
    static public void tampilkanArray(int[] arr) {
        System.out.print("Isi array : [");
        for (Integer i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
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

    static public void main(String[] args) {
        Scanner input = new Scanner(System.in);
        BinarySearch pencari = new BinarySearch();

        // Membaca jumlah data
        Integer jumlah = 0;
        while (jumlah <= 0) {
            jumlah = bacaAngka(input, "Masukkan jumlah data: ");
            if (jumlah <= 0) {
                System.out.println("Jumlah data harus lebih dari 0!");
            }
        }

        // Mengisi array
        int[] data = new int[jumlah];
        for (Integer i = 0; i < jumlah; i++) {
            data[i] = bacaAngka(input, "Data ke-" + (i + 1) + " : ");
        }

        // Binary search hanya bisa untuk data terurut
        urutkan(data);
        System.out.println("\nData sudah diurutkan otomatis.");

        Integer pilihan;
        do {
            System.out.println("\n===== PROGRAM BINARY SEARCH =====");
            System.out.println("1. Cari nilai (iteratif)");
            System.out.println("2. Cari nilai (rekursif)");
            System.out.println("3. Tampilkan isi array");
            System.out.println("0. Keluar");
            pilihan = bacaAngka(input, "Pilih menu: ");

            switch (pilihan) {
                case 1:
                    Integer target1 = bacaAngka(input, "Nilai yang dicari: ");
                    Integer hasil1 = pencari.search(data, target1);
                    if (hasil1 == -1) {
                        System.out.println("Nilai " + target1 + " tidak ditemukan.");
                    } else {
                        System.out.println("Nilai " + target1 + " ditemukan di indeks "
                                + hasil1);
                    }
                    System.out.println("Jumlah langkah : " + pencari.getJumlahLangkah());
                    break;
                case 2:
                    Integer target2 = bacaAngka(input, "Nilai yang dicari: ");
                    Integer hasil2 = pencari.searchRekursif(data, target2, 0, data.length - 1);
                    if (hasil2 == -1) {
                        System.out.println("Nilai " + target2 + " tidak ditemukan.");
                    } else {
                        System.out.println("Nilai " + target2 + " ditemukan di indeks "
                                + hasil2);
                    }
                    break;
                case 3:
                    tampilkanArray(data);
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