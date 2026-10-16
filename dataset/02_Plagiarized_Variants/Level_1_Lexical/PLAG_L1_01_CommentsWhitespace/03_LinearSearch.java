/* ========================================================================= *
 * File        : LinearSearch.java
 * Implementasi: Algoritma Pencarian Sekuensial / Linear Search
 * Keterangan  : Mendukung pencarian single, multi-occurrence, dan step count
 * ========================================================================= */

import java.util.Scanner;

public class LinearSearch
{
    /* ---------------------------------------------------------------------
     * Operasi dasar pencarian sekuensial (first match)
     * --------------------------------------------------------------------- */
    public static int search( int[] arr, int target )
    {
        for ( int i = 0; i < arr.length; i++ )
        {
            if ( arr[i] == target )
            {
                return i;
            }
        }
        return -1;
    }

    /* Menghitung frekuensi kemunculan nilai target */
    public static int hitungKemunculan( int[] arr, int target )
    {
        int jumlah = 0;
        for ( int i = 0; i < arr.length; i++ )
        {
            if ( arr[i] == target )
            {
                jumlah++;
            }
        }
        return jumlah;
    }

    /* Cetak seluruh posisi indeks yang sesuai target */
    public static void tampilkanSemuaIndeks( int[] arr, int target )
    {
        boolean ditemukan = false;
        System.out.print( "Indeks kemunculan " + target + " : " );

        for ( int i = 0; i < arr.length; i++ )
        {
            if ( arr[i] == target )
            {
                System.out.print( i + " " );
                ditemukan = true;
            }
        }

        if ( !ditemukan )
        {
            System.out.print( "(tidak ada)" );
        }
        System.out.println();
    }

    /* Menghitung total iterasi sebelum target ditemukan / loop selesai */
    public static int hitungLangkah( int[] arr, int target )
    {
        int langkah = 0;
        for ( int i = 0; i < arr.length; i++ )
        {
            langkah++;
            if ( arr[i] == target )
            {
                break;
            }
        }
        return langkah;
    }

    /* Menampilkan format data array */
    public static void tampilkanArray( int[] arr )
    {
        System.out.print( "Isi array : [" );
        for ( int i = 0; i < arr.length; i++ )
        {
            System.out.print( arr[i] );
            if ( i < arr.length - 1 )
            {
                System.out.print( ", " );
            }
        }
        System.out.println( "]" );
    }

    /* Helper pembacaan masukan integer */
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

        int jumlah = 0;
        while ( jumlah <= 0 )
        {
            jumlah = bacaAngka( input, "Masukkan jumlah data: " );
            if ( jumlah <= 0 )
            {
                System.out.println( "Jumlah data harus lebih dari 0!" );
            }
        }

        int[] data = new int[jumlah];
        for ( int i = 0; i < jumlah; i++ )
        {
            data[i] = bacaAngka( input, "Data ke-" + (i + 1) + " : " );
        }

        int pilihan;
        do
        {
            System.out.println( "\n===== PROGRAM LINEAR SEARCH =====" );
            System.out.println( "1. Cari satu nilai (indeks pertama)" );
            System.out.println( "2. Cari semua kemunculan" );
            System.out.println( "3. Tampilkan isi array" );
            System.out.println( "0. Keluar" );
            pilihan = bacaAngka( input, "Pilih menu: " );

            switch ( pilihan )
            {
                case 1:
                    int target1 = bacaAngka( input, "Nilai yang dicari: " );
                    int hasil   = search( data, target1 );
                    if ( hasil == -1 )
                    {
                        System.out.println( "Nilai " + target1 + " tidak ditemukan." );
                    }
                    else
                    {
                        System.out.println( "Nilai " + target1 + " ditemukan di indeks " + hasil );
                    }
                    System.out.println( "Jumlah langkah : " + hitungLangkah( data, target1 ) );
                    break;

                case 2:
                    int target2 = bacaAngka( input, "Nilai yang dicari: " );
                    System.out.println( "Jumlah kemunculan : " + hitungKemunculan( data, target2 ) );
                    tampilkanSemuaIndeks( data, target2 );
                    break;

                case 3:
                    tampilkanArray( data );
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