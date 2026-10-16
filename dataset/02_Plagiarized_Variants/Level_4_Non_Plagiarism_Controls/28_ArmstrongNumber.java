/*
 * Program   : ArmstrongNumber
 * Deskripsi : Mengecek apakah suatu angka merupakan bilangan Armstrong
 *             (Narsisistik), yaitu bilangan yang sama dengan jumlah
 *             setiap digitnya dipangkatkan dengan banyak digit.
 *             Contoh: 153 = 1^3 + 5^3 + 3^3 = 1 + 125 + 27 = 153.
 *             Dilengkapi pencarian Armstrong dalam rentang tertentu.
 */
import java.util.Scanner;

public class ArmstrongNumber {

    // Menghitung banyak digit dari suatu bilangan
    public static int hitungDigit(long angka) {
        if (angka == 0) {
            return 1;
        }
        int jumlah = 0;
        while (angka > 0) {
            jumlah++;
            angka = angka / 10;
        }
        return jumlah;
    }

    // Menghitung pangkat bilangan bulat secara manual (tanpa Math.pow)
    public static long pangkat(int dasar, int eksponen) {
        long hasil = 1;
        for (int i = 0; i < eksponen; i++) {
            hasil = hasil * dasar;
        }
        return hasil;
    }

    // Menghitung jumlah setiap digit dipangkatkan banyak digit
    public static long jumlahPangkatDigit(long angka) {
        int banyakDigit = hitungDigit(angka);
        long jumlah = 0;
        long sisa = angka;

        while (sisa > 0) {
            int digit = (int) (sisa % 10);   // ambil digit paling kanan
            jumlah = jumlah + pangkat(digit, banyakDigit);
            sisa = sisa / 10;                // buang digit paling kanan
        }
        return jumlah;
    }

    // Mengecek apakah angka adalah bilangan Armstrong
    public static boolean isArmstrong(long angka) {
        if (angka < 0) {
            return false;
        }
        return jumlahPangkatDigit(angka) == angka;
    }

    // Menampilkan rincian perhitungan, misalnya 1^3 + 5^3 + 3^3 = 153
    public static void tampilkanRincian(long angka) {
        String teks = String.valueOf(angka);
        int banyakDigit = teks.length();
        long total = 0;
        StringBuilder rumus = new StringBuilder();

        for (int i = 0; i < banyakDigit; i++) {
            int digit = teks.charAt(i) - '0';
            long nilai = pangkat(digit, banyakDigit);
            total = total + nilai;

            rumus.append(digit).append("^").append(banyakDigit);
            if (i < banyakDigit - 1) {
                rumus.append(" + ");
            }
        }

        System.out.println("Rincian : " + rumus + " = " + total);
    }

    // Mencari dan menampilkan semua bilangan Armstrong pada rentang awal-akhir
    public static int cariDalamRentang(long awal, long akhir) {
        int jumlahDitemukan = 0;
        System.out.print("Bilangan Armstrong: ");
        for (long i = awal; i <= akhir; i++) {
            if (isArmstrong(i)) {
                System.out.print(i + " ");
                jumlahDitemukan++;
            }
        }
        System.out.println();
        return jumlahDitemukan;
    }

    // Membaca bilangan bulat dari input, mengembalikan -1 jika salah ketik
    public static long bacaAngka(Scanner input, String pesan) {
        System.out.print(pesan);
        try {
            return Long.parseLong(input.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Contoh pengujian dengan data tetap
    public static void tampilkanContoh() {
        long[] contoh = {0, 5, 9, 10, 153, 370, 371, 407, 1634, 9474, 9475};

        System.out.println("=== CONTOH PENGUJIAN ===");
        for (int i = 0; i < contoh.length; i++) {
            String status = isArmstrong(contoh[i]) ? "Armstrong" : "Bukan Armstrong";
            System.out.println(contoh[i] + "\t: " + status);
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int pilihan = -1;

        tampilkanContoh();

        do {
            // Menu utama
            System.out.println("\n=== MENU BILANGAN ARMSTRONG ===");
            System.out.println("1. Cek satu bilangan");
            System.out.println("2. Cari Armstrong dalam rentang");
            System.out.println("0. Keluar");
            pilihan = (int) bacaAngka(input, "Pilihan: ");

            switch (pilihan) {
                case 1:
                    long angka = bacaAngka(input, "Masukkan bilangan (>= 0): ");
                    if (angka < 0) {
                        System.out.println("Input harus bilangan bulat >= 0!");
                    } else {
                        System.out.println("Jumlah digit : " + hitungDigit(angka));
                        tampilkanRincian(angka);
                        if (isArmstrong(angka)) {
                            System.out.println(angka + " adalah bilangan Armstrong.");
                        } else {
                            System.out.println(angka + " bukan bilangan Armstrong.");
                        }
                    }
                    break;
                case 2:
                    long awal = bacaAngka(input, "Batas awal  : ");
                    long akhir = bacaAngka(input, "Batas akhir : ");
                    if (awal < 0 || akhir < awal) {
                        System.out.println("Rentang tidak valid!");
                    } else {
                        int total = cariDalamRentang(awal, akhir);
                        System.out.println("Total ditemukan: " + total + " bilangan");
                    }
                    break;
                case 0:
                    System.out.println("Program selesai.");
                    break;
                default:
                    System.out.println("Pilihan tidak tersedia!");
            }
        } while (pilihan != 0);

        input.close();
    }
}