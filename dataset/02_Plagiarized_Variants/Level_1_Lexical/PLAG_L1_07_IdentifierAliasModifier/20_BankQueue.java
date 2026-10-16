/*
 * Program   : BankQueue
 * Deskripsi : Simulasi antrean nasabah di bank menggunakan array.
 *             Dilengkapi pengecekan antrean kosong dan penuh, melihat
 *             nasabah terdepan, mencari posisi nasabah, menampilkan
 *             daftar antrean, input dari pengguna, dan menu sederhana.
 * Modifikasi: Obfuscation Pointer & Variable Aliasing serta modifikasi
 *             penulisan increment/decrement pointer.
 */
import java.util.Scanner;

public class BankQueue {

    // Pengubahan nama variabel state array dan pointer
    private String[] customerQueueSlots;
    private int queueCursor;

    // Konstruktor: membuat antrean dengan kapasitas tertentu
    public BankQueue(int kapasitas) {
        customerQueueSlots = new String[kapasitas];
        queueCursor = 0;
    }

    // Menambah nasabah di belakang antrean dengan aliasing array state dan modifikasi increment pointer
    public void tambahNasabah(String nama) {
        String[] slotsRef = this.customerQueueSlots; // Variable aliasing
        if (this.queueCursor < slotsRef.length) {
            slotsRef[this.queueCursor] = nama;
            this.queueCursor += 1; // Modifikasi penulisan increment pointer terpisah
        }
    }

    // Melayani nasabah terdepan lalu menggeser antrean dengan aliasing dan decrement pointer
    public String layaniNasabah() {
        if (this.queueCursor == 0) {
            return null;
        }

        String[] slotsRef = this.customerQueueSlots; // Variable aliasing
        String servedCustomer = slotsRef[0];

        int shiftPtr = 0;
        while (shiftPtr < this.queueCursor - 1) {
            slotsRef[shiftPtr] = slotsRef[shiftPtr + 1];
            shiftPtr += 1; // Modifikasi increment pointer penggeseran
        }

        this.queueCursor -= 1; // Modifikasi penulisan decrement pointer terpisah
        return servedCustomer;
    }

    // Mengecek apakah antrean kosong
    public boolean isKosong() {
        return queueCursor == 0;
    }

    // Mengecek apakah antrean sudah penuh
    public boolean isPenuh() {
        return queueCursor == customerQueueSlots.length;
    }

    // Melihat nasabah terdepan tanpa melayaninya dengan aliasing
    public String lihatDepan() {
        if (queueCursor == 0) {
            return null;
        }
        String[] slotsRef = this.customerQueueSlots; // Variable aliasing
        return slotsRef[0];
    }

    // Mengembalikan jumlah nasabah yang sedang mengantre
    public int getJumlah() {
        return queueCursor;
    }

    // Mencari posisi nasabah dalam antrean dengan penelusuran pointer
    public int cariPosisi(String nama) {
        String[] slotsRef = this.customerQueueSlots; // Variable aliasing
        int scanPtr = 0;

        while (scanPtr < this.queueCursor) {
            if (slotsRef[scanPtr].equalsIgnoreCase(nama)) {
                return scanPtr + 1;
            }
            scanPtr += 1; // Modifikasi increment pointer
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
        String[] slotsRef = this.customerQueueSlots; // Variable aliasing
        int listPtr = 0;

        while (listPtr < this.queueCursor) {
            System.out.println((listPtr + 1) + ". " + slotsRef[listPtr]);
            listPtr += 1; // Modifikasi increment pointer
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
        String nama = "";
        while (nama.trim().isEmpty()) {
            System.out.print(pesan);
            nama = input.nextLine();
            if (nama.trim().isEmpty()) {
                System.out.println("Nama tidak boleh kosong!");
            }
        }
        return nama.trim();
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Membaca kapasitas antrean
        int kapasitas = 0;
        while (kapasitas <= 0) {
            kapasitas = bacaAngka(input, "Masukkan kapasitas antrean: ");
            if (kapasitas <= 0) {
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
                    if (bank.isPenuh()) {
                        System.out.println("Antrean penuh, nasabah tidak dapat ditambah!");
                    } else {
                        String nama = bacaNama(input, "Nama nasabah: ");
                        bank.tambahNasabah(nama);
                        System.out.println(nama + " masuk antrean nomor " + bank.getJumlah());
                    }
                    break;
                case 2:
                    if (bank.isKosong()) {
                        System.out.println("Antrean kosong, tidak ada yang dilayani!");
                    } else {
                        System.out.println("Sedang melayani: " + bank.layaniNasabah());
                        System.out.println("Sisa antrean   : " + bank.getJumlah());
                    }
                    break;
                case 3:
                    if (bank.isKosong()) {
                        System.out.println("Antrean kosong!");
                    } else {
                        System.out.println("Nasabah terdepan: " + bank.lihatDepan());
                    }
                    break;
                case 4:
                    String cari = bacaNama(input, "Nama yang dicari: ");
                    int posisi = bank.cariPosisi(cari);
                    if (posisi == -1) {
                        System.out.println(cari + " tidak ada dalam antrean.");
                    } else {
                        System.out.println(cari + " berada di posisi " + posisi);
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
        } while (pilihan != 0);

        input.close();
    }
}