package io.github.delokoseni.rag_search.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

@Service
public class DocumentParserService {

    public String extractText(MultipartFile file) throws Exception {

        String fileName = file.getOriginalFilename();

        if (fileName == null) {
            throw new IllegalArgumentException("Файл без имени.");
        }

        String extension = fileName.substring(fileName.lastIndexOf('.') + 1)
                .toLowerCase();

        return switch (extension) {

            case "txt", "md" ->
                    new String(file.getBytes(), StandardCharsets.UTF_8);

            case "pdf" -> parsePdf(file);

            case "docx" -> parseDocx(file);

            case "doc" -> parseDoc(file);

            default ->
                    throw new IllegalArgumentException(
                            "Неподдерживаемый формат: " + extension
                    );
        };
    }

    private String parsePdf(MultipartFile file) throws Exception {

        try (PDDocument document =
                     Loader.loadPDF(file.getBytes())) {

            PDFTextStripper stripper = new PDFTextStripper();

            return stripper.getText(document);
        }
    }

    private String parseDocx(MultipartFile file) throws Exception {

        try (XWPFDocument document =
                     new XWPFDocument(file.getInputStream())) {

            XWPFWordExtractor extractor =
                    new XWPFWordExtractor(document);

            return extractor.getText();
        }
    }

    private String parseDoc(MultipartFile file) throws Exception {

        try (HWPFDocument document =
                     new HWPFDocument(file.getInputStream())) {

            WordExtractor extractor =
                    new WordExtractor(document);

            return extractor.getText();
        }
    }

}