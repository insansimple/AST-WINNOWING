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
    static final double LAJU_PPN = 0.11;

    // Logika inti: menghitung total akhir setelah diskon member
    public double jumlahAkhir(double[] daftarHarga, int[] daftarQty, double potonganMember) {
        double jmlAwal = 0;
        for (int k = 0; k < daftarHarga.length; k++) {
            jmlAwal += daftarHarga[k] * daftarQty[k];
        }
        double pengurang = jmlAwal * potonganMember;
        double hasilAkhir = jmlAwal - pengurang;
        return hasilAkhir;
    }

    // Menghitung subtotal sebelum diskon
    public double jumlahAwal(double[] daftarHarga, int[] daftarQty) {
        double jmlAwal = 0;
        for (int k = 0; k < daftarHarga.length; k++) {
            jmlAwal += daftarHarga[k] * daftarQty[k];
        }
        return jmlAwal;
    }

    // Menghitung pajak dari sebuah nilai
    public double taksirPajak(double angka) {
        return angka * LAJU_PPN;
    }

    // Menghitung kembalian, bernilai negatif jika uang kurang
    public double taksirKembalian(double tagihan, double uangMasuk) {
        return uangMasuk - tagihan;
    }

    // Mencetak struk belanja
    public void tulisStruk(String[] daftarNama, double[] daftarHarga, int[] daftarQty, double potonganMember) {
        double jmlAwal = jumlahAwal(daftarHarga, daftarQty);
        double sesudahDiskon = jumlahAkhir(daftarHarga, daftarQty, potonganMember);
        double pengurang = jmlAwal - sesudahDiskon;
        double nilaiPajak = taksirPajak(sesudahDiskon);
        double tagihan = sesudahDiskon + nilaiPajak;

        System.out.print("\n========== STRUK BELANJA ==========");
        System.out.print(System.lineSeparator());
        for (int k = 0; k < daftarNama.length; k++) {
            System.out.printf("%-12s %3d x %10.0f = %11.0f%n",
                    daftarNama[k], daftarQty[k], daftarHarga[k], daftarHarga[k] * daftarQty[k]);
        }
        System.out.print("-----------------------------------");
        System.out.print(System.lineSeparator());
        System.out.printf("Subtotal        : %14.0f%n", jmlAwal);
        System.out.printf("Diskon member   : %14.0f%n", pengurang);
        System.out.printf("PPN (11%%)       : %14.0f%n", nilaiPajak);
        System.out.printf("Total bayar     : %14.0f%n", tagihan);
        System.out.print("===================================");
        System.out.print(System.lineSeparator());
    }

    // Membaca bilangan bulat dengan validasi sederhana
    public static int ambilInteger(Scanner pemindai, String keterangan) {
        System.out.print(keterangan);
        while (!pemindai.hasNextInt()) {
            System.out.print("Input harus angka bulat, ulangi: ");
            pemindai.next();
        }
        int hasilBaca = pemindai.nextInt();
        pemindai.nextLine(); // buang sisa baris
        return hasilBaca;
    }

    // Membaca bilangan desimal positif (lebih dari 0)
    public static double ambilNominal(Scanner pemindai, String keterangan) {
        double angka = 0;
        while (angka <= 0) {
            System.out.print(keterangan);
            while (!pemindai.hasNextDouble()) {
                System.out.print("Input harus angka, ulangi: ");
                pemindai.next();
            }
            angka = pemindai.nextDouble();
            pemindai.nextLine(); // buang sisa baris
            if (angka <= 0) {
                System.out.print("Nilai harus lebih dari 0!");
                System.out.print(System.lineSeparator());
            }
        }
        return angka;
    }

    public static void main(String[] args) {
        Scanner pemindai = new Scanner(System.in);
        KasirToko petugas = new KasirToko();

        // Membaca jumlah jenis barang
        int banyakItem = 0;
        while (banyakItem <= 0) {
            banyakItem = ambilInteger(pemindai, "Jumlah jenis barang: ");
            if (banyakItem <= 0) {
                System.out.print("Jumlah barang harus lebih dari 0!");
                System.out.print(System.lineSeparator());
            }
        }

        String[] namaItem = new String[banyakItem];
        double[] hargaItem = new double[banyakItem];
        int[] kuantitas = new int[banyakItem];

        // Mengisi data setiap barang
        for (int k = 0; k < banyakItem; k++) {
            System.out.print(new StringBuilder().append("\nBarang ke-").append(k + 1).toString());
            System.out.print(System.lineSeparator());
            System.out.print("Nama barang : ");
            namaItem[k] = pemindai.nextLine();
            hargaItem[k] = ambilNominal(pemindai, "Harga satuan: ");
            kuantitas[k] = 0;
            while (kuantitas[k] <= 0) {
                kuantitas[k] = ambilInteger(pemindai, "Jumlah      : ");
                if (kuantitas[k] <= 0) {
                    System.out.print("Jumlah harus lebih dari 0!");
                    System.out.print(System.lineSeparator());
                }
            }
        }

        // Menentukan diskon member
        int statusMember = ambilInteger(pemindai, "\nApakah member? (1 = ya, 0 = tidak): ");
        double persenDiskon = 0;
        if (statusMember == 1) {
            persenDiskon = 0.10; // diskon member 10 persen
            System.out.print("Diskon member 10% diterapkan.");
            System.out.print(System.lineSeparator());
        }

        // Cetak struk
        petugas.tulisStruk(namaItem, hargaItem, kuantitas, persenDiskon);

        // Proses pembayaran
        double tagihan = petugas.jumlahAkhir(hargaItem, kuantitas, persenDiskon);
        tagihan = tagihan + petugas.taksirPajak(tagihan);

        double uangBayar = 0;
        while (uangBayar < tagihan) {
            uangBayar = ambilNominal(pemindai, "Uang diterima : ");
            if (uangBayar < tagihan) {
                System.out.printf("Uang kurang! Total bayar %.0f%n", tagihan);
            }
        }
        System.out.printf("Kembalian     : %.0f%n", petugas.taksirKembalian(tagihan, uangBayar));
        System.out.print("Terima kasih sudah berbelanja!");
        System.out.print(System.lineSeparator());

        pemindai.close();
    }
}