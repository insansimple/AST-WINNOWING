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
    public void urutkanNaik(int[] larik) {
        totalBanding = 0;
        totalTukar = 0;

        int panjang = larik.length;
        for (int p = 0; p < panjang - 1; p++) {
            for (int q = 0; q < panjang - p - 1; q++) {
                totalBanding++;
                if (larik[q] > larik[q + 1]) {
                    // Tukar posisi dua elemen
                    int sementara = larik[q];
                    larik[q] = larik[q + 1];
                    larik[q + 1] = sementara;
                    totalTukar++;
                }
            }
        }
    }

    // Mengurutkan array dari besar ke kecil
    public void urutkanTurun(int[] larik) {
        totalBanding = 0;
        totalTukar = 0;

        int panjang = larik.length;
        for (int p = 0; p < panjang - 1; p++) {
            for (int q = 0; q < panjang - p - 1; q++) {
                totalBanding++;
                if (larik[q] < larik[q + 1]) {
                    int sementara = larik[q];
                    larik[q] = larik[q + 1];
                    larik[q + 1] = sementara;
                    totalTukar++;
                }
            }
        }
    }

    public int ambilBanding() {
        return totalBanding;
    }

    public int ambilTukar() {
        return totalTukar;
    }

    // Menampilkan isi array ke layar
    public static void cetakArray(String label, int[] isi) {
        System.out.print(new StringBuilder().append(label).append(" : [").toString());
        for (int p = 0; p < isi.length; p++) {
            System.out.print(isi[p]);
            if (p < isi.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.print("]");
        System.out.print(System.lineSeparator());
    }

    // Menyalin array agar data asli tidak berubah
    public static int[] gandakanArray(int[] isi) {
        int[] salinan = new int[isi.length];
        for (int p = 0; p < isi.length; p++) {
            salinan[p] = isi[p];
        }
        return salinan;
    }

    public static void main(String[] args) {
        Scanner pemindai = new Scanner(System.in);
        BubbleSort pengurut = new BubbleSort();

        // Membaca jumlah data dengan validasi
        int banyak = 0;
        while (banyak <= 0) {
            System.out.print("Masukkan jumlah data: ");
            if (pemindai.hasNextInt()) {
                banyak = pemindai.nextInt();
                if (banyak <= 0) {
                    System.out.print("Jumlah data harus lebih dari 0!");
                    System.out.print(System.lineSeparator());
                }
            } else {
                System.out.print("Input harus berupa angka bulat!");
                System.out.print(System.lineSeparator());
                pemindai.next();
            }
        }

        // Membaca isi array
        int[] kumpulan = new int[banyak];
        for (int p = 0; p < banyak; p++) {
            System.out.print(new StringBuilder().append("Data ke-").append(p + 1).append(" : ").toString());
            while (!pemindai.hasNextInt()) {
                System.out.print("Harus angka, ulangi: ");
                pemindai.next();
            }
            kumpulan[p] = pemindai.nextInt();
        }

        System.out.print("\n===== HASIL PENGURUTAN =====");
        System.out.print(System.lineSeparator());
        cetakArray("Data awal   ", kumpulan);

        // Urutkan ascending pada salinan array
        int[] naik = gandakanArray(kumpulan);
        pengurut.urutkanNaik(naik);
        cetakArray("Ascending   ", naik);
        System.out.print(new StringBuilder().append("Perbandingan: ").append(pengurut.ambilBanding())
                .append(", Pertukaran: ").append(pengurut.ambilTukar()).toString());
        System.out.print(System.lineSeparator());

        // Urutkan descending pada salinan array
        int[] turun = gandakanArray(kumpulan);
        pengurut.urutkanTurun(turun);
        cetakArray("Descending  ", turun);
        System.out.print(new StringBuilder().append("Perbandingan: ").append(pengurut.ambilBanding())
                .append(", Pertukaran: ").append(pengurut.ambilTukar()).toString());
        System.out.print(System.lineSeparator());

        pemindai.close();
    }
}