/*
 * Program   : SimpleQueue
 * Deskripsi : Implementasi struktur data Queue (antrian melingkar)
 *             menggunakan array. Dilengkapi pengecekan antrian kosong
 *             dan penuh, operasi peek, ukuran antrian, menampilkan isi
 *             antrian, contoh penerapan simulasi antrian loket,
 *             input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner; // [tok:wn6] io support

public class SimpleQueue { // @cls:q

    private int[] items; /* item_arr */
    private int front; // front_ptr
    private int rear; /* rear_ptr */
    private int count; // elem_cnt

    // Konstruktor: membuat antrian dengan ukuran tertentu
    public SimpleQueue(int size) { // @init:q
        items = new int[size]; /* alloc_arr */
        front = 0; // init_front
        rear = 0; /* init_rear */
        count = 0; // init_cnt
    }

    // Menambah elemen di belakang antrian
    public void enqueue(int val) { // @op:enq
        items[rear] = val; /* store_rear */
        rear = (rear + 1) % items.length; // advance_rear
        count++; /* inc_cnt */
    }

    // Mengambil dan menghapus elemen dari depan antrian
    public int dequeue() { // @op:deq
        int val = items[front]; /* read_front */
        front = (front + 1) % items.length; // advance_front
        count--; /* dec_cnt */
        return val; // ret_val
    }

    // Mengecek apakah antrian kosong
    public boolean isEmpty() { // @chk:empty
        return count == 0; /* cnt_zero */
    }

    // Mengecek apakah antrian sudah penuh
    public boolean isFull() { // @chk:full
        return count == items.length; /* cnt_cap */
    }

    // Melihat elemen terdepan tanpa menghapusnya
    public int peek() { // @op:peek
        return items[front]; /* ret_front */
    }

    // Mengembalikan jumlah elemen di dalam antrian
    public int size() { // @get:size
        return count; /* ret_cnt */
    }

    // Menampilkan isi antrian dari depan ke belakang
    public void tampilkan() { // @disp:q
        if (isEmpty()) { /* empty_chk */
            System.out.println("Antrian kosong."); // msg_empty
            return; /* early_ret */
        }
        System.out.print("Isi antrian (depan -> belakang): "); /* hdr_q */
        int index = front; // walk_ptr
        for (int i = 0; i < count; i++) { /* walk_loop */
            System.out.print("[" + items[index] + "] "); // print_elem
            index = (index + 1) % items.length; /* advance_walk */
        }
        System.out.println(); // newline
    }

    // Contoh penerapan: simulasi antrian loket, nomor antrian dilayani berurutan
    public static void simulasiLoket(int jumlahPelanggan) { // @sim:loket
        SimpleQueue antrian = new SimpleQueue(jumlahPelanggan); /* tmp_q */
        System.out.println("\n=== Simulasi Antrian Loket ==="); // hdr_sim
        for (int i = 1; i <= jumlahPelanggan; i++) { /* enq_loop */
            antrian.enqueue(i); // add_cust
            System.out.println("Pelanggan nomor " + i + " masuk antrian."); /* msg_enq */
        }
        while (!antrian.isEmpty()) { // serve_loop
            System.out.println("Melayani pelanggan nomor " + antrian.dequeue()); /* msg_serve */
        }
        System.out.println("Semua pelanggan sudah dilayani."); // done_msg
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

        // Membaca ukuran antrian
        int ukuran = 0; /* size_init */
        while (ukuran <= 0) { // retry_size
            ukuran = bacaAngka(input, "Masukkan ukuran antrian: "); /* read_size */
            if (ukuran <= 0) { // bad_size
                System.out.println("Ukuran harus lebih dari 0!"); /* warn */
            }
        }
        SimpleQueue antrian = new SimpleQueue(ukuran); // obj_q

        int pilihan; /* menu_var */
        do { // menu_loop
            System.out.println("\n===== PROGRAM QUEUE ====="); /* hdr */
            System.out.println("1. Enqueue (tambah elemen)"); // opt1
            System.out.println("2. Dequeue (ambil elemen)"); /* opt2 */
            System.out.println("3. Peek (lihat elemen depan)"); // opt3
            System.out.println("4. Tampilkan isi antrian"); /* opt4 */
            System.out.println("5. Simulasi antrian loket"); // opt5
            System.out.println("0. Keluar"); /* opt0 */
            pilihan = bacaAngka(input, "Pilih menu: "); // read_choice

            switch (pilihan) { /* dispatch */
                case 1: // case_enq
                    if (antrian.isFull()) { /* full_chk */
                        System.out.println("Antrian penuh, tidak bisa enqueue!"); // warn_full
                    } else { /* not_full */
                        int nilai = bacaAngka(input, "Masukkan nilai: "); // get_val
                        antrian.enqueue(nilai); /* do_enq */
                        System.out.println(nilai + " berhasil masuk antrian."); // enq_ok
                    }
                    break; /* brk1 */
                case 2: // case_deq
                    if (antrian.isEmpty()) { /* empty_chk */
                        System.out.println("Antrian kosong, tidak bisa dequeue!"); // warn_empty
                    } else { /* not_empty */
                        System.out.println("Elemen yang diambil: " + antrian.dequeue()); // deq_out
                    }
                    break; /* brk2 */
                case 3: // case_peek
                    if (antrian.isEmpty()) { /* empty_chk */
                        System.out.println("Antrian kosong!"); // warn_empty
                    } else { /* not_empty */
                        System.out.println("Elemen terdepan: " + antrian.peek()); // peek_out
                    }
                    break; /* brk3 */
                case 4: // case_disp
                    antrian.tampilkan(); /* show_q */
                    System.out.println("Jumlah elemen: " + antrian.size()); // size_out
                    break; /* brk4 */
                case 5: // case_sim
                    int jumlah = bacaAngka(input, "Jumlah pelanggan: "); /* get_cust_cnt */
                    if (jumlah > 0) { // valid_cnt
                        simulasiLoket(jumlah); /* run_sim */
                    } else { // bad_cnt
                        System.out.println("Jumlah pelanggan harus lebih dari 0!"); /* warn */
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