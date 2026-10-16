/*
 * Program   : MaximumFinder
 * Deskripsi : Mencari nilai terbesar dari tiga bilangan. Dilengkapi
 *             pencarian nilai terkecil, pencarian nilai terbesar dari
 *             array, rata-rata tiga bilangan, pengurutan tiga bilangan,
 *             input dari pengguna, dan menu sederhana.
 */
import java.util.Scanner;

public class MaximumFinder {

    // Inner static class wrapper untuk tiga bilangan komparasi
    public static class NumberTriplet {
        private int first;
        private int second;
        private int third;

        public NumberTriplet(int first, int second, int third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }

        public int getFirst() {
            return first;
        }

        public int getSecond() {
            return second;
        }

        public int getThird() {
            return third;
        }

        public int sum() {
            return first + second + third;
        }
    }

    // Inner static class wrapper untuk koleksi data numerik
    public static class NumberSeries {
        private int[] numbers;

        public NumberSeries(int[] numbers) {
            this.numbers = numbers;
        }

        public int size() {
            return numbers.length;
        }

        public int get(int index) {
            return numbers[index];
        }
    }

    // Logika inti: mencari nilai terbesar dari tiga bilangan
    public int findMax(int a, int b, int c) {
        NumberTriplet triplet = new NumberTriplet(a, b, c);
        int maxVal = triplet.getFirst();
        if (triplet.getSecond() > maxVal) {
            maxVal = triplet.getSecond();
        }
        if (triplet.getThird() > maxVal) {
            maxVal = triplet.getThird();
        }
        return maxVal;
    }

    // Mencari nilai terkecil dari tiga bilangan
    public int findMin(int a, int b, int c) {
        NumberTriplet triplet = new NumberTriplet(a, b, c);
        int minVal = triplet.getFirst();
        if (triplet.getSecond() < minVal) {
            minVal = triplet.getSecond();
        }
        if (triplet.getThird() < minVal) {
            minVal = triplet.getThird();
        }
        return minVal;
    }

    // Mencari nilai terbesar dari sebuah array
    public int findMaxArray(int[] data) {
        NumberSeries series = new NumberSeries(data);
        int maxVal = series.get(0);
        for (int i = 1; i < series.size(); i++) {
            if (series.get(i) > maxVal) {
                maxVal = series.get(i);
            }
        }
        return maxVal;
    }

    // Menghitung rata-rata dari tiga bilangan
    public double hitungRataRata(int a, int b, int c) {
        NumberTriplet triplet = new NumberTriplet(a, b, c);
        return triplet.sum() / 3.0;
    }

    // Menampilkan tiga bilangan dari yang terkecil ke terbesar
    public void tampilkanUrut(int a, int b, int c) {
        NumberTriplet triplet = new NumberTriplet(a, b, c);
        int terbesar = findMax(triplet.getFirst(), triplet.getSecond(), triplet.getThird());
        int terkecil = findMin(triplet.getFirst(), triplet.getSecond(), triplet.getThird());
        // Nilai tengah = jumlah semua dikurangi terbesar dan terkecil
        int tengah = triplet.sum() - terbesar - terkecil;
        System.out.println("Urutan naik  : " + terkecil + ", " + tengah + ", " + terbesar);
        System.out.println("Urutan turun : " + terbesar + ", " + tengah + ", " + terkecil);
    }

    // Membaca bilangan bulat dengan validasi sederhana
    public static int bacaAngka(Scanner input, String pesan) {
        System.out.print(pesan);
        while (!input.hasNextInt()) {
            System.out.print("Input harus angka bulat, ulangi: ");
            input.next();
        }
        return input.nextInt();
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        MaximumFinder pencari = new MaximumFinder();

        int pilihan = 0;
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
                    int a1 = bacaAngka(input, "Bilangan pertama : ");
                    int b1 = bacaAngka(input, "Bilangan kedua   : ");
                    int c1 = bacaAngka(input, "Bilangan ketiga  : ");
                    System.out.println("Nilai terbesar : " + pencari.findMax(a1, b1, c1));
                    break;
                case 2:
                    int a2 = bacaAngka(input, "Bilangan pertama : ");
                    int b2 = bacaAngka(input, "Bilangan kedua   : ");
                    int c2 = bacaAngka(input, "Bilangan ketiga  : ");
                    System.out.println("Nilai terkecil : " + pencari.findMin(a2, b2, c2));
                    break;
                case 3:
                    int a3 = bacaAngka(input, "Bilangan pertama : ");
                    int b3 = bacaAngka(input, "Bilangan kedua   : ");
                    int c3 = bacaAngka(input, "Bilangan ketiga  : ");
                    System.out.println("Rata-rata : " + pencari.hitungRataRata(a3, b3, c3));
                    break;
                case 4:
                    int a4 = bacaAngka(input, "Bilangan pertama : ");
                    int b4 = bacaAngka(input, "Bilangan kedua   : ");
                    int c4 = bacaAngka(input, "Bilangan ketiga  : ");
                    pencari.tampilkanUrut(a4, b4, c4);
                    break;
                case 5:
                    int jumlah = bacaAngka(input, "Jumlah data : ");
                    if (jumlah <= 0) {
                        System.out.println("Jumlah data harus lebih dari 0!");
                    } else {
                        int[] data = new int[jumlah];
                        for (int i = 0; i < jumlah; i++) {
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