/*
 * =========================================================================
 * Nama Berkas : LCMCalculator.java
 * Deskripsi   : Penentuan Kelipatan Persekutuan Terkecil (KPK)
 * Gaya Kode   : Allman Bracing Layout & Restructured Internal Documentation
 * =========================================================================
 */

import java.util.Scanner;

public class LCMCalculator
{
    public int computeLCM(int num1, int num2)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Pendekatan inkremental: menguji kelipatan nilai maksimum kedua bilangan
         */
        int max = Math.max(num1, num2);
        while (true)
        {
            if (max % num1 == 0 && max % num2 == 0)
            {
                return max;
            }
            max++;
        }
    }

    public int computeGCD(int a, int b)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Algoritma Euclid pembantu komputasi efisien KPK
         */
        while (b != 0)
        {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public long computeLCMCepat(int a, int b)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Evaluasi cepat: KPK(a,b) = (a / FPB(a,b)) * b dengan proteksi 64-bit long
         */
        return (long) a / computeGCD(a, b) * b;
    }

    public long computeLCMTiga(int a, int b, int c)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Ekstensi KPK untuk tiga bilangan secara berantai
         */
        long kpkAB = computeLCMCepat(a, b);
        long x = kpkAB;
        long y = c;

        while (y != 0)
        {
            long temp = y;
            y = x % y;
            x = temp;
        }
        return kpkAB / x * c;
    }

    public void tampilkanKelipatan(int angka, int jumlah)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Menampilkan deret kelipatan skalar dari suatu bilangan
         */
        System.out.print("Kelipatan " + angka + " : ");
        for (int i = 1; i <= jumlah; i++)
        {
            System.out.print((long) angka * i + " ");
        }
        System.out.println();
    }

    public void bandingkanMetode(int a, int b)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Verifikasi ekuivalensi hasil metode inkremental vs formula FPB
         */
        int hasilLambat = computeLCM(a, b);
        long hasilCepat = computeLCMCepat(a, b);

        System.out.println("Hasil cara bertahap : " + hasilLambat);
        System.out.println("Hasil cara FPB      : " + hasilCepat);

        if (hasilLambat == hasilCepat)
        {
            System.out.println("Kedua cara menghasilkan nilai yang sama.");
        }
        else
        {
            System.out.println("Hasil berbeda, ada yang salah!");
        }
    }

    public static int bacaAngka(Scanner input, String pesan)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Validasi tipe masukan angka bulat
         */
        System.out.print(pesan);
        while (!input.hasNextInt())
        {
            System.out.print("Input harus angka bulat, ulangi: ");
            input.next();
        }
        return input.nextInt();
    }

    public static int bacaPositif(Scanner input, String pesan)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Memastikan bilangan masukan bernilai lebih dari 0
         */
        int nilai = 0;
        while (nilai <= 0)
        {
            nilai = bacaAngka(input, pesan);
            if (nilai <= 0)
            {
                System.out.println("Bilangan harus lebih dari 0!");
            }
        }
        return nilai;
    }

    public static void main(String[] args)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Loop eksekusi antarmuka kalkulator KPK
         */
        Scanner input = new Scanner(System.in);
        LCMCalculator kalkulator = new LCMCalculator();

        int pilihan;
        do
        {
            System.out.println("\n===== PROGRAM KPK =====");
            System.out.println("1. Hitung KPK dua bilangan");
            System.out.println("2. Hitung KPK tiga bilangan");
            System.out.println("3. Tampilkan kelipatan sebuah bilangan");
            System.out.println("4. Bandingkan dua cara menghitung KPK");
            System.out.println("0. Keluar");
            pilihan = bacaAngka(input, "Pilih menu: ");

            switch (pilihan)
            {
                case 1:
                {
                    int a1 = bacaPositif(input, "Bilangan pertama : ");
                    int b1 = bacaPositif(input, "Bilangan kedua   : ");
                    System.out.println("KPK(" + a1 + ", " + b1 + ") = "
                            + kalkulator.computeLCM(a1, b1));
                    break;
                }
                case 2:
                {
                    int a2 = bacaPositif(input, "Bilangan pertama : ");
                    int b2 = bacaPositif(input, "Bilangan kedua   : ");
                    int c2 = bacaPositif(input, "Bilangan ketiga  : ");
                    System.out.println("KPK(" + a2 + ", " + b2 + ", " + c2 + ") = "
                            + kalkulator.computeLCMTiga(a2, b2, c2));
                    break;
                }
                case 3:
                {
                    int angka = bacaPositif(input, "Masukkan bilangan : ");
                    int jumlah = bacaPositif(input, "Jumlah kelipatan : ");
                    kalkulator.tampilkanKelipatan(angka, jumlah);
                    break;
                }
                case 4:
                {
                    int a4 = bacaPositif(input, "Bilangan pertama : ");
                    int b4 = bacaPositif(input, "Bilangan kedua   : ");
                    kalkulator.bandingkanMetode(a4, b4);
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