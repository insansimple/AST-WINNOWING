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
        for (int i = input.length() - 1; i >= 0; i--) {
            sb.append(input.charAt(i));
        }
        return sb.toString();
    }

    // Membalik urutan kata dalam kalimat (kata tetap utuh)
    public String balikUrutanKata(String kalimat) {
        String[] kata = kalimat.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = kata.length - 1; i >= 0; i--) {
            sb.append(kata[i]);
            if (0 < i) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    // Membalik huruf pada setiap kata, urutan kata tetap
    public String balikTiapKata(String kalimat) {
        String[] kata = kalimat.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < kata.length; i++) {
            sb.append(reverse(kata[i]));
            if (kata.length - 1 > i) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    // Menghitung jumlah huruf vokal dalam teks
    public int hitungVokal(String teks) {
        int jumlah = 0;
        String vokal = "aiueoAIUEO";
        for (int i = 0; i < teks.length(); i++) {
            if (-1 != vokal.indexOf(teks.charAt(i))) {
                jumlah++;
            }
        }
        return jumlah;
    }

    // Mengecek apakah teks sama dengan hasil baliknya (tanpa membedakan huruf besar kecil)
    public boolean samaDenganBalikan(String teks) {
        return teks.equalsIgnoreCase(reverse(teks));
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

    // Membaca satu baris teks yang tidak boleh kosong
    public static String bacaTeks(Scanner input, String pesan) {
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

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        StringReverser pembalik = new StringReverser();

        int pilihan;
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
                    if (!pembalik.samaDenganBalikan(teks5)) {
                        System.out.println("Teks berbeda dengan hasil baliknya: "
                                + pembalik.reverse(teks5));
                    } else {
                        System.out.println("Teks sama dengan hasil baliknya.");
                    }
                    break;
                case 0:
                    System.out.println("Terima kasih, program selesai.");
                    break;
                default:
                    System.out.println("Menu tidak tersedia!");
            }
        } while (0 != pilihan);

        input.close();
    }
}