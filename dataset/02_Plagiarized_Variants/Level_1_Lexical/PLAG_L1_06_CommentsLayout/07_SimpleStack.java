/*
 * =========================================================================
 * Nama Berkas : SimpleStack.java
 * Deskripsi   : Struktur Data Tumpukan (LIFO - Last In First Out)
 * Tata Letak  : Allman Bracing Style dengan Dokumentasi Terstruktur
 * =========================================================================
 */

import java.util.Scanner;

public class SimpleStack
{
    private int[] elements;
    private int top;

    public SimpleStack(int capacity)
    {
        /*
         * [DOKUMENTASI KONSTRUKTOR]
         * Inisialisasi array penyimpan data berukuran fixed capacity
         * dan inisialisasi pointer puncak stack (top = -1)
         */
        elements = new int[capacity];
        top = -1;
    }

    public void push(int item)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Operasi penyisipan elemen ke posisi indeks top berikutnya
         */
        elements[++top] = item;
    }

    public int pop()
    {
        /*
         * [DOKUMENTASI METHOD]
         * Operasi pengambilan dan eliminasi elemen di puncak stack
         */
        return elements[top--];
    }

    public boolean isEmpty()
    {
        /*
         * [DOKUMENTASI METHOD]
         * Memeriksa apakah tumpukan dalam kondisi hampa
         */
        return top == -1;
    }

    public boolean isFull()
    {
        /*
         * [DOKUMENTASI METHOD]
         * Memeriksa apakah kapasitas maksimum tumpukan telah tercapai
         */
        return top == elements.length - 1;
    }

    public int peek()
    {
        /*
         * [DOKUMENTASI METHOD]
         * Inspeksi nilai pada puncak stack tanpa memodifikasi pointer
         */
        return elements[top];
    }

    public int size()
    {
        /*
         * [DOKUMENTASI METHOD]
         * Menghitung kuantitas data yang tersimpan saat ini
         */
        return top + 1;
    }

    public void tampilkan()
    {
        /*
         * [DOKUMENTASI METHOD]
         * Visualisasi transversal stack dari elemen puncak ke elemen terbawah
         */
        if (isEmpty())
        {
            System.out.println("Stack kosong.");
            return;
        }

        System.out.println("Isi stack (puncak -> dasar):");
        for (int i = top; i >= 0; i--)
        {
            System.out.println("| " + elements[i] + " |");
        }
        System.out.println("-----");
    }

    public static String balikKata(String kata)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Contoh aplikasi: inversi urutan karakter menggunakan mekanisme LIFO
         */
        SimpleStack tumpukan = new SimpleStack(kata.length());
        for (int i = 0; i < kata.length(); i++)
        {
            tumpukan.push(kata.charAt(i));
        }

        String hasil = "";
        while (!tumpukan.isEmpty())
        {
            hasil += (char) tumpukan.pop();
        }
        return hasil;
    }

    public static int bacaAngka(Scanner input, String pesan)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Pembacaan nilai integer dengan mekanisme sanitasi token non-angka
         */
        System.out.print(pesan);
        while (!input.hasNextInt())
        {
            System.out.print("Input harus angka bulat, ulangi: ");
            input.next();
        }
        return input.nextInt();
    }

    public static void main(String[] args)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Entry point pengujian operasi tumpukan berbasis konsol interaktif
         */
        Scanner input = new Scanner(System.in);

        int kapasitas = 0;
        while (kapasitas <= 0)
        {
            kapasitas = bacaAngka(input, "Masukkan kapasitas stack: ");
            if (kapasitas <= 0)
            {
                System.out.println("Kapasitas harus lebih dari 0!");
            }
        }
        SimpleStack stack = new SimpleStack(kapasitas);

        int pilihan;
        do
        {
            System.out.println("\n===== PROGRAM STACK =====");
            System.out.println("1. Push (tambah elemen)");
            System.out.println("2. Pop (ambil elemen)");
            System.out.println("3. Peek (lihat elemen puncak)");
            System.out.println("4. Tampilkan isi stack");
            System.out.println("5. Balik kata menggunakan stack");
            System.out.println("0. Keluar");
            pilihan = bacaAngka(input, "Pilih menu: ");

            switch (pilihan)
            {
                case 1:
                {
                    if (stack.isFull())
                    {
                        System.out.println("Stack penuh, tidak bisa push!");
                    }
                    else
                    {
                        int nilai = bacaAngka(input, "Masukkan nilai: ");
                        stack.push(nilai);
                        System.out.println(nilai + " berhasil ditambahkan.");
                    }
                    break;
                }
                case 2:
                {
                    if (stack.isEmpty())
                    {
                        System.out.println("Stack kosong, tidak bisa pop!");
                    }
                    else
                    {
                        System.out.println("Elemen yang diambil: " + stack.pop());
                    }
                    break;
                }
                case 3:
                {
                    if (stack.isEmpty())
                    {
                        System.out.println("Stack kosong!");
                    }
                    else
                    {
                        System.out.println("Elemen puncak: " + stack.peek());
                    }
                    break;
                }
                case 4:
                {
                    stack.tampilkan();
                    System.out.println("Jumlah elemen: " + stack.size());
                    break;
                }
                case 5:
                {
                    System.out.print("Masukkan kata: ");
                    String kata = input.next();
                    System.out.println("Hasil dibalik: " + balikKata(kata));
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