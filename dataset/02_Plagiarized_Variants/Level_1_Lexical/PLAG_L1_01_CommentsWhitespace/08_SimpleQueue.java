/* =========================================================================
 * Modul    : SimpleQueue.java
 * Deskripsi: Struktur Data Antrean Sirkular (Circular FIFO Queue)
 * ========================================================================= */

import java.util.Scanner;

public class SimpleQueue
{
    private int[] items;
    private int front;
    private int rear;
    private int count;

    /* Konstruktor inisialisasi antrean */
    public SimpleQueue( int size )
    {
        items = new int[size];
        front = 0;
        rear  = 0;
        count = 0;
    }

    /* Memasukkan data baru ke ekor antrean */
    public void enqueue( int val )
    {
        items[rear] = val;
        rear = ( rear + 1 ) % items.length;
        count++;
    }

    /* Mengeluarkan data dari kepala antrean */
    public int dequeue()
    {
        int val = items[front];
        front = ( front + 1 ) % items.length;
        count--;
        return val;
    }

    public boolean isEmpty()
    {
        return count == 0;
    }

    public boolean isFull()
    {
        return count == items.length;
    }

    public int peek()
    {
        return items[front];
    }

    public int size()
    {
        return count;
    }

    /* Format cetak status antrean */
    public void tampilkan()
    {
        if ( isEmpty() )
        {
            System.out.println( "Antrian kosong." );
            return;
        }

        System.out.print( "Isi antrian (depan -> belakang): " );
        int index = front;
        for ( int i = 0; i < count; i++ )
        {
            System.out.print( "[" + items[index] + "] " );
            index = ( index + 1 ) % items.length;
        }
        System.out.println();
    }

    /* Model simulasi antrean pada sistem loket */
    public static void simulasiLoket( int jumlahPelanggan )
    {
        SimpleQueue antrian = new SimpleQueue( jumlahPelanggan );
        System.out.println( "\n=== Simulasi Antrian Loket ===" );

        for ( int i = 1; i <= jumlahPelanggan; i++ )
        {
            antrian.enqueue( i );
            System.out.println( "Pelanggan nomor " + i + " masuk antrian." );
        }

        while ( !antrian.isEmpty() )
        {
            System.out.println( "Melayani pelanggan nomor " + antrian.dequeue() );
        }
        System.out.println( "Semua pelanggan sudah dilayani." );
    }

    /* Input helper untuk angka bulat */
    public static int bacaAngka( Scanner input, String pesan )
    {
        System.out.print( pesan );
        while ( !input.hasNextInt() )
        {
            System.out.print( "Input harus angka bulat, ulangi: " );
            input.next();
        }
        return input.nextInt();
    }

    public static void main( String[] args )
    {
        Scanner input = new Scanner( System.in );

        int ukuran = 0;
        while ( ukuran <= 0 )
        {
            ukuran = bacaAngka( input, "Masukkan ukuran antrian: " );
            if ( ukuran <= 0 )
            {
                System.out.println( "Ukuran harus lebih dari 0!" );
            }
        }
        SimpleQueue antrian = new SimpleQueue( ukuran );

        int pilihan;
        do
        {
            System.out.println( "\n===== PROGRAM QUEUE =====" );
            System.out.println( "1. Enqueue (tambah elemen)" );
            System.out.println( "2. Dequeue (ambil elemen)" );
            System.out.println( "3. Peek (lihat elemen depan)" );
            System.out.println( "4. Tampilkan isi antrian" );
            System.out.println( "5. Simulasi antrian loket" );
            System.out.println( "0. Keluar" );
            pilihan = bacaAngka( input, "Pilih menu: " );

            switch ( pilihan )
            {
                case 1:
                    if ( antrian.isFull() )
                    {
                        System.out.println( "Antrian penuh, tidak bisa enqueue!" );
                    }
                    else
                    {
                        int nilai = bacaAngka( input, "Masukkan nilai: " );
                        antrian.enqueue( nilai );
                        System.out.println( nilai + " berhasil masuk antrian." );
                    }
                    break;

                case 2:
                    if ( antrian.isEmpty() )
                    {
                        System.out.println( "Antrian kosong, tidak bisa dequeue!" );
                    }
                    else
                    {
                        System.out.println( "Elemen yang diambil: " + antrian.dequeue() );
                    }
                    break;

                case 3:
                    if ( antrian.isEmpty() )
                    {
                        System.out.println( "Antrian kosong!" );
                    }
                    else
                    {
                        System.out.println( "Elemen terdepan: " + antrian.peek() );
                    }
                    break;

                case 4:
                    antrian.tampilkan();
                    System.out.println( "Jumlah elemen: " + antrian.size() );
                    break;

                case 5:
                    int jumlah = bacaAngka( input, "Jumlah pelanggan: " );
                    if ( jumlah > 0 )
                    {
                        simulasiLoket( jumlah );
                    }
                    else
                    {
                        System.out.println( "Jumlah pelanggan harus lebih dari 0!" );
                    }
                    break;

                case 0:
                    System.out.println( "Terima kasih, program selesai." );
                    break;

                default:
                    System.out.println( "Menu tidak tersedia!" );
            }
        } while ( pilihan != 0 );

        input.close();
    }
}