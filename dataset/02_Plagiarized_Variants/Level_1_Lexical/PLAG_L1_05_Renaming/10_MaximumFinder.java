/*
 * Program   : MaximumFinder
 * Deskripsi : Mencari nilai terbesar dari tiga bilangan. Dilengkapi
 *             pencarian nilai terkecil, pencarian nilai terbesar dari
 *             array, rata-rata tiga bilangan, pengurutan tiga bilangan,
 *             input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner;

public class MaximumFinder {

    // Logika inti: mencari nilai terbesar dari tiga bilangan
    public int tentukanTerbesar(int p, int q, int r) {
        int puncak = p;
        if (q > puncak) {
            puncak = q;
        }
        if (r > puncak) {
            puncak = r;
        }
        return puncak;
    }

    // Mencari nilai terkecil dari tiga bilangan
    public int tentukanTerkecil(int p, int q, int r) {
        int dasar = p;
        if (q < dasar) {
            dasar = q;
        }
        if (r < dasar) {
            dasar = r;
        }
        return dasar;
    }

    // Mencari nilai terbesar dari sebuah array
    public int terbesarDalamLarik(int[] kumpulan) {
        int puncak = kumpulan[0];
        for (int k = 1; k < kumpulan.length; k++) {
            if (kumpulan[k] > puncak) {
                puncak = kumpulan[k];
            }
        }
        return puncak;
    }

    // Menghitung rata-rata dari tiga bilangan
    public double reratakan(int p, int q, int r) {
        return (p + q + r) / 3.0;
    }

    // Menampilkan tiga bilangan dari yang terkecil ke terbesar
    public void cetakUrutan(int p, int q, int r) {
        int paling = tentukanTerbesar(p, q, r);
        int terendah = tentukanTerkecil(p, q, r);
        // Nilai tengah = jumlah semua dikurangi terbesar dan terkecil
        int median = p + q + r - paling - terendah;
        System.out.print(new StringBuilder().append("Urutan naik  : ").append(terendah).append(", ").append(median).append(", ").append(paling).toString());
        System.out.print(System.lineSeparator());
        System.out.print(new StringBuilder().append("Urutan turun : ").append(paling).append(", ").append(median).append(", ").append(terendah).toString());
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
        MaximumFinder penentu = new MaximumFinder();

        int opsi;
        do {
            System.out.print("\n===== PROGRAM NILAI TERBESAR =====");
            System.out.print(System.lineSeparator());
            System.out.print("1. Cari terbesar dari tiga bilangan");
            System.out.print(System.lineSeparator());
            System.out.print("2. Cari terkecil dari tiga bilangan");
            System.out.print(System.lineSeparator());
            System.out.print("3. Hitung rata-rata tiga bilangan");
            System.out.print(System.lineSeparator());
            System.out.print("4. Urutkan tiga bilangan");
            System.out.print(System.lineSeparator());
            System.out.print("5. Cari terbesar dari sejumlah data");
            System.out.print(System.lineSeparator());
            System.out.print("0. Keluar");
            System.out.print(System.lineSeparator());
            opsi = ambilInteger(pemindai, "Pilih menu: ");

            switch (opsi) {
                case 1:
                    int m1 = ambilInteger(pemindai, "Bilangan pertama : ");
                    int n1 = ambilInteger(pemindai, "Bilangan kedua   : ");
                    int o1 = ambilInteger(pemindai, "Bilangan ketiga  : ");
                    System.out.print(new StringBuilder().append("Nilai terbesar : ").append(penentu.tentukanTerbesar(m1, n1, o1)).toString());
                    System.out.print(System.lineSeparator());
                    break;
                case 2:
                    int m2 = ambilInteger(pemindai, "Bilangan pertama : ");
                    int n2 = ambilInteger(pemindai, "Bilangan kedua   : ");
                    int o2 = ambilInteger(pemindai, "Bilangan ketiga  : ");
                    System.out.print(new StringBuilder().append("Nilai terkecil : ").append(penentu.tentukanTerkecil(m2, n2, o2)).toString());
                    System.out.print(System.lineSeparator());
                    break;
                case 3:
                    int m3 = ambilInteger(pemindai, "Bilangan pertama : ");
                    int n3 = ambilInteger(pemindai, "Bilangan kedua   : ");
                    int o3 = ambilInteger(pemindai, "Bilangan ketiga  : ");
                    System.out.print(new StringBuilder().append("Rata-rata : ").append(penentu.reratakan(m3, n3, o3)).toString());
                    System.out.print(System.lineSeparator());
                    break;
                case 4:
                    int m4 = ambilInteger(pemindai, "Bilangan pertama : ");
                    int n4 = ambilInteger(pemindai, "Bilangan kedua   : ");
                    int o4 = ambilInteger(pemindai, "Bilangan ketiga  : ");
                    penentu.cetakUrutan(m4, n4, o4);
                    break;
                case 5:
                    int banyak = ambilInteger(pemindai, "Jumlah data : ");
                    if (banyak <= 0) {
                        System.out.print("Jumlah data harus lebih dari 0!");
                        System.out.print(System.lineSeparator());
                    } else {
                        int[] himpunan = new int[banyak];
                        for (int k = 0; k < banyak; k++) {
                            himpunan[k] = ambilInteger(pemindai, new StringBuilder().append("Data ke-").append(k + 1).append(" : ").toString());
                        }
                        System.out.print(new StringBuilder().append("Nilai terbesar : ").append(penentu.terbesarDalamLarik(himpunan)).toString());
                        System.out.print(System.lineSeparator());
                    }
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