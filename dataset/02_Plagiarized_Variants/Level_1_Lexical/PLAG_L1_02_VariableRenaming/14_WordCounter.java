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
    public int hitungKata(String kalimat) {
        if (kalimat == null || kalimat.trim().isEmpty()) {
            return 0;
        }
        String[] token = kalimat.trim().split("\\s+");
        return token.length;
    }

    // Menghitung jumlah huruf dan angka (spasi dan tanda baca tidak dihitung)
    public int hitungKarakter(String kalimat) {
        int cacah = 0;
        for (int pos = 0; pos < kalimat.length(); pos++) {
            if (Character.isLetterOrDigit(kalimat.charAt(pos))) {
                cacah++;
            }
        }
        return cacah;
    }

    // Mencari kata terpanjang dalam kalimat
    public String kataPalingPanjang(String kalimat) {
        if (hitungKata(kalimat) == 0) {
            return "";
        }
        String[] token = kalimat.trim().split("\\s+");
        String paling = token[0];
        for (int pos = 1; pos < token.length; pos++) {
            if (token[pos].length() > paling.length()) {
                paling = token[pos];
            }
        }
        return paling;
    }

    // Menghitung berapa kali sebuah kata muncul (huruf besar kecil diabaikan)
    public int hitungFrekuensi(String kalimat, String cari) {
        if (hitungKata(kalimat) == 0) {
            return 0;
        }
        String[] token = kalimat.trim().split("\\s+");
        int cacah = 0;
        for (int pos = 0; pos < token.length; pos++) {
            if (token[pos].equalsIgnoreCase(cari)) {
                cacah++;
            }
        }
        return cacah;
    }

    // Menghitung rata-rata panjang kata
    public double rataRataPanjang(String kalimat) {
        int banyakKata = hitungKata(kalimat);
        if (banyakKata == 0) {
            return 0;
        }
        String[] token = kalimat.trim().split("\\s+");
        int total = 0;
        for (int pos = 0; pos < token.length; pos++) {
            total += token[pos].length();
        }
        return (double) total / banyakKata;
    }

    // Menampilkan setiap kata beserta nomor urutnya
    public void tampilkanDaftar(String kalimat) {
        if (hitungKata(kalimat) == 0) {
            System.out.println("Tidak ada kata.");
            return;
        }
        String[] token = kalimat.trim().split("\\s+");
        for (int pos = 0; pos < token.length; pos++) {
            System.out.println((pos + 1) + ". " + token[pos] + " (" + token[pos].length() + " karakter)");
        }
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
        WordCounter alat = new WordCounter();

        int menu;
        do {
            System.out.println("\n===== PROGRAM PENGHITUNG KATA =====");
            System.out.println("1. Hitung jumlah kata");
            System.out.println("2. Hitung jumlah huruf");
            System.out.println("3. Cari kata terpanjang");
            System.out.println("4. Hitung frekuensi sebuah kata");
            System.out.println("5. Rata-rata panjang kata");
            System.out.println("6. Tampilkan daftar kata");
            System.out.println("0. Keluar");
            menu = ambilAngka(scanner, "Pilih menu: ");

            switch (menu) {
                case 1:
                    String m1 = ambilTeks(scanner, "Masukkan kalimat: ");
                    System.out.println("Jumlah kata : " + alat.hitungKata(m1));
                    break;
                case 2:
                    String m2 = ambilTeks(scanner, "Masukkan kalimat: ");
                    System.out.println("Jumlah huruf dan angka : " + alat.hitungKarakter(m2));
                    break;
                case 3:
                    String m3 = ambilTeks(scanner, "Masukkan kalimat: ");
                    String paling = alat.kataPalingPanjang(m3);
                    if (paling.isEmpty()) {
                        System.out.println("Kalimat kosong, tidak ada kata.");
                    } else {
                        System.out.println("Kata terpanjang : " + paling
                                + " (" + paling.length() + " karakter)");
                    }
                    break;
                case 4:
                    String m4 = ambilTeks(scanner, "Masukkan kalimat: ");
                    String cari = ambilTeks(scanner, "Kata yang dicari: ");
                    System.out.println("Kata \"" + cari + "\" muncul "
                            + alat.hitungFrekuensi(m4, cari) + " kali.");
                    break;
                case 5:
                    String m5 = ambilTeks(scanner, "Masukkan kalimat: ");
                    System.out.printf("Rata-rata panjang kata : %.2f%n",
                            alat.rataRataPanjang(m5));
                    break;
                case 6:
                    String m6 = ambilTeks(scanner, "Masukkan kalimat: ");
                    alat.tampilkanDaftar(m6);
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