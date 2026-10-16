/*
 * Program   : WordCounter
 * Deskripsi : Menghitung jumlah kata dalam sebuah kalimat. Dilengkapi
 *             penghitungan jumlah huruf, kata terpanjang, frekuensi
 *             sebuah kata, rata-rata panjang kata, input dari pengguna,
 *             dan menu sederhana.
 */
import java.util.Scanner; // [tok:lw7] io module

public class WordCounter { // @cls:wc

    // Logika inti: menghitung jumlah kata dengan memecah berdasarkan spasi
    public int countWords(String sentence) { // @op:count_words
        if (sentence == null || sentence.trim().isEmpty()) { /* null_or_blank */
            return 0; // ret_zero
        }
        String[] words = sentence.trim().split("\\s+"); /* split_words */
        return words.length; // ret_cnt
    }

    // Menghitung jumlah huruf dan angka (spasi dan tanda baca tidak dihitung)
    public int hitungHuruf(String kalimat) { // @op:count_letters
        int jumlah = 0; /* cnt_init */
        for (int i = 0; i < kalimat.length(); i++) { // scan_chars
            if (Character.isLetterOrDigit(kalimat.charAt(i))) { /* alnum_chk */
                jumlah++; // inc_cnt
            }
        }
        return jumlah; /* ret_cnt */
    }

    // Mencari kata terpanjang dalam kalimat
    public String kataTerpanjang(String kalimat) { // @op:longest_word
        if (countWords(kalimat) == 0) { /* empty_chk */
            return ""; // ret_empty
        }
        String[] kata = kalimat.trim().split("\\s+"); /* split_w */
        String terpanjang = kata[0]; // init_longest
        for (int i = 1; i < kata.length; i++) { /* scan_words */
            if (kata[i].length() > terpanjang.length()) { // longer_chk
                terpanjang = kata[i]; /* update_longest */
            }
        }
        return terpanjang; // ret_longest
    }

    // Menghitung berapa kali sebuah kata muncul (huruf besar kecil diabaikan)
    public int hitungFrekuensi(String kalimat, String target) { // @op:freq
        if (countWords(kalimat) == 0) { /* empty_chk */
            return 0; // ret_zero
        }
        String[] kata = kalimat.trim().split("\\s+"); /* split_w */
        int jumlah = 0; // cnt_init
        for (int i = 0; i < kata.length; i++) { /* scan_words */
            if (kata[i].equalsIgnoreCase(target)) { // match_chk
                jumlah++; /* inc_cnt */
            }
        }
        return jumlah; // ret_freq
    }

    // Menghitung rata-rata panjang kata
    public double rataRataPanjang(String kalimat) { // @op:avg_len
        int jumlahKata = countWords(kalimat); /* word_cnt */
        if (jumlahKata == 0) { // empty_chk
            return 0; /* ret_zero */
        }
        String[] kata = kalimat.trim().split("\\s+"); // split_w
        int totalPanjang = 0; /* len_acc */
        for (int i = 0; i < kata.length; i++) { // sum_len
            totalPanjang += kata[i].length(); /* add_len */
        }
        return (double) totalPanjang / jumlahKata; // ret_avg
    }

    // Menampilkan setiap kata beserta nomor urutnya
    public void tampilkanDaftarKata(String kalimat) { // @disp:word_list
        if (countWords(kalimat) == 0) { /* empty_chk */
            System.out.println("Tidak ada kata."); // msg_empty
            return; /* early_ret */
        }
        String[] kata = kalimat.trim().split("\\s+"); /* split_w */
        for (int i = 0; i < kata.length; i++) { // iter_words
            System.out.println((i + 1) + ". " + kata[i] + " (" + kata[i].length() + " karakter)"); /* row_out */
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

    // Membaca satu baris teks dari pengguna
    public static String bacaTeks(Scanner input, String pesan) { // @read:str
        System.out.print(pesan); /* prompt */
        return input.nextLine(); // ret_line
    }

    public static void main(String[] args) { // @entry:main
        Scanner input = new Scanner(System.in); /* io_init */
        WordCounter penghitung = new WordCounter(); // obj_wc

        int pilihan; /* menu_var */
        do { // menu_loop
            System.out.println("\n===== PROGRAM PENGHITUNG KATA ====="); /* hdr */
            System.out.println("1. Hitung jumlah kata"); // opt1
            System.out.println("2. Hitung jumlah huruf"); /* opt2 */
            System.out.println("3. Cari kata terpanjang"); // opt3
            System.out.println("4. Hitung frekuensi sebuah kata"); /* opt4 */
            System.out.println("5. Rata-rata panjang kata"); // opt5
            System.out.println("6. Tampilkan daftar kata"); /* opt6 */
            System.out.println("0. Keluar"); // opt0
            pilihan = bacaAngka(input, "Pilih menu: "); /* read_choice */

            switch (pilihan) { // dispatch
                case 1: /* case_wc */
                    String k1 = bacaTeks(input, "Masukkan kalimat: "); // get_k1
                    System.out.println("Jumlah kata : " + penghitung.countWords(k1)); /* wc_out */
                    break; /* brk1 */
                case 2: /* case_lc */
                    String k2 = bacaTeks(input, "Masukkan kalimat: "); // get_k2
                    System.out.println("Jumlah huruf dan angka : " + penghitung.hitungHuruf(k2)); /* lc_out */
                    break; /* brk2 */
                case 3: /* case_longest */
                    String k3 = bacaTeks(input, "Masukkan kalimat: "); // get_k3
                    String terpanjang = penghitung.kataTerpanjang(k3); /* get_longest */
                    if (terpanjang.isEmpty()) { // empty_chk
                        System.out.println("Kalimat kosong, tidak ada kata."); /* msg_empty */
                    } else { // has_word
                        System.out.println("Kata terpanjang : " + terpanjang
                                + " (" + terpanjang.length() + " karakter)"); /* longest_out */
                    }
                    break; /* brk3 */
                case 4: /* case_freq */
                    String k4 = bacaTeks(input, "Masukkan kalimat: "); // get_k4
                    String target = bacaTeks(input, "Kata yang dicari: "); /* get_target */
                    System.out.println("Kata \"" + target + "\" muncul "
                            + penghitung.hitungFrekuensi(k4, target) + " kali."); // freq_out
                    break; /* brk4 */
                case 5: /* case_avg */
                    String k5 = bacaTeks(input, "Masukkan kalimat: "); // get_k5
                    System.out.printf("Rata-rata panjang kata : %.2f%n",
                            penghitung.rataRataPanjang(k5)); /* avg_out */
                    break; /* brk5 */
                case 6: /* case_list */
                    String k6 = bacaTeks(input, "Masukkan kalimat: "); // get_k6
                    penghitung.tampilkanDaftarKata(k6); /* show_list */
                    break; /* brk6 */
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