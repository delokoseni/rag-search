package io.github.delokoseni.rag_search.llm.ollama;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;

/**
 * Конфигурация интеграции с Ollama.
 *
 * Отвечает за создание и настройку клиентов для работы с локальной LLM:
 * - ChatModel для генерации текста
 * - (в будущем) EmbeddingModel для RAG
 *
 * Все настройки берутся из OllamaProperties.
 */
@Configuration
public class OllamaConfig {

    /**
     * Создаёт ChatModel, который используется во всём приложении
     * для взаимодействия с LLM (Qwen, LLaMA и др.).
     *
     * @param props конфигурация Ollama (URL и модель)
     * @return настроенный ChatModel
     */
    @Bean
    public ChatModel chatModel(OllamaProperties props) {

        return OllamaChatModel.builder()
                .baseUrl(props.url())
                .modelName(props.chatModel())
                .build();
    }

    /**
     * Embedding модель для преобразования текста в векторы.
     *
     * Используется в RAG для:
     * - поиска похожих документов
     * - работы с pgvector
     */
    @Bean
    public EmbeddingModel embeddingModel(OllamaProperties props) {

        return OllamaEmbeddingModel.builder()
                .baseUrl(props.url())
                .modelName(props.embeddingModel())
                .build();
    }
}