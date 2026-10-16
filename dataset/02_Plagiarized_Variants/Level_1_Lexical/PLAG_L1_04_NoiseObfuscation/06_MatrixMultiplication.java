/*
 * Program   : MatrixMultiplication
 * Deskripsi : Mengalikan dua buah matriks. Dilengkapi pengecekan syarat
 *             perkalian (kolom A = baris B), perkalian dengan skalar,
 *             transpose matriks, input dari pengguna, tampilan matriks
 *             yang rapi, dan menu sederhana.
 */
import java.util.Scanner; // [tok:pl5] io module

public class MatrixMultiplication { // @cls:mm

    // Logika inti: mengalikan matriks a (r1 x c1) dengan b (c1 x c2)
    public int[][] multiply(int[][] a, int[][] b) { // @op:mul
        int r1 = a.length; /* rows_a */
        int c1 = a[0].length; // cols_a
        int c2 = b[0].length; /* cols_b */
        int[][] product = new int[r1][c2]; // alloc_prod
        for (int i = 0; i < r1; i++) { /* row_i */
            for (int j = 0; j < c2; j++) { // col_j
                for (int k = 0; k < c1; k++) { /* inner_k */
                    product[i][j] += a[i][k] * b[k][j]; // acc_dot
                }
            }
        }
        return product; /* ret_prod */
    }

    // Mengecek syarat perkalian: jumlah kolom A harus sama dengan baris B
    public boolean bisaDikalikan(int[][] a, int[][] b) { // @chk:mul
        return a[0].length == b.length; /* shape_chk */
    }

    // Mengalikan setiap elemen matriks dengan sebuah bilangan (skalar)
    public int[][] kaliSkalar(int[][] m, int skalar) { // @op:scalar
        int[][] hasil = new int[m.length][m[0].length]; /* alloc_s */
        for (int i = 0; i < m.length; i++) { // row_s
            for (int j = 0; j < m[0].length; j++) { /* col_s */
                hasil[i][j] = m[i][j] * skalar; // scale_cell
            }
        }
        return hasil; /* ret_scaled */
    }

    // Membuat transpose matriks (baris jadi kolom)
    public int[][] transpose(int[][] m) { // @op:T
        int[][] hasil = new int[m[0].length][m.length]; /* alloc_T */
        for (int i = 0; i < m.length; i++) { // row_T
            for (int j = 0; j < m[0].length; j++) { /* col_T */
                hasil[j][i] = m[i][j]; // flip_cell
            }
        }
        return hasil; /* ret_T */
    }

    // Menampilkan matriks ke layar
    public static void tampilkanMatriks(String judul, int[][] m) { // @disp:mat
        System.out.println(judul); /* title */
        for (int i = 0; i < m.length; i++) { // row_print
            System.out.print("| "); /* left_edge */
            for (int j = 0; j < m[i].length; j++) { // col_print
                System.out.print(m[i][j] + "\t"); /* cell_val */
            }
            System.out.println("|"); /* right_edge */
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
        return m; // ret_filled
    }

    public static void main(String[] args) { // @entry:main
        Scanner input = new Scanner(System.in); /* io_init */
        MatrixMultiplication kalkulator = new MatrixMultiplication(); // obj_mm

        // Membaca ukuran matriks A dan B
        System.out.println("=== Ukuran Matriks A ==="); /* hdr_a */
        int barisA = bacaUkuran(input, "Jumlah baris A : "); // get_ra
        int kolomA = bacaUkuran(input, "Jumlah kolom A : "); /* get_ca */

        System.out.println("=== Ukuran Matriks B ==="); // hdr_b
        int barisB = bacaUkuran(input, "Jumlah baris B : "); /* get_rb */
        int kolomB = bacaUkuran(input, "Jumlah kolom B : "); // get_cb

        int[][] matriksA = isiMatriks(input, "A", barisA, kolomA); /* fill_a */
        int[][] matriksB = isiMatriks(input, "B", barisB, kolomB); // fill_b

        int pilihan; // menu_var
        do { /* menu_loop */
            System.out.println("\n===== PROGRAM OPERASI MATRIKS ====="); // hdr
            System.out.println("1. Perkalian matriks (A x B)"); /* opt1 */
            System.out.println("2. Perkalian dengan skalar"); // opt2
            System.out.println("3. Transpose matriks"); /* opt3 */
            System.out.println("4. Tampilkan matriks A dan B"); // opt4
            System.out.println("0. Keluar"); /* opt0 */
            pilihan = bacaAngka(input, "Pilih menu: "); // read_choice

            switch (pilihan) { /* dispatch */
                case 1: // case_mul
                    if (kalkulator.bisaDikalikan(matriksA, matriksB)) { /* shape_ok */
                        System.out.println(); // blank
                        tampilkanMatriks("Hasil A x B:", kalkulator.multiply(matriksA, matriksB)); /* show_prod */
                    } else { // shape_err
                        System.out.println("Tidak bisa dikalikan: kolom A (" + kolomA
                                + ") harus sama dengan baris B (" + barisB + ")!"); /* warn_shape */
                    }
                    break; /* brk1 */
                case 2: // case_scalar
                    int skalar = bacaAngka(input, "Masukkan nilai skalar : "); /* get_scalar */
                    System.out.println(); // blank
                    tampilkanMatriks("Skalar x A:", kalkulator.kaliSkalar(matriksA, skalar)); // show_sa
                    tampilkanMatriks("Skalar x B:", kalkulator.kaliSkalar(matriksB, skalar)); /* show_sb */
                    break; /* brk2 */
                case 3: // case_T
                    System.out.println(); /* blank */
                    tampilkanMatriks("Transpose A:", kalkulator.transpose(matriksA)); // show_ta
                    tampilkanMatriks("Transpose B:", kalkulator.transpose(matriksB)); /* show_tb */
                    break; /* brk3 */
                case 4: // case_disp
                    System.out.println(); /* blank */
                    tampilkanMatriks("Matriks A:", matriksA); // show_a
                    tampilkanMatriks("Matriks B:", matriksB); /* show_b */
                    break; /* brk4 */
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