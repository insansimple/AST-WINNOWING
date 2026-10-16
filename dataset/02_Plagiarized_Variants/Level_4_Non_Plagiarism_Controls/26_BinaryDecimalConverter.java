/*
 * Program   : BinaryDecimalConverter
 * Deskripsi : Konversi bilangan antar-basis: Biner (2), Oktal (8),
 *             Desimal (10), dan Heksadesimal (16). Konversi dilakukan
 *             manual (tanpa Integer.toBinaryString dll) dengan metode
 *             pembagian berulang dan penjumlahan bobot posisi.
 */
import java.util.Scanner;

public class BinaryDecimalConverter {

    // Daftar digit yang dipakai sampai basis 16
    private static final String DIGIT = "0123456789ABCDEF";

    // Desimal -> basis lain (2 s.d. 16) dengan pembagian berulang
    public static String desimalKeBasis(long angka, int basis) {
        if (angka == 0) {
            return "0";
        }
        StringBuilder hasil = new StringBuilder();
        while (angka > 0) {
            int sisa = (int) (angka % basis);
            hasil.append(DIGIT.charAt(sisa));   // sisa bagi jadi digit
            angka = angka / basis;
        }
        // sisa bagi terbaca dari bawah ke atas, jadi dibalik
        return hasil.reverse().toString();
    }

    // Mengecek apakah semua karakter valid untuk basis tertentu
    public static boolean isValid(String teks, int basis) {
        if (teks.isEmpty()) {
            return false;
        }
        String besar = teks.toUpperCase();
        for (int i = 0; i < besar.length(); i++) {
            int nilai = DIGIT.indexOf(besar.charAt(i));
            if (nilai < 0 || nilai >= basis) {
                return false;
            }
        }
        return true;
    }

    // Basis lain -> desimal dengan menjumlahkan digit x bobot posisi
    // Mengembalikan -1 jika input tidak valid atau terlalu besar
    public static long basisKeDesimal(String teks, int basis) {
        if (!isValid(teks, basis)) {
            return -1;
        }
        String besar = teks.toUpperCase();
        long hasil = 0;
        for (int i = 0; i < besar.length(); i++) {
            int nilai = DIGIT.indexOf(besar.charAt(i));
            // cek supaya tidak overflow pada tipe long
            if (hasil > (Long.MAX_VALUE - nilai) / basis) {
                return -1;
            }
            hasil = hasil * basis + nilai;
        }
        return hasil;
    }

    // Konversi bebas: basis asal -> desimal -> basis tujuan
    public static String konversi(String teks, int basisAsal, int basisTujuan) {
        long desimal = basisKeDesimal(teks, basisAsal);
        if (desimal < 0) {
            return null;
        }
        return desimalKeBasis(desimal, basisTujuan);
    }

    // Menampilkan hasil desimal ke biner, oktal, dan heksadesimal
    public static void tampilkanSemua(long angka) {
        System.out.println("Desimal      : " + angka);
        System.out.println("Biner        : " + desimalKeBasis(angka, 2));
        System.out.println("Oktal        : " + desimalKeBasis(angka, 8));
        System.out.println("Heksadesimal : " + desimalKeBasis(angka, 16));
    }

    // Tabel konversi 0 sampai 16
    public static void tampilkanTabel() {
        System.out.println("\nDesimal\tBiner\tOktal\tHeksa");
        System.out.println("-------------------------------");
        for (int i = 0; i <= 16; i++) {
            System.out.println(i + "\t" + desimalKeBasis(i, 2) + "\t"
                    + desimalKeBasis(i, 8) + "\t" + desimalKeBasis(i, 16));
        }
    }

    // Membaca bilangan bulat dari input dengan penanganan salah ketik
    public static long bacaAngka(Scanner input, String pesan) {
        System.out.print(pesan);
        try {
            return Long.parseLong(input.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Membaca teks bilangan lalu menampilkan hasil konversi ke desimal
    public static void prosesKeDesimal(Scanner input, String namaBasis, int basis) {
        System.out.print("Masukkan bilangan " + namaBasis + " : ");
        String teks = input.nextLine().trim();
        long desimal = basisKeDesimal(teks, basis);

        if (desimal < 0) {
            System.out.println("Input tidak valid untuk basis " + basis + "!");
        } else {
            System.out.println(teks.toUpperCase() + " (basis " + basis + ") = "
                    + desimal + " (desimal)");
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int pilihan = -1;

        // Contoh pemakaian dengan data tetap
        System.out.println("=== CONTOH PENGUJIAN ===");
        tampilkanSemua(45);
        System.out.println("1101 (biner)  = " + basisKeDesimal("1101", 2) + " (desimal)");
        System.out.println("FF (heksa)    = " + basisKeDesimal("FF", 16) + " (desimal)");
        System.out.println("755 (oktal)   = " + basisKeDesimal("755", 8) + " (desimal)");

        do {
            // Menu utama
            System.out.println("\n=== MENU KONVERSI BASIS ===");
            System.out.println("1. Desimal -> Biner, Oktal, Heksadesimal");
            System.out.println("2. Biner -> Desimal");
            System.out.println("3. Oktal -> Desimal");
            System.out.println("4. Heksadesimal -> Desimal");
            System.out.println("5. Konversi bebas (basis 2-16)");
            System.out.println("6. Tabel konversi 0-16");
            System.out.println("0. Keluar");
            pilihan = (int) bacaAngka(input, "Pilihan: ");

            switch (pilihan) {
                case 1:
                    long angka = bacaAngka(input, "Masukkan bilangan desimal : ");
                    if (angka < 0) {
                        System.out.println("Input harus bilangan bulat >= 0!");
                    } else {
                        tampilkanSemua(angka);
                    }
                    break;
                case 2:
                    prosesKeDesimal(input, "biner", 2);
                    break;
                case 3:
                    prosesKeDesimal(input, "oktal", 8);
                    break;
                case 4:
                    prosesKeDesimal(input, "heksadesimal", 16);
                    break;
                case 5:
                    int asal = (int) bacaAngka(input, "Basis asal   (2-16) : ");
                    int tujuan = (int) bacaAngka(input, "Basis tujuan (2-16) : ");
                    if (asal < 2 || asal > 16 || tujuan < 2 || tujuan > 16) {
                        System.out.println("Basis harus antara 2 sampai 16!");
                        break;
                    }
                    System.out.print("Masukkan bilangan : ");
                    String teks = input.nextLine().trim();
                    String hasil = konversi(teks, asal, tujuan);
                    if (hasil == null) {
                        System.out.println("Bilangan tidak valid untuk basis " + asal + "!");
                    } else {
                        System.out.println(teks.toUpperCase() + " (basis " + asal + ") = "
                                + hasil + " (basis " + tujuan + ")");
                    }
                    break;
                case 6:
                    tampilkanTabel();
                    break;
                case 0:
                    System.out.println("Program selesai.");
                    break;
                default:
                    System.out.println("Pilihan tidak tersedia!");
            }
        } while (pilihan != 0);

        input.close();
    }
}