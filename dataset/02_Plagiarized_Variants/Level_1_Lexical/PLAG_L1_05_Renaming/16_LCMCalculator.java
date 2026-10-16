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
    public int cariKpkBertahap(int bil1, int bil2) {
        int kandidat = Math.max(bil1, bil2);
        while (true) {
            if (kandidat % bil1 == 0 && kandidat % bil2 == 0) {
                return kandidat;
            }
            kandidat++;
        }
    }

    // Menghitung FPB dengan algoritma Euclid (dipakai untuk cara cepat)
    public int cariFpb(int p, int q) {
        while (q != 0) {
            int sementara = q;
            q = p % q;
            p = sementara;
        }
        return p;
    }

    // Cara cepat: KPK = (a x b) / FPB (pakai long agar tidak overflow)
    public long cariKpkCepat(int p, int q) {
        return (long) p / cariFpb(p, q) * q;
    }

    // Menghitung KPK dari tiga bilangan
    public long cariKpkTiga(int p, int q, int r) {
        long kpkPQ = cariKpkCepat(p, q);
        // KPK(ab, c) = (kpkAB x c) / FPB(kpkAB, c)
        long u = kpkPQ;
        long w = r;
        while (w != 0) {
            long sementara = w;
            w = u % w;
            u = sementara;
        }
        return kpkPQ / u * r;
    }

    // Menampilkan beberapa kelipatan pertama dari sebuah bilangan
    public void cetakKelipatan(int bilangan, int banyak) {
        System.out.print(new StringBuilder().append("Kelipatan ").append(bilangan).append(" : ").toString());
        for (int k = 1; k <= banyak; k++) {
            System.out.print(new StringBuilder().append((long) bilangan * k).append(" ").toString());
        }
        System.out.print(System.lineSeparator());
    }

    // Membandingkan hasil cara lambat dan cara cepat
    public void adukCara(int p, int q) {
        int hslBertahap = cariKpkBertahap(p, q);
        long hslCepat = cariKpkCepat(p, q);

        System.out.print(new StringBuilder().append("Hasil cara bertahap : ").append(hslBertahap).toString());
        System.out.print(System.lineSeparator());
        System.out.print(new StringBuilder().append("Hasil cara FPB      : ").append(hslCepat).toString());
        System.out.print(System.lineSeparator());

        if (hslBertahap == hslCepat) {
            System.out.print("Kedua cara menghasilkan nilai yang sama.");
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

    // Membaca bilangan bulat positif (lebih dari 0)
    public static int ambilPositif(Scanner pemindai, String keterangan) {
        int angka = 0;
        while (angka <= 0) {
            angka = ambilInteger(pemindai, keterangan);
            if (angka <= 0) {
                System.out.print("Bilangan harus lebih dari 0!");
                System.out.print(System.lineSeparator());
            }
        }
        return angka;
    }

    public static void main(String[] args) {
        Scanner pemindai = new Scanner(System.in);
        LCMCalculator pengolah = new LCMCalculator();

        int opsi;
        do {
            System.out.print("\n===== PROGRAM KPK =====");
            System.out.print(System.lineSeparator());
            System.out.print("1. Hitung KPK dua bilangan");
            System.out.print(System.lineSeparator());
            System.out.print("2. Hitung KPK tiga bilangan");
            System.out.print(System.lineSeparator());
            System.out.print("3. Tampilkan kelipatan sebuah bilangan");
            System.out.print(System.lineSeparator());
            System.out.print("4. Bandingkan dua cara menghitung KPK");
            System.out.print(System.lineSeparator());
            System.out.print("0. Keluar");
            System.out.print(System.lineSeparator());
            opsi = ambilInteger(pemindai, "Pilih menu: ");

            switch (opsi) {
                case 1:
                    int m1 = ambilPositif(pemindai, "Bilangan pertama : ");
                    int n1 = ambilPositif(pemindai, "Bilangan kedua   : ");
                    System.out.print(new StringBuilder().append("KPK(").append(m1).append(", ").append(n1).append(") = ")
                            .append(pengolah.cariKpkBertahap(m1, n1)).toString());
                    System.out.print(System.lineSeparator());
                    break;
                case 2:
                    int m2 = ambilPositif(pemindai, "Bilangan pertama : ");
                    int n2 = ambilPositif(pemindai, "Bilangan kedua   : ");
                    int o2 = ambilPositif(pemindai, "Bilangan ketiga  : ");
                    System.out.print(new StringBuilder().append("KPK(").append(m2).append(", ").append(n2).append(", ").append(o2).append(") = ")
                            .append(pengolah.cariKpkTiga(m2, n2, o2)).toString());
                    System.out.print(System.lineSeparator());
                    break;
                case 3:
                    int angkaDasar = ambilPositif(pemindai, "Masukkan bilangan : ");
                    int banyakKali = ambilPositif(pemindai, "Jumlah kelipatan : ");
                    pengolah.cetakKelipatan(angkaDasar, banyakKali);
                    break;
                case 4:
                    int m4 = ambilPositif(pemindai, "Bilangan pertama : ");
                    int n4 = ambilPositif(pemindai, "Bilangan kedua   : ");
                    pengolah.adukCara(m4, n4);
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