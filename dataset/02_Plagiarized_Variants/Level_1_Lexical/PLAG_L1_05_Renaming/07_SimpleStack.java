/*
 * Program   : SimpleStack
 * Deskripsi : Implementasi struktur data Stack menggunakan array.
 *             Dilengkapi pengecekan stack penuh, operasi peek, ukuran
 *             stack, menampilkan isi stack, contoh penerapan membalik
 *             kata, input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner;

public class SimpleStack {

    private int[] isiLarik;
    private int posisiAtas;

    // Konstruktor: membuat stack dengan kapasitas tertentu
    public SimpleStack(int daya) {
        isiLarik = new int[daya];
        posisiAtas = -1;
    }

    // Menambah elemen ke puncak stack
    public void dorong(int butir) {
        isiLarik[++posisiAtas] = butir;
    }

    // Mengambil dan menghapus elemen dari puncak stack
    public int ambilAtas() {
        return isiLarik[posisiAtas--];
    }

    // Mengecek apakah stack kosong
    public boolean kosongkah() {
        return posisiAtas == -1;
    }

    // Mengecek apakah stack sudah penuh
    public boolean penuhkah() {
        return posisiAtas == isiLarik.length - 1;
    }

    // Melihat elemen puncak tanpa menghapusnya
    public int intip() {
        return isiLarik[posisiAtas];
    }

    // Mengembalikan jumlah elemen di dalam stack
    public int cacah() {
        return posisiAtas + 1;
    }

    // Menampilkan isi stack dari puncak ke dasar
    public void cetakIsi() {
        if (kosongkah()) {
            System.out.print("Stack kosong.");
            System.out.print(System.lineSeparator());
            return;
        }
        System.out.print("Isi stack (puncak -> dasar):");
        System.out.print(System.lineSeparator());
        for (int k = posisiAtas; k >= 0; k--) {
            System.out.print(new StringBuilder().append("| ").append(isiLarik[k]).append(" |").toString());
            System.out.print(System.lineSeparator());
        }
        System.out.print("-----");
        System.out.print(System.lineSeparator());
    }

    // Contoh penerapan: membalik sebuah kata dengan stack
    // (karakter disimpan sebagai kode angka)
    public static String bolakBalik(String teks) {
        SimpleStack timbunan = new SimpleStack(teks.length());
        for (int k = 0; k < teks.length(); k++) {
            timbunan.dorong(teks.charAt(k));
        }
        StringBuilder penampung = new StringBuilder();
        while (!timbunan.kosongkah()) {
            penampung.append((char) timbunan.ambilAtas());
        }
        return penampung.toString();
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

        // Membaca kapasitas stack
        int dayaTampung = 0;
        while (dayaTampung <= 0) {
            dayaTampung = ambilInteger(pemindai, "Masukkan kapasitas stack: ");
            if (dayaTampung <= 0) {
                System.out.print("Kapasitas harus lebih dari 0!");
                System.out.print(System.lineSeparator());
            }
        }
        SimpleStack timbunanUtama = new SimpleStack(dayaTampung);

        int opsi;
        do {
            System.out.print("\n===== PROGRAM STACK =====");
            System.out.print(System.lineSeparator());
            System.out.print("1. Push (tambah elemen)");
            System.out.print(System.lineSeparator());
            System.out.print("2. Pop (ambil elemen)");
            System.out.print(System.lineSeparator());
            System.out.print("3. Peek (lihat elemen puncak)");
            System.out.print(System.lineSeparator());
            System.out.print("4. Tampilkan isi stack");
            System.out.print(System.lineSeparator());
            System.out.print("5. Balik kata menggunakan stack");
            System.out.print(System.lineSeparator());
            System.out.print("0. Keluar");
            System.out.print(System.lineSeparator());
            opsi = ambilInteger(pemindai, "Pilih menu: ");

            switch (opsi) {
                case 1:
                    if (timbunanUtama.penuhkah()) {
                        System.out.print("Stack penuh, tidak bisa push!");
                        System.out.print(System.lineSeparator());
                    } else {
                        int angkaMasuk = ambilInteger(pemindai, "Masukkan nilai: ");
                        timbunanUtama.dorong(angkaMasuk);
                        System.out.print(new StringBuilder().append(angkaMasuk).append(" berhasil ditambahkan.").toString());
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 2:
                    if (timbunanUtama.kosongkah()) {
                        System.out.print("Stack kosong, tidak bisa pop!");
                        System.out.print(System.lineSeparator());
                    } else {
                        System.out.print(new StringBuilder().append("Elemen yang diambil: ").append(timbunanUtama.ambilAtas()).toString());
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 3:
                    if (timbunanUtama.kosongkah()) {
                        System.out.print("Stack kosong!");
                        System.out.print(System.lineSeparator());
                    } else {
                        System.out.print(new StringBuilder().append("Elemen puncak: ").append(timbunanUtama.intip()).toString());
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 4:
                    timbunanUtama.cetakIsi();
                    System.out.print(new StringBuilder().append("Jumlah elemen: ").append(timbunanUtama.cacah()).toString());
                    System.out.print(System.lineSeparator());
                    break;
                case 5:
                    System.out.print("Masukkan kata: ");
                    String masukanKata = pemindai.next();
                    System.out.print(new StringBuilder().append("Hasil dibalik: ").append(bolakBalik(masukanKata)).toString());
                    System.out.print(System.lineSeparator());
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