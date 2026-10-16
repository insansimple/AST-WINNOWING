/*
 * Program   : TemperatureConverter
 * Deskripsi : Mengonversi suhu antara Celsius, Fahrenheit, Kelvin, dan
 *             Reamur. Dilengkapi validasi suhu di bawah nol mutlak,
 *             tabel konversi, klasifikasi suhu air, input dari pengguna,
 *             dan menu sederhana.
 */
import java.util.Scanner;

public class TemperatureConverter {

    // Batas suhu terendah (nol mutlak) dalam Celsius
    static final double BATAS_MINIMUM = -273.15;

    // Logika inti: Celsius ke Fahrenheit
    public double cToF(double cel) {
        return (cel * 9.0 / 5.0) + 32.0;
    }

    // Logika inti: Fahrenheit ke Celsius
    public double fToC(double fah) {
        return (fah - 32.0) * 5.0 / 9.0;
    }

    // Logika inti: Celsius ke Kelvin
    public double cToK(double cel) {
        return cel + 273.15;
    }

    // Kelvin ke Celsius
    public double kToC(double kel) {
        return kel - 273.15;
    }

    // Celsius ke Reamur
    public double cToR(double cel) {
        return cel * 4.0 / 5.0;
    }

    // Reamur ke Celsius
    public double rToC(double rea) {
        return rea * 5.0 / 4.0;
    }

    // Mengecek apakah suhu Celsius masih di atas atau sama dengan nol mutlak
    public boolean suhuSah(double cel) {
        return cel >= BATAS_MINIMUM;
    }

    // Menentukan wujud air pada tekanan 1 atm berdasarkan suhu Celsius
    public String wujudAir(double cel) {
        if (cel <= 0) {
            return "Padat (es)";
        } else if (cel < 100) {
            return "Cair";
        } else {
            return "Gas (uap)";
        }
    }

    // Menampilkan semua konversi dari satu nilai Celsius
    public void konversiSemua(double cel) {
        System.out.println("\nSuhu awal : " + cel + " C");
        System.out.printf("Fahrenheit : %.2f F%n", cToF(cel));
        System.out.printf("Kelvin     : %.2f K%n", cToK(cel));
        System.out.printf("Reamur     : %.2f R%n", cToR(cel));
        System.out.println("Wujud air  : " + wujudAir(cel));
    }

    // Menampilkan tabel konversi Celsius ke Fahrenheit dengan langkah tertentu
    public void cetakTabel(double mulai, double selesai, double selang) {
        System.out.println("\n  Celsius | Fahrenheit");
        System.out.println("-----------------------");
        for (double cel = mulai; cel <= selesai; cel += selang) {
            System.out.printf("%9.1f | %9.1f%n", cel, cToF(cel));
        }
    }

    // Membaca bilangan desimal dengan validasi sederhana
    public static double ambilDesimal(Scanner scanner, String teks) {
        System.out.print(teks);
        while (!scanner.hasNextDouble()) {
            System.out.print("Input harus berupa angka, ulangi: ");
            scanner.next();
        }
        return scanner.nextDouble();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TemperatureConverter alat = new TemperatureConverter();

        int menu;
        do {
            System.out.println("\n===== PROGRAM KONVERSI SUHU =====");
            System.out.println("1. Celsius ke Fahrenheit");
            System.out.println("2. Fahrenheit ke Celsius");
            System.out.println("3. Celsius ke Kelvin");
            System.out.println("4. Kelvin ke Celsius");
            System.out.println("5. Celsius ke semua satuan");
            System.out.println("6. Tabel Celsius ke Fahrenheit");
            System.out.println("0. Keluar");
            menu = (int) ambilDesimal(scanner, "Pilih menu: ");

            switch (menu) {
                case 1:
                    double suhu1 = ambilDesimal(scanner, "Masukkan suhu (C): ");
                    if (alat.suhuSah(suhu1)) {
                        System.out.printf("%.2f C = %.2f F%n", suhu1,
                                alat.cToF(suhu1));
                    } else {
                        System.out.println("Suhu di bawah nol mutlak!");
                    }
                    break;
                case 2:
                    double suhu2 = ambilDesimal(scanner, "Masukkan suhu (F): ");
                    double cel2 = alat.fToC(suhu2);
                    if (alat.suhuSah(cel2)) {
                        System.out.printf("%.2f F = %.2f C%n", suhu2, cel2);
                    } else {
                        System.out.println("Suhu di bawah nol mutlak!");
                    }
                    break;
                case 3:
                    double suhu3 = ambilDesimal(scanner, "Masukkan suhu (C): ");
                    if (alat.suhuSah(suhu3)) {
                        System.out.printf("%.2f C = %.2f K%n", suhu3,
                                alat.cToK(suhu3));
                    } else {
                        System.out.println("Suhu di bawah nol mutlak!");
                    }
                    break;
                case 4:
                    double suhu4 = ambilDesimal(scanner, "Masukkan suhu (K): ");
                    if (suhu4 >= 0) {
                        System.out.printf("%.2f K = %.2f C%n", suhu4,
                                alat.kToC(suhu4));
                    } else {
                        System.out.println("Kelvin tidak boleh negatif!");
                    }
                    break;
                case 5:
                    double suhu5 = ambilDesimal(scanner, "Masukkan suhu (C): ");
                    if (alat.suhuSah(suhu5)) {
                        alat.konversiSemua(suhu5);
                    } else {
                        System.out.println("Suhu di bawah nol mutlak!");
                    }
                    break;
                case 6:
                    double mulai = ambilDesimal(scanner, "Suhu awal (C)  : ");
                    double selesai = ambilDesimal(scanner, "Suhu akhir (C) : ");
                    double selang = ambilDesimal(scanner, "Langkah        : ");
                    if (selang <= 0 || mulai > selesai || !alat.suhuSah(mulai)) {
                        System.out.println("Rentang atau langkah tidak valid!");
                    } else {
                        alat.cetakTabel(mulai, selesai, selang);
                    }
                    break;
                case 0:
                    System.out.println("Terima kasih, program selesai.");
                    break;
                default:
                    System.out.println("Menu tidak tersedia!");
            }
        } while (menu != 0);

        scanner.close();
    }
}