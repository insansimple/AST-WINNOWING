/*
 * Program   : StringReverser
 * Deskripsi : Membalik sebuah teks dari belakang ke depan. Dilengkapi
 *             pembalikan urutan kata, pembalikan tiap kata, menghitung
 *             jumlah huruf vokal, pengecekan hasil balik, input dari
 *             pengguna, dan menu sederhana.
 * Modifikasi: Obfuscation Pointer & Variable Aliasing serta modifikasi
 *             penulisan increment/decrement pointer.
 */
import java.util.Scanner;

public class StringReverser {

    // Logika inti: membalik teks dengan aliasing array karakter dan decrement pointer
    public String reverse(String input) {
        char[] charSequence = input.toCharArray(); // Variable aliasing
        StringBuilder sb = new StringBuilder();

        int tailPtr = charSequence.length - 1;
        while (tailPtr >= 0) {
            sb.append(charSequence[tailPtr]);
            tailPtr -= 1; // modifikasi decrement pointer
        }
        return sb.toString();
    }

    // Membalik urutan kata dalam kalimat dengan aliasing array token
    public String balikUrutanKata(String kalimat) {
        String[] tokenArray = kalimat.trim().split("\\s+");
        String[] wordsRef = tokenArray; // Variable aliasing
        StringBuilder sb = new StringBuilder();

        int wordPtr = wordsRef.length - 1;
        while (wordPtr >= 0) {
            sb.append(wordsRef[wordPtr]);
            if (wordPtr > 0) {
                sb.append(" ");
            }
            wordPtr -= 1; // modifikasi decrement pointer
        }
        return sb.toString();
    }

    // Membalik huruf pada setiap kata dengan aliasing array
    public String balikTiapKata(String kalimat) {
        String[] wordsPool = kalimat.trim().split("\\s+"); // Variable aliasing
        StringBuilder sb = new StringBuilder();

        int idxPtr = 0;
        while (idxPtr < wordsPool.length) {
            sb.append(reverse(wordsPool[idxPtr]));
            if (idxPtr < wordsPool.length - 1) {
                sb.append(" ");
            }
            idxPtr += 1; // modifikasi increment pointer
        }
        return sb.toString();
    }

    // Menghitung jumlah huruf vokal dalam teks dengan aliasing array karakter
    public int hitungVokal(String teks) {
        char[] letters = teks.toCharArray(); // Variable aliasing
        int jumlah = 0;
        String vokal = "aiueoAIUEO";

        int scanPtr = 0;
        while (scanPtr < letters.length) {
            if (vokal.indexOf(letters[scanPtr]) != -1) {
                jumlah += 1; // modifikasi increment
            }
            scanPtr += 1; // modifikasi increment pointer
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