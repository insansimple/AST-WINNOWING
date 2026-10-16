/*
 * =========================================================================
 * Nama Berkas : SimpleQueue.java
 * Deskripsi   : Struktur Data Antrian Melingkar (FIFO - First In First Out)
 * Gaya Kode   : Allman Bracing Style dengan Dokumentasi Method Internal
 * =========================================================================
 */

import java.util.Scanner;

public class SimpleQueue
{
    private int[] items;
    private int front;
    private int rear;
    private int count;

    public SimpleQueue(int size)
    {
        /*
         * [DOKUMENTASI KONSTRUKTOR]
         * Inisialisasi circular buffer berbasis array
         * Pointer front, rear, dan pencatat kapasitas efektif count di-reset ke 0
         */
        items = new int[size];
        front = 0;
        rear = 0;
        count = 0;
    }

    public void enqueue(int val)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Menambahkan item ke posisi pointer rear secara modular
         */
        items[rear] = val;
        rear = (rear + 1) % items.length;
        count++;
    }

    public int dequeue()
    {
        /*
         * [DOKUMENTASI METHOD]
         * Mengambil dan menghapus item dari posisi pointer front secara modular
         */
        int val = items[front];
        front = (front + 1) % items.length;
        count--;
        return val;
    }

    public boolean isEmpty()
    {
        /*
         * [DOKUMENTASI METHOD]
         * Mengembalikan status kekosongan antrian
         */
        return count == 0;
    }

    public boolean isFull()
    {
        /*
         * [DOKUMENTASI METHOD]
         * Mengembalikan status kejenuhan antrian (kapasitas penuh)
         */
        return count == items.length;
    }

    public int peek()
    {
        /*
         * [DOKUMENTASI METHOD]
         * Melihat elemen terdepan pada pointer front tanpa penghapusan
         */
        return items[front];
    }

    public int size()
    {
        /*
         * [DOKUMENTASI METHOD]
         * Mengembalikan total elemen aktif yang mengantri
         */
        return count;
    }

    public void tampilkan()
    {
        /*
         * [DOKUMENTASI METHOD]
         * Visualisasi sekuensial antrian dari front menuju rear
         */
        if (isEmpty())
        {
            System.out.println("Antrian kosong.");
            return;
        }

        System.out.print("Isi antrian (depan -> belakang): ");
        int index = front;
        for (int i = 0; i < count; i++)
        {
            System.out.print("[" + items[index] + "] ");
            index = (index + 1) % items.length;
        }
        System.out.println();
    }

    public static void simulasiLoket(int jumlahPelanggan)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Contoh penerapan kasus nyata simulasi pelayanan loket perbankan/tiket
         */
        SimpleQueue antrian = new SimpleQueue(jumlahPelanggan);
        System.out.println("\n=== Simulasi Antrian Loket ===");

        for (int i = 1; i <= jumlahPelanggan; i++)
        {
            antrian.enqueue(i);
            System.out.println("Pelanggan nomor " + i + " masuk antrian.");
        }

        while (!antrian.isEmpty())
        {
            System.out.println("Melayani pelanggan nomor " + antrian.dequeue());
        }
        System.out.println("Semua pelanggan sudah dilayani.");
    }

    public static int bacaAngka(Scanner input, String pesan)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Pembacaan integer scanner dengan validasi input
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
         * Prosedur kendali antarmuka CLI queue
         */
        Scanner input = new Scanner(System.in);

        int ukuran = 0;
        while (ukuran <= 0)
        {
            ukuran = bacaAngka(input, "Masukkan ukuran antrian: ");
            if (ukuran <= 0)
            {
                System.out.println("Ukuran harus lebih dari 0!");
            }
        }
        SimpleQueue antrian = new SimpleQueue(ukuran);

        int pilihan;
        do
        {
            System.out.println("\n===== PROGRAM QUEUE =====");
            System.out.println("1. Enqueue (tambah elemen)");
            System.out.println("2. Dequeue (ambil elemen)");
            System.out.println("3. Peek (lihat elemen depan)");
            System.out.println("4. Tampilkan isi antrian");
            System.out.println("5. Simulasi antrian loket");
            System.out.println("0. Keluar");
            pilihan = bacaAngka(input, "Pilih menu: ");

            switch (pilihan)
            {
                case 1:
                {
                    if (antrian.isFull())
                    {
                        System.out.println("Antrian penuh, tidak bisa enqueue!");
                    }
                    else
                    {
                        int nilai = bacaAngka(input, "Masukkan nilai: ");
                        antrian.enqueue(nilai);
                        System.out.println(nilai + " berhasil masuk antrian.");
                    }
                    break;
                }
                case 2:
                {
                    if (antrian.isEmpty())
                    {
                        System.out.println("Antrian kosong, tidak bisa dequeue!");
                    }
                    else
                    {
                        System.out.println("Elemen yang diambil: " + antrian.dequeue());
                    }
                    break;
                }
                case 3:
                {
                    if (antrian.isEmpty())
                    {
                        System.out.println("Antrian kosong!");
                    }
                    else
                    {
                        System.out.println("Elemen terdepan: " + antrian.peek());
                    }
                    break;
                }
                case 4:
                {
                    antrian.tampilkan();
                    System.out.println("Jumlah elemen: " + antrian.size());
                    break;
                }
                case 5:
                {
                    int jumlah = bacaAngka(input, "Jumlah pelanggan: ");
                    if (jumlah > 0)
                    {
                        simulasiLoket(jumlah);
                    }
                    else
                    {
                        System.out.println("Jumlah pelanggan harus lebih dari 0!");
                    }
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