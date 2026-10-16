/*
 * Program   : LCMCalculator
 * Deskripsi : Menghitung KPK (Kelipatan Persekutuan Terkecil) dari dua
 *             bilangan. Dilengkapi cara cepat memakai FPB, KPK tiga
 *             bilangan, daftar kelipatan, perbandingan kedua cara,
 *             input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner;

public class LCMCalculator {

    // Logika inti: mencari KPK dengan menaikkan nilai terbesar satu per satu
    public int hitungKPK(int bil1, int bil2) {
        int kandidat = Math.max(bil1, bil2);
        while (true) {
            if (kandidat % bil1 == 0 && kandidat % bil2 == 0) {
                return kandidat;
            }
            kandidat++;
        }
    }

    // Menghitung FPB dengan algoritma Euclid (dipakai untuk cara cepat)
    public int hitungFPB(int x, int y) {
        while (y != 0) {
            int sisa = y;
            y = x % y;
            x = sisa;
        }
        return x;
    }

    // Cara cepat: KPK = (x x y) / FPB (pakai long agar tidak overflow)
    public long hitungKPKCepat(int x, int y) {
        return (long) x / hitungFPB(x, y) * y;
    }

    // Menghitung KPK dari tiga bilangan
    public long hitungKPKTiga(int x, int y, int z) {
        long kpkXY = hitungKPKCepat(x, y);
        // KPK(xy, z) = (kpkXY x z) / FPB(kpkXY, z)
        long p = kpkXY;
        long q = z;
        while (q != 0) {
            long sisa = q;
            q = p % q;
            p = sisa;
        }
        return kpkXY / p * z;
    }

    // Menampilkan beberapa kelipatan pertama dari sebuah bilangan
    public void tampilkanKelipatan(int bilangan, int banyak) {
        System.out.print("Kelipatan " + bilangan + " : ");
        for (int idx = 1; idx <= banyak; idx++) {
            System.out.print((long) bilangan * idx + " ");
        }
        System.out.println();
    }

    // Membandingkan hasil cara lambat dan cara cepat
    public void bandingkanCara(int x, int y) {
        int hasilLambat = hitungKPK(x, y);
        long hasilCepat = hitungKPKCepat(x, y);

        System.out.println("Hasil cara bertahap : " + hasilLambat);
        System.out.println("Hasil cara FPB      : " + hasilCepat);

        if (hasilLambat == hasilCepat) {
            System.out.println("Kedua cara menghasilkan nilai yang sama.");
        } else {
            System.out.println("Hasil berbeda, ada yang salah!");
        }
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

    // Membaca bilangan bulat positif (lebih dari 0)
    public static int ambilPositif(Scanner scanner, String teks) {
        int val = 0;
        while (val <= 0) {
            val = ambilAngka(scanner, teks);
            if (val <= 0) {
                System.out.println("Bilangan harus lebih dari 0!");
            }
        }
        return val;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LCMCalculator alat = new LCMCalculator();

        int menu;
        do {
            System.out.println("\n===== PROGRAM KPK =====");
            System.out.println("1. Hitung KPK dua bilangan");
            System.out.println("2. Hitung KPK tiga bilangan");
            System.out.println("3. Tampilkan kelipatan sebuah bilangan");
            System.out.println("4. Bandingkan dua cara menghitung KPK");
            System.out.println("0. Keluar");
            menu = ambilAngka(scanner, "Pilih menu: ");

            switch (menu) {
                case 1:
                    int x1 = ambilPositif(scanner, "Bilangan pertama : ");
                    int y1 = ambilPositif(scanner, "Bilangan kedua   : ");
                    System.out.println("KPK(" + x1 + ", " + y1 + ") = "
                            + alat.hitungKPK(x1, y1));
                    break;
                case 2:
                    int x2 = ambilPositif(scanner, "Bilangan pertama : ");
                    int y2 = ambilPositif(scanner, "Bilangan kedua   : ");
                    int z2 = ambilPositif(scanner, "Bilangan ketiga  : ");
                    System.out.println("KPK(" + x2 + ", " + y2 + ", " + z2 + ") = "
                            + alat.hitungKPKTiga(x2, y2, z2));
                    break;
                case 3:
                    int bilangan = ambilPositif(scanner, "Masukkan bilangan : ");
                    int banyak = ambilPositif(scanner, "Jumlah kelipatan : ");
                    alat.tampilkanKelipatan(bilangan, banyak);
                    break;
                case 4:
                    int x4 = ambilPositif(scanner, "Bilangan pertama : ");
                    int y4 = ambilPositif(scanner, "Bilangan kedua   : ");
                    alat.bandingkanCara(x4, y4);
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