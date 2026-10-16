/*
 * Program   : PalindromeChecker
 * Deskripsi : Mengecek apakah sebuah kata atau kalimat merupakan
 *             palindrom. Dilengkapi pengecekan kalimat (mengabaikan
 *             spasi dan tanda baca), membalik teks, menghitung jumlah
 *             huruf, pengecekan palindrom angka, input dari pengguna,
 *             dan menu sederhana.
 */
import java.util.Scanner; // [tok:dp3] io module

public class PalindromeChecker { // @cls:palchk

    // Logika inti: mengecek palindrom dengan dua penunjuk (kiri dan kanan)
    public boolean isPalindrome(String str) { // @op:pal_chk
        String cleaned = str.toLowerCase(); /* normalize */
        int left = 0; // left_ptr
        int right = cleaned.length() - 1; /* right_ptr */
        while (left < right) { // two_ptr_loop
            if (cleaned.charAt(left) != cleaned.charAt(right)) { /* mismatch */
                return false; // not_pal
            }
            left++; /* advance_l */
            right--; // retreat_r
        }
        return true; /* is_pal */
    }

    // Membuang semua karakter selain huruf dan angka
    public String bersihkanTeks(String teks) { // @op:clean
        String hasil = ""; /* acc_str */
        for (int i = 0; i < teks.length(); i++) { // scan_chars
            char c = teks.charAt(i); /* get_char */
            if (Character.isLetterOrDigit(c)) { // alnum_chk
                hasil += c; /* append_ch */
            }
        }
        return hasil; // ret_cleaned
    }

    // Mengecek palindrom untuk kalimat (spasi dan tanda baca diabaikan)
    public boolean isPalindromeKalimat(String kalimat) { // @op:pal_sent
        String bersih = bersihkanTeks(kalimat); /* cleaned_str */
        return isPalindrome(bersih); // check_pal
    }

    // Membalik teks dari belakang ke depan
    public String balikTeks(String teks) { // @op:reverse
        String hasil = ""; /* acc_rev */
        for (int i = teks.length() - 1; i >= 0; i--) { // reverse_loop
            hasil += teks.charAt(i); /* append_ch */
        }
        return hasil; // ret_reversed
    }

    // Mengecek apakah sebuah angka merupakan palindrom
    public boolean isPalindromeAngka(int angka) { // @op:pal_num
        if (angka < 0) { /* neg_chk */
            return false; // neg_reject
        }
        int asli = angka; /* orig_val */
        int terbalik = 0; // rev_val
        while (angka > 0) { /* digit_loop */
            int digit = angka % 10; // last_digit
            terbalik = terbalik * 10 + digit; /* shift_add */
            angka = angka / 10; // remove_digit
        }
        return asli == terbalik; /* compare */
    }

    // Menghitung jumlah huruf dan angka dalam teks
    public int hitungHurufAngka(String teks) { // @op:count_alnum
        return bersihkanTeks(teks).length(); /* cnt_alnum */
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
        PalindromeChecker pengecek = new PalindromeChecker(); // obj_pc

        int pilihan; /* menu_var */
        do { // menu_loop
            System.out.println("\n===== PROGRAM PALINDROM ====="); /* hdr */
            System.out.println("1. Cek palindrom kata"); // opt1
            System.out.println("2. Cek palindrom kalimat"); /* opt2 */
            System.out.println("3. Cek palindrom angka"); // opt3
            System.out.println("4. Balik teks"); /* opt4 */
            System.out.println("0. Keluar"); // opt0
            pilihan = bacaAngka(input, "Pilih menu: "); /* read_choice */

            switch (pilihan) { // dispatch
                case 1: /* case_word */
                    String kata = bacaTeks(input, "Masukkan kata: "); // get_word
                    if (pengecek.isPalindrome(kata)) { /* pal_chk */
                        System.out.println("\"" + kata + "\" adalah palindrom."); // msg_pal
                    } else { /* not_pal */
                        System.out.println("\"" + kata + "\" bukan palindrom."); // msg_npal
                    }
                    break; /* brk1 */
                case 2: /* case_sent */
                    String kalimat = bacaTeks(input, "Masukkan kalimat: "); // get_sent
                    System.out.println("Jumlah huruf dan angka : "
                            + pengecek.hitungHurufAngka(kalimat)); /* cnt_out */
                    if (pengecek.isPalindromeKalimat(kalimat)) { // pal_sent_chk
                        System.out.println("Kalimat tersebut adalah palindrom."); /* msg_pal_s */
                    } else { // not_pal_s
                        System.out.println("Kalimat tersebut bukan palindrom."); /* msg_npal_s */
                    }
                    break; /* brk2 */
                case 3: /* case_num */
                    int angka = bacaAngka(input, "Masukkan angka: "); // get_num
                    if (pengecek.isPalindromeAngka(angka)) { /* pal_num_chk */
                        System.out.println(angka + " adalah palindrom."); // msg_pal_n
                    } else { /* not_pal_n */
                        System.out.println(angka + " bukan palindrom."); // msg_npal_n
                    }
                    break; /* brk3 */
                case 4: /* case_rev */
                    String teks = bacaTeks(input, "Masukkan teks: "); // get_teks
                    System.out.println("Hasil dibalik : " + pengecek.balikTeks(teks)); /* rev_out */
                    break; /* brk4 */
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