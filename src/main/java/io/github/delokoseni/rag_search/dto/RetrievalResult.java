package io.github.delokoseni.rag_search.dto;

import java.util.List;

/**
 * Результат этапа Retrieval.
 *
 * Содержит только результаты поиска.
 */
public record RetrievalResult(

        String question,

        List<RetrievedChunk> chunks

) {
}