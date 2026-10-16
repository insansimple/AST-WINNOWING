/*
 * Program   : BubbleSort
 * Deskripsi : Mengurutkan array bilangan bulat menggunakan algoritma
 *             Bubble Sort. Dilengkapi versi ascending dan descending,
 *             input dari pengguna, serta penghitungan jumlah langkah.
 */
import java.util.Scanner;

public class BubbleSort {

    // Menghitung jumlah perbandingan dan pertukaran (untuk analisis)
    private int totalBanding = 0;
    private int totalTukar = 0;

    // Logika inti: mengurutkan array dari kecil ke besar
    public void urutkan(int[] arr) {
        totalBanding = 0;
        totalTukar = 0;

        int panjang = arr.length;
        for (int p = 0; p < panjang - 1; p++) {
            for (int q = 0; q < panjang - p - 1; q++) {
                totalBanding++;
                if (arr[q] > arr[q + 1]) {
                    // Tukar posisi dua elemen
                    int sementara = arr[q];
                    arr[q] = arr[q + 1];
                    arr[q + 1] = sementara;
                    totalTukar++;
                }
            }
        }
    }

    // Mengurutkan array dari besar ke kecil
    public void urutkanMenurun(int[] arr) {
        totalBanding = 0;
        totalTukar = 0;

        int panjang = arr.length;
        for (int p = 0; p < panjang - 1; p++) {
            for (int q = 0; q < panjang - p - 1; q++) {
                totalBanding++;
                if (arr[q] < arr[q + 1]) {
                    int sementara = arr[q];
                    arr[q] = arr[q + 1];
                    arr[q + 1] = sementara;
                    totalTukar++;
                }
            }
        }
    }

    public int getTotalBanding() {
        return totalBanding;
    }

    public int getTotalTukar() {
        return totalTukar;
    }

    // Menampilkan isi array ke layar
    public static void cetakArray(String label, int[] elemen) {
        System.out.print(label + " : [");
        for (int k = 0; k < elemen.length; k++) {
            System.out.print(elemen[k]);
            if (k < elemen.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    // Menyalin array agar data asli tidak berubah
    public static int[] duplikasiArray(int[] elemen) {
        int[] salinan = new int[elemen.length];
        for (int k = 0; k < elemen.length; k++) {
            salinan[k] = elemen[k];
        }
        return salinan;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BubbleSort sorter = new BubbleSort();

        // Membaca jumlah data dengan validasi
        int ukuran = 0;
        while (ukuran <= 0) {
            System.out.print("Masukkan jumlah data: ");
            if (scanner.hasNextInt()) {
                ukuran = scanner.nextInt();
                if (ukuran <= 0) {
                    System.out.println("Jumlah data harus lebih dari 0!");
                }
            } else {
                System.out.println("Input harus berupa angka bulat!");
                scanner.next();
            }
        }

        // Membaca isi array
        int[] tabel = new int[ukuran];
        for (int k = 0; k < ukuran; k++) {
            System.out.print("Data ke-" + (k + 1) + " : ");
            while (!scanner.hasNextInt()) {
                System.out.print("Harus angka, ulangi: ");
                scanner.next();
            }
            tabel[k] = scanner.nextInt();
        }

        System.out.println("\n===== HASIL PENGURUTAN =====");
        cetakArray("Data awal   ", tabel);

        // Urutkan ascending pada salinan array
        int[] naik = duplikasiArray(tabel);
        sorter.urutkan(naik);
        cetakArray("Ascending   ", naik);
        System.out.println("Perbandingan: " + sorter.getTotalBanding()
                + ", Pertukaran: " + sorter.getTotalTukar());

        // Urutkan descending pada salinan array
        int[] turun = duplikasiArray(tabel);
        sorter.urutkanMenurun(turun);
        cetakArray("Descending  ", turun);
        System.out.println("Perbandingan: " + sorter.getTotalBanding()
                + ", Pertukaran: " + sorter.getTotalTukar());

        scanner.close();
    }
}