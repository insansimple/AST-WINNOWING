/*
  Unit        : StringReverser.java
  Deskripsi   : Manipulasi dan Inversi Susunan Karakter Teks
*/

import java.util.Scanner;

public class StringReverser {

  /* Operasi pembalikan karakter per karakter */
  public String reverse(String input) {
    StringBuilder sb = new StringBuilder();
    for (int i = input.length() - 1; i >= 0; i--) {
      sb.append(input.charAt(i));
    }
    return sb.toString();
  }

  /* Operasi penataan ulang urutan kata */
  public String balikUrutanKata(String kalimat) {
    String[] kata = kalimat.trim().split("\\s+");
    StringBuilder sb = new StringBuilder();
    for (int i = kata.length - 1; i >= 0; i--) {
      sb.append(kata[i]);
      if (i > 0) sb.append(" ");
    }
    return sb.toString();
  }

  /* Pembalikan karakter individual dalam setiap kata */
  public String balikTiapKata(String kalimat) {
    String[] kata = kalimat.trim().split("\\s+");
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < kata.length; i++) {
      sb.append(reverse(kata[i]));
      if (i < kata.length - 1) sb.append(" ");
    }
    return sb.toString();
  }

  /* Kalkulasi frekuensi fonem vokal */
  public int hitungVokal(String teks) {
    int jumlah = 0;
    String vokal = "aiueoAIUEO";
    for (int i = 0; i < teks.length(); i++) {
      if (vokal.indexOf(teks.charAt(i)) != -1) {
        jumlah++;
      }
    }
    return jumlah;
  }

  /* Pengujian kesetaraan kata dengan inverse-nya */
  public boolean samaDenganBalikan(String teks) {
    return teks.equalsIgnoreCase(reverse(teks));
  }

  /* Utility input angka */
  public static int bacaAngka(Scanner input, String pesan) {
    System.out.print(pesan);
    while (!input.hasNextInt()) {
      System.out.print("Input harus angka bulat, ulangi: ");
      input.next();
    }
    int nilai = input.nextInt();
    input.nextLine(); /* buang newline */
    return nilai;
  }

  /* Utility input teks */
  public static String bacaTeks(Scanner input, String pesan) {
    String teks = "";
    while (teks.trim().isEmpty()) {
      System.out.print(pesan);
      teks = input.nextLine();
      if (teks.trim().isEmpty()) {
        System.out.println("Teks tidak boleh kosong!");
      }
    }
    return teks;
  }

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    StringReverser pembalik = new StringReverser();

    int pilihan;
    do {
      System.out.println("\n===== PROGRAM PEMBALIK TEKS =====");
      System.out.println("1. Balik seluruh teks");
      System.out.println("2. Balik urutan kata");
      System.out.println("3. Balik huruf tiap kata");
      System.out.println("4. Hitung jumlah huruf vokal");
      System.out.println("5. Cek teks sama dengan balikannya");
      System.out.println("0. Keluar");
      pilihan = bacaAngka(input, "Pilih menu: ");

      switch (pilihan) {
        case 1:
          String teks1 = bacaTeks(input, "Masukkan teks: ");
          System.out.println("Hasil : " + pembalik.reverse(teks1));
          break;

        case 2:
          String teks2 = bacaTeks(input, "Masukkan kalimat: ");
          System.out.println("Hasil : " + pembalik.balikUrutanKata(teks2));
          break;

        case 3:
          String teks3 = bacaTeks(input, "Masukkan kalimat: ");
          System.out.println("Hasil : " + pembalik.balikTiapKata(teks3));
          break;

        case 4:
          String teks4 = bacaTeks(input, "Masukkan teks: ");
          System.out.println("Jumlah huruf vokal : " + pembalik.hitungVokal(teks4));
          break;

        case 5:
          String teks5 = bacaTeks(input, "Masukkan teks: ");
          if (pembalik.samaDenganBalikan(teks5)) {
            System.out.println("Teks sama dengan hasil baliknya.");
          } else {
            System.out.println("Teks berbeda dengan hasil baliknya: " + pembalik.reverse(teks5));
          }
          break;

        case 0:
          System.out.println("Terima kasih, program selesai.");
          break;

        default:
          System.out.println("Menu tidak tersedia!");
      }
    } while (pilihan != 0);

    input.close();
  }
}