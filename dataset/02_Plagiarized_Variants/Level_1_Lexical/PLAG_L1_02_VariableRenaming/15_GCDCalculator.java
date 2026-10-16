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
    public int hitungFPB(int x, int y) {
        while (y != 0) {
            int sisa = y;
            y = x % y;
            x = sisa;
        }
        return x;
    }

    // Algoritma Euclid secara rekursif sebagai pembanding
    public int hitungFPBRekursif(int x, int y) {
        if (y == 0) {
            return x;
        }
        return hitungFPBRekursif(y, x % y);
    }

    // Menghitung KPK dengan rumus KPK = (x x y) / FPB (pakai long agar aman)
    public long hitungKPK(int x, int y) {
        if (x == 0 || y == 0) {
            return 0;
        }
        return (long) x / hitungFPB(x, y) * y;
    }

    // Menghitung FPB dari tiga bilangan
    public int hitungFPBTiga(int x, int y, int z) {
        return hitungFPB(hitungFPB(x, y), z);
    }

    // Mengecek apakah dua bilangan relatif prima (FPB = 1)
    public boolean coprimeDua(int x, int y) {
        return hitungFPB(x, y) == 1;
    }

    // Menampilkan semua faktor persekutuan dari dua bilangan
    public void tampilkanFaktorSekutu(int x, int y) {
        int fpb = hitungFPB(x, y);
        System.out.print("Faktor persekutuan " + x + " dan " + y + " : ");
        for (int f = 1; f <= fpb; f++) {
            if (fpb % f == 0) {
                System.out.print(f + " ");
            }
        }
        System.out.println();
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

    // Membaca bilangan bulat positif (lebih dari 0)
    public static int ambilPositif(Scanner scanner, String teks) {
        int val = 0;
        while (val <= 0) {
            val = ambilAngka(scanner, teks);
            if (val <= 0) {
                System.out.println("Bilangan harus lebih dari 0!");
            }
        }
        return val;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GCDCalculator alat = new GCDCalculator();

        int menu;
        do {
            System.out.println("\n===== PROGRAM FPB DAN KPK =====");
            System.out.println("1. Hitung FPB dua bilangan");
            System.out.println("2. Hitung KPK dua bilangan");
            System.out.println("3. Hitung FPB tiga bilangan");
            System.out.println("4. Cek relatif prima");
            System.out.println("5. Tampilkan faktor persekutuan");
            System.out.println("6. Bandingkan iteratif vs rekursif");
            System.out.println("0. Keluar");
            menu = ambilAngka(scanner, "Pilih menu: ");

            switch (menu) {
                case 1:
                    int x1 = ambilPositif(scanner, "Bilangan pertama : ");
                    int y1 = ambilPositif(scanner, "Bilangan kedua   : ");
                    System.out.println("FPB(" + x1 + ", " + y1 + ") = "
                            + alat.hitungFPB(x1, y1));
                    break;
                case 2:
                    int x2 = ambilPositif(scanner, "Bilangan pertama : ");
                    int y2 = ambilPositif(scanner, "Bilangan kedua   : ");
                    System.out.println("KPK(" + x2 + ", " + y2 + ") = "
                            + alat.hitungKPK(x2, y2));
                    break;
                case 3:
                    int x3 = ambilPositif(scanner, "Bilangan pertama : ");
                    int y3 = ambilPositif(scanner, "Bilangan kedua   : ");
                    int z3 = ambilPositif(scanner, "Bilangan ketiga  : ");
                    System.out.println("FPB(" + x3 + ", " + y3 + ", " + z3 + ") = "
                            + alat.hitungFPBTiga(x3, y3, z3));
                    break;
                case 4:
                    int x4 = ambilPositif(scanner, "Bilangan pertama : ");
                    int y4 = ambilPositif(scanner, "Bilangan kedua   : ");
                    if (alat.coprimeDua(x4, y4)) {
                        System.out.println(x4 + " dan " + y4 + " relatif prima.");
                    } else {
                        System.out.println(x4 + " dan " + y4 + " tidak relatif prima.");
                    }
                    break;
                case 5:
                    int x5 = ambilPositif(scanner, "Bilangan pertama : ");
                    int y5 = ambilPositif(scanner, "Bilangan kedua   : ");
                    alat.tampilkanFaktorSekutu(x5, y5);
                    break;
                case 6:
                    int x6 = ambilPositif(scanner, "Bilangan pertama : ");
                    int y6 = ambilPositif(scanner, "Bilangan kedua   : ");
                    int hasilIter = alat.hitungFPB(x6, y6);
                    int hasilRek = alat.hitungFPBRekursif(x6, y6);
                    System.out.println("Hasil iteratif : " + hasilIter);
                    System.out.println("Hasil rekursif : " + hasilRek);
                    if (hasilIter == hasilRek) {
                        System.out.println("Kedua metode menghasilkan nilai yang sama.");
                    } else {
                        System.out.println("Hasil berbeda, ada yang salah!");
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