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
    public String balik(String str) {
        StringBuilder pembangun = new StringBuilder();
        for (int pos = str.length() - 1; pos >= 0; pos--) {
            pembangun.append(str.charAt(pos));
        }
        return pembangun.toString();
    }

    // Membalik urutan kata dalam kalimat (kata tetap utuh)
    public String balikUrutan(String kalimat) {
        String[] token = kalimat.trim().split("\\s+");
        StringBuilder pembangun = new StringBuilder();
        for (int pos = token.length - 1; pos >= 0; pos--) {
            pembangun.append(token[pos]);
            if (pos > 0) {
                pembangun.append(" ");
            }
        }
        return pembangun.toString();
    }

    // Membalik huruf pada setiap kata, urutan kata tetap
    public String balikPerKata(String kalimat) {
        String[] token = kalimat.trim().split("\\s+");
        StringBuilder pembangun = new StringBuilder();
        for (int pos = 0; pos < token.length; pos++) {
            pembangun.append(balik(token[pos]));
            if (pos < token.length - 1) {
                pembangun.append(" ");
            }
        }
        return pembangun.toString();
    }

    // Menghitung jumlah huruf vokal dalam teks
    public int cacahVokal(String str) {
        int cacah = 0;
        String daftarVokal = "aiueoAIUEO";
        for (int pos = 0; pos < str.length(); pos++) {
            if (daftarVokal.indexOf(str.charAt(pos)) != -1) {
                cacah++;
            }
        }
        return cacah;
    }

    // Mengecek apakah teks sama dengan hasil baliknya (tanpa membedakan huruf besar kecil)
    public boolean samaBalikan(String str) {
        return str.equalsIgnoreCase(balik(str));
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

    // Membaca satu baris teks yang tidak boleh kosong
    public static String ambilTeks(Scanner scanner, String teks) {
        String masukan = "";
        while (masukan.trim().isEmpty()) {
            System.out.print(teks);
            masukan = scanner.nextLine();
            if (masukan.trim().isEmpty()) {
                System.out.println("Teks tidak boleh kosong!");
            }
        }
        return masukan;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringReverser alat = new StringReverser();

        int menu;
        do {
            System.out.println("\n===== PROGRAM PEMBALIK TEKS =====");
            System.out.println("1. Balik seluruh teks");
            System.out.println("2. Balik urutan kata");
            System.out.println("3. Balik huruf tiap kata");
            System.out.println("4. Hitung jumlah huruf vokal");
            System.out.println("5. Cek teks sama dengan balikannya");
            System.out.println("0. Keluar");
            menu = ambilAngka(scanner, "Pilih menu: ");

            switch (menu) {
                case 1:
                    String masukan1 = ambilTeks(scanner, "Masukkan teks: ");
                    System.out.println("Hasil : " + alat.balik(masukan1));
                    break;
                case 2:
                    String masukan2 = ambilTeks(scanner, "Masukkan kalimat: ");
                    System.out.println("Hasil : " + alat.balikUrutan(masukan2));
                    break;
                case 3:
                    String masukan3 = ambilTeks(scanner, "Masukkan kalimat: ");
                    System.out.println("Hasil : " + alat.balikPerKata(masukan3));
                    break;
                case 4:
                    String masukan4 = ambilTeks(scanner, "Masukkan teks: ");
                    System.out.println("Jumlah huruf vokal : " + alat.cacahVokal(masukan4));
                    break;
                case 5:
                    String masukan5 = ambilTeks(scanner, "Masukkan teks: ");
                    if (alat.samaBalikan(masukan5)) {
                        System.out.println("Teks sama dengan hasil baliknya.");
                    } else {
                        System.out.println("Teks berbeda dengan hasil baliknya: "
                                + alat.balik(masukan5));
                    }
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