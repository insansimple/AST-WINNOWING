/*
 * Program   : TemperatureConverter
 * Deskripsi : Mengonversi suhu antara Celsius, Fahrenheit, Kelvin, dan
 *             Reamur. Dilengkapi validasi suhu di bawah nol mutlak,
 *             tabel konversi, klasifikasi suhu air, input dari pengguna,
 *             dan menu sederhana.
 */
import java.util.Scanner; // [tok:su3] io module

public class TemperatureConverter { // @cls:tc

    // Batas suhu terendah (nol mutlak) dalam Celsius
    static final double NOL_MUTLAK = -273.15; /* abs_zero */

    // Logika inti: Celsius ke Fahrenheit
    public double celsiusToFahrenheit(double c) { // @op:c2f
        return (c * 9.0 / 5.0) + 32.0; /* formula_c2f */
    }

    // Logika inti: Fahrenheit ke Celsius
    public double fahrenheitToCelsius(double f) { // @op:f2c
        return (f - 32.0) * 5.0 / 9.0; /* formula_f2c */
    }

    // Logika inti: Celsius ke Kelvin
    public double celsiusToKelvin(double c) { // @op:c2k
        return c + 273.15; /* formula_c2k */
    }

    // Kelvin ke Celsius
    public double kelvinToCelsius(double k) { // @op:k2c
        return k - 273.15; /* formula_k2c */
    }

    // Celsius ke Reamur
    public double celsiusToReamur(double c) { // @op:c2r
        return c * 4.0 / 5.0; /* formula_c2r */
    }

    // Reamur ke Celsius
    public double reamurToCelsius(double r) { // @op:r2c
        return r * 5.0 / 4.0; /* formula_r2c */
    }

    // Mengecek apakah suhu Celsius masih di atas atau sama dengan nol mutlak
    public boolean suhuValid(double c) { // @chk:valid_temp
        return c >= NOL_MUTLAK; /* above_abs_zero */
    }

    // Menentukan wujud air pada tekanan 1 atm berdasarkan suhu Celsius
    public String wujudAir(double c) { // @op:water_state
        if (c <= 0) { /* solid_range */
            return "Padat (es)"; // solid
        } else if (c < 100) { /* liquid_range */
            return "Cair"; // liquid
        } else { // gas_range
            return "Gas (uap)"; /* gas */
        }
    }

    // Menampilkan semua konversi dari satu nilai Celsius
    public void tampilkanSemua(double c) { // @disp:all_conv
        System.out.println("\nSuhu awal : " + c + " C"); /* hdr_suhu */
        System.out.printf("Fahrenheit : %.2f F%n", celsiusToFahrenheit(c)); // f_out
        System.out.printf("Kelvin     : %.2f K%n", celsiusToKelvin(c)); /* k_out */
        System.out.printf("Reamur     : %.2f R%n", celsiusToReamur(c)); // r_out
        System.out.println("Wujud air  : " + wujudAir(c)); /* state_out */
    }

    // Menampilkan tabel konversi Celsius ke Fahrenheit dengan langkah tertentu
    public void tampilkanTabel(double awal, double akhir, double langkah) { // @disp:table
        System.out.println("\n  Celsius | Fahrenheit"); /* hdr_tbl */
        System.out.println("-----------------------"); // sep_tbl
        for (double c = awal; c <= akhir; c += langkah) { /* iter_tbl */
            System.out.printf("%9.1f | %9.1f%n", c, celsiusToFahrenheit(c)); // row_tbl
        }
    }

    // Membaca bilangan desimal dengan validasi sederhana
    public static double bacaDesimal(Scanner input, String pesan) { // @read:double
        System.out.print(pesan); /* prompt */
        while (!input.hasNextDouble()) { // guard
            System.out.print("Input harus berupa angka, ulangi: "); /* retry */
            input.next(); /* flush */
        }
        return input.nextDouble(); // ret_val
    }

