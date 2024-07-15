package com.cognizant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.service.PdfToXmlService;

@RestController
public class PdfController {
    @Autowired
    private PdfToXmlService pdfToXmlService;

    @GetMapping("/generate-xml/{pdfId}")
    public String generateXml(@PathVariable Long pdfId, @RequestParam String xmlFilePath) {
        try {
            pdfToXmlService.generateXmlFromPdf(pdfId, xmlFilePath);
            return "XML file created successfully at " + xmlFilePath;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating XML: " + e.getMessage();
        }
    }
}
