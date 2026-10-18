/**
 * Package / File : 02_BubbleSort.java
 * Algoritma Pengurutan Bubble Sort (Ascending & Descending)
 * Dibuat untuk pengujian analisis leksikal
 */
import java.util.Scanner;

public class BubbleSort
{
    /* Atribut pencatat metrik eksekusi */
    private int jumlahPerbandingan = 0;
    private int jumlahPertukaran    = 0;

    /**
     * Mengurutkan data secara menaik (Ascending)
     */
    public void sort(int[] numbers)
    {
        jumlahPerbandingan = 0;
        jumlahPertukaran    = 0;

        int n = numbers.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < (n - i - 1); j++) {
                jumlahPerbandingan++;
                if (numbers[j] > numbers[j + 1]) {
                    /* Proses swap nilai */
                    int temp = numbers[j];
                    numbers[j] = numbers[j + 1];
                    numbers[j + 1] = temp;
                    jumlahPertukaran++;
                }
            }
        }
    }

    /**
     * Mengurutkan data secara menurun (Descending)
     */
    public void sortDescending(int[] numbers)
    {
        jumlahPerbandingan = 0;
        jumlahPertukaran    = 0;

        int n = numbers.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < (n - i - 1); j++) {
                jumlahPerbandingan++;
                if (numbers[j] < numbers[j + 1]) {
                    /* Proses pertukaran elemen */
                    int temp = numbers[j];
                    numbers[j] = numbers[j + 1];
                    numbers[j + 1] = temp;
                    jumlahPertukaran++;
                }
            }
        }
    }

    public int getJumlahPerbandingan() { return jumlahPerbandingan; }

    public int getJumlahPertukaran() { return jumlahPertukaran; }

    /* Utilitas mencetak elemen array ke konsol */
    public static void tampilkanArray(String judul, int[] data)
    {
        System.out.print(judul + " : [");
        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i]);
            if (i < data.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    /* Utilitas penggandaan array */
    public static int[] salinArray(int[] data)
    {
        int[] hasil = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            hasil[i] = data[i];
        }
        return hasil;
    }

    /* Rutin utama program */
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        BubbleSort pengurut = new BubbleSort();

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
                input.next(); /* bersihkan buffer */
            }
        }

        int[] data = new int[jumlah];
        for (int i = 0; i < jumlah; i++) {
            System.out.print("Data ke-" + (i + 1) + " : ");
            while (!input.hasNextInt()) {
                System.out.print("Harus angka, ulangi: ");
                input.next();
            }
            data[i] = input.nextInt();
        }

        System.out.println("\n===== HASIL PENGURUTAN =====");
        tampilkanArray("Data awal   ", data);

        /* Evaluasi sorting urutan menaik */
        int[] asc = salinArray(data);
        pengurut.sort(asc);
        tampilkanArray("Ascending   ", asc);
        System.out.println("Perbandingan: " + pengurut.getJumlahPerbandingan() + ", Pertukaran: " + pengurut.getJumlahPertukaran());

        /* Evaluasi sorting urutan menurun */
        int[] desc = salinArray(data);
        pengurut.sortDescending(desc);
        tampilkanArray("Descending  ", desc);
        System.out.println("Perbandingan: " + pengurut.getJumlahPerbandingan() + ", Pertukaran: " + pengurut.getJumlahPertukaran());

        input.close();
    }
}