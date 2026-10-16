/*
 * File         : SimpleStack.java
 * DataStructure: Array-based LIFO Stack
 */

import java.util.Scanner;

public class SimpleStack {

  private int[] elements;
  private int top;

  // Inisialisasi kapasitas stack
  public SimpleStack(int capacity) {
    elements = new int[capacity];
    top = -1;
  }

  // Operasi push elemen ke puncak
  public void push(int item) {
    elements[++top] = item;
  }

  // Operasi pop elemen teratas
  public int pop() {
    return elements[top--];
  }

  public boolean isEmpty() { return top == -1; }

  public boolean isFull() { return top == elements.length - 1; }

  public int peek() { return elements[top]; }

  public int size() { return top + 1; }

  // Cetak visual tumpukan data
  public void tampilkan() {
    if (isEmpty()) {
      System.out.println("Stack kosong.");
      return;
    }
    System.out.println("Isi stack (puncak -> dasar):");
    for (int i = top; i >= 0; i--) {
      System.out.println("| " + elements[i] + " |");
    }
    System.out.println("-----");
  }

  // Aplikasi stack: Pembalikan string
  public static String balikKata(String kata) {
    SimpleStack tumpukan = new SimpleStack(kata.length());
    for (int i = 0; i < kata.length(); i++) {
      tumpukan.push(kata.charAt(i));
    }
    String hasil = "";
    while (!tumpukan.isEmpty()) {
      hasil += (char) tumpukan.pop();
    }
    return hasil;
  }

  // Validasi input angka
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

    int kapasitas = 0;
    while (kapasitas <= 0) {
      kapasitas = bacaAngka(input, "Masukkan kapasitas stack: ");
      if (kapasitas <= 0) {
        System.out.println("Kapasitas harus lebih dari 0!");
      }
    }
    SimpleStack stack = new SimpleStack(kapasitas);

    int pilihan;
    do {
      System.out.println("\n===== PROGRAM STACK =====");
      System.out.println("1. Push (tambah elemen)");
      System.out.println("2. Pop (ambil elemen)");
      System.out.println("3. Peek (lihat elemen puncak)");
      System.out.println("4. Tampilkan isi stack");
      System.out.println("5. Balik kata menggunakan stack");
      System.out.println("0. Keluar");
      pilihan = bacaAngka(input, "Pilih menu: ");

      switch (pilihan) {
        case 1:
          if (stack.isFull()) {
            System.out.println("Stack penuh, tidak bisa push!");
          } else {
            int nilai = bacaAngka(input, "Masukkan nilai: ");
            stack.push(nilai);
            System.out.println(nilai + " berhasil ditambahkan.");
          }
          break;

        case 2:
          if (stack.isEmpty()) {
            System.out.println("Stack kosong, tidak bisa pop!");
          } else {
            System.out.println("Elemen yang diambil: " + stack.pop());
          }
          break;

        case 3:
          if (stack.isEmpty()) {
            System.out.println("Stack kosong!");
          } else {
            System.out.println("Elemen puncak: " + stack.peek());
          }
          break;

        case 4:
          stack.tampilkan();
          System.out.println("Jumlah elemen: " + stack.size());
          break;

        case 5:
          System.out.print("Masukkan kata: ");
          String kata = input.next();
          System.out.println("Hasil dibalik: " + balikKata(kata));
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