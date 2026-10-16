/*
 * Program   : FactorialCalculator
 * Deskripsi : Menghitung faktorial dari sebuah bilangan menggunakan
 *             cara iteratif dan rekursif, serta menampilkan tabel
 *             faktorial. Dilengkapi menu sederhana dan validasi input.
 */
import java.util.Scanner;

public class FactorialCalculator {

    // Batas maksimal agar hasil tidak overflow pada tipe long
    static final int NILAI_MAKS = 20;

    // Menghitung faktorial secara iteratif (logika inti)
    public static long hitung(int angka) {
        if (angka <= 1) {
            return 1;
        }
        long produk = 1;
        for (int idx = 2; idx <= angka; idx++) {
            produk = produk * idx;
        }
        return produk;
    }

    // Menghitung faktorial secara rekursif sebagai pembanding
    public static long hitungRekursif(int angka) {
        if (angka <= 1) {
            return 1;
        }
        return angka * hitungRekursif(angka - 1);
    }

    // Menampilkan tabel faktorial dari 0 sampai angka
    public static void tampilkanTabel(int angka) {
        System.out.println("\n===== Tabel Faktorial 0 s.d. " + angka + " =====");
        System.out.println("n\t| n!");
        System.out.println("--------------------------");
        for (int idx = 0; idx <= angka; idx++) {
            System.out.println(idx + "\t| " + hitung(idx));
        }
    }

    // Membandingkan hasil iteratif dan rekursif
    public static void bandingkanMetode(int angka) {
        long nilaiIteratif = hitung(angka);
        long nilaiRekursif = hitungRekursif(angka);

        System.out.println("\nHasil iteratif  : " + nilaiIteratif);
        System.out.println("Hasil rekursif  : " + nilaiRekursif);

        if (nilaiIteratif == nilaiRekursif) {
            System.out.println("Kedua metode menghasilkan nilai yang sama.");
        } else {
            System.out.println("Hasil berbeda, ada yang salah!");
        }
    }

    // Membaca bilangan dari pengguna dengan validasi
    public static int bacaBilangan(Scanner scanner) {
        int angka = -1;
        boolean inputValid = false;

        while (!inputValid) {
            System.out.print("Masukkan bilangan (0-" + NILAI_MAKS + "): ");
            if (scanner.hasNextInt()) {
                angka = scanner.nextInt();
                if (angka >= 0 && angka <= NILAI_MAKS) {
                    inputValid = true;
                } else {
                    System.out.println("Bilangan di luar jangkauan, coba lagi!");
                }
            } else {
                System.out.println("Input harus berupa angka bulat!");
                scanner.next(); // buang input yang salah
            }
        }
        return angka;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int menu;

        do {
            System.out.println("\n===== KALKULATOR FAKTORIAL =====");
            System.out.println("1. Hitung faktorial satu bilangan");
            System.out.println("2. Tampilkan tabel faktorial");
            System.out.println("3. Bandingkan iteratif vs rekursif");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu: ");

            while (!scanner.hasNextInt()) {
                System.out.print("Pilihan harus angka, ulangi: ");
                scanner.next();
            }
            menu = scanner.nextInt();

            switch (menu) {
                case 1:
                    int x = bacaBilangan(scanner);
                    System.out.println(x + "! = " + hitung(x));
                    break;
                case 2:
                    int y = bacaBilangan(scanner);
                    tampilkanTabel(y);
                    break;
                case 3:
                    int z = bacaBilangan(scanner);
                    bandingkanMetode(z);
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