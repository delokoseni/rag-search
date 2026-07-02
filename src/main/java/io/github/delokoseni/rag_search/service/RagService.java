package io.github.delokoseni.rag_search.service;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import io.github.delokoseni.rag_search.dto.RagResult;
import io.github.delokoseni.rag_search.dto.RetrievedChunk;
import io.github.delokoseni.rag_search.repository.DocumentChunkJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RagService {

    private final ChatModel chatModel;

    private final EmbeddingModel embeddingModel;

    private final EmbeddingService embeddingService;

    private final DocumentChunkJdbcRepository chunkRepository;

    public RagResult ask(String question) {

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
                        .collect(Collectors.joining(
                                "\n\n----------------------------------------\n\n"
                        ));

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

        String answer =
                chatModel.chat(prompt);

        return new RagResult(
                question,
                "unknown",
                prompt,
                context,
                chunks,
                answer
        );

    }

}