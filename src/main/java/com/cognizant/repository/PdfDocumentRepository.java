package com.cognizant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cognizant.model.PdfDocument;

public interface PdfDocumentRepository extends JpaRepository<PdfDocument, Long> {
}
