/**
 * MODULE     : BubbleSort.java
 * DESKRIPSI  : Implementasi algoritma pengurutan Bubble Sort (Naik dan Turun)
 * GAYA KODE  : Allman Bracing & Restructured Internal Method Documentation
 */

import java.util.Scanner;

public class BubbleSort
{
    // Properti pelacak kompleksitas operasi (perbandingan dan pergeseran elemen)
    private int jumlahPerbandingan = 0;
    private int jumlahPertukaran = 0;

    public void sort(int[] numbers)
    {
        /*
         * [METHOD DOC]
         * Nama    : sort
         * Tujuan  : Mengurutkan array bilangan secara menaik (ascending)
         * Efek    : Mengubah urutan elemen pada parameter array `numbers`
         */
        jumlahPerbandingan = 0;
        jumlahPertukaran = 0;

        int n = numbers.length;
        for (int i = 0; i < n - 1; i++)
        {
            for (int j = 0; j < n - i - 1; j++)
            {
                jumlahPerbandingan++;
                if (numbers[j] > numbers[j + 1])
                {
                    // Operasi swap dua elemen bersebelahan
                    int temp = numbers[j];
                    numbers[j] = numbers[j + 1];
                    numbers[j + 1] = temp;
                    jumlahPertukaran++;
                }
            }
        }
    }

    public void sortDescending(int[] numbers)
    {
        /*
         * [METHOD DOC]
         * Nama    : sortDescending
         * Tujuan  : Mengurutkan array bilangan secara menurun (descending)
         * Efek    : Mengubah urutan elemen pada parameter array `numbers`
         */
        jumlahPerbandingan = 0;
        jumlahPertukaran = 0;

        int n = numbers.length;
        for (int i = 0; i < n - 1; i++)
        {
            for (int j = 0; j < n - i - 1; j++)
            {
                jumlahPerbandingan++;
                if (numbers[j] < numbers[j + 1])
                {
                    int temp = numbers[j];
                    numbers[j] = numbers[j + 1];
                    numbers[j + 1] = temp;
                    jumlahPertukaran++;
                }
            }
        }
    }

    public int getJumlahPerbandingan()
    {
        /* [GETTER] Mengembalikan jumlah komparasi yang terjadi */
        return jumlahPerbandingan;
    }

    public int getJumlahPertukaran()
    {
        /* [GETTER] Mengembalikan jumlah pertukaran data yang terjadi */
        return jumlahPertukaran;
    }

    public static void tampilkanArray(String judul, int[] data)
    {
        /*
         * [METHOD DOC]
         * Nama    : tampilkanArray
         * Deskripsi: Menampilkan representasi string dari array ke konsol
         */
        System.out.print(judul + " : [");
        for (int i = 0; i < data.length; i++)
        {
            System.out.print(data[i]);
            if (i < data.length - 1)
            {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    public static int[] salinArray(int[] data)
    {
        /*
         * [METHOD DOC]
         * Nama    : salinArray
         * Deskripsi: Membuat klon independen dari array masukan
         * Return  : int[] array baru
         */
        int[] hasil = new int[data.length];
        for (int i = 0; i < data.length; i++)
        {
            hasil[i] = data[i];
        }
        return hasil;
    }

    public static void main(String[] args)
    {
        /*
         * [METHOD DOC]
         * Nama    : main
         * Deskripsi: Alur interaktif pengambilan input, eksekusi sorting, dan pencetakan hasil
         */
        Scanner input = new Scanner(System.in);
        BubbleSort pengurut = new BubbleSort();

        int jumlah = 0;
        while (jumlah <= 0)
        {
            System.out.print("Masukkan jumlah data: ");
            if (input.hasNextInt())
            {
                jumlah = input.nextInt();
                if (jumlah <= 0)
                {
                    System.out.println("Jumlah data harus lebih dari 0!");
                }
            }
            else
            {
                System.out.println("Input harus berupa angka bulat!");
                input.next();
            }
        }

        int[] data = new int[jumlah];
        for (int i = 0; i < jumlah; i++)
        {
            System.out.print("Data ke-" + (i + 1) + " : ");
            while (!input.hasNextInt())
            {
                System.out.print("Harus angka, ulangi: ");
                input.next();
            }
            data[i] = input.nextInt();
        }

        System.out.println("\n===== HASIL PENGURUTAN =====");
        tampilkanArray("Data awal   ", data);

        // Sorting ascending
        int[] asc = salinArray(data);
        pengurut.sort(asc);
        tampilkanArray("Ascending   ", asc);
        System.out.println("Perbandingan: " + pengurut.getJumlahPerbandingan()
                + ", Pertukaran: " + pengurut.getJumlahPertukaran());

        // Sorting descending
        int[] desc = salinArray(data);
        pengurut.sortDescending(desc);
        tampilkanArray("Descending  ", desc);
        System.out.println("Perbandingan: " + pengurut.getJumlahPerbandingan()
                + ", Pertukaran: " + pengurut.getJumlahPertukaran());

        input.close();
    }
}