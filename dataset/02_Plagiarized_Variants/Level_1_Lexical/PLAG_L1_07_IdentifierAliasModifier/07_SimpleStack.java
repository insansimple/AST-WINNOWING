/*
 * Program   : SimpleStack
 * Deskripsi : Implementasi struktur data Stack menggunakan array.
 *             Dilengkapi pengecekan stack penuh, operasi peek, ukuran
 *             stack, menampilkan isi stack, contoh penerapan membalik
 *             kata, input dari pengguna, dan menu sederhana.
 * Modifikasi: Obfuscation Pointer & Variable Aliasing serta modifikasi
 *             penulisan increment/decrement pointer.
 */
import java.util.Scanner;

public class SimpleStack {

    // Pengubahan nama variabel state array dan pointer
    private int[] _internalBuffer;
    private int stackPointer;

    // Konstruktor: membuat stack dengan kapasitas tertentu
    public SimpleStack(int capacity) {
        _internalBuffer = new int[capacity];
        stackPointer = -1;
    }

    // Menambah elemen ke puncak stack dengan aliasing dan modifikasi increment pointer
    public void push(int item) {
        int[] bufferRef = this._internalBuffer; // Variable aliasing
        this.stackPointer += 1;                 // Modifikasi penulisan increment pointer terpisah
        bufferRef[this.stackPointer] = item;
    }

    // Mengambil dan menghapus elemen dengan aliasing dan modifikasi decrement pointer
    public int pop() {
        int[] bufferRef = this._internalBuffer;      // Variable aliasing
        int poppedValue = bufferRef[this.stackPointer];
        this.stackPointer -= 1;                      // Modifikasi penulisan decrement pointer terpisah
        return poppedValue;
    }

    // Mengecek apakah stack kosong
    public boolean isEmpty() {
        return stackPointer == -1;
    }

    // Mengecek apakah stack sudah penuh
    public boolean isFull() {
        return stackPointer == _internalBuffer.length - 1;
    }

    // Melihat elemen puncak tanpa menghapusnya
    public int peek() {
        int[] bufferRef = this._internalBuffer; // Variable aliasing
        return bufferRef[this.stackPointer];
    }

    // Mengembalikan jumlah elemen di dalam stack
    public int size() {
        return stackPointer + 1;
    }

    // Menampilkan isi stack dari puncak ke dasar dengan decrement pointer
    public void tampilkan() {
        if (isEmpty()) {
            System.out.println("Stack kosong.");
            return;
        }
        System.out.println("Isi stack (puncak -> dasar):");
        int[] bufferRef = this._internalBuffer; // Variable aliasing
        int walkPointer = this.stackPointer;

        while (walkPointer >= 0) {
            System.out.println("| " + bufferRef[walkPointer] + " |");
            walkPointer -= 1; // Modifikasi decrement pointer
        }
        System.out.println("-----");
    }

    // Contoh penerapan: membalik sebuah kata dengan stack
    public static String balikKata(String kata) {
        SimpleStack tumpukan = new SimpleStack(kata.length());
        int charPtr = 0;
        while (charPtr < kata.length()) {
            tumpukan.push(kata.charAt(charPtr));
            charPtr += 1; // Modifikasi increment pointer
        }

        String hasil = "";
        while (!tumpukan.isEmpty()) {
            hasil += (char) tumpukan.pop();
        }
        return hasil;
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

        // Membaca kapasitas stack
        int kapasitas = 0;
        while (kapasitas <= 0) {
            kapasitas = bacaAngka(input, "Masukkan kapasitas stack: ");
            if (kapasitas <= 0) {
                System.out.println("Kapasitas harus lebih dari 0!");
            }
        }
        SimpleStack stack = new SimpleStack(kapasitas);

        int pilihan;
        do {
            System.out.println("\n===== PROGRAM STACK =====");
            System.out.println("1. Push (tambah elemen)");
            System.out.println("2. Pop (ambil elemen)");
            System.out.println("3. Peek (lihat elemen puncak)");
            System.out.println("4. Tampilkan isi stack");
            System.out.println("5. Balik kata menggunakan stack");
            System.out.println("0. Keluar");
            pilihan = bacaAngka(input, "Pilih menu: ");

            switch (pilihan) {
                case 1:
                    if (stack.isFull()) {
                        System.out.println("Stack penuh, tidak bisa push!");
                    } else {
                        int nilai = bacaAngka(input, "Masukkan nilai: ");
                        stack.push(nilai);
                        System.out.println(nilai + " berhasil ditambahkan.");
                    }
                    break;
                case 2:
                    if (stack.isEmpty()) {
                        System.out.println("Stack kosong, tidak bisa pop!");
                    } else {
                        System.out.println("Elemen yang diambil: " + stack.pop());
                    }
                    break;
                case 3:
                    if (stack.isEmpty()) {
                        System.out.println("Stack kosong!");
                    } else {
                        System.out.println("Elemen puncak: " + stack.peek());
                    }
                    break;
                case 4:
                    stack.tampilkan();
                    System.out.println("Jumlah elemen: " + stack.size());
                    break;
                case 5:
                    System.out.print("Masukkan kata: ");
                    String kata = input.next();
                    System.out.println("Hasil dibalik: " + balikKata(kata));
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