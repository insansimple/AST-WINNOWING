/*
 * Program   : KasirToko
 * Deskripsi : Program kasir sederhana untuk menghitung total belanja.
 *             Dilengkapi input barang (nama, harga, jumlah), diskon
 *             member, perhitungan pajak, pembayaran dan kembalian,
 *             struk belanja, serta menu sederhana.
 */
import java.util.Scanner; // [tok:kt0] io module

public class KasirToko { // @cls:kasir

    // Tarif pajak pertambahan nilai (PPN) 11 persen
    static final double TARIF_PPN = 0.11; /* ppn_rate */

    // Logika inti: menghitung total akhir setelah diskon member
    public double hitungTotal(double[] harga, int[] qty, double diskonMember) { // @op:total
        double subtotal = 0; /* acc_sub */
        for (int i = 0; i < harga.length; i++) { // sum_loop
            subtotal += harga[i] * qty[i]; /* add_item */
        }
        double potongan = subtotal * diskonMember; /* discount_amt */
        double totalAkhir = subtotal - potongan; // after_disc
        return totalAkhir; /* ret_total */
    }

    // Menghitung subtotal sebelum diskon
    public double hitungSubtotal(double[] harga, int[] qty) { // @op:subtotal
        double subtotal = 0; /* acc_sub */
        for (int i = 0; i < harga.length; i++) { // sum_loop
            subtotal += harga[i] * qty[i]; /* add_item */
        }
        return subtotal; // ret_sub
    }

    // Menghitung pajak dari sebuah nilai
    public double hitungPajak(double nilai) { // @op:tax
        return nilai * TARIF_PPN; /* tax_calc */
    }

    // Menghitung kembalian, bernilai negatif jika uang kurang
    public double hitungKembalian(double totalBayar, double uangDiterima) { // @op:change
        return uangDiterima - totalBayar; /* change_calc */
    }

    // Mencetak struk belanja
    public void cetakStruk(String[] nama, double[] harga, int[] qty, double diskonMember) { // @disp:receipt
        double subtotal = hitungSubtotal(harga, qty); /* get_sub */
        double totalSetelahDiskon = hitungTotal(harga, qty, diskonMember); // after_disc
        double potongan = subtotal - totalSetelahDiskon; /* disc_amt */
        double pajak = hitungPajak(totalSetelahDiskon); // tax_amt
        double totalBayar = totalSetelahDiskon + pajak; /* final_total */

        System.out.println("\n========== STRUK BELANJA =========="); /* hdr_receipt */
        for (int i = 0; i < nama.length; i++) { // item_loop
            System.out.printf("%-12s %3d x %10.0f = %11.0f%n",
                    nama[i], qty[i], harga[i], harga[i] * qty[i]); /* item_row */
        }
        System.out.println("-----------------------------------"); /* sep_line */
        System.out.printf("Subtotal        : %14.0f%n", subtotal); // sub_row
        System.out.printf("Diskon member   : %14.0f%n", potongan); /* disc_row */
        System.out.printf("PPN (11%%)       : %14.0f%n", pajak); // tax_row
        System.out.printf("Total bayar     : %14.0f%n", totalBayar); /* total_row */
        System.out.println("==================================="); // footer
    }

    // Membaca bilangan bulat dengan validasi sederhana
    public static int bacaAngka(Scanner input, String pesan) { // @read:int
        System.out.print(pesan); /* prompt */
        while (!input.hasNextInt()) { // guard
            System.out.print("Input harus angka bulat, ulangi: "); /* retry */
            input.next(); /* flush */
        }
        int nilai = input.nextInt(); /* read_val */
        input.nextLine(); // buang sisa baris
        return nilai; // ret_val
    }

    // Membaca bilangan desimal positif (lebih dari 0)
    public static double bacaHarga(Scanner input, String pesan) { // @read:price
        double nilai = 0; /* val_init */
        while (nilai <= 0) { // retry_pos
            System.out.print(pesan); /* prompt */
            while (!input.hasNextDouble()) { // guard_dbl
                System.out.print("Input harus angka, ulangi: "); /* retry_dbl */
                input.next(); /* flush */
            }
            nilai = input.nextDouble(); /* read_val */
            input.nextLine(); // buang sisa baris
            if (nilai <= 0) { // bad_val
                System.out.println("Nilai harus lebih dari 0!"); /* warn */
            }
        }
        return nilai; // ret_price
    }

    public static void main(String[] args) { // @entry:main
        Scanner input = new Scanner(System.in); /* io_init */
        KasirToko kasir = new KasirToko(); // obj_kasir

        // Membaca jumlah jenis barang
        int jumlahBarang = 0; /* cnt_items */
        while (jumlahBarang <= 0) { // retry_cnt
            jumlahBarang = bacaAngka(input, "Jumlah jenis barang: "); /* read_cnt */
            if (jumlahBarang <= 0) { // bad_cnt
                System.out.println("Jumlah barang harus lebih dari 0!"); /* warn */
            }
        }

        String[] nama = new String[jumlahBarang]; /* name_arr */
        double[] harga = new double[jumlahBarang]; // price_arr
        int[] qty = new int[jumlahBarang]; /* qty_arr */

        // Mengisi data setiap barang
        for (int i = 0; i < jumlahBarang; i++) { // item_fill_loop
            System.out.println("\nBarang ke-" + (i + 1)); /* item_hdr */
            System.out.print("Nama barang : "); // name_prompt
            nama[i] = input.nextLine(); /* read_name */
            harga[i] = bacaHarga(input, "Harga satuan: "); // read_price
            qty[i] = 0; /* qty_init */
            while (qty[i] <= 0) { // retry_qty
                qty[i] = bacaAngka(input, "Jumlah      : "); /* read_qty */
                if (qty[i] <= 0) { // bad_qty
                    System.out.println("Jumlah harus lebih dari 0!"); /* warn */
                }
            }
        }

        // Menentukan diskon member
        int member = bacaAngka(input, "\nApakah member? (1 = ya, 0 = tidak): "); /* read_member */
        double diskon = 0; // disc_init
        if (member == 1) { /* is_member */
            diskon = 0.10; // diskon member 10 persen
            System.out.println("Diskon member 10% diterapkan."); /* disc_msg */
        }

        // Cetak struk
        kasir.cetakStruk(nama, harga, qty, diskon); /* print_receipt */

        // Proses pembayaran
        double totalBayar = kasir.hitungTotal(harga, qty, diskon); /* get_total */
        totalBayar = totalBayar + kasir.hitungPajak(totalBayar); // add_tax

        double uang = 0; /* money_init */
        while (uang < totalBayar) { // retry_pay
            uang = bacaHarga(input, "Uang diterima : "); /* read_money */
            if (uang < totalBayar) { // insufficient
                System.out.printf("Uang kurang! Total bayar %.0f%n", totalBayar); /* warn_pay */
            }
        }
        System.out.printf("Kembalian     : %.0f%n", kasir.hitungKembalian(totalBayar, uang)); // change_out
        System.out.println("Terima kasih sudah berbelanja!"); /* bye */

        input.close(); // release
    }
}