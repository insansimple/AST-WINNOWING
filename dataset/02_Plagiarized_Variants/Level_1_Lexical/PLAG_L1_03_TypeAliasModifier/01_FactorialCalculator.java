/*
 * Program   : FactorialCalculator
 * Deskripsi : Menghitung faktorial dari sebuah bilangan menggunakan
 *             cara iteratif dan rekursif, serta menampilkan tabel
 *             faktorial. Dilengkapi menu sederhana dan validasi input.
 */
import java.util.Scanner;

public class FactorialCalculator {

    // Batas maksimal agar hasil tidak overflow pada tipe long
    static final Integer BATAS_MAKS = 20;

    // Menghitung faktorial secara iteratif (logika inti)
    static public Long compute(Integer n) {
        if (n <= 1) {
            return 1L;
        }
        Long result = 1L;
        for (Integer i = 2; i <= n; i++) {
            result = result * i;
        }
        return result;
    }

    // Menghitung faktorial secara rekursif sebagai pembanding
    static public Long computeRekursif(Integer n) {
        if (n <= 1) {
            return 1L;
        }
        return n * computeRekursif(n - 1);
    }

    // Menampilkan tabel faktorial dari 0 sampai n
    static public void tampilkanTabel(Integer n) {
        System.out.println("\n===== Tabel Faktorial 0 s.d. " + n + " =====");
        System.out.println("n\t| n!");
        System.out.println("--------------------------");
        for (Integer i = 0; i <= n; i++) {
            System.out.println(i + "\t| " + compute(i));
        }
    }

    // Membandingkan hasil iteratif dan rekursif
    static public void bandingkanMetode(Integer n) {
        Long hasilIteratif = compute(n);
        Long hasilRekursif = computeRekursif(n);

        System.out.println("\nHasil iteratif  : " + hasilIteratif);
        System.out.println("Hasil rekursif  : " + hasilRekursif);

        if (hasilIteratif.equals(hasilRekursif)) {
            System.out.println("Kedua metode menghasilkan nilai yang sama.");
        } else {
            System.out.println("Hasil berbeda, ada yang salah!");
        }
    }

    // Membaca bilangan dari pengguna dengan validasi
    static public Integer bacaBilangan(Scanner input) {
        Integer n = -1;
        Boolean valid = false;

        while (!valid) {
            System.out.print("Masukkan bilangan (0-" + BATAS_MAKS + "): ");
            if (input.hasNextInt()) {
                n = input.nextInt();
                if (n >= 0 && n <= BATAS_MAKS) {
                    valid = true;
                } else {
                    System.out.println("Bilangan di luar jangkauan, coba lagi!");
                }
            } else {
                System.out.println("Input harus berupa angka bulat!");
                input.next(); // buang input yang salah
            }
        }
        return n;
    }

    static public void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Integer pilihan;

        do {
            System.out.println("\n===== KALKULATOR FAKTORIAL =====");
            System.out.println("1. Hitung faktorial satu bilangan");
            System.out.println("2. Tampilkan tabel faktorial");
            System.out.println("3. Bandingkan iteratif vs rekursif");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu: ");

            while (!input.hasNextInt()) {
                System.out.print("Pilihan harus angka, ulangi: ");
                input.next();
            }
            pilihan = input.nextInt();

            switch (pilihan) {
                case 1:
                    Integer a = bacaBilangan(input);
                    System.out.println(a + "! = " + compute(a));
                    break;
                case 2:
                    Integer b = bacaBilangan(input);
                    tampilkanTabel(b);
                    break;
                case 3:
                    Integer c = bacaBilangan(input);
                    bandingkanMetode(c);
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