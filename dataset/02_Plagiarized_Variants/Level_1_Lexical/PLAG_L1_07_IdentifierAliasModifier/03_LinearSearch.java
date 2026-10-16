/*
 * Program   : LinearSearch
 * Deskripsi : Mencari sebuah nilai di dalam array menggunakan algoritma
 *             Linear Search. Dilengkapi pencarian semua kemunculan,
 *             penghitungan jumlah langkah, input dari pengguna,
 *             dan menu sederhana.
 * Modifikasi: Obfuscation Pointer & Variable Aliasing serta modifikasi
 *             penulisan increment/decrement pointer.
 */
import java.util.Scanner;

public class LinearSearch {

    // Logika inti: mencari target dengan aliasing array state dan modifikasi pointer
    public static int search(int[] arr, int target) {
        int[] searchBuffer = arr; // Variable aliasing
        int indexPointer = 0;

        while (indexPointer < searchBuffer.length) {
            if (searchBuffer[indexPointer] == target) {
                return indexPointer;
            }
            indexPointer += 1; // modifikasi increment pointer
        }
        return -1;
    }

    // Menghitung berapa kali target muncul di dalam array dengan aliasing
    public static int hitungKemunculan(int[] arr, int target) {
        int[] poolRef = arr; // Variable aliasing
        int matchCount = 0;
        int cursorPtr = 0;

        while (cursorPtr < poolRef.length) {
            if (poolRef[cursorPtr] == target) {
                matchCount += 1; // modifikasi increment
            }
            cursorPtr += 1; // modifikasi increment pointer
        }
        return matchCount;
    }

    // Menampilkan semua indeks tempat target ditemukan
    public static void tampilkanSemuaIndeks(int[] arr, int target) {
        int[] arrayRef = arr; // Variable aliasing
        boolean ditemukan = false;
        System.out.print("Indeks kemunculan " + target + " : ");

        int scanPtr = 0;
        while (scanPtr < arrayRef.length) {
            if (arrayRef[scanPtr] == target) {
                System.out.print(scanPtr + " ");
                ditemukan = true;
            }
            scanPtr += 1; // modifikasi increment pointer
        }
        if (!ditemukan) {
            System.out.print("(tidak ada)");
        }
        System.out.println();
    }

    // Menghitung jumlah langkah pencarian sampai target ditemukan
    public static int hitungLangkah(int[] arr, int target) {
        int[] targetSequence = arr; // Variable aliasing
        int langkah = 0;
        int stepPtr = 0;

        while (stepPtr < targetSequence.length) {
            langkah += 1; // modifikasi increment
            if (targetSequence[stepPtr] == target) {
                break;
            }
            stepPtr += 1; // modifikasi increment pointer
        }
        return langkah;
    }

    // Menampilkan isi array ke layar
    public static void tampilkanArray(int[] arr) {
        int[] elementsRef = arr; // Variable aliasing
        System.out.print("Isi array : [");

        int printPtr = 0;
        while (printPtr < elementsRef.length) {
            System.out.print(elementsRef[printPtr]);
            if (printPtr < elementsRef.length - 1) {
                System.out.print(", ");
            }
            printPtr += 1; // modifikasi increment pointer
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

        // Mengisi array dengan aliasing penunjuk
        int[] data = new int[jumlah];
        int[] inputStore = data; // Variable aliasing
        int fillPtr = 0;

        while (fillPtr < jumlah) {
            inputStore[fillPtr] = bacaAngka(input, "Data ke-" + (fillPtr + 1) + " : ");
            fillPtr += 1; // modifikasi increment pointer
        }

        int pilihan;
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
                    int hasil = search(data, target1);
                    if (hasil == -1) {
                        System.out.println("Nilai " + target1 + " tidak ditemukan.");
                    } else {
                        System.out.println("Nilai " + target1 + " ditemukan di indeks "
                                + hasil);
                    }
                    System.out.println("Jumlah langkah : " + hitungLangkah(data, target1));
                    break;
                case 2:
                    int target2 = bacaAngka(input, "Nilai yang dicari: ");
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