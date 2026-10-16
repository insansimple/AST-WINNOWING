/*
 * Program   : PrimeChecker
 * Deskripsi : Mengecek apakah sebuah bilangan merupakan bilangan prima.
 *             Dilengkapi daftar bilangan prima dalam rentang tertentu,
 *             penghitungan jumlah prima, pencarian prima berikutnya,
 *             faktorisasi prima, input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner;

public class PrimeChecker {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        PrimeChecker pengecek = new PrimeChecker();

        int pilihan;
        do {
            System.out.println("\n===== PROGRAM BILANGAN PRIMA =====");
            System.out.println("1. Cek apakah sebuah bilangan prima");
            System.out.println("2. Tampilkan prima dalam rentang");
            System.out.println("3. Cari prima berikutnya");
            System.out.println("4. Faktorisasi prima");
            System.out.println("0. Keluar");
            pilihan = bacaAngka(input, "Pilih menu: ");

            switch (pilihan) {
                case 1:
                    int n = bacaAngka(input, "Masukkan bilangan: ");
                    if (pengecek.isPrime(n)) {
                        System.out.println(n + " adalah bilangan prima.");
                    } else {
                        System.out.println(n + " bukan bilangan prima.");
                    }
                    break;
                case 2:
                    int awal = bacaAngka(input, "Batas awal  : ");
                    int akhir = bacaAngka(input, "Batas akhir : ");
                    if (awal > akhir) {
                        System.out.println("Batas awal tidak boleh lebih besar dari batas akhir!");
                    } else {
                        int jumlah = pengecek.tampilkanPrima(awal, akhir);
                        System.out.println("Jumlah bilangan prima : " + jumlah);
                    }
                    break;
                case 3:
                    int m = bacaAngka(input, "Masukkan bilangan: ");
                    System.out.println("Prima berikutnya setelah " + m + " adalah "
                            + pengecek.primaBerikutnya(m));
                    break;
                case 4:
                    int f = bacaAngka(input, "Masukkan bilangan: ");
                    pengecek.faktorisasiPrima(f);
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

    // Membaca bilangan bulat dengan validasi sederhana
    public static int bacaAngka(Scanner input, String pesan) {
        System.out.print(pesan);
        while (!input.hasNextInt()) {
            System.out.print("Input harus angka bulat, ulangi: ");
            input.next();
        }
        return input.nextInt();
    }

    // Menampilkan faktorisasi prima dari sebuah bilangan
    public void faktorisasiPrima(int n) {
        if (n < 2) {
            System.out.println("Faktorisasi prima hanya untuk bilangan >= 2.");
            return;
        }
        System.out.print(n + " = ");
        int sisa = n;
        boolean pertama = true;
        for (int i = 2; i * i <= sisa; i++) {
            while (sisa % i == 0) {
                if (!pertama) {
                    System.out.print(" x ");
                }
                System.out.print(i);
                pertama = false;
                sisa = sisa / i;
            }
        }
        // Jika masih ada sisa, berarti sisa itu sendiri bilangan prima
        if (sisa > 1) {
            if (!pertama) {
                System.out.print(" x ");
            }
            System.out.print(sisa);
        }
        System.out.println();
    }

    // Mencari bilangan prima pertama yang lebih besar dari n
    public int primaBerikutnya(int n) {
        int kandidat = n + 1;
        while (!isPrime(kandidat)) {
            kandidat++;
        }
        return kandidat;
    }

    // Menampilkan semua bilangan prima dari awal sampai akhir
    public int tampilkanPrima(int awal, int akhir) {
        int jumlah = 0;
        System.out.print("Bilangan prima " + awal + " s.d. " + akhir + " : ");
        for (int i = awal; i <= akhir; i++) {
            if (isPrime(i)) {
                System.out.print(i + " ");
                jumlah++;
            }
        }
        if (jumlah == 0) {
            System.out.print("(tidak ada)");
        }
        System.out.println();
        return jumlah;
    }

    // Logika inti: mengecek bilangan prima sampai akar dari n
    public boolean isPrime(int n) {
        if (n <= 1) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}