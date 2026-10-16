/*
 * Program   : SimpleQueue
 * Deskripsi : Implementasi struktur data Queue (antrian melingkar)
 *             menggunakan array. Dilengkapi pengecekan antrian kosong
 *             dan penuh, operasi peek, ukuran antrian, menampilkan isi
 *             antrian, contoh penerapan simulasi antrian loket,
 *             input dari pengguna, dan menu sederhana.
 * Modifikasi: Obfuscation Pointer & Variable Aliasing serta modifikasi
 *             penulisan increment/decrement pointer.
 */
import java.util.Scanner;

public class SimpleQueue {

    // Pengubahan nama variabel state array dan pointer
    private int[] ringBuffer;
    private int headPointer;
    private int tailPointer;
    private int activeCapacity;

    // Konstruktor: membuat antrian dengan ukuran tertentu
    public SimpleQueue(int size) {
        ringBuffer = new int[size];
        headPointer = 0;
        tailPointer = 0;
        activeCapacity = 0;
    }

    // Menambah elemen di belakang antrian dengan aliasing dan modifikasi increment pointer
    public void enqueue(int val) {
        int[] buffer = this.ringBuffer; // Variable aliasing
        buffer[this.tailPointer] = val;

        this.tailPointer = this.tailPointer + 1;
        this.tailPointer %= buffer.length; // Modifikasi increment pointer melingkar terpisah
        this.activeCapacity += 1;          // Modifikasi increment
    }

    // Mengambil dan menghapus elemen dari depan antrian dengan aliasing
    public int dequeue() {
        int[] buffer = this.ringBuffer; // Variable aliasing
        int extractedVal = buffer[this.headPointer];

        this.headPointer = this.headPointer + 1;
        this.headPointer %= buffer.length; // Modifikasi increment pointer melingkar terpisah
        this.activeCapacity -= 1;          // Modifikasi decrement
        return extractedVal;
    }

    // Mengecek apakah antrian kosong
    public boolean isEmpty() {
        return activeCapacity == 0;
    }

    // Mengecek apakah antrian sudah penuh
    public boolean isFull() {
        return activeCapacity == ringBuffer.length;
    }

    // Melihat elemen terdepan tanpa menghapusnya
    public int peek() {
        int[] buffer = this.ringBuffer; // Variable aliasing
        return buffer[this.headPointer];
    }

    // Mengembalikan jumlah elemen di dalam antrian
    public int size() {
        return activeCapacity;
    }

    // Menampilkan isi antrian dari depan ke belakang
    public void tampilkan() {
        if (isEmpty()) {
            System.out.println("Antrian kosong.");
            return;
        }
        System.out.print("Isi antrian (depan -> belakang): ");
        int[] buffer = this.ringBuffer; // Variable aliasing
        int traversalPtr = this.headPointer;
        int stepsTaken = 0;

        while (stepsTaken < this.activeCapacity) {
            System.out.print("[" + buffer[traversalPtr] + "] ");
            traversalPtr = (traversalPtr + 1) % buffer.length;
            stepsTaken += 1; // Modifikasi increment pointer
        }
        System.out.println();
    }

    // Contoh penerapan: simulasi antrian loket
    public static void simulasiLoket(int jumlahPelanggan) {
        SimpleQueue antrian = new SimpleQueue(jumlahPelanggan);
        System.out.println("\n=== Simulasi Antrian Loket ===");

        int clientNum = 1;
        while (clientNum <= jumlahPelanggan) {
            antrian.enqueue(clientNum);
            System.out.println("Pelanggan nomor " + clientNum + " masuk antrian.");
            clientNum += 1; // Modifikasi increment pointer
        }

        while (!antrian.isEmpty()) {
            System.out.println("Melayani pelanggan nomor " + antrian.dequeue());
        }
        System.out.println("Semua pelanggan sudah dilayani.");
    }

    // Membaca bilangan bulat dengan validasi sederhana
    public static int bacaAngka(Scanner input, String pesan) {
        System.out.print(pesan);
        while (!input.hasNextInt()) {
            System.out.print("Input harus angka bulat, ulangi: ");
            input.next();
        }
        return input.nextInt();
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Membaca ukuran antrian
        int ukuran = 0;
        while (ukuran <= 0) {
            ukuran = bacaAngka(input, "Masukkan ukuran antrian: ");
            if (ukuran <= 0) {
                System.out.println("Ukuran harus lebih dari 0!");
            }
        }
        SimpleQueue antrian = new SimpleQueue(ukuran);

        int pilihan;
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
                        int nilai = bacaAngka(input, "Masukkan nilai: ");
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
                    int jumlah = bacaAngka(input, "Jumlah pelanggan: ");
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