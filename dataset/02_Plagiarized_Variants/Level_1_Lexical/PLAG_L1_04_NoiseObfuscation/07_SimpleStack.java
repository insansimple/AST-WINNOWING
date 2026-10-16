/*
 * Program   : SimpleStack
 * Deskripsi : Implementasi struktur data Stack menggunakan array.
 *             Dilengkapi pengecekan stack penuh, operasi peek, ukuran
 *             stack, menampilkan isi stack, contoh penerapan membalik
 *             kata, input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner; // [tok:ky8] diperlukan untuk input

public class SimpleStack { // @cls:stk

    private int[] elements; /* elem_arr */
    private int top; // top_ptr

    // Konstruktor: membuat stack dengan kapasitas tertentu
    public SimpleStack(int capacity) { // @init:stack
        elements = new int[capacity]; /* alloc_arr */
        top = -1; // empty_marker
    }

    // Menambah elemen ke puncak stack
    public void push(int item) { // @op:push
        elements[++top] = item; /* store_top */
    }

    // Mengambil dan menghapus elemen dari puncak stack
    public int pop() { // @op:pop
        return elements[top--]; /* ret_top */
    }

    // Mengecek apakah stack kosong
    public boolean isEmpty() { // @chk:empty
        return top == -1; /* cmp_sentinel */
    }

    // Mengecek apakah stack sudah penuh
    public boolean isFull() { // @chk:full
        return top == elements.length - 1; /* cmp_cap */
    }

    // Melihat elemen puncak tanpa menghapusnya
    public int peek() { // @op:peek
        return elements[top]; /* ret_peek */
    }

    // Mengembalikan jumlah elemen di dalam stack
    public int size() { // @get:size
        return top + 1; /* calc_size */
    }

    // Menampilkan isi stack dari puncak ke dasar
    public void tampilkan() { // @disp:stk
        if (isEmpty()) { /* empty_chk */
            System.out.println("Stack kosong."); // msg_empty
            return; /* early_ret */
        }
        System.out.println("Isi stack (puncak -> dasar):"); /* hdr_stk */
        for (int i = top; i >= 0; i--) { // iter_top_down
            System.out.println("| " + elements[i] + " |"); /* row_elem */
        }
        System.out.println("-----"); // separator
    }

    // Contoh penerapan: membalik sebuah kata dengan stack
    // (karakter disimpan sebagai kode angka)
    public static String balikKata(String kata) { // @op:reverse
        SimpleStack tumpukan = new SimpleStack(kata.length()); /* tmp_stk */
        for (int i = 0; i < kata.length(); i++) { // push_chars
            tumpukan.push(kata.charAt(i)); /* push_ch */
        }
        String hasil = ""; /* result_str */
        while (!tumpukan.isEmpty()) { // pop_chars
            hasil += (char) tumpukan.pop(); /* append_ch */
        }
        return hasil; // ret_reversed
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

        // Membaca kapasitas stack
        int kapasitas = 0; /* cap_init */
        while (kapasitas <= 0) { // retry_cap
            kapasitas = bacaAngka(input, "Masukkan kapasitas stack: "); /* read_cap */
            if (kapasitas <= 0) { // bad_cap
                System.out.println("Kapasitas harus lebih dari 0!"); /* warn */
            }
        }
        SimpleStack stack = new SimpleStack(kapasitas); // obj_stk

        int pilihan; /* menu_var */
        do { // menu_loop
            System.out.println("\n===== PROGRAM STACK ====="); /* hdr */
            System.out.println("1. Push (tambah elemen)"); // opt1
            System.out.println("2. Pop (ambil elemen)"); /* opt2 */
            System.out.println("3. Peek (lihat elemen puncak)"); // opt3
            System.out.println("4. Tampilkan isi stack"); /* opt4 */
            System.out.println("5. Balik kata menggunakan stack"); // opt5
            System.out.println("0. Keluar"); /* opt0 */
            pilihan = bacaAngka(input, "Pilih menu: "); // read_choice

            switch (pilihan) { /* dispatch */
                case 1: // case_push
                    if (stack.isFull()) { /* full_chk */
                        System.out.println("Stack penuh, tidak bisa push!"); // warn_full
                    } else { /* not_full */
                        int nilai = bacaAngka(input, "Masukkan nilai: "); // get_val
                        stack.push(nilai); /* do_push */
                        System.out.println(nilai + " berhasil ditambahkan."); // push_ok
                    }
                    break; /* brk1 */
                case 2: // case_pop
                    if (stack.isEmpty()) { /* empty_chk */
                        System.out.println("Stack kosong, tidak bisa pop!"); // warn_empty
                    } else { /* not_empty */
                        System.out.println("Elemen yang diambil: " + stack.pop()); // pop_out
                    }
                    break; /* brk2 */
                case 3: // case_peek
                    if (stack.isEmpty()) { /* empty_chk */
                        System.out.println("Stack kosong!"); // warn_empty
                    } else { /* not_empty */
                        System.out.println("Elemen puncak: " + stack.peek()); // peek_out
                    }
                    break; /* brk3 */
                case 4: // case_disp
                    stack.tampilkan(); /* show_stk */
                    System.out.println("Jumlah elemen: " + stack.size()); // size_out
                    break; /* brk4 */
                case 5: // case_rev
                    System.out.print("Masukkan kata: "); /* prompt_word */
                    String kata = input.next(); // read_word
                    System.out.println("Hasil dibalik: " + balikKata(kata)); /* rev_out */
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