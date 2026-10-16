/*
 * Program   : LCMCalculator
 * Deskripsi : Menghitung KPK (Kelipatan Persekutuan Terkecil) dari dua
 *             bilangan. Dilengkapi cara cepat memakai FPB, KPK tiga
 *             bilangan, daftar kelipatan, perbandingan kedua cara,
 *             input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner;

public class LCMCalculator {

    // Logika inti: mencari KPK dengan menaikkan nilai terbesar satu per satu
    public int computeLCM(int num1, int num2) {
        int max = Math.max(num1, num2);
        while (true) {
            if (0 == max % num1 && 0 == max % num2) {
                return max;
            }
            max++;
        }
    }

    // Menghitung FPB dengan algoritma Euclid (dipakai untuk cara cepat)
    public int computeGCD(int a, int b) {
        while (0 != b) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // Cara cepat: KPK = (a x b) / FPB (pakai long agar tidak overflow)
    public long computeLCMCepat(int a, int b) {
        return (long) a / computeGCD(a, b) * b;
    }

    // Menghitung KPK dari tiga bilangan
    public long computeLCMTiga(int a, int b, int c) {
        long kpkAB = computeLCMCepat(a, b);
        // KPK(ab, c) = (kpkAB x c) / FPB(kpkAB, c)
        long x = kpkAB;
        long y = c;
        while (0 != y) {
            long temp = y;
            y = x % y;
            x = temp;
        }
        return kpkAB / x * c;
    }

    // Menampilkan beberapa kelipatan pertama dari sebuah bilangan
    public void tampilkanKelipatan(int angka, int jumlah) {
        System.out.print("Kelipatan " + angka + " : ");
        for (int i = 1; i <= jumlah; i++) {
            System.out.print((long) angka * i + " ");
        }
        System.out.println();
    }

    // Membandingkan hasil cara lambat dan cara cepat
    public void bandingkanMetode(int a, int b) {
        int hasilLambat = computeLCM(a, b);
        long hasilCepat = computeLCMCepat(a, b);

        System.out.println("Hasil cara bertahap : " + hasilLambat);
        System.out.println("Hasil cara FPB      : " + hasilCepat);

        if (hasilLambat != hasilCepat) {
            System.out.println("Hasil berbeda, ada yang salah!");
        } else {
            System.out.println("Kedua cara menghasilkan nilai yang sama.");
        }
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
        while (0 >= nilai) {
            nilai = bacaAngka(input, pesan);
            if (0 >= nilai) {
                System.out.println("Bilangan harus lebih dari 0!");
            }
        }
        return nilai;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        LCMCalculator kalkulator = new LCMCalculator();

        int pilihan;
        do {
            System.out.println("\n===== PROGRAM KPK =====");
            System.out.println("1. Hitung KPK dua bilangan");
            System.out.println("2. Hitung KPK tiga bilangan");
            System.out.println("3. Tampilkan kelipatan sebuah bilangan");
            System.out.println("4. Bandingkan dua cara menghitung KPK");
            System.out.println("0. Keluar");
            pilihan = bacaAngka(input, "Pilih menu: ");

            switch (pilihan) {
                case 1:
                    int a1 = bacaPositif(input, "Bilangan pertama : ");
                    int b1 = bacaPositif(input, "Bilangan kedua   : ");
                    System.out.println("KPK(" + a1 + ", " + b1 + ") = "
                            + kalkulator.computeLCM(a1, b1));
                    break;
                case 2:
                    int a2 = bacaPositif(input, "Bilangan pertama : ");
                    int b2 = bacaPositif(input, "Bilangan kedua   : ");
                    int c2 = bacaPositif(input, "Bilangan ketiga  : ");
                    System.out.println("KPK(" + a2 + ", " + b2 + ", " + c2 + ") = "
                            + kalkulator.computeLCMTiga(a2, b2, c2));
                    break;
                case 3:
                    int angka = bacaPositif(input, "Masukkan bilangan : ");
                    int jumlah = bacaPositif(input, "Jumlah kelipatan : ");
                    kalkulator.tampilkanKelipatan(angka, jumlah);
                    break;
                case 4:
                    int a4 = bacaPositif(input, "Bilangan pertama : ");
                    int b4 = bacaPositif(input, "Bilangan kedua   : ");
                    kalkulator.bandingkanMetode(a4, b4);
                    break;
                case 0:
                    System.out.println("Terima kasih, program selesai.");
                    break;
                default:
                    System.out.println("Menu tidak tersedia!");
            }
        } while (0 != pilihan);

        input.close();
    }
}