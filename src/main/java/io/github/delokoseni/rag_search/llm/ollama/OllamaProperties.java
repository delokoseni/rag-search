package io.github.delokoseni.rag_search.llm.ollama;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Конфигурационные свойства для интеграции с Ollama.
 *
 * <p>Загружаются из application.properties или переменных окружения
 * с префиксом <b>ollama</b>.</p>
 *
 * <p>Используются для настройки подключения к локальному серверу Ollama
 * и выбора моделей для генерации текста и эмбеддингов.</p>
 *
 * Пример конфигурации:
 * <pre>
 * ollama.url=http://localhost:11434
 * ollama.chat-model=qwen3:8b
 * ollama.embedding-model=nomic-embed-text
 * </pre>
 *
 * @param url базовый URL сервера Ollama
 * @param chatModel модель для генерации текста (LLM)
 * @param embeddingModel модель для получения эмбеддингов (RAG)
 */
@ConfigurationProperties(prefix = "ollama")
public record OllamaProperties(
        String url,
        String chatModel,
        String embeddingModel
) {}