/*
 * Program   : WordCounter
 * Deskripsi : Menghitung jumlah kata dalam sebuah kalimat. Dilengkapi
 *             penghitungan jumlah huruf, kata terpanjang, frekuensi
 *             sebuah kata, rata-rata panjang kata, input dari pengguna,
 *             dan menu sederhana.
 * Modifikasi: Obfuscation Pointer & Variable Aliasing serta modifikasi
 *             penulisan increment/decrement pointer.
 */
import java.util.Scanner;

public class WordCounter {

    // Logika inti: menghitung jumlah kata dengan memecah berdasarkan spasi
    public int countWords(String sentence) {
        if (sentence == null || sentence.trim().isEmpty()) {
            return 0;
        }
        String[] tokenSequence = sentence.trim().split("\\s+"); // Variable aliasing
        return tokenSequence.length;
    }

    // Menghitung jumlah huruf dan angka dengan aliasing array karakter
    public int hitungHuruf(String kalimat) {
        char[] charPool = kalimat.toCharArray(); // Variable aliasing
        int jumlah = 0;
        int charPtr = 0;

        while (charPtr < charPool.length) {
            if (Character.isLetterOrDigit(charPool[charPtr])) {
                jumlah += 1; // modifikasi increment
            }
            charPtr += 1; // modifikasi increment pointer
        }
        return jumlah;
    }

    // Mencari kata terpanjang dalam kalimat dengan aliasing array token
    public String kataTerpanjang(String kalimat) {
        if (countWords(kalimat) == 0) {
            return "";
        }
        String[] wordsArray = kalimat.trim().split("\\s+");
        String[] poolRef = wordsArray; // Variable aliasing
        String terpanjang = poolRef[0];

        int scanIdx = 1;
        while (scanIdx < poolRef.length) {
            if (poolRef[scanIdx].length() > terpanjang.length()) {
                terpanjang = poolRef[scanIdx];
            }
            scanIdx += 1; // modifikasi increment pointer
        }
        return terpanjang;
    }

    // Menghitung berapa kali sebuah kata muncul dengan aliasing
    public int hitungFrekuensi(String kalimat, String target) {
        if (countWords(kalimat) == 0) {
            return 0;
        }
        String[] tokenList = kalimat.trim().split("\\s+"); // Variable aliasing
        int matchCount = 0;
        int tokenPtr = 0;

        while (tokenPtr < tokenList.length) {
            if (tokenList[tokenPtr].equalsIgnoreCase(target)) {
                matchCount += 1; // modifikasi increment
            }
            tokenPtr += 1; // modifikasi increment pointer
        }
        return matchCount;
    }

    // Menghitung rata-rata panjang kata dengan pointer loop
    public double rataRataPanjang(String kalimat) {
        int jumlahKata = countWords(kalimat);
        if (jumlahKata == 0) {
            return 0;
        }
        String[] wordTokens = kalimat.trim().split("\\s+"); // Variable aliasing
        int totalPanjang = 0;
        int idx = 0;

        while (idx < wordTokens.length) {
            totalPanjang += wordTokens[idx].length();
            idx += 1; // modifikasi increment pointer
        }
        return (double) totalPanjang / jumlahKata;
    }

    // Menampilkan setiap kata beserta nomor urutnya dengan aliasing array
    public void tampilkanDaftarKata(String kalimat) {
        if (countWords(kalimat) == 0) {
            System.out.println("Tidak ada kata.");
            return;
        }
        String[] displayWords = kalimat.trim().split("\\s+"); // Variable aliasing
        int wordNum = 0;

        while (wordNum < displayWords.length) {
            System.out.println((wordNum + 1) + ". " + displayWords[wordNum]
                    + " (" + displayWords[wordNum].length() + " karakter)");
            wordNum += 1; // modifikasi increment pointer
        }
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
        WordCounter penghitung = new WordCounter();

        int pilihan;
        do {
            System.out.println("\n===== PROGRAM PENGHITUNG KATA =====");
            System.out.println("1. Hitung jumlah kata");
            System.out.println("2. Hitung jumlah huruf");
            System.out.println("3. Cari kata terpanjang");
            System.out.println("4. Hitung frekuensi sebuah kata");
            System.out.println("5. Rata-rata panjang kata");
            System.out.println("6. Tampilkan daftar kata");
            System.out.println("0. Keluar");
            pilihan = bacaAngka(input, "Pilih menu: ");

            switch (pilihan) {
                case 1:
                    String k1 = bacaTeks(input, "Masukkan kalimat: ");
                    System.out.println("Jumlah kata : " + penghitung.countWords(k1));
                    break;
                case 2:
                    String k2 = bacaTeks(input, "Masukkan kalimat: ");
                    System.out.println("Jumlah huruf dan angka : " + penghitung.hitungHuruf(k2));
                    break;
                case 3:
                    String k3 = bacaTeks(input, "Masukkan kalimat: ");
                    String terpanjang = penghitung.kataTerpanjang(k3);
                    if (terpanjang.isEmpty()) {
                        System.out.println("Kalimat kosong, tidak ada kata.");
                    } else {
                        System.out.println("Kata terpanjang : " + terpanjang
                                + " (" + terpanjang.length() + " karakter)");
                    }
                    break;
                case 4:
                    String k4 = bacaTeks(input, "Masukkan kalimat: ");
                    String target = bacaTeks(input, "Kata yang dicari: ");
                    System.out.println("Kata \"" + target + "\" muncul "
                            + penghitung.hitungFrekuensi(k4, target) + " kali.");
                    break;
                case 5:
                    String k5 = bacaTeks(input, "Masukkan kalimat: ");
                    System.out.printf("Rata-rata panjang kata : %.2f%n",
                            penghitung.rataRataPanjang(k5));
                    break;
                case 6:
                    String k6 = bacaTeks(input, "Masukkan kalimat: ");
                    penghitung.tampilkanDaftarKata(k6);
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