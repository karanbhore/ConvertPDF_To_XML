package com.cognizant.configuration;

import java.io.File;
import java.io.FileInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cognizant.model.PdfDocument;
import com.cognizant.repository.PdfDocumentRepository;

@Configuration
public class DatabaseLoader {

    @Autowired
    private PdfDocumentRepository pdfDocumentRepository;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            File file = new File("path/to/your/pdf_file.pdf");
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] pdfData = fis.readAllBytes();
                PdfDocument pdfDocument = new PdfDocument();
                pdfDocument.setId(1L); // Set the ID
                pdfDocument.setPdfData(pdfData);
                pdfDocumentRepository.save(pdfDocument);
                System.out.println("PDF file inserted.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}
