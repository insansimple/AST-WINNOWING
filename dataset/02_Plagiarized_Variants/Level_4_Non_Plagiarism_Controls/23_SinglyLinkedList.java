/*
 * Program   : SinglyLinkedList
 * Deskripsi : Implementasi linked list satu arah (singly linked list)
 *             dengan operasi insert depan, insert belakang, delete
 *             (berdasarkan nilai), dan display.
 */
import java.util.Scanner;

public class SinglyLinkedList {

    // Class Node: satu simpul berisi data dan penunjuk ke simpul berikutnya
    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;   // penunjuk ke simpul pertama
    private int size;    // jumlah simpul

    public SinglyLinkedList() {
        head = null;
        size = 0;
    }

    // Cek apakah list kosong
    public boolean isEmpty() {
        return head == null;
    }

    // Menyisipkan simpul baru di depan list
    public void insertFront(int nilai) {
        Node baru = new Node(nilai);
        baru.next = head;
        head = baru;
        size++;
        System.out.println("Berhasil menambah " + nilai + " di depan.");
    }

    // Menyisipkan simpul baru di belakang list
    public void insertBack(int nilai) {
        Node baru = new Node(nilai);
        if (isEmpty()) {
            head = baru;
        } else {
            Node bantu = head;
            // telusuri sampai simpul terakhir
            while (bantu.next != null) {
                bantu = bantu.next;
            }
            bantu.next = baru;
        }
        size++;
        System.out.println("Berhasil menambah " + nilai + " di belakang.");
    }

    // Menghapus simpul pertama yang berisi nilai tertentu
    public void delete(int nilai) {
        if (isEmpty()) {
            System.out.println("List kosong, tidak ada yang dihapus.");
            return;
        }

        // kasus 1: data yang dihapus ada di head
        if (head.data == nilai) {
            head = head.next;
            size--;
            System.out.println("Data " + nilai + " berhasil dihapus.");
            return;
        }

        // kasus 2: data berada di tengah atau belakang
        Node sebelum = head;
        while (sebelum.next != null && sebelum.next.data != nilai) {
            sebelum = sebelum.next;
        }

        if (sebelum.next == null) {
            System.out.println("Data " + nilai + " tidak ditemukan.");
        } else {
            sebelum.next = sebelum.next.next;
            size--;
            System.out.println("Data " + nilai + " berhasil dihapus.");
        }
    }

    // Menampilkan seluruh isi list
    public void display() {
        if (isEmpty()) {
            System.out.println("List kosong.");
            return;
        }
        Node bantu = head;
        System.out.print("Isi list : ");
        while (bantu != null) {
            System.out.print(bantu.data);
            if (bantu.next != null) {
                System.out.print(" -> ");
            }
            bantu = bantu.next;
        }
        System.out.println(" -> null");
        System.out.println("Jumlah   : " + size + " elemen");
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        SinglyLinkedList list = new SinglyLinkedList();
        int pilihan;

        do {
            // Menu utama
            System.out.println("\n=== MENU SINGLY LINKED LIST ===");
            System.out.println("1. Insert depan");
            System.out.println("2. Insert belakang");
            System.out.println("3. Delete berdasarkan nilai");
            System.out.println("4. Display");
            System.out.println("0. Keluar");
            System.out.print("Pilihan: ");
            pilihan = input.nextInt();

            switch (pilihan) {
                case 1:
                    System.out.print("Masukkan nilai: ");
                    list.insertFront(input.nextInt());
                    break;
                case 2:
                    System.out.print("Masukkan nilai: ");
                    list.insertBack(input.nextInt());
                    break;
                case 3:
                    System.out.print("Nilai yang dihapus: ");
                    list.delete(input.nextInt());
                    break;
                case 4:
                    list.display();
                    break;
                case 0:
                    System.out.println("Program selesai.");
                    break;
                default:
                    System.out.println("Pilihan tidak tersedia!");
            }
        } while (pilihan != 0);

        input.close();
    }
}