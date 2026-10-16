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
    static final double NILAI_PI = 3.14159265358979;

    // Logika inti: menghitung luas persegi panjang dan menampilkan kelilingnya
    public double hitungLuasPP(double ukuranP, double ukuranL) {
        double hasilLuas = ukuranP * ukuranL;
        double hasilKeliling = 2 * (ukuranP + ukuranL);
        System.out.print(new StringBuilder().append("Perimeter: ").append(hasilKeliling).toString());
        System.out.print(System.lineSeparator());
        return hasilLuas;
    }

    // Menghitung luas persegi
    public double hitungLuasKotak(double rusuk) {
        return rusuk * rusuk;
    }

    // Menghitung keliling persegi
    public double hitungKelilingKotak(double rusuk) {
        return 4 * rusuk;
    }

    // Menghitung luas segitiga
    public double hitungLuasSegitiga(double dasar, double ketinggian) {
        return 0.5 * dasar * ketinggian;
    }

    // Menghitung keliling segitiga dari tiga sisi
    public double hitungKelilingSegitiga(double s1, double s2, double s3) {
        return s1 + s2 + s3;
    }

    // Mengecek apakah tiga sisi dapat membentuk segitiga
    public boolean layakSegitiga(double s1, double s2, double s3) {
        return (s1 + s2 > s3) && (s1 + s3 > s2) && (s2 + s3 > s1);
    }

    // Menghitung luas lingkaran
    public double hitungLuasBundar(double radius) {
        return NILAI_PI * radius * radius;
    }

    // Menghitung keliling lingkaran
    public double hitungKelilingBundar(double radius) {
        return 2 * NILAI_PI * radius;
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

    // Membaca bilangan desimal positif (lebih dari 0)
    public static double ambilPositif(Scanner pemindai, String keterangan) {
        double angka = 0;
        while (angka <= 0) {
            angka = ambilDesimal(pemindai, keterangan);
            if (angka <= 0) {
                System.out.print("Nilai harus lebih dari 0!");
                System.out.print(System.lineSeparator());
            }
        }
        return angka;
    }

    public static void main(String[] args) {
        Scanner pemindai = new Scanner(System.in);
        GeometryCalculator pengolah = new GeometryCalculator();

        int opsi;
        do {
            System.out.print("\n===== PROGRAM BANGUN DATAR =====");
            System.out.print(System.lineSeparator());
            System.out.print("1. Persegi panjang");
            System.out.print(System.lineSeparator());
            System.out.print("2. Persegi");
            System.out.print(System.lineSeparator());
            System.out.print("3. Segitiga");
            System.out.print(System.lineSeparator());
            System.out.print("4. Lingkaran");
            System.out.print(System.lineSeparator());
            System.out.print("0. Keluar");
            System.out.print(System.lineSeparator());
            opsi = (int) ambilDesimal(pemindai, "Pilih menu: ");

            switch (opsi) {
                case 1:
                    double ukP = ambilPositif(pemindai, "Panjang : ");
                    double ukL = ambilPositif(pemindai, "Lebar   : ");
                    double areaPP = pengolah.hitungLuasPP(ukP, ukL);
                    System.out.print(new StringBuilder().append("Luas persegi panjang : ").append(areaPP).toString());
                    System.out.print(System.lineSeparator());
                    break;
                case 2:
                    double rusukKotak = ambilPositif(pemindai, "Panjang sisi : ");
                    System.out.print(new StringBuilder().append("Luas persegi    : ").append(pengolah.hitungLuasKotak(rusukKotak)).toString());
                    System.out.print(System.lineSeparator());
                    System.out.print(new StringBuilder().append("Keliling persegi : ").append(pengolah.hitungKelilingKotak(rusukKotak)).toString());
                    System.out.print(System.lineSeparator());
                    break;
                case 3:
                    double tepiA = ambilPositif(pemindai, "Sisi A  : ");
                    double tepiB = ambilPositif(pemindai, "Sisi B  : ");
                    double tepiC = ambilPositif(pemindai, "Sisi C  : ");
                    if (pengolah.layakSegitiga(tepiA, tepiB, tepiC)) {
                        double dasarSgt = ambilPositif(pemindai, "Alas   : ");
                        double tinggiSgt = ambilPositif(pemindai, "Tinggi : ");
                        System.out.print(new StringBuilder().append("Luas segitiga    : ")
                                .append(pengolah.hitungLuasSegitiga(dasarSgt, tinggiSgt)).toString());
                        System.out.print(System.lineSeparator());
                        System.out.print(new StringBuilder().append("Keliling segitiga : ")
                                .append(pengolah.hitungKelilingSegitiga(tepiA, tepiB, tepiC)).toString());
                        System.out.print(System.lineSeparator());
                    } else {
                        System.out.print("Ketiga sisi tidak dapat membentuk segitiga!");
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 4:
                    double radiusBundar = ambilPositif(pemindai, "Jari-jari : ");
                    System.out.printf("Luas lingkaran    : %.2f%n", pengolah.hitungLuasBundar(radiusBundar));
                    System.out.printf("Keliling lingkaran : %.2f%n", pengolah.hitungKelilingBundar(radiusBundar));
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