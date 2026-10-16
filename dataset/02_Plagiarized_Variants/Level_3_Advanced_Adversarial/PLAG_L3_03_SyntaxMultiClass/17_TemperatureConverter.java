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
    static final double NOL_MUTLAK = -273.15;

    // Inner static class wrapper untuk nilai suhu
    public static class TemperatureReading {
        private double value;

        public TemperatureReading(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }
    }

    // Inner static class wrapper untuk spesifikasi rentang tabel konversi
    public static class ConversionRange {
        private double start;
        private double end;
        private double step;

        public ConversionRange(double start, double end, double step) {
            this.start = start;
            this.end = end;
            this.step = step;
        }

        public double getStart() {
            return start;
        }

        public double getEnd() {
            return end;
        }

        public double getStep() {
            return step;
        }

        public boolean isValid(double absoluteZero) {
            return step > 0 && start <= end && start >= absoluteZero;
        }
    }

    // Logika inti: Celsius ke Fahrenheit
    public double celsiusToFahrenheit(double c) {
        TemperatureReading reading = new TemperatureReading(c);
        return (reading.getValue() * 9.0 / 5.0) + 32.0;
    }

    // Logika inti: Fahrenheit ke Celsius
    public double fahrenheitToCelsius(double f) {
        TemperatureReading reading = new TemperatureReading(f);
        return (reading.getValue() - 32.0) * 5.0 / 9.0;
    }

    // Logika inti: Celsius ke Kelvin
    public double celsiusToKelvin(double c) {
        TemperatureReading reading = new TemperatureReading(c);
        return reading.getValue() + 273.15;
    }

    // Kelvin ke Celsius
    public double kelvinToCelsius(double k) {
        TemperatureReading reading = new TemperatureReading(k);
        return reading.getValue() - 273.15;
    }

    // Celsius ke Reamur
    public double celsiusToReamur(double c) {
        TemperatureReading reading = new TemperatureReading(c);
        return reading.getValue() * 4.0 / 5.0;
    }

    // Reamur ke Celsius
    public double reamurToCelsius(double r) {
        TemperatureReading reading = new TemperatureReading(r);
        return reading.getValue() * 5.0 / 4.0;
    }

    // Mengecek apakah suhu Celsius masih di atas atau sama dengan nol mutlak
    public boolean suhuValid(double c) {
        TemperatureReading reading = new TemperatureReading(c);
        return reading.getValue() >= NOL_MUTLAK;
    }

    // Menentukan wujud air pada tekanan 1 atm berdasarkan suhu Celsius
    public String wujudAir(double c) {
        TemperatureReading reading = new TemperatureReading(c);
        if (reading.getValue() <= 0) {
            return "Padat (es)";
        } else if (reading.getValue() < 100) {
            return "Cair";
        } else {
            return "Gas (uap)";
        }
    }

    // Menampilkan semua konversi dari satu nilai Celsius
    public void tampilkanSemua(double c) {
        TemperatureReading reading = new TemperatureReading(c);
        System.out.println("\nSuhu awal : " + reading.getValue() + " C");
        System.out.printf("Fahrenheit : %.2f F%n", celsiusToFahrenheit(reading.getValue()));
        System.out.printf("Kelvin     : %.2f K%n", celsiusToKelvin(reading.getValue()));
        System.out.printf("Reamur     : %.2f R%n", celsiusToReamur(reading.getValue()));
        System.out.println("Wujud air  : " + wujudAir(reading.getValue()));
    }

    // Menampilkan tabel konversi Celsius ke Fahrenheit dengan langkah tertentu
    public void tampilkanTabel(double awal, double akhir, double langkah) {
        ConversionRange range = new ConversionRange(awal, akhir, langkah);
        System.out.println("\n  Celsius | Fahrenheit");
        System.out.println("-----------------------");
        for (double c = range.getStart(); c <= range.getEnd(); c += range.getStep()) {
            System.out.printf("%9.1f | %9.1f%n", c, celsiusToFahrenheit(c));
        }
    }

    // Membaca bilangan desimal dengan validasi sederhana
    public static double bacaDesimal(Scanner input, String pesan) {
        System.out.print(pesan);
        while (!input.hasNextDouble()) {
            System.out.print("Input harus berupa angka, ulangi: ");
            input.next();
        }
        return input.nextDouble();
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        TemperatureConverter konverter = new TemperatureConverter();

        int pilihan = 0;
        do {
            System.out.println("\n===== PROGRAM KONVERSI SUHU =====");
            System.out.println("1. Celsius ke Fahrenheit");
            System.out.println("2. Fahrenheit ke Celsius");
            System.out.println("3. Celsius ke Kelvin");
            System.out.println("4. Kelvin ke Celsius");
            System.out.println("5. Celsius ke semua satuan");
            System.out.println("6. Tabel Celsius ke Fahrenheit");
            System.out.println("0. Keluar");
            pilihan = (int) bacaDesimal(input, "Pilih menu: ");

            switch (pilihan) {
                case 1:
                    double c1 = bacaDesimal(input, "Masukkan suhu (C): ");
                    if (konverter.suhuValid(c1)) {
                        System.out.printf("%.2f C = %.2f F%n", c1,
                                konverter.celsiusToFahrenheit(c1));
                    } else {
                        System.out.println("Suhu di bawah nol mutlak!");
                    }
                    break;
                case 2:
                    double f2 = bacaDesimal(input, "Masukkan suhu (F): ");
                    double c2 = konverter.fahrenheitToCelsius(f2);
                    if (konverter.suhuValid(c2)) {
                        System.out.printf("%.2f F = %.2f C%n", f2, c2);
                    } else {
                        System.out.println("Suhu di bawah nol mutlak!");
                    }
                    break;
                case 3:
                    double c3 = bacaDesimal(input, "Masukkan suhu (C): ");
                    if (konverter.suhuValid(c3)) {
                        System.out.printf("%.2f C = %.2f K%n", c3,
                                konverter.celsiusToKelvin(c3));
                    } else {
                        System.out.println("Suhu di bawah nol mutlak!");
                    }
                    break;
                case 4:
                    double k4 = bacaDesimal(input, "Masukkan suhu (K): ");
                    if (k4 >= 0) {
                        System.out.printf("%.2f K = %.2f C%n", k4,
                                konverter.kelvinToCelsius(k4));
                    } else {
                        System.out.println("Kelvin tidak boleh negatif!");
                    }
                    break;
                case 5:
                    double c5 = bacaDesimal(input, "Masukkan suhu (C): ");
                    if (konverter.suhuValid(c5)) {
                        konverter.tampilkanSemua(c5);
                    } else {
                        System.out.println("Suhu di bawah nol mutlak!");
                    }
                    break;
                case 6:
                    double awal = bacaDesimal(input, "Suhu awal (C)  : ");
                    double akhir = bacaDesimal(input, "Suhu akhir (C) : ");
                    double langkah = bacaDesimal(input, "Langkah        : ");
                    ConversionRange range = new ConversionRange(awal, akhir, langkah);
                    if (!range.isValid(NOL_MUTLAK)) {
                        System.out.println("Rentang atau langkah tidak valid!");
                    } else {
                        konverter.tampilkanTabel(range.getStart(), range.getEnd(), range.getStep());
                    }
                    break;
                case 0:
                    System.out.println("Terima kasih, program selesai.");
                    break;
                default:
                    System.out.println("Menu tidak tersedia!");
            }
        } while (pilihan != 0);

        input.close();
    }
}