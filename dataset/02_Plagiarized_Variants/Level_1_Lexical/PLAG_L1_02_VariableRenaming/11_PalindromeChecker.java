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
    public boolean cekPalindrom(String teks) {
        String normal = teks.toLowerCase();
        int awal = 0;
        int akhir = normal.length() - 1;
        while (awal < akhir) {
            if (normal.charAt(awal) != normal.charAt(akhir)) {
                return false;
            }
            awal++;
            akhir--;
        }
        return true;
    }

    // Membuang semua karakter selain huruf dan angka
    public String bersihkan(String str) {
        String bersih = "";
        for (int pos = 0; pos < str.length(); pos++) {
            char kar = str.charAt(pos);
            if (Character.isLetterOrDigit(kar)) {
                bersih += kar;
            }
        }
        return bersih;
    }

    // Mengecek palindrom untuk kalimat (spasi dan tanda baca diabaikan)
    public boolean cekPalindromKalimat(String kalimat) {
        String murni = bersihkan(kalimat);
        return cekPalindrom(murni);
    }

    // Membalik teks dari belakang ke depan
    public String balikkan(String str) {
        String balik = "";
        for (int pos = str.length() - 1; pos >= 0; pos--) {
            balik += str.charAt(pos);
        }
        return balik;
    }

    // Mengecek apakah sebuah angka merupakan palindrom
    public boolean cekPalindromAngka(int num) {
        if (num < 0) {
            return false;
        }
        int asli = num;
        int dibalik = 0;
        while (num > 0) {
            int digit = num % 10;
            dibalik = dibalik * 10 + digit;
            num = num / 10;
        }
        return asli == dibalik;
    }

    // Menghitung jumlah huruf dan angka dalam teks
    public int cacahKarakter(String str) {
        return bersihkan(str).length();
    }

    // Membaca bilangan bulat dengan validasi sederhana
    public static int ambilAngka(Scanner scanner, String teks) {
        System.out.print(teks);
        while (!scanner.hasNextInt()) {
            System.out.print("Input harus angka bulat, ulangi: ");
            scanner.next();
        }
        int val = scanner.nextInt();
        scanner.nextLine(); // buang sisa baris
        return val;
    }

    // Membaca satu baris teks dari pengguna
    public static String ambilTeks(Scanner scanner, String teks) {
        System.out.print(teks);
        return scanner.nextLine();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PalindromeChecker alat = new PalindromeChecker();

        int menu;
        do {
            System.out.println("\n===== PROGRAM PALINDROM =====");
            System.out.println("1. Cek palindrom kata");
            System.out.println("2. Cek palindrom kalimat");
            System.out.println("3. Cek palindrom angka");
            System.out.println("4. Balik teks");
            System.out.println("0. Keluar");
            menu = ambilAngka(scanner, "Pilih menu: ");

            switch (menu) {
                case 1:
                    String kata = ambilTeks(scanner, "Masukkan kata: ");
                    if (alat.cekPalindrom(kata)) {
                        System.out.println("\"" + kata + "\" adalah palindrom.");
                    } else {
                        System.out.println("\"" + kata + "\" bukan palindrom.");
                    }
                    break;
                case 2:
                    String kalimat = ambilTeks(scanner, "Masukkan kalimat: ");
                    System.out.println("Jumlah huruf dan angka : "
                            + alat.cacahKarakter(kalimat));
                    if (alat.cekPalindromKalimat(kalimat)) {
                        System.out.println("Kalimat tersebut adalah palindrom.");
                    } else {
                        System.out.println("Kalimat tersebut bukan palindrom.");
                    }
                    break;
                case 3:
                    int num = ambilAngka(scanner, "Masukkan angka: ");
                    if (alat.cekPalindromAngka(num)) {
                        System.out.println(num + " adalah palindrom.");
                    } else {
                        System.out.println(num + " bukan palindrom.");
                    }
                    break;
                case 4:
                    String str = ambilTeks(scanner, "Masukkan teks: ");
                    System.out.println("Hasil dibalik : " + alat.balikkan(str));
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