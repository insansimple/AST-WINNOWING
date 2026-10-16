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
    public boolean periksaPalindrom(String untaian) {
        String seragam = untaian.toLowerCase();
        int ujungKiri = 0;
        int ujungKanan = seragam.length() - 1;
        while (ujungKiri < ujungKanan) {
            if (seragam.charAt(ujungKiri) != seragam.charAt(ujungKanan)) {
                return false;
            }
            ujungKiri++;
            ujungKanan--;
        }
        return true;
    }

    // Membuang semua karakter selain huruf dan angka
    public String saringTeks(String bahan) {
        StringBuilder penampung = new StringBuilder();
        for (int k = 0; k < bahan.length(); k++) {
            char huruf = bahan.charAt(k);
            if (Character.isLetterOrDigit(huruf)) {
                penampung.append(huruf);
            }
        }
        return penampung.toString();
    }

    // Mengecek palindrom untuk kalimat (spasi dan tanda baca diabaikan)
    public boolean periksaPalindromKalimat(String ucapan) {
        String murni = saringTeks(ucapan);
        return periksaPalindrom(murni);
    }

    // Membalik teks dari belakang ke depan
    public String putarBalik(String bahan) {
        StringBuilder penampung = new StringBuilder();
        for (int k = bahan.length() - 1; k >= 0; k--) {
            penampung.append(bahan.charAt(k));
        }
        return penampung.toString();
    }

    // Mengecek apakah sebuah angka merupakan palindrom
    public boolean periksaPalindromAngka(int bilangan) {
        if (bilangan < 0) {
            return false;
        }
        int semula = bilangan;
        int kebalikan = 0;
        while (bilangan > 0) {
            int satuan = bilangan % 10;
            kebalikan = kebalikan * 10 + satuan;
            bilangan = bilangan / 10;
        }
        return semula == kebalikan;
    }

    // Menghitung jumlah huruf dan angka dalam teks
    public int cacahHurufAngka(String bahan) {
        return saringTeks(bahan).length();
    }

    // Membaca bilangan bulat dengan validasi sederhana
    public static int ambilInteger(Scanner pemindai, String keterangan) {
        System.out.print(keterangan);
        while (!pemindai.hasNextInt()) {
            System.out.print("Input harus angka bulat, ulangi: ");
            pemindai.next();
        }
        int hasilBaca = pemindai.nextInt();
        pemindai.nextLine(); // buang sisa baris
        return hasilBaca;
    }

    // Membaca satu baris teks dari pengguna
    public static String ambilBaris(Scanner pemindai, String keterangan) {
        System.out.print(keterangan);
        return pemindai.nextLine();
    }

    public static void main(String[] args) {
        Scanner pemindai = new Scanner(System.in);
        PalindromeChecker penguji = new PalindromeChecker();

        int opsi;
        do {
            System.out.print("\n===== PROGRAM PALINDROM =====");
            System.out.print(System.lineSeparator());
            System.out.print("1. Cek palindrom kata");
            System.out.print(System.lineSeparator());
            System.out.print("2. Cek palindrom kalimat");
            System.out.print(System.lineSeparator());
            System.out.print("3. Cek palindrom angka");
            System.out.print(System.lineSeparator());
            System.out.print("4. Balik teks");
            System.out.print(System.lineSeparator());
            System.out.print("0. Keluar");
            System.out.print(System.lineSeparator());
            opsi = ambilInteger(pemindai, "Pilih menu: ");

            switch (opsi) {
                case 1:
                    String masukanKata = ambilBaris(pemindai, "Masukkan kata: ");
                    if (penguji.periksaPalindrom(masukanKata)) {
                        System.out.print(new StringBuilder().append("\"").append(masukanKata).append("\" adalah palindrom.").toString());
                        System.out.print(System.lineSeparator());
                    } else {
                        System.out.print(new StringBuilder().append("\"").append(masukanKata).append("\" bukan palindrom.").toString());
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 2:
                    String masukanKalimat = ambilBaris(pemindai, "Masukkan kalimat: ");
                    System.out.print(new StringBuilder().append("Jumlah huruf dan angka : ")
                            .append(penguji.cacahHurufAngka(masukanKalimat)).toString());
                    System.out.print(System.lineSeparator());
                    if (penguji.periksaPalindromKalimat(masukanKalimat)) {
                        System.out.print("Kalimat tersebut adalah palindrom.");
                        System.out.print(System.lineSeparator());
                    } else {
                        System.out.print("Kalimat tersebut bukan palindrom.");
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 3:
                    int masukanAngka = ambilInteger(pemindai, "Masukkan angka: ");
                    if (penguji.periksaPalindromAngka(masukanAngka)) {
                        System.out.print(new StringBuilder().append(masukanAngka).append(" adalah palindrom.").toString());
                        System.out.print(System.lineSeparator());
                    } else {
                        System.out.print(new StringBuilder().append(masukanAngka).append(" bukan palindrom.").toString());
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 4:
                    String masukanTeks = ambilBaris(pemindai, "Masukkan teks: ");
                    System.out.print(new StringBuilder().append("Hasil dibalik : ").append(penguji.putarBalik(masukanTeks)).toString());
                    System.out.print(System.lineSeparator());
                    break;
                case 0:
                    System.out.print("Terima kasih, program selesai.");
                    System.out.print(System.lineSeparator());
                    break;
                default:
                    System.out.print("Menu tidak tersedia!");
                    System.out.print(System.lineSeparator());
            }
        } while (opsi != 0);

        pemindai.close();
    }
}