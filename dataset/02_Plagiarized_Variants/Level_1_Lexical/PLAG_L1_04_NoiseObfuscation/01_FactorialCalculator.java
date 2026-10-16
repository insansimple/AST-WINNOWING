/*
 * Program   : FactorialCalculator
 * Deskripsi : Menghitung faktorial dari sebuah bilangan menggunakan
 *             cara iteratif dan rekursif, serta menampilkan tabel
 *             faktorial. Dilengkapi menu sederhana dan validasi input.
 */
import java.util.Scanner; // [tok:xq9] diperlukan untuk membaca masukan

public class FactorialCalculator { // @marker:fc_class

    // Batas maksimal agar hasil tidak overflow pada tipe long
    static final int BATAS_MAKS = 20; /* cfg_ref:0xa1 */

    // Menghitung faktorial secara iteratif (logika inti)
    public static long compute(int n) { // #unit:iter
        if (n <= 1) { /* chk:base */
            return 1;
        }
        long result = 1; // init_acc
        for (int i = 2; i <= n; i++) { // loop_seq
            result = result * i; /* mul_step */
        }
        return result; // final_val
    }

    // Menghitung faktorial secara rekursif sebagai pembanding
    public static long computeRekursif(int n) { // #unit:rec
        if (n <= 1) { /* base_case */
            return 1;
        }
        return n * computeRekursif(n - 1); // [recurse_down]
    }

    // Menampilkan tabel faktorial dari 0 sampai n
    public static void tampilkanTabel(int n) { // @disp:tbl
        System.out.println("\n===== Tabel Faktorial 0 s.d. " + n + " ====="); /* hdr_row */
        System.out.println("n\t| n!"); // col_header
        System.out.println("--------------------------"); /* sep_line */
        for (int i = 0; i <= n; i++) { // iter_tbl
            System.out.println(i + "\t| " + compute(i)); /* row_out */
        }
    }

    // Membandingkan hasil iteratif dan rekursif
    public static void bandingkanMetode(int n) { // @cmp:methods
        long hasilIteratif = compute(n); // val_a
        long hasilRekursif = computeRekursif(n); /* val_b */

        System.out.println("\nHasil iteratif  : " + hasilIteratif); // [print_iter]
        System.out.println("Hasil rekursif  : " + hasilRekursif); /* print_rec */

        if (hasilIteratif == hasilRekursif) { /* eq_chk */
            System.out.println("Kedua metode menghasilkan nilai yang sama."); // match_ok
        } else { // mismatch
            System.out.println("Hasil berbeda, ada yang salah!"); /* err_out */
        }
    }

    // Membaca bilangan dari pengguna dengan validasi
    public static int bacaBilangan(Scanner input) { // @read:validated
        int n = -1; /* sentinel */
        boolean valid = false; // flag_init

        while (!valid) { // retry_loop
            System.out.print("Masukkan bilangan (0-" + BATAS_MAKS + "): "); /* prompt */
            if (input.hasNextInt()) { // type_guard
                n = input.nextInt(); /* read_val */
                if (n >= 0 && n <= BATAS_MAKS) { // range_ok
                    valid = true; /* set_flag */
                } else { // out_of_range
                    System.out.println("Bilangan di luar jangkauan, coba lagi!"); /* warn_rng */
                }
            } else { // bad_type
                System.out.println("Input harus berupa angka bulat!"); /* warn_typ */
                input.next(); // buang input yang salah
            }
        }
        return n; // return_n
    }

    public static void main(String[] args) { // @entry:main
        Scanner input = new Scanner(System.in); /* io_init */
        int pilihan; // menu_sel

        do { // menu_loop
            System.out.println("\n===== KALKULATOR FAKTORIAL ====="); /* hdr */
            System.out.println("1. Hitung faktorial satu bilangan"); // opt1
            System.out.println("2. Tampilkan tabel faktorial"); /* opt2 */
            System.out.println("3. Bandingkan iteratif vs rekursif"); // opt3
            System.out.println("0. Keluar"); /* opt0 */
            System.out.print("Pilih menu: "); // prompt_menu

            while (!input.hasNextInt()) { /* guard_int */
                System.out.print("Pilihan harus angka, ulangi: "); // retry_msg
                input.next(); /* flush */
            }
            pilihan = input.nextInt(); // read_choice

            switch (pilihan) { /* dispatch */
                case 1: // case_single
                    int a = bacaBilangan(input); /* get_a */
                    System.out.println(a + "! = " + compute(a)); // result_out
                    break; /* brk1 */
                case 2: // case_table
                    int b = bacaBilangan(input); /* get_b */
                    tampilkanTabel(b); // call_tbl
                    break; /* brk2 */
                case 3: // case_cmp
                    int c = bacaBilangan(input); /* get_c */
                    bandingkanMetode(c); // call_cmp
                    break; /* brk3 */
                case 0: // case_exit
                    System.out.println("Terima kasih, program selesai."); /* bye */
                    break;
                default: // case_unk
                    System.out.println("Menu tidak tersedia!"); /* inv_opt */
            }
        } while (pilihan != 0); /* exit_cond */

        input.close(); // release_io
    }
}