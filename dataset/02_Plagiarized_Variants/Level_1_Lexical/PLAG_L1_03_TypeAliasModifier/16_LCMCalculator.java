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
    public Integer computeLCM(Integer num1, Integer num2) {
        Integer max = Math.max(num1, num2);
        while (true) {
            if (max % num1 == 0 && max % num2 == 0) {
                return max;
            }
            max++;
        }
    }

    // Menghitung FPB dengan algoritma Euclid (dipakai untuk cara cepat)
    public Integer computeGCD(Integer a, Integer b) {
        while (b != 0) {
            Integer temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // Cara cepat: KPK = (a x b) / FPB (pakai long agar tidak overflow)
    public Long computeLCMCepat(Integer a, Integer b) {
        return (long) a / computeGCD(a, b) * b;
    }

    // Menghitung KPK dari tiga bilangan
    public Long computeLCMTiga(Integer a, Integer b, Integer c) {
        Long kpkAB = computeLCMCepat(a, b);
        // KPK(ab, c) = (kpkAB x c) / FPB(kpkAB, c)
        Long x = kpkAB;
        Long y = (long) c;
        while (y != 0) {
            Long temp = y;
            y = x % y;
            x = temp;
        }
        return kpkAB / x * c;
    }

    // Menampilkan beberapa kelipatan pertama dari sebuah bilangan
    public void tampilkanKelipatan(Integer angka, Integer jumlah) {
        System.out.print("Kelipatan " + angka + " : ");
        for (Integer i = 1; i <= jumlah; i++) {
            System.out.print((long) angka * i + " ");
        }
        System.out.println();
    }

    // Membandingkan hasil cara lambat dan cara cepat
    public void bandingkanMetode(Integer a, Integer b) {
        Integer hasilLambat = computeLCM(a, b);
        Long hasilCepat = computeLCMCepat(a, b);

        System.out.println("Hasil cara bertahap : " + hasilLambat);
        System.out.println("Hasil cara FPB      : " + hasilCepat);

        if (hasilLambat.equals(hasilCepat.intValue())) {
            System.out.println("Kedua cara menghasilkan nilai yang sama.");
        } else {
            System.out.println("Hasil berbeda, ada yang salah!");
        }
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
        LCMCalculator kalkulator = new LCMCalculator();

        Integer pilihan;
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
                    Integer a1 = bacaPositif(input, "Bilangan pertama : ");
                    Integer b1 = bacaPositif(input, "Bilangan kedua   : ");
                    System.out.println("KPK(" + a1 + ", " + b1 + ") = "
                            + kalkulator.computeLCM(a1, b1));
                    break;
                case 2:
                    Integer a2 = bacaPositif(input, "Bilangan pertama : ");
                    Integer b2 = bacaPositif(input, "Bilangan kedua   : ");
                    Integer c2 = bacaPositif(input, "Bilangan ketiga  : ");
                    System.out.println("KPK(" + a2 + ", " + b2 + ", " + c2 + ") = "
                            + kalkulator.computeLCMTiga(a2, b2, c2));
                    break;
                case 3:
                    Integer angka = bacaPositif(input, "Masukkan bilangan : ");
                    Integer jumlah = bacaPositif(input, "Jumlah kelipatan : ");
                    kalkulator.tampilkanKelipatan(angka, jumlah);
                    break;
                case 4:
                    Integer a4 = bacaPositif(input, "Bilangan pertama : ");
                    Integer b4 = bacaPositif(input, "Bilangan kedua   : ");
                    kalkulator.bandingkanMetode(a4, b4);
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