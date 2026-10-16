/*
  Module: BinarySearch.java
  Implementation of Binary Search Algorithm (Iterative & Recursive)
*/
import java.util.Scanner;

public class BinarySearch {

  // Counter metrics
  private int jumlahLangkah = 0;

  /* Binary search - Iterative implementation */
  public int search(int[] arr, int target) {
    jumlahLangkah = 0;
    int low = 0;
    int high = arr.length - 1;

    while (low <= high) {
      jumlahLangkah++;
      int mid = low + (high - low) / 2;

      if (arr[mid] == target) return mid;

      if (arr[mid] < target) {
        low = mid + 1;
      } else {
        high = mid - 1;
      }
    }
    return -1;
  }

  /* Binary search - Recursive approach */
  public int searchRekursif(int[] arr, int target, int low, int high) {
    if (low > high) {
      return -1;
    }
    int mid = low + (high - low) / 2;

    if (arr[mid] == target) {
      return mid;
    }
    if (arr[mid] < target) {
      return searchRekursif(arr, target, mid + 1, high);
    }
    return searchRekursif(arr, target, low, mid - 1);
  }

  public int getJumlahLangkah() {
    return this.jumlahLangkah;
  }

  // Sorting precondition using bubble algorithm
  public static void urutkan(int[] arr) {
    int n = arr.length;
    for (int i = 0; i < n - 1; i++) {
      for (int j = 0; j < n - i - 1; j++) {
        if (arr[j] > arr[j + 1]) {
          int temp = arr[j];
          arr[j] = arr[j + 1];
          arr[j + 1] = temp;
        }
      }
    }
  }

  // Display array items
  public static void tampilkanArray(int[] arr) {
    System.out.print("Isi array : [");
    for (int i = 0; i < arr.length; i++) {
      System.out.print(arr[i]);
      if (i < arr.length - 1) System.out.print(", ");
    }
    System.out.println("]");
  }

  // Read validated integer input
  public static int bacaAngka(Scanner input, String pesan) {
    System.out.print(pesan);
    while (!input.hasNextInt()) {
      System.out.print("Input harus angka bulat, ulangi: ");
      input.next();
    }
    return input.nextInt();
  }

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    BinarySearch pencari = new BinarySearch();

    int jumlah = 0;
    while (jumlah <= 0) {
      jumlah = bacaAngka(input, "Masukkan jumlah data: ");
      if (jumlah <= 0) {
        System.out.println("Jumlah data harus lebih dari 0!");
      }
    }

    int[] data = new int[jumlah];
    for (int i = 0; i < jumlah; i++) {
      data[i] = bacaAngka(input, "Data ke-" + (i + 1) + " : ");
    }

    // Pastikan array terurut sebelum pencarian
    urutkan(data);
    System.out.println("\nData sudah diurutkan otomatis.");

    int pilihan;
    do {
      System.out.println("\n===== PROGRAM BINARY SEARCH =====");
      System.out.println("1. Cari nilai (iteratif)");
      System.out.println("2. Cari nilai (rekursif)");
      System.out.println("3. Tampilkan isi array");
      System.out.println("0. Keluar");
      pilihan = bacaAngka(input, "Pilih menu: ");

      switch (pilihan) {
        case 1:
          int target1 = bacaAngka(input, "Nilai yang dicari: ");
          int hasil1 = pencari.search(data, target1);
          if (hasil1 == -1) {
            System.out.println("Nilai " + target1 + " tidak ditemukan.");
          } else {
            System.out.println("Nilai " + target1 + " ditemukan di indeks " + hasil1);
          }
          System.out.println("Jumlah langkah : " + pencari.getJumlahLangkah());
          break;

        case 2:
          int target2 = bacaAngka(input, "Nilai yang dicari: ");
          int hasil2 = pencari.searchRekursif(data, target2, 0, data.length - 1);
          if (hasil2 == -1) {
            System.out.println("Nilai " + target2 + " tidak ditemukan.");
          } else {
            System.out.println("Nilai " + target2 + " ditemukan di indeks " + hasil2);
          }
          break;

        case 3:
          tampilkanArray(data);
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