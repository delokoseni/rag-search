
package io.github.delokoseni.rag_search.service;

import io.github.delokoseni.rag_search.exceptions.DocumentParsingException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class DocumentParserService {

    public String extractText(MultipartFile file) {

        String fileName = file.getOriginalFilename();

        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("Файл без имени.");
        }

        String extension = fileName
                .substring(fileName.lastIndexOf('.') + 1)
                .toLowerCase();

        try {
            return switch (extension) {
                case "txt", "md" ->
                        new String(file.getBytes(), StandardCharsets.UTF_8);

                case "pdf" -> parsePdf(file);

                case "docx" -> parseDocx(file);

                case "doc" -> parseDoc(file);

                default -> throw new IllegalArgumentException(
                        "Неподдерживаемый формат: " + extension
                );
            };
        } catch (IOException e) {
            throw new DocumentParsingException(
                    "Не удалось прочитать файл: " + fileName,
                    e
            );
        }
    }

    private String parsePdf(MultipartFile file) throws IOException {

        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private String parseDocx(MultipartFile file) throws IOException {

        try (XWPFDocument document =
                     new XWPFDocument(file.getInputStream())) {

            XWPFWordExtractor extractor =
                    new XWPFWordExtractor(document);

            return extractor.getText();
        }
    }

    private String parseDoc(MultipartFile file) throws IOException {

        try (HWPFDocument document =
                     new HWPFDocument(file.getInputStream())) {

            WordExtractor extractor = new WordExtractor(document);
            return extractor.getText();
        }
    }
}
