/*
 * Program   : PrimeChecker
 * Deskripsi : Mengecek apakah sebuah bilangan merupakan bilangan prima.
 *             Dilengkapi daftar bilangan prima dalam rentang tertentu,
 *             penghitungan jumlah prima, pencarian prima berikutnya,
 *             faktorisasi prima, input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner;

public class PrimeChecker {

    // Logika inti: mengecek bilangan prima sampai akar dari num
    public boolean adalahPrima(int num) {
        if (num <= 1) return false;
        for (int pembagi = 2; pembagi * pembagi <= num; pembagi++) {
            if (num % pembagi == 0) {
                return false;
            }
        }
        return true;
    }

    // Menampilkan semua bilangan prima dari batas1 sampai batas2
    public int tampilkanPrima(int batas1, int batas2) {
        int cacah = 0;
        System.out.print("Bilangan prima " + batas1 + " s.d. " + batas2 + " : ");
        for (int num = batas1; num <= batas2; num++) {
            if (adalahPrima(num)) {
                System.out.print(num + " ");
                cacah++;
            }
        }
        if (cacah == 0) {
            System.out.print("(tidak ada)");
        }
        System.out.println();
        return cacah;
    }

    // Mencari bilangan prima pertama yang lebih besar dari num
    public int primaSelanjutnya(int num) {
        int calon = num + 1;
        while (!adalahPrima(calon)) {
            calon++;
        }
        return calon;
    }

    // Menampilkan faktorisasi prima dari sebuah bilangan
    public void faktorisasiPrima(int num) {
        if (num < 2) {
            System.out.println("Faktorisasi prima hanya untuk bilangan >= 2.");
            return;
        }
        System.out.print(num + " = ");
        int sisa = num;
        boolean awal = true;
        for (int pembagi = 2; pembagi * pembagi <= sisa; pembagi++) {
            while (sisa % pembagi == 0) {
                if (!awal) {
                    System.out.print(" x ");
                }
                System.out.print(pembagi);
                awal = false;
                sisa = sisa / pembagi;
            }
        }
        // Jika masih ada sisa, berarti sisa itu sendiri bilangan prima
        if (sisa > 1) {
            if (!awal) {
                System.out.print(" x ");
            }
            System.out.print(sisa);
        }
        System.out.println();
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PrimeChecker alat = new PrimeChecker();

        int menu;
        do {
            System.out.println("\n===== PROGRAM BILANGAN PRIMA =====");
            System.out.println("1. Cek apakah sebuah bilangan prima");
            System.out.println("2. Tampilkan prima dalam rentang");
            System.out.println("3. Cari prima berikutnya");
            System.out.println("4. Faktorisasi prima");
            System.out.println("0. Keluar");
            menu = ambilAngka(scanner, "Pilih menu: ");

            switch (menu) {
                case 1:
                    int bilangan = ambilAngka(scanner, "Masukkan bilangan: ");
                    if (alat.adalahPrima(bilangan)) {
                        System.out.println(bilangan + " adalah bilangan prima.");
                    } else {
                        System.out.println(bilangan + " bukan bilangan prima.");
                    }
                    break;
                case 2:
                    int dari = ambilAngka(scanner, "Batas awal  : ");
                    int sampai = ambilAngka(scanner, "Batas akhir : ");
                    if (dari > sampai) {
                        System.out.println("Batas awal tidak boleh lebih besar dari batas akhir!");
                    } else {
                        int cacah = alat.tampilkanPrima(dari, sampai);
                        System.out.println("Jumlah bilangan prima : " + cacah);
                    }
                    break;
                case 3:
                    int bil = ambilAngka(scanner, "Masukkan bilangan: ");
                    System.out.println("Prima berikutnya setelah " + bil + " adalah "
                            + alat.primaSelanjutnya(bil));
                    break;
                case 4:
                    int fak = ambilAngka(scanner, "Masukkan bilangan: ");
                    alat.faktorisasiPrima(fak);
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