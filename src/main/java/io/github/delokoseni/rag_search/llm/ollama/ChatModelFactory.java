package io.github.delokoseni.rag_search.llm.ollama;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Фабрика для создания ChatModel.
 *
 * Для каждой модели экземпляр создаётся только один раз,
 * после чего переиспользуется.
 */
@Component
@RequiredArgsConstructor
public class ChatModelFactory {

    private final OllamaProperties props;

    /**
     * Кэш уже созданных моделей.
     */
    private final Map<String, ChatModel> models = new ConcurrentHashMap<>();

    /**
     * Возвращает ChatModel для указанной модели Ollama.
     *
     * Если модель уже создавалась ранее,
     * возвращается существующий экземпляр.
     */
    public ChatModel create(String modelName) {

        return models.computeIfAbsent(modelName, name ->
                OllamaChatModel.builder()
                        .baseUrl(props.url())
                        .modelName(name)
                        .timeout(Duration.ofMinutes(5))
                        .build()
        );
    }

}