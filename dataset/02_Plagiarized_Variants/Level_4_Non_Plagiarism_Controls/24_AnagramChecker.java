/*
 * Program   : AnagramChecker
 * Deskripsi : Memeriksa apakah dua kata/kalimat merupakan anagram
 *             satu sama lain. Spasi, tanda baca, dan perbedaan
 *             huruf besar/kecil diabaikan.
 */
import java.util.Scanner;

public class AnagramChecker {

    // Membersihkan teks: huruf kecil, hanya huruf dan angka
    public static String bersihkan(String teks) {
        StringBuilder hasil = new StringBuilder();
        String kecil = teks.toLowerCase();
        for (int i = 0; i < kecil.length(); i++) {
            char c = kecil.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                hasil.append(c);
            }
        }
        return hasil.toString();
    }

    // Metode 1: bandingkan frekuensi tiap karakter (array counter)
    public static boolean isAnagramFrekuensi(String a, String b) {
        String x = bersihkan(a);
        String y = bersihkan(b);

        // panjang beda otomatis bukan anagram
        if (x.length() != y.length()) {
            return false;
        }

        int[] hitung = new int[128];
        for (int i = 0; i < x.length(); i++) {
            hitung[x.charAt(i)]++;   // tambah untuk string pertama
            hitung[y.charAt(i)]--;   // kurangi untuk string kedua
        }

        // jika semua nol, berarti huruf-hurufnya sama persis
        for (int i = 0; i < hitung.length; i++) {
            if (hitung[i] != 0) {
                return false;
            }
        }
        return true;
    }

    // Metode 2: urutkan karakter lalu bandingkan (bubble sort sederhana)
    public static boolean isAnagramSorting(String a, String b) {
        char[] x = bersihkan(a).toCharArray();
        char[] y = bersihkan(b).toCharArray();

        if (x.length != y.length) {
            return false;
        }

        urutkan(x);
        urutkan(y);

        for (int i = 0; i < x.length; i++) {
            if (x[i] != y[i]) {
                return false;
            }
        }
        return true;
    }

    // Mengurutkan array char dengan bubble sort
    public static void urutkan(char[] data) {
        for (int i = 0; i < data.length - 1; i++) {
            for (int j = 0; j < data.length - 1 - i; j++) {
                if (data[j] > data[j + 1]) {
                    char temp = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = temp;
                }
            }
        }
    }

    // Menampilkan hasil pemeriksaan
    public static void tampilkanHasil(String a, String b) {
        boolean cek1 = isAnagramFrekuensi(a, b);
        boolean cek2 = isAnagramSorting(a, b);

        System.out.println("\nTeks 1 : \"" + a + "\"");
        System.out.println("Teks 2 : \"" + b + "\"");
        System.out.println("Metode frekuensi : " + (cek1 ? "ANAGRAM" : "BUKAN anagram"));
        System.out.println("Metode sorting   : " + (cek2 ? "ANAGRAM" : "BUKAN anagram"));
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Contoh pemakaian dengan data tetap
        System.out.println("=== CONTOH PENGUJIAN ===");
        String[][] contoh = {
            {"listen", "silent"},
            {"Dormitory", "Dirty room"},
            {"Astronomer", "Moon starer"},
            {"hello", "world"},
            {"kasur", "rusak"}
        };
        for (int i = 0; i < contoh.length; i++) {
            tampilkanHasil(contoh[i][0], contoh[i][1]);
        }

        // Input dari pengguna
        String lagi;
        do {
            System.out.println("\n=== CEK ANAGRAM ===");
            System.out.print("Masukkan teks pertama : ");
            String teks1 = input.nextLine();
            System.out.print("Masukkan teks kedua   : ");
            String teks2 = input.nextLine();

            if (bersihkan(teks1).isEmpty() || bersihkan(teks2).isEmpty()) {
                System.out.println("Teks tidak boleh kosong!");
            } else {
                tampilkanHasil(teks1, teks2);
            }

            System.out.print("\nCek lagi? (y/n): ");
            lagi = input.nextLine();
        } while (lagi.equalsIgnoreCase("y"));

        System.out.println("Program selesai.");
        input.close();
    }
}