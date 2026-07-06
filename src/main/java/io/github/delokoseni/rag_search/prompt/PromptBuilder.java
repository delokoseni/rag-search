package io.github.delokoseni.rag_search.prompt;

import io.github.delokoseni.rag_search.dto.RetrievalResult;
import io.github.delokoseni.rag_search.dto.RetrievedChunk;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Отвечает за построение промпта для LLM.
 */
@Component
public class PromptBuilder {

    public String build(RetrievalResult retrieval) {

        String context =
                retrieval.chunks()
                        .stream()
                        .map(this::formatChunk)
                        .collect(Collectors.joining(
                                "\n\n----------------------------------------\n\n"
                        ));

        return """
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
                .formatted(
                        context,
                        retrieval.question()
                );
    }

    private String formatChunk(RetrievedChunk chunk) {

        return """
                Документ: %s
                Фрагмент: %d

                %s
                """
                .formatted(
                        chunk.getFileName(),
                        chunk.getChunkIndex(),
                        chunk.getContent()
                );
    }

}