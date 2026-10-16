/*
 * Program   : PalindromeChecker
 * Deskripsi : Mengecek apakah sebuah kata atau kalimat merupakan
 *             palindrom. Dilengkapi pengecekan kalimat (mengabaikan
 *             spasi dan tanda baca), membalik teks, menghitung jumlah
 *             huruf, pengecekan palindrom angka, input dari pengguna,
 *             dan menu sederhana.
 */
import java.util.Scanner;

public class PalindromeChecker {

    // Logika inti: mengecek palindrom dengan dua penunjuk (kiri dan kanan)
    public boolean isPalindrome(String str) {
        String cleaned = str.toLowerCase();
        for (int left = 0, right = cleaned.length() - 1; left < right; left++, right--) {
            if (cleaned.charAt(left) != cleaned.charAt(right)) {
                return false;
            }
        }
        return true;
    }

    // Membuang semua karakter selain huruf dan angka
    public String bersihkanTeks(String teks) {
        String hasil = "";
        int i = 0;
        while (i < teks.length()) {
            char c = teks.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                hasil += c;
            }
            i++;
        }
        return hasil;
    }

    // Mengecek palindrom untuk kalimat (spasi dan tanda baca diabaikan)
    public boolean isPalindromeKalimat(String kalimat) {
        String bersih = bersihkanTeks(kalimat);
        return isPalindrome(bersih);
    }

    // Membalik teks dari belakang ke depan
    public String balikTeks(String teks) {
        String hasil = "";
        int i = teks.length() - 1;
        while (i >= 0) {
            hasil += teks.charAt(i);
            i--;
        }
        return hasil;
    }

    // Mengecek apakah sebuah angka merupakan palindrom
    public boolean isPalindromeAngka(int angka) {
        if (angka < 0) {
            return false;
        }
        int asli = angka;
        int terbalik = 0;
        for (; angka > 0; angka = angka / 10) {
            int digit = angka % 10;
            terbalik = terbalik * 10 + digit;
        }
        return asli == terbalik;
    }

    // Menghitung jumlah huruf dan angka dalam teks
    public int hitungHurufAngka(String teks) {
        return bersihkanTeks(teks).length();
    }

    // Membaca bilangan bulat dengan validasi sederhana
    public static int bacaAngka(Scanner input, String pesan) {
        System.out.print(pesan);
        while (!input.hasNextInt()) {
            System.out.print("Input harus angka bulat, ulangi: ");
            input.next();
        }
        int nilai = input.nextInt();
        input.nextLine(); // buang sisa baris
        return nilai;
    }

    // Membaca satu baris teks dari pengguna
    public static String bacaTeks(Scanner input, String pesan) {
        System.out.print(pesan);
        return input.nextLine();
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        PalindromeChecker pengecek = new PalindromeChecker();

        int pilihan;
        do {
            System.out.println("\n===== PROGRAM PALINDROM =====");
            System.out.println("1. Cek palindrom kata");
            System.out.println("2. Cek palindrom kalimat");
            System.out.println("3. Cek palindrom angka");
            System.out.println("4. Balik teks");
            System.out.println("0. Keluar");
            pilihan = bacaAngka(input, "Pilih menu: ");

            switch (pilihan) {
                case 1:
                    String kata = bacaTeks(input, "Masukkan kata: ");
                    if (pengecek.isPalindrome(kata)) {
                        System.out.println("\"" + kata + "\" adalah palindrom.");
                    } else {
                        System.out.println("\"" + kata + "\" bukan palindrom.");
                    }
                    break;
                case 2:
                    String kalimat = bacaTeks(input, "Masukkan kalimat: ");
                    System.out.println("Jumlah huruf dan angka : "
                            + pengecek.hitungHurufAngka(kalimat));
                    if (pengecek.isPalindromeKalimat(kalimat)) {
                        System.out.println("Kalimat tersebut adalah palindrom.");
                    } else {
                        System.out.println("Kalimat tersebut bukan palindrom.");
                    }
                    break;
                case 3:
                    int angka = bacaAngka(input, "Masukkan angka: ");
                    if (pengecek.isPalindromeAngka(angka)) {
                        System.out.println(angka + " adalah palindrom.");
                    } else {
                        System.out.println(angka + " bukan palindrom.");
                    }
                    break;
                case 4:
                    String teks = bacaTeks(input, "Masukkan teks: ");
                    System.out.println("Hasil dibalik : " + pengecek.balikTeks(teks));
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