package io.github.delokoseni.rag_search.dto;

import java.util.List;

/**
 * Результат выполнения полного RAG-запроса.
 */
public record RagResult(

        String question,

        String model,

        List<RetrievedChunk> chunks,

        String prompt,

        String answer

) {
}