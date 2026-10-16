/* ------------------------------------------------------------
   Berkas  : FactorialCalculator.java
   Catatan : program konsol berbasis menu
   ------------------------------------------------------------ */
import java.util.Scanner;

/* Kelas utama program */
public class FactorialCalculator
{

  /* Konstanta batas atas input */
  static final int BATAS_MAKS=20;


  /* Bagian 1: perhitungan */
  public static long compute( int n )
  {
    if ( n<=1 )
    {
      return 1;
    }
    long result=1;
    for ( int i=2 ; i<=n ; i++ )
    {
      result=result*i;
    }
    return result;
  }


  /* Bagian 2: versi lain */
  public static long computeRekursif( int n )
  {
    if ( n<=1 ) { return 1; }
    return n*computeRekursif( n-1 );
  }


  /* Bagian 3: tampilan tabel */
  public static void tampilkanTabel( int n )
  {
    System.out.println( "\n===== Tabel Faktorial 0 s.d. "+n+" =====" );
    System.out.println( "n\t| n!" );
    System.out.println( "--------------------------" );

    for ( int i=0 ; i<=n ; i++ )
    {
      System.out.println( i+"\t| "+compute( i ) );
    }
  }


  /* Bagian 4: pembandingan */
  public static void bandingkanMetode( int n )
  {
    long hasilIteratif = compute( n );
    long hasilRekursif = computeRekursif( n );

    System.out.println( "\nHasil iteratif  : "+hasilIteratif );
    System.out.println( "Hasil rekursif  : "+hasilRekursif );

    if ( hasilIteratif==hasilRekursif )
    {
      System.out.println( "Kedua metode menghasilkan nilai yang sama." );
    }
    else
    {
      System.out.println( "Hasil berbeda, ada yang salah!" );
    }
  }


  /* Bagian 5: pembacaan input */
  public static int bacaBilangan( Scanner input )
  {
    int n       = -1;
    boolean valid = false;

    while ( !valid )
    {
      System.out.print( "Masukkan bilangan (0-"+BATAS_MAKS+"): " );
      if ( input.hasNextInt() )
      {
        n=input.nextInt();
        if ( n>=0 && n<=BATAS_MAKS )
        {
          valid=true;
        }
        else
        {
          System.out.println( "Bilangan di luar jangkauan, coba lagi!" );
        }
      }
      else
      {
        System.out.println( "Input harus berupa angka bulat!" );
        input.next();   /* langkah pembersihan */
      }
    }
    return n;
  }

  /* Titik masuk program */
  public static void main( String[] args )
  {
    Scanner input=new Scanner( System.in );
    int pilihan;

    do
    {
      System.out.println( "\n===== KALKULATOR FAKTORIAL =====" );
      System.out.println( "1. Hitung faktorial satu bilangan" );
      System.out.println( "2. Tampilkan tabel faktorial" );
      System.out.println( "3. Bandingkan iteratif vs rekursif" );
      System.out.println( "0. Keluar" );
      System.out.print( "Pilih menu: " );

      while ( !input.hasNextInt() )
      {
        System.out.print( "Pilihan harus angka, ulangi: " );
        input.next();
      }
      pilihan=input.nextInt();

      /* Percabangan menu */
      switch ( pilihan )
      {
        case 1:
          int a=bacaBilangan( input );
          System.out.println( a+"! = "+compute( a ) );
          break;
        case 2:
          int b=bacaBilangan( input );
          tampilkanTabel( b );
          break;
        case 3:
          int c=bacaBilangan( input );
          bandingkanMetode( c );
          break;
        case 0:
          System.out.println( "Terima kasih, program selesai." );
          break;
        default:
          System.out.println( "Menu tidak tersedia!" );
      }
    } while ( pilihan!=0 );

    input.close();
  }

}