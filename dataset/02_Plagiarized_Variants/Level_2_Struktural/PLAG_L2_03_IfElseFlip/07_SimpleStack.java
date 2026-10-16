/*
 * Program   : SimpleStack
 * Deskripsi : Implementasi struktur data Stack menggunakan array.
 *             Dilengkapi pengecekan stack penuh, operasi peek, ukuran
 *             stack, menampilkan isi stack, contoh penerapan membalik
 *             kata, input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner;

public class SimpleStack {

    private int[] elements;
    private int top;

    // Konstruktor: membuat stack dengan kapasitas tertentu
    public SimpleStack(int capacity) {
        elements = new int[capacity];
        top = -1;
    }

    // Menambah elemen ke puncak stack
    public void push(int item) {
        elements[++top] = item;
    }

    // Mengambil dan menghapus elemen dari puncak stack
    public int pop() {
        return elements[top--];
    }

    // Mengecek apakah stack kosong
    public boolean isEmpty() {
        return -1 == top;
    }

    // Mengecek apakah stack sudah penuh
    public boolean isFull() {
        return elements.length - 1 == top;
    }

    // Melihat elemen puncak tanpa menghapusnya
    public int peek() {
        return elements[top];
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
            System.out.println("| " + elements[i] + " |");
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
            if (0 >= kapasitas) {
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
                    if (!stack.isFull()) {
                        int nilai = bacaAngka(input, "Masukkan nilai: ");
                        stack.push(nilai);
                        System.out.println(nilai + " berhasil ditambahkan.");
                    } else {
                        System.out.println("Stack penuh, tidak bisa push!");
                    }
                    break;
                case 2:
                    if (!stack.isEmpty()) {
                        System.out.println("Elemen yang diambil: " + stack.pop());
                    } else {
                        System.out.println("Stack kosong, tidak bisa pop!");
                    }
                    break;
                case 3:
                    if (!stack.isEmpty()) {
                        System.out.println("Elemen puncak: " + stack.peek());
                    } else {
                        System.out.println("Stack kosong!");
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