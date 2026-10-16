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

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        GCDCalculator kalkulator = new GCDCalculator();

        int pilihan;
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
                    int a1 = bacaPositif(input, "Bilangan pertama : ");
                    int b1 = bacaPositif(input, "Bilangan kedua   : ");
                    System.out.println("FPB(" + a1 + ", " + b1 + ") = "
                            + kalkulator.computeGCD(a1, b1));
                    break;
                case 2:
                    int a2 = bacaPositif(input, "Bilangan pertama : ");
                    int b2 = bacaPositif(input, "Bilangan kedua   : ");
                    System.out.println("KPK(" + a2 + ", " + b2 + ") = "
                            + kalkulator.computeLCM(a2, b2));
                    break;
                case 3:
                    int a3 = bacaPositif(input, "Bilangan pertama : ");
                    int b3 = bacaPositif(input, "Bilangan kedua   : ");
                    int c3 = bacaPositif(input, "Bilangan ketiga  : ");
                    System.out.println("FPB(" + a3 + ", " + b3 + ", " + c3 + ") = "
                            + kalkulator.computeGCDTiga(a3, b3, c3));
                    break;
                case 4:
                    int a4 = bacaPositif(input, "Bilangan pertama : ");
                    int b4 = bacaPositif(input, "Bilangan kedua   : ");
                    if (kalkulator.relatifPrima(a4, b4)) {
                        System.out.println(a4 + " dan " + b4 + " relatif prima.");
                    } else {
                        System.out.println(a4 + " dan " + b4 + " tidak relatif prima.");
                    }
                    break;
                case 5:
                    int a5 = bacaPositif(input, "Bilangan pertama : ");
                    int b5 = bacaPositif(input, "Bilangan kedua   : ");
                    kalkulator.tampilkanFaktorPersekutuan(a5, b5);
                    break;
                case 6:
                    int a6 = bacaPositif(input, "Bilangan pertama : ");
                    int b6 = bacaPositif(input, "Bilangan kedua   : ");
                    int iteratif = kalkulator.computeGCD(a6, b6);
                    int rekursif = kalkulator.computeGCDRekursif(a6, b6);
                    System.out.println("Hasil iteratif : " + iteratif);
                    System.out.println("Hasil rekursif : " + rekursif);
                    if (iteratif == rekursif) {
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

    // Membaca bilangan bulat positif (lebih dari 0)
    public static int bacaPositif(Scanner input, String pesan) {
        int nilai = 0;
        while (nilai <= 0) {
            nilai = bacaAngka(input, pesan);
            if (nilai <= 0) {
                System.out.println("Bilangan harus lebih dari 0!");
            }
        }
        return nilai;
    }

    // Membaca bilangan bulat dengan validasi sederhana
    public static int bacaAngka(Scanner input, String pesan) {
        System.out.print(pesan);
        while (!input.hasNextInt()) {
            System.out.print("Input harus angka bulat, ulangi: ");
            input.next();
        }
        return input.nextInt();
    }

    // Menampilkan semua faktor persekutuan dari dua bilangan
    public void tampilkanFaktorPersekutuan(int a, int b) {
        int fpb = computeGCD(a, b);
        System.out.print("Faktor persekutuan " + a + " dan " + b + " : ");
        for (int i = 1; i <= fpb; i++) {
            if (fpb % i == 0) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
    }

    // Mengecek apakah dua bilangan relatif prima (FPB = 1)
    public boolean relatifPrima(int a, int b) {
        return computeGCD(a, b) == 1;
    }

    // Menghitung FPB dari tiga bilangan
    public int computeGCDTiga(int a, int b, int c) {
        return computeGCD(computeGCD(a, b), c);
    }

    // Menghitung KPK dengan rumus KPK = (a x b) / FPB (pakai long agar aman)
    public long computeLCM(int a, int b) {
        if (a == 0 || b == 0) {
            return 0;
        }
        return (long) a / computeGCD(a, b) * b;
    }

    // Algoritma Euclid secara rekursif sebagai pembanding
    public int computeGCDRekursif(int a, int b) {
        if (b == 0) {
            return a;
        }
        return computeGCDRekursif(b, a % b);
    }

    // Logika inti: algoritma Euclid secara iteratif
    public int computeGCD(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}