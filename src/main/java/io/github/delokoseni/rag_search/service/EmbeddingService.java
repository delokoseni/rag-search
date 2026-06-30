package io.github.delokoseni.rag_search.service;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import io.github.delokoseni.rag_search.config.RagProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmbeddingService {

    private final EmbeddingModel embeddingModel;
    private final RagProperties ragProperties;

    /**
     * Получение эмбеддингов для списка текстов.
     */
    public List<String> createEmbeddings(List<TextSegment> segments) {

        List<String> result = new ArrayList<>();

        int batchSize = ragProperties.getBatchSize();

        for (int i = 0; i < segments.size(); i += batchSize) {

            int end = Math.min(i + batchSize, segments.size());

            List<TextSegment> batch = segments.subList(i, end);

            List<Embedding> embeddings =
                    embeddingModel
                            .embedAll(batch)
                            .content();

            for (Embedding embedding : embeddings) {
                result.add(toVectorString(embedding.vector()));
            }

        }

        return result;
    }

    /**
     * Преобразование float[] -> "[0.12,0.45,...]"
     */
    private String toVectorString(float[] vector) {

        StringBuilder builder = new StringBuilder("[");

        for (int i = 0; i < vector.length; i++) {

            builder.append(vector[i]);

            if (i != vector.length - 1) {
                builder.append(",");
            }

        }

        builder.append("]");

        return builder.toString();

    }

}