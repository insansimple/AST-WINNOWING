/*
 * Program   : BankQueue
 * Deskripsi : Simulasi antrean nasabah di bank menggunakan array.
 *             Dilengkapi pengecekan antrean kosong dan penuh, melihat
 *             nasabah terdepan, mencari posisi nasabah, menampilkan
 *             daftar antrean, input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner;

public class BankQueue {

    // Inner static class wrapper untuk entitas nasabah
    public static class CustomerEntity {
        private String name;

        public CustomerEntity(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public boolean matches(String otherName) {
            return name != null && name.equalsIgnoreCase(otherName);
        }
    }

    // Inner static class wrapper untuk penyimpanan register antrean nasabah
    public static class QueueRegister {
        private CustomerEntity[] queue;

        public QueueRegister(int capacity) {
            this.queue = new CustomerEntity[capacity];
        }

        public int capacity() {
            return queue.length;
        }

        public void set(int index, CustomerEntity customer) {
            queue[index] = customer;
        }

        public CustomerEntity get(int index) {
            return queue[index];
        }
    }

    private QueueRegister register;
    private int jumlah;

    // Konstruktor: membuat antrean dengan kapasitas tertentu
    public BankQueue(int kapasitas) {
        register = new QueueRegister(kapasitas);
        jumlah = 0;
    }

    // Logika inti: menambah nasabah di belakang antrean
    public void tambahNasabah(String nama) {
        if (jumlah < register.capacity()) {
            register.set(jumlah++, new CustomerEntity(nama));
        }
    }

    // Logika inti: melayani nasabah terdepan lalu menggeser antrean
    public String layaniNasabah() {
        if (jumlah == 0) return null;
        String nasabah = register.get(0).getName();
        for (int i = 0; i < jumlah - 1; i++) {
            register.set(i, register.get(i + 1));
        }
        jumlah--;
        return nasabah;
    }

    // Mengecek apakah antrean kosong
    public boolean isKosong() {
        return jumlah == 0;
    }

    // Mengecek apakah antrean sudah penuh
    public boolean isPenuh() {
        return jumlah == register.capacity();
    }

    // Melihat nasabah terdepan tanpa melayaninya
    public String lihatDepan() {
        if (jumlah == 0) return null;
        return register.get(0).getName();
    }

    // Mengembalikan jumlah nasabah yang sedang mengantre
    public int getJumlah() {
        return jumlah;
    }

    // Mencari posisi nasabah dalam antrean (1 = paling depan), -1 jika tidak ada
    public int cariPosisi(String nama) {
        for (int i = 0; i < jumlah; i++) {
            if (register.get(i).matches(nama)) {
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
            System.out.println((i + 1) + ". " + register.get(i).getName());
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

        int pilihan = 0;
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