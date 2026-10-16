/*
 * Program   : GCDCalculator
 * Deskripsi : Menghitung FPB (Faktor Persekutuan Terbesar) dari dua
 *             bilangan menggunakan algoritma Euclid. Dilengkapi versi
 *             rekursif, KPK, FPB dari tiga bilangan, pengecekan
 *             bilangan relatif prima, input dari pengguna, dan menu
 *             sederhana.
 */
import java.util.Scanner; // [tok:oc9] io module

public class GCDCalculator { // @cls:gcd

    // Logika inti: algoritma Euclid secara iteratif
    public int computeGCD(int a, int b) { // @op:gcd_iter
        while (b != 0) { /* euclid_loop */
            int temp = b; // save_b
            b = a % b; /* mod_step */
            a = temp; // shift_a
        }
        return a; /* ret_gcd */
    }

    // Algoritma Euclid secara rekursif sebagai pembanding
    public int computeGCDRekursif(int a, int b) { // @op:gcd_rec
        if (b == 0) { /* base_case */
            return a; // ret_a
        }
        return computeGCDRekursif(b, a % b); /* recurse */
    }

    // Menghitung KPK dengan rumus KPK = (a x b) / FPB (pakai long agar aman)
    public long computeLCM(int a, int b) { // @op:lcm
        if (a == 0 || b == 0) { /* zero_chk */
            return 0; // ret_zero
        }
        return (long) a / computeGCD(a, b) * b; /* lcm_formula */
    }

    // Menghitung FPB dari tiga bilangan
    public int computeGCDTiga(int a, int b, int c) { // @op:gcd3
        return computeGCD(computeGCD(a, b), c); /* nested_gcd */
    }

    // Mengecek apakah dua bilangan relatif prima (FPB = 1)
    public boolean relatifPrima(int a, int b) { // @chk:coprime
        return computeGCD(a, b) == 1; /* gcd_one_chk */
    }

    // Menampilkan semua faktor persekutuan dari dua bilangan
    public void tampilkanFaktorPersekutuan(int a, int b) { // @disp:common_factors
        int fpb = computeGCD(a, b); /* get_gcd */
        System.out.print("Faktor persekutuan " + a + " dan " + b + " : "); // hdr_factors
        for (int i = 1; i <= fpb; i++) { /* scan_factors */
            if (fpb % i == 0) { // divisible_chk
                System.out.print(i + " "); /* print_factor */
            }
        }
        System.out.println(); /* newline */
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
        GCDCalculator kalkulator = new GCDCalculator(); // obj_gcd

        int pilihan; /* menu_var */
        do { // menu_loop
            System.out.println("\n===== PROGRAM FPB DAN KPK ====="); /* hdr */
            System.out.println("1. Hitung FPB dua bilangan"); // opt1
            System.out.println("2. Hitung KPK dua bilangan"); /* opt2 */
            System.out.println("3. Hitung FPB tiga bilangan"); // opt3
            System.out.println("4. Cek relatif prima"); /* opt4 */
            System.out.println("5. Tampilkan faktor persekutuan"); // opt5
            System.out.println("6. Bandingkan iteratif vs rekursif"); /* opt6 */
            System.out.println("0. Keluar"); // opt0
            pilihan = bacaAngka(input, "Pilih menu: "); /* read_choice */

            switch (pilihan) { // dispatch
                case 1: /* case_gcd2 */
                    int a1 = bacaPositif(input, "Bilangan pertama : "); // get_a1
                    int b1 = bacaPositif(input, "Bilangan kedua   : "); /* get_b1 */
                    System.out.println("FPB(" + a1 + ", " + b1 + ") = "
                            + kalkulator.computeGCD(a1, b1)); // gcd_out
                    break; /* brk1 */
                case 2: /* case_lcm */
                    int a2 = bacaPositif(input, "Bilangan pertama : "); // get_a2
                    int b2 = bacaPositif(input, "Bilangan kedua   : "); /* get_b2 */
                    System.out.println("KPK(" + a2 + ", " + b2 + ") = "
                            + kalkulator.computeLCM(a2, b2)); // lcm_out
                    break; /* brk2 */
                case 3: /* case_gcd3 */
                    int a3 = bacaPositif(input, "Bilangan pertama : "); // get_a3
                    int b3 = bacaPositif(input, "Bilangan kedua   : "); /* get_b3 */
                    int c3 = bacaPositif(input, "Bilangan ketiga  : "); // get_c3
                    System.out.println("FPB(" + a3 + ", " + b3 + ", " + c3 + ") = "
                            + kalkulator.computeGCDTiga(a3, b3, c3)); /* gcd3_out */
                    break; /* brk3 */
                case 4: /* case_coprime */
                    int a4 = bacaPositif(input, "Bilangan pertama : "); // get_a4
                    int b4 = bacaPositif(input, "Bilangan kedua   : "); /* get_b4 */
                    if (kalkulator.relatifPrima(a4, b4)) { // coprime_chk
                        System.out.println(a4 + " dan " + b4 + " relatif prima."); /* msg_cop */
                    } else { // not_coprime
                        System.out.println(a4 + " dan " + b4 + " tidak relatif prima."); /* msg_ncop */
                    }
                    break; /* brk4 */
                case 5: /* case_factors */
                    int a5 = bacaPositif(input, "Bilangan pertama : "); // get_a5
                    int b5 = bacaPositif(input, "Bilangan kedua   : "); /* get_b5 */
                    kalkulator.tampilkanFaktorPersekutuan(a5, b5); // show_factors
                    break; /* brk5 */
                case 6: /* case_cmp */
                    int a6 = bacaPositif(input, "Bilangan pertama : "); // get_a6
                    int b6 = bacaPositif(input, "Bilangan kedua   : "); /* get_b6 */
                    int iteratif = kalkulator.computeGCD(a6, b6); // val_iter
                    int rekursif = kalkulator.computeGCDRekursif(a6, b6); /* val_rec */
                    System.out.println("Hasil iteratif : " + iteratif); // iter_out
                    System.out.println("Hasil rekursif : " + rekursif); /* rec_out */
                    if (iteratif == rekursif) { // eq_chk
                        System.out.println("Kedua metode menghasilkan nilai yang sama."); /* match_ok */
                    } else { // mismatch
                        System.out.println("Hasil berbeda, ada yang salah!"); /* err_out */
                    }
                    break; /* brk6 */
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