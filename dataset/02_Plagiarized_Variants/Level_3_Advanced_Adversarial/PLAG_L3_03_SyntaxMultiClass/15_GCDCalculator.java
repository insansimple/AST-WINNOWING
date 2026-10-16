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

    // Inner static class wrapper untuk pasangan operan FPB dan KPK
    public static class OperanPair {
        private int first;
        private int second;

        public OperanPair(int first, int second) {
            this.first = first;
            this.second = second;
        }

        public int getFirst() {
            return first;
        }

        public int getSecond() {
            return second;
        }

        public void setFirst(int first) {
            this.first = first;
        }

        public void setSecond(int second) {
            this.second = second;
        }
    }

    // Inner static class wrapper untuk tiga operan FPB
    public static class TripletOperan {
        private int a;
        private int b;
        private int c;

        public TripletOperan(int a, int b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public int getA() {
            return a;
        }

        public int getB() {
            return b;
        }

        public int getC() {
            return c;
        }
    }

    // Logika inti: algoritma Euclid secara iteratif
    public int computeGCD(int a, int b) {
        OperanPair pair = new OperanPair(a, b);
        while (pair.getSecond() != 0) {
            int temp = pair.getSecond();
            pair.setSecond(pair.getFirst() % pair.getSecond());
            pair.setFirst(temp);
        }
        return pair.getFirst();
    }

    // Algoritma Euclid secara rekursif sebagai pembanding
    public int computeGCDRekursif(int a, int b) {
        OperanPair pair = new OperanPair(a, b);
        if (pair.getSecond() == 0) {
            return pair.getFirst();
        }
        return computeGCDRekursif(pair.getSecond(), pair.getFirst() % pair.getSecond());
    }

    // Menghitung KPK dengan rumus KPK = (a x b) / FPB (pakai long agar aman)
    public long computeLCM(int a, int b) {
        OperanPair pair = new OperanPair(a, b);
        if (pair.getFirst() == 0 || pair.getSecond() == 0) {
            return 0;
        }
        return (long) pair.getFirst() / computeGCD(pair.getFirst(), pair.getSecond()) * pair.getSecond();
    }

    // Menghitung FPB dari tiga bilangan
    public int computeGCDTiga(int a, int b, int c) {
        TripletOperan triplet = new TripletOperan(a, b, c);
        return computeGCD(computeGCD(triplet.getA(), triplet.getB()), triplet.getC());
    }

    // Mengecek apakah dua bilangan relatif prima (FPB = 1)
    public boolean relatifPrima(int a, int b) {
        OperanPair pair = new OperanPair(a, b);
        return computeGCD(pair.getFirst(), pair.getSecond()) == 1;
    }

    // Menampilkan semua faktor persekutuan dari dua bilangan
    public void tampilkanFaktorPersekutuan(int a, int b) {
        OperanPair pair = new OperanPair(a, b);
        int fpb = computeGCD(pair.getFirst(), pair.getSecond());
        System.out.print("Faktor persekutuan " + pair.getFirst() + " dan " + pair.getSecond() + " : ");
        for (int i = 1; i <= fpb; i++) {
            if (fpb % i == 0) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
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

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        GCDCalculator kalkulator = new GCDCalculator();

        int pilihan = 0;
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
}