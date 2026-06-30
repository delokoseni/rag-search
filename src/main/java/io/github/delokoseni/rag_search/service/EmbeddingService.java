package io.github.delokoseni.rag_search.service;

import dev.langchain4j.model.embedding.EmbeddingModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmbeddingService {

    private final EmbeddingModel embeddingModel;

    public String createEmbedding(String text) {

        float[] vector = embeddingModel
                .embed(text)
                .content()
                .vector();

        StringBuilder builder = new StringBuilder("[");

        for (int i = 0; i < vector.length; i++) {

            builder.append(vector[i]);

            if (i < vector.length - 1) {
                builder.append(",");
            }

        }

        builder.append("]");

        return builder.toString();
    }

}