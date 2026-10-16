/*
 * =========================================================================
 * Nama Berkas : TemperatureConverter.java
 * Deskripsi   : Konversi Skala Termometrik (Celsius, Fahrenheit, Kelvin, Reamur)
 * Gaya Format : Allman Bracing Style dengan Dokumentasi Method Internal
 * =========================================================================
 */

import java.util.Scanner;

public class TemperatureConverter
{
    // Batas termodinamika terendah (nol mutlak) pada skala Celsius
    static final double NOL_MUTLAK = -273.15;

    public double celsiusToFahrenheit(double c)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Formula konversi: F = (C * 9/5) + 32
         */
        return (c * 9.0 / 5.0) + 32.0;
    }

    public double fahrenheitToCelsius(double f)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Formula konversi: C = (F - 32) * 5/9
         */
        return (f - 32.0) * 5.0 / 9.0;
    }

    public double celsiusToKelvin(double c)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Formula konversi: K = C + 273.15
         */
        return c + 273.15;
    }

    public double kelvinToCelsius(double k)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Formula konversi: C = K - 273.15
         */
        return k - 273.15;
    }

    public double celsiusToReamur(double c)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Formula konversi: R = C * 4/5
         */
        return c * 4.0 / 5.0;
    }

    public double reamurToCelsius(double r)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Formula konversi: C = R * 5/4
         */
        return r * 5.0 / 4.0;
    }

    public boolean suhuValid(double c)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Validasi batas fisik: suhu tidak boleh berada di bawah nol mutlak
         */
        return c >= NOL_MUTLAK;
    }

    public String wujudAir(double c)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Identifikasi fase wujud zat air pada tekanan standar 1 atmosfer
         */
        if (c <= 0)
        {
            return "Padat (es)";
        }
        else if (c < 100)
        {
            return "Cair";
        }
        else
        {
            return "Gas (uap)";
        }
    }

    public void tampilkanSemua(double c)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Kompilasi seluruh konversi skala suhu dari inputan Celsius
         */
        System.out.println("\nSuhu awal : " + c + " C");
        System.out.printf("Fahrenheit : %.2f F%n", celsiusToFahrenheit(c));
        System.out.printf("Kelvin     : %.2f K%n", celsiusToKelvin(c));
        System.out.printf("Reamur     : %.2f R%n", celsiusToReamur(c));
        System.out.println("Wujud air  : " + wujudAir(c));
    }

    public void tampilkanTabel(double awal, double akhir, double langkah)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Membuat tabel korespondensi nilai suhu Celsius vs Fahrenheit
         */
        System.out.println("\n  Celsius | Fahrenheit");
        System.out.println("-----------------------");
        for (double c = awal; c <= akhir; c += langkah)
        {
            System.out.printf("%9.1f | %9.1f%n", c, celsiusToFahrenheit(c));
        }
    }

    public static double bacaDesimal(Scanner input, String pesan)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Input scanner parsing untuk bilangan pecahan double
         */
        System.out.print(pesan);
        while (!input.hasNextDouble())
        {
            System.out.print("Input harus berupa angka, ulangi: ");
            input.next();
        }
        return input.nextDouble();
    }

    public static void main(String[] args)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Handler interaksi menu CLI konversi suhu
         */
        Scanner input = new Scanner(System.in);
        TemperatureConverter konverter = new TemperatureConverter();

        int pilihan;
        do
        {
            System.out.println("\n===== PROGRAM KONVERSI SUHU =====");
            System.out.println("1. Celsius ke Fahrenheit");
            System.out.println("2. Fahrenheit ke Celsius");
            System.out.println("3. Celsius ke Kelvin");
            System.out.println("4. Kelvin ke Celsius");
            System.out.println("5. Celsius ke semua satuan");
            System.out.println("6. Tabel Celsius ke Fahrenheit");
            System.out.println("0. Keluar");
            pilihan = (int) bacaDesimal(input, "Pilih menu: ");

            switch (pilihan)
            {
                case 1:
                {
                    double c1 = bacaDesimal(input, "Masukkan suhu (C): ");
                    if (konverter.suhuValid(c1))
                    {
                        System.out.printf("%.2f C = %.2f F%n", c1,
                                konverter.celsiusToFahrenheit(c1));
                    }
                    else
                    {
                        System.out.println("Suhu di bawah nol mutlak!");
                    }
                    break;
                }
                case 2:
                {
                    double f2 = bacaDesimal(input, "Masukkan suhu (F): ");
                    double c2 = konverter.fahrenheitToCelsius(f2);
                    if (konverter.suhuValid(c2))
                    {
                        System.out.printf("%.2f F = %.2f C%n", f2, c2);
                    }
                    else
                    {
                        System.out.println("Suhu di bawah nol mutlak!");
                    }
                    break;
                }
                case 3:
                {
                    double c3 = bacaDesimal(input, "Masukkan suhu (C): ");
                    if (konverter.suhuValid(c3))
                    {
                        System.out.printf("%.2f C = %.2f K%n", c3,
                                konverter.celsiusToKelvin(c3));
                    }
                    else
                    {
                        System.out.println("Suhu di bawah nol mutlak!");
                    }
                    break;
                }
                case 4:
                {
                    double k4 = bacaDesimal(input, "Masukkan suhu (K): ");
                    if (k4 >= 0)
                    {
                        System.out.printf("%.2f K = %.2f C%n", k4,
                                konverter.kelvinToCelsius(k4));
                    }
                    else
                    {
                        System.out.println("Kelvin tidak boleh negatif!");
                    }
                    break;
                }
                case 5:
                {
                    double c5 = bacaDesimal(input, "Masukkan suhu (C): ");
                    if (konverter.suhuValid(c5))
                    {
                        konverter.tampilkanSemua(c5);
                    }
                    else
                    {
                        System.out.println("Suhu di bawah nol mutlak!");
                    }
                    break;
                }
                case 6:
                {
                    double awal = bacaDesimal(input, "Suhu awal (C)  : ");
                    double akhir = bacaDesimal(input, "Suhu akhir (C) : ");
                    double langkah = bacaDesimal(input, "Langkah        : ");
                    if (langkah <= 0 || awal > akhir || !konverter.suhuValid(awal))
                    {
                        System.out.println("Rentang atau langkah tidak valid!");
                    }
                    else
                    {
                        konverter.tampilkanTabel(awal, akhir, langkah);
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