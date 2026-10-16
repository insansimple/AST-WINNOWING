// ========================================================
// Module      : FactorialCalculator.java
// Deskripsi   : Layanan komputasi faktorial berbasis iterasi & rekursi
// Penulis     : Peneliti (Restructured Comments & Allman Layout)
// ========================================================

import java.util.Scanner;

public class FactorialCalculator
{
    // Nilai batas maksimal masukan numerik (mencegah aritmatika overflow)
    static final int BATAS_MAKS = 20;

    public static long compute(int n)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Fungsi  : compute
         * Tujuan  : Menghitung nilai faktorial secara iteratif (pendekatan loop)
         * Param   : n - bilangan bulat non-negatif
         * Return  : nilai n! bertipe long
         */
        if (n <= 1)
        {
            return 1;
        }

        long result = 1;
        for (int i = 2; i <= n; i++)
        {
            result = result * i;
        }
        return result;
    }

    public static long computeRekursif(int n)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Fungsi  : computeRekursif
         * Tujuan  : Menghitung nilai faktorial secara rekursif sebagai pembanding
         * Param   : n - nilai ordinal yang dievaluasi
         * Return  : hasil perkalian rekursif n * (n-1)!
         */
        if (n <= 1)
        {
            return 1;
        }
        return n * computeRekursif(n - 1);
    }

    public static void tampilkanTabel(int n)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Prosedur : tampilkanTabel
         * Tujuan   : Menampilkan representasi tabular faktorial dari 0 hingga n
         */
        System.out.println("\n===== Tabel Faktorial 0 s.d. " + n + " =====");
        System.out.println("n\t| n!");
        System.out.println("--------------------------");

        for (int i = 0; i <= n; i++)
        {
            System.out.println(i + "\t| " + compute(i));
        }
    }

    public static void bandingkanMetode(int n)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Prosedur : bandingkanMetode
         * Tujuan   : Memverifikasi kesetaraan hasil komputasi iteratif vs rekursif
         */
        long hasilIteratif = compute(n);
        long hasilRekursif = computeRekursif(n);

        System.out.println("\nHasil iteratif  : " + hasilIteratif);
        System.out.println("Hasil rekursif  : " + hasilRekursif);

        if (hasilIteratif == hasilRekursif)
        {
            System.out.println("Kedua metode menghasilkan nilai yang sama.");
        }
        else
        {
            System.out.println("Hasil berbeda, ada yang salah!");
        }
    }

    public static int bacaBilangan(Scanner input)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Fungsi  : bacaBilangan
         * Tujuan  : Memvalidasi masukan angka pengguna dalam batas [0, BATAS_MAKS]
         * Return  : integer valid
         */
        int n = -1;
        boolean valid = false;

        while (!valid)
        {
            System.out.print("Masukkan bilangan (0-" + BATAS_MAKS + "): ");
            if (input.hasNextInt())
            {
                n = input.nextInt();
                if (n >= 0 && n <= BATAS_MAKS)
                {
                    valid = true;
                }
                else
                {
                    System.out.println("Bilangan di luar jangkauan, coba lagi!");
                }
            }
            else
            {
                System.out.println("Input harus berupa angka bulat!");
                input.next(); // eliminasi token non-angka
            }
        }
        return n;
    }

    public static void main(String[] args)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Titik masuk utama program (Console UI Loop)
         */
        Scanner input = new Scanner(System.in);
        int pilihan;

        do
        {
            System.out.println("\n===== KALKULATOR FAKTORIAL =====");
            System.out.println("1. Hitung faktorial satu bilangan");
            System.out.println("2. Tampilkan tabel faktorial");
            System.out.println("3. Bandingkan iteratif vs rekursif");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu: ");

            while (!input.hasNextInt())
            {
                System.out.print("Pilihan harus angka, ulangi: ");
                input.next();
            }
            pilihan = input.nextInt();

            switch (pilihan)
            {
                case 1:
                {
                    int a = bacaBilangan(input);
                    System.out.println(a + "! = " + compute(a));
                    break;
                }
                case 2:
                {
                    int b = bacaBilangan(input);
                    tampilkanTabel(b);
                    break;
                }
                case 3:
                {
                    int c = bacaBilangan(input);
                    bandingkanMetode(c);
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