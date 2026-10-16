/*
   Berkas   : BankQueue.java
   Catatan  : Program konsol berbasis menu untuk mengelola antrean
              menggunakan struktur data larik.
*/
import java.util.Scanner;

/* Kelas utama pengelola antrean */
public class BankQueue
{

	/* ---- Atribut ---- */
	private String[]  antrean ;
	private int       jumlah ;


	/* Konstruktor */
	public BankQueue( int kapasitas )
	{
		antrean = new String[ kapasitas ] ;
		jumlah  = 0 ;
	}


	/* Operasi penambahan di bagian belakang */
	public void tambahNasabah( String nama )
	{
		if ( jumlah < antrean.length )
		{
			antrean[ jumlah++ ] = nama ;
		}
	}


	/* Operasi pelayanan dengan pergeseran elemen */
	public String layaniNasabah()
	{
		if ( jumlah == 0 ) return null ;

		String nasabah = antrean[ 0 ] ;
		for ( int i = 0 ; i < jumlah - 1 ; i++ )
		{
			antrean[ i ] = antrean[ i + 1 ] ;
		}
		jumlah-- ;
		return nasabah ;
	}


	/* Pemeriksaan kondisi kosong */
	public boolean isKosong()
	{
		return jumlah == 0 ;
	}


	/* Pemeriksaan kondisi penuh */
	public boolean isPenuh()
	{
		return jumlah == antrean.length ;
	}


	/* Pengintipan elemen terdepan */
	public String lihatDepan()
	{
		if ( jumlah == 0 ) return null ;
		return antrean[ 0 ] ;
	}


	/* Pengambilan banyaknya elemen */
	public int getJumlah()
	{
		return jumlah ;
	}


	/* Pencarian linear berdasarkan nama */
	public int cariPosisi( String nama )
	{
		for ( int i = 0 ; i < jumlah ; i++ )
		{
			if ( antrean[ i ].equalsIgnoreCase( nama ) )
			{
				return i + 1 ;
			}
		}
		return -1 ;
	}


	/* Pencetakan seluruh isi antrean */
	public void tampilkanAntrean()
	{
		if ( isKosong() )
		{
			System.out.println( "Antrean kosong." ) ;
			return ;
		}

		System.out.println( "Daftar antrean:" ) ;
		for ( int i = 0 ; i < jumlah ; i++ )
		{
			System.out.println( ( i + 1 ) + ". " + antrean[ i ] ) ;
		}
	}


	/* Utilitas masukan bilangan bulat */
	public static int bacaAngka( Scanner input , String pesan )
	{
		System.out.print( pesan ) ;
		while ( !input.hasNextInt() )
		{
			System.out.print( "Input harus angka bulat, ulangi: " ) ;
			input.next() ;
		}
		int nilai = input.nextInt() ;
		input.nextLine() ; /* sisa baris dibuang */
		return nilai ;
	}


	/* Utilitas masukan teks tidak kosong */
	public static String bacaNama( Scanner input , String pesan )
	{
		String nama = "" ;
		while ( nama.trim().isEmpty() )
		{
			System.out.print( pesan ) ;
			nama = input.nextLine() ;
			if ( nama.trim().isEmpty() )
			{
				System.out.println( "Nama tidak boleh kosong!" ) ;
			}
		}
		return nama.trim() ;
	}


	/* Titik masuk program */
	public static void main( String[] args )
	{
		Scanner input = new Scanner( System.in ) ;

		/* Langkah 1: tentukan kapasitas */
		int kapasitas = 0 ;
		while ( kapasitas <= 0 )
		{
			kapasitas = bacaAngka( input , "Masukkan kapasitas antrean: " ) ;
			if ( kapasitas <= 0 )
			{
				System.out.println( "Kapasitas harus lebih dari 0!" ) ;
			}
		}
		BankQueue bank = new BankQueue( kapasitas ) ;

		/* Langkah 2: jalankan menu berulang */
		int pilihan ;
		do
		{
			System.out.println( "\n===== ANTREAN BANK =====" ) ;
			System.out.println( "1. Tambah nasabah" ) ;
			System.out.println( "2. Layani nasabah" ) ;
			System.out.println( "3. Lihat nasabah terdepan" ) ;
			System.out.println( "4. Cari posisi nasabah" ) ;
			System.out.println( "5. Tampilkan daftar antrean" ) ;
			System.out.println( "0. Keluar" ) ;
			pilihan = bacaAngka( input , "Pilih menu: " ) ;

			switch ( pilihan )
			{
				case 1 :
					if ( bank.isPenuh() )
					{
						System.out.println( "Antrean penuh, nasabah tidak dapat ditambah!" ) ;
					}
					else
					{
						String nama = bacaNama( input , "Nama nasabah: " ) ;
						bank.tambahNasabah( nama ) ;
						System.out.println( nama + " masuk antrean nomor " + bank.getJumlah() ) ;
					}
					break ;

				case 2 :
					if ( bank.isKosong() )
					{
						System.out.println( "Antrean kosong, tidak ada yang dilayani!" ) ;
					}
					else
					{
						System.out.println( "Sedang melayani: " + bank.layaniNasabah() ) ;
						System.out.println( "Sisa antrean   : " + bank.getJumlah() ) ;
					}
					break ;

				case 3 :
					if ( bank.isKosong() )
					{
						System.out.println( "Antrean kosong!" ) ;
					}
					else
					{
						System.out.println( "Nasabah terdepan: " + bank.lihatDepan() ) ;
					}
					break ;

				case 4 :
					String cari = bacaNama( input , "Nama yang dicari: " ) ;
					int posisi = bank.cariPosisi( cari ) ;
					if ( posisi == -1 )
					{
						System.out.println( cari + " tidak ada dalam antrean." ) ;
					}
					else
					{
						System.out.println( cari + " berada di posisi " + posisi ) ;
					}
					break ;

				case 5 :
					bank.tampilkanAntrean() ;
					System.out.println( "Jumlah nasabah: " + bank.getJumlah() ) ;
					break ;

				case 0 :
					System.out.println( "Terima kasih, program selesai." ) ;
					break ;

				default :
					System.out.println( "Menu tidak tersedia!" ) ;
			}
		} while ( pilihan != 0 ) ;

		input.close() ;
	}

}