package com.deliboyraz.eticaret.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "http://localhost:5173")
public class FileUploadController {

    private final String uploadDir;

    public FileUploadController() {
        // Proje kök dizininde 'uploads' klasörü oluştur
        this.uploadDir = "uploads/";
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Uploads dizini oluşturulamadı!", e);
        }
    }

    // Çoklu dosya yükleme endpoint'i
    @PostMapping("/images")
    public ResponseEntity<List<String>> uploadImages(@RequestParam("files") MultipartFile[] files) {
        try {
            List<String> fileNames = new ArrayList<>();

            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue;
                }

                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);

                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                fileNames.add(fileName);
            }

            if (fileNames.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(null);
            }

            return ResponseEntity.ok(fileNames);

        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // Tekli dosya yükleme endpoint'i
    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Lütfen bir dosya seçin");
            }

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.ok(fileName);

        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Dosya yükleme hatası: " + e.getMessage());
        }
    }

    // Dosya silme endpoint'i
    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteImage(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(uploadDir + fileName);

            if (!Files.exists(filePath)) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Dosya bulunamadı");
            }

            Files.delete(filePath);
            return ResponseEntity.ok("Dosya başarıyla silindi");

        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Dosya silme hatası: " + e.getMessage());
        }
    }

    // Dosya var mı kontrol endpoint'i
    @GetMapping("/check/{fileName}")
    public ResponseEntity<Boolean> checkImageExists(@PathVariable String fileName) {
        Path filePath = Paths.get(uploadDir + fileName);
        return ResponseEntity.ok(Files.exists(filePath));
    }

    // Maksimum dosya boyutu kontrolü için yardımcı metod
    private boolean isValidFileSize(MultipartFile file) {
        // Maksimum dosya boyutu (örn: 5MB)
        long maxFileSize = 5 * 1024 * 1024; // 5MB in bytes
        return file.getSize() <= maxFileSize;
    }

    // Dosya tipi kontrolü için yardımcı metod
    private boolean isValidFileType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null) return false;

        // İzin verilen dosya tipleri
        return contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/gif") ||
                contentType.equals("image/webp");
    }

    // Dosya adı temizleme için yardımcı metod
    private String sanitizeFileName(String fileName) {
        // Özel karakterleri ve boşlukları temizle
        return fileName.replaceAll("[^a-zA-Z0-9.-]", "_");
    }
}