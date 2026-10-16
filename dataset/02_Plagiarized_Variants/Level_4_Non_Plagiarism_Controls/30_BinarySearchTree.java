/*
 * Program  : BinarySearchTree
 * Deskripsi: Implementasi Binary Search Tree (BST) sederhana dengan
 *            operasi insert, search, delete, traversal (inorder, preorder,
 *            postorder), serta menu interaktif untuk pengguna.
 */
import java.util.Scanner;

public class BinarySearchTree {

    // Class Node untuk menyimpan data tiap simpul
    static class Node {
        int data;
        Node kiri;
        Node kanan;

        Node(int data) {
            this.data = data;
            this.kiri = null;
            this.kanan = null;
        }
    }

    // Akar dari tree
    private Node root;

    public BinarySearchTree() {
        root = null;
    }

    // ===================== INSERT =====================
    public void insert(int nilai) {
        root = insertRekursif(root, nilai);
    }

    private Node insertRekursif(Node current, int nilai) {
        // Jika posisi kosong, buat node baru
        if (current == null) {
            return new Node(nilai);
        }
        if (nilai < current.data) {
            current.kiri = insertRekursif(current.kiri, nilai);
        } else if (nilai > current.data) {
            current.kanan = insertRekursif(current.kanan, nilai);
        } else {
            System.out.println("Nilai " + nilai + " sudah ada, tidak ditambahkan.");
        }
        return current;
    }

    // ===================== SEARCH =====================
    public boolean search(int nilai) {
        Node current = root;
        while (current != null) {
            if (nilai == current.data) {
                return true;
            } else if (nilai < current.data) {
                current = current.kiri;
            } else {
                current = current.kanan;
            }
        }
        return false;
    }

    // ===================== DELETE =====================
    public void delete(int nilai) {
        root = deleteRekursif(root, nilai);
    }

    private Node deleteRekursif(Node current, int nilai) {
        if (current == null) {
            System.out.println("Nilai " + nilai + " tidak ditemukan.");
            return null;
        }
        if (nilai < current.data) {
            current.kiri = deleteRekursif(current.kiri, nilai);
        } else if (nilai > current.data) {
            current.kanan = deleteRekursif(current.kanan, nilai);
        } else {
            // Kasus 1 dan 2: node tanpa anak atau hanya satu anak
            if (current.kiri == null) {
                return current.kanan;
            } else if (current.kanan == null) {
                return current.kiri;
            }
            // Kasus 3: dua anak, ganti dengan nilai terkecil di subtree kanan
            current.data = cariMinimum(current.kanan);
            current.kanan = deleteRekursif(current.kanan, current.data);
        }
        return current;
    }

    private int cariMinimum(Node node) {
        int min = node.data;
        while (node.kiri != null) {
            node = node.kiri;
            min = node.data;
        }
        return min;
    }

    // ===================== TRAVERSAL =====================
    public void inorder(Node node) {
        if (node != null) {
            inorder(node.kiri);
            System.out.print(node.data + " ");
            inorder(node.kanan);
        }
    }

    public void preorder(Node node) {
        if (node != null) {
            System.out.print(node.data + " ");
            preorder(node.kiri);
            preorder(node.kanan);
        }
    }

    public void postorder(Node node) {
        if (node != null) {
            postorder(node.kiri);
            postorder(node.kanan);
            System.out.print(node.data + " ");
        }
    }

    // ===================== INFO TREE =====================
    public int tinggi(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(tinggi(node.kiri), tinggi(node.kanan));
    }

    public int hitungNode(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + hitungNode(node.kiri) + hitungNode(node.kanan);
    }

    public Node getRoot() {
        return root;
    }

    // ===================== MAIN =====================
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        BinarySearchTree bst = new BinarySearchTree();

        // Data awal sebagai contoh pemakaian
        int[] dataAwal = {50, 30, 70, 20, 40, 60, 80};
        System.out.println("=== Data awal dimasukkan ke tree ===");
        for (int angka : dataAwal) {
            bst.insert(angka);
            System.out.print(angka + " ");
        }
        System.out.println("\n");

        int pilihan;
        do {
            System.out.println("========== MENU BST ==========");
            System.out.println("1. Tambah data");
            System.out.println("2. Cari data");
            System.out.println("3. Hapus data");
            System.out.println("4. Tampilkan traversal");
            System.out.println("5. Info tree (tinggi & jumlah node)");
            System.out.println("0. Keluar");
            System.out.print("Pilihan Anda: ");
            pilihan = input.nextInt();

            switch (pilihan) {
                case 1:
                    System.out.print("Masukkan angka: ");
                    int tambah = input.nextInt();
                    bst.insert(tambah);
                    System.out.println("Proses tambah selesai.");
                    break;
                case 2:
                    System.out.print("Masukkan angka yang dicari: ");
                    int cari = input.nextInt();
                    if (bst.search(cari)) {
                        System.out.println("Angka " + cari + " DITEMUKAN.");
                    } else {
                        System.out.println("Angka " + cari + " TIDAK ditemukan.");
                    }
                    break;
                case 3:
                    System.out.print("Masukkan angka yang dihapus: ");
                    int hapus = input.nextInt();
                    bst.delete(hapus);
                    break;
                case 4:
                    System.out.print("Inorder   : ");
                    bst.inorder(bst.getRoot());
                    System.out.print("\nPreorder  : ");
                    bst.preorder(bst.getRoot());
                    System.out.print("\nPostorder : ");
                    bst.postorder(bst.getRoot());
                    System.out.println();
                    break;
                case 5:
                    System.out.println("Tinggi tree : " + bst.tinggi(bst.getRoot()));
                    System.out.println("Jumlah node : " + bst.hitungNode(bst.getRoot()));
                    break;
                case 0:
                    System.out.println("Terima kasih, program selesai.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
            System.out.println();
        } while (pilihan != 0);

        input.close();
    }
}