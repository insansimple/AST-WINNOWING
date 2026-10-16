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
    public int cacahKata(String ucapan) {
        if (ucapan == null || ucapan.trim().isEmpty()) {
            return 0;
        }
        String[] larikKata = ucapan.trim().split("\\s+");
        return larikKata.length;
    }

    // Menghitung jumlah huruf dan angka (spasi dan tanda baca tidak dihitung)
    public int cacahKarakter(String bahan) {
        int total = 0;
        for (int k = 0; k < bahan.length(); k++) {
            if (Character.isLetterOrDigit(bahan.charAt(k))) {
                total++;
            }
        }
        return total;
    }

    // Mencari kata terpanjang dalam kalimat
    public String temukanTerpanjang(String bahan) {
        if (cacahKata(bahan) == 0) {
            return "";
        }
        String[] larikKata = bahan.trim().split("\\s+");
        String juara = larikKata[0];
        for (int k = 1; k < larikKata.length; k++) {
            if (larikKata[k].length() > juara.length()) {
                juara = larikKata[k];
            }
        }
        return juara;
    }

    // Menghitung berapa kali sebuah kata muncul (huruf besar kecil diabaikan)
    public int cacahKemunculan(String bahan, String sasaran) {
        if (cacahKata(bahan) == 0) {
            return 0;
        }
        String[] larikKata = bahan.trim().split("\\s+");
        int total = 0;
        for (int k = 0; k < larikKata.length; k++) {
            if (larikKata[k].equalsIgnoreCase(sasaran)) {
                total++;
            }
        }
        return total;
    }

    // Menghitung rata-rata panjang kata
    public double reratakanPanjang(String bahan) {
        int banyakKata = cacahKata(bahan);
        if (banyakKata == 0) {
            return 0;
        }
        String[] larikKata = bahan.trim().split("\\s+");
        int akumulasi = 0;
        for (int k = 0; k < larikKata.length; k++) {
            akumulasi += larikKata[k].length();
        }
        return (double) akumulasi / banyakKata;
    }

    // Menampilkan setiap kata beserta nomor urutnya
    public void cetakRincianKata(String bahan) {
        if (cacahKata(bahan) == 0) {
            System.out.print("Tidak ada kata.");
            System.out.print(System.lineSeparator());
            return;
        }
        String[] larikKata = bahan.trim().split("\\s+");
        for (int k = 0; k < larikKata.length; k++) {
            System.out.print(new StringBuilder().append(k + 1).append(". ").append(larikKata[k]).append(" (").append(larikKata[k].length()).append(" karakter)").toString());
            System.out.print(System.lineSeparator());
        }
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

    // Membaca satu baris teks dari pengguna
    public static String ambilBaris(Scanner pemindai, String keterangan) {
        System.out.print(keterangan);
        return pemindai.nextLine();
    }

    public static void main(String[] args) {
        Scanner pemindai = new Scanner(System.in);
        WordCounter pencacah = new WordCounter();

        int opsi;
        do {
            System.out.print("\n===== PROGRAM PENGHITUNG KATA =====");
            System.out.print(System.lineSeparator());
            System.out.print("1. Hitung jumlah kata");
            System.out.print(System.lineSeparator());
            System.out.print("2. Hitung jumlah huruf");
            System.out.print(System.lineSeparator());
            System.out.print("3. Cari kata terpanjang");
            System.out.print(System.lineSeparator());
            System.out.print("4. Hitung frekuensi sebuah kata");
            System.out.print(System.lineSeparator());
            System.out.print("5. Rata-rata panjang kata");
            System.out.print(System.lineSeparator());
            System.out.print("6. Tampilkan daftar kata");
            System.out.print(System.lineSeparator());
            System.out.print("0. Keluar");
            System.out.print(System.lineSeparator());
            opsi = ambilInteger(pemindai, "Pilih menu: ");

            switch (opsi) {
                case 1:
                    String s1 = ambilBaris(pemindai, "Masukkan kalimat: ");
                    System.out.print(new StringBuilder().append("Jumlah kata : ").append(pencacah.cacahKata(s1)).toString());
                    System.out.print(System.lineSeparator());
                    break;
                case 2:
                    String s2 = ambilBaris(pemindai, "Masukkan kalimat: ");
                    System.out.print(new StringBuilder().append("Jumlah huruf dan angka : ").append(pencacah.cacahKarakter(s2)).toString());
                    System.out.print(System.lineSeparator());
                    break;
                case 3:
                    String s3 = ambilBaris(pemindai, "Masukkan kalimat: ");
                    String kataJuara = pencacah.temukanTerpanjang(s3);
                    if (kataJuara.isEmpty()) {
                        System.out.print("Kalimat kosong, tidak ada kata.");
                        System.out.print(System.lineSeparator());
                    } else {
                        System.out.print(new StringBuilder().append("Kata terpanjang : ").append(kataJuara)
                                .append(" (").append(kataJuara.length()).append(" karakter)").toString());
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 4:
                    String s4 = ambilBaris(pemindai, "Masukkan kalimat: ");
                    String kataCari = ambilBaris(pemindai, "Kata yang dicari: ");
                    System.out.print(new StringBuilder().append("Kata \"").append(kataCari).append("\" muncul ")
                            .append(pencacah.cacahKemunculan(s4, kataCari)).append(" kali.").toString());
                    System.out.print(System.lineSeparator());
                    break;
                case 5:
                    String s5 = ambilBaris(pemindai, "Masukkan kalimat: ");
                    System.out.printf("Rata-rata panjang kata : %.2f%n",
                            pencacah.reratakanPanjang(s5));
                    break;
                case 6:
                    String s6 = ambilBaris(pemindai, "Masukkan kalimat: ");
                    pencacah.cetakRincianKata(s6);
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