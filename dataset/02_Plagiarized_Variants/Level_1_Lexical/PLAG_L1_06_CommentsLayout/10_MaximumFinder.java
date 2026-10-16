/*
 * =========================================================================
 * Nama Berkas : MaximumFinder.java
 * Deskripsi   : Komputasi Ekstremum (Maksimum, Minimum, Rata-rata, Urutan)
 * Tata Letak  : Allman Bracing Style dengan Dokumentasi Method Internal
 * =========================================================================
 */

import java.util.Scanner;

public class MaximumFinder
{
    public int findMax(int a, int b, int c)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Menentukan nilai terbesar dari 3 integer masukan
         */
        int maxVal = a;
        if (b > maxVal)
        {
            maxVal = b;
        }
        if (c > maxVal)
        {
            maxVal = c;
        }
        return maxVal;
    }

    public int findMin(int a, int b, int c)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Menentukan nilai terkecil dari 3 integer masukan
         */
        int minVal = a;
        if (b < minVal)
        {
            minVal = b;
        }
        if (c < minVal)
        {
            minVal = c;
        }
        return minVal;
    }

    public int findMaxArray(int[] data)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Menemukan nilai puncak maksimum dari koleksi array integer
         */
        int maxVal = data[0];
        for (int i = 1; i < data.length; i++)
        {
            if (data[i] > maxVal)
            {
                maxVal = data[i];
            }
        }
        return maxVal;
    }

    public double hitungRataRata(int a, int b, int c)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Menghitung nilai mean aritmatika dari 3 bilangan
         */
        return (a + b + c) / 3.0;
    }

    public void tampilkanUrut(int a, int b, int c)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Menampilkan permutasi urutan terurut (naik dan turun) dari 3 nilai
         */
        int terbesar = findMax(a, b, c);
        int terkecil = findMin(a, b, c);
        int tengah = a + b + c - terbesar - terkecil;

        System.out.println("Urutan naik  : " + terkecil + ", " + tengah + ", " + terbesar);
        System.out.println("Urutan turun : " + terbesar + ", " + tengah + ", " + terkecil);
    }

    public static int bacaAngka(Scanner input, String pesan)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Input reader terverifikasi integer
         */
        System.out.print(pesan);
        while (!input.hasNextInt())
        {
            System.out.print("Input harus angka bulat, ulangi: ");
            input.next();
        }
        return input.nextInt();
    }

    public static void main(String[] args)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Menu utama pengujian fungsi-fungsi MaximumFinder
         */
        Scanner input = new Scanner(System.in);
        MaximumFinder pencari = new MaximumFinder();

        int pilihan;
        do
        {
            System.out.println("\n===== PROGRAM NILAI TERBESAR =====");
            System.out.println("1. Cari terbesar dari tiga bilangan");
            System.out.println("2. Cari terkecil dari tiga bilangan");
            System.out.println("3. Hitung rata-rata tiga bilangan");
            System.out.println("4. Urutkan tiga bilangan");
            System.out.println("5. Cari terbesar dari sejumlah data");
            System.out.println("0. Keluar");
            pilihan = bacaAngka(input, "Pilih menu: ");

            switch (pilihan)
            {
                case 1:
                {
                    int a1 = bacaAngka(input, "Bilangan pertama : ");
                    int b1 = bacaAngka(input, "Bilangan kedua   : ");
                    int c1 = bacaAngka(input, "Bilangan ketiga  : ");
                    System.out.println("Nilai terbesar : " + pencari.findMax(a1, b1, c1));
                    break;
                }
                case 2:
                {
                    int a2 = bacaAngka(input, "Bilangan pertama : ");
                    int b2 = bacaAngka(input, "Bilangan kedua   : ");
                    int c2 = bacaAngka(input, "Bilangan ketiga  : ");
                    System.out.println("Nilai terkecil : " + pencari.findMin(a2, b2, c2));
                    break;
                }
                case 3:
                {
                    int a3 = bacaAngka(input, "Bilangan pertama : ");
                    int b3 = bacaAngka(input, "Bilangan kedua   : ");
                    int c3 = bacaAngka(input, "Bilangan ketiga  : ");
                    System.out.println("Rata-rata : " + pencari.hitungRataRata(a3, b3, c3));
                    break;
                }
                case 4:
                {
                    int a4 = bacaAngka(input, "Bilangan pertama : ");
                    int b4 = bacaAngka(input, "Bilangan kedua   : ");
                    int c4 = bacaAngka(input, "Bilangan ketiga  : ");
                    pencari.tampilkanUrut(a4, b4, c4);
                    break;
                }
                case 5:
                {
                    int jumlah = bacaAngka(input, "Jumlah data : ");
                    if (jumlah <= 0)
                    {
                        System.out.println("Jumlah data harus lebih dari 0!");
                    }
                    else
                    {
                        int[] data = new int[jumlah];
                        for (int i = 0; i < jumlah; i++)
                        {
                            data[i] = bacaAngka(input, "Data ke-" + (i + 1) + " : ");
                        }
                        System.out.println("Nilai terbesar : " + pencari.findMaxArray(data));
                    }
                    break;
                }
                case 0:
                {
                    System.out.println("Terima kasih, program selesai.");
                    break;
                }
                default:
                {
                    System.out.println("Menu tidak tersedia!");
                    break;
                }
            }
        }
        while (pilihan != 0);

        input.close();
    }
}