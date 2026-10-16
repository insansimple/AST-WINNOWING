/*
 * =========================================================================
 * Nama Berkas : WordCounter.java
 * Deskripsi   : Analisis Morfologi Teks, Kepadatan Kata, dan Panjang Rata-rata
 * Tata Letak  : Allman Bracing Style dengan Dokumentasi Method Internal
 * =========================================================================
 */

import java.util.Scanner;

public class WordCounter
{
    public int countWords(String sentence)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Tokenisasi kalimat berdasarkan delimiter whitespace reguler
         * Return: total kata valid
         */
        if (sentence == null || sentence.trim().isEmpty())
        {
            return 0;
        }
        String[] words = sentence.trim().split("\\s+");
        return words.length;
    }

    public int hitungHuruf(String kalimat)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Menghitung total karakter alfanumerik bebas tanda baca dan spasi
         */
        int jumlah = 0;
        for (int i = 0; i < kalimat.length(); i++)
        {
            if (Character.isLetterOrDigit(kalimat.charAt(i)))
            {
                jumlah++;
            }
        }
        return jumlah;
    }

    public String kataTerpanjang(String kalimat)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Memindai kumpulan kata untuk mendapatkan token dengan jumlah huruf terbanyak
         */
        if (countWords(kalimat) == 0)
        {
            return "";
        }
        String[] kata = kalimat.trim().split("\\s+");
        String terpanjang = kata[0];

        for (int i = 1; i < kata.length; i++)
        {
            if (kata[i].length() > terpanjang.length())
            {
                terpanjang = kata[i];
            }
        }
        return terpanjang;
    }

    public int hitungFrekuensi(String kalimat, String target)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Menghitung berapa kali string target muncul dalam kalimat secara case-insensitive
         */
        if (countWords(kalimat) == 0)
        {
            return 0;
        }
        String[] kata = kalimat.trim().split("\\s+");
        int jumlah = 0;

        for (int i = 0; i < kata.length; i++)
        {
            if (kata[i].equalsIgnoreCase(target))
            {
                jumlah++;
            }
        }
        return jumlah;
    }

    public double rataRataPanjang(String kalimat)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Menghitung rasio panjang rata-rata per token kata
         */
        int jumlahKata = countWords(kalimat);
        if (jumlahKata == 0)
        {
            return 0;
        }

        String[] kata = kalimat.trim().split("\\s+");
        int totalPanjang = 0;

        for (int i = 0; i < kata.length; i++)
        {
            totalPanjang += kata[i].length();
        }
        return (double) totalPanjang / jumlahKata;
    }

    public void tampilkanDaftarKata(String kalimat)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Enumerasi seluruh token kata beserta panjang masing-masing karakter
         */
        if (countWords(kalimat) == 0)
        {
            System.out.println("Tidak ada kata.");
            return;
        }

        String[] kata = kalimat.trim().split("\\s+");
        for (int i = 0; i < kata.length; i++)
        {
            System.out.println((i + 1) + ". " + kata[i] + " (" + kata[i].length() + " karakter)");
        }
    }

    public static int bacaAngka(Scanner input, String pesan)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Membaca angka bulat dari keyboard dengan pembersihan baris
         */
        System.out.print(pesan);
        while (!input.hasNextInt())
        {
            System.out.print("Input harus angka bulat, ulangi: ");
            input.next();
        }
        int nilai = input.nextInt();
        input.nextLine();
        return nilai;
    }

    public static String bacaTeks(Scanner input, String pesan)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Membaca baris kalimat dari konsol pengguna
         */
        System.out.print(pesan);
        return input.nextLine();
    }

    public static void main(String[] args)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Prosedur kendali loop eksekusi penganalisis teks kata
         */
        Scanner input = new Scanner(System.in);
        WordCounter penghitung = new WordCounter();

        int pilihan;
        do
        {
            System.out.println("\n===== PROGRAM PENGHITUNG KATA =====");
            System.out.println("1. Hitung jumlah kata");
            System.out.println("2. Hitung jumlah huruf");
            System.out.println("3. Cari kata terpanjang");
            System.out.println("4. Hitung frekuensi sebuah kata");
            System.out.println("5. Rata-rata panjang kata");
            System.out.println("6. Tampilkan daftar kata");
            System.out.println("0. Keluar");
            pilihan = bacaAngka(input, "Pilih menu: ");

            switch (pilihan)
            {
                case 1:
                {
                    String k1 = bacaTeks(input, "Masukkan kalimat: ");
                    System.out.println("Jumlah kata : " + penghitung.countWords(k1));
                    break;
                }
                case 2:
                {
                    String k2 = bacaTeks(input, "Masukkan kalimat: ");
                    System.out.println("Jumlah huruf dan angka : " + penghitung.hitungHuruf(k2));
                    break;
                }
                case 3:
                {
                    String k3 = bacaTeks(input, "Masukkan kalimat: ");
                    String terpanjang = penghitung.kataTerpanjang(k3);
                    if (terpanjang.isEmpty())
                    {
                        System.out.println("Kalimat kosong, tidak ada kata.");
                    }
                    else
                    {
                        System.out.println("Kata terpanjang : " + terpanjang
                                + " (" + terpanjang.length() + " karakter)");
                    }
                    break;
                }
                case 4:
                {
                    String k4 = bacaTeks(input, "Masukkan kalimat: ");
                    String target = bacaTeks(input, "Kata yang dicari: ");
                    System.out.println("Kata \"" + target + "\" muncul "
                            + penghitung.hitungFrekuensi(k4, target) + " kali.");
                    break;
                }
                case 5:
                {
                    String k5 = bacaTeks(input, "Masukkan kalimat: ");
                    System.out.printf("Rata-rata panjang kata : %.2f%n",
                            penghitung.rataRataPanjang(k5));
                    break;
                }
                case 6:
                {
                    String k6 = bacaTeks(input, "Masukkan kalimat: ");
                    penghitung.tampilkanDaftarKata(k6);
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