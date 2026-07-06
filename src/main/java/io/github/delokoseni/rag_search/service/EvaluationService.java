package io.github.delokoseni.rag_search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.delokoseni.rag_search.dto.RagResult;
import io.github.delokoseni.rag_search.evaluation.EvaluationQuestion;
import io.github.delokoseni.rag_search.evaluation.EvaluationRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final RagService ragService;
    private final ObjectMapper objectMapper;

    /**
     * Список моделей для сравнения.
     * Потом легко расширишь.
     */
    private final List<String> models = List.of(
            "qwen3:8b",
            "llama3.1:8b"
    );

    /**
     * Главный метод запуска эксперимента
     */
    public void runEvaluation(List<EvaluationQuestion> questions) {

        List<EvaluationRecord> results = new ArrayList<>();

        for (EvaluationQuestion q : questions) {

            for (String model : models) {

                RagResult result =
                        ragService.ask(q.question(), model);

                results.add(
                        new EvaluationRecord(
                                q.id(),
                                model,
                                result
                        )
                );
            }
        }

        save(results);
    }

    /**
     * Сохраняем JSON для Python + RAGAS
     */
    private void save(List<EvaluationRecord> results) {

        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(
                            new File("evaluation-results.json"),
                            results
                    );

        } catch (Exception e) {
            throw new RuntimeException("Failed to save evaluation results", e);
        }
    }
}