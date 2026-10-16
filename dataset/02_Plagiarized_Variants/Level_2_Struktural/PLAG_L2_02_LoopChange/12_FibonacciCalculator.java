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
    static final int BATAS_MAKS = 46;

    // Logika inti: menghitung Fibonacci ke-n secara iteratif
    public int fibonacci(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        int prev = 0, curr = 1;
        int i = 2;
        while (i <= n) {
            int next = prev + curr;
            prev = curr;
            curr = next;
            i++;
        }
        return curr;
    }

    // Menghitung Fibonacci ke-n secara rekursif sebagai pembanding
    public int fibonacciRekursif(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        return fibonacciRekursif(n - 1) + fibonacciRekursif(n - 2);
    }

    // Menampilkan deret Fibonacci dari suku ke-0 sampai ke-n
    public void tampilkanDeret(int n) {
        System.out.print("Deret Fibonacci : ");
        int i = 0;
        while (i <= n) {
            System.out.print(fibonacci(i));
            if (i < n) {
                System.out.print(", ");
            }
            i++;
        }
        System.out.println();
    }

    // Menghitung jumlah semua suku dari ke-0 sampai ke-n (pakai long agar aman)
    public long jumlahDeret(int n) {
        long total = 0;
        int i = 0;
        while (i <= n) {
            total += fibonacci(i);
            i++;
        }
        return total;
    }

    // Mengecek apakah sebuah bilangan termasuk bilangan Fibonacci
    public boolean isFibonacci(int angka) {
        if (angka < 0) {
            return false;
        }
        int i = 0;
        int nilai = fibonacci(i);
        for (; nilai < angka && i < BATAS_MAKS; ) {
            i++;
            nilai = fibonacci(i);
        }
        return nilai == angka;
    }

    // Membandingkan hasil iteratif dan rekursif
    public void bandingkanMetode(int n) {
        int hasilIteratif = fibonacci(n);
        int hasilRekursif = fibonacciRekursif(n);

        System.out.println("Hasil iteratif : " + hasilIteratif);
        System.out.println("Hasil rekursif : " + hasilRekursif);

        if (hasilIteratif == hasilRekursif) {
            System.out.println("Kedua metode menghasilkan nilai yang sama.");
        } else {
            System.out.println("Hasil berbeda, ada yang salah!");
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

    // Membaca suku ke-n dengan validasi rentang
    public static int bacaSuku(Scanner input) {
        int n = -1;
        while (n < 0 || n > BATAS_MAKS) {
            n = bacaAngka(input, "Masukkan n (0-" + BATAS_MAKS + "): ");
            if (n < 0 || n > BATAS_MAKS) {
                System.out.println("Nilai n di luar jangkauan, coba lagi!");
            }
        }
        return n;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        FibonacciCalculator kalkulator = new FibonacciCalculator();

        int pilihan;
        do {
            System.out.println("\n===== PROGRAM FIBONACCI =====");
            System.out.println("1. Hitung Fibonacci ke-n");
            System.out.println("2. Tampilkan deret Fibonacci");
            System.out.println("3. Jumlah deret Fibonacci");
            System.out.println("4. Cek apakah bilangan Fibonacci");
            System.out.println("5. Bandingkan iteratif vs rekursif");
            System.out.println("0. Keluar");
            pilihan = bacaAngka(input, "Pilih menu: ");

            switch (pilihan) {
                case 1:
                    int n1 = bacaSuku(input);
                    System.out.println("F(" + n1 + ") = " + kalkulator.fibonacci(n1));
                    break;
                case 2:
                    int n2 = bacaSuku(input);
                    kalkulator.tampilkanDeret(n2);
                    break;
                case 3:
                    int n3 = bacaSuku(input);
                    System.out.println("Jumlah suku ke-0 s.d. ke-" + n3 + " = "
                            + kalkulator.jumlahDeret(n3));
                    break;
                case 4:
                    int angka = bacaAngka(input, "Masukkan bilangan: ");
                    if (kalkulator.isFibonacci(angka)) {
                        System.out.println(angka + " adalah bilangan Fibonacci.");
                    } else {
                        System.out.println(angka + " bukan bilangan Fibonacci.");
                    }
                    break;
                case 5:
                    // Batasi n agar rekursif tidak terlalu lama
                    int n5 = -1;
                    while (n5 < 0 || n5 > 35) {
                        n5 = bacaAngka(input, "Masukkan n (0-35): ");
                        if (n5 < 0 || n5 > 35) {
                            System.out.println("Nilai n di luar jangkauan, coba lagi!");
                        }
                    }
                    kalkulator.bandingkanMetode(n5);
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