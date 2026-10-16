/*
 * Program   : CaesarCipher
 * Deskripsi : Enkripsi dan dekripsi teks menggunakan Caesar Cipher,
 *             yaitu pergeseran setiap huruf sebanyak k posisi pada
 *             alfabet. Huruf besar/kecil dipertahankan, karakter
 *             selain huruf (spasi, angka, tanda baca) tidak diubah.
 *             Dilengkapi fitur brute force untuk mencoba semua kunci.
 */
import java.util.Scanner;

public class CaesarCipher {

    // Menggeser satu karakter sebanyak kunci (kunci boleh negatif)
    public static char geserKarakter(char c, int kunci) {
        if (Character.isUpperCase(c)) {
            // ((c - 'A' + kunci) mod 26), dijaga supaya tidak negatif
            int posisi = ((c - 'A' + kunci) % 26 + 26) % 26;
            return (char) ('A' + posisi);
        } else if (Character.isLowerCase(c)) {
            int posisi = ((c - 'a' + kunci) % 26 + 26) % 26;
            return (char) ('a' + posisi);
        }
        // bukan huruf alfabet, kembalikan apa adanya
        return c;
    }

    // Enkripsi: geser huruf ke kanan sebanyak kunci
    public static String enkripsi(String teks, int kunci) {
        StringBuilder hasil = new StringBuilder();
        for (int i = 0; i < teks.length(); i++) {
            hasil.append(geserKarakter(teks.charAt(i), kunci));
        }
        return hasil.toString();
    }

    // Dekripsi: geser huruf ke kiri sebanyak kunci
    public static String dekripsi(String teks, int kunci) {
        return enkripsi(teks, -kunci);
    }

    // Mencoba semua kemungkinan kunci (1 sampai 25)
    public static void bruteForce(String teksSandi) {
        System.out.println("\nHasil brute force untuk: " + teksSandi);
        System.out.println("-----------------------------------");
        for (int k = 1; k <= 25; k++) {
            System.out.printf("Kunci %2d : %s%n", k, dekripsi(teksSandi, k));
        }
    }

    // Membaca kunci dari pengguna dengan validasi
    public static int bacaKunci(Scanner input) {
        int kunci = 0;
        boolean valid = false;
        while (!valid) {
            System.out.print("Masukkan kunci (bilangan bulat): ");
            String masukan = input.nextLine().trim();
            try {
                kunci = Integer.parseInt(masukan);
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Kunci harus berupa bilangan bulat!");
            }
        }
        return kunci;
    }

    // Menampilkan contoh pengujian dengan data tetap
    public static void tampilkanContoh() {
        String asli = "Hello World, Caesar Cipher 2024!";
        int kunci = 3;
        String sandi = enkripsi(asli, kunci);
        String kembali = dekripsi(sandi, kunci);

        System.out.println("=== CONTOH PENGUJIAN ===");
        System.out.println("Teks asli      : " + asli);
        System.out.println("Kunci          : " + kunci);
        System.out.println("Hasil enkripsi : " + sandi);
        System.out.println("Hasil dekripsi : " + kembali);
        System.out.println("Sama dengan asli? " + (asli.equals(kembali) ? "Ya" : "Tidak"));
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int pilihan = -1;

        tampilkanContoh();

        do {
            // Menu utama
            System.out.println("\n=== MENU CAESAR CIPHER ===");
            System.out.println("1. Enkripsi teks");
            System.out.println("2. Dekripsi teks");
            System.out.println("3. Brute force (tanpa kunci)");
            System.out.println("0. Keluar");
            System.out.print("Pilihan: ");

            try {
                pilihan = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                pilihan = -1;
            }

            switch (pilihan) {
                case 1:
                    System.out.print("Masukkan teks asli : ");
                    String teksAsli = input.nextLine();
                    int kunciEnkripsi = bacaKunci(input);
                    System.out.println("Hasil enkripsi     : " + enkripsi(teksAsli, kunciEnkripsi));
                    break;
                case 2:
                    System.out.print("Masukkan teks sandi : ");
                    String teksSandi = input.nextLine();
                    int kunciDekripsi = bacaKunci(input);
                    System.out.println("Hasil dekripsi      : " + dekripsi(teksSandi, kunciDekripsi));
                    break;
                case 3:
                    System.out.print("Masukkan teks sandi : ");
                    bruteForce(input.nextLine());
                    break;
                case 0:
                    System.out.println("Program selesai.");
                    break;
                default:
                    System.out.println("Pilihan tidak tersedia!");
            }
        } while (pilihan != 0);

        input.close();
    }
}