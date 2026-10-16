/*
 * Program   : BankQueue
 * Deskripsi : Simulasi antrean nasabah di bank menggunakan array.
 *             Dilengkapi pengecekan antrean kosong dan penuh, melihat
 *             nasabah terdepan, mencari posisi nasabah, menampilkan
 *             daftar antrean, input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner;

public class BankQueue {

    private String[] daftar;
    private int panjang;

    // Konstruktor: membuat antrean dengan kapasitas tertentu
    public BankQueue(int maks) {
        daftar = new String[maks];
        panjang = 0;
    }

    // Logika inti: menambah nasabah di belakang antrean
    public void daftarkan(String orang) {
        if (panjang < daftar.length) {
            daftar[panjang++] = orang;
        }
    }

    // Logika inti: melayani nasabah terdepan lalu menggeser antrean
    public String layani() {
        if (panjang == 0) return null;
        String orang = daftar[0];
        for (int pos = 0; pos < panjang - 1; pos++) {
            daftar[pos] = daftar[pos + 1];
        }
        panjang--;
        return orang;
    }

    // Mengecek apakah antrean kosong
    public boolean kosong() {
        return panjang == 0;
    }

    // Mengecek apakah antrean sudah penuh
    public boolean penuh() {
        return panjang == daftar.length;
    }

    // Melihat nasabah terdepan tanpa melayaninya
    public String lihatTerdepan() {
        if (panjang == 0) return null;
        return daftar[0];
    }

    // Mengembalikan jumlah nasabah yang sedang mengantre
    public int getPanjang() {
        return panjang;
    }

    // Mencari posisi nasabah dalam antrean (1 = paling depan), -1 jika tidak ada
    public int temukanPosisi(String orang) {
        for (int pos = 0; pos < panjang; pos++) {
            if (daftar[pos].equalsIgnoreCase(orang)) {
                return pos + 1;
            }
        }
        return -1;
    }

    // Menampilkan daftar antrean dari depan ke belakang
    public void tampilkan() {
        if (kosong()) {
            System.out.println("Antrean kosong.");
            return;
        }
        System.out.println("Daftar antrean:");
        for (int pos = 0; pos < panjang; pos++) {
            System.out.println((pos + 1) + ". " + daftar[pos]);
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

    // Membaca nama yang tidak boleh kosong
    public static String ambilNama(Scanner scanner, String teks) {
        String masukan = "";
        while (masukan.trim().isEmpty()) {
            System.out.print(teks);
            masukan = scanner.nextLine();
            if (masukan.trim().isEmpty()) {
                System.out.println("Nama tidak boleh kosong!");
            }
        }
        return masukan.trim();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Membaca kapasitas antrean
        int maks = 0;
        while (maks <= 0) {
            maks = ambilAngka(scanner, "Masukkan kapasitas antrean: ");
            if (maks <= 0) {
                System.out.println("Kapasitas harus lebih dari 0!");
            }
        }
        BankQueue antrian = new BankQueue(maks);

        int menu;
        do {
            System.out.println("\n===== ANTREAN BANK =====");
            System.out.println("1. Tambah nasabah");
            System.out.println("2. Layani nasabah");
            System.out.println("3. Lihat nasabah terdepan");
            System.out.println("4. Cari posisi nasabah");
            System.out.println("5. Tampilkan daftar antrean");
            System.out.println("0. Keluar");
            menu = ambilAngka(scanner, "Pilih menu: ");

            switch (menu) {
                case 1:
                    if (antrian.penuh()) {
                        System.out.println("Antrean penuh, nasabah tidak dapat ditambah!");
                    } else {
                        String orang = ambilNama(scanner, "Nama nasabah: ");
                        antrian.daftarkan(orang);
                        System.out.println(orang + " masuk antrean nomor " + antrian.getPanjang());
                    }
                    break;
                case 2:
                    if (antrian.kosong()) {
                        System.out.println("Antrean kosong, tidak ada yang dilayani!");
                    } else {
                        System.out.println("Sedang melayani: " + antrian.layani());
                        System.out.println("Sisa antrean   : " + antrian.getPanjang());
                    }
                    break;
                case 3:
                    if (antrian.kosong()) {
                        System.out.println("Antrean kosong!");
                    } else {
                        System.out.println("Nasabah terdepan: " + antrian.lihatTerdepan());
                    }
                    break;
                case 4:
                    String cari = ambilNama(scanner, "Nama yang dicari: ");
                    int posisi = antrian.temukanPosisi(cari);
                    if (posisi == -1) {
                        System.out.println(cari + " tidak ada dalam antrean.");
                    } else {
                        System.out.println(cari + " berada di posisi " + posisi);
                    }
                    break;
                case 5:
                    antrian.tampilkan();
                    System.out.println("Jumlah nasabah: " + antrian.getPanjang());
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