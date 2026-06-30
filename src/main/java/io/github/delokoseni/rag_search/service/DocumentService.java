package io.github.delokoseni.rag_search.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import io.github.delokoseni.rag_search.dto.ChunkInsert;
import io.github.delokoseni.rag_search.model.DocumentChunk;
import io.github.delokoseni.rag_search.repository.DocumentChunkJdbcRepository;
import io.github.delokoseni.rag_search.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentChunkJdbcRepository chunkRepository;

    private final DocumentParserService parserService;
    private final EmbeddingService embeddingService;

    /**
     * Разбивает документ на чанки по 500 токенов
     * с перекрытием 100 токенов.
     */
    private final DocumentSplitter splitter =
            DocumentSplitters.recursive(500, 100);

    @Transactional
    public void process(MultipartFile[] files) {

        for (MultipartFile file : files) {

            try {

                String text = parserService.extractText(file);

                io.github.delokoseni.rag_search.model.Document document =
                        io.github.delokoseni.rag_search.model.Document.builder()
                                .fileName(file.getOriginalFilename())
                                .uploadDate(LocalDateTime.now())
                                .build();

                documentRepository.save(document);

                List<TextSegment> segments =
                        splitter.split(Document.from(text));

                List<ChunkInsert> inserts = new ArrayList<>();

                for (int i = 0; i < segments.size(); i++) {

                    TextSegment segment = segments.get(i);

                    inserts.add(
                            new ChunkInsert(
                                    document.getId(),
                                    i,
                                    segment.text(),
                                    embeddingService.createEmbedding(segment.text())
                            )
                    );

                }

                chunkRepository.batchInsert(inserts);

            } catch (Exception e) {

                throw new RuntimeException(
                        "Ошибка обработки файла: "
                                + file.getOriginalFilename(),
                        e
                );

            }

        }

    }

}