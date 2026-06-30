package io.github.delokoseni.rag_search.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "rag.embedding")
public class RagProperties {

    /**
     * Размер одного батча для получения эмбеддингов.
     */
    private int batchSize = 64;

}