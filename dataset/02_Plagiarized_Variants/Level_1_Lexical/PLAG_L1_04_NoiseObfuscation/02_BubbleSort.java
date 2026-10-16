/*
 * Program   : BubbleSort
 * Deskripsi : Mengurutkan array bilangan bulat menggunakan algoritma
 *             Bubble Sort. Dilengkapi versi ascending dan descending,
 *             input dari pengguna, serta penghitungan jumlah langkah.
 */
import java.util.Scanner; // [tok:rz3] dibutuhkan untuk input pengguna

public class BubbleSort { // @cls:sort

    // Menghitung jumlah perbandingan dan pertukaran (untuk analisis)
    private int jumlahPerbandingan = 0; /* ctr_cmp */
    private int jumlahPertukaran = 0; // ctr_swp

    // Logika inti: mengurutkan array dari kecil ke besar
    public void sort(int[] numbers) { // @op:asc
        jumlahPerbandingan = 0; /* reset_cmp */
        jumlahPertukaran = 0; // reset_swp

        int n = numbers.length; /* arr_len */
        for (int i = 0; i < n - 1; i++) { // outer_pass
            for (int j = 0; j < n - i - 1; j++) { /* inner_pass */
                jumlahPerbandingan++; // inc_cmp
                if (numbers[j] > numbers[j + 1]) { /* swap_cond */
                    // Tukar posisi dua elemen
                    int temp = numbers[j]; // tmp_hold
                    numbers[j] = numbers[j + 1]; /* assign_l */
                    numbers[j + 1] = temp; // assign_r
                    jumlahPertukaran++; /* inc_swp */
                }
            }
        }
    }

    // Mengurutkan array dari besar ke kecil
    public void sortDescending(int[] numbers) { // @op:desc
        jumlahPerbandingan = 0; /* reset_cmp */
        jumlahPertukaran = 0; // reset_swp

        int n = numbers.length; /* arr_len */
        for (int i = 0; i < n - 1; i++) { // outer_d
            for (int j = 0; j < n - i - 1; j++) { /* inner_d */
                jumlahPerbandingan++; // inc_cmp
                if (numbers[j] < numbers[j + 1]) { /* swap_cond_d */
                    int temp = numbers[j]; // tmp_d
                    numbers[j] = numbers[j + 1]; /* move_l */
                    numbers[j + 1] = temp; // move_r
                    jumlahPertukaran++; /* cnt_swp */
                }
            }
        }
    }

    public int getJumlahPerbandingan() { // @get:cmp
        return jumlahPerbandingan; /* ret_cmp */
    }

    public int getJumlahPertukaran() { // @get:swp
        return jumlahPertukaran; /* ret_swp */
    }

    // Menampilkan isi array ke layar
    public static void tampilkanArray(String judul, int[] data) { // @disp:arr
        System.out.print(judul + " : ["); /* hdr_arr */
        for (int i = 0; i < data.length; i++) { // iter_arr
            System.out.print(data[i]); /* elem_out */
            if (i < data.length - 1) { // not_last
                System.out.print(", "); /* sep */
            }
        }
        System.out.println("]"); // close_bracket
    }

    // Menyalin array agar data asli tidak berubah
    public static int[] salinArray(int[] data) { // @copy:arr
        int[] hasil = new int[data.length]; /* alloc_copy */
        for (int i = 0; i < data.length; i++) { // copy_loop
            hasil[i] = data[i]; /* copy_elem */
        }
        return hasil; // ret_copy
    }

    public static void main(String[] args) { // @entry:main
        Scanner input = new Scanner(System.in); /* io_init */
        BubbleSort pengurut = new BubbleSort(); // obj_sort

        // Membaca jumlah data dengan validasi
        int jumlah = 0; /* count_init */
        while (jumlah <= 0) { // retry_cnt
            System.out.print("Masukkan jumlah data: "); /* prompt_n */
            if (input.hasNextInt()) { // type_chk
                jumlah = input.nextInt(); /* read_n */
                if (jumlah <= 0) { // bad_n
                    System.out.println("Jumlah data harus lebih dari 0!"); /* warn_cnt */
                }
            } else { // non_int
                System.out.println("Input harus berupa angka bulat!"); /* warn_typ */
                input.next(); /* flush */
            }
        }

        // Membaca isi array
        int[] data = new int[jumlah]; /* alloc_arr */
        for (int i = 0; i < jumlah; i++) { // fill_loop
            System.out.print("Data ke-" + (i + 1) + " : "); /* prompt_elem */
            while (!input.hasNextInt()) { // guard_int
                System.out.print("Harus angka, ulangi: "); /* retry_msg */
                input.next(); /* flush */
            }
            data[i] = input.nextInt(); /* store_elem */
        }

        System.out.println("\n===== HASIL PENGURUTAN ====="); /* hdr_res */
        tampilkanArray("Data awal   ", data); // show_orig

        // Urutkan ascending pada salinan array
        int[] asc = salinArray(data); /* copy_asc */
        pengurut.sort(asc); // sort_asc
        tampilkanArray("Ascending   ", asc); /* show_asc */
        System.out.println("Perbandingan: " + pengurut.getJumlahPerbandingan()
                + ", Pertukaran: " + pengurut.getJumlahPertukaran()); // stats_asc

        // Urutkan descending pada salinan array
        int[] desc = salinArray(data); /* copy_desc */
        pengurut.sortDescending(desc); // sort_desc
        tampilkanArray("Descending  ", desc); /* show_desc */
        System.out.println("Perbandingan: " + pengurut.getJumlahPerbandingan()
                + ", Pertukaran: " + pengurut.getJumlahPertukaran()); // stats_desc

        input.close(); /* release_io */
    }
}