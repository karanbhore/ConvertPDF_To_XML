package com.cognizant.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.cognizant.model.PdfDocument;
import com.cognizant.repository.PdfDocumentRepository;

import jakarta.transaction.Transactional;

@Service
public class PdfToXmlService {
    @Autowired
    private PdfDocumentRepository pdfDocumentRepository;

    @Transactional
    public void generateXmlFromPdf(Long pdfId, String xmlFilePath) throws IOException, ParserConfigurationException, TransformerException {
        Optional<PdfDocument> pdfDocumentOptional = pdfDocumentRepository.findById(pdfId);
        if (pdfDocumentOptional.isPresent()) {
            PdfDocument pdfDocument = pdfDocumentOptional.get();
            byte[] pdfData = pdfDocument.getPdfData();

            // Load PDF from byte array
            try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfData))) {
                PDFTextStripper pdfStripper = new PDFTextStripper();
                String pdfText = pdfStripper.getText(document);

                // Create XML document
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document xmlDoc = docBuilder.newDocument();

                // Root element
                Element rootElement = xmlDoc.createElement("PDFContent");
                xmlDoc.appendChild(rootElement);

                // Content element
                Element contentElement = xmlDoc.createElement("Content");
                contentElement.appendChild(xmlDoc.createTextNode(pdfText));
                rootElement.appendChild(contentElement);

                // Write the XML content to a file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty("indent", "yes");
                DOMSource source = new DOMSource(xmlDoc);
                StreamResult result = new StreamResult(new File(xmlFilePath));

                transformer.transform(source, result);

                System.out.println("XML file created: " + xmlFilePath);
            }
        } else {
            throw new IOException("PDF with ID " + pdfId + " not found.");
        }
    }
}
