/*
 * Program   : SimpleStack
 * Deskripsi : Implementasi struktur data Stack menggunakan array.
 *             Dilengkapi pengecekan stack penuh, operasi peek, ukuran
 *             stack, menampilkan isi stack, contoh penerapan membalik
 *             kata, input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner;

public class SimpleStack {

    // Inner static class wrapper untuk merepresentasikan elemen entri stack
    public static class StackEntry {
        private int value;

        public StackEntry(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    // Inner static class wrapper untuk penyimpanan array internal stack
    public static class StorageBuffer {
        private StackEntry[] buffer;

        public StorageBuffer(int capacity) {
            this.buffer = new StackEntry[capacity];
        }

        public int capacity() {
            return buffer.length;
        }

        public void store(int index, StackEntry entry) {
            buffer[index] = entry;
        }

        public StackEntry retrieve(int index) {
            return buffer[index];
        }
    }

    private StorageBuffer storage;
    private int top;

    // Konstruktor: membuat stack dengan kapasitas tertentu
    public SimpleStack(int capacity) {
        storage = new StorageBuffer(capacity);
        top = -1;
    }

    // Menambah elemen ke puncak stack
    public void push(int item) {
        storage.store(++top, new StackEntry(item));
    }

    // Mengambil dan menghapus elemen dari puncak stack
    public int pop() {
        return storage.retrieve(top--).getValue();
    }

    // Mengecek apakah stack kosong
    public boolean isEmpty() {
        return top == -1;
    }

    // Mengecek apakah stack sudah penuh
    public boolean isFull() {
        return top == storage.capacity() - 1;
    }

    // Melihat elemen puncak tanpa menghapusnya
    public int peek() {
        return storage.retrieve(top).getValue();
    }

    // Mengembalikan jumlah elemen di dalam stack
    public int size() {
        return top + 1;
    }

    // Menampilkan isi stack dari puncak ke dasar
    public void tampilkan() {
        if (isEmpty()) {
            System.out.println("Stack kosong.");
            return;
        }
        System.out.println("Isi stack (puncak -> dasar):");
        for (int i = top; i >= 0; i--) {
            System.out.println("| " + storage.retrieve(i).getValue() + " |");
        }
        System.out.println("-----");
    }

    // Contoh penerapan: membalik sebuah kata dengan stack
    // (karakter disimpan sebagai kode angka)
    public static String balikKata(String kata) {
        SimpleStack tumpukan = new SimpleStack(kata.length());
        for (int i = 0; i < kata.length(); i++) {
            tumpukan.push(kata.charAt(i));
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

        int pilihan = 0;
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