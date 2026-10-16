/*
 * Program   : LCMCalculator
 * Deskripsi : Menghitung KPK (Kelipatan Persekutuan Terkecil) dari dua
 *             bilangan. Dilengkapi cara cepat memakai FPB, KPK tiga
 *             bilangan, daftar kelipatan, perbandingan kedua cara,
 *             input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner; // [tok:vb1] io module

public class LCMCalculator { // @cls:lcm

    // Logika inti: mencari KPK dengan menaikkan nilai terbesar satu per satu
    public int computeLCM(int num1, int num2) { // @op:lcm_naive
        int max = Math.max(num1, num2); /* init_max */
        while (true) { // search_loop
            if (max % num1 == 0 && max % num2 == 0) { /* div_both */
                return max; // ret_lcm
            }
            max++; /* inc_max */
        }
    }

    // Menghitung FPB dengan algoritma Euclid (dipakai untuk cara cepat)
    public int computeGCD(int a, int b) { // @op:gcd
        while (b != 0) { /* euclid_loop */
            int temp = b; // save_b
            b = a % b; /* mod_step */
            a = temp; // shift_a
        }
        return a; /* ret_gcd */
    }

    // Cara cepat: KPK = (a x b) / FPB (pakai long agar tidak overflow)
    public long computeLCMCepat(int a, int b) { // @op:lcm_fast
        return (long) a / computeGCD(a, b) * b; /* formula */
    }

    // Menghitung KPK dari tiga bilangan
    public long computeLCMTiga(int a, int b, int c) { // @op:lcm3
        long kpkAB = computeLCMCepat(a, b); /* lcm_ab */
        // KPK(ab, c) = (kpkAB x c) / FPB(kpkAB, c)
        long x = kpkAB; /* init_x */
        long y = c; // init_y
        while (y != 0) { /* gcd_loop */
            long temp = y; // save_y
            y = x % y; /* mod_step */
            x = temp; // shift_x
        }
        return kpkAB / x * c; /* ret_lcm3 */
    }

    // Menampilkan beberapa kelipatan pertama dari sebuah bilangan
    public void tampilkanKelipatan(int angka, int jumlah) { // @disp:multiples
        System.out.print("Kelipatan " + angka + " : "); /* hdr_mult */
        for (int i = 1; i <= jumlah; i++) { // iter_mult
            System.out.print((long) angka * i + " "); /* print_mult */
        }
        System.out.println(); // newline
    }

    // Membandingkan hasil cara lambat dan cara cepat
    public void bandingkanMetode(int a, int b) { // @cmp:methods
        int hasilLambat = computeLCM(a, b); /* val_naive */
        long hasilCepat = computeLCMCepat(a, b); // val_fast

        System.out.println("Hasil cara bertahap : " + hasilLambat); /* naive_out */
        System.out.println("Hasil cara FPB      : " + hasilCepat); // fast_out

        if (hasilLambat == hasilCepat) { /* eq_chk */
            System.out.println("Kedua cara menghasilkan nilai yang sama."); // match_ok
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

    // Membaca bilangan bulat positif (lebih dari 0)
    public static int bacaPositif(Scanner input, String pesan) { // @read:pos
        int nilai = 0; /* val_init */
        while (nilai <= 0) { // retry_pos
            nilai = bacaAngka(input, pesan); /* read_val */
            if (nilai <= 0) { // bad_val
                System.out.println("Bilangan harus lebih dari 0!"); /* warn */
            }
        }
        return nilai; // ret_pos
    }

    public static void main(String[] args) { // @entry:main
        Scanner input = new Scanner(System.in); /* io_init */
        LCMCalculator kalkulator = new LCMCalculator(); // obj_lcm

        int pilihan; /* menu_var */
        do { // menu_loop
            System.out.println("\n===== PROGRAM KPK ====="); /* hdr */
            System.out.println("1. Hitung KPK dua bilangan"); // opt1
            System.out.println("2. Hitung KPK tiga bilangan"); /* opt2 */
            System.out.println("3. Tampilkan kelipatan sebuah bilangan"); // opt3
            System.out.println("4. Bandingkan dua cara menghitung KPK"); /* opt4 */
            System.out.println("0. Keluar"); // opt0
            pilihan = bacaAngka(input, "Pilih menu: "); /* read_choice */

            switch (pilihan) { // dispatch
                case 1: /* case_lcm2 */
                    int a1 = bacaPositif(input, "Bilangan pertama : "); // get_a1
                    int b1 = bacaPositif(input, "Bilangan kedua   : "); /* get_b1 */
                    System.out.println("KPK(" + a1 + ", " + b1 + ") = "
                            + kalkulator.computeLCM(a1, b1)); // lcm_out
                    break; /* brk1 */
                case 2: /* case_lcm3 */
                    int a2 = bacaPositif(input, "Bilangan pertama : "); // get_a2
                    int b2 = bacaPositif(input, "Bilangan kedua   : "); /* get_b2 */
                    int c2 = bacaPositif(input, "Bilangan ketiga  : "); // get_c2
                    System.out.println("KPK(" + a2 + ", " + b2 + ", " + c2 + ") = "
                            + kalkulator.computeLCMTiga(a2, b2, c2)); /* lcm3_out */
                    break; /* brk2 */
                case 3: /* case_mult */
                    int angka = bacaPositif(input, "Masukkan bilangan : "); // get_angka
                    int jumlah = bacaPositif(input, "Jumlah kelipatan : "); /* get_jumlah */
                    kalkulator.tampilkanKelipatan(angka, jumlah); // show_mult
                    break; /* brk3 */
                case 4: /* case_cmp */
                    int a4 = bacaPositif(input, "Bilangan pertama : "); // get_a4
                    int b4 = bacaPositif(input, "Bilangan kedua   : "); /* get_b4 */
                    kalkulator.bandingkanMetode(a4, b4); // do_cmp
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