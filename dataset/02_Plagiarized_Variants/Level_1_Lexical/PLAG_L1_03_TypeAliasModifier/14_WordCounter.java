/*
 * Program   : WordCounter
 * Deskripsi : Menghitung jumlah kata dalam sebuah kalimat. Dilengkapi
 *             penghitungan jumlah huruf, kata terpanjang, frekuensi
 *             sebuah kata, rata-rata panjang kata, input dari pengguna,
 *             dan menu sederhana.
 */
import java.util.Scanner;

public class WordCounter {

    // Logika inti: menghitung jumlah kata dengan memecah berdasarkan spasi
    public Integer countWords(String sentence) {
        if (sentence == null || sentence.trim().isEmpty()) {
            return 0;
        }
        String[] words = sentence.trim().split("\\s+");
        return words.length;
    }

    // Menghitung jumlah huruf dan angka (spasi dan tanda baca tidak dihitung)
    public Integer hitungHuruf(String kalimat) {
        Integer jumlah = 0;
        for (Integer i = 0; i < kalimat.length(); i++) {
            if (Character.isLetterOrDigit(kalimat.charAt(i))) {
                jumlah++;
            }
        }
        return jumlah;
    }

    // Mencari kata terpanjang dalam kalimat
    public String kataTerpanjang(String kalimat) {
        if (countWords(kalimat) == 0) {
            return "";
        }
        String[] kata = kalimat.trim().split("\\s+");
        String terpanjang = kata[0];
        for (Integer i = 1; i < kata.length; i++) {
            if (kata[i].length() > terpanjang.length()) {
                terpanjang = kata[i];
            }
        }
        return terpanjang;
    }

    // Menghitung berapa kali sebuah kata muncul (huruf besar kecil diabaikan)
    public Integer hitungFrekuensi(String kalimat, String target) {
        if (countWords(kalimat) == 0) {
            return 0;
        }
        String[] kata = kalimat.trim().split("\\s+");
        Integer jumlah = 0;
        for (Integer i = 0; i < kata.length; i++) {
            if (kata[i].equalsIgnoreCase(target)) {
                jumlah++;
            }
        }
        return jumlah;
    }

    // Menghitung rata-rata panjang kata
    public Double rataRataPanjang(String kalimat) {
        Integer jumlahKata = countWords(kalimat);
        if (jumlahKata == 0) {
            return 0.0;
        }
        String[] kata = kalimat.trim().split("\\s+");
        Integer totalPanjang = 0;
        for (Integer i = 0; i < kata.length; i++) {
            totalPanjang += kata[i].length();
        }
        return (double) totalPanjang / jumlahKata;
    }

    // Menampilkan setiap kata beserta nomor urutnya
    public void tampilkanDaftarKata(String kalimat) {
        if (countWords(kalimat) == 0) {
            System.out.println("Tidak ada kata.");
            return;
        }
        String[] kata = kalimat.trim().split("\\s+");
        for (Integer i = 0; i < kata.length; i++) {
            System.out.println((i + 1) + ". " + kata[i] + " (" + kata[i].length() + " karakter)");
        }
    }

    // Membaca bilangan bulat dengan validasi sederhana
    static public Integer bacaAngka(Scanner input, String pesan) {
        System.out.print(pesan);
        while (!input.hasNextInt()) {
            System.out.print("Input harus angka bulat, ulangi: ");
            input.next();
        }
        Integer nilai = input.nextInt();
        input.nextLine(); // buang sisa baris
        return nilai;
    }

    // Membaca satu baris teks dari pengguna
    static public String bacaTeks(Scanner input, String pesan) {
        System.out.print(pesan);
        return input.nextLine();
    }

    static public void main(String[] args) {
        Scanner input = new Scanner(System.in);
        WordCounter penghitung = new WordCounter();

        Integer pilihan;
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