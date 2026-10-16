/*
 * Program   : BankQueue
 * Deskripsi : Simulasi antrean nasabah di bank menggunakan array.
 *             Dilengkapi pengecekan antrean kosong dan penuh, melihat
 *             nasabah terdepan, mencari posisi nasabah, menampilkan
 *             daftar antrean, input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner; // [tok:bq8] io module

public class BankQueue { // @cls:bq

    private String[] antrean; /* q_arr */
    private int jumlah; // q_cnt

    // Konstruktor: membuat antrean dengan kapasitas tertentu
    public BankQueue(int kapasitas) { // @init:bq
        antrean = new String[kapasitas]; /* alloc_arr */
        jumlah = 0; // init_cnt
    }

    // Logika inti: menambah nasabah di belakang antrean
    public void tambahNasabah(String nama) { // @op:enqueue
        if (jumlah < antrean.length) { /* cap_chk */
            antrean[jumlah++] = nama; // store_rear
        }
    }

    // Logika inti: melayani nasabah terdepan lalu menggeser antrean
    public String layaniNasabah() { // @op:dequeue
        if (jumlah == 0) return null; /* empty_chk */
        String nasabah = antrean[0]; /* read_front */
        for (int i = 0; i < jumlah - 1; i++) { // shift_loop
            antrean[i] = antrean[i + 1]; /* shift_elem */
        }
        jumlah--; /* dec_cnt */
        return nasabah; // ret_served
    }

    // Mengecek apakah antrean kosong
    public boolean isKosong() { // @chk:empty
        return jumlah == 0; /* cnt_zero */
    }

    // Mengecek apakah antrean sudah penuh
    public boolean isPenuh() { // @chk:full
        return jumlah == antrean.length; /* cnt_cap */
    }

    // Melihat nasabah terdepan tanpa melayaninya
    public String lihatDepan() { // @op:peek
        if (jumlah == 0) return null; /* empty_chk */
        return antrean[0]; /* ret_front */
    }

    // Mengembalikan jumlah nasabah yang sedang mengantre
    public int getJumlah() { // @get:count
        return jumlah; /* ret_cnt */
    }

    // Mencari posisi nasabah dalam antrean (1 = paling depan), -1 jika tidak ada
    public int cariPosisi(String nama) { // @op:find_pos
        for (int i = 0; i < jumlah; i++) { /* scan_q */
            if (antrean[i].equalsIgnoreCase(nama)) { // name_match
                return i + 1; /* ret_pos */
            }
        }
        return -1; // not_found
    }

    // Menampilkan daftar antrean dari depan ke belakang
    public void tampilkanAntrean() { // @disp:q
        if (isKosong()) { /* empty_chk */
            System.out.println("Antrean kosong."); // msg_empty
            return; /* early_ret */
        }
        System.out.println("Daftar antrean:"); /* hdr_q */
        for (int i = 0; i < jumlah; i++) { // iter_q
            System.out.println((i + 1) + ". " + antrean[i]); /* row_out */
        }
    }

    // Membaca bilangan bulat dengan validasi sederhana
    public static int bacaAngka(Scanner input, String pesan) { // @read:int
        System.out.print(pesan); /* prompt */
        while (!input.hasNextInt()) { // guard
            System.out.print("Input harus angka bulat, ulangi: "); /* retry */
            input.next(); /* flush */
        }
        int nilai = input.nextInt(); /* read_val */
        input.nextLine(); // buang sisa baris
        return nilai; // ret_val
    }

    // Membaca nama yang tidak boleh kosong
    public static String bacaNama(Scanner input, String pesan) { // @read:name
        String nama = ""; /* name_init */
        while (nama.trim().isEmpty()) { // retry_nonempty
            System.out.print(pesan); /* prompt */
            nama = input.nextLine(); // read_name
            if (nama.trim().isEmpty()) { /* empty_chk */
                System.out.println("Nama tidak boleh kosong!"); // warn_empty
            }
        }
        return nama.trim(); /* ret_trimmed */
    }

    public static void main(String[] args) { // @entry:main
        Scanner input = new Scanner(System.in); /* io_init */

        // Membaca kapasitas antrean
        int kapasitas = 0; /* cap_init */
        while (kapasitas <= 0) { // retry_cap
            kapasitas = bacaAngka(input, "Masukkan kapasitas antrean: "); /* read_cap */
            if (kapasitas <= 0) { // bad_cap
                System.out.println("Kapasitas harus lebih dari 0!"); /* warn */
            }
        }
        BankQueue bank = new BankQueue(kapasitas); // obj_bq

        int pilihan; /* menu_var */
        do { // menu_loop
            System.out.println("\n===== ANTREAN BANK ====="); /* hdr */
            System.out.println("1. Tambah nasabah"); // opt1
            System.out.println("2. Layani nasabah"); /* opt2 */
            System.out.println("3. Lihat nasabah terdepan"); // opt3
            System.out.println("4. Cari posisi nasabah"); /* opt4 */
            System.out.println("5. Tampilkan daftar antrean"); // opt5
            System.out.println("0. Keluar"); /* opt0 */
            pilihan = bacaAngka(input, "Pilih menu: "); // read_choice

            switch (pilihan) { /* dispatch */
                case 1: // case_add
                    if (bank.isPenuh()) { /* full_chk */
                        System.out.println("Antrean penuh, nasabah tidak dapat ditambah!"); // warn_full
                    } else { /* not_full */
                        String nama = bacaNama(input, "Nama nasabah: "); // get_name
                        bank.tambahNasabah(nama); /* do_enqueue */
                        System.out.println(nama + " masuk antrean nomor " + bank.getJumlah()); // enq_ok
                    }
                    break; /* brk1 */
                case 2: // case_serve
                    if (bank.isKosong()) { /* empty_chk */
                        System.out.println("Antrean kosong, tidak ada yang dilayani!"); // warn_empty
                    } else { /* not_empty */
                        System.out.println("Sedang melayani: " + bank.layaniNasabah()); // serve_out
                        System.out.println("Sisa antrean   : " + bank.getJumlah()); /* remain_out */
                    }
                    break; /* brk2 */
                case 3: // case_peek
                    if (bank.isKosong()) { /* empty_chk */
                        System.out.println("Antrean kosong!"); // warn_empty
                    } else { /* not_empty */
                        System.out.println("Nasabah terdepan: " + bank.lihatDepan()); // peek_out
                    }
                    break; /* brk3 */
                case 4: // case_find
                    String cari = bacaNama(input, "Nama yang dicari: "); /* get_search_name */
                    int posisi = bank.cariPosisi(cari); // do_search
                    if (posisi == -1) { /* not_found */
                        System.out.println(cari + " tidak ada dalam antrean."); // msg_nf
                    } else { /* found */
                        System.out.println(cari + " berada di posisi " + posisi); /* msg_f */
                    }
                    break; /* brk4 */
                case 5: // case_list
                    bank.tampilkanAntrean(); /* show_q */
                    System.out.println("Jumlah nasabah: " + bank.getJumlah()); // cnt_out
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