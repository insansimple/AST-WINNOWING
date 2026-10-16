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
    static final Double PI = 3.14159265358979;

    // Logika inti: menghitung luas persegi panjang dan menampilkan kelilingnya
    public Double calculateArea(Double length, Double width) {
        Double area = length * width;
        Double perimeter = 2 * (length + width);
        System.out.println("Perimeter: " + perimeter);
        return area;
    }

    // Menghitung luas persegi
    public Double luasPersegi(Double sisi) {
        return sisi * sisi;
    }

    // Menghitung keliling persegi
    public Double kelilingPersegi(Double sisi) {
        return 4 * sisi;
    }

    // Menghitung luas segitiga
    public Double luasSegitiga(Double alas, Double tinggi) {
        return 0.5 * alas * tinggi;
    }

    // Menghitung keliling segitiga dari tiga sisi
    public Double kelilingSegitiga(Double a, Double b, Double c) {
        return a + b + c;
    }

    // Mengecek apakah tiga sisi dapat membentuk segitiga
    public Boolean segitigaValid(Double a, Double b, Double c) {
        return (a + b > c) && (a + c > b) && (b + c > a);
    }

    // Menghitung luas lingkaran
    public Double luasLingkaran(Double jariJari) {
        return PI * jariJari * jariJari;
    }

    // Menghitung keliling lingkaran
    public Double kelilingLingkaran(Double jariJari) {
        return 2 * PI * jariJari;
    }

    // Membaca bilangan desimal dengan validasi sederhana
    static public Double bacaDesimal(Scanner input, String pesan) {
        System.out.print(pesan);
        while (!input.hasNextDouble()) {
            System.out.print("Input harus berupa angka, ulangi: ");
            input.next();
        }
        return input.nextDouble();
    }

    // Membaca bilangan desimal positif (lebih dari 0)
    static public Double bacaPositif(Scanner input, String pesan) {
        Double nilai = 0.0;
        while (nilai <= 0) {
            nilai = bacaDesimal(input, pesan);
            if (nilai <= 0) {
                System.out.println("Nilai harus lebih dari 0!");
            }
        }
        return nilai;
    }

    static public void main(String[] args) {
        Scanner input = new Scanner(System.in);
        GeometryCalculator kalkulator = new GeometryCalculator();

        Integer pilihan;
        do {
            System.out.println("\n===== PROGRAM BANGUN DATAR =====");
            System.out.println("1. Persegi panjang");
            System.out.println("2. Persegi");
            System.out.println("3. Segitiga");
            System.out.println("4. Lingkaran");
            System.out.println("0. Keluar");
            pilihan = (int) bacaDesimal(input, "Pilih menu: ").doubleValue();

            switch (pilihan) {
                case 1:
                    Double panjang = bacaPositif(input, "Panjang : ");
                    Double lebar = bacaPositif(input, "Lebar   : ");
                    Double luas1 = kalkulator.calculateArea(panjang, lebar);
                    System.out.println("Luas persegi panjang : " + luas1);
                    break;
                case 2:
                    Double sisi = bacaPositif(input, "Panjang sisi : ");
                    System.out.println("Luas persegi    : " + kalkulator.luasPersegi(sisi));
                    System.out.println("Keliling persegi : " + kalkulator.kelilingPersegi(sisi));
                    break;
                case 3:
                    Double sisiA = bacaPositif(input, "Sisi A  : ");
                    Double sisiB = bacaPositif(input, "Sisi B  : ");
                    Double sisiC = bacaPositif(input, "Sisi C  : ");
                    if (kalkulator.segitigaValid(sisiA, sisiB, sisiC)) {
                        Double alas = bacaPositif(input, "Alas   : ");
                        Double tinggi = bacaPositif(input, "Tinggi : ");
                        System.out.println("Luas segitiga    : "
                                + kalkulator.luasSegitiga(alas, tinggi));
                        System.out.println("Keliling segitiga : "
                                + kalkulator.kelilingSegitiga(sisiA, sisiB, sisiC));
                    } else {
                        System.out.println("Ketiga sisi tidak dapat membentuk segitiga!");
                    }
                    break;
                case 4:
                    Double r = bacaPositif(input, "Jari-jari : ");
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