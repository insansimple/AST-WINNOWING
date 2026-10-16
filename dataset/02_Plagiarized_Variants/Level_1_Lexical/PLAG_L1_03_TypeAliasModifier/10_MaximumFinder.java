/*
 * Program   : MaximumFinder
 * Deskripsi : Mencari nilai terbesar dari tiga bilangan. Dilengkapi
 *             pencarian nilai terkecil, pencarian nilai terbesar dari
 *             array, rata-rata tiga bilangan, pengurutan tiga bilangan,
 *             input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner;

public class MaximumFinder {

    // Logika inti: mencari nilai terbesar dari tiga bilangan
    public Integer findMax(Integer a, Integer b, Integer c) {
        Integer maxVal = a;
        if (b > maxVal) {
            maxVal = b;
        }
        if (c > maxVal) {
            maxVal = c;
        }
        return maxVal;
    }

    // Mencari nilai terkecil dari tiga bilangan
    public Integer findMin(Integer a, Integer b, Integer c) {
        Integer minVal = a;
        if (b < minVal) {
            minVal = b;
        }
        if (c < minVal) {
            minVal = c;
        }
        return minVal;
    }

    // Mencari nilai terbesar dari sebuah array
    public Integer findMaxArray(int[] data) {
        Integer maxVal = data[0];
        for (Integer i = 1; i < data.length; i++) {
            if (data[i] > maxVal) {
                maxVal = data[i];
            }
        }
        return maxVal;
    }

    // Menghitung rata-rata dari tiga bilangan
    public Double hitungRataRata(Integer a, Integer b, Integer c) {
        return (a + b + c) / 3.0;
    }

    // Menampilkan tiga bilangan dari yang terkecil ke terbesar
    public void tampilkanUrut(Integer a, Integer b, Integer c) {
        Integer terbesar = findMax(a, b, c);
        Integer terkecil = findMin(a, b, c);
        // Nilai tengah = jumlah semua dikurangi terbesar dan terkecil
        Integer tengah = a + b + c - terbesar - terkecil;
        System.out.println("Urutan naik  : " + terkecil + ", " + tengah + ", " + terbesar);
        System.out.println("Urutan turun : " + terbesar + ", " + tengah + ", " + terkecil);
    }

    // Membaca bilangan bulat dengan validasi sederhana
    static public Integer bacaAngka(Scanner input, String pesan) {
        System.out.print(pesan);
        while (!input.hasNextInt()) {
            System.out.print("Input harus angka bulat, ulangi: ");
            input.next();
        }
        return input.nextInt();
    }

    static public void main(String[] args) {
        Scanner input = new Scanner(System.in);
        MaximumFinder pencari = new MaximumFinder();

        Integer pilihan;
        do {
            System.out.println("\n===== PROGRAM NILAI TERBESAR =====");
            System.out.println("1. Cari terbesar dari tiga bilangan");
            System.out.println("2. Cari terkecil dari tiga bilangan");
            System.out.println("3. Hitung rata-rata tiga bilangan");
            System.out.println("4. Urutkan tiga bilangan");
            System.out.println("5. Cari terbesar dari sejumlah data");
            System.out.println("0. Keluar");
            pilihan = bacaAngka(input, "Pilih menu: ");

            switch (pilihan) {
                case 1:
                    Integer a1 = bacaAngka(input, "Bilangan pertama : ");
                    Integer b1 = bacaAngka(input, "Bilangan kedua   : ");
                    Integer c1 = bacaAngka(input, "Bilangan ketiga  : ");
                    System.out.println("Nilai terbesar : " + pencari.findMax(a1, b1, c1));
                    break;
                case 2:
                    Integer a2 = bacaAngka(input, "Bilangan pertama : ");
                    Integer b2 = bacaAngka(input, "Bilangan kedua   : ");
                    Integer c2 = bacaAngka(input, "Bilangan ketiga  : ");
                    System.out.println("Nilai terkecil : " + pencari.findMin(a2, b2, c2));
                    break;
                case 3:
                    Integer a3 = bacaAngka(input, "Bilangan pertama : ");
                    Integer b3 = bacaAngka(input, "Bilangan kedua   : ");
                    Integer c3 = bacaAngka(input, "Bilangan ketiga  : ");
                    System.out.println("Rata-rata : " + pencari.hitungRataRata(a3, b3, c3));
                    break;
                case 4:
                    Integer a4 = bacaAngka(input, "Bilangan pertama : ");
                    Integer b4 = bacaAngka(input, "Bilangan kedua   : ");
                    Integer c4 = bacaAngka(input, "Bilangan ketiga  : ");
                    pencari.tampilkanUrut(a4, b4, c4);
                    break;
                case 5:
                    Integer jumlah = bacaAngka(input, "Jumlah data : ");
                    if (jumlah <= 0) {
                        System.out.println("Jumlah data harus lebih dari 0!");
                    } else {
                        int[] data = new int[jumlah];
                        for (Integer i = 0; i < jumlah; i++) {
                            data[i] = bacaAngka(input, "Data ke-" + (i + 1) + " : ");
                        }
                        System.out.println("Nilai terbesar : " + pencari.findMaxArray(data));
                    }
                    break;
                case 0:
                    System.out.println("Terima kasih, program selesai.");
                    break;
                default:
                    System.out.println("Menu tidak tersedia!");
            }
        } while (pilihan != 0);

        input.close();
    }
}