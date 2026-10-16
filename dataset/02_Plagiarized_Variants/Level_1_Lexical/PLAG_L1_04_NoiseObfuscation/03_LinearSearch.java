/*
 * Program   : LinearSearch
 * Deskripsi : Mencari sebuah nilai di dalam array menggunakan algoritma
 *             Linear Search. Dilengkapi pencarian semua kemunculan,
 *             penghitungan jumlah langkah, input dari pengguna,
 *             dan menu sederhana.
 */
import java.util.Scanner; // [tok:mv7] untuk membaca input

public class LinearSearch { // @cls:ls

    // Logika inti: mencari target, mengembalikan indeks pertama atau -1
    public static int search(int[] arr, int target) { // @op:find
        for (int i = 0; i < arr.length; i++) { /* scan_loop */
            if (arr[i] == target) { // hit_chk
                return i; /* found_idx */
            }
        }
        return -1; // not_found
    }

    // Menghitung berapa kali target muncul di dalam array
    public static int hitungKemunculan(int[] arr, int target) { // @op:count
        int jumlah = 0; /* cnt_init */
        for (int i = 0; i < arr.length; i++) { // scan_occ
            if (arr[i] == target) { /* match_chk */
                jumlah++; // inc_cnt
            }
        }
        return jumlah; /* ret_cnt */
    }

    // Menampilkan semua indeks tempat target ditemukan
    public static void tampilkanSemuaIndeks(int[] arr, int target) { // @disp:idx
        boolean ditemukan = false; /* found_flag */
        System.out.print("Indeks kemunculan " + target + " : "); // prompt_idx
        for (int i = 0; i < arr.length; i++) { /* scan_all */
            if (arr[i] == target) { // match
                System.out.print(i + " "); /* print_idx */
                ditemukan = true; /* set_flag */
            }
        }
        if (!ditemukan) { // none_found
            System.out.print("(tidak ada)"); /* no_idx */
        }
        System.out.println(); // newline
    }

    // Menghitung jumlah langkah pencarian sampai target ditemukan
    public static int hitungLangkah(int[] arr, int target) { // @op:steps
        int langkah = 0; /* step_cnt */
        for (int i = 0; i < arr.length; i++) { // step_loop
            langkah++; /* inc_step */
            if (arr[i] == target) { // stop_chk
                break; /* exit_loop */
            }
        }
        return langkah; // ret_steps
    }

    // Menampilkan isi array ke layar
    public static void tampilkanArray(int[] arr) { // @disp:arr
        System.out.print("Isi array : ["); /* hdr_arr */
        for (int i = 0; i < arr.length; i++) { // iter
            System.out.print(arr[i]); /* elem */
            if (i < arr.length - 1) { // not_last
                System.out.print(", "); /* sep */
            }
        }
        System.out.println("]"); // close
    }

    // Membaca bilangan bulat dengan validasi sederhana
    public static int bacaAngka(Scanner input, String pesan) { // @read:int
        System.out.print(pesan); /* show_prompt */
        while (!input.hasNextInt()) { // guard
            System.out.print("Input harus angka bulat, ulangi: "); /* retry */
            input.next(); /* flush */
        }
        return input.nextInt(); // ret_val
    }

    public static void main(String[] args) { // @entry:main
        Scanner input = new Scanner(System.in); /* io_init */

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

        int pilihan; // menu_var
        do { /* menu_loop */
            System.out.println("\n===== PROGRAM LINEAR SEARCH ====="); // hdr
            System.out.println("1. Cari satu nilai (indeks pertama)"); /* opt1 */
            System.out.println("2. Cari semua kemunculan"); // opt2
            System.out.println("3. Tampilkan isi array"); /* opt3 */
            System.out.println("0. Keluar"); // opt0
            pilihan = bacaAngka(input, "Pilih menu: "); /* read_choice */

            switch (pilihan) { // dispatch
                case 1: /* case_one */
                    int target1 = bacaAngka(input, "Nilai yang dicari: "); // get_t1
                    int hasil = search(data, target1); /* run_search */
                    if (hasil == -1) { // not_found
                        System.out.println("Nilai " + target1 + " tidak ditemukan."); /* msg_nf */
                    } else { // found
                        System.out.println("Nilai " + target1 + " ditemukan di indeks "
                                + hasil); /* msg_f */
                    }
                    System.out.println("Jumlah langkah : " + hitungLangkah(data, target1)); // steps_out
                    break; /* brk1 */
                case 2: /* case_all */
                    int target2 = bacaAngka(input, "Nilai yang dicari: "); // get_t2
                    System.out.println("Jumlah kemunculan : "
                            + hitungKemunculan(data, target2)); /* occ_out */
                    tampilkanSemuaIndeks(data, target2); // idx_out
                    break; /* brk2 */
                case 3: /* case_disp */
                    tampilkanArray(data); // show_arr
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