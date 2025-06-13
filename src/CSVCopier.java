import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class CSVCopier {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== PROGRAM PENYALIN FILE CSV ===");
        System.out.println("Program ini akan menyalin isi file CSV dari sumber ke tujuan");
        System.out.println("=================================================\n");
        
        try {
            // Input nama file sumber
            String sourceFile = getValidFileName(scanner, "sumber");
            
            // Input nama file tujuan
            String targetFile = getValidFileName(scanner, "tujuan");
            
            // Cek apakah file sumber ada dan bisa dibaca
            if (!isFileReadable(sourceFile)) {
                System.err.println("Error: File sumber '" + sourceFile + "' tidak ditemukan atau tidak bisa dibaca!");
                return;
            }
            
            // Cek apakah file tujuan sudah ada
            if (Files.exists(Paths.get(targetFile))) {
                System.out.println("\nPeringatan: File tujuan '" + targetFile + "' sudah ada!");
                System.out.print("Apakah Anda ingin menimpanya? (y/n): ");
                String confirm = scanner.nextLine().trim().toLowerCase();
                
                if (!confirm.equals("y") && !confirm.equals("yes")) {
                    System.out.println("Operasi dibatalkan oleh pengguna.");
                    return;
                }
            }
            
            // Pilihan mode penyalinan
            int copyMode = getCopyMode(scanner);
            
            // Melakukan proses penyalinan
            int result = copyCSVFile(sourceFile, targetFile, copyMode);
            
            // Menampilkan hasil
            if (result > 0) {
                System.out.println("\n=== HASIL PENYALINAN ===");
                System.out.println("✓ Penyalinan berhasil!");
                System.out.println("File sumber: " + sourceFile);
                System.out.println("File tujuan: " + targetFile);
                System.out.println("Total baris yang disalin: " + result);
                
                // Tampilkan ukuran file untuk verifikasi
                displayFileInfo(sourceFile, targetFile);
            } else {
                System.err.println("Penyalinan gagal atau tidak ada data yang disalin.");
            }
            
        } catch (Exception e) {
            System.err.println("Error tidak terduga: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
    
    /**
     * Method untuk mendapatkan nama file yang valid dari pengguna
     */
    private static String getValidFileName(Scanner scanner, String type) {
        String fileName;
        while (true) {
            System.out.print("Masukkan nama file " + type + " (dengan ekstensi .csv): ");
            fileName = scanner.nextLine().trim();
            
            if (fileName.isEmpty()) {
                System.out.println("Nama file tidak boleh kosong!");
                continue;
            }
            
            // Otomatis tambahkan ekstensi .csv jika belum ada
            if (!fileName.toLowerCase().endsWith(".csv")) {
                fileName += ".csv";
                System.out.println("Ekstensi .csv ditambahkan otomatis: " + fileName);
            }
            
            break;
        }
        return fileName;
    }
    
    /**
     * Method untuk mengecek apakah file bisa dibaca
     */
    private static boolean isFileReadable(String fileName) {
        Path path = Paths.get(fileName);
        return Files.exists(path) && Files.isReadable(path) && !Files.isDirectory(path);
    }
    
    /**
     * Method untuk mendapatkan pilihan mode penyalinan dari pengguna
     */
    private static int getCopyMode(Scanner scanner) {
        System.out.println("\nPilih mode penyalinan:");
        System.out.println("1. Copy semua data (termasuk header)");
        System.out.println("2. Copy hanya data (tanpa header)");
        System.out.println("3. Copy dengan validasi (skip baris yang error)");
        
        int mode;
        while (true) {
            try {
                System.out.print("Pilihan Anda (1-3): ");
                mode = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                if (mode >= 1 && mode <= 3) {
                    break;
                } else {
                    System.out.println("Pilihan harus antara 1-3!");
                }
            } catch (Exception e) {
                System.out.println("Input harus berupa angka!");
                scanner.nextLine(); // Clear invalid input
            }
        }
        return mode;
    }
    
    /**
     * Method utama untuk menyalin file CSV berdasarkan mode yang dipilih
     */
    private static int copyCSVFile(String sourceFile, String targetFile, int mode) {
        int lineCount = 0;
        int errorCount = 0;
        boolean isFirstLine = true;
        
        System.out.println("\nMemulai proses penyalinan...");
        
        try (BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile))) {
            
            String line;
            
            while ((line = reader.readLine()) != null) {
                boolean shouldCopyLine = false;
                
                // Logika berdasarkan mode yang dipilih
                switch (mode) {
                    case 1: // Copy semua data
                        shouldCopyLine = true;
                        break;
                        
                    case 2: // Copy tanpa header
                        if (isFirstLine) {
                            isFirstLine = false;
                            continue; // Skip header
                        }
                        shouldCopyLine = true;
                        break;
                        
                    case 3: // Copy dengan validasi
                        if (isFirstLine) {
                            shouldCopyLine = true; // Header selalu di-copy
                            isFirstLine = false;
                        } else {
                            // Validasi format CSV (harus ada 4 kolom)
                            String[] fields = line.split(",");
                            if (fields.length == 4 && !line.trim().isEmpty()) {
                                shouldCopyLine = true;
                            } else {
                                errorCount++;
                                System.out.println("Baris error diabaikan: " + line);
                            }
                        }
                        break;
                }
                
                // Tulis baris jika valid
                if (shouldCopyLine) {
                    writer.write(line);
                    writer.newLine();
                    lineCount++;
                    
                    // Progress indicator setiap 10 baris
                    if (lineCount % 10 == 0) {
                        System.out.print(".");
                    }
                }
            }
            
            System.out.println("\nProses penyalinan selesai.");
            
            if (mode == 3 && errorCount > 0) {
                System.out.println("Peringatan: " + errorCount + " baris error diabaikan.");
            }
            
        } catch (IOException e) {
            System.err.println("Error saat menyalin file: " + e.getMessage());
            return -1;
        }
        
        return lineCount;
    }
    
    /**
     * Method untuk menampilkan informasi file setelah penyalinan
     */
    private static void displayFileInfo(String sourceFile, String targetFile) {
        try {
            Path sourcePath = Paths.get(sourceFile);
            Path targetPath = Paths.get(targetFile);
            
            long sourceSize = Files.size(sourcePath);
            long targetSize = Files.size(targetPath);
            
            System.out.println("\n=== INFORMASI FILE ===");
            System.out.printf("Ukuran file sumber: %d bytes%n", sourceSize);
            System.out.printf("Ukuran file tujuan: %d bytes%n", targetSize);
            
            if (sourceSize == targetSize) {
                System.out.println("✓ Ukuran file sama - penyalinan sempurna!");
            } else {
                System.out.println("ℹ Ukuran file berbeda - mungkin karena mode penyalinan yang dipilih.");
            }
            
        } catch (IOException e) {
            System.out.println("Tidak dapat menampilkan informasi ukuran file.");
        }
    }
}