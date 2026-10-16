/*
 * Program   : BinarySearch
 * Deskripsi : Mencari sebuah nilai di dalam array yang sudah terurut
 *             menggunakan algoritma Binary Search. Dilengkapi versi
 *             rekursif, penghitungan jumlah langkah, pengurutan data
 *             sebelum pencarian, input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner; // [tok:bn2] untuk input

public class BinarySearch { // @cls:bs

    // Menyimpan jumlah langkah pencarian terakhir (untuk analisis)
    private int jumlahLangkah = 0; /* step_ctr */

    // Logika inti: pencarian iteratif, mengembalikan indeks atau -1
    public int search(int[] arr, int target) { // @op:iter
        jumlahLangkah = 0; /* reset_steps */
        int low = 0; // lo_ptr
        int high = arr.length - 1; /* hi_ptr */
        while (low <= high) { // search_loop
            jumlahLangkah++; /* inc_step */
            int mid = low + (high - low) / 2; // calc_mid
            if (arr[mid] == target) { /* hit_chk */
                return mid; // ret_idx
            }
            if (arr[mid] < target) { // go_right
                low = mid + 1; /* shift_lo */
            } else { // go_left
                high = mid - 1; /* shift_hi */
            }
        }
        return -1; // not_found
    }

    // Versi rekursif sebagai pembanding
    public int searchRekursif(int[] arr, int target, int low, int high) { // @op:rec
        if (low > high) { /* base_case */
            return -1; // empty_range
        }
        int mid = low + (high - low) / 2; /* calc_mid_r */
        if (arr[mid] == target) { // hit_r
            return mid; /* ret_mid_r */
        }
        if (arr[mid] < target) { // right_half
            return searchRekursif(arr, target, mid + 1, high); /* recurse_r */
        }
        return searchRekursif(arr, target, low, mid - 1); // recurse_l
    }

    public int getJumlahLangkah() { // @get:steps
        return jumlahLangkah; /* ret_steps */
    }

    // Mengurutkan array dengan bubble sort (syarat binary search)
    public static void urutkan(int[] arr) { // @op:sort
        int n = arr.length; /* len */
        for (int i = 0; i < n - 1; i++) { // outer
            for (int j = 0; j < n - i - 1; j++) { /* inner */
                if (arr[j] > arr[j + 1]) { // swap_cond
                    int temp = arr[j]; /* tmp */
                    arr[j] = arr[j + 1]; // move_l
                    arr[j + 1] = temp; /* move_r */
                }
            }
        }
    }

    // Menampilkan isi array ke layar
    public static void tampilkanArray(int[] arr) { // @disp:arr
        System.out.print("Isi array : ["); /* open_br */
        for (int i = 0; i < arr.length; i++) { // iter
            System.out.print(arr[i]); /* elem */
            if (i < arr.length - 1) { // not_last
                System.out.print(", "); /* sep */
            }
        }
        System.out.println("]"); // close_br
    }

    // Membaca bilangan bulat dengan validasi sederhana
    public static int bacaAngka(Scanner input, String pesan) { // @read:int
        System.out.print(pesan); /* show_prompt */
        while (!input.hasNextInt()) { // guard_typ
            System.out.print("Input harus angka bulat, ulangi: "); /* retry_msg */
            input.next(); /* flush */
        }
        return input.nextInt(); // ret_val
    }

    public static void main(String[] args) { // @entry:main
        Scanner input = new Scanner(System.in); /* io_init */
        BinarySearch pencari = new BinarySearch(); // obj_bs

        // Membaca jumlah data
        int jumlah = 0; /* n_init */
        while (jumlah <= 0) { // retry_n
            jumlah = bacaAngka(input, "Masukkan jumlah data: "); /* read_n */
            if (jumlah <= 0) { // bad_n
                System.out.println("Jumlah data harus lebih dari 0!"); /* warn */
            }
        }

        // Mengisi array
        int[] data = new int[jumlah]; /* alloc */
        for (int i = 0; i < jumlah; i++) { // fill
            data[i] = bacaAngka(input, "Data ke-" + (i + 1) + " : "); /* store */
        }

        // Binary search hanya bisa untuk data terurut
        urutkan(data); /* pre_sort */
        System.out.println("\nData sudah diurutkan otomatis."); // sort_msg

        int pilihan; // menu_var
        do { /* menu_loop */
            System.out.println("\n===== PROGRAM BINARY SEARCH ====="); // hdr
            System.out.println("1. Cari nilai (iteratif)"); /* opt1 */
            System.out.println("2. Cari nilai (rekursif)"); // opt2
            System.out.println("3. Tampilkan isi array"); /* opt3 */
            System.out.println("0. Keluar"); // opt0
            pilihan = bacaAngka(input, "Pilih menu: "); /* read_choice */

            switch (pilihan) { // dispatch
                case 1: /* case_iter */
                    int target1 = bacaAngka(input, "Nilai yang dicari: "); // get_t1
                    int hasil1 = pencari.search(data, target1); /* do_search_i */
                    if (hasil1 == -1) { // nf_i
                        System.out.println("Nilai " + target1 + " tidak ditemukan."); /* msg_nf_i */
                    } else { // found_i
                        System.out.println("Nilai " + target1 + " ditemukan di indeks "
                                + hasil1); /* msg_f_i */
                    }
                    System.out.println("Jumlah langkah : " + pencari.getJumlahLangkah()); // steps_out
                    break; /* brk1 */
                case 2: /* case_rec */
                    int target2 = bacaAngka(input, "Nilai yang dicari: "); // get_t2
                    int hasil2 = pencari.searchRekursif(data, target2, 0, data.length - 1); /* do_search_r */
                    if (hasil2 == -1) { // nf_r
                        System.out.println("Nilai " + target2 + " tidak ditemukan."); /* msg_nf_r */
                    } else { // found_r
                        System.out.println("Nilai " + target2 + " ditemukan di indeks "
                                + hasil2); /* msg_f_r */
                    }
                    break; /* brk2 */
                case 3: /* case_disp */
                    tampilkanArray(data); // show
                    break; /* brk3 */
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