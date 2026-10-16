/*
 * Program   : GCDCalculator
 * Deskripsi : Menghitung FPB (Faktor Persekutuan Terbesar) dari dua
 *             bilangan menggunakan algoritma Euclid. Dilengkapi versi
 *             rekursif, KPK, FPB dari tiga bilangan, pengecekan
 *             bilangan relatif prima, input dari pengguna, dan menu
 *             sederhana.
 */
import java.util.Scanner;

public class GCDCalculator {

    // Logika inti: algoritma Euclid secara iteratif
    public int cariFpbIter(int p, int q) {
        while (q != 0) {
            int sementara = q;
            q = p % q;
            p = sementara;
        }
        return p;
    }

    // Algoritma Euclid secara rekursif sebagai pembanding
    public int cariFpbRek(int p, int q) {
        if (q == 0) {
            return p;
        }
        return cariFpbRek(q, p % q);
    }

    // Menghitung KPK dengan rumus KPK = (a x b) / FPB (pakai long agar aman)
    public long cariKpk(int p, int q) {
        if (p == 0 || q == 0) {
            return 0;
        }
        return (long) p / cariFpbIter(p, q) * q;
    }

    // Menghitung FPB dari tiga bilangan
    public int cariFpbTiga(int p, int q, int r) {
        return cariFpbIter(cariFpbIter(p, q), r);
    }

    // Mengecek apakah dua bilangan relatif prima (FPB = 1)
    public boolean salingPrima(int p, int q) {
        return cariFpbIter(p, q) == 1;
    }

    // Menampilkan semua faktor persekutuan dari dua bilangan
    public void cetakFaktorBersama(int p, int q) {
        int pembagiTerbesar = cariFpbIter(p, q);
        System.out.print(new StringBuilder().append("Faktor persekutuan ").append(p).append(" dan ").append(q).append(" : ").toString());
        for (int k = 1; k <= pembagiTerbesar; k++) {
            if (pembagiTerbesar % k == 0) {
                System.out.print(new StringBuilder().append(k).append(" ").toString());
            }
        }
        System.out.print(System.lineSeparator());
    }

    // Membaca bilangan bulat dengan validasi sederhana
    public static int ambilInteger(Scanner pemindai, String keterangan) {
        System.out.print(keterangan);
        while (!pemindai.hasNextInt()) {
            System.out.print("Input harus angka bulat, ulangi: ");
            pemindai.next();
        }
        return pemindai.nextInt();
    }

    // Membaca bilangan bulat positif (lebih dari 0)
    public static int ambilPositif(Scanner pemindai, String keterangan) {
        int angka = 0;
        while (angka <= 0) {
            angka = ambilInteger(pemindai, keterangan);
            if (angka <= 0) {
                System.out.print("Bilangan harus lebih dari 0!");
                System.out.print(System.lineSeparator());
            }
        }
        return angka;
    }

    public static void main(String[] args) {
        Scanner pemindai = new Scanner(System.in);
        GCDCalculator pengolah = new GCDCalculator();

        int opsi;
        do {
            System.out.print("\n===== PROGRAM FPB DAN KPK =====");
            System.out.print(System.lineSeparator());
            System.out.print("1. Hitung FPB dua bilangan");
            System.out.print(System.lineSeparator());
            System.out.print("2. Hitung KPK dua bilangan");
            System.out.print(System.lineSeparator());
            System.out.print("3. Hitung FPB tiga bilangan");
            System.out.print(System.lineSeparator());
            System.out.print("4. Cek relatif prima");
            System.out.print(System.lineSeparator());
            System.out.print("5. Tampilkan faktor persekutuan");
            System.out.print(System.lineSeparator());
            System.out.print("6. Bandingkan iteratif vs rekursif");
            System.out.print(System.lineSeparator());
            System.out.print("0. Keluar");
            System.out.print(System.lineSeparator());
            opsi = ambilInteger(pemindai, "Pilih menu: ");

            switch (opsi) {
                case 1:
                    int m1 = ambilPositif(pemindai, "Bilangan pertama : ");
                    int n1 = ambilPositif(pemindai, "Bilangan kedua   : ");
                    System.out.print(new StringBuilder().append("FPB(").append(m1).append(", ").append(n1).append(") = ")
                            .append(pengolah.cariFpbIter(m1, n1)).toString());
                    System.out.print(System.lineSeparator());
                    break;
                case 2:
                    int m2 = ambilPositif(pemindai, "Bilangan pertama : ");
                    int n2 = ambilPositif(pemindai, "Bilangan kedua   : ");
                    System.out.print(new StringBuilder().append("KPK(").append(m2).append(", ").append(n2).append(") = ")
                            .append(pengolah.cariKpk(m2, n2)).toString());
                    System.out.print(System.lineSeparator());
                    break;
                case 3:
                    int m3 = ambilPositif(pemindai, "Bilangan pertama : ");
                    int n3 = ambilPositif(pemindai, "Bilangan kedua   : ");
                    int o3 = ambilPositif(pemindai, "Bilangan ketiga  : ");
                    System.out.print(new StringBuilder().append("FPB(").append(m3).append(", ").append(n3).append(", ").append(o3).append(") = ")
                            .append(pengolah.cariFpbTiga(m3, n3, o3)).toString());
                    System.out.print(System.lineSeparator());
                    break;
                case 4:
                    int m4 = ambilPositif(pemindai, "Bilangan pertama : ");
                    int n4 = ambilPositif(pemindai, "Bilangan kedua   : ");
                    if (pengolah.salingPrima(m4, n4)) {
                        System.out.print(new StringBuilder().append(m4).append(" dan ").append(n4).append(" relatif prima.").toString());
                        System.out.print(System.lineSeparator());
                    } else {
                        System.out.print(new StringBuilder().append(m4).append(" dan ").append(n4).append(" tidak relatif prima.").toString());
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 5:
                    int m5 = ambilPositif(pemindai, "Bilangan pertama : ");
                    int n5 = ambilPositif(pemindai, "Bilangan kedua   : ");
                    pengolah.cetakFaktorBersama(m5, n5);
                    break;
                case 6:
                    int m6 = ambilPositif(pemindai, "Bilangan pertama : ");
                    int n6 = ambilPositif(pemindai, "Bilangan kedua   : ");
                    int hslIter = pengolah.cariFpbIter(m6, n6);
                    int hslRek = pengolah.cariFpbRek(m6, n6);
                    System.out.print(new StringBuilder().append("Hasil iteratif : ").append(hslIter).toString());
                    System.out.print(System.lineSeparator());
                    System.out.print(new StringBuilder().append("Hasil rekursif : ").append(hslRek).toString());
                    System.out.print(System.lineSeparator());
                    if (hslIter == hslRek) {
                        System.out.print("Kedua metode menghasilkan nilai yang sama.");
                        System.out.print(System.lineSeparator());
                    } else {
                        System.out.print("Hasil berbeda, ada yang salah!");
                        System.out.print(System.lineSeparator());
                    }
                    break;
                case 0:
                    System.out.print("Terima kasih, program selesai.");
                    System.out.print(System.lineSeparator());
                    break;
                default:
                    System.out.print("Menu tidak tersedia!");
                    System.out.print(System.lineSeparator());
            }
        } while (opsi != 0);

        pemindai.close();
    }
}