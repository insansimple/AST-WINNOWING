/*
 * Program   : StringReverser
 * Deskripsi : Membalik sebuah teks dari belakang ke depan. Dilengkapi
 *             pembalikan urutan kata, pembalikan tiap kata, menghitung
 *             jumlah huruf vokal, pengecekan hasil balik, input dari
 *             pengguna, dan menu sederhana.
 */
import java.util.Scanner;

public class StringReverser {

    // Logika inti: membalik teks menggunakan StringBuilder
    public String reverse(String input) {
        StringBuilder sb = new StringBuilder();
        for (Integer i = input.length() - 1; i >= 0; i--) {
            sb.append(input.charAt(i));
        }
        return sb.toString();
    }

    // Membalik urutan kata dalam kalimat (kata tetap utuh)
    public String balikUrutanKata(String kalimat) {
        String[] kata = kalimat.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (Integer i = kata.length - 1; i >= 0; i--) {
            sb.append(kata[i]);
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    // Membalik huruf pada setiap kata, urutan kata tetap
    public String balikTiapKata(String kalimat) {
        String[] kata = kalimat.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (Integer i = 0; i < kata.length; i++) {
            sb.append(reverse(kata[i]));
            if (i < kata.length - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    // Menghitung jumlah huruf vokal dalam teks
    public Integer hitungVokal(String teks) {
        Integer jumlah = 0;
        String vokal = "aiueoAIUEO";
        for (Integer i = 0; i < teks.length(); i++) {
            if (vokal.indexOf(teks.charAt(i)) != -1) {
                jumlah++;
            }
        }
        return jumlah;
    }

    // Mengecek apakah teks sama dengan hasil baliknya (tanpa membedakan huruf besar kecil)
    public Boolean samaDenganBalikan(String teks) {
        return teks.equalsIgnoreCase(reverse(teks));
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

    // Membaca satu baris teks yang tidak boleh kosong
    static public String bacaTeks(Scanner input, String pesan) {
        String teks = "";
        while (teks.trim().isEmpty()) {
            System.out.print(pesan);
            teks = input.nextLine();
            if (teks.trim().isEmpty()) {
                System.out.println("Teks tidak boleh kosong!");
            }
        }
        return teks;
    }

    static public void main(String[] args) {
        Scanner input = new Scanner(System.in);
        StringReverser pembalik = new StringReverser();

        Integer pilihan;
        do {
            System.out.println("\n===== PROGRAM PEMBALIK TEKS =====");
            System.out.println("1. Balik seluruh teks");
            System.out.println("2. Balik urutan kata");
            System.out.println("3. Balik huruf tiap kata");
            System.out.println("4. Hitung jumlah huruf vokal");
            System.out.println("5. Cek teks sama dengan balikannya");
            System.out.println("0. Keluar");
            pilihan = bacaAngka(input, "Pilih menu: ");

            switch (pilihan) {
                case 1:
                    String teks1 = bacaTeks(input, "Masukkan teks: ");
                    System.out.println("Hasil : " + pembalik.reverse(teks1));
                    break;
                case 2:
                    String teks2 = bacaTeks(input, "Masukkan kalimat: ");
                    System.out.println("Hasil : " + pembalik.balikUrutanKata(teks2));
                    break;
                case 3:
                    String teks3 = bacaTeks(input, "Masukkan kalimat: ");
                    System.out.println("Hasil : " + pembalik.balikTiapKata(teks3));
                    break;
                case 4:
                    String teks4 = bacaTeks(input, "Masukkan teks: ");
                    System.out.println("Jumlah huruf vokal : " + pembalik.hitungVokal(teks4));
                    break;
                case 5:
                    String teks5 = bacaTeks(input, "Masukkan teks: ");
                    if (pembalik.samaDenganBalikan(teks5)) {
                        System.out.println("Teks sama dengan hasil baliknya.");
                    } else {
                        System.out.println("Teks berbeda dengan hasil baliknya: "
                                + pembalik.reverse(teks5));
                    }
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