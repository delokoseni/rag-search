package io.github.delokoseni.rag_search.dto;

import java.util.List;

/**
 * Результат выполнения RAG-запроса.
 *
 * Используется как единый объект,
 * содержащий всю информацию,
 * необходимую как для ответа пользователю,
 * так и для последующей оценки качества (RAGAS).
 */
public record RagResult(

        String question,

        String model,

        String prompt,

        String context,

        List<RetrievedChunk> chunks,

        String answer

) {
}