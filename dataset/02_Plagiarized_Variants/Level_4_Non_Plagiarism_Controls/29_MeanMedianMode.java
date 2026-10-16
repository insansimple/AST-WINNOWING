/*
 * Program   : MeanMedianMode
 * Deskripsi : Menghitung statistika dasar dari sekumpulan data:
 *             mean (rata-rata), median (nilai tengah), dan modus
 *             (nilai yang paling sering muncul). Modus bisa lebih
 *             dari satu (multimodus) atau tidak ada sama sekali.
 */
import java.util.Scanner;

public class MeanMedianMode {

    // Menampilkan isi array dalam satu baris
    public static void tampilkanArray(double[] data) {
        System.out.print("[ ");
        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i] + " ");
        }
        System.out.println("]");
    }

    // Menghitung mean = jumlah seluruh data / banyak data
    public static double hitungMean(double[] data) {
        double total = 0;
        for (int i = 0; i < data.length; i++) {
            total = total + data[i];
        }
        return total / data.length;
    }

    // Mengurutkan data dengan bubble sort (salinan, array asli tidak berubah)
    public static double[] urutkan(double[] data) {
        double[] salinan = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            salinan[i] = data[i];
        }

        for (int i = 0; i < salinan.length - 1; i++) {
            for (int j = 0; j < salinan.length - 1 - i; j++) {
                if (salinan[j] > salinan[j + 1]) {
                    double temp = salinan[j];
                    salinan[j] = salinan[j + 1];
                    salinan[j + 1] = temp;
                }
            }
        }
        return salinan;
    }

    // Menghitung median: data diurutkan dulu, lalu ambil nilai tengah
    public static double hitungMedian(double[] data) {
        double[] terurut = urutkan(data);
        int n = terurut.length;

        if (n % 2 == 1) {
            // jumlah ganjil: ambil elemen tengah
            return terurut[n / 2];
        } else {
            // jumlah genap: rata-rata dua elemen tengah
            return (terurut[n / 2 - 1] + terurut[n / 2]) / 2;
        }
    }

    // Menghitung modus, mengembalikan array berisi semua modus.
    // Jika semua data muncul sama banyak, array kosong (tidak ada modus).
    public static double[] hitungModus(double[] data) {
        double[] terurut = urutkan(data);
        int n = terurut.length;

        // hitung frekuensi setiap nilai (data sudah terurut, jadi nilai sama berdekatan)
        double[] nilaiUnik = new double[n];
        int[] frekuensi = new int[n];
        int jumlahUnik = 0;

        for (int i = 0; i < n; i++) {
            if (jumlahUnik > 0 && nilaiUnik[jumlahUnik - 1] == terurut[i]) {
                frekuensi[jumlahUnik - 1]++;
            } else {
                nilaiUnik[jumlahUnik] = terurut[i];
                frekuensi[jumlahUnik] = 1;
                jumlahUnik++;
            }
        }

        // cari frekuensi terbesar
        int frekuensiMax = 0;
        for (int i = 0; i < jumlahUnik; i++) {
            if (frekuensi[i] > frekuensiMax) {
                frekuensiMax = frekuensi[i];
            }
        }

        // jika semua nilai frekuensinya sama, dianggap tidak ada modus
        if (frekuensiMax == 1 || jumlahUnik == 1 && n == 1) {
            return new double[0];
        }
        boolean semuaSama = true;
        for (int i = 0; i < jumlahUnik; i++) {
            if (frekuensi[i] != frekuensiMax) {
                semuaSama = false;
            }
        }
        if (semuaSama && jumlahUnik > 1) {
            return new double[0];
        }

        // kumpulkan semua nilai yang frekuensinya sama dengan frekuensi terbesar
        int banyakModus = 0;
        for (int i = 0; i < jumlahUnik; i++) {
            if (frekuensi[i] == frekuensiMax) {
                banyakModus++;
            }
        }
        double[] hasil = new double[banyakModus];
        int indeks = 0;
        for (int i = 0; i < jumlahUnik; i++) {
            if (frekuensi[i] == frekuensiMax) {
                hasil[indeks] = nilaiUnik[i];
                indeks++;
            }
        }
        return hasil;
    }

    // Menampilkan seluruh hasil statistik
    public static void tampilkanStatistik(double[] data) {
        System.out.print("\nData        : ");
        tampilkanArray(data);
        System.out.print("Data urut   : ");
        tampilkanArray(urutkan(data));
        System.out.println("Banyak data : " + data.length);
        System.out.printf("Mean        : %.2f%n", hitungMean(data));
        System.out.printf("Median      : %.2f%n", hitungMedian(data));

        double[] modus = hitungModus(data);
        if (modus.length == 0) {
            System.out.println("Modus       : tidak ada modus");
        } else {
            System.out.print("Modus       : ");
            for (int i = 0; i < modus.length; i++) {
                System.out.print(modus[i]);
                if (i < modus.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Contoh pemakaian dengan data tetap
        System.out.println("=== CONTOH 1: modus tunggal ===");
        tampilkanStatistik(new double[]{7, 8, 8, 9, 10, 8, 6});

        System.out.println("\n=== CONTOH 2: data genap, dua modus ===");
        tampilkanStatistik(new double[]{1, 2, 2, 3, 3, 4});

        System.out.println("\n=== CONTOH 3: tidak ada modus ===");
        tampilkanStatistik(new double[]{5, 1, 4, 2, 3});

        // Input dari pengguna
        System.out.println("\n=== INPUT MANUAL ===");
        int jumlah;
        do {
            System.out.print("Masukkan jumlah data (minimal 1): ");
            jumlah = input.nextInt();
            if (jumlah < 1) {
                System.out.println("Jumlah tidak valid, coba lagi!");
            }
        } while (jumlah < 1);

        double[] dataUser = new double[jumlah];
        for (int i = 0; i < jumlah; i++) {
            System.out.print("Data ke-" + (i + 1) + " : ");
            dataUser[i] = input.nextDouble();
        }

        tampilkanStatistik(dataUser);

        System.out.println("\nProgram selesai.");
        input.close();
    }
}