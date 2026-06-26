package io.github.delokoseni.rag_search.controller;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatModel chatModel;
    private final EmbeddingModel embeddingModel;

    public ChatController(ChatModel chatModel,
                          EmbeddingModel embeddingModel) {
        this.chatModel = chatModel;
        this.embeddingModel = embeddingModel;
    }

    @PostMapping
    public Map<String, String> chat(@RequestBody Map<String, String> request) {

        String question = request.get("message");

        Embedding embedding = embeddingModel.embed(question).content();

        // TODO: pgvector search

        String context = "Пока нет базы знаний";

        String prompt = """
                Ты RAG ассистент.
                Используй контекст ниже для ответа.

                Контекст:
                %s

                Вопрос:
                %s
                """.formatted(context, question);

        String answer = chatModel.chat(prompt);

        return Map.of("answer", answer);
    }
}