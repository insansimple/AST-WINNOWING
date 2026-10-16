/*
 * Program   : PrimeChecker
 * Deskripsi : Mengecek apakah sebuah bilangan merupakan bilangan prima.
 *             Dilengkapi daftar bilangan prima dalam rentang tertentu,
 *             penghitungan jumlah prima, pencarian prima berikutnya,
 *             faktorisasi prima, input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner;

public class PrimeChecker {

    // Logika inti: mengecek bilangan prima sampai akar dari n
    public Boolean isPrime(Integer n) {
        if (n <= 1) return false;
        for (Integer i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    // Menampilkan semua bilangan prima dari awal sampai akhir
    public Integer tampilkanPrima(Integer awal, Integer akhir) {
        Integer jumlah = 0;
        System.out.print("Bilangan prima " + awal + " s.d. " + akhir + " : ");
        for (Integer i = awal; i <= akhir; i++) {
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

    // Mencari bilangan prima pertama yang lebih besar dari n
    public Integer primaBerikutnya(Integer n) {
        Integer kandidat = n + 1;
        while (!isPrime(kandidat)) {
            kandidat++;
        }
        return kandidat;
    }

    // Menampilkan faktorisasi prima dari sebuah bilangan
    public void faktorisasiPrima(Integer n) {
        if (n < 2) {
            System.out.println("Faktorisasi prima hanya untuk bilangan >= 2.");
            return;
        }
        System.out.print(n + " = ");
        Integer sisa = n;
        Boolean pertama = true;
        for (Integer i = 2; i * i <= sisa; i++) {
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

    // Membaca bilangan bulat dengan validasi sederhana
    static public Integer bacaAngka(Scanner input, String pesan) {
        System.out.print(pesan);
        while (!input.hasNextInt()) {
            System.out.print("Input harus angka bulat, ulangi: ");
            input.next();
        }
        return input.nextInt();
    }

    static public void main(String[] args) {
        Scanner input = new Scanner(System.in);
        PrimeChecker pengecek = new PrimeChecker();

        Integer pilihan;
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
                    Integer n = bacaAngka(input, "Masukkan bilangan: ");
                    if (pengecek.isPrime(n)) {
                        System.out.println(n + " adalah bilangan prima.");
                    } else {
                        System.out.println(n + " bukan bilangan prima.");
                    }
                    break;
                case 2:
                    Integer awal = bacaAngka(input, "Batas awal  : ");
                    Integer akhir = bacaAngka(input, "Batas akhir : ");
                    if (awal > akhir) {
                        System.out.println("Batas awal tidak boleh lebih besar dari batas akhir!");
                    } else {
                        Integer jumlah = pengecek.tampilkanPrima(awal, akhir);
                        System.out.println("Jumlah bilangan prima : " + jumlah);
                    }
                    break;
                case 3:
                    Integer m = bacaAngka(input, "Masukkan bilangan: ");
                    System.out.println("Prima berikutnya setelah " + m + " adalah "
                            + pengecek.primaBerikutnya(m));
                    break;
                case 4:
                    Integer f = bacaAngka(input, "Masukkan bilangan: ");
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
}