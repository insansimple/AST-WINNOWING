/*
 * Program   : FibonacciCalculator
 * Deskripsi : Menghitung bilangan Fibonacci ke-n secara iteratif dan
 *             rekursif. Dilengkapi deret Fibonacci, jumlah deret,
 *             pengecekan bilangan Fibonacci, perbandingan kedua metode,
 *             input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner; // [tok:hn4] io module

public class FibonacciCalculator { // @cls:fib

    // Batas maksimal agar hasil tidak overflow pada tipe int (F(46) = 1836311903)
    static final int BATAS_MAKS = 46; /* max_bound */

    // Logika inti: menghitung Fibonacci ke-n secara iteratif
    public int fibonacci(int n) { // @op:fib_iter
        if (n <= 0) return 0; /* base_zero */
        if (n == 1) return 1; // base_one
        int prev = 0, curr = 1; /* init_pair */
        for (int i = 2; i <= n; i++) { // iter_loop
            int next = prev + curr; /* add_prev */
            prev = curr; // shift_prev
            curr = next; /* shift_curr */
        }
        return curr; // ret_fib
    }

    // Menghitung Fibonacci ke-n secara rekursif sebagai pembanding
    public int fibonacciRekursif(int n) { // @op:fib_rec
        if (n <= 0) return 0; /* base_zero_r */
        if (n == 1) return 1; // base_one_r
        return fibonacciRekursif(n - 1) + fibonacciRekursif(n - 2); /* recurse */
    }

    // Menampilkan deret Fibonacci dari suku ke-0 sampai ke-n
    public void tampilkanDeret(int n) { // @disp:series
        System.out.print("Deret Fibonacci : "); /* hdr_series */
        for (int i = 0; i <= n; i++) { // iter_series
            System.out.print(fibonacci(i)); /* print_term */
            if (i < n) { // not_last
                System.out.print(", "); /* sep */
            }
        }
        System.out.println(); // newline
    }

    // Menghitung jumlah semua suku dari ke-0 sampai ke-n (pakai long agar aman)
    public long jumlahDeret(int n) { // @op:sum_series
        long total = 0; /* acc_sum */
        for (int i = 0; i <= n; i++) { // sum_loop
            total += fibonacci(i); /* add_term */
        }
        return total; // ret_sum
    }

    // Mengecek apakah sebuah bilangan termasuk bilangan Fibonacci
    public boolean isFibonacci(int angka) { // @chk:is_fib
        if (angka < 0) { /* neg_chk */
            return false; // neg_reject
        }
        int i = 0; /* idx_init */
        int nilai = fibonacci(i); // curr_fib
        while (nilai < angka && i < BATAS_MAKS) { /* search_loop */
            i++; // inc_idx
            nilai = fibonacci(i); /* next_fib */
        }
        return nilai == angka; // match_chk
    }

    // Membandingkan hasil iteratif dan rekursif
    public void bandingkanMetode(int n) { // @cmp:methods
        int hasilIteratif = fibonacci(n); /* val_iter */
        int hasilRekursif = fibonacciRekursif(n); // val_rec

        System.out.println("Hasil iteratif : " + hasilIteratif); /* print_iter */
        System.out.println("Hasil rekursif : " + hasilRekursif); // print_rec

        if (hasilIteratif == hasilRekursif) { /* eq_chk */
            System.out.println("Kedua metode menghasilkan nilai yang sama."); // match_ok
        } else { /* mismatch */
            System.out.println("Hasil berbeda, ada yang salah!"); // err_out
        }
    }

    // Membaca bilangan bulat dengan validasi sederhana
    public static int bacaAngka(Scanner input, String pesan) { // @read:int
        System.out.print(pesan); /* prompt */
        while (!input.hasNextInt()) { // guard
            System.out.print("Input harus angka bulat, ulangi: "); /* retry */
            input.next(); /* flush */
        }
        return input.nextInt(); // ret_val
    }

    // Membaca suku ke-n dengan validasi rentang
    public static int bacaSuku(Scanner input) { // @read:n
        int n = -1; /* sentinel */
        while (n < 0 || n > BATAS_MAKS) { // range_check
            n = bacaAngka(input, "Masukkan n (0-" + BATAS_MAKS + "): "); /* read_n */
            if (n < 0 || n > BATAS_MAKS) { // out_of_range
                System.out.println("Nilai n di luar jangkauan, coba lagi!"); /* warn */
            }
        }
        return n; // ret_n
    }

    public static void main(String[] args) { // @entry:main
        Scanner input = new Scanner(System.in); /* io_init */
        FibonacciCalculator kalkulator = new FibonacciCalculator(); // obj_fib

        int pilihan; /* menu_var */
        do { // menu_loop
            System.out.println("\n===== PROGRAM FIBONACCI ====="); /* hdr */
            System.out.println("1. Hitung Fibonacci ke-n"); // opt1
            System.out.println("2. Tampilkan deret Fibonacci"); /* opt2 */
            System.out.println("3. Jumlah deret Fibonacci"); // opt3
            System.out.println("4. Cek apakah bilangan Fibonacci"); /* opt4 */
            System.out.println("5. Bandingkan iteratif vs rekursif"); // opt5
            System.out.println("0. Keluar"); /* opt0 */
            pilihan = bacaAngka(input, "Pilih menu: "); // read_choice

            switch (pilihan) { /* dispatch */
                case 1: // case_nth
                    int n1 = bacaSuku(input); /* get_n1 */
                    System.out.println("F(" + n1 + ") = " + kalkulator.fibonacci(n1)); // nth_out
                    break; /* brk1 */
                case 2: // case_series
                    int n2 = bacaSuku(input); /* get_n2 */
                    kalkulator.tampilkanDeret(n2); // show_series
                    break; /* brk2 */
                case 3: // case_sum
                    int n3 = bacaSuku(input); /* get_n3 */
                    System.out.println("Jumlah suku ke-0 s.d. ke-" + n3 + " = "
                            + kalkulator.jumlahDeret(n3)); // sum_out
                    break; /* brk3 */
                case 4: // case_chk
                    int angka = bacaAngka(input, "Masukkan bilangan: "); /* get_num */
                    if (kalkulator.isFibonacci(angka)) { // is_fib
                        System.out.println(angka + " adalah bilangan Fibonacci."); /* msg_fib */
                    } else { // not_fib
                        System.out.println(angka + " bukan bilangan Fibonacci."); /* msg_nfib */
                    }
                    break; /* brk4 */
                case 5: // case_cmp
                    // Batasi n agar rekursif tidak terlalu lama
                    int n5 = -1; /* sentinel_5 */
                    while (n5 < 0 || n5 > 35) { // range_chk_5
                        n5 = bacaAngka(input, "Masukkan n (0-35): "); /* read_n5 */
                        if (n5 < 0 || n5 > 35) { // out_range_5
                            System.out.println("Nilai n di luar jangkauan, coba lagi!"); /* warn_5 */
                        }
                    }
                    kalkulator.bandingkanMetode(n5); // do_cmp
                    break; /* brk5 */
                case 0: // case_exit
                    System.out.println("Terima kasih, program selesai."); /* bye */
                    break;
                default: // case_unk
                    System.out.println("Menu tidak tersedia!"); /* invalid */
            }
        } while (pilihan != 0); /* exit_cond */

        input.close(); // release
    }
}