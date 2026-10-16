/*
 * Program   : GeometryCalculator
 * Deskripsi : Menghitung luas dan keliling bangun datar. Dilengkapi
 *             persegi panjang, persegi, segitiga, lingkaran, validasi
 *             input bernilai positif, input dari pengguna, dan menu
 *             sederhana.
 */
import java.util.Scanner;

public class GeometryCalculator {

    // Nilai konstanta pi untuk perhitungan lingkaran
    static final double KONSTANTA_PI = 3.14159265358979;

    // Logika inti: menghitung luas persegi panjang dan menampilkan kelilingnya
    public double hitungLuas(double pjg, double lbr) {
        double luasnya = pjg * lbr;
        double kelilingnya = 2 * (pjg + lbr);
        System.out.println("Perimeter: " + kelilingnya);
        return luasnya;
    }

    // Menghitung luas persegi
    public double luasPersegi(double tepi) {
        return tepi * tepi;
    }

    // Menghitung keliling persegi
    public double kelilingPersegi(double tepi) {
        return 4 * tepi;
    }

    // Menghitung luas segitiga
    public double luasSegitiga(double dasar, double tinggi) {
        return 0.5 * dasar * tinggi;
    }

    // Menghitung keliling segitiga dari tiga sisi
    public double kelilingSegitiga(double s1, double s2, double s3) {
        return s1 + s2 + s3;
    }

    // Mengecek apakah tiga sisi dapat membentuk segitiga
    public boolean segitigaSah(double s1, double s2, double s3) {
        return (s1 + s2 > s3) && (s1 + s3 > s2) && (s2 + s3 > s1);
    }

    // Menghitung luas lingkaran
    public double luasLingkaran(double radius) {
        return KONSTANTA_PI * radius * radius;
    }

    // Menghitung keliling lingkaran
    public double kelilingLingkaran(double radius) {
        return 2 * KONSTANTA_PI * radius;
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

    // Membaca bilangan desimal positif (lebih dari 0)
    public static double ambilPositif(Scanner scanner, String teks) {
        double val = 0;
        while (val <= 0) {
            val = ambilDesimal(scanner, teks);
            if (val <= 0) {
                System.out.println("Nilai harus lebih dari 0!");
            }
        }
        return val;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GeometryCalculator alat = new GeometryCalculator();

        int menu;
        do {
            System.out.println("\n===== PROGRAM BANGUN DATAR =====");
            System.out.println("1. Persegi panjang");
            System.out.println("2. Persegi");
            System.out.println("3. Segitiga");
            System.out.println("4. Lingkaran");
            System.out.println("0. Keluar");
            menu = (int) ambilDesimal(scanner, "Pilih menu: ");

            switch (menu) {
                case 1:
                    double pjg = ambilPositif(scanner, "Panjang : ");
                    double lbr = ambilPositif(scanner, "Lebar   : ");
                    double hasil1 = alat.hitungLuas(pjg, lbr);
                    System.out.println("Luas persegi panjang : " + hasil1);
                    break;
                case 2:
                    double tepi = ambilPositif(scanner, "Panjang sisi : ");
                    System.out.println("Luas persegi    : " + alat.luasPersegi(tepi));
                    System.out.println("Keliling persegi : " + alat.kelilingPersegi(tepi));
                    break;
                case 3:
                    double s1 = ambilPositif(scanner, "Sisi A  : ");
                    double s2 = ambilPositif(scanner, "Sisi B  : ");
                    double s3 = ambilPositif(scanner, "Sisi C  : ");
                    if (alat.segitigaSah(s1, s2, s3)) {
                        double dasar = ambilPositif(scanner, "Alas   : ");
                        double tinggi = ambilPositif(scanner, "Tinggi : ");
                        System.out.println("Luas segitiga    : "
                                + alat.luasSegitiga(dasar, tinggi));
                        System.out.println("Keliling segitiga : "
                                + alat.kelilingSegitiga(s1, s2, s3));
                    } else {
                        System.out.println("Ketiga sisi tidak dapat membentuk segitiga!");
                    }
                    break;
                case 4:
                    double radius = ambilPositif(scanner, "Jari-jari : ");
                    System.out.printf("Luas lingkaran    : %.2f%n", alat.luasLingkaran(radius));
                    System.out.printf("Keliling lingkaran : %.2f%n", alat.kelilingLingkaran(radius));
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