/*
 * Program   : SimpleQueue
 * Deskripsi : Implementasi struktur data Queue (antrian melingkar)
 *             menggunakan array. Dilengkapi pengecekan antrian kosong
 *             dan penuh, operasi peek, ukuran antrian, menampilkan isi
 *             antrian, contoh penerapan simulasi antrian loket,
 *             input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner;

public class SimpleQueue {

    private int[] wadah;
    private int kepala;
    private int ekor;
    private int terisi;

    // Konstruktor: membuat antrian dengan ukuran tertentu
    public SimpleQueue(int dimensi) {
        wadah = new int[dimensi];
        kepala = 0;
        ekor = 0;
        terisi = 0;
    }

    // Menambah elemen di belakang antrian
    public void masukkan(int angka) {
        wadah[ekor] = angka;
        ekor = (ekor + 1) % wadah.length;
        terisi++;
    }

    // Mengambil dan menghapus elemen dari depan antrian
    public int keluarkan() {
        int angka = wadah[kepala];
        kepala = (kepala + 1) % wadah.length;
        terisi--;
        return angka;
    }

    // Mengecek apakah antrian kosong
    public boolean kosongkah() {
        return terisi == 0;
    }

    // Mengecek apakah antrian sudah penuh
    public boolean penuhkah() {
        return terisi == wadah.length;
    }

    // Melihat elemen terdepan tanpa menghapusnya
    public int intipDepan() {
        return wadah[kepala];
    }

    // Mengembalikan jumlah elemen di dalam antrian
    public int cacah() {
        return terisi;
    }

    // Menampilkan isi antrian dari depan ke belakang
    public void cetakIsi() {
        if (kosongkah()) {
            System.out.print("Antrian kosong.");
            System.out.print(System.lineSeparator());
            return;
        }
        System.out.print("Isi antrian (depan -> belakang): ");
        int penunjuk = kepala;
        for (int k = 0; k < terisi; k++) {
            System.out.print(new StringBuilder().append("[").append(wadah[penunjuk]).append("] ").toString());
            penunjuk = (penunjuk + 1) % wadah.length;
        }
        System.out.print(System.lineSeparator());
    }

    // Contoh penerapan: simulasi antrian loket, nomor antrian dilayani berurutan
    public static void jalankanLoket(int banyakOrang) {
        SimpleQueue barisan = new SimpleQueue(banyakOrang);
        System.out.print("\n=== Simulasi Antrian Loket ===");
        System.out.print(System.lineSeparator());
        for (int k = 1; k <= banyakOrang; k++) {
            barisan.masukkan(k);
            System.out.print(new StringBuilder().append("Pelanggan nomor ").append(k).append(" masuk antrian.").toString());
            System.out.print(System.lineSeparator());
        }
        while (!barisan.kosongkah()) {
            System.out.print(new StringBuilder().append("Melayani pelanggan nomor ").append(barisan.keluarkan()).toString());
            System.out.print(System.lineSeparator());
        }
        System.out.print("Semua pelanggan sudah dilayani.");
        System.out.print(System.lineSeparator());
    }

    // Membaca bilangan bulat dengan validasi sederhana
    public static int ambilInteger(Scanner pemindai, String keterangan) {
        System.out.print(keterangan);
        while (!pemindai.hasNextInt()) {
            System.out.print("Input harus angka bulat, ulangi: ");
            pemindai.next();
        }
        return pemindai.nextInt();
    }

    public static void main(String[] args) {
        Scanner pemindai = new Scanner(System.in);

        // Membaca ukuran antrian
        int panjangAntre = 0;
        while (panjangAntre <= 0) {
            panjangAntre = ambilInteger(pemindai, "Masukkan ukuran antrian: ");
            if (panjangAntre <= 0) {
                System.out.print("Ukuran harus lebih dari 0!");
                System.out.print(System.lineSeparator());
            }
        }
        SimpleQueue barisanUtama = new SimpleQueue(panjangAntre);

        int opsi;
        do {
            System.out.print("\n===== PROGRAM QUEUE =====");
            System.out.print(System.lineSeparator());
            System.out.print("1. Enqueue (tambah elemen)");
            System.out.print(System.lineSeparator());
            System.out.print("2. Dequeue (ambil elemen)");
            System.out.print(System.lineSeparator());
            System.out.print("3. Peek (lihat elemen depan)");
            System.out.print(System.lineSeparator());
            System.out.print("4. Tampilkan isi antrian");
            System.out.print(System.lineSeparator());
            System.out.print("5. Simulasi antrian loket");
            System.out.print(System.lineSeparator());
            System.out.print("0. Keluar");
            System.out.print(System.lineSeparator());
            opsi = ambilInteger(pemindai, "Pilih menu: ");

            switch (opsi) {
                case 1:
                    if (barisanUtama.penuhkah()) {
                        System.out.print("Antrian penuh, tidak bisa enqueue!");
                        System.out.print(System.lineSeparator());
                    } else {
                        int angkaMasuk = ambilInteger(pemindai, "Masukkan nilai: ");
                        barisanUtama.masukkan(angkaMasuk);
                        System.out.print(new StringBuilder().append(angkaMasuk).append(" berhasil masuk antrian.").toString());
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 2:
                    if (barisanUtama.kosongkah()) {
                        System.out.print("Antrian kosong, tidak bisa dequeue!");
                        System.out.print(System.lineSeparator());
                    } else {
                        System.out.print(new StringBuilder().append("Elemen yang diambil: ").append(barisanUtama.keluarkan()).toString());
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 3:
                    if (barisanUtama.kosongkah()) {
                        System.out.print("Antrian kosong!");
                        System.out.print(System.lineSeparator());
                    } else {
                        System.out.print(new StringBuilder().append("Elemen terdepan: ").append(barisanUtama.intipDepan()).toString());
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 4:
                    barisanUtama.cetakIsi();
                    System.out.print(new StringBuilder().append("Jumlah elemen: ").append(barisanUtama.cacah()).toString());
                    System.out.print(System.lineSeparator());
                    break;
                case 5:
                    int totalOrang = ambilInteger(pemindai, "Jumlah pelanggan: ");
                    if (totalOrang > 0) {
                        jalankanLoket(totalOrang);
                    } else {
                        System.out.print("Jumlah pelanggan harus lebih dari 0!");
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