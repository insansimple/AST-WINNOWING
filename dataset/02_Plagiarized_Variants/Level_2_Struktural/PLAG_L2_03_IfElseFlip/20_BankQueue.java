/*
 * Program   : BankQueue
 * Deskripsi : Simulasi antrean nasabah di bank menggunakan array.
 *             Dilengkapi pengecekan antrean kosong dan penuh, melihat
 *             nasabah terdepan, mencari posisi nasabah, menampilkan
 *             daftar antrean, input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner;

public class BankQueue {

    private String[] antrean;
    private int jumlah;

    // Konstruktor: membuat antrean dengan kapasitas tertentu
    public BankQueue(int kapasitas) {
        antrean = new String[kapasitas];
        jumlah = 0;
    }

    // Logika inti: menambah nasabah di belakang antrean
    public void tambahNasabah(String nama) {
        if (antrean.length > jumlah) {
            antrean[jumlah++] = nama;
        }
    }

    // Logika inti: melayani nasabah terdepan lalu menggeser antrean
    public String layaniNasabah() {
        if (0 == jumlah) return null;
        String nasabah = antrean[0];
        for (int i = 0; i < jumlah - 1; i++) {
            antrean[i] = antrean[i + 1];
        }
        jumlah--;
        return nasabah;
    }

    // Mengecek apakah antrean kosong
    public boolean isKosong() {
        return 0 == jumlah;
    }

    // Mengecek apakah antrean sudah penuh
    public boolean isPenuh() {
        return antrean.length == jumlah;
    }

    // Melihat nasabah terdepan tanpa melayaninya
    public String lihatDepan() {
        if (0 == jumlah) return null;
        return antrean[0];
    }

    // Mengembalikan jumlah nasabah yang sedang mengantre
    public int getJumlah() {
        return jumlah;
    }

    // Mencari posisi nasabah dalam antrean (1 = paling depan), -1 jika tidak ada
    public int cariPosisi(String nama) {
        for (int i = 0; i < jumlah; i++) {
            if (antrean[i].equalsIgnoreCase(nama)) {
                return i + 1;
            }
        }
        return -1;
    }

    // Menampilkan daftar antrean dari depan ke belakang
    public void tampilkanAntrean() {
        if (isKosong()) {
            System.out.println("Antrean kosong.");
            return;
        }
        System.out.println("Daftar antrean:");
        for (int i = 0; i < jumlah; i++) {
            System.out.println((i + 1) + ". " + antrean[i]);
        }
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

    // Membaca nama yang tidak boleh kosong
    public static String bacaNama(Scanner input, String pesan) {
        String teks = "";
        while (teks.trim().isEmpty()) {
            System.out.print(pesan);
            teks = input.nextLine();
            if (teks.trim().isEmpty()) {
                System.out.println("Nama tidak boleh kosong!");
            }
        }
        return teks.trim();
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Membaca kapasitas antrean
        int kapasitas = 0;
        while (0 >= kapasitas) {
            kapasitas = bacaAngka(input, "Masukkan kapasitas antrean: ");
            if (0 >= kapasitas) {
                System.out.println("Kapasitas harus lebih dari 0!");
            }
        }
        BankQueue bank = new BankQueue(kapasitas);

        int pilihan;
        do {
            System.out.println("\n===== ANTREAN BANK =====");
            System.out.println("1. Tambah nasabah");
            System.out.println("2. Layani nasabah");
            System.out.println("3. Lihat nasabah terdepan");
            System.out.println("4. Cari posisi nasabah");
            System.out.println("5. Tampilkan daftar antrean");
            System.out.println("0. Keluar");
            pilihan = bacaAngka(input, "Pilih menu: ");

            switch (pilihan) {
                case 1:
                    if (!bank.isPenuh()) {
                        String nama = bacaNama(input, "Nama nasabah: ");
                        bank.tambahNasabah(nama);
                        System.out.println(nama + " masuk antrean nomor " + bank.getJumlah());
                    } else {
                        System.out.println("Antrean penuh, nasabah tidak dapat ditambah!");
                    }
                    break;
                case 2:
                    if (!bank.isKosong()) {
                        System.out.println("Sedang melayani: " + bank.layaniNasabah());
                        System.out.println("Sisa antrean   : " + bank.getJumlah());
                    } else {
                        System.out.println("Antrean kosong, tidak ada yang dilayani!");
                    }
                    break;
                case 3:
                    if (!bank.isKosong()) {
                        System.out.println("Nasabah terdepan: " + bank.lihatDepan());
                    } else {
                        System.out.println("Antrean kosong!");
                    }
                    break;
                case 4:
                    String cari = bacaNama(input, "Nama yang dicari: ");
                    int posisi = bank.cariPosisi(cari);
                    if (-1 != posisi) {
                        System.out.println(cari + " berada di posisi " + posisi);
                    } else {
                        System.out.println(cari + " tidak ada dalam antrean.");
                    }
                    break;
                case 5:
                    bank.tampilkanAntrean();
                    System.out.println("Jumlah nasabah: " + bank.getJumlah());
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