package io.github.delokoseni.rag_search.controller;

import io.github.delokoseni.rag_search.dto.RagResult;
import io.github.delokoseni.rag_search.service.RagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final RagService ragService;

    @PostMapping
    public Map<String, String> chat(
            @RequestBody Map<String, String> request
    ) {

        String question =
                request.get("message");

        RagResult result =
                ragService.ask(question);

        return Map.of(
                "answer",
                result.answer()
        );

    }

}