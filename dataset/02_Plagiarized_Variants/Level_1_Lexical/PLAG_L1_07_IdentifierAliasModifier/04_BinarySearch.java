/*
 * Program   : BinarySearch
 * Deskripsi : Mencari sebuah nilai di dalam array yang sudah terurut
 *             menggunakan algoritma Binary Search. Dilengkapi versi
 *             rekursif, penghitungan jumlah langkah, pengurutan data
 *             sebelum pencarian, input dari pengguna, dan menu sederhana.
 * Modifikasi: Obfuscation Pointer & Variable Aliasing serta modifikasi
 *             penulisan increment/decrement pointer.
 */
import java.util.Scanner;

public class BinarySearch {

    // Menyimpan jumlah langkah pencarian terakhir (untuk analisis)
    private int jumlahLangkah = 0;

    // Logika inti: pencarian iteratif dengan pointer & variable aliasing
    public int search(int[] arr, int target) {
        jumlahLangkah = 0;

        int[] searchRangeRef = arr; // Variable aliasing
        int leftPtr = 0;
        int rightPtr = searchRangeRef.length - 1;

        while (leftPtr <= rightPtr) {
            jumlahLangkah += 1; // modifikasi increment

            int centerPtr = leftPtr + (rightPtr - leftPtr) / 2;
            if (searchRangeRef[centerPtr] == target) {
                return centerPtr;
            }
            if (searchRangeRef[centerPtr] < target) {
                leftPtr = centerPtr + 1; // modifikasi pointer shift
            } else {
                rightPtr = centerPtr - 1; // modifikasi pointer shift
            }
        }
        return -1;
    }

    // Versi rekursif dengan aliasing penunjuk rentang
    public int searchRekursif(int[] arr, int target, int low, int high) {
        int leftBound = low;
        int rightBound = high;
        int[] sequenceRef = arr; // Variable aliasing

        if (leftBound > rightBound) {
            return -1;
        }

        int centerPtr = leftBound + (rightBound - leftBound) / 2;
        if (sequenceRef[centerPtr] == target) {
            return centerPtr;
        }
        if (sequenceRef[centerPtr] < target) {
            return searchRekursif(sequenceRef, target, centerPtr + 1, rightBound);
        }
        return searchRekursif(sequenceRef, target, leftBound, centerPtr - 1);
    }

    public int getJumlahLangkah() {
        return jumlahLangkah;
    }

    // Mengurutkan array dengan aliasing buffer dan modifikasi pointer
    public static void urutkan(int[] arr) {
        int[] targetBuffer = arr; // Variable aliasing
        int n = targetBuffer.length;

        int outerScan = 0;
        while (outerScan < n - 1) {
            int innerScan = 0;
            while (innerScan < n - outerScan - 1) {
                int cur = innerScan;
                int nxt = innerScan + 1;
                if (targetBuffer[cur] > targetBuffer[nxt]) {
                    int temp = targetBuffer[cur];
                    targetBuffer[cur] = targetBuffer[nxt];
                    targetBuffer[nxt] = temp;
                }
                innerScan += 1; // modifikasi increment pointer
            }
            outerScan += 1; // modifikasi increment pointer
        }
    }

    // Menampilkan isi array ke layar
    public static void tampilkanArray(int[] arr) {
        int[] printBuf = arr; // Variable aliasing
        System.out.print("Isi array : [");

        int printPtr = 0;
        while (printPtr < printBuf.length) {
            System.out.print(printBuf[printPtr]);
            if (printPtr < printBuf.length - 1) {
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
        BinarySearch pencari = new BinarySearch();

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
        int[] dataStore = data; // Variable aliasing
        int fillPtr = 0;

        while (fillPtr < jumlah) {
            dataStore[fillPtr] = bacaAngka(input, "Data ke-" + (fillPtr + 1) + " : ");
            fillPtr += 1; // modifikasi increment pointer
        }

        // Binary search hanya bisa untuk data terurut
        urutkan(data);
        System.out.println("\nData sudah diurutkan otomatis.");

        int pilihan;
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
                    int hasil1 = pencari.search(data, target1);
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
                    int hasil2 = pencari.searchRekursif(data, target2, 0, data.length - 1);
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