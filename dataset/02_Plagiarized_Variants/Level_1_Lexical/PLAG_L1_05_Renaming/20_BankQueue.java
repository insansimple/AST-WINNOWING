/*
 * Program   : BankQueue
 * Deskripsi : Simulasi antrean nasabah di bank menggunakan array.
 *             Dilengkapi pengecekan antrean kosong dan penuh, melihat
 *             nasabah terdepan, mencari posisi nasabah, menampilkan
 *             daftar antrean, input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner;

public class BankQueue {

    private String[] barisan;
    private int terisi;

    // Konstruktor: membuat antrean dengan kapasitas tertentu
    public BankQueue(int daya) {
        barisan = new String[daya];
        terisi = 0;
    }

    // Logika inti: menambah nasabah di belakang antrean
    public void masukkanNasabah(String sebutan) {
        if (terisi < barisan.length) {
            barisan[terisi++] = sebutan;
        }
    }

    // Logika inti: melayani nasabah terdepan lalu menggeser antrean
    public String proseNasabah() {
        if (terisi == 0) return null;
        String pelanggan = barisan[0];
        for (int k = 0; k < terisi - 1; k++) {
            barisan[k] = barisan[k + 1];
        }
        terisi--;
        return pelanggan;
    }

    // Mengecek apakah antrean kosong
    public boolean kosongkah() {
        return terisi == 0;
    }

    // Mengecek apakah antrean sudah penuh
    public boolean penuhkah() {
        return terisi == barisan.length;
    }

    // Melihat nasabah terdepan tanpa melayaninya
    public String intipPertama() {
        if (terisi == 0) return null;
        return barisan[0];
    }

    // Mengembalikan jumlah nasabah yang sedang mengantre
    public int ambilCacah() {
        return terisi;
    }

    // Mencari posisi nasabah dalam antrean (1 = paling depan), -1 jika tidak ada
    public int temukanUrutan(String sebutan) {
        for (int k = 0; k < terisi; k++) {
            if (barisan[k].equalsIgnoreCase(sebutan)) {
                return k + 1;
            }
        }
        return -1;
    }

    // Menampilkan daftar antrean dari depan ke belakang
    public void cetakBarisan() {
        if (kosongkah()) {
            System.out.print("Antrean kosong.");
            System.out.print(System.lineSeparator());
            return;
        }
        System.out.print("Daftar antrean:");
        System.out.print(System.lineSeparator());
        for (int k = 0; k < terisi; k++) {
            System.out.print(new StringBuilder().append(k + 1).append(". ").append(barisan[k]).toString());
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

    // Membaca nama yang tidak boleh kosong
    public static String ambilSebutan(Scanner pemindai, String keterangan) {
        String isian = "";
        while (isian.trim().isEmpty()) {
            System.out.print(keterangan);
            isian = pemindai.nextLine();
            if (isian.trim().isEmpty()) {
                System.out.print("Nama tidak boleh kosong!");
                System.out.print(System.lineSeparator());
            }
        }
        return isian.trim();
    }

    public static void main(String[] args) {
        Scanner pemindai = new Scanner(System.in);

        // Membaca kapasitas antrean
        int dayaTampung = 0;
        while (dayaTampung <= 0) {
            dayaTampung = ambilInteger(pemindai, "Masukkan kapasitas antrean: ");
            if (dayaTampung <= 0) {
                System.out.print("Kapasitas harus lebih dari 0!");
                System.out.print(System.lineSeparator());
            }
        }
        BankQueue loket = new BankQueue(dayaTampung);

        int opsi;
        do {
            System.out.print("\n===== ANTREAN BANK =====");
            System.out.print(System.lineSeparator());
            System.out.print("1. Tambah nasabah");
            System.out.print(System.lineSeparator());
            System.out.print("2. Layani nasabah");
            System.out.print(System.lineSeparator());
            System.out.print("3. Lihat nasabah terdepan");
            System.out.print(System.lineSeparator());
            System.out.print("4. Cari posisi nasabah");
            System.out.print(System.lineSeparator());
            System.out.print("5. Tampilkan daftar antrean");
            System.out.print(System.lineSeparator());
            System.out.print("0. Keluar");
            System.out.print(System.lineSeparator());
            opsi = ambilInteger(pemindai, "Pilih menu: ");

            switch (opsi) {
                case 1:
                    if (loket.penuhkah()) {
                        System.out.print("Antrean penuh, nasabah tidak dapat ditambah!");
                        System.out.print(System.lineSeparator());
                    } else {
                        String namaBaru = ambilSebutan(pemindai, "Nama nasabah: ");
                        loket.masukkanNasabah(namaBaru);
                        System.out.print(new StringBuilder().append(namaBaru).append(" masuk antrean nomor ").append(loket.ambilCacah()).toString());
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 2:
                    if (loket.kosongkah()) {
                        System.out.print("Antrean kosong, tidak ada yang dilayani!");
                        System.out.print(System.lineSeparator());
                    } else {
                        System.out.print(new StringBuilder().append("Sedang melayani: ").append(loket.proseNasabah()).toString());
                        System.out.print(System.lineSeparator());
                        System.out.print(new StringBuilder().append("Sisa antrean   : ").append(loket.ambilCacah()).toString());
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 3:
                    if (loket.kosongkah()) {
                        System.out.print("Antrean kosong!");
                        System.out.print(System.lineSeparator());
                    } else {
                        System.out.print(new StringBuilder().append("Nasabah terdepan: ").append(loket.intipPertama()).toString());
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 4:
                    String namaCari = ambilSebutan(pemindai, "Nama yang dicari: ");
                    int urutan = loket.temukanUrutan(namaCari);
                    if (urutan == -1) {
                        System.out.print(new StringBuilder().append(namaCari).append(" tidak ada dalam antrean.").toString());
                        System.out.print(System.lineSeparator());
                    } else {
                        System.out.print(new StringBuilder().append(namaCari).append(" berada di posisi ").append(urutan).toString());
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 5:
                    loket.cetakBarisan();
                    System.out.print(new StringBuilder().append("Jumlah nasabah: ").append(loket.ambilCacah()).toString());
                    System.out.print(System.lineSeparator());
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