package io.github.delokoseni.rag_search.controller;

import io.github.delokoseni.rag_search.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadController {

    private final DocumentService documentService;

    @PostMapping
    public ResponseEntity<String> upload(
            @RequestParam("files") MultipartFile[] files) {

        if (files.length == 0) {
            return ResponseEntity.badRequest()
                    .body("Файлы отсутствуют.");
        }

        documentService.process(files);

        return ResponseEntity.ok("Документы успешно загружены.");
    }

}