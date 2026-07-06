package io.github.delokoseni.rag_search.service;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import io.github.delokoseni.rag_search.dto.RagResult;
import io.github.delokoseni.rag_search.dto.RetrievalResult;
import io.github.delokoseni.rag_search.dto.RetrievedChunk;
import io.github.delokoseni.rag_search.llm.ollama.ChatModelFactory;
import io.github.delokoseni.rag_search.llm.ollama.OllamaProperties;
import io.github.delokoseni.rag_search.prompt.PromptBuilder;
import io.github.delokoseni.rag_search.repository.DocumentChunkJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RagService {

    private final ChatModelFactory chatModelFactory;

    private final OllamaProperties props;

    private final EmbeddingModel embeddingModel;

    private final EmbeddingService embeddingService;

    private final DocumentChunkJdbcRepository chunkRepository;

    private final PromptBuilder promptBuilder;

    /**
     * Использует модель по умолчанию.
     */
    public RagResult ask(String question) {

        RetrievalResult retrieval =
                retrieve(question);

        return generate(
                retrieval,
                props.chatModel()
        );
    }

    /**
     * Использует указанную модель.
     */
    public RagResult ask(
            String question,
            String modelName
    ) {

        RetrievalResult retrieval =
                retrieve(question);

        return generate(
                retrieval,
                modelName
        );
    }

    /**
     * Retrieval.
     */
    public RetrievalResult retrieve(String question) {

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

        return new RetrievalResult(
                question,
                chunks
        );
    }

    /**
     * Generation.
     */
    public RagResult generate(
            RetrievalResult retrieval,
            String modelName
    ) {

        ChatModel chatModel =
                chatModelFactory.create(modelName);

        String prompt =
                promptBuilder.build(retrieval);

        String answer =
                chatModel.chat(prompt);

        return new RagResult(
                retrieval.question(),
                modelName,
                retrieval.chunks(),
                prompt,
                answer
        );
    }

}