/*
 * =========================================================================
 * Nama Berkas : MatrixMultiplication.java
 * Deskripsi   : Komputasi Perkalian Matriks, Skalar, dan Transposisi
 * Layout      : Allman Bracing Convention & Restructured Method Documentation
 * =========================================================================
 */

import java.util.Scanner;

public class MatrixMultiplication
{
    public int[][] multiply(int[][] a, int[][] b)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Nama    : multiply
         * Algoritma: Dot product baris matriks A dengan kolom matriks B
         * Return  : Matriks hasil perkalian dimensi (r1 x c2)
         */
        int r1 = a.length;
        int c1 = a[0].length;
        int c2 = b[0].length;
        int[][] product = new int[r1][c2];

        for (int i = 0; i < r1; i++)
        {
            for (int j = 0; j < c2; j++)
            {
                for (int k = 0; k < c1; k++)
                {
                    product[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return product;
    }

    public boolean bisaDikalikan(int[][] a, int[][] b)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Memastikan kecocokan dimensi ordo: kolom A == baris B
         */
        return a[0].length == b.length;
    }

    public int[][] kaliSkalar(int[][] m, int skalar)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Mengalikan setiap elemen m[i][j] dengan konstanta skalar
         */
        int[][] hasil = new int[m.length][m[0].length];
        for (int i = 0; i < m.length; i++)
        {
            for (int j = 0; j < m[0].length; j++)
            {
                hasil[i][j] = m[i][j] * skalar;
            }
        }
        return hasil;
    }

    public int[][] transpose(int[][] m)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Melakukan rotasi matriks: menukar indeks baris menjadi kolom
         */
        int[][] hasil = new int[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
        {
            for (int j = 0; j < m[0].length; j++)
            {
                hasil[j][i] = m[i][j];
            }
        }
        return hasil;
    }

    public static void tampilkanMatriks(String judul, int[][] m)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Menampilkan representasi visual elemen matriks
         */
        System.out.println(judul);
        for (int i = 0; i < m.length; i++)
        {
            System.out.print("| ");
            for (int j = 0; j < m[i].length; j++)
            {
                System.out.print(m[i][j] + "\t");
            }
            System.out.println("|");
        }
    }

    public static int bacaAngka(Scanner input, String pesan)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Validasi input integer dari keyboard
         */
        System.out.print(pesan);
        while (!input.hasNextInt())
        {
            System.out.print("Input harus angka bulat, ulangi: ");
            input.next();
        }
        return input.nextInt();
    }

    public static int bacaUkuran(Scanner input, String pesan)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Memastikan masukan ukuran matriks bernilai positif
         */
        int nilai = 0;
        while (nilai <= 0)
        {
            nilai = bacaAngka(input, pesan);
            if (nilai <= 0)
            {
                System.out.println("Ukuran harus lebih dari 0!");
            }
        }
        return nilai;
    }

    public static int[][] isiMatriks(Scanner input, String nama, int baris, int kolom)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Loop pengisian nilai tiap sel matriks
         */
        int[][] m = new int[baris][kolom];
        System.out.println("\nIsi matriks " + nama + ":");

        for (int i = 0; i < baris; i++)
        {
            for (int j = 0; j < kolom; j++)
            {
                m[i][j] = bacaAngka(input, nama + "[" + i + "][" + j + "] = ");
            }
        }
        return m;
    }

    public static void main(String[] args)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Alur eksekusi menu pengujian operasi perkalian matriks
         */
        Scanner input = new Scanner(System.in);
        MatrixMultiplication kalkulator = new MatrixMultiplication();

        System.out.println("=== Ukuran Matriks A ===");
        int barisA = bacaUkuran(input, "Jumlah baris A : ");
        int kolomA = bacaUkuran(input, "Jumlah kolom A : ");

        System.out.println("=== Ukuran Matriks B ===");
        int barisB = bacaUkuran(input, "Jumlah baris B : ");
        int kolomB = bacaUkuran(input, "Jumlah kolom B : ");

        int[][] matriksA = isiMatriks(input, "A", barisA, kolomA);
        int[][] matriksB = isiMatriks(input, "B", barisB, kolomB);

        int pilihan;
        do
        {
            System.out.println("\n===== PROGRAM OPERASI MATRIKS =====");
            System.out.println("1. Perkalian matriks (A x B)");
            System.out.println("2. Perkalian dengan skalar");
            System.out.println("3. Transpose matriks");
            System.out.println("4. Tampilkan matriks A dan B");
            System.out.println("0. Keluar");
            pilihan = bacaAngka(input, "Pilih menu: ");

            switch (pilihan)
            {
                case 1:
                {
                    if (kalkulator.bisaDikalikan(matriksA, matriksB))
                    {
                        System.out.println();
                        tampilkanMatriks("Hasil A x B:", kalkulator.multiply(matriksA, matriksB));
                    }
                    else
                    {
                        System.out.println("Tidak bisa dikalikan: kolom A (" + kolomA
                                + ") harus sama dengan baris B (" + barisB + ")!");
                    }
                    break;
                }
                case 2:
                {
                    int skalar = bacaAngka(input, "Masukkan nilai skalar : ");
                    System.out.println();
                    tampilkanMatriks("Skalar x A:", kalkulator.kaliSkalar(matriksA, skalar));
                    tampilkanMatriks("Skalar x B:", kalkulator.kaliSkalar(matriksB, skalar));
                    break;
                }
                case 3:
                {
                    System.out.println();
                    tampilkanMatriks("Transpose A:", kalkulator.transpose(matriksA));
                    tampilkanMatriks("Transpose B:", kalkulator.transpose(matriksB));
                    break;
                }
                case 4:
                {
                    System.out.println();
                    tampilkanMatriks("Matriks A:", matriksA);
                    tampilkanMatriks("Matriks B:", matriksB);
                    break;
                }
                case 0:
                {
                    System.out.println("Terima kasih, program selesai.");
                    break;
                }
                default:
                {
                    System.out.println("Menu tidak tersedia!");
                    break;
                }
            }
        }
        while (pilihan != 0);

        input.close();
    }
}