/*
 * Program   : BubbleSort
 * Deskripsi : Mengurutkan array bilangan bulat menggunakan algoritma
 *             Bubble Sort. Dilengkapi versi ascending dan descending,
 *             input dari pengguna, serta penghitungan jumlah langkah.
 */
import java.util.Scanner;

public class BubbleSort {

    // Inner static class wrapper untuk enkapsulasi array data primitif
    public static class ArrayWrapper {
        private int[] elements;

        public ArrayWrapper(int[] data) {
            this.elements = data;
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

        public void swap(int i, int j) {
            int temp = elements[i];
            elements[i] = elements[j];
            elements[j] = temp;
        }

        public int[] rawArray() {
            return elements;
        }
    }

    // Inner static class wrapper untuk metrik statistik perbandingan dan pertukaran
    public static class SortMetrics {
        private int comparisons = 0;
        private int swaps = 0;

        public void reset() {
            comparisons = 0;
            swaps = 0;
        }

        public void incComparisons() {
            comparisons++;
        }

        public void incSwaps() {
            swaps++;
        }

        public int getComparisons() {
            return comparisons;
        }

        public int getSwaps() {
            return swaps;
        }
    }

    private SortMetrics metrics = new SortMetrics();

    // Logika inti: mengurutkan array dari kecil ke besar
    public void sort(int[] numbers) {
        metrics.reset();
        ArrayWrapper wrapper = new ArrayWrapper(numbers);
        int n = wrapper.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                metrics.incComparisons();
                if (wrapper.get(j) > wrapper.get(j + 1)) {
                    // Tukar posisi dua elemen
                    wrapper.swap(j, j + 1);
                    metrics.incSwaps();
                }
            }
        }
    }

    // Mengurutkan array dari besar ke kecil
    public void sortDescending(int[] numbers) {
        metrics.reset();
        ArrayWrapper wrapper = new ArrayWrapper(numbers);
        int n = wrapper.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                metrics.incComparisons();
                if (wrapper.get(j) < wrapper.get(j + 1)) {
                    wrapper.swap(j, j + 1);
                    metrics.incSwaps();
                }
            }
        }
    }

    public int getJumlahPerbandingan() {
        return metrics.getComparisons();
    }

    public int getJumlahPertukaran() {
        return metrics.getSwaps();
    }

    // Menampilkan isi array ke layar
    public static void tampilkanArray(String judul, int[] data) {
        ArrayWrapper wrapper = new ArrayWrapper(data);
        System.out.print(judul + " : [");
        for (int i = 0; i < wrapper.size(); i++) {
            System.out.print(wrapper.get(i));
            if (i < wrapper.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    // Menyalin array agar data asli tidak berubah
    public static int[] salinArray(int[] data) {
        ArrayWrapper src = new ArrayWrapper(data);
        int[] hasil = new int[src.size()];
        ArrayWrapper dst = new ArrayWrapper(hasil);
        for (int i = 0; i < src.size(); i++) {
            dst.set(i, src.get(i));
        }
        return dst.rawArray();
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
        ArrayWrapper inputWrapper = new ArrayWrapper(data);
        for (int i = 0; i < jumlah; i++) {
            System.out.print("Data ke-" + (i + 1) + " : ");
            while (!input.hasNextInt()) {
                System.out.print("Harus angka, ulangi: ");
                input.next();
            }
            inputWrapper.set(i, input.nextInt());
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