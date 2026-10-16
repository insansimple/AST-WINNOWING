/*
 * Program   : InsertionSortApp
 * Deskripsi : Pengurutan array dengan menyisipkan elemen ke posisi
 *             yang tepat satu per satu (Insertion Sort).
 *             Mendukung urutan ascending dan descending.
 */
import java.util.Scanner;

public class InsertionSort {

    // Menampilkan isi array dalam satu baris
    public static void tampilkanArray(int[] data) {
        System.out.print("[ ");
        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i] + " ");
        }
        System.out.println("]");
    }

    // Insertion sort ascending: sisipkan elemen ke bagian kiri yang sudah terurut
    public static void sortAscending(int[] data) {
        int n = data.length;
        for (int i = 1; i < n; i++) {
            int kunci = data[i];   // elemen yang akan disisipkan
            int j = i - 1;

            // geser elemen yang lebih besar dari kunci ke kanan
            while (j >= 0 && data[j] > kunci) {
                data[j + 1] = data[j];
                j--;
            }
            // tempatkan kunci di posisi yang tepat
            data[j + 1] = kunci;

            System.out.print("Langkah " + i + " (sisip " + kunci + ") : ");
            tampilkanArray(data);
        }
    }

    // Insertion sort descending: sama seperti ascending, perbandingan dibalik
    public static void sortDescending(int[] data) {
        int n = data.length;
        for (int i = 1; i < n; i++) {
            int kunci = data[i];
            int j = i - 1;

            // geser elemen yang lebih kecil dari kunci ke kanan
            while (j >= 0 && data[j] < kunci) {
                data[j + 1] = data[j];
                j--;
            }
            data[j + 1] = kunci;

            System.out.print("Langkah " + i + " (sisip " + kunci + ") : ");
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
        System.out.println("1. Ascending  (kecil ke besar)");
        System.out.println("2. Descending (besar ke kecil)");
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