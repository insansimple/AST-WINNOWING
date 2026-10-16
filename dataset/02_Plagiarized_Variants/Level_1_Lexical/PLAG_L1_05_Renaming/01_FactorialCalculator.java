/*
 * Program   : FactorialCalculator
 * Deskripsi : Menghitung faktorial dari sebuah bilangan menggunakan
 *             cara iteratif dan rekursif, serta menampilkan tabel
 *             faktorial. Dilengkapi menu sederhana dan validasi input.
 */
import java.util.Scanner;

public class FactorialCalculator {

    // Batas maksimal agar hasil tidak overflow pada tipe long
    static final int LIMIT_ATAS = 20;

    // Menghitung faktorial secara iteratif (logika inti)
    public static long hitungIter(int angka) {
        if (angka <= 1) {
            return 1;
        }
        long hsl = 1;
        for (int k = 2; k <= angka; k++) {
            hsl = hsl * k;
        }
        return hsl;
    }

    // Menghitung faktorial secara rekursif sebagai pembanding
    public static long hitungRek(int angka) {
        if (angka <= 1) {
            return 1;
        }
        return angka * hitungRek(angka - 1);
    }

    // Menampilkan tabel faktorial dari 0 sampai n
    public static void cetakTabel(int angka) {
        System.out.print(new StringBuilder().append("\n===== Tabel Faktorial 0 s.d. ").append(angka).append(" =====").toString());
        System.out.print(System.lineSeparator());
        System.out.print("n\t| n!");
        System.out.print(System.lineSeparator());
        System.out.print("--------------------------");
        System.out.print(System.lineSeparator());
        for (int k = 0; k <= angka; k++) {
            System.out.print(new StringBuilder().append(k).append("\t| ").append(hitungIter(k)).toString());
            System.out.print(System.lineSeparator());
        }
    }

    // Membandingkan hasil iteratif dan rekursif
    public static void cekPerbandingan(int angka) {
        long hslIter = hitungIter(angka);
        long hslRek = hitungRek(angka);

        System.out.print(new StringBuilder().append("\nHasil iteratif  : ").append(hslIter).toString());
        System.out.print(System.lineSeparator());
        System.out.print(new StringBuilder().append("Hasil rekursif  : ").append(hslRek).toString());
        System.out.print(System.lineSeparator());

        if (hslIter == hslRek) {
            System.out.print("Kedua metode menghasilkan nilai yang sama.");
            System.out.print(System.lineSeparator());
        } else {
            System.out.print("Hasil berbeda, ada yang salah!");
            System.out.print(System.lineSeparator());
        }
    }

    // Membaca bilangan dari pengguna dengan validasi
    public static int ambilAngka(Scanner pemindai) {
        int angka = -1;
        boolean sah = false;

        while (!sah) {
            System.out.print(new StringBuilder().append("Masukkan bilangan (0-").append(LIMIT_ATAS).append("): ").toString());
            if (pemindai.hasNextInt()) {
                angka = pemindai.nextInt();
                if (angka >= 0 && angka <= LIMIT_ATAS) {
                    sah = true;
                } else {
                    System.out.print("Bilangan di luar jangkauan, coba lagi!");
                    System.out.print(System.lineSeparator());
                }
            } else {
                System.out.print("Input harus berupa angka bulat!");
                System.out.print(System.lineSeparator());
                pemindai.next(); // buang input yang salah
            }
        }
        return angka;
    }

    public static void main(String[] args) {
        Scanner pemindai = new Scanner(System.in);
        int opsi;

        do {
            System.out.print("\n===== KALKULATOR FAKTORIAL =====");
            System.out.print(System.lineSeparator());
            System.out.print("1. Hitung faktorial satu bilangan");
            System.out.print(System.lineSeparator());
            System.out.print("2. Tampilkan tabel faktorial");
            System.out.print(System.lineSeparator());
            System.out.print("3. Bandingkan iteratif vs rekursif");
            System.out.print(System.lineSeparator());
            System.out.print("0. Keluar");
            System.out.print(System.lineSeparator());
            System.out.print("Pilih menu: ");

            while (!pemindai.hasNextInt()) {
                System.out.print("Pilihan harus angka, ulangi: ");
                pemindai.next();
            }
            opsi = pemindai.nextInt();

            switch (opsi) {
                case 1:
                    int x1 = ambilAngka(pemindai);
                    System.out.print(new StringBuilder().append(x1).append("! = ").append(hitungIter(x1)).toString());
                    System.out.print(System.lineSeparator());
                    break;
                case 2:
                    int x2 = ambilAngka(pemindai);
                    cetakTabel(x2);
                    break;
                case 3:
                    int x3 = ambilAngka(pemindai);
                    cekPerbandingan(x3);
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