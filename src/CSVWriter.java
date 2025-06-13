
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CSVWriter {
    
    public static void main(String[] args) {
        // Nama file CSV yang akan ditulis
        String csvFile = "./data/student-praktikum.csv";
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== PROGRAM PENAMBAH DATA MAHASISWA KE CSV ===");
        System.out.println("Data akan ditambahkan ke file: " + csvFile);
        System.out.println("===============================================\n");
        
        // Menanyakan mode penulisan kepada pengguna
        System.out.println("Pilih mode penulisan:");
        System.out.println("1. Overwrite (menimpa file yang ada)");
        System.out.println("2. Append (menambahkan ke file yang sudah ada)");
        System.out.print("Pilihan Anda (1/2): ");
        
        int pilihan = scanner.nextInt();
        scanner.nextLine(); // Consume newline setelah nextInt()
        
        boolean appendMode = (pilihan == 2);
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile, appendMode))) {
            
            // Jika mode overwrite, tulis header terlebih dahulu
            if (!appendMode) {
                String header = "NIM,NAMA,UMUR,PRODI";
                bw.write(header);
                bw.newLine();
                System.out.println("Header CSV berhasil ditulis.");
            }
            
            System.out.println("\n=== INPUT DATA MAHASISWA ===");
            System.out.println("Ketik 'selesai' pada NIM untuk mengakhiri input\n");
            
            int nomorUrut = 1; // Counter untuk menampilkan urutan input
            
            // Loop untuk input data mahasiswa
            while (true) {
                System.out.println("--- Data Mahasiswa ke-" + nomorUrut + " ---");
                
                // Input NIM dengan validasi
                System.out.print("Masukkan NIM: ");
                String nim = scanner.nextLine().trim();
                
                // Kondisi untuk mengakhiri input
                if (nim.equalsIgnoreCase("selesai")) {
                    System.out.println("Input data selesai.");
                    break;
                }
                
                // Validasi NIM tidak boleh kosong
                if (nim.isEmpty()) {
                    System.out.println("NIM tidak boleh kosong! Silakan coba lagi.\n");
                    continue;
                }
                
                // Input Nama dengan validasi
                System.out.print("Masukkan Nama: ");
                String nama = scanner.nextLine().trim();
                if (nama.isEmpty()) {
                    System.out.println("Nama tidak boleh kosong! Silakan coba lagi.\n");
                    continue;
                }
                
                // Input Umur dengan validasi
                int umur = 0;
                boolean validUmur = false;
                while (!validUmur) {
                    try {
                        System.out.print("Masukkan Umur: ");
                        umur = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        
                        if (umur > 0 && umur < 100) {
                            validUmur = true;
                        } else {
                            System.out.println("Umur harus antara 1-99 tahun!");
                        }
                    } catch (Exception e) {
                        System.out.println("Umur harus berupa angka!");
                        scanner.nextLine(); // Clear invalid input
                    }
                }
                
                // Input Program Studi dengan validasi
                System.out.print("Masukkan Program Studi (TI/TE/SI/dll): ");
                String prodi = scanner.nextLine().trim().toUpperCase();
                if (prodi.isEmpty()) {
                    System.out.println("Program Studi tidak boleh kosong! Silakan coba lagi.\n");
                    continue;
                }
                
                // Membuat string data CSV
                String dataLine = nim + "," + nama + "," + umur + "," + prodi;
                
                // Menulis data ke file
                bw.write(dataLine);
                bw.newLine();
                
                // Konfirmasi data yang berhasil disimpan
                System.out.println("✓ Data berhasil disimpan: " + dataLine);
                System.out.println(""); // Baris kosong untuk pemisah
                
                nomorUrut++;
            }
            
            System.out.println("\n===============================================");
            System.out.println("PROSES SELESAI!");
            System.out.println("Total data yang ditambahkan: " + (nomorUrut - 1) + " mahasiswa");
            System.out.println("File tersimpan di: " + csvFile);
            
        } catch (IOException e) {
            // Handle error saat menulis file
            System.err.println("Error saat menulis ke file CSV:");
            System.err.println("Pesan error: " + e.getMessage());
        } finally {
            // Tutup scanner untuk mencegah resource leak
            scanner.close();
            System.out.println("Program selesai. Terima kasih!");
        }
    }
}