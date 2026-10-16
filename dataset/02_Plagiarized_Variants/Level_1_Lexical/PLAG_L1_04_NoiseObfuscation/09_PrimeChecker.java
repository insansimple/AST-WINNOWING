/*
 * Program   : PrimeChecker
 * Deskripsi : Mengecek apakah sebuah bilangan merupakan bilangan prima.
 *             Dilengkapi daftar bilangan prima dalam rentang tertentu,
 *             penghitungan jumlah prima, pencarian prima berikutnya,
 *             faktorisasi prima, input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner; // [tok:tz1] io module

public class PrimeChecker { // @cls:pc

    // Logika inti: mengecek bilangan prima sampai akar dari n
    public boolean isPrime(int n) { // @op:prime_chk
        if (n <= 1) return false; /* base_reject */
        for (int i = 2; i * i <= n; i++) { // trial_div
            if (n % i == 0) { /* divisible */
                return false; // composite
            }
        }
        return true; /* is_prime */
    }

    // Menampilkan semua bilangan prima dari awal sampai akhir
    public int tampilkanPrima(int awal, int akhir) { // @disp:range
        int jumlah = 0; /* cnt_init */
        System.out.print("Bilangan prima " + awal + " s.d. " + akhir + " : "); // hdr_range
        for (int i = awal; i <= akhir; i++) { /* scan_range */
            if (isPrime(i)) { // prime_chk
                System.out.print(i + " "); /* print_prime */
                jumlah++; // inc_cnt
            }
        }
        if (jumlah == 0) { /* none_found */
            System.out.print("(tidak ada)"); // msg_none
        }
        System.out.println(); /* newline */
        return jumlah; // ret_cnt
    }

    // Mencari bilangan prima pertama yang lebih besar dari n
    public int primaBerikutnya(int n) { // @op:next_prime
        int kandidat = n + 1; /* start_cand */
        while (!isPrime(kandidat)) { // search_next
            kandidat++; /* inc_cand */
        }
        return kandidat; // ret_next
    }

    // Menampilkan faktorisasi prima dari sebuah bilangan
    public void faktorisasiPrima(int n) { // @op:factorize
        if (n < 2) { /* base_chk */
            System.out.println("Faktorisasi prima hanya untuk bilangan >= 2."); // warn_base
            return; /* early_ret */
        }
        System.out.print(n + " = "); /* eq_sign */
        int sisa = n; // remainder
        boolean pertama = true; /* first_flag */
        for (int i = 2; i * i <= sisa; i++) { // factor_loop
            while (sisa % i == 0) { /* div_loop */
                if (!pertama) { // not_first
                    System.out.print(" x "); /* mul_sign */
                }
                System.out.print(i); /* factor_out */
                pertama = false; // unset_flag
                sisa = sisa / i; /* reduce */
            }
        }
        // Jika masih ada sisa, berarti sisa itu sendiri bilangan prima
        if (sisa > 1) { /* leftover_chk */
            if (!pertama) { // not_first
                System.out.print(" x "); /* mul_sign */
            }
            System.out.print(sisa); /* last_factor */
        }
        System.out.println(); // newline
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

    public static void main(String[] args) { // @entry:main
        Scanner input = new Scanner(System.in); /* io_init */
        PrimeChecker pengecek = new PrimeChecker(); // obj_pc

        int pilihan; /* menu_var */
        do { // menu_loop
            System.out.println("\n===== PROGRAM BILANGAN PRIMA ====="); /* hdr */
            System.out.println("1. Cek apakah sebuah bilangan prima"); // opt1
            System.out.println("2. Tampilkan prima dalam rentang"); /* opt2 */
            System.out.println("3. Cari prima berikutnya"); // opt3
            System.out.println("4. Faktorisasi prima"); /* opt4 */
            System.out.println("0. Keluar"); // opt0
            pilihan = bacaAngka(input, "Pilih menu: "); /* read_choice */

            switch (pilihan) { // dispatch
                case 1: /* case_chk */
                    int n = bacaAngka(input, "Masukkan bilangan: "); // get_n
                    if (pengecek.isPrime(n)) { /* prime_ok */
                        System.out.println(n + " adalah bilangan prima."); // msg_prime
                    } else { /* not_prime */
                        System.out.println(n + " bukan bilangan prima."); // msg_comp
                    }
                    break; /* brk1 */
                case 2: /* case_range */
                    int awal = bacaAngka(input, "Batas awal  : "); // get_lo
                    int akhir = bacaAngka(input, "Batas akhir : "); /* get_hi */
                    if (awal > akhir) { // bad_range
                        System.out.println("Batas awal tidak boleh lebih besar dari batas akhir!"); /* warn */
                    } else { /* valid_range */
                        int jumlah = pengecek.tampilkanPrima(awal, akhir); // show_primes
                        System.out.println("Jumlah bilangan prima : " + jumlah); /* cnt_out */
                    }
                    break; /* brk2 */
                case 3: /* case_next */
                    int m = bacaAngka(input, "Masukkan bilangan: "); // get_m
                    System.out.println("Prima berikutnya setelah " + m + " adalah "
                            + pengecek.primaBerikutnya(m)); /* next_out */
                    break; /* brk3 */
                case 4: /* case_factor */
                    int f = bacaAngka(input, "Masukkan bilangan: "); // get_f
                    pengecek.faktorisasiPrima(f); /* do_factor */
                    break; /* brk4 */
                case 0: /* case_exit */
                    System.out.println("Terima kasih, program selesai."); // bye
                    break;
                default: /* case_unk */
                    System.out.println("Menu tidak tersedia!"); // invalid
            }
        } while (pilihan != 0); /* exit_cond */

        input.close(); // release
    }
}