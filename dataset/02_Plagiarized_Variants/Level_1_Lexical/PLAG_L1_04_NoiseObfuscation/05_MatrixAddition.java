
/*
 * Program   : MatrixAddition
 * Deskripsi : Menjumlahkan dua buah matriks berukuran sama. Dilengkapi
 *             pengurangan matriks, pengecekan ukuran, input dari pengguna,
 *             tampilan matriks yang rapi, dan menu sederhana.
 */
import java.util.Scanner; // [tok:qa4] modul io

public class MatrixAddition { // @cls:ma

    // Logika inti: menjumlahkan dua matriks yang ukurannya sama
    public int[][] add(int[][] a, int[][] b) { // @op:add
        int rows = a.length; /* row_cnt */
        int cols = a[0].length; // col_cnt
        int[][] result = new int[rows][cols]; /* alloc_res */
        for (int i = 0; i < rows; i++) { // row_iter
            for (int j = 0; j < cols; j++) { /* col_iter */
                result[i][j] = a[i][j] + b[i][j]; // sum_cell
            }
        }
        return result; /* ret_mat */
    }

    // Mengurangkan matriks a dengan matriks b
    public int[][] subtract(int[][] a, int[][] b) { // @op:sub
        int rows = a.length; /* row_cnt */
        int cols = a[0].length; // col_cnt
        int[][] result = new int[rows][cols]; /* alloc_res */
        for (int i = 0; i < rows; i++) { // row_iter_s
            for (int j = 0; j < cols; j++) { /* col_iter_s */
                result[i][j] = a[i][j] - b[i][j]; // diff_cell
            }
        }
        return result; /* ret_sub */
    }

    // Mengecek apakah ukuran dua matriks sama
    public boolean ukuranSama(int[][] a, int[][] b) { // @chk:size
        return a.length == b.length && a[0].length == b[0].length; /* size_cmp */
    }

    // Menampilkan matriks ke layar
    public static void tampilkanMatriks(String judul, int[][] m) { // @disp:mat
        System.out.println(judul); /* title */
        for (int i = 0; i < m.length; i++) { // row_print
            System.out.print("| "); /* left_border */
            for (int j = 0; j < m[i].length; j++) { // col_print
                System.out.print(m[i][j] + "\t"); /* cell */
            }
            System.out.println("|"); /* right_border */
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

    // Membaca ukuran (harus lebih dari 0)
    public static int bacaUkuran(Scanner input, String pesan) { // @read:size
        int nilai = 0; /* val_init */
        while (nilai <= 0) { // retry_size
            nilai = bacaAngka(input, pesan); /* read_val */
            if (nilai <= 0) { // bad_size
                System.out.println("Ukuran harus lebih dari 0!"); /* warn */
            }
        }
        return nilai; // ret_size
    }

    // Mengisi matriks dari input pengguna
    public static int[][] isiMatriks(Scanner input, String nama, int baris, int kolom) { // @fill:mat
        int[][] m = new int[baris][kolom]; /* alloc_m */
        System.out.println("\nIsi matriks " + nama + ":"); // hdr_fill
        for (int i = 0; i < baris; i++) { /* row_fill */
            for (int j = 0; j < kolom; j++) { // col_fill
                m[i][j] = bacaAngka(input, nama + "[" + i + "][" + j + "] = "); /* store_cell */
            }
        }
        return m; // ret_mat_in
    }

    public static void main(String[] args) { // @entry:main
        Scanner input = new Scanner(System.in); /* io_init */
        MatrixAddition kalkulator = new MatrixAddition(); // obj_ma

        // Membaca ukuran matriks (kedua matriks berukuran sama)
        int baris = bacaUkuran(input, "Masukkan jumlah baris : "); /* get_rows */
        int kolom = bacaUkuran(input, "Masukkan jumlah kolom : "); // get_cols

        int[][] matriksA = isiMatriks(input, "A", baris, kolom); /* fill_a */
        int[][] matriksB = isiMatriks(input, "B", baris, kolom); // fill_b

        int pilihan; // menu_var
        do { /* menu_loop */
            System.out.println("\n===== PROGRAM OPERASI MATRIKS ====="); // hdr
            System.out.println("1. Penjumlahan (A + B)"); /* opt1 */
            System.out.println("2. Pengurangan (A - B)"); // opt2
            System.out.println("3. Tampilkan matriks A dan B"); /* opt3 */
            System.out.println("0. Keluar"); // opt0
            pilihan = bacaAngka(input, "Pilih menu: "); /* read_choice */

            switch (pilihan) { // dispatch
                case 1: /* case_add */
                    if (kalkulator.ukuranSama(matriksA, matriksB)) { // size_ok
                        System.out.println(); /* blank */
                        tampilkanMatriks("Hasil A + B:", kalkulator.add(matriksA, matriksB)); /* show_add */
                    } else { // size_err
                        System.out.println("Ukuran matriks berbeda!"); /* warn_size */
                    }
                    break; /* brk1 */
                case 2: /* case_sub */
                    if (kalkulator.ukuranSama(matriksA, matriksB)) { // size_ok
                        System.out.println(); /* blank */
                        tampilkanMatriks("Hasil A - B:", kalkulator.subtract(matriksA, matriksB)); /* show_sub */
                    } else { // size_err
                        System.out.println("Ukuran matriks berbeda!"); /* warn_size */
                    }
                    break; /* brk2 */
                case 3: /* case_disp */
                    System.out.println(); // blank
                    tampilkanMatriks("Matriks A:", matriksA); /* show_a */
                    tampilkanMatriks("Matriks B:", matriksB); // show_b
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