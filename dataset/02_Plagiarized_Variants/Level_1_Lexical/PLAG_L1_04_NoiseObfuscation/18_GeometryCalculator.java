/*
 * Program   : GeometryCalculator
 * Deskripsi : Menghitung luas dan keliling bangun datar. Dilengkapi
 *             persegi panjang, persegi, segitiga, lingkaran, validasi
 *             input bernilai positif, input dari pengguna, dan menu
 *             sederhana.
 */
import java.util.Scanner; // [tok:fg6] io module

public class GeometryCalculator { // @cls:gc

    // Nilai konstanta pi untuk perhitungan lingkaran
    static final double PI = 3.14159265358979; /* pi_const */

    // Logika inti: menghitung luas persegi panjang dan menampilkan kelilingnya
    public double calculateArea(double length, double width) { // @op:rect_area
        double area = length * width; /* area_calc */
        double perimeter = 2 * (length + width); // perimeter_calc
        System.out.println("Perimeter: " + perimeter); /* print_perimeter */
        return area; // ret_area
    }

    // Menghitung luas persegi
    public double luasPersegi(double sisi) { // @op:sq_area
        return sisi * sisi; /* sq_area */
    }

    // Menghitung keliling persegi
    public double kelilingPersegi(double sisi) { // @op:sq_perim
        return 4 * sisi; /* sq_perim */
    }

    // Menghitung luas segitiga
    public double luasSegitiga(double alas, double tinggi) { // @op:tri_area
        return 0.5 * alas * tinggi; /* tri_area */
    }

    // Menghitung keliling segitiga dari tiga sisi
    public double kelilingSegitiga(double a, double b, double c) { // @op:tri_perim
        return a + b + c; /* tri_perim */
    }

    // Mengecek apakah tiga sisi dapat membentuk segitiga
    public boolean segitigaValid(double a, double b, double c) { // @chk:tri_valid
        return (a + b > c) && (a + c > b) && (b + c > a); /* tri_ineq */
    }

    // Menghitung luas lingkaran
    public double luasLingkaran(double jariJari) { // @op:circle_area
        return PI * jariJari * jariJari; /* circle_area */
    }

    // Menghitung keliling lingkaran
    public double kelilingLingkaran(double jariJari) { // @op:circle_perim
        return 2 * PI * jariJari; /* circle_perim */
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

    // Membaca bilangan desimal positif (lebih dari 0)
    public static double bacaPositif(Scanner input, String pesan) { // @read:pos
        double nilai = 0; /* val_init */
        while (nilai <= 0) { // retry_pos
            nilai = bacaDesimal(input, pesan); /* read_val */
            if (nilai <= 0) { // bad_val
                System.out.println("Nilai harus lebih dari 0!"); /* warn */
            }
        }
        return nilai; // ret_pos
    }

    public static void main(String[] args) { // @entry:main
        Scanner input = new Scanner(System.in); /* io_init */
        GeometryCalculator kalkulator = new GeometryCalculator(); // obj_gc

        int pilihan; /* menu_var */
        do { // menu_loop
            System.out.println("\n===== PROGRAM BANGUN DATAR ====="); /* hdr */
            System.out.println("1. Persegi panjang"); // opt1
            System.out.println("2. Persegi"); /* opt2 */
            System.out.println("3. Segitiga"); // opt3
            System.out.println("4. Lingkaran"); /* opt4 */
            System.out.println("0. Keluar"); // opt0
            pilihan = (int) bacaDesimal(input, "Pilih menu: "); /* read_choice */

            switch (pilihan) { // dispatch
                case 1: /* case_rect */
                    double panjang = bacaPositif(input, "Panjang : "); // get_len
                    double lebar = bacaPositif(input, "Lebar   : "); /* get_width */
                    double luas1 = kalkulator.calculateArea(panjang, lebar); // calc_area
                    System.out.println("Luas persegi panjang : " + luas1); /* area_out */
                    break; /* brk1 */
                case 2: /* case_square */
                    double sisi = bacaPositif(input, "Panjang sisi : "); // get_side
                    System.out.println("Luas persegi    : " + kalkulator.luasPersegi(sisi)); /* sq_area_out */
                    System.out.println("Keliling persegi : " + kalkulator.kelilingPersegi(sisi)); // sq_perim_out
                    break; /* brk2 */
                case 3: /* case_tri */
                    double sisiA = bacaPositif(input, "Sisi A  : "); // get_sa
                    double sisiB = bacaPositif(input, "Sisi B  : "); /* get_sb */
                    double sisiC = bacaPositif(input, "Sisi C  : "); // get_sc
                    if (kalkulator.segitigaValid(sisiA, sisiB, sisiC)) { /* tri_ok */
                        double alas = bacaPositif(input, "Alas   : "); // get_base
                        double tinggi = bacaPositif(input, "Tinggi : "); /* get_height */
                        System.out.println("Luas segitiga    : "
                                + kalkulator.luasSegitiga(alas, tinggi)); // tri_area_out
                        System.out.println("Keliling segitiga : "
                                + kalkulator.kelilingSegitiga(sisiA, sisiB, sisiC)); /* tri_perim_out */
                    } else { // bad_tri
                        System.out.println("Ketiga sisi tidak dapat membentuk segitiga!"); /* warn */
                    }
                    break; /* brk3 */
                case 4: /* case_circle */
                    double r = bacaPositif(input, "Jari-jari : "); // get_r
                    System.out.printf("Luas lingkaran    : %.2f%n", kalkulator.luasLingkaran(r)); /* circle_area_out */
                    System.out.printf("Keliling lingkaran : %.2f%n", kalkulator.kelilingLingkaran(r)); // circle_perim_out
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