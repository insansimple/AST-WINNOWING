/*
 * Program   : FibonacciCalculator
 * Deskripsi : Menghitung bilangan Fibonacci ke-n secara iteratif dan
 *             rekursif. Dilengkapi deret Fibonacci, jumlah deret,
 *             pengecekan bilangan Fibonacci, perbandingan kedua metode,
 *             input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner;

public class FibonacciCalculator {

    // Batas maksimal agar hasil tidak overflow pada tipe int (F(46) = 1836311903)
    static final int INDEKS_MAKS = 46;

    // Logika inti: menghitung Fibonacci ke-n secara iteratif
    public int fib(int suku) {
        if (suku <= 0) return 0;
        if (suku == 1) return 1;
        int sebelum = 0, sekarang = 1;
        for (int idx = 2; idx <= suku; idx++) {
            int berikut = sebelum + sekarang;
            sebelum = sekarang;
            sekarang = berikut;
        }
        return sekarang;
    }

    // Menghitung Fibonacci ke-n secara rekursif sebagai pembanding
    public int fibRekursif(int suku) {
        if (suku <= 0) return 0;
        if (suku == 1) return 1;
        return fibRekursif(suku - 1) + fibRekursif(suku - 2);
    }

    // Menampilkan deret Fibonacci dari suku ke-0 sampai ke-n
    public void tampilkanDeret(int suku) {
        System.out.print("Deret Fibonacci : ");
        for (int idx = 0; idx <= suku; idx++) {
            System.out.print(fib(idx));
            if (idx < suku) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }

    // Menghitung jumlah semua suku dari ke-0 sampai ke-n (pakai long agar aman)
    public long jumlahDeret(int suku) {
        long akumulasi = 0;
        for (int idx = 0; idx <= suku; idx++) {
            akumulasi += fib(idx);
        }
        return akumulasi;
    }

    // Mengecek apakah sebuah bilangan termasuk bilangan Fibonacci
    public boolean adalahFibonacci(int num) {
        if (num < 0) {
            return false;
        }
        int pos = 0;
        int cek = fib(pos);
        while (cek < num && pos < INDEKS_MAKS) {
            pos++;
            cek = fib(pos);
        }
        return cek == num;
    }

    // Membandingkan hasil iteratif dan rekursif
    public void bandingkanMetode(int suku) {
        int nilaiIter = fib(suku);
        int nilaiRek = fibRekursif(suku);

        System.out.println("Hasil iteratif : " + nilaiIter);
        System.out.println("Hasil rekursif : " + nilaiRek);

        if (nilaiIter == nilaiRek) {
            System.out.println("Kedua metode menghasilkan nilai yang sama.");
        } else {
            System.out.println("Hasil berbeda, ada yang salah!");
        }
    }

    // Membaca bilangan bulat dengan validasi sederhana
    public static int ambilAngka(Scanner scanner, String teks) {
        System.out.print(teks);
        while (!scanner.hasNextInt()) {
            System.out.print("Input harus angka bulat, ulangi: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    // Membaca suku ke-n dengan validasi rentang
    public static int ambilSuku(Scanner scanner) {
        int suku = -1;
        while (suku < 0 || suku > INDEKS_MAKS) {
            suku = ambilAngka(scanner, "Masukkan n (0-" + INDEKS_MAKS + "): ");
            if (suku < 0 || suku > INDEKS_MAKS) {
                System.out.println("Nilai n di luar jangkauan, coba lagi!");
            }
        }
        return suku;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FibonacciCalculator alat = new FibonacciCalculator();

        int menu;
        do {
            System.out.println("\n===== PROGRAM FIBONACCI =====");
            System.out.println("1. Hitung Fibonacci ke-n");
            System.out.println("2. Tampilkan deret Fibonacci");
            System.out.println("3. Jumlah deret Fibonacci");
            System.out.println("4. Cek apakah bilangan Fibonacci");
            System.out.println("5. Bandingkan iteratif vs rekursif");
            System.out.println("0. Keluar");
            menu = ambilAngka(scanner, "Pilih menu: ");

            switch (menu) {
                case 1:
                    int s1 = ambilSuku(scanner);
                    System.out.println("F(" + s1 + ") = " + alat.fib(s1));
                    break;
                case 2:
                    int s2 = ambilSuku(scanner);
                    alat.tampilkanDeret(s2);
                    break;
                case 3:
                    int s3 = ambilSuku(scanner);
                    System.out.println("Jumlah suku ke-0 s.d. ke-" + s3 + " = "
                            + alat.jumlahDeret(s3));
                    break;
                case 4:
                    int num = ambilAngka(scanner, "Masukkan bilangan: ");
                    if (alat.adalahFibonacci(num)) {
                        System.out.println(num + " adalah bilangan Fibonacci.");
                    } else {
                        System.out.println(num + " bukan bilangan Fibonacci.");
                    }
                    break;
                case 5:
                    // Batasi n agar rekursif tidak terlalu lama
                    int s5 = -1;
                    while (s5 < 0 || s5 > 35) {
                        s5 = ambilAngka(scanner, "Masukkan n (0-35): ");
                        if (s5 < 0 || s5 > 35) {
                            System.out.println("Nilai n di luar jangkauan, coba lagi!");
                        }
                    }
                    alat.bandingkanMetode(s5);
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