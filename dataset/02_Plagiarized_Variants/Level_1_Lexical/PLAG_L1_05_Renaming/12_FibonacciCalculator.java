/*
 * Program   : FibonacciCalculator
 * Deskripsi : Menghitung bilangan Fibonacci ke-n secara iteratif dan
 *             rekursif. Dilengkapi deret Fibonacci, jumlah deret,
 *             pengecekan bilangan Fibonacci, perbandingan kedua metode,
 *             input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner;

public class FibonacciCalculator {

    // Batas maksimal agar hasil tidak overflow pada tipe int (F(46) = 1836311903)
    static final int LIMIT_SUKU = 46;

    // Logika inti: menghitung Fibonacci ke-n secara iteratif
    public int hitungFibIter(int urutan) {
        if (urutan <= 0) return 0;
        if (urutan == 1) return 1;
        int lampau = 0, kini = 1;
        for (int k = 2; k <= urutan; k++) {
            int depan = lampau + kini;
            lampau = kini;
            kini = depan;
        }
        return kini;
    }

    // Menghitung Fibonacci ke-n secara rekursif sebagai pembanding
    public int hitungFibRek(int urutan) {
        if (urutan <= 0) return 0;
        if (urutan == 1) return 1;
        return hitungFibRek(urutan - 1) + hitungFibRek(urutan - 2);
    }

    // Menampilkan deret Fibonacci dari suku ke-0 sampai ke-n
    public void cetakRangkaian(int urutan) {
        System.out.print("Deret Fibonacci : ");
        for (int k = 0; k <= urutan; k++) {
            System.out.print(hitungFibIter(k));
            if (k < urutan) {
                System.out.print(", ");
            }
        }
        System.out.print(System.lineSeparator());
    }

    // Menghitung jumlah semua suku dari ke-0 sampai ke-n (pakai long agar aman)
    public long akumulasiRangkaian(int urutan) {
        long keseluruhan = 0;
        for (int k = 0; k <= urutan; k++) {
            keseluruhan += hitungFibIter(k);
        }
        return keseluruhan;
    }

    // Mengecek apakah sebuah bilangan termasuk bilangan Fibonacci
    public boolean termasukFib(int bilangan) {
        if (bilangan < 0) {
            return false;
        }
        int k = 0;
        int isi = hitungFibIter(k);
        while (isi < bilangan && k < LIMIT_SUKU) {
            k++;
            isi = hitungFibIter(k);
        }
        return isi == bilangan;
    }

    // Membandingkan hasil iteratif dan rekursif
    public void adukMetode(int urutan) {
        int hslIter = hitungFibIter(urutan);
        int hslRek = hitungFibRek(urutan);

        System.out.print(new StringBuilder().append("Hasil iteratif : ").append(hslIter).toString());
        System.out.print(System.lineSeparator());
        System.out.print(new StringBuilder().append("Hasil rekursif : ").append(hslRek).toString());
        System.out.print(System.lineSeparator());

        if (hslIter == hslRek) {
            System.out.print("Kedua metode menghasilkan nilai yang sama.");
            System.out.print(System.lineSeparator());
        } else {
            System.out.print("Hasil berbeda, ada yang salah!");
            System.out.print(System.lineSeparator());
        }
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

    // Membaca suku ke-n dengan validasi rentang
    public static int ambilSuku(Scanner pemindai) {
        int urutan = -1;
        while (urutan < 0 || urutan > LIMIT_SUKU) {
            urutan = ambilInteger(pemindai, new StringBuilder().append("Masukkan n (0-").append(LIMIT_SUKU).append("): ").toString());
            if (urutan < 0 || urutan > LIMIT_SUKU) {
                System.out.print("Nilai n di luar jangkauan, coba lagi!");
                System.out.print(System.lineSeparator());
            }
        }
        return urutan;
    }

    public static void main(String[] args) {
        Scanner pemindai = new Scanner(System.in);
        FibonacciCalculator pengolah = new FibonacciCalculator();

        int opsi;
        do {
            System.out.print("\n===== PROGRAM FIBONACCI =====");
            System.out.print(System.lineSeparator());
            System.out.print("1. Hitung Fibonacci ke-n");
            System.out.print(System.lineSeparator());
            System.out.print("2. Tampilkan deret Fibonacci");
            System.out.print(System.lineSeparator());
            System.out.print("3. Jumlah deret Fibonacci");
            System.out.print(System.lineSeparator());
            System.out.print("4. Cek apakah bilangan Fibonacci");
            System.out.print(System.lineSeparator());
            System.out.print("5. Bandingkan iteratif vs rekursif");
            System.out.print(System.lineSeparator());
            System.out.print("0. Keluar");
            System.out.print(System.lineSeparator());
            opsi = ambilInteger(pemindai, "Pilih menu: ");

            switch (opsi) {
                case 1:
                    int u1 = ambilSuku(pemindai);
                    System.out.print(new StringBuilder().append("F(").append(u1).append(") = ").append(pengolah.hitungFibIter(u1)).toString());
                    System.out.print(System.lineSeparator());
                    break;
                case 2:
                    int u2 = ambilSuku(pemindai);
                    pengolah.cetakRangkaian(u2);
                    break;
                case 3:
                    int u3 = ambilSuku(pemindai);
                    System.out.print(new StringBuilder().append("Jumlah suku ke-0 s.d. ke-").append(u3).append(" = ")
                            .append(pengolah.akumulasiRangkaian(u3)).toString());
                    System.out.print(System.lineSeparator());
                    break;
                case 4:
                    int bilUji = ambilInteger(pemindai, "Masukkan bilangan: ");
                    if (pengolah.termasukFib(bilUji)) {
                        System.out.print(new StringBuilder().append(bilUji).append(" adalah bilangan Fibonacci.").toString());
                        System.out.print(System.lineSeparator());
                    } else {
                        System.out.print(new StringBuilder().append(bilUji).append(" bukan bilangan Fibonacci.").toString());
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 5:
                    // Batasi n agar rekursif tidak terlalu lama
                    int u5 = -1;
                    while (u5 < 0 || u5 > 35) {
                        u5 = ambilInteger(pemindai, "Masukkan n (0-35): ");
                        if (u5 < 0 || u5 > 35) {
                            System.out.print("Nilai n di luar jangkauan, coba lagi!");
                            System.out.print(System.lineSeparator());
                        }
                    }
                    pengolah.adukMetode(u5);
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