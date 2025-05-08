package com.mycompany.p_uts_23090122_b_2025;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 * Program CRUD MongoDB 
 * @author dede
 */
public class P_UTS_23090122_B_2025 {
    // Definisi variabel koneksi MongoDB
    private static final String CONNECTION_STRING = "mongodb+srv://nanda:nanda92@nanda.prhf4cx.mongodb.net/?retryWrites=true&w=majority&appName=nanda";
    private static final String DATABASE_NAME = "uts_23090122_B_2025";
    private static final String COLLECTION_NAME = "coll_23090122_B_2025";
    
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        try {
            // Inisialisasi koneksi ke MongoDB
            mongoClient = MongoClients.create(CONNECTION_STRING);
            // Mendapatkan database, jika tidak ada akan dibuat otomatis
            database = mongoClient.getDatabase(DATABASE_NAME);
            // Mendapatkan collection, jika tidak ada akan dibuat otomatis
            collection = database.getCollection(COLLECTION_NAME);
            
            scanner = new Scanner(System.in);
            boolean running = true;
            
            while (running) {
                // Menampilkan menu pilihan
                System.out.println("\n=== PROGRAM CRUD MONGODB ===");
                System.out.println("1. Tambah Data (Create)");
                System.out.println("2. Tampilkan Data (Read)");
                System.out.println("3. Perbarui Data (Update)");
                System.out.println("4. Hapus Data (Delete)");
                System.out.println("5. Cari Data (Search)");
                System.out.println("0. Keluar");
                System.out.print("Pilih menu (0-5): ");
                
                int choice = scanner.nextInt();
                scanner.nextLine(); // Membersihkan buffer
                
                switch (choice) {
                    case 1 -> createData();
                    case 2 -> readData();
                    case 3 -> updateData();
                    case 4 -> deleteData();
                    case 5 -> searchData();
                    case 0 -> {
                        running = false;
                        System.out.println("Program berakhir.");
                    }
                    default -> System.out.println("Pilihan tidak valid!");
                }
            }
            
        } catch (Exception e) {
            // Menampilkan error jika terjadi masalah
            System.err.println("Error: " + e.getMessage());
        } finally {
            // Menutup koneksi MongoDB dan scanner
            if (mongoClient != null) {
                mongoClient.close();
            }
            if (scanner != null) {
                scanner.close();
            }
        }
    }
    
    /**
     * Fungsi untuk menambahkan data ke MongoDB
     * Akan menambahkan 3 dokumen dengan dimensi data berbeda
     */
    private static void createData() {
        System.out.println("\n=== TAMBAH DATA ===");
        System.out.println("Menambahkan 3 data dengan dimensi berbeda...");
        
        try {
            // Dokumen 1: Data mahasiswa
            Document doc1 = new Document("nama", "Budi Santoso")
                    .append("nim", "23090001")
                    .append("jurusan", "Teknik Informatika")
                    .append("semester", 4)
                    .append("ipk", 3.75);
            
            // Dokumen 2: Data buku dengan array penulis
            Document doc2 = new Document("judul", "Pemrograman Java untuk Pemula")
                    .append("penulis", Arrays.asList("Ahmad Dahlan", "Siti Aminah"))
                    .append("tahun", 2023)
                    .append("penerbit", "Gramedia")
                    .append("halaman", 350)
                    .append("kategori", Arrays.asList("Programming", "Java", "Pemula"));
            
            // Dokumen 3: Data produk dengan dokumen bersarang
            Document supplier = new Document("nama", "PT Sejahtera")
                    .append("alamat", "Jl. Merdeka No. 123")
                    .append("kontak", "021-5552345");
            
            Document doc3 = new Document("kode", "PRD001")
                    .append("nama", "Laptop Asus VivoBook")
                    .append("harga", 8500000)
                    .append("stok", 15)
                    .append("spesifikasi", new Document("prosesor", "Intel i5")
                            .append("ram", "8GB")
                            .append("storage", "512GB SSD")
                            .append("ukuran_layar", 14.0))
                    .append("supplier", supplier);
            
            // Menambahkan dokumen ke collection
            InsertOneResult result1 = collection.insertOne(doc1);
            InsertOneResult result2 = collection.insertOne(doc2);
            InsertOneResult result3 = collection.insertOne(doc3);
            
            // Menampilkan ID dokumen yang berhasil ditambahkan
            System.out.println("Data berhasil ditambahkan dengan ID:");
            System.out.println("Dokumen 1: " + result1.getInsertedId());
            System.out.println("Dokumen 2: " + result2.getInsertedId());
            System.out.println("Dokumen 3: " + result3.getInsertedId());
            
        } catch (Exception e) {
            // Menampilkan error jika gagal menambahkan data
            System.err.println("Gagal menambahkan data: " + e.getMessage());
        }
    }
    
    /**
     * Fungsi untuk menampilkan semua data dari MongoDB
     */
    private static void readData() {
        System.out.println("\n=== TAMPILKAN DATA ===");
        
        try {
            // Membuat list untuk menyimpan semua dokumen
            List<Document> documents = new ArrayList<>();
            // Memasukkan semua dokumen ke dalam list
            collection.find().into(documents);
            
            if (documents.isEmpty()) {
                // Pesan jika tidak ada data
                System.out.println("Tidak ada data yang tersimpan.");
            } else {
                // Menampilkan jumlah data yang ditemukan
                System.out.println("Jumlah data: " + documents.size());
                System.out.println("Data yang tersimpan:");
                
                // Iterasi setiap dokumen dan menampilkannya
                int counter = 1;
                for (Document doc : documents) {
                    System.out.println("\nData #" + counter);
                    System.out.println("ID: " + doc.getObjectId("_id"));
                    // Menampilkan semua field dan value dari dokumen
                    doc.forEach((key, value) -> {
                        if (!key.equals("_id")) {
                            System.out.println(key + ": " + value);
                        }
                    });
                    counter++;
                }
            }
        } catch (Exception e) {
            // Menampilkan error jika gagal membaca data
            System.err.println("Gagal membaca data: " + e.getMessage());
        }
    }
    
    /**
     * Fungsi untuk memperbarui data di MongoDB
     */
    private static void updateData() {
        System.out.println("\n=== PERBARUI DATA ===");
        
        try {
            // Tampilkan data terlebih dahulu
            List<Document> documents = new ArrayList<>();
            collection.find().into(documents);
            
            if (documents.isEmpty()) {
                // Pesan jika tidak ada data
                System.out.println("Tidak ada data yang tersimpan untuk diperbarui.");
                return;
            }
            
            // Menampilkan data yang tersedia untuk diperbarui
            System.out.println("Data yang tersedia untuk diperbarui:");
            for (int i = 0; i < documents.size(); i++) {
                Document doc = documents.get(i);
                System.out.println((i + 1) + ". ID: " + doc.getObjectId("_id"));
                
                // Menampilkan salah satu field untuk identifikasi
                if (doc.containsKey("nama")) {
                    System.out.println("   Nama: " + doc.getString("nama"));
                } else if (doc.containsKey("judul")) {
                    System.out.println("   Judul: " + doc.getString("judul"));
                } else if (doc.containsKey("kode")) {
                    System.out.println("   Kode: " + doc.getString("kode"));
                }
            }
            
            // Meminta user memilih data yang akan diperbarui
            System.out.print("\nPilih nomor data yang akan diperbarui (1-" + documents.size() + "): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Membersihkan buffer
            
            if (choice < 1 || choice > documents.size()) {
                System.out.println("Pilihan tidak valid!");
                return;
            }
            
            // Mendapatkan dokumen yang dipilih
            Document selectedDoc = documents.get(choice - 1);
            String docId = selectedDoc.getObjectId("_id").toString();
            
            // Menampilkan field-field dari dokumen yang dipilih
            System.out.println("\nField yang tersedia:");
            List<String> fields = new ArrayList<>();
            selectedDoc.forEach((key, value) -> {
                if (!key.equals("_id")) {
                    fields.add(key);
                    System.out.println("- " + key + " (nilai sekarang: " + value + ")");
                }
            });
            
            // Meminta user memilih field yang akan diperbarui
            System.out.print("\nMasukkan nama field yang ingin diperbarui: ");
            String fieldToUpdate = scanner.nextLine();
            
            if (!fields.contains(fieldToUpdate)) {
                System.out.println("Field tidak ditemukan!");
                return;
            }
            
            // Meminta user memasukkan nilai baru
            System.out.print("Masukkan nilai baru untuk " + fieldToUpdate + ": ");
            String newValue = scanner.nextLine();
            
            // Membuat filter berdasarkan ID
            Bson filter = Filters.eq("_id", selectedDoc.getObjectId("_id"));
            
            // Membuat update operation
            Bson updateOperation;
            
            // Cek tipe data field yang akan diupdate
            Object currentValue = selectedDoc.get(fieldToUpdate);
            if (currentValue instanceof Integer) {
                // Jika tipe data integer
                updateOperation = Updates.set(fieldToUpdate, Integer.parseInt(newValue));
            } else if (currentValue instanceof Double) {
                // Jika tipe data double
                updateOperation = Updates.set(fieldToUpdate, Double.parseDouble(newValue));
            } else {
                // Untuk tipe data string dan lainnya
                updateOperation = Updates.set(fieldToUpdate, newValue);
            }
            
            // Melakukan update
            UpdateResult result = collection.updateOne(filter, updateOperation);
            
            // Menampilkan hasil update
            if (result.getModifiedCount() > 0) {
                System.out.println("Data berhasil diperbarui!");
            } else {
                System.out.println("Tidak ada data yang diperbarui.");
            }
            
        } catch (Exception e) {
            // Menampilkan error jika gagal memperbarui data
            System.err.println("Gagal memperbarui data: " + e.getMessage());
        }
    }
    
    /**
     * Fungsi untuk menghapus data dari MongoDB
     */
    private static void deleteData() {
        System.out.println("\n=== HAPUS DATA ===");
        
        try {
            // Tampilkan data terlebih dahulu
            List<Document> documents = new ArrayList<>();
            collection.find().into(documents);
            
            if (documents.isEmpty()) {
                // Pesan jika tidak ada data
                System.out.println("Tidak ada data yang tersimpan untuk dihapus.");
                return;
            }
            
            // Menampilkan data yang tersedia untuk dihapus
            System.out.println("Data yang tersedia untuk dihapus:");
            for (int i = 0; i < documents.size(); i++) {
                Document doc = documents.get(i);
                System.out.println((i + 1) + ". ID: " + doc.getObjectId("_id"));
                
                // Menampilkan salah satu field untuk identifikasi
                if (doc.containsKey("nama")) {
                    System.out.println("   Nama: " + doc.getString("nama"));
                } else if (doc.containsKey("judul")) {
                    System.out.println("   Judul: " + doc.getString("judul"));
                } else if (doc.containsKey("kode")) {
                    System.out.println("   Kode: " + doc.getString("kode"));
                }
            }
            
            // Meminta user memilih data yang akan dihapus
            System.out.print("\nPilih nomor data yang akan dihapus (1-" + documents.size() + "): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Membersihkan buffer
            
            if (choice < 1 || choice > documents.size()) {
                System.out.println("Pilihan tidak valid!");
                return;
            }
            
            // Mendapatkan dokumen yang dipilih
            Document selectedDoc = documents.get(choice - 1);
            
            // Konfirmasi penghapusan
            System.out.print("Anda yakin ingin menghapus data ini? (y/n): ");
            String confirm = scanner.nextLine();
            
            if (confirm.equalsIgnoreCase("y")) {
                // Membuat filter berdasarkan ID
                Bson filter = Filters.eq("_id", selectedDoc.getObjectId("_id"));
                
                // Melakukan delete
                DeleteResult result = collection.deleteOne(filter);
                
                // Menampilkan hasil delete
                if (result.getDeletedCount() > 0) {
                    System.out.println("Data berhasil dihapus!");
                } else {
                    System.out.println("Tidak ada data yang dihapus.");
                }
            } else {
                System.out.println("Penghapusan dibatalkan.");
            }
            
        } catch (Exception e) {
            // Menampilkan error jika gagal menghapus data
            System.err.println("Gagal menghapus data: " + e.getMessage());
        }
    }
    
    /**
     * Fungsi untuk mencari data berdasarkan kata kunci
     */
    private static void searchData() {
        System.out.println("\n=== CARI DATA ===");
        
        try {
            System.out.print("Masukkan field yang ingin dicari: ");
            String field = scanner.nextLine();
            
            System.out.print("Masukkan kata kunci: ");
            String keyword = scanner.nextLine();
            
            // Membuat filter pencarian (case insensitive dengan regex)
            Bson filter = Filters.regex(field, keyword, "i");
            
            // Mencari dokumen yang sesuai dengan filter
            List<Document> searchResults = new ArrayList<>();
            collection.find(filter).into(searchResults);
            
            // Menampilkan hasil pencarian
            if (searchResults.isEmpty()) {
                System.out.println("Tidak ditemukan data dengan " + field + " yang mengandung '" + keyword + "'.");
            } else {
                System.out.println("Ditemukan " + searchResults.size() + " data:");
                
                // Menampilkan hasil pencarian
                int counter = 1;
                for (Document doc : searchResults) {
                    System.out.println("\nHasil #" + counter);
                    System.out.println("ID: " + doc.getObjectId("_id"));
                    // Menampilkan semua field dan value dari dokumen
                    doc.forEach((key, value) -> {
                        if (!key.equals("_id")) {
                            System.out.println(key + ": " + value);
                        }
                    });
                    counter++;
                }
            }
            
        } catch (Exception e) {
            // Menampilkan error jika gagal mencari data
            System.err.println("Gagal mencari data: " + e.getMessage());
        }
    }
}