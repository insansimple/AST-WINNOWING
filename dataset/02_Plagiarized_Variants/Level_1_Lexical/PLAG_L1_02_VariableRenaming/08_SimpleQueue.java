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
    private int depan;
    private int belakang;
    private int cacah;

    // Konstruktor: membuat antrian dengan ukuran tertentu
    public SimpleQueue(int kapasitas) {
        wadah = new int[kapasitas];
        depan = 0;
        belakang = 0;
        cacah = 0;
    }

    // Menambah elemen di belakang antrian
    public void tambah(int angka) {
        wadah[belakang] = angka;
        belakang = (belakang + 1) % wadah.length;
        cacah++;
    }

    // Mengambil dan menghapus elemen dari depan antrian
    public int ambil() {
        int angka = wadah[depan];
        depan = (depan + 1) % wadah.length;
        cacah--;
        return angka;
    }

    // Mengecek apakah antrian kosong
    public boolean kosong() {
        return cacah == 0;
    }

    // Mengecek apakah antrian sudah penuh
    public boolean penuh() {
        return cacah == wadah.length;
    }

    // Melihat elemen terdepan tanpa menghapusnya
    public int lihatDepan() {
        return wadah[depan];
    }

    // Mengembalikan jumlah elemen di dalam antrian
    public int banyak() {
        return cacah;
    }

    // Menampilkan isi antrian dari depan ke belakang
    public void cetak() {
        if (kosong()) {
            System.out.println("Antrian kosong.");
            return;
        }
        System.out.print("Isi antrian (depan -> belakang): ");
        int pos = depan;
        for (int idx = 0; idx < cacah; idx++) {
            System.out.print("[" + wadah[pos] + "] ");
            pos = (pos + 1) % wadah.length;
        }
        System.out.println();
    }

    // Contoh penerapan: simulasi antrian loket, nomor antrian dilayani berurutan
    public static void simulasiLoket(int banyakPelanggan) {
        SimpleQueue antrian = new SimpleQueue(banyakPelanggan);
        System.out.println("\n=== Simulasi Antrian Loket ===");
        for (int idx = 1; idx <= banyakPelanggan; idx++) {
            antrian.tambah(idx);
            System.out.println("Pelanggan nomor " + idx + " masuk antrian.");
        }
        while (!antrian.kosong()) {
            System.out.println("Melayani pelanggan nomor " + antrian.ambil());
        }
        System.out.println("Semua pelanggan sudah dilayani.");
    }

    // Membaca bilangan bulat dengan validasi sederhana
    public static int ambilAngka(Scanner scanner, String teks) {
        System.out.print(teks);
        while (!scanner.hasNextInt()) {
            System.out.print("Input harus angka bulat, ulangi: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Membaca ukuran antrian
        int maks = 0;
        while (maks <= 0) {
            maks = ambilAngka(scanner, "Masukkan ukuran antrian: ");
            if (maks <= 0) {
                System.out.println("Ukuran harus lebih dari 0!");
            }
        }
        SimpleQueue antrian = new SimpleQueue(maks);

        int menu;
        do {
            System.out.println("\n===== PROGRAM QUEUE =====");
            System.out.println("1. Enqueue (tambah elemen)");
            System.out.println("2. Dequeue (ambil elemen)");
            System.out.println("3. Peek (lihat elemen depan)");
            System.out.println("4. Tampilkan isi antrian");
            System.out.println("5. Simulasi antrian loket");
            System.out.println("0. Keluar");
            menu = ambilAngka(scanner, "Pilih menu: ");

            switch (menu) {
                case 1:
                    if (antrian.penuh()) {
                        System.out.println("Antrian penuh, tidak bisa enqueue!");
                    } else {
                        int angka = ambilAngka(scanner, "Masukkan nilai: ");
                        antrian.tambah(angka);
                        System.out.println(angka + " berhasil masuk antrian.");
                    }
                    break;
                case 2:
                    if (antrian.kosong()) {
                        System.out.println("Antrian kosong, tidak bisa dequeue!");
                    } else {
                        System.out.println("Elemen yang diambil: " + antrian.ambil());
                    }
                    break;
                case 3:
                    if (antrian.kosong()) {
                        System.out.println("Antrian kosong!");
                    } else {
                        System.out.println("Elemen terdepan: " + antrian.lihatDepan());
                    }
                    break;
                case 4:
                    antrian.cetak();
                    System.out.println("Jumlah elemen: " + antrian.banyak());
                    break;
                case 5:
                    int jml = ambilAngka(scanner, "Jumlah pelanggan: ");
                    if (jml > 0) {
                        simulasiLoket(jml);
                    } else {
                        System.out.println("Jumlah pelanggan harus lebih dari 0!");
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