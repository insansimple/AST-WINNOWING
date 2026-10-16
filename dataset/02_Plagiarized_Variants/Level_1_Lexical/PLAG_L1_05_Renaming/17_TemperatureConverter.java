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
    static final double TITIK_TERENDAH = -273.15;

    // Logika inti: Celsius ke Fahrenheit
    public double konversiCkeF(double derajat) {
        return (derajat * 9.0 / 5.0) + 32.0;
    }

    // Logika inti: Fahrenheit ke Celsius
    public double konversiFkeC(double derajat) {
        return (derajat - 32.0) * 5.0 / 9.0;
    }

    // Logika inti: Celsius ke Kelvin
    public double konversiCkeK(double derajat) {
        return derajat + 273.15;
    }

    // Kelvin ke Celsius
    public double konversiKkeC(double derajat) {
        return derajat - 273.15;
    }

    // Celsius ke Reamur
    public double konversiCkeR(double derajat) {
        return derajat * 4.0 / 5.0;
    }

    // Reamur ke Celsius
    public double konversiRkeC(double derajat) {
        return derajat * 5.0 / 4.0;
    }

    // Mengecek apakah suhu Celsius masih di atas atau sama dengan nol mutlak
    public boolean layakSuhu(double derajat) {
        return derajat >= TITIK_TERENDAH;
    }

    // Menentukan wujud air pada tekanan 1 atm berdasarkan suhu Celsius
    public String tentukanWujudAir(double derajat) {
        if (derajat <= 0) {
            return "Padat (es)";
        } else if (derajat < 100) {
            return "Cair";
        } else {
            return "Gas (uap)";
        }
    }

    // Menampilkan semua konversi dari satu nilai Celsius
    public void cetakSemuaKonversi(double derajat) {
        System.out.print(new StringBuilder().append("\nSuhu awal : ").append(derajat).append(" C").toString());
        System.out.print(System.lineSeparator());
        System.out.printf("Fahrenheit : %.2f F%n", konversiCkeF(derajat));
        System.out.printf("Kelvin     : %.2f K%n", konversiCkeK(derajat));
        System.out.printf("Reamur     : %.2f R%n", konversiCkeR(derajat));
        System.out.print(new StringBuilder().append("Wujud air  : ").append(tentukanWujudAir(derajat)).toString());
        System.out.print(System.lineSeparator());
    }

    // Menampilkan tabel konversi Celsius ke Fahrenheit dengan langkah tertentu
    public void cetakTabelKonversi(double mulai, double selesai, double selisih) {
        System.out.print("\n  Celsius | Fahrenheit");
        System.out.print(System.lineSeparator());
        System.out.print("-----------------------");
        System.out.print(System.lineSeparator());
        for (double derajat = mulai; derajat <= selesai; derajat += selisih) {
            System.out.printf("%9.1f | %9.1f%n", derajat, konversiCkeF(derajat));
        }
    }

    // Membaca bilangan desimal dengan validasi sederhana
    public static double ambilDesimal(Scanner pemindai, String keterangan) {
        System.out.print(keterangan);
        while (!pemindai.hasNextDouble()) {
            System.out.print("Input harus berupa angka, ulangi: ");
            pemindai.next();
        }
        return pemindai.nextDouble();
    }

    public static void main(String[] args) {
        Scanner pemindai = new Scanner(System.in);
        TemperatureConverter pengubah = new TemperatureConverter();

        int opsi;
        do {
            System.out.print("\n===== PROGRAM KONVERSI SUHU =====");
            System.out.print(System.lineSeparator());
            System.out.print("1. Celsius ke Fahrenheit");
            System.out.print(System.lineSeparator());
            System.out.print("2. Fahrenheit ke Celsius");
            System.out.print(System.lineSeparator());
            System.out.print("3. Celsius ke Kelvin");
            System.out.print(System.lineSeparator());
            System.out.print("4. Kelvin ke Celsius");
            System.out.print(System.lineSeparator());
            System.out.print("5. Celsius ke semua satuan");
            System.out.print(System.lineSeparator());
            System.out.print("6. Tabel Celsius ke Fahrenheit");
            System.out.print(System.lineSeparator());
            System.out.print("0. Keluar");
            System.out.print(System.lineSeparator());
            opsi = (int) ambilDesimal(pemindai, "Pilih menu: ");

            switch (opsi) {
                case 1:
                    double s1 = ambilDesimal(pemindai, "Masukkan suhu (C): ");
                    if (pengubah.layakSuhu(s1)) {
                        System.out.printf("%.2f C = %.2f F%n", s1,
                                pengubah.konversiCkeF(s1));
                    } else {
                        System.out.print("Suhu di bawah nol mutlak!");
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 2:
                    double fh2 = ambilDesimal(pemindai, "Masukkan suhu (F): ");
                    double cl2 = pengubah.konversiFkeC(fh2);
                    if (pengubah.layakSuhu(cl2)) {
                        System.out.printf("%.2f F = %.2f C%n", fh2, cl2);
                    } else {
                        System.out.print("Suhu di bawah nol mutlak!");
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 3:
                    double s3 = ambilDesimal(pemindai, "Masukkan suhu (C): ");
                    if (pengubah.layakSuhu(s3)) {
                        System.out.printf("%.2f C = %.2f K%n", s3,
                                pengubah.konversiCkeK(s3));
                    } else {
                        System.out.print("Suhu di bawah nol mutlak!");
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 4:
                    double kv4 = ambilDesimal(pemindai, "Masukkan suhu (K): ");
                    if (kv4 >= 0) {
                        System.out.printf("%.2f K = %.2f C%n", kv4,
                                pengubah.konversiKkeC(kv4));
                    } else {
                        System.out.print("Kelvin tidak boleh negatif!");
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 5:
                    double s5 = ambilDesimal(pemindai, "Masukkan suhu (C): ");
                    if (pengubah.layakSuhu(s5)) {
                        pengubah.cetakSemuaKonversi(s5);
                    } else {
                        System.out.print("Suhu di bawah nol mutlak!");
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 6:
                    double suhuMulai = ambilDesimal(pemindai, "Suhu awal (C)  : ");
                    double suhuSelesai = ambilDesimal(pemindai, "Suhu akhir (C) : ");
                    double selisihLangkah = ambilDesimal(pemindai, "Langkah        : ");
                    if (selisihLangkah <= 0 || suhuMulai > suhuSelesai || !pengubah.layakSuhu(suhuMulai)) {
                        System.out.print("Rentang atau langkah tidak valid!");
                        System.out.print(System.lineSeparator());
                    } else {
                        pengubah.cetakTabelKonversi(suhuMulai, suhuSelesai, selisihLangkah);
                    }
                    break;
                case 0:
                    System.out.print("Terima kasih, program selesai.");
                    System.out.print(System.lineSeparator());
                    break;
                default:
                    System.out.print("Menu tidak tersedia!");
                    System.out.print(System.lineSeparator());
            }
        } while (opsi != 0);

        pemindai.close();
    }
}