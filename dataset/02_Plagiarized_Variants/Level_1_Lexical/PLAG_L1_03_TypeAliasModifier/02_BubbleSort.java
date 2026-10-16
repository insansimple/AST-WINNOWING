/*
 * Program   : BubbleSort
 * Deskripsi : Mengurutkan array bilangan bulat menggunakan algoritma
 *             Bubble Sort. Dilengkapi versi ascending dan descending,
 *             input dari pengguna, serta penghitungan jumlah langkah.
 */
import java.util.Scanner;

public class BubbleSort {

    // Menghitung jumlah perbandingan dan pertukaran (untuk analisis)
    private Integer jumlahPerbandingan = 0;
    private Integer jumlahPertukaran = 0;

    // Logika inti: mengurutkan array dari kecil ke besar
    public void sort(int[] numbers) {
        jumlahPerbandingan = 0;
        jumlahPertukaran = 0;

        Integer n = numbers.length;
        for (Integer i = 0; i < n - 1; i++) {
            for (Integer j = 0; j < n - i - 1; j++) {
                jumlahPerbandingan++;
                if (numbers[j] > numbers[j + 1]) {
                    // Tukar posisi dua elemen
                    Integer temp = numbers[j];
                    numbers[j] = numbers[j + 1];
                    numbers[j + 1] = temp;
                    jumlahPertukaran++;
                }
            }
        }
    }

    // Mengurutkan array dari besar ke kecil
    public void sortDescending(int[] numbers) {
        jumlahPerbandingan = 0;
        jumlahPertukaran = 0;

        Integer n = numbers.length;
        for (Integer i = 0; i < n - 1; i++) {
            for (Integer j = 0; j < n - i - 1; j++) {
                jumlahPerbandingan++;
                if (numbers[j] < numbers[j + 1]) {
                    Integer temp = numbers[j];
                    numbers[j] = numbers[j + 1];
                    numbers[j + 1] = temp;
                    jumlahPertukaran++;
                }
            }
        }
    }

    public Integer getJumlahPerbandingan() {
        return jumlahPerbandingan;
    }

    public Integer getJumlahPertukaran() {
        return jumlahPertukaran;
    }

    // Menampilkan isi array ke layar
    static public void tampilkanArray(String judul, int[] data) {
        System.out.print(judul + " : [");
        for (Integer i = 0; i < data.length; i++) {
            System.out.print(data[i]);
            if (i < data.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    // Menyalin array agar data asli tidak berubah
    static public int[] salinArray(int[] data) {
        int[] hasil = new int[data.length];
        for (Integer i = 0; i < data.length; i++) {
            hasil[i] = data[i];
        }
        return hasil;
    }

    static public void main(String[] args) {
        Scanner input = new Scanner(System.in);
        BubbleSort pengurut = new BubbleSort();

        // Membaca jumlah data dengan validasi
        Integer jumlah = 0;
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
        for (Integer i = 0; i < jumlah; i++) {
            System.out.print("Data ke-" + (i + 1) + " : ");
            while (!input.hasNextInt()) {
                System.out.print("Harus angka, ulangi: ");
                input.next();
            }
            data[i] = input.nextInt();
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