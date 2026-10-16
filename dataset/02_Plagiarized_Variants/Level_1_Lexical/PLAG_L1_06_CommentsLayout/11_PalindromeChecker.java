/*
 * =========================================================================
 * Nama Berkas : PalindromeChecker.java
 * Deskripsi   : Validasi Pola Simetri Palindrom (String, Kalimat, dan Angka)
 * Tata Letak  : Allman Bracing Style dengan Dokumentasi Method Internal
 * =========================================================================
 */

import java.util.Scanner;

public class PalindromeChecker
{
    public boolean isPalindrome(String str)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Evaluasi kesimetrisan karakter dua arah (two-pointer technique)
         */
        String cleaned = str.toLowerCase();
        int left = 0;
        int right = cleaned.length() - 1;

        while (left < right)
        {
            if (cleaned.charAt(left) != cleaned.charAt(right))
            {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    public String bersihkanTeks(String teks)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Filter string: hanya mempertahankan karakter alfanumerik
         */
        String hasil = "";
        for (int i = 0; i < teks.length(); i++)
        {
            char c = teks.charAt(i);
            if (Character.isLetterOrDigit(c))
            {
                hasil += c;
            }
        }
        return hasil;
    }

    public boolean isPalindromeKalimat(String kalimat)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Pengujian palindrom kalimat bebas spasi dan tanda baca
         */
        String bersih = bersihkanTeks(kalimat);
        return isPalindrome(bersih);
    }

    public String balikTeks(String teks)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Pembalikan urutan karakter string dari indeks akhir ke awal
         */
        String hasil = "";
        for (int i = teks.length() - 1; i >= 0; i--)
        {
            hasil += teks.charAt(i);
        }
        return hasil;
    }

    public boolean isPalindromeAngka(int angka)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Verifikasi palindrom bilangan integer berbasis ekstraksi modulo digit
         */
        if (angka < 0)
        {
            return false;
        }

        int asli = angka;
        int terbalik = 0;

        while (angka > 0)
        {
            int digit = angka % 10;
            terbalik = terbalik * 10 + digit;
            angka = angka / 10;
        }
        return asli == terbalik;
    }

    public int hitungHurufAngka(String teks)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Menghitung total karakter alfanumerik yang ada pada teks
         */
        return bersihkanTeks(teks).length();
    }

    public static int bacaAngka(Scanner input, String pesan)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Pembacaan integer scanner aman disertai pembersihan baris baru
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
         * Pembacaan baris teks lengkap (termasuk spasi)
         */
        System.out.print(pesan);
        return input.nextLine();
    }

    public static void main(String[] args)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Menu interaktif aplikasi verifikasi palindrom
         */
        Scanner input = new Scanner(System.in);
        PalindromeChecker pengecek = new PalindromeChecker();

        int pilihan;
        do
        {
            System.out.println("\n===== PROGRAM PALINDROM =====");
            System.out.println("1. Cek palindrom kata");
            System.out.println("2. Cek palindrom kalimat");
            System.out.println("3. Cek palindrom angka");
            System.out.println("4. Balik teks");
            System.out.println("0. Keluar");
            pilihan = bacaAngka(input, "Pilih menu: ");

            switch (pilihan)
            {
                case 1:
                {
                    String kata = bacaTeks(input, "Masukkan kata: ");
                    if (pengecek.isPalindrome(kata))
                    {
                        System.out.println("\"" + kata + "\" adalah palindrom.");
                    }
                    else
                    {
                        System.out.println("\"" + kata + "\" bukan palindrom.");
                    }
                    break;
                }
                case 2:
                {
                    String kalimat = bacaTeks(input, "Masukkan kalimat: ");
                    System.out.println("Jumlah huruf dan angka : " + pengecek.hitungHurufAngka(kalimat));
                    if (pengecek.isPalindromeKalimat(kalimat))
                    {
                        System.out.println("Kalimat tersebut adalah palindrom.");
                    }
                    else
                    {
                        System.out.println("Kalimat tersebut bukan palindrom.");
                    }
                    break;
                }
                case 3:
                {
                    int angka = bacaAngka(input, "Masukkan angka: ");
                    if (pengecek.isPalindromeAngka(angka))
                    {
                        System.out.println(angka + " adalah palindrom.");
                    }
                    else
                    {
                        System.out.println(angka + " bukan palindrom.");
                    }
                    break;
                }
                case 4:
                {
                    String teks = bacaTeks(input, "Masukkan teks: ");
                    System.out.println("Hasil dibalik : " + pengecek.balikTeks(teks));
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