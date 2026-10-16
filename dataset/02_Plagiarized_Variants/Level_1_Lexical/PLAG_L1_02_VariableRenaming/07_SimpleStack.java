/*
 * Program   : SimpleStack
 * Deskripsi : Implementasi struktur data Stack menggunakan array.
 *             Dilengkapi pengecekan stack penuh, operasi peek, ukuran
 *             stack, menampilkan isi stack, contoh penerapan membalik
 *             kata, input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner;

public class SimpleStack {

    private int[] wadah;
    private int puncak;

    // Konstruktor: membuat stack dengan kapasitas tertentu
    public SimpleStack(int ukuran) {
        wadah = new int[ukuran];
        puncak = -1;
    }

    // Menambah elemen ke puncak stack
    public void masukkan(int nilai) {
        wadah[++puncak] = nilai;
    }

    // Mengambil dan menghapus elemen dari puncak stack
    public int keluarkan() {
        return wadah[puncak--];
    }

    // Mengecek apakah stack kosong
    public boolean kosong() {
        return puncak == -1;
    }

    // Mengecek apakah stack sudah penuh
    public boolean penuh() {
        return puncak == wadah.length - 1;
    }

    // Melihat elemen puncak tanpa menghapusnya
    public int lihatPuncak() {
        return wadah[puncak];
    }

    // Mengembalikan jumlah elemen di dalam stack
    public int banyakElemen() {
        return puncak + 1;
    }

    // Menampilkan isi stack dari puncak ke dasar
    public void cetak() {
        if (kosong()) {
            System.out.println("Stack kosong.");
            return;
        }
        System.out.println("Isi stack (puncak -> dasar):");
        for (int idx = puncak; idx >= 0; idx--) {
            System.out.println("| " + wadah[idx] + " |");
        }
        System.out.println("-----");
    }

    // Contoh penerapan: membalik sebuah kata dengan stack
    // (karakter disimpan sebagai kode angka)
    public static String balikKata(String teks) {
        SimpleStack tumpukan = new SimpleStack(teks.length());
        for (int idx = 0; idx < teks.length(); idx++) {
            tumpukan.masukkan(teks.charAt(idx));
        }
        String balik = "";
        while (!tumpukan.kosong()) {
            balik += (char) tumpukan.keluarkan();
        }
        return balik;
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

        // Membaca kapasitas stack
        int maks = 0;
        while (maks <= 0) {
            maks = ambilAngka(scanner, "Masukkan kapasitas stack: ");
            if (maks <= 0) {
                System.out.println("Kapasitas harus lebih dari 0!");
            }
        }
        SimpleStack tumpukan = new SimpleStack(maks);

        int menu;
        do {
            System.out.println("\n===== PROGRAM STACK =====");
            System.out.println("1. Push (tambah elemen)");
            System.out.println("2. Pop (ambil elemen)");
            System.out.println("3. Peek (lihat elemen puncak)");
            System.out.println("4. Tampilkan isi stack");
            System.out.println("5. Balik kata menggunakan stack");
            System.out.println("0. Keluar");
            menu = ambilAngka(scanner, "Pilih menu: ");

            switch (menu) {
                case 1:
                    if (tumpukan.penuh()) {
                        System.out.println("Stack penuh, tidak bisa push!");
                    } else {
                        int angka = ambilAngka(scanner, "Masukkan nilai: ");
                        tumpukan.masukkan(angka);
                        System.out.println(angka + " berhasil ditambahkan.");
                    }
                    break;
                case 2:
                    if (tumpukan.kosong()) {
                        System.out.println("Stack kosong, tidak bisa pop!");
                    } else {
                        System.out.println("Elemen yang diambil: " + tumpukan.keluarkan());
                    }
                    break;
                case 3:
                    if (tumpukan.kosong()) {
                        System.out.println("Stack kosong!");
                    } else {
                        System.out.println("Elemen puncak: " + tumpukan.lihatPuncak());
                    }
                    break;
                case 4:
                    tumpukan.cetak();
                    System.out.println("Jumlah elemen: " + tumpukan.banyakElemen());
                    break;
                case 5:
                    System.out.print("Masukkan kata: ");
                    String kata = scanner.next();
                    System.out.println("Hasil dibalik: " + balikKata(kata));
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