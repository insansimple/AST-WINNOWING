/*
 * Program   : KasirToko
 * Deskripsi : Program kasir sederhana untuk menghitung total belanja.
 *             Dilengkapi input barang (nama, harga, jumlah), diskon
 *             member, perhitungan pajak, pembayaran dan kembalian,
 *             struk belanja, serta menu sederhana.
 */
import java.util.Scanner;

public class KasirToko {

    // Tarif pajak pertambahan nilai (PPN) 11 persen
    static final double PERSEN_PAJAK = 0.11;

    // Logika inti: menghitung total akhir setelah diskon member
    public double kalkulasiTotal(double[] daftarHarga, int[] jumlahBeli, double persenDiskon) {
        double jumlah = 0;
        for (int pos = 0; pos < daftarHarga.length; pos++) {
            jumlah += daftarHarga[pos] * jumlahBeli[pos];
        }
        double potongan = jumlah * persenDiskon;
        double totalAkhir = jumlah - potongan;
        return totalAkhir;
    }

    // Menghitung subtotal sebelum diskon
    public double kalkulasiSubtotal(double[] daftarHarga, int[] jumlahBeli) {
        double jumlah = 0;
        for (int pos = 0; pos < daftarHarga.length; pos++) {
            jumlah += daftarHarga[pos] * jumlahBeli[pos];
        }
        return jumlah;
    }

    // Menghitung pajak dari sebuah nilai
    public double kalkulasiPajak(double nominal) {
        return nominal * PERSEN_PAJAK;
    }

    // Menghitung kembalian, bernilai negatif jika uang kurang
    public double kalkulasiKembalian(double tagihan, double bayar) {
        return bayar - tagihan;
    }

    // Mencetak struk belanja
    public void cetakStruk(String[] namaBarang, double[] daftarHarga, int[] jumlahBeli, double persenDiskon) {
        double jumlah = kalkulasiSubtotal(daftarHarga, jumlahBeli);
        double setelahDiskon = kalkulasiTotal(daftarHarga, jumlahBeli, persenDiskon);
        double potongan = jumlah - setelahDiskon;
        double pajak = kalkulasiPajak(setelahDiskon);
        double tagihan = setelahDiskon + pajak;

        System.out.println("\n========== STRUK BELANJA ==========");
        for (int pos = 0; pos < namaBarang.length; pos++) {
            System.out.printf("%-12s %3d x %10.0f = %11.0f%n",
                    namaBarang[pos], jumlahBeli[pos], daftarHarga[pos], daftarHarga[pos] * jumlahBeli[pos]);
        }
        System.out.println("-----------------------------------");
        System.out.printf("Subtotal        : %14.0f%n", jumlah);
        System.out.printf("Diskon member   : %14.0f%n", potongan);
        System.out.printf("PPN (11%%)       : %14.0f%n", pajak);
        System.out.printf("Total bayar     : %14.0f%n", tagihan);
        System.out.println("===================================");
    }

    // Membaca bilangan bulat dengan validasi sederhana
    public static int ambilAngka(Scanner scanner, String teks) {
        System.out.print(teks);
        while (!scanner.hasNextInt()) {
            System.out.print("Input harus angka bulat, ulangi: ");
            scanner.next();
        }
        int val = scanner.nextInt();
        scanner.nextLine(); // buang sisa baris
        return val;
    }

    // Membaca bilangan desimal positif (lebih dari 0)
    public static double ambilNominal(Scanner scanner, String teks) {
        double val = 0;
        while (val <= 0) {
            System.out.print(teks);
            while (!scanner.hasNextDouble()) {
                System.out.print("Input harus angka, ulangi: ");
                scanner.next();
            }
            val = scanner.nextDouble();
            scanner.nextLine(); // buang sisa baris
            if (val <= 0) {
                System.out.println("Nilai harus lebih dari 0!");
            }
        }
        return val;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        KasirToko mesin = new KasirToko();

        // Membaca jumlah jenis barang
        int banyakBarang = 0;
        while (banyakBarang <= 0) {
            banyakBarang = ambilAngka(scanner, "Jumlah jenis barang: ");
            if (banyakBarang <= 0) {
                System.out.println("Jumlah barang harus lebih dari 0!");
            }
        }

        String[] namaBarang = new String[banyakBarang];
        double[] daftarHarga = new double[banyakBarang];
        int[] jumlahBeli = new int[banyakBarang];

        // Mengisi data setiap barang
        for (int pos = 0; pos < banyakBarang; pos++) {
            System.out.println("\nBarang ke-" + (pos + 1));
            System.out.print("Nama barang : ");
            namaBarang[pos] = scanner.nextLine();
            daftarHarga[pos] = ambilNominal(scanner, "Harga satuan: ");
            jumlahBeli[pos] = 0;
            while (jumlahBeli[pos] <= 0) {
                jumlahBeli[pos] = ambilAngka(scanner, "Jumlah      : ");
                if (jumlahBeli[pos] <= 0) {
                    System.out.println("Jumlah harus lebih dari 0!");
                }
            }
        }

        // Menentukan diskon member
        int isMember = ambilAngka(scanner, "\nApakah member? (1 = ya, 0 = tidak): ");
        double diskon = 0;
        if (isMember == 1) {
            diskon = 0.10; // diskon member 10 persen
            System.out.println("Diskon member 10% diterapkan.");
        }

        // Cetak struk
        mesin.cetakStruk(namaBarang, daftarHarga, jumlahBeli, diskon);

        // Proses pembayaran
        double tagihan = mesin.kalkulasiTotal(daftarHarga, jumlahBeli, diskon);
        tagihan = tagihan + mesin.kalkulasiPajak(tagihan);

        double bayar = 0;
        while (bayar < tagihan) {
            bayar = ambilNominal(scanner, "Uang diterima : ");
            if (bayar < tagihan) {
                System.out.printf("Uang kurang! Total bayar %.0f%n", tagihan);
            }
        }
        System.out.printf("Kembalian     : %.0f%n", mesin.kalkulasiKembalian(tagihan, bayar));
        System.out.println("Terima kasih sudah berbelanja!");

        scanner.close();
    }
}