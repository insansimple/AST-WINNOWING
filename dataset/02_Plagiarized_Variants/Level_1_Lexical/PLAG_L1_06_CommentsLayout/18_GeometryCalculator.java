/*
 * =========================================================================
 * Nama Berkas : GeometryCalculator.java
 * Deskripsi   : Komputasi Metrik Geometri Bangun Datar 2 Dimensi
 * Format Gaya : Allman Bracing Convention & Restructured Internal Method Docs
 * =========================================================================
 */

import java.util.Scanner;

public class GeometryCalculator
{
    // Tetapan nilai pi untuk kalkulasi sirkular
    static final double PI = 3.14159265358979;

    public double calculateArea(double length, double width)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Komputasi luas dan keliling bangun persegi panjang
         * Return : luas persegi panjang (p x l)
         */
        double area = length * width;
        double perimeter = 2 * (length + width);
        System.out.println("Perimeter: " + perimeter);
        return area;
    }

    public double luasPersegi(double sisi)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Formula luas bujur sangkar (sisi * sisi)
         */
        return sisi * sisi;
    }

    public double kelilingPersegi(double sisi)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Formula keliling bujur sangkar (4 * sisi)
         */
        return 4 * sisi;
    }

    public double luasSegitiga(double alas, double tinggi)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Formula luas segitiga (0.5 * alas * tinggi)
         */
        return 0.5 * alas * tinggi;
    }

    public double kelilingSegitiga(double a, double b, double c)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Penjumlahan keliling ketiga sisi segitiga
         */
        return a + b + c;
    }

    public boolean segitigaValid(double a, double b, double c)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Teorema pertidaksamaan segitiga (triangle inequality theorem)
         */
        return (a + b > c) && (a + c > b) && (b + c > a);
    }

    public double luasLingkaran(double jariJari)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Formula luas lingkaran: pi * r^2
         */
        return PI * jariJari * jariJari;
    }

    public double kelilingLingkaran(double jariJari)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Formula keliling lingkaran: 2 * pi * r
         */
        return 2 * PI * jariJari;
    }

    public static double bacaDesimal(Scanner input, String pesan)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Pembacaan double scanner bersanitasi
         */
        System.out.print(pesan);
        while (!input.hasNextDouble())
        {
            System.out.print("Input harus berupa angka, ulangi: ");
            input.next();
        }
        return input.nextDouble();
    }

    public static double bacaPositif(Scanner input, String pesan)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Memastikan dimensi ukuran bernilai strictly positive (> 0)
         */
        double nilai = 0;
        while (nilai <= 0)
        {
            nilai = bacaDesimal(input, pesan);
            if (nilai <= 0)
            {
                System.out.println("Nilai harus lebih dari 0!");
            }
        }
        return nilai;
    }

    public static void main(String[] args)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Prosedur kendali antarmuka pemilihan geometri konsol
         */
        Scanner input = new Scanner(System.in);
        GeometryCalculator kalkulator = new GeometryCalculator();

        int pilihan;
        do
        {
            System.out.println("\n===== PROGRAM BANGUN DATAR =====");
            System.out.println("1. Persegi panjang");
            System.out.println("2. Persegi");
            System.out.println("3. Segitiga");
            System.out.println("4. Lingkaran");
            System.out.println("0. Keluar");
            pilihan = (int) bacaDesimal(input, "Pilih menu: ");

            switch (pilihan)
            {
                case 1:
                {
                    double panjang = bacaPositif(input, "Panjang : ");
                    double lebar = bacaPositif(input, "Lebar   : ");
                    double luas1 = kalkulator.calculateArea(panjang, lebar);
                    System.out.println("Luas persegi panjang : " + luas1);
                    break;
                }
                case 2:
                {
                    double sisi = bacaPositif(input, "Panjang sisi : ");
                    System.out.println("Luas persegi    : " + kalkulator.luasPersegi(sisi));
                    System.out.println("Keliling persegi : " + kalkulator.kelilingPersegi(sisi));
                    break;
                }
                case 3:
                {
                    double sisiA = bacaPositif(input, "Sisi A  : ");
                    double sisiB = bacaPositif(input, "Sisi B  : ");
                    double sisiC = bacaPositif(input, "Sisi C  : ");
                    if (kalkulator.segitigaValid(sisiA, sisiB, sisiC))
                    {
                        double alas = bacaPositif(input, "Alas   : ");
                        double tinggi = bacaPositif(input, "Tinggi : ");
                        System.out.println("Luas segitiga    : "
                                + kalkulator.luasSegitiga(alas, tinggi));
                        System.out.println("Keliling segitiga : "
                                + kalkulator.kelilingSegitiga(sisiA, sisiB, sisiC));
                    }
                    else
                    {
                        System.out.println("Ketiga sisi tidak dapat membentuk segitiga!");
                    }
                    break;
                }
                case 4:
                {
                    double r = bacaPositif(input, "Jari-jari : ");
                    System.out.printf("Luas lingkaran    : %.2f%n", kalkulator.luasLingkaran(r));
                    System.out.printf("Keliling lingkaran : %.2f%n", kalkulator.kelilingLingkaran(r));
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