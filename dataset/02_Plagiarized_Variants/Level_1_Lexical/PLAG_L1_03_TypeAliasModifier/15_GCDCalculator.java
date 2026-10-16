/*
 * Program   : GCDCalculator
 * Deskripsi : Menghitung FPB (Faktor Persekutuan Terbesar) dari dua
 *             bilangan menggunakan algoritma Euclid. Dilengkapi versi
 *             rekursif, KPK, FPB dari tiga bilangan, pengecekan
 *             bilangan relatif prima, input dari pengguna, dan menu
 *             sederhana.
 */
import java.util.Scanner;

public class GCDCalculator {

    // Logika inti: algoritma Euclid secara iteratif
    public Integer computeGCD(Integer a, Integer b) {
        while (b != 0) {
            Integer temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // Algoritma Euclid secara rekursif sebagai pembanding
    public Integer computeGCDRekursif(Integer a, Integer b) {
        if (b == 0) {
            return a;
        }
        return computeGCDRekursif(b, a % b);
    }

    // Menghitung KPK dengan rumus KPK = (a x b) / FPB (pakai long agar aman)
    public Long computeLCM(Integer a, Integer b) {
        if (a == 0 || b == 0) {
            return 0L;
        }
        return (long) a / computeGCD(a, b) * b;
    }

    // Menghitung FPB dari tiga bilangan
    public Integer computeGCDTiga(Integer a, Integer b, Integer c) {
        return computeGCD(computeGCD(a, b), c);
    }

    // Mengecek apakah dua bilangan relatif prima (FPB = 1)
    public Boolean relatifPrima(Integer a, Integer b) {
        return computeGCD(a, b) == 1;
    }

    // Menampilkan semua faktor persekutuan dari dua bilangan
    public void tampilkanFaktorPersekutuan(Integer a, Integer b) {
        Integer fpb = computeGCD(a, b);
        System.out.print("Faktor persekutuan " + a + " dan " + b + " : ");
        for (Integer i = 1; i <= fpb; i++) {
            if (fpb % i == 0) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
    }

    // Membaca bilangan bulat dengan validasi sederhana
    static public Integer bacaAngka(Scanner input, String pesan) {
        System.out.print(pesan);
        while (!input.hasNextInt()) {
            System.out.print("Input harus angka bulat, ulangi: ");
            input.next();
        }
        return input.nextInt();
    }

    // Membaca bilangan bulat positif (lebih dari 0)
    static public Integer bacaPositif(Scanner input, String pesan) {
        Integer nilai = 0;
        while (nilai <= 0) {
            nilai = bacaAngka(input, pesan);
            if (nilai <= 0) {
                System.out.println("Bilangan harus lebih dari 0!");
            }
        }
        return nilai;
    }

    static public void main(String[] args) {
        Scanner input = new Scanner(System.in);
        GCDCalculator kalkulator = new GCDCalculator();

        Integer pilihan;
        do {
            System.out.println("\n===== PROGRAM FPB DAN KPK =====");
            System.out.println("1. Hitung FPB dua bilangan");
            System.out.println("2. Hitung KPK dua bilangan");
            System.out.println("3. Hitung FPB tiga bilangan");
            System.out.println("4. Cek relatif prima");
            System.out.println("5. Tampilkan faktor persekutuan");
            System.out.println("6. Bandingkan iteratif vs rekursif");
            System.out.println("0. Keluar");
            pilihan = bacaAngka(input, "Pilih menu: ");

            switch (pilihan) {
                case 1:
                    Integer a1 = bacaPositif(input, "Bilangan pertama : ");
                    Integer b1 = bacaPositif(input, "Bilangan kedua   : ");
                    System.out.println("FPB(" + a1 + ", " + b1 + ") = "
                            + kalkulator.computeGCD(a1, b1));
                    break;
                case 2:
                    Integer a2 = bacaPositif(input, "Bilangan pertama : ");
                    Integer b2 = bacaPositif(input, "Bilangan kedua   : ");
                    System.out.println("KPK(" + a2 + ", " + b2 + ") = "
                            + kalkulator.computeLCM(a2, b2));
                    break;
                case 3:
                    Integer a3 = bacaPositif(input, "Bilangan pertama : ");
                    Integer b3 = bacaPositif(input, "Bilangan kedua   : ");
                    Integer c3 = bacaPositif(input, "Bilangan ketiga  : ");
                    System.out.println("FPB(" + a3 + ", " + b3 + ", " + c3 + ") = "
                            + kalkulator.computeGCDTiga(a3, b3, c3));
                    break;
                case 4:
                    Integer a4 = bacaPositif(input, "Bilangan pertama : ");
                    Integer b4 = bacaPositif(input, "Bilangan kedua   : ");
                    if (kalkulator.relatifPrima(a4, b4)) {
                        System.out.println(a4 + " dan " + b4 + " relatif prima.");
                    } else {
                        System.out.println(a4 + " dan " + b4 + " tidak relatif prima.");
                    }
                    break;
                case 5:
                    Integer a5 = bacaPositif(input, "Bilangan pertama : ");
                    Integer b5 = bacaPositif(input, "Bilangan kedua   : ");
                    kalkulator.tampilkanFaktorPersekutuan(a5, b5);
                    break;
                case 6:
                    Integer a6 = bacaPositif(input, "Bilangan pertama : ");
                    Integer b6 = bacaPositif(input, "Bilangan kedua   : ");
                    Integer iteratif = kalkulator.computeGCD(a6, b6);
                    Integer rekursif = kalkulator.computeGCDRekursif(a6, b6);
                    System.out.println("Hasil iteratif : " + iteratif);
                    System.out.println("Hasil rekursif : " + rekursif);
                    if (iteratif.equals(rekursif)) {
                        System.out.println("Kedua metode menghasilkan nilai yang sama.");
                    } else {
                        System.out.println("Hasil berbeda, ada yang salah!");
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