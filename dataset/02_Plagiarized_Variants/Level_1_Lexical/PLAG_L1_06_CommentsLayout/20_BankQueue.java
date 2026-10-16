/*
 * =========================================================================
 * Nama Berkas : BankQueue.java
 * Deskripsi   : Sistem Simulasi Antrean Layanan Teller Perbankan
 * Gaya Format : Allman Bracing Style dengan Dokumentasi Method Internal
 * =========================================================================
 */

import java.util.Scanner;

public class BankQueue
{
    private String[] antrean;
    private int jumlah;

    public BankQueue(int kapasitas)
    {
        /*
         * [DOKUMENTASI KONSTRUKTOR]
         * Alokasi memori array antrean berdasarkan kapasitas batas
         */
        antrean = new String[kapasitas];
        jumlah = 0;
    }

    public void tambahNasabah(String nama)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Registrasi nasabah baru pada posisi ekor antrean
         */
        if (jumlah < antrean.length)
        {
            antrean[jumlah++] = nama;
        }
    }

    public String layaniNasabah()
    {
        /*
         * [DOKUMENTASI METHOD]
         * Pemrosesan nasabah terdepan dan pergeseran seluruh elemen antrean ke kiri
         */
        if (jumlah == 0)
        {
            return null;
        }

        String nasabah = antrean[0];
        for (int i = 0; i < jumlah - 1; i++)
        {
            antrean[i] = antrean[i + 1];
        }
        jumlah--;
        return nasabah;
    }

    public boolean isKosong()
    {
        /*
         * [DOKUMENTASI METHOD]
         * Verifikasi kondisi antrean nihil/kosong
         */
        return jumlah == 0;
    }

    public boolean isPenuh()
    {
        /*
         * [DOKUMENTASI METHOD]
         * Verifikasi kondisi antrean telah mencapai kapasitas maksimal
         */
        return jumlah == antrean.length;
    }

    public String lihatDepan()
    {
        /*
         * [DOKUMENTASI METHOD]
         * Menampilkan identitas nasabah giliran terdepan tanpa pemanggilan layanan
         */
        if (jumlah == 0)
        {
            return null;
        }
        return antrean[0];
    }

    public int getJumlah()
    {
        /*
         * [DOKUMENTASI METHOD]
         * Mengembalikan jumlah antrean nasabah aktif
         */
        return jumlah;
    }

    public int cariPosisi(String nama)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Pencarian posisi urutan tiket antrean (1-based index) berdasarkan nama
         */
        for (int i = 0; i < jumlah; i++)
        {
            if (antrean[i].equalsIgnoreCase(nama))
            {
                return i + 1;
            }
        }
        return -1;
    }

    public void tampilkanAntrean()
    {
        /*
         * [DOKUMENTASI METHOD]
         * Menampilkan daftar lengkap antrean nasabah terdaftar saat ini
         */
        if (isKosong())
        {
            System.out.println("Antrean kosong.");
            return;
        }

        System.out.println("Daftar antrean:");
        for (int i = 0; i < jumlah; i++)
        {
            System.out.println((i + 1) + ". " + antrean[i]);
        }
    }

    public static int bacaAngka(Scanner input, String pesan)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Pembacaan integer masukan dengan eliminasi trailing newline
         */
        System.out.print(pesan);
        while (!input.hasNextInt())
        {
            System.out.print("Input harus angka bulat, ulangi: ");
            input.next();
        }
        int nilai = input.nextInt();
        input.nextLine();
        return nilai;
    }

    public static String bacaNama(Scanner input, String pesan)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Pembacaan string nama nasabah dengan validasi string tidak kosong
         */
        String nama = "";
        while (nama.trim().isEmpty())
        {
            System.out.print(pesan);
            nama = input.nextLine();
            if (nama.trim().isEmpty())
            {
                System.out.println("Nama tidak boleh kosong!");
            }
        }
        return nama.trim();
    }

    public static void main(String[] args)
    {
        /*
         * [DOKUMENTASI METHOD]
         * Loop antarmuka program simulasi antrean loket bank
         */
        Scanner input = new Scanner(System.in);

        int kapasitas = 0;
        while (kapasitas <= 0)
        {
            kapasitas = bacaAngka(input, "Masukkan kapasitas antrean: ");
            if (kapasitas <= 0)
            {
                System.out.println("Kapasitas harus lebih dari 0!");
            }
        }
        BankQueue bank = new BankQueue(kapasitas);

        int pilihan;
        do
        {
            System.out.println("\n===== ANTREAN BANK =====");
            System.out.println("1. Tambah nasabah");
            System.out.println("2. Layani nasabah");
            System.out.println("3. Lihat nasabah terdepan");
            System.out.println("4. Cari posisi nasabah");
            System.out.println("5. Tampilkan daftar antrean");
            System.out.println("0. Keluar");
            pilihan = bacaAngka(input, "Pilih menu: ");

            switch (pilihan)
            {
                case 1:
                {
                    if (bank.isPenuh())
                    {
                        System.out.println("Antrean penuh, nasabah tidak dapat ditambah!");
                    }
                    else
                    {
                        String nama = bacaNama(input, "Nama nasabah: ");
                        bank.tambahNasabah(nama);
                        System.out.println(nama + " masuk antrean nomor " + bank.getJumlah());
                    }
                    break;
                }
                case 2:
                {
                    if (bank.isKosong())
                    {
                        System.out.println("Antrean kosong, tidak ada yang dilayani!");
                    }
                    else
                    {
                        System.out.println("Sedang melayani: " + bank.layaniNasabah());
                        System.out.println("Sisa antrean   : " + bank.getJumlah());
                    }
                    break;
                }
                case 3:
                {
                    if (bank.isKosong())
                    {
                        System.out.println("Antrean kosong!");
                    }
                    else
                    {
                        System.out.println("Nasabah terdepan: " + bank.lihatDepan());
                    }
                    break;
                }
                case 4:
                {
                    String cari = bacaNama(input, "Nama yang dicari: ");
                    int posisi = bank.cariPosisi(cari);
                    if (posisi == -1)
                    {
                        System.out.println(cari + " tidak ada dalam antrean.");
                    }
                    else
                    {
                        System.out.println(cari + " berada di posisi " + posisi);
                    }
                    break;
                }
                case 5:
                {
                    bank.tampilkanAntrean();
                    System.out.println("Jumlah nasabah: " + bank.getJumlah());
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