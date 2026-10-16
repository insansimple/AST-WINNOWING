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
    static final double PI = 3.14159265358979;

    // Inner static class wrapper untuk dimensi persegi panjang
    public static class RectangleShape {
        private double length;
        private double width;

        public RectangleShape(double length, double width) {
            this.length = length;
            this.width = width;
        }

        public double getLength() {
            return length;
        }

        public double getWidth() {
            return width;
        }

        public double area() {
            return length * width;
        }

        public double perimeter() {
            return 2 * (length + width);
        }
    }

    // Inner static class wrapper untuk dimensi sisi-sisi segitiga
    public static class TriangleShape {
        private double a;
        private double b;
        private double c;

        public TriangleShape(double a, double b, double c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public double getA() {
            return a;
        }

        public double getB() {
            return b;
        }

        public double getC() {
            return c;
        }

        public boolean isValid() {
            return (a + b > c) && (a + c > b) && (b + c > a);
        }

        public double perimeter() {
            return a + b + c;
        }
    }

    // Logika inti: menghitung luas persegi panjang dan menampilkan kelilingnya
    public double calculateArea(double length, double width) {
        RectangleShape rect = new RectangleShape(length, width);
        double area = rect.area();
        double perimeter = rect.perimeter();
        System.out.println("Perimeter: " + perimeter);
        return area;
    }

    // Menghitung luas persegi
    public double luasPersegi(double sisi) {
        return sisi * sisi;
    }

    // Menghitung keliling persegi
    public double kelilingPersegi(double sisi) {
        return 4 * sisi;
    }

    // Menghitung luas segitiga
    public double luasSegitiga(double alas, double tinggi) {
        return 0.5 * alas * tinggi;
    }

    // Menghitung keliling segitiga dari tiga sisi
    public double kelilingSegitiga(double a, double b, double c) {
        TriangleShape tri = new TriangleShape(a, b, c);
        return tri.perimeter();
    }

    // Mengecek apakah tiga sisi dapat membentuk segitiga
    public boolean segitigaValid(double a, double b, double c) {
        TriangleShape tri = new TriangleShape(a, b, c);
        return tri.isValid();
    }

    // Menghitung luas lingkaran
    public double luasLingkaran(double jariJari) {
        return PI * jariJari * jariJari;
    }

    // Menghitung keliling lingkaran
    public double kelilingLingkaran(double jariJari) {
        return 2 * PI * jariJari;
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

    // Membaca bilangan desimal positif (lebih dari 0)
    public static double bacaPositif(Scanner input, String pesan) {
        double nilai = 0;
        while (nilai <= 0) {
            nilai = bacaDesimal(input, pesan);
            if (nilai <= 0) {
                System.out.println("Nilai harus lebih dari 0!");
            }
        }
        return nilai;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        GeometryCalculator kalkulator = new GeometryCalculator();

        int pilihan = 0;
        do {
            System.out.println("\n===== PROGRAM BANGUN DATAR =====");
            System.out.println("1. Persegi panjang");
            System.out.println("2. Persegi");
            System.out.println("3. Segitiga");
            System.out.println("4. Lingkaran");
            System.out.println("0. Keluar");
            pilihan = (int) bacaDesimal(input, "Pilih menu: ");

            switch (pilihan) {
                case 1:
                    double panjang = bacaPositif(input, "Panjang : ");
                    double lebar = bacaPositif(input, "Lebar   : ");
                    double luas1 = kalkulator.calculateArea(panjang, lebar);
                    System.out.println("Luas persegi panjang : " + luas1);
                    break;
                case 2:
                    double sisi = bacaPositif(input, "Panjang sisi : ");
                    System.out.println("Luas persegi    : " + kalkulator.luasPersegi(sisi));
                    System.out.println("Keliling persegi : " + kalkulator.kelilingPersegi(sisi));
                    break;
                case 3:
                    double sisiA = bacaPositif(input, "Sisi A  : ");
                    double sisiB = bacaPositif(input, "Sisi B  : ");
                    double sisiC = bacaPositif(input, "Sisi C  : ");
                    if (kalkulator.segitigaValid(sisiA, sisiB, sisiC)) {
                        double alas = bacaPositif(input, "Alas   : ");
                        double tinggi = bacaPositif(input, "Tinggi : ");
                        System.out.println("Luas segitiga    : "
                                + kalkulator.luasSegitiga(alas, tinggi));
                        System.out.println("Keliling segitiga : "
                                + kalkulator.kelilingSegitiga(sisiA, sisiB, sisiC));
                    } else {
                        System.out.println("Ketiga sisi tidak dapat membentuk segitiga!");
                    }
                    break;
                case 4:
                    double r = bacaPositif(input, "Jari-jari : ");
                    System.out.printf("Luas lingkaran    : %.2f%n", kalkulator.luasLingkaran(r));
                    System.out.printf("Keliling lingkaran : %.2f%n", kalkulator.kelilingLingkaran(r));
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