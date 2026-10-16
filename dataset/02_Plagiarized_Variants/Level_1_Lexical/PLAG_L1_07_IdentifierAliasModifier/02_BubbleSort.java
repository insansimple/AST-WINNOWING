/*
 * Program   : BubbleSort
 * Deskripsi : Mengurutkan array bilangan bulat menggunakan algoritma
 *             Bubble Sort. Dilengkapi versi ascending dan descending,
 *             input dari pengguna, serta penghitungan jumlah langkah.
 * Modifikasi: Obfuscation Pointer & Variable Aliasing serta modifikasi
 *             penulisan increment/decrement pointer.
 */
import java.util.Scanner;

public class BubbleSort {

    // Menghitung jumlah perbandingan dan pertukaran (untuk analisis)
    private int jumlahPerbandingan = 0;
    private int jumlahPertukaran = 0;

    // Logika inti: mengurutkan array dari kecil ke besar dengan pointer & variable aliasing
    public void sort(int[] numbers) {
        jumlahPerbandingan = 0;
        jumlahPertukaran = 0;

        // Pointer & Variable Aliasing pada array state
        int[] targetArrayRef = numbers;
        int n = targetArrayRef.length;

        int outerPtr = 0;
        while (outerPtr < n - 1) {
            int innerPtr = 0;
            while (innerPtr < n - outerPtr - 1) {
                jumlahPerbandingan += 1; // modifikasi increment

                int currentIdx = innerPtr;
                int nextIdx = innerPtr + 1;

                if (targetArrayRef[currentIdx] > targetArrayRef[nextIdx]) {
                    // Tukar posisi dua elemen
                    int temp = targetArrayRef[currentIdx];
                    targetArrayRef[currentIdx] = targetArrayRef[nextIdx];
                    targetArrayRef[nextIdx] = temp;
                    jumlahPertukaran += 1; // modifikasi increment
                }
                innerPtr += 1; // modifikasi increment pointer
            }
            outerPtr += 1; // modifikasi increment pointer
        }
    }

    // Mengurutkan array dari besar ke kecil dengan pointer & variable aliasing
    public void sortDescending(int[] numbers) {
        jumlahPerbandingan = 0;
        jumlahPertukaran = 0;

        // Pointer & Variable Aliasing pada array state
        int[] targetArrayRef = numbers;
        int n = targetArrayRef.length;

        int outerPtr = 0;
        while (outerPtr < n - 1) {
            int innerPtr = 0;
            while (innerPtr < n - outerPtr - 1) {
                jumlahPerbandingan += 1; // modifikasi increment

                int currentIdx = innerPtr;
                int nextIdx = innerPtr + 1;

                if (targetArrayRef[currentIdx] < targetArrayRef[nextIdx]) {
                    int temp = targetArrayRef[currentIdx];
                    targetArrayRef[currentIdx] = targetArrayRef[nextIdx];
                    targetArrayRef[nextIdx] = temp;
                    jumlahPertukaran += 1; // modifikasi increment
                }
                innerPtr += 1; // modifikasi increment pointer
            }
            outerPtr += 1; // modifikasi increment pointer
        }
    }

    public int getJumlahPerbandingan() {
        return jumlahPerbandingan;
    }

    public int getJumlahPertukaran() {
        return jumlahPertukaran;
    }

    // Menampilkan isi array ke layar dengan aliasing
    public static void tampilkanArray(String judul, int[] data) {
        int[] displayBuffer = data; // Variable aliasing
        System.out.print(judul + " : [");

        int cursorPtr = 0;
        while (cursorPtr < displayBuffer.length) {
            System.out.print(displayBuffer[cursorPtr]);
            if (cursorPtr < displayBuffer.length - 1) {
                System.out.print(", ");
            }
            cursorPtr += 1; // modifikasi increment pointer
        }
        System.out.println("]");
    }

    // Menyalin array dengan aliasing buffer
    public static int[] salinArray(int[] data) {
        int[] srcArray = data; // Variable aliasing
        int[] destArray = new int[srcArray.length];

        int copyPtr = 0;
        while (copyPtr < srcArray.length) {
            destArray[copyPtr] = srcArray[copyPtr];
            copyPtr += 1; // modifikasi increment pointer
        }
        return destArray;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        BubbleSort pengurut = new BubbleSort();

        // Membaca jumlah data dengan validasi
        int jumlah = 0;
        while (jumlah <= 0) {
            System.out.print("Masukkan jumlah data: ");
            if (input.hasNextInt()) {
                jumlah = input.nextInt();
                if (jumlah <= 0) {
                    System.out.println("Jumlah data harus lebih dari 0!");
                }
            } else {
                System.out.println("Input harus berupa angka bulat!");
                input.next();
            }
        }

        // Membaca isi array
        int[] data = new int[jumlah];
        int inputCursor = 0;
        while (inputCursor < jumlah) {
            System.out.print("Data ke-" + (inputCursor + 1) + " : ");
            while (!input.hasNextInt()) {
                System.out.print("Harus angka, ulangi: ");
                input.next();
            }
            data[inputCursor] = input.nextInt();
            inputCursor += 1; // modifikasi increment pointer
        }

        System.out.println("\n===== HASIL PENGURUTAN =====");
        tampilkanArray("Data awal   ", data);

        // Urutkan ascending pada salinan array
        int[] asc = salinArray(data);
        pengurut.sort(asc);
        tampilkanArray("Ascending   ", asc);
        System.out.println("Perbandingan: " + pengurut.getJumlahPerbandingan()
                + ", Pertukaran: " + pengurut.getJumlahPertukaran());

        // Urutkan descending pada salinan array
        int[] desc = salinArray(data);
        pengurut.sortDescending(desc);
        tampilkanArray("Descending  ", desc);
        System.out.println("Perbandingan: " + pengurut.getJumlahPerbandingan()
                + ", Pertukaran: " + pengurut.getJumlahPertukaran());

        input.close();
    }
}