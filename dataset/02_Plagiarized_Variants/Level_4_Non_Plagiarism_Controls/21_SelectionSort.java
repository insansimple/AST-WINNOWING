/*
 * Program   : SelectionSortApp
 * Deskripsi : Pengurutan array dengan mencari elemen minimum/maksimum
 *             secara berulang (Selection Sort). Mendukung urutan
 *             ascending (cari minimum) dan descending (cari maksimum).
 */
import java.util.Scanner;

public class SelectionSort {

    // Menampilkan isi array dalam satu baris
    public static void tampilkanArray(int[] data) {
        System.out.print("[ ");
        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i] + " ");
        }
        System.out.println("]");
    }

    // Menukar dua elemen pada array
    public static void tukar(int[] data, int a, int b) {
        int temp = data[a];
        data[a] = data[b];
        data[b] = temp;
    }

    // Selection sort ascending: cari elemen MINIMUM secara berulang
    public static void sortAscending(int[] data) {
        int n = data.length;
        for (int i = 0; i < n - 1; i++) {
            int indeksMin = i;
            // cari nilai terkecil pada sisa array (i+1 sampai akhir)
            for (int j = i + 1; j < n; j++) {
                if (data[j] < data[indeksMin]) {
                    indeksMin = j;
                }
            }
            // taruh nilai terkecil di posisi i
            if (indeksMin != i) {
                tukar(data, i, indeksMin);
            }
            System.out.print("Langkah " + (i + 1) + " : ");
            tampilkanArray(data);
        }
    }

    // Selection sort descending: cari elemen MAKSIMUM secara berulang
    public static void sortDescending(int[] data) {
        int n = data.length;
        for (int i = 0; i < n - 1; i++) {
            int indeksMax = i;
            // cari nilai terbesar pada sisa array (i+1 sampai akhir)
            for (int j = i + 1; j < n; j++) {
                if (data[j] > data[indeksMax]) {
                    indeksMax = j;
                }
            }
            // taruh nilai terbesar di posisi i
            if (indeksMax != i) {
                tukar(data, i, indeksMax);
            }
            System.out.print("Langkah " + (i + 1) + " : ");
            tampilkanArray(data);
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Input jumlah elemen dengan validasi sederhana
        int jumlah;
        do {
            System.out.print("Masukkan jumlah elemen (minimal 1): ");
            jumlah = input.nextInt();
            if (jumlah < 1) {
                System.out.println("Jumlah tidak valid, coba lagi!");
            }
        } while (jumlah < 1);

        // Input elemen array
        int[] angka = new int[jumlah];
        for (int i = 0; i < jumlah; i++) {
            System.out.print("Elemen ke-" + (i + 1) + " : ");
            angka[i] = input.nextInt();
        }

        // Menu pilihan jenis pengurutan
        System.out.println("\nPilih jenis pengurutan:");
        System.out.println("1. Ascending  (cari minimum)");
        System.out.println("2. Descending (cari maksimum)");
        System.out.print("Pilihan: ");
        int pilihan = input.nextInt();

        System.out.print("\nArray awal : ");
        tampilkanArray(angka);
        System.out.println();

        switch (pilihan) {
            case 1:
                sortAscending(angka);
                break;
            case 2:
                sortDescending(angka);
                break;
            default:
                System.out.println("Pilihan tidak tersedia!");
                input.close();
                return;
        }

        System.out.print("\nHasil akhir: ");
        tampilkanArray(angka);

        input.close();
    }
}