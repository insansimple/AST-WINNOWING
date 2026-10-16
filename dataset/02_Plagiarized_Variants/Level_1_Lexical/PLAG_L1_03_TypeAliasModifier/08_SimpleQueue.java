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

    private int[] items;
    private Integer front;
    private Integer rear;
    private Integer count;

    // Konstruktor: membuat antrian dengan ukuran tertentu
    public SimpleQueue(Integer size) {
        items = new int[size];
        front = 0;
        rear = 0;
        count = 0;
    }

    // Menambah elemen di belakang antrian
    public void enqueue(Integer val) {
        items[rear] = val;
        rear = (rear + 1) % items.length;
        count++;
    }

    // Mengambil dan menghapus elemen dari depan antrian
    public Integer dequeue() {
        Integer val = items[front];
        front = (front + 1) % items.length;
        count--;
        return val;
    }

    // Mengecek apakah antrian kosong
    public Boolean isEmpty() {
        return count == 0;
    }

    // Mengecek apakah antrian sudah penuh
    public Boolean isFull() {
        return count == items.length;
    }

    // Melihat elemen terdepan tanpa menghapusnya
    public Integer peek() {
        return items[front];
    }

    // Mengembalikan jumlah elemen di dalam antrian
    public Integer size() {
        return count;
    }

    // Menampilkan isi antrian dari depan ke belakang
    public void tampilkan() {
        if (isEmpty()) {
            System.out.println("Antrian kosong.");
            return;
        }
        System.out.print("Isi antrian (depan -> belakang): ");
        Integer index = front;
        for (Integer i = 0; i < count; i++) {
            System.out.print("[" + items[index] + "] ");
            index = (index + 1) % items.length;
        }
        System.out.println();
    }

    // Contoh penerapan: simulasi antrian loket, nomor antrian dilayani berurutan
    static public void simulasiLoket(Integer jumlahPelanggan) {
        SimpleQueue antrian = new SimpleQueue(jumlahPelanggan);
        System.out.println("\n=== Simulasi Antrian Loket ===");
        for (Integer i = 1; i <= jumlahPelanggan; i++) {
            antrian.enqueue(i);
            System.out.println("Pelanggan nomor " + i + " masuk antrian.");
        }
        while (!antrian.isEmpty()) {
            System.out.println("Melayani pelanggan nomor " + antrian.dequeue());
        }
        System.out.println("Semua pelanggan sudah dilayani.");
    }

    // Membaca bilangan bulat dengan validasi sederhana
    static public Integer bacaAngka(Scanner input, String pesan) {
        System.out.print(pesan);
        while (!input.hasNextInt()) {
            System.out.print("Input harus angka bulat, ulangi: ");
            input.next();
        }
        return input.nextInt();
    }

    static public void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Membaca ukuran antrian
        Integer ukuran = 0;
        while (ukuran <= 0) {
            ukuran = bacaAngka(input, "Masukkan ukuran antrian: ");
            if (ukuran <= 0) {
                System.out.println("Ukuran harus lebih dari 0!");
            }
        }
        SimpleQueue antrian = new SimpleQueue(ukuran);

        Integer pilihan;
        do {
            System.out.println("\n===== PROGRAM QUEUE =====");
            System.out.println("1. Enqueue (tambah elemen)");
            System.out.println("2. Dequeue (ambil elemen)");
            System.out.println("3. Peek (lihat elemen depan)");
            System.out.println("4. Tampilkan isi antrian");
            System.out.println("5. Simulasi antrian loket");
            System.out.println("0. Keluar");
            pilihan = bacaAngka(input, "Pilih menu: ");

            switch (pilihan) {
                case 1:
                    if (antrian.isFull()) {
                        System.out.println("Antrian penuh, tidak bisa enqueue!");
                    } else {
                        Integer nilai = bacaAngka(input, "Masukkan nilai: ");
                        antrian.enqueue(nilai);
                        System.out.println(nilai + " berhasil masuk antrian.");
                    }
                    break;
                case 2:
                    if (antrian.isEmpty()) {
                        System.out.println("Antrian kosong, tidak bisa dequeue!");
                    } else {
                        System.out.println("Elemen yang diambil: " + antrian.dequeue());
                    }
                    break;
                case 3:
                    if (antrian.isEmpty()) {
                        System.out.println("Antrian kosong!");
                    } else {
                        System.out.println("Elemen terdepan: " + antrian.peek());
                    }
                    break;
                case 4:
                    antrian.tampilkan();
                    System.out.println("Jumlah elemen: " + antrian.size());
                    break;
                case 5:
                    Integer jumlah = bacaAngka(input, "Jumlah pelanggan: ");
                    if (jumlah > 0) {
                        simulasiLoket(jumlah);
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
        } while (pilihan != 0);

        input.close();
    }
}