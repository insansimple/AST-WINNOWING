/*
 * Program   : StringReverser
 * Deskripsi : Membalik sebuah teks dari belakang ke depan. Dilengkapi
 *             pembalikan urutan kata, pembalikan tiap kata, menghitung
 *             jumlah huruf vokal, pengecekan hasil balik, input dari
 *             pengguna, dan menu sederhana.
 */
import java.util.Scanner; // [tok:er5] io module

public class StringReverser { // @cls:sr

    // Logika inti: membalik teks menggunakan StringBuilder
    public String reverse(String input) { // @op:reverse
        StringBuilder sb = new StringBuilder(); /* sb_init */
        for (int i = input.length() - 1; i >= 0; i--) { // rev_loop
            sb.append(input.charAt(i)); /* append_ch */
        }
        return sb.toString(); // ret_reversed
    }

    // Membalik urutan kata dalam kalimat (kata tetap utuh)
    public String balikUrutanKata(String kalimat) { // @op:rev_words
        String[] kata = kalimat.trim().split("\\s+"); /* split_words */
        StringBuilder sb = new StringBuilder(); // sb_words
        for (int i = kata.length - 1; i >= 0; i--) { /* rev_order */
            sb.append(kata[i]); // append_word
            if (i > 0) { /* not_first */
                sb.append(" "); // space_sep
            }
        }
        return sb.toString(); /* ret_rev_words */
    }

    // Membalik huruf pada setiap kata, urutan kata tetap
    public String balikTiapKata(String kalimat) { // @op:rev_each
        String[] kata = kalimat.trim().split("\\s+"); /* split_words_e */
        StringBuilder sb = new StringBuilder(); // sb_each
        for (int i = 0; i < kata.length; i++) { /* iter_words */
            sb.append(reverse(kata[i])); // rev_word
            if (i < kata.length - 1) { /* not_last */
                sb.append(" "); // space_sep
            }
        }
        return sb.toString(); /* ret_rev_each */
    }

    // Menghitung jumlah huruf vokal dalam teks
    public int hitungVokal(String teks) { // @op:count_vowel
        int jumlah = 0; /* cnt_init */
        String vokal = "aiueoAIUEO"; /* vowel_set */
        for (int i = 0; i < teks.length(); i++) { // scan_chars
            if (vokal.indexOf(teks.charAt(i)) != -1) { /* vowel_chk */
                jumlah++; // inc_cnt
            }
        }
        return jumlah; /* ret_cnt */
    }

    // Mengecek apakah teks sama dengan hasil baliknya (tanpa membedakan huruf besar kecil)
    public boolean samaDenganBalikan(String teks) { // @chk:is_pal
        return teks.equalsIgnoreCase(reverse(teks)); /* compare */
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

    // Membaca satu baris teks yang tidak boleh kosong
    public static String bacaTeks(Scanner input, String pesan) { // @read:nonempty
        String teks = ""; /* acc_str */
        while (teks.trim().isEmpty()) { // retry_nonempty
            System.out.print(pesan); /* prompt */
            teks = input.nextLine(); // read_line
            if (teks.trim().isEmpty()) { /* empty_chk */
                System.out.println("Teks tidak boleh kosong!"); // warn_empty
            }
        }
        return teks; /* ret_text */
    }

    public static void main(String[] args) { // @entry:main
        Scanner input = new Scanner(System.in); /* io_init */
        StringReverser pembalik = new StringReverser(); // obj_sr

        int pilihan; /* menu_var */
        do { // menu_loop
            System.out.println("\n===== PROGRAM PEMBALIK TEKS ====="); /* hdr */
            System.out.println("1. Balik seluruh teks"); // opt1
            System.out.println("2. Balik urutan kata"); /* opt2 */
            System.out.println("3. Balik huruf tiap kata"); // opt3
            System.out.println("4. Hitung jumlah huruf vokal"); /* opt4 */
            System.out.println("5. Cek teks sama dengan balikannya"); // opt5
            System.out.println("0. Keluar"); /* opt0 */
            pilihan = bacaAngka(input, "Pilih menu: "); // read_choice

            switch (pilihan) { /* dispatch */
                case 1: // case_rev_all
                    String teks1 = bacaTeks(input, "Masukkan teks: "); /* get_teks1 */
                    System.out.println("Hasil : " + pembalik.reverse(teks1)); // rev_out
                    break; /* brk1 */
                case 2: // case_rev_ord
                    String teks2 = bacaTeks(input, "Masukkan kalimat: "); /* get_teks2 */
                    System.out.println("Hasil : " + pembalik.balikUrutanKata(teks2)); // rev_ord_out
                    break; /* brk2 */
                case 3: // case_rev_each
                    String teks3 = bacaTeks(input, "Masukkan kalimat: "); /* get_teks3 */
                    System.out.println("Hasil : " + pembalik.balikTiapKata(teks3)); // rev_each_out
                    break; /* brk3 */
                case 4: // case_vowel
                    String teks4 = bacaTeks(input, "Masukkan teks: "); /* get_teks4 */
                    System.out.println("Jumlah huruf vokal : " + pembalik.hitungVokal(teks4)); // vowel_out
                    break; /* brk4 */
                case 5: // case_pal
                    String teks5 = bacaTeks(input, "Masukkan teks: "); /* get_teks5 */
                    if (pembalik.samaDenganBalikan(teks5)) { // pal_chk
                        System.out.println("Teks sama dengan hasil baliknya."); /* msg_pal */
                    } else { // not_pal
                        System.out.println("Teks berbeda dengan hasil baliknya: "
                                + pembalik.reverse(teks5)); /* msg_npal */
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