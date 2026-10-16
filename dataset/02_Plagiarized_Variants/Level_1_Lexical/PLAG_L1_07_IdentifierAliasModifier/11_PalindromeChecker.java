/*
 * Program   : PalindromeChecker
 * Deskripsi : Mengecek apakah sebuah kata atau kalimat merupakan
 *             palindrom. Dilengkapi pengecekan kalimat (mengabaikan
 *             spasi dan tanda baca), membalik teks, menghitung jumlah
 *             huruf, pengecekan palindrom angka, input dari pengguna,
 *             dan menu sederhana.
 * Modifikasi: Obfuscation Pointer & Variable Aliasing serta modifikasi
 *             penulisan increment/decrement pointer.
 */
import java.util.Scanner;

public class PalindromeChecker {

    // Logika inti: mengecek palindrom dengan aliasing array karakter dan modifikasi pointer dua arah
    public boolean isPalindrome(String str) {
        // Variable aliasing string menjadi array karakter
        char[] charSequence = str.toLowerCase().toCharArray();
        int leftPtr = 0;
        int rightPtr = charSequence.length - 1;

        while (leftPtr < rightPtr) {
            if (charSequence[leftPtr] != charSequence[rightPtr]) {
                return false;
            }
            leftPtr += 1;  // modifikasi increment pointer
            rightPtr -= 1; // modifikasi decrement pointer
        }
        return true;
    }

    // Membuang semua karakter selain huruf dan angka dengan penelusuran pointer
    public String bersihkanTeks(String teks) {
        char[] sourceChars = teks.toCharArray(); // Variable aliasing
        StringBuilder hasil = new StringBuilder();

        int scanPtr = 0;
        while (scanPtr < sourceChars.length) {
            char c = sourceChars[scanPtr];
            if (Character.isLetterOrDigit(c)) {
                hasil.append(c);
            }
            scanPtr += 1; // modifikasi increment pointer
        }
        return hasil.toString();
    }

    // Mengecek palindrom untuk kalimat (spasi dan tanda baca diabaikan)
    public boolean isPalindromeKalimat(String kalimat) {
        String bersih = bersihkanTeks(kalimat);
        return isPalindrome(bersih);
    }

    // Membalik teks dari belakang ke depan dengan decrement pointer array
    public String balikTeks(String teks) {
        char[] bufferChars = teks.toCharArray(); // Variable aliasing
        StringBuilder hasil = new StringBuilder();

        int revPtr = bufferChars.length - 1;
        while (revPtr >= 0) {
            hasil.append(bufferChars[revPtr]);
            revPtr -= 1; // modifikasi decrement pointer
        }
        return hasil.toString();
    }

    // Mengecek apakah sebuah angka merupakan palindrom
    public boolean isPalindromeAngka(int angka) {
        if (angka < 0) {
            return false;
        }
        int asli = angka;
        int terbalik = 0;
        while (angka > 0) {
            int digit = angka % 10;
            terbalik = terbalik * 10 + digit;
            angka = angka / 10;
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