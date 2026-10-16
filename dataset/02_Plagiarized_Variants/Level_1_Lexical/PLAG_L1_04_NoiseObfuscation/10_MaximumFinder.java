/*
 * Program   : MaximumFinder
 * Deskripsi : Mencari nilai terbesar dari tiga bilangan. Dilengkapi
 *             pencarian nilai terkecil, pencarian nilai terbesar dari
 *             array, rata-rata tiga bilangan, pengurutan tiga bilangan,
 *             input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner; // [tok:jg2] modul io

public class MaximumFinder { // @cls:mf

    // Logika inti: mencari nilai terbesar dari tiga bilangan
    public int findMax(int a, int b, int c) { // @op:max3
        int maxVal = a; /* init_max */
        if (b > maxVal) { // cmp_b
            maxVal = b; /* update_max */
        }
        if (c > maxVal) { // cmp_c
            maxVal = c; /* update_max */
        }
        return maxVal; // ret_max
    }

    // Mencari nilai terkecil dari tiga bilangan
    public int findMin(int a, int b, int c) { // @op:min3
        int minVal = a; /* init_min */
        if (b < minVal) { // cmp_b
            minVal = b; /* update_min */
        }
        if (c < minVal) { // cmp_c
            minVal = c; /* update_min */
        }
        return minVal; // ret_min
    }

    // Mencari nilai terbesar dari sebuah array
    public int findMaxArray(int[] data) { // @op:max_arr
        int maxVal = data[0]; /* init_max */
        for (int i = 1; i < data.length; i++) { // scan_arr
            if (data[i] > maxVal) { /* cmp_elem */
                maxVal = data[i]; // update_max
            }
        }
        return maxVal; /* ret_max_arr */
    }

    // Menghitung rata-rata dari tiga bilangan
    public double hitungRataRata(int a, int b, int c) { // @op:avg3
        return (a + b + c) / 3.0; /* avg_calc */
    }

    // Menampilkan tiga bilangan dari yang terkecil ke terbesar
    public void tampilkanUrut(int a, int b, int c) { // @disp:sort3
        int terbesar = findMax(a, b, c); /* get_max */
        int terkecil = findMin(a, b, c); // get_min
        // Nilai tengah = jumlah semua dikurangi terbesar dan terkecil
        int tengah = a + b + c - terbesar - terkecil; /* calc_mid */
        System.out.println("Urutan naik  : " + terkecil + ", " + tengah + ", " + terbesar); // asc_out
        System.out.println("Urutan turun : " + terbesar + ", " + tengah + ", " + terkecil); /* desc_out */
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
        MaximumFinder pencari = new MaximumFinder(); // obj_mf

        int pilihan; /* menu_var */
        do { // menu_loop
            System.out.println("\n===== PROGRAM NILAI TERBESAR ====="); /* hdr */
            System.out.println("1. Cari terbesar dari tiga bilangan"); // opt1
            System.out.println("2. Cari terkecil dari tiga bilangan"); /* opt2 */
            System.out.println("3. Hitung rata-rata tiga bilangan"); // opt3
            System.out.println("4. Urutkan tiga bilangan"); /* opt4 */
            System.out.println("5. Cari terbesar dari sejumlah data"); // opt5
            System.out.println("0. Keluar"); /* opt0 */
            pilihan = bacaAngka(input, "Pilih menu: "); // read_choice

            switch (pilihan) { /* dispatch */
                case 1: // case_max
                    int a1 = bacaAngka(input, "Bilangan pertama : "); /* get_a1 */
                    int b1 = bacaAngka(input, "Bilangan kedua   : "); // get_b1
                    int c1 = bacaAngka(input, "Bilangan ketiga  : "); /* get_c1 */
                    System.out.println("Nilai terbesar : " + pencari.findMax(a1, b1, c1)); // max_out
                    break; /* brk1 */
                case 2: // case_min
                    int a2 = bacaAngka(input, "Bilangan pertama : "); /* get_a2 */
                    int b2 = bacaAngka(input, "Bilangan kedua   : "); // get_b2
                    int c2 = bacaAngka(input, "Bilangan ketiga  : "); /* get_c2 */
                    System.out.println("Nilai terkecil : " + pencari.findMin(a2, b2, c2)); // min_out
                    break; /* brk2 */
                case 3: // case_avg
                    int a3 = bacaAngka(input, "Bilangan pertama : "); /* get_a3 */
                    int b3 = bacaAngka(input, "Bilangan kedua   : "); // get_b3
                    int c3 = bacaAngka(input, "Bilangan ketiga  : "); /* get_c3 */
                    System.out.println("Rata-rata : " + pencari.hitungRataRata(a3, b3, c3)); // avg_out
                    break; /* brk3 */
                case 4: // case_sort
                    int a4 = bacaAngka(input, "Bilangan pertama : "); /* get_a4 */
                    int b4 = bacaAngka(input, "Bilangan kedua   : "); // get_b4
                    int c4 = bacaAngka(input, "Bilangan ketiga  : "); /* get_c4 */
                    pencari.tampilkanUrut(a4, b4, c4); // sort_out
                    break; /* brk4 */
                case 5: // case_arr
                    int jumlah = bacaAngka(input, "Jumlah data : "); /* get_cnt */
                    if (jumlah <= 0) { // bad_cnt
                        System.out.println("Jumlah data harus lebih dari 0!"); /* warn */
                    } else { /* valid_cnt */
                        int[] data = new int[jumlah]; // alloc_arr
                        for (int i = 0; i < jumlah; i++) { /* fill_loop */
                            data[i] = bacaAngka(input, "Data ke-" + (i + 1) + " : "); // store_elem
                        }
                        System.out.println("Nilai terbesar : " + pencari.findMaxArray(data)); /* max_arr_out */
                    }
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