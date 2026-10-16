/*
 * =========================================================================
 * Nama Berkas : MatrixAddition.java
 * Deskripsi   : Komputasi Aljabar Linear Penjumlahan & Pengurangan Matriks
 * Pola Desain : Allman Bracing Style dengan Dokumentasi Method Internal
 * =========================================================================
 */

import java.util.Scanner;

public class MatrixAddition
{
    public int[][] add(int[][] a, int[][] b)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Nama   : add
         * Operasi: C[i][j] = A[i][j] + B[i][j]
         * Return : matriks hasil penjumlahan
         */
        int rows = a.length;
        int cols = a[0].length;
        int[][] result = new int[rows][cols];

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                result[i][j] = a[i][j] + b[i][j];
            }
        }
        return result;
    }

    public int[][] subtract(int[][] a, int[][] b)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Nama   : subtract
         * Operasi: C[i][j] = A[i][j] - B[i][j]
         * Return : matriks hasil pengurangan
         */
        int rows = a.length;
        int cols = a[0].length;
        int[][] result = new int[rows][cols];

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                result[i][j] = a[i][j] - b[i][j];
            }
        }
        return result;
    }

    public boolean ukuranSama(int[][] a, int[][] b)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Memvalidasi kesesuaian ordo baris dan kolom dua matriks
         */
        return a.length == b.length && a[0].length == b[0].length;
    }

    public static void tampilkanMatriks(String judul, int[][] m)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Visualisasi matriks 2D dalam bentuk tabel berpagar
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
         * Sanitasi pembacaan bilangan bulat dari scanner
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
         * Pembacaan dimensi matriks (positif > 0)
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
         * Pengisian elemen matriks interaktif baris demi kolom
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
         * Handler utama eksekusi operasi matriks konsol
         */
        Scanner input = new Scanner(System.in);
        MatrixAddition kalkulator = new MatrixAddition();

        int baris = bacaUkuran(input, "Masukkan jumlah baris : ");
        int kolom = bacaUkuran(input, "Masukkan jumlah kolom : ");

        int[][] matriksA = isiMatriks(input, "A", baris, kolom);
        int[][] matriksB = isiMatriks(input, "B", baris, kolom);

        int pilihan;
        do
        {
            System.out.println("\n===== PROGRAM OPERASI MATRIKS =====");
            System.out.println("1. Penjumlahan (A + B)");
            System.out.println("2. Pengurangan (A - B)");
            System.out.println("3. Tampilkan matriks A dan B");
            System.out.println("0. Keluar");
            pilihan = bacaAngka(input, "Pilih menu: ");

            switch (pilihan)
            {
                case 1:
                {
                    if (kalkulator.ukuranSama(matriksA, matriksB))
                    {
                        System.out.println();
                        tampilkanMatriks("Hasil A + B:", kalkulator.add(matriksA, matriksB));
                    }
                    else
                    {
                        System.out.println("Ukuran matriks berbeda!");
                    }
                    break;
                }
                case 2:
                {
                    if (kalkulator.ukuranSama(matriksA, matriksB))
                    {
                        System.out.println();
                        tampilkanMatriks("Hasil A - B:", kalkulator.subtract(matriksA, matriksB));
                    }
                    else
                    {
                        System.out.println("Ukuran matriks berbeda!");
                    }
                    break;
                }
                case 3:
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