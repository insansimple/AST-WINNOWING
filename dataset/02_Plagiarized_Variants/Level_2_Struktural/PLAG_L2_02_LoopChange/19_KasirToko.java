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
    static final double TARIF_PPN = 0.11;

    // Logika inti: menghitung total akhir setelah diskon member
    public double hitungTotal(double[] harga, int[] qty, double diskonMember) {
        double subtotal = 0;
        int i = 0;
        while (i < harga.length) {
            subtotal += harga[i] * qty[i];
            i++;
        }
        double potongan = subtotal * diskonMember;
        double totalAkhir = subtotal - potongan;
        return totalAkhir;
    }

    // Menghitung subtotal sebelum diskon
    public double hitungSubtotal(double[] harga, int[] qty) {
        double subtotal = 0;
        int i = 0;
        while (i < harga.length) {
            subtotal += harga[i] * qty[i];
            i++;
        }
        return subtotal;
    }

    // Menghitung pajak dari sebuah nilai
    public double hitungPajak(double nilai) {
        return nilai * TARIF_PPN;
    }

    // Menghitung kembalian, bernilai negatif jika uang kurang
    public double hitungKembalian(double totalBayar, double uangDiterima) {
        return uangDiterima - totalBayar;
    }

    // Mencetak struk belanja
    public void cetakStruk(String[] nama, double[] harga, int[] qty, double diskonMember) {
        double subtotal = hitungSubtotal(harga, qty);
        double totalSetelahDiskon = hitungTotal(harga, qty, diskonMember);
        double potongan = subtotal - totalSetelahDiskon;
        double pajak = hitungPajak(totalSetelahDiskon);
        double totalBayar = totalSetelahDiskon + pajak;

        System.out.println("\n========== STRUK BELANJA ==========");
        int i = 0;
        while (i < nama.length) {
            System.out.printf("%-12s %3d x %10.0f = %11.0f%n",
                    nama[i], qty[i], harga[i], harga[i] * qty[i]);
            i++;
        }
        System.out.println("-----------------------------------");
        System.out.printf("Subtotal        : %14.0f%n", subtotal);
        System.out.printf("Diskon member   : %14.0f%n", potongan);
        System.out.printf("PPN (11%%)       : %14.0f%n", pajak);
        System.out.printf("Total bayar     : %14.0f%n", totalBayar);
        System.out.println("===================================");
    }

    // Membaca bilangan bulat dengan validasi sederhana
    public static int bacaAngka(Scanner input, String pesan) {
        System.out.print(pesan);
        while (!input.hasNextInt()) {
            System.out.print("Input harus angka bulat, ulangi: ");
            input.next();
        }
        int nilai = input.nextInt();
        input.nextLine(); // buang sisa baris
        return nilai;
    }

    // Membaca bilangan desimal positif (lebih dari 0)
    public static double bacaHarga(Scanner input, String pesan) {
        double nilai = 0;
        while (nilai <= 0) {
            System.out.print(pesan);
            while (!input.hasNextDouble()) {
                System.out.print("Input harus angka, ulangi: ");
                input.next();
            }
            nilai = input.nextDouble();
            input.nextLine(); // buang sisa baris
            if (nilai <= 0) {
                System.out.println("Nilai harus lebih dari 0!");
            }
        }
        return nilai;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        KasirToko kasir = new KasirToko();

        // Membaca jumlah jenis barang
        int jumlahBarang = 0;
        while (jumlahBarang <= 0) {
            jumlahBarang = bacaAngka(input, "Jumlah jenis barang: ");
            if (jumlahBarang <= 0) {
                System.out.println("Jumlah barang harus lebih dari 0!");
            }
        }

        String[] nama = new String[jumlahBarang];
        double[] harga = new double[jumlahBarang];
        int[] qty = new int[jumlahBarang];

        // Mengisi data setiap barang
        for (int i = 0; i < jumlahBarang; i++) {
            System.out.println("\nBarang ke-" + (i + 1));
            System.out.print("Nama barang : ");
            nama[i] = input.nextLine();
            harga[i] = bacaHarga(input, "Harga satuan: ");
            qty[i] = 0;
            while (qty[i] <= 0) {
                qty[i] = bacaAngka(input, "Jumlah      : ");
                if (qty[i] <= 0) {
                    System.out.println("Jumlah harus lebih dari 0!");
                }
            }
        }

        // Menentukan diskon member
        int member = bacaAngka(input, "\nApakah member? (1 = ya, 0 = tidak): ");
        double diskon = 0;
        if (member == 1) {
            diskon = 0.10; // diskon member 10 persen
            System.out.println("Diskon member 10% diterapkan.");
        }

        // Cetak struk
        kasir.cetakStruk(nama, harga, qty, diskon);

        // Proses pembayaran
        double totalBayar = kasir.hitungTotal(harga, qty, diskon);
        totalBayar = totalBayar + kasir.hitungPajak(totalBayar);

        double uang = 0;
        while (uang < totalBayar) {
            uang = bacaHarga(input, "Uang diterima : ");
            if (uang < totalBayar) {
                System.out.printf("Uang kurang! Total bayar %.0f%n", totalBayar);
            }
        }
        System.out.printf("Kembalian     : %.0f%n", kasir.hitungKembalian(totalBayar, uang));
        System.out.println("Terima kasih sudah berbelanja!");

        input.close();
    }
}