/* ============================================================================
 * Sumber      : FibonacciCalculator.java
 * Deskripsi   : Pembangkit dan Verifikator Barisan Fibonacci
 * ============================================================================ */

import java.util.Scanner;

public class FibonacciCalculator
{
    /* Batas atas suku untuk menghindari integer overflow */
    static final int BATAS_MAKS = 46;

    /* Pembangkitan suku ke-n secara linear/iteratif */
    public int fibonacci( int n )
    {
        if ( n <= 0 ) return 0;
        if ( n == 1 ) return 1;

        int prev = 0, curr = 1;
        for ( int i = 2; i <= n; i++ )
        {
            int next = prev + curr;
            prev = curr;
            curr = next;
        }
        return curr;
    }

    /* Pembangkitan suku ke-n secara rekursif */
    public int fibonacciRekursif( int n )
    {
        if ( n <= 0 ) return 0;
        if ( n == 1 ) return 1;
        return fibonacciRekursif( n - 1 ) + fibonacciRekursif( n - 2 );
    }

    /* Output deret barisan hingga elemen ke-n */
    public void tampilkanDeret( int n )
    {
        System.out.print( "Deret Fibonacci : " );
        for ( int i = 0; i <= n; i++ )
        {
            System.out.print( fibonacci( i ) );
            if ( i < n )
            {
                System.out.print( ", " );
            }
        }
        System.out.println();
    }

    /* Akumulasi sigma deret Fibonacci */
    public long jumlahDeret( int n )
    {
        long total = 0;
        for ( int i = 0; i <= n; i++ )
        {
            total += fibonacci( i );
        }
        return total;
    }

    /* Verifikasi keanggotaan nilai dalam barisan */
    public boolean isFibonacci( int angka )
    {
        if ( angka < 0 )
        {
            return false;
        }
        int i = 0;
        int nilai = fibonacci( i );
        while ( ( nilai < angka ) && ( i < BATAS_MAKS ) )
        {
            i++;
            nilai = fibonacci( i );
        }
        return ( nilai == angka );
    }

    /* Komparasi akurasi hasil pendekatan iteratif dan rekursif */
    public void bandingkanMetode( int n )
    {
        int hasilIteratif = fibonacci( n );
        int hasilRekursif = fibonacciRekursif( n );

        System.out.println( "Hasil iteratif : " + hasilIteratif );
        System.out.println( "Hasil rekursif : " + hasilRekursif );

        if ( hasilIteratif == hasilRekursif )
        {
            System.out.println( "Kedua metode menghasilkan nilai yang sama." );
        }
        else
        {
            System.out.println( "Hasil berbeda, ada yang salah!" );
        }
    }

    /* Helper input numerik */
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

    /* Helper masukan indeks suku n */
    public static int bacaSuku( Scanner input )
    {
        int n = -1;
        while ( n < 0 || n > BATAS_MAKS )
        {
            n = bacaAngka( input, "Masukkan n (0-" + BATAS_MAKS + "): " );
            if ( n < 0 || n > BATAS_MAKS )
            {
                System.out.println( "Nilai n di luar jangkauan, coba lagi!" );
            }
        }
        return n;
    }

    public static void main( String[] args )
    {
        Scanner input = new Scanner( System.in );
        FibonacciCalculator kalkulator = new FibonacciCalculator();

        int pilihan;
        do
        {
            System.out.println( "\n===== PROGRAM FIBONACCI =====" );
            System.out.println( "1. Hitung Fibonacci ke-n" );
            System.out.println( "2. Tampilkan deret Fibonacci" );
            System.out.println( "3. Jumlah deret Fibonacci" );
            System.out.println( "4. Cek apakah bilangan Fibonacci" );
            System.out.println( "5. Bandingkan iteratif vs rekursif" );
            System.out.println( "0. Keluar" );
            pilihan = bacaAngka( input, "Pilih menu: " );

            switch ( pilihan )
            {
                case 1:
                    int n1 = bacaSuku( input );
                    System.out.println( "F(" + n1 + ") = " + kalkulator.fibonacci( n1 ) );
                    break;

                case 2:
                    int n2 = bacaSuku( input );
                    kalkulator.tampilkanDeret( n2 );
                    break;

                case 3:
                    int n3 = bacaSuku( input );
                    System.out.println( "Jumlah suku ke-0 s.d. ke-" + n3 + " = " + kalkulator.jumlahDeret( n3 ) );
                    break;

                case 4:
                    int angka = bacaAngka( input, "Masukkan bilangan: " );
                    if ( kalkulator.isFibonacci( angka ) )
                    {
                        System.out.println( angka + " adalah bilangan Fibonacci." );
                    }
                    else
                    {
                        System.out.println( angka + " bukan bilangan Fibonacci." );
                    }
                    break;

                case 5:
                    int n5 = -1;
                    while ( n5 < 0 || n5 > 35 )
                    {
                        n5 = bacaAngka( input, "Masukkan n (0-35): " );
                        if ( n5 < 0 || n5 > 35 )
                        {
                            System.out.println( "Nilai n di luar jangkauan, coba lagi!" );
                        }
                    }
                    kalkulator.bandingkanMetode( n5 );
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