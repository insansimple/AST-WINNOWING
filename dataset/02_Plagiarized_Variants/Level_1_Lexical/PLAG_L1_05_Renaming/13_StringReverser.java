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
    public String putarTeks(String masukan) {
        StringBuilder penampung = new StringBuilder();
        for (int k = masukan.length() - 1; k >= 0; k--) {
            penampung.append(masukan.charAt(k));
        }
        return penampung.toString();
    }

    // Membalik urutan kata dalam kalimat (kata tetap utuh)
    public String putarUrutanKata(String ucapan) {
        String[] daftarKata = ucapan.trim().split("\\s+");
        StringBuilder penampung = new StringBuilder();
        for (int k = daftarKata.length - 1; k >= 0; k--) {
            penampung.append(daftarKata[k]);
            if (k > 0) {
                penampung.append(" ");
            }
        }
        return penampung.toString();
    }

    // Membalik huruf pada setiap kata, urutan kata tetap
    public String putarSetiapKata(String ucapan) {
        String[] daftarKata = ucapan.trim().split("\\s+");
        StringBuilder penampung = new StringBuilder();
        for (int k = 0; k < daftarKata.length; k++) {
            penampung.append(putarTeks(daftarKata[k]));
            if (k < daftarKata.length - 1) {
                penampung.append(" ");
            }
        }
        return penampung.toString();
    }

    // Menghitung jumlah huruf vokal dalam teks
    public int cacahVokal(String bahan) {
        int total = 0;
        String daftarVokal = "aiueoAIUEO";
        for (int k = 0; k < bahan.length(); k++) {
            if (daftarVokal.indexOf(bahan.charAt(k)) != -1) {
                total++;
            }
        }
        return total;
    }

    // Mengecek apakah teks sama dengan hasil baliknya (tanpa membedakan huruf besar kecil)
    public boolean cocokDenganKebalikan(String bahan) {
        return bahan.equalsIgnoreCase(putarTeks(bahan));
    }

    // Membaca bilangan bulat dengan validasi sederhana
    public static int ambilInteger(Scanner pemindai, String keterangan) {
        System.out.print(keterangan);
        while (!pemindai.hasNextInt()) {
            System.out.print("Input harus angka bulat, ulangi: ");
            pemindai.next();
        }
        int hasilBaca = pemindai.nextInt();
        pemindai.nextLine(); // buang sisa baris
        return hasilBaca;
    }

    // Membaca satu baris teks yang tidak boleh kosong
    public static String ambilBarisTeks(Scanner pemindai, String keterangan) {
        String baris = "";
        while (baris.trim().isEmpty()) {
            System.out.print(keterangan);
            baris = pemindai.nextLine();
            if (baris.trim().isEmpty()) {
                System.out.print("Teks tidak boleh kosong!");
                System.out.print(System.lineSeparator());
            }
        }
        return baris;
    }

    public static void main(String[] args) {
        Scanner pemindai = new Scanner(System.in);
        StringReverser pemutar = new StringReverser();

        int opsi;
        do {
            System.out.print("\n===== PROGRAM PEMBALIK TEKS =====");
            System.out.print(System.lineSeparator());
            System.out.print("1. Balik seluruh teks");
            System.out.print(System.lineSeparator());
            System.out.print("2. Balik urutan kata");
            System.out.print(System.lineSeparator());
            System.out.print("3. Balik huruf tiap kata");
            System.out.print(System.lineSeparator());
            System.out.print("4. Hitung jumlah huruf vokal");
            System.out.print(System.lineSeparator());
            System.out.print("5. Cek teks sama dengan balikannya");
            System.out.print(System.lineSeparator());
            System.out.print("0. Keluar");
            System.out.print(System.lineSeparator());
            opsi = ambilInteger(pemindai, "Pilih menu: ");

            switch (opsi) {
                case 1:
                    String bahan1 = ambilBarisTeks(pemindai, "Masukkan teks: ");
                    System.out.print(new StringBuilder().append("Hasil : ").append(pemutar.putarTeks(bahan1)).toString());
                    System.out.print(System.lineSeparator());
                    break;
                case 2:
                    String bahan2 = ambilBarisTeks(pemindai, "Masukkan kalimat: ");
                    System.out.print(new StringBuilder().append("Hasil : ").append(pemutar.putarUrutanKata(bahan2)).toString());
                    System.out.print(System.lineSeparator());
                    break;
                case 3:
                    String bahan3 = ambilBarisTeks(pemindai, "Masukkan kalimat: ");
                    System.out.print(new StringBuilder().append("Hasil : ").append(pemutar.putarSetiapKata(bahan3)).toString());
                    System.out.print(System.lineSeparator());
                    break;
                case 4:
                    String bahan4 = ambilBarisTeks(pemindai, "Masukkan teks: ");
                    System.out.print(new StringBuilder().append("Jumlah huruf vokal : ").append(pemutar.cacahVokal(bahan4)).toString());
                    System.out.print(System.lineSeparator());
                    break;
                case 5:
                    String bahan5 = ambilBarisTeks(pemindai, "Masukkan teks: ");
                    if (pemutar.cocokDenganKebalikan(bahan5)) {
                        System.out.print("Teks sama dengan hasil baliknya.");
                        System.out.print(System.lineSeparator());
                    } else {
                        System.out.print(new StringBuilder().append("Teks berbeda dengan hasil baliknya: ")
                                .append(pemutar.putarTeks(bahan5)).toString());
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 0:
                    System.out.print("Terima kasih, program selesai.");
                    System.out.print(System.lineSeparator());
                    break;
                default:
                    System.out.print("Menu tidak tersedia!");
                    System.out.print(System.lineSeparator());
            }
        } while (opsi != 0);

        pemindai.close();
    }
}