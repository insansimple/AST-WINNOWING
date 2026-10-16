/*
 * Program   : PrimeChecker
 * Deskripsi : Mengecek apakah sebuah bilangan merupakan bilangan prima.
 *             Dilengkapi daftar bilangan prima dalam rentang tertentu,
 *             penghitungan jumlah prima, pencarian prima berikutnya,
 *             faktorisasi prima, input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner;

public class PrimeChecker {

    // Logika inti: mengecek bilangan prima sampai akar dari n
    public boolean apakahPrima(int bil) {
        if (bil <= 1) return false;
        for (int d = 2; d * d <= bil; d++) {
            if (bil % d == 0) {
                return false;
            }
        }
        return true;
    }

    // Menampilkan semua bilangan prima dari awal sampai akhir
    public int cetakDaftarPrima(int mulai, int selesai) {
        int total = 0;
        System.out.print(new StringBuilder().append("Bilangan prima ").append(mulai).append(" s.d. ").append(selesai).append(" : ").toString());
        for (int d = mulai; d <= selesai; d++) {
            if (apakahPrima(d)) {
                System.out.print(new StringBuilder().append(d).append(" ").toString());
                total++;
            }
        }
        if (total == 0) {
            System.out.print("(tidak ada)");
        }
        System.out.print(System.lineSeparator());
        return total;
    }

    // Mencari bilangan prima pertama yang lebih besar dari n
    public int cariPrimaLanjut(int bil) {
        int calon = bil + 1;
        while (!apakahPrima(calon)) {
            calon++;
        }
        return calon;
    }

    // Menampilkan faktorisasi prima dari sebuah bilangan
    public void uraikanPrima(int bil) {
        if (bil < 2) {
            System.out.print("Faktorisasi prima hanya untuk bilangan >= 2.");
            System.out.print(System.lineSeparator());
            return;
        }
        System.out.print(new StringBuilder().append(bil).append(" = ").toString());
        int tersisa = bil;
        boolean awalan = true;
        for (int d = 2; d * d <= tersisa; d++) {
            while (tersisa % d == 0) {
                if (!awalan) {
                    System.out.print(" x ");
                }
                System.out.print(d);
                awalan = false;
                tersisa = tersisa / d;
            }
        }
        // Jika masih ada sisa, berarti sisa itu sendiri bilangan prima
        if (tersisa > 1) {
            if (!awalan) {
                System.out.print(" x ");
            }
            System.out.print(tersisa);
        }
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
        PrimeChecker penguji = new PrimeChecker();

        int opsi;
        do {
            System.out.print("\n===== PROGRAM BILANGAN PRIMA =====");
            System.out.print(System.lineSeparator());
            System.out.print("1. Cek apakah sebuah bilangan prima");
            System.out.print(System.lineSeparator());
            System.out.print("2. Tampilkan prima dalam rentang");
            System.out.print(System.lineSeparator());
            System.out.print("3. Cari prima berikutnya");
            System.out.print(System.lineSeparator());
            System.out.print("4. Faktorisasi prima");
            System.out.print(System.lineSeparator());
            System.out.print("0. Keluar");
            System.out.print(System.lineSeparator());
            opsi = ambilInteger(pemindai, "Pilih menu: ");

            switch (opsi) {
                case 1:
                    int angkaUji = ambilInteger(pemindai, "Masukkan bilangan: ");
                    if (penguji.apakahPrima(angkaUji)) {
                        System.out.print(new StringBuilder().append(angkaUji).append(" adalah bilangan prima.").toString());
                        System.out.print(System.lineSeparator());
                    } else {
                        System.out.print(new StringBuilder().append(angkaUji).append(" bukan bilangan prima.").toString());
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 2:
                    int batasBawah = ambilInteger(pemindai, "Batas awal  : ");
                    int batasAtas = ambilInteger(pemindai, "Batas akhir : ");
                    if (batasBawah > batasAtas) {
                        System.out.print("Batas awal tidak boleh lebih besar dari batas akhir!");
                        System.out.print(System.lineSeparator());
                    } else {
                        int banyakPrima = penguji.cetakDaftarPrima(batasBawah, batasAtas);
                        System.out.print(new StringBuilder().append("Jumlah bilangan prima : ").append(banyakPrima).toString());
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 3:
                    int angkaDasar = ambilInteger(pemindai, "Masukkan bilangan: ");
                    System.out.print(new StringBuilder().append("Prima berikutnya setelah ").append(angkaDasar).append(" adalah ")
                            .append(penguji.cariPrimaLanjut(angkaDasar)).toString());
                    System.out.print(System.lineSeparator());
                    break;
                case 4:
                    int angkaUrai = ambilInteger(pemindai, "Masukkan bilangan: ");
                    penguji.uraikanPrima(angkaUrai);
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