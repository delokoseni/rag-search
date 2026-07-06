package io.github.delokoseni.rag_search.evaluation;

import io.github.delokoseni.rag_search.dto.RagResult;

public record EvaluationRecord(

        String questionId,
        String model,
        RagResult result

) {
}