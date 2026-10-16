/*
 * =========================================================================
 * Nama Berkas : PrimeChecker.java
 * Deskripsi   : Pengujian Bilangan Prima, Interval Rentang, dan Faktorisasi
 * Gaya Format : Allman Block Placement & Restructured Internal Docstrings
 * =========================================================================
 */

import java.util.Scanner;

public class PrimeChecker
{
    public boolean isPrime(int n)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Nama    : isPrime
         * Evaluasi: Pengujian faktor pembagi hingga batas akar n (O(sqrt(n)))
         * Return  : true jika n prima, false jika n <= 1 atau komposit
         */
        if (n <= 1)
        {
            return false;
        }

        for (int i = 2; i * i <= n; i++)
        {
            if (n % i == 0)
            {
                return false;
            }
        }
        return true;
    }

    public int tampilkanPrima(int awal, int akhir)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Menampilkan deret bilangan prima di dalam rentang tertutup [awal, akhir]
         * Return : total kemunculan bilangan prima
         */
        int jumlah = 0;
        System.out.print("Bilangan prima " + awal + " s.d. " + akhir + " : ");

        for (int i = awal; i <= akhir; i++)
        {
            if (isPrime(i))
            {
                System.out.print(i + " ");
                jumlah++;
            }
        }

        if (jumlah == 0)
        {
            System.out.print("(tidak ada)");
        }
        System.out.println();
        return jumlah;
    }

    public int primaBerikutnya(int n)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Melakukan pencarian linier bilangan prima terdekat yang lebih besar dari n
         */
        int kandidat = n + 1;
        while (!isPrime(kandidat))
        {
            kandidat++;
        }
        return kandidat;
    }

    public void faktorisasiPrima(int n)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Dekomposisi bilangan bulat menjadi perkalian faktor-faktor prima penyusunnya
         */
        if (n < 2)
        {
            System.out.println("Faktorisasi prima hanya untuk bilangan >= 2.");
            return;
        }

        System.out.print(n + " = ");
        int sisa = n;
        boolean pertama = true;

        for (int i = 2; i * i <= sisa; i++)
        {
            while (sisa % i == 0)
            {
                if (!pertama)
                {
                    System.out.print(" x ");
                }
                System.out.print(i);
                pertama = false;
                sisa = sisa / i;
            }
        }

        if (sisa > 1)
        {
            if (!pertama)
            {
                System.out.print(" x ");
            }
            System.out.print(sisa);
        }
        System.out.println();
    }

    public static int bacaAngka(Scanner input, String pesan)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Pembacaan nilai integer terminal dengan validasi tipe masukan
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
         * Prosedur utama antarmuka konsol menu pengecekan prima
         */
        Scanner input = new Scanner(System.in);
        PrimeChecker pengecek = new PrimeChecker();

        int pilihan;
        do
        {
            System.out.println("\n===== PROGRAM BILANGAN PRIMA =====");
            System.out.println("1. Cek apakah sebuah bilangan prima");
            System.out.println("2. Tampilkan prima dalam rentang");
            System.out.println("3. Cari prima berikutnya");
            System.out.println("4. Faktorisasi prima");
            System.out.println("0. Keluar");
            pilihan = bacaAngka(input, "Pilih menu: ");

            switch (pilihan)
            {
                case 1:
                {
                    int n = bacaAngka(input, "Masukkan bilangan: ");
                    if (pengecek.isPrime(n))
                    {
                        System.out.println(n + " adalah bilangan prima.");
                    }
                    else
                    {
                        System.out.println(n + " bukan bilangan prima.");
                    }
                    break;
                }
                case 2:
                {
                    int awal = bacaAngka(input, "Batas awal  : ");
                    int akhir = bacaAngka(input, "Batas akhir : ");
                    if (awal > akhir)
                    {
                        System.out.println("Batas awal tidak boleh lebih besar dari batas akhir!");
                    }
                    else
                    {
                        int jumlah = pengecek.tampilkanPrima(awal, akhir);
                        System.out.println("Jumlah bilangan prima : " + jumlah);
                    }
                    break;
                }
                case 3:
                {
                    int m = bacaAngka(input, "Masukkan bilangan: ");
                    System.out.println("Prima berikutnya setelah " + m + " adalah "
                            + pengecek.primaBerikutnya(m));
                    break;
                }
                case 4:
                {
                    int f = bacaAngka(input, "Masukkan bilangan: ");
                    pengecek.faktorisasiPrima(f);
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