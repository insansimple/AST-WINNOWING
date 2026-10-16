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
    public int cariBesar(int p, int q, int r) {
        int nilaiMax = p;
        if (q > nilaiMax) {
            nilaiMax = q;
        }
        if (r > nilaiMax) {
            nilaiMax = r;
        }
        return nilaiMax;
    }

    // Mencari nilai terkecil dari tiga bilangan
    public int cariKecil(int p, int q, int r) {
        int nilaiMin = p;
        if (q < nilaiMin) {
            nilaiMin = q;
        }
        if (r < nilaiMin) {
            nilaiMin = r;
        }
        return nilaiMin;
    }

    // Mencari nilai terbesar dari sebuah array
    public int cariBesarDariArray(int[] arr) {
        int nilaiMax = arr[0];
        for (int pos = 1; pos < arr.length; pos++) {
            if (arr[pos] > nilaiMax) {
                nilaiMax = arr[pos];
            }
        }
        return nilaiMax;
    }

    // Menghitung rata-rata dari tiga bilangan
    public double rataRata(int p, int q, int r) {
        return (p + q + r) / 3.0;
    }

    // Menampilkan tiga bilangan dari yang terkecil ke terbesar
    public void urutkanTiga(int p, int q, int r) {
        int maks = cariBesar(p, q, r);
        int min = cariKecil(p, q, r);
        // Nilai tengah = jumlah semua dikurangi terbesar dan terkecil
        int mid = p + q + r - maks - min;
        System.out.println("Urutan naik  : " + min + ", " + mid + ", " + maks);
        System.out.println("Urutan turun : " + maks + ", " + mid + ", " + min);
    }

    // Membaca bilangan bulat dengan validasi sederhana
    public static int ambilAngka(Scanner scanner, String teks) {
        System.out.print(teks);
        while (!scanner.hasNextInt()) {
            System.out.print("Input harus angka bulat, ulangi: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MaximumFinder alat = new MaximumFinder();

        int menu;
        do {
            System.out.println("\n===== PROGRAM NILAI TERBESAR =====");
            System.out.println("1. Cari terbesar dari tiga bilangan");
            System.out.println("2. Cari terkecil dari tiga bilangan");
            System.out.println("3. Hitung rata-rata tiga bilangan");
            System.out.println("4. Urutkan tiga bilangan");
            System.out.println("5. Cari terbesar dari sejumlah data");
            System.out.println("0. Keluar");
            menu = ambilAngka(scanner, "Pilih menu: ");

            switch (menu) {
                case 1:
                    int x1 = ambilAngka(scanner, "Bilangan pertama : ");
                    int y1 = ambilAngka(scanner, "Bilangan kedua   : ");
                    int z1 = ambilAngka(scanner, "Bilangan ketiga  : ");
                    System.out.println("Nilai terbesar : " + alat.cariBesar(x1, y1, z1));
                    break;
                case 2:
                    int x2 = ambilAngka(scanner, "Bilangan pertama : ");
                    int y2 = ambilAngka(scanner, "Bilangan kedua   : ");
                    int z2 = ambilAngka(scanner, "Bilangan ketiga  : ");
                    System.out.println("Nilai terkecil : " + alat.cariKecil(x2, y2, z2));
                    break;
                case 3:
                    int x3 = ambilAngka(scanner, "Bilangan pertama : ");
                    int y3 = ambilAngka(scanner, "Bilangan kedua   : ");
                    int z3 = ambilAngka(scanner, "Bilangan ketiga  : ");
                    System.out.println("Rata-rata : " + alat.rataRata(x3, y3, z3));
                    break;
                case 4:
                    int x4 = ambilAngka(scanner, "Bilangan pertama : ");
                    int y4 = ambilAngka(scanner, "Bilangan kedua   : ");
                    int z4 = ambilAngka(scanner, "Bilangan ketiga  : ");
                    alat.urutkanTiga(x4, y4, z4);
                    break;
                case 5:
                    int banyak = ambilAngka(scanner, "Jumlah data : ");
                    if (banyak <= 0) {
                        System.out.println("Jumlah data harus lebih dari 0!");
                    } else {
                        int[] arr = new int[banyak];
                        for (int pos = 0; pos < banyak; pos++) {
                            arr[pos] = ambilAngka(scanner, "Data ke-" + (pos + 1) + " : ");
                        }
                        System.out.println("Nilai terbesar : " + alat.cariBesarDariArray(arr));
                    }
                    break;
                case 0:
                    System.out.println("Terima kasih, program selesai.");
                    break;
                default:
                    System.out.println("Menu tidak tersedia!");
            }
        } while (menu != 0);

        scanner.close();
    }
}