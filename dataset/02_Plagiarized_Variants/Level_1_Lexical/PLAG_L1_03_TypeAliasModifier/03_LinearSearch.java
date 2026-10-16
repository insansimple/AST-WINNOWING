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
    static public Integer search(int[] arr, Integer target) {
        for (Integer i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        return -1;
    }

    // Menghitung berapa kali target muncul di dalam array
    static public Integer hitungKemunculan(int[] arr, Integer target) {
        Integer jumlah = 0;
        for (Integer i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                jumlah++;
            }
        }
        return jumlah;
    }

    // Menampilkan semua indeks tempat target ditemukan
    static public void tampilkanSemuaIndeks(int[] arr, Integer target) {
        Boolean ditemukan = false;
        System.out.print("Indeks kemunculan " + target + " : ");
        for (Integer i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
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
    static public Integer hitungLangkah(int[] arr, Integer target) {
        Integer langkah = 0;
        for (Integer i = 0; i < arr.length; i++) {
            langkah++;
            if (arr[i] == target) {
                break;
            }
        }
        return langkah;
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

        Integer pilihan;
        do {
            System.out.println("\n===== PROGRAM LINEAR SEARCH =====");
            System.out.println("1. Cari satu nilai (indeks pertama)");
            System.out.println("2. Cari semua kemunculan");
            System.out.println("3. Tampilkan isi array");
            System.out.println("0. Keluar");
            pilihan = bacaAngka(input, "Pilih menu: ");

            switch (pilihan) {
                case 1:
                    Integer target1 = bacaAngka(input, "Nilai yang dicari: ");
                    Integer hasil = search(data, target1);
                    if (hasil == -1) {
                        System.out.println("Nilai " + target1 + " tidak ditemukan.");
                    } else {
                        System.out.println("Nilai " + target1 + " ditemukan di indeks "
                                + hasil);
                    }
                    System.out.println("Jumlah langkah : " + hitungLangkah(data, target1));
                    break;
                case 2:
                    Integer target2 = bacaAngka(input, "Nilai yang dicari: ");
                    System.out.println("Jumlah kemunculan : "
                            + hitungKemunculan(data, target2));
                    tampilkanSemuaIndeks(data, target2);
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