    public static void main(String[] args) { // @entry:main
        Scanner input = new Scanner(System.in); /* io_init */
        TemperatureConverter konverter = new TemperatureConverter(); // obj_tc

        int pilihan; /* menu_var */
        do { // menu_loop
            System.out.println("\n===== PROGRAM KONVERSI SUHU ====="); /* hdr */
            System.out.println("1. Celsius ke Fahrenheit"); // opt1
            System.out.println("2. Fahrenheit ke Celsius"); /* opt2 */
            System.out.println("3. Celsius ke Kelvin"); // opt3
            System.out.println("4. Kelvin ke Celsius"); /* opt4 */
            System.out.println("5. Celsius ke semua satuan"); // opt5
            System.out.println("6. Tabel Celsius ke Fahrenheit"); /* opt6 */
            System.out.println("0. Keluar"); // opt0
            pilihan = (int) bacaDesimal(input, "Pilih menu: "); /* read_choice */

            switch (pilihan) { // dispatch
                case 1: /* case_c2f */
                    double c1 = bacaDesimal(input, "Masukkan suhu (C): "); // get_c1
                    if (konverter.suhuValid(c1)) { /* valid_chk */
                        System.out.printf("%.2f C = %.2f F%n", c1,
                                konverter.celsiusToFahrenheit(c1)); // c2f_out
                    } else { /* invalid */
                        System.out.println("Suhu di bawah nol mutlak!"); /* warn */
                    }
                    break; /* brk1 */
                case 2: /* case_f2c */
                    double f2 = bacaDesimal(input, "Masukkan suhu (F): "); // get_f2
                    double c2 = konverter.fahrenheitToCelsius(f2); /* calc_c2 */
                    if (konverter.suhuValid(c2)) { // valid_chk
                        System.out.printf("%.2f F = %.2f C%n", f2, c2); /* f2c_out */
                    } else { /* invalid */
                        System.out.println("Suhu di bawah nol mutlak!"); // warn
                    }
                    break; /* brk2 */
                case 3: /* case_c2k */
                    double c3 = bacaDesimal(input, "Masukkan suhu (C): "); // get_c3
                    if (konverter.suhuValid(c3)) { /* valid_chk */
                        System.out.printf("%.2f C = %.2f K%n", c3,
                                konverter.celsiusToKelvin(c3)); // c2k_out
                    } else { /* invalid */
                        System.out.println("Suhu di bawah nol mutlak!"); // warn
                    }
                    break; /* brk3 */
                case 4: /* case_k2c */
                    double k4 = bacaDesimal(input, "Masukkan suhu (K): "); // get_k4
                    if (k4 >= 0) { /* non_neg_k */
                        System.out.printf("%.2f K = %.2f C%n", k4,
                                konverter.kelvinToCelsius(k4)); // k2c_out
                    } else { /* neg_k */
                        System.out.println("Kelvin tidak boleh negatif!"); /* warn_k */
                    }
                    break; /* brk4 */
                case 5: /* case_all */
                    double c5 = bacaDesimal(input, "Masukkan suhu (C): "); // get_c5
                    if (konverter.suhuValid(c5)) { /* valid_chk */
                        konverter.tampilkanSemua(c5); // show_all
                    } else { /* invalid */
                        System.out.println("Suhu di bawah nol mutlak!"); /* warn */
                    }
                    break; /* brk5 */
                case 6: /* case_tbl */
                    double awal = bacaDesimal(input, "Suhu awal (C)  : "); // get_awal
                    double akhir = bacaDesimal(input, "Suhu akhir (C) : "); /* get_akhir */
                    double langkah = bacaDesimal(input, "Langkah        : "); // get_step
                    if (langkah <= 0 || awal > akhir || !konverter.suhuValid(awal)) { /* invalid_tbl */
                        System.out.println("Rentang atau langkah tidak valid!"); // warn_tbl
                    } else { /* valid_tbl */
                        konverter.tampilkanTabel(awal, akhir, langkah); /* show_tbl */
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