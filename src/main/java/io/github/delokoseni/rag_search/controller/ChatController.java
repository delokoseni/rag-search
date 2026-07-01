package io.github.delokoseni.rag_search.controller;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import io.github.delokoseni.rag_search.dto.RetrievedChunk;
import io.github.delokoseni.rag_search.repository.DocumentChunkJdbcRepository;
import io.github.delokoseni.rag_search.service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatModel chatModel;
    private final EmbeddingModel embeddingModel;
    private final EmbeddingService embeddingService;
    private final DocumentChunkJdbcRepository chunkRepository;

    @PostMapping
    public Map<String, String> chat(
            @RequestBody Map<String, String> request
    ) {

        String question = request.get("message");

        Embedding embedding =
                embeddingModel.embed(question).content();

        String vector =
                embeddingService.toVectorString(
                        embedding.vector()
                );

        List<RetrievedChunk> chunks =
                chunkRepository.findSimilarChunks(
                        vector,
                        30
                );

        String context =
                chunks.stream()
                        .map(chunk ->
                                """
                                Документ: %s
                                Фрагмент: %d

                                %s
                                """
                                        .formatted(
                                                chunk.getFileName(),
                                                chunk.getChunkIndex(),
                                                chunk.getContent()
                                        )
                        )
                        .collect(Collectors.joining("\n\n----------------------------------------\n\n"));

        String prompt = """
                Ты интеллектуальный RAG-ассистент.

                Используй ТОЛЬКО информацию из предоставленного контекста.

                Если ответа в контексте нет,
                честно скажи, что не удалось найти информацию.

                Не придумывай факты.

                После ответа обязательно перечисли,
                из каких документов была получена информация.

                =====================
                КОНТЕКСТ

                %s

                =====================

                ВОПРОС

                %s
                """
                .formatted(context, question);

        String answer = chatModel.chat(prompt);

        return Map.of("answer", answer);

    }

}