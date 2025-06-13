
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader {
    
    public static void main(String[] args) {
        // Nama file CSV yang akan dibaca
        String csvFile = "./data/student-praktikum.csv";
        String line = "";
        String csvSplitBy = ",";
        int jumlahBaris = 0; // Counter untuk menghitung jumlah baris data
        boolean isFirstLine = true; // Flag untuk mengidentifikasi header
        
        System.out.println("=== PROGRAM PEMBACA FILE CSV MAHASISWA ===");
        System.out.println("Membaca data dari file: " + csvFile);
        System.out.println("==========================================\n");
        
        // Menggunakan try-with-resources untuk otomatis menutup file
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            
            // Membaca file baris per baris
            while ((line = br.readLine()) != null) {
                
                // Jika baris pertama (header), tampilkan sebagai header
                if (isFirstLine) {
                    String[] header = line.split(csvSplitBy);
                    System.out.println("Header CSV:");
                    System.out.printf("%-5s %-15s %-5s %-10s%n", 
                                    header[0], header[1], header[2], header[3]);
                    System.out.println("----------------------------------------");
                    isFirstLine = false;
                    continue; // Skip ke iterasi berikutnya, jangan hitung header
                }
                
                // Memisahkan data berdasarkan koma
                String[] student = line.split(csvSplitBy);
                
                // Validasi: pastikan data memiliki 4 kolom
                if (student.length >= 4) {
                    // Menampilkan data mahasiswa dengan format yang rapi
                    System.out.printf("%-5s %-15s %-5s %-10s%n", 
                                    student[0], student[1], student[2], student[3]);
                    
                    // Increment counter untuk setiap baris data valid
                    jumlahBaris++;
                } else {
                    // Jika ada baris yang tidak valid, beri peringatan
                    System.out.println("Peringatan: Baris tidak valid - " + line);
                }
            }
            
            // Tampilkan hasil akhir
            System.out.println("==========================================");
            System.out.println("HASIL PEMBACAAN:");
            System.out.println("Total jumlah baris data mahasiswa: " + jumlahBaris);
            
            // Berikan informasi tambahan berdasarkan jumlah data
            if (jumlahBaris > 0) {
                System.out.println("File berhasil dibaca dengan " + jumlahBaris + " record mahasiswa.");
            } else {
                System.out.println("Tidak ada data mahasiswa yang valid ditemukan.");
            }
            
        } catch (IOException e) {
            // Handle error jika file tidak ditemukan atau ada masalah I/O
            System.err.println("Error saat membaca file CSV:");
            System.err.println("Pesan error: " + e.getMessage());
            System.err.println("\nPastikan file '" + csvFile + "' ada di direktori yang sama dengan program Java ini.");
        }
    }
}
