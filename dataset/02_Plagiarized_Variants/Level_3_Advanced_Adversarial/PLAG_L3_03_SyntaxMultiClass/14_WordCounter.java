/*
 * Program   : WordCounter
 * Deskripsi : Menghitung jumlah kata dalam sebuah kalimat. Dilengkapi
 *             penghitungan jumlah huruf, kata terpanjang, frekuensi
 *             sebuah kata, rata-rata panjang kata, input dari pengguna,
 *             dan menu sederhana.
 */
import java.util.Scanner;

public class WordCounter {

    // Inner static class wrapper untuk kalimat dan koleksi token kata
    public static class SentenceAnalysis {
        private String original;
        private String[] wordTokens;

        public SentenceAnalysis(String sentence) {
            this.original = sentence != null ? sentence : "";
            if (sentence != null && !sentence.trim().isEmpty()) {
                this.wordTokens = sentence.trim().split("\\s+");
            } else {
                this.wordTokens = new String[0];
            }
        }

        public String getOriginal() {
            return original;
        }

        public int wordCount() {
            return wordTokens.length;
        }

        public String getWord(int index) {
            return wordTokens[index];
        }

        public String[] getWordTokens() {
            return wordTokens;
        }
    }

    // Inner static class wrapper untuk kalkulasi metrik karakter
    public static class CharacterMetrics {
        private int letterOrDigitCount;

        public CharacterMetrics(String text) {
            this.letterOrDigitCount = 0;
            if (text != null) {
                for (int i = 0; i < text.length(); i++) {
                    if (Character.isLetterOrDigit(text.charAt(i))) {
                        letterOrDigitCount++;
                    }
                }
            }
        }

        public int getCount() {
            return letterOrDigitCount;
        }
    }

    // Logika inti: menghitung jumlah kata dengan memecah berdasarkan spasi
    public int countWords(String sentence) {
        SentenceAnalysis analysis = new SentenceAnalysis(sentence);
        return analysis.wordCount();
    }

    // Menghitung jumlah huruf dan angka (spasi dan tanda baca tidak dihitung)
    public int hitungHuruf(String kalimat) {
        CharacterMetrics metrics = new CharacterMetrics(kalimat);
        return metrics.getCount();
    }

    // Mencari kata terpanjang dalam kalimat
    public String kataTerpanjang(String kalimat) {
        SentenceAnalysis analysis = new SentenceAnalysis(kalimat);
        if (analysis.wordCount() == 0) {
            return "";
        }
        String terpanjang = analysis.getWord(0);
        for (int i = 1; i < analysis.wordCount(); i++) {
            if (analysis.getWord(i).length() > terpanjang.length()) {
                terpanjang = analysis.getWord(i);
            }
        }
        return terpanjang;
    }

    // Menghitung berapa kali sebuah kata muncul (huruf besar kecil diabaikan)
    public int hitungFrekuensi(String kalimat, String target) {
        SentenceAnalysis analysis = new SentenceAnalysis(kalimat);
        if (analysis.wordCount() == 0) {
            return 0;
        }
        int jumlah = 0;
        for (int i = 0; i < analysis.wordCount(); i++) {
            if (analysis.getWord(i).equalsIgnoreCase(target)) {
                jumlah++;
            }
        }
        return jumlah;
    }

    // Menghitung rata-rata panjang kata
    public double rataRataPanjang(String kalimat) {
        SentenceAnalysis analysis = new SentenceAnalysis(kalimat);
        int jumlahKata = analysis.wordCount();
        if (jumlahKata == 0) {
            return 0;
        }
        int totalPanjang = 0;
        for (int i = 0; i < jumlahKata; i++) {
            totalPanjang += analysis.getWord(i).length();
        }
        return (double) totalPanjang / jumlahKata;
    }

    // Menampilkan setiap kata beserta nomor urutnya
    public void tampilkanDaftarKata(String kalimat) {
        SentenceAnalysis analysis = new SentenceAnalysis(kalimat);
        if (analysis.wordCount() == 0) {
            System.out.println("Tidak ada kata.");
            return;
        }
        for (int i = 0; i < analysis.wordCount(); i++) {
            String word = analysis.getWord(i);
            System.out.println((i + 1) + ". " + word + " (" + word.length() + " karakter)");
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

        int pilihan = 0;
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