/*
 * Program   : FactorialCalculator
 * Deskripsi : Menghitung faktorial dari sebuah bilangan menggunakan
 *             cara iteratif dan rekursif, serta menampilkan tabel
 *             faktorial. Dilengkapi menu sederhana dan validasi input.
 */
import java.util.Scanner;

public class FactorialCalculator {

    // Batas maksimal agar hasil tidak overflow pada tipe long
    static final int BATAS_MAKS = 20;

    // Inner static class wrapper untuk enkapsulasi state komputasi faktorial
    public static class FactorialContext {
        private int number;
        private long value;

        public FactorialContext(int number) {
            this.number = number;
            this.value = 1;
        }

        public int getNumber() {
            return number;
        }

        public long getValue() {
            return value;
        }

        public void multiplyBy(int factor) {
            this.value = this.value * factor;
        }

        public void setValue(long value) {
            this.value = value;
        }
    }

    // Inner static class wrapper untuk hasil perbandingan metode
    public static class ComparisonContext {
        private long iterativeResult;
        private long recursiveResult;

        public ComparisonContext(long iter, long rec) {
            this.iterativeResult = iter;
            this.recursiveResult = rec;
        }

        public long getIterativeResult() {
            return iterativeResult;
        }

        public long getRecursiveResult() {
            return recursiveResult;
        }

        public boolean isIdentical() {
            return iterativeResult == recursiveResult;
        }
    }

    // Menghitung faktorial secara iteratif (logika inti)
    public static long compute(int n) {
        FactorialContext ctx = new FactorialContext(n);
        if (ctx.getNumber() <= 1) {
            return 1;
        }
        for (int i = 2; i <= ctx.getNumber(); i++) {
            ctx.multiplyBy(i);
        }
        return ctx.getValue();
    }

    // Menghitung faktorial secara rekursif sebagai pembanding
    public static long computeRekursif(int n) {
        FactorialContext ctx = new FactorialContext(n);
        if (ctx.getNumber() <= 1) {
            return 1;
        }
        ctx.setValue(ctx.getNumber() * computeRekursif(ctx.getNumber() - 1));
        return ctx.getValue();
    }

    // Menampilkan tabel faktorial dari 0 sampai n
    public static void tampilkanTabel(int n) {
        System.out.println("\n===== Tabel Faktorial 0 s.d. " + n + " =====");
        System.out.println("n\t| n!");
        System.out.println("--------------------------");
        for (int i = 0; i <= n; i++) {
            FactorialContext item = new FactorialContext(i);
            System.out.println(item.getNumber() + "\t| " + compute(item.getNumber()));
        }
    }

    // Membandingkan hasil iteratif dan rekursif
    public static void bandingkanMetode(int n) {
        ComparisonContext comp = new ComparisonContext(compute(n), computeRekursif(n));

        System.out.println("\nHasil iteratif  : " + comp.getIterativeResult());
        System.out.println("Hasil rekursif  : " + comp.getRecursiveResult());

        if (comp.isIdentical()) {
            System.out.println("Kedua metode menghasilkan nilai yang sama.");
        } else {
            System.out.println("Hasil berbeda, ada yang salah!");
        }
    }

    // Membaca bilangan dari pengguna dengan validasi
    public static int bacaBilangan(Scanner input) {
        int n = -1;
        boolean valid = false;

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

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int pilihan;

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
                    int a = bacaBilangan(input);
                    FactorialContext ctxA = new FactorialContext(a);
                    System.out.println(ctxA.getNumber() + "! = " + compute(ctxA.getNumber()));
                    break;
                case 2:
                    int b = bacaBilangan(input);
                    tampilkanTabel(b);
                    break;
                case 3:
                    int c = bacaBilangan(input);
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