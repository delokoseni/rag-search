package io.github.delokoseni.rag_search.evaluation;

public record EvaluationQuestion(
        String id,
        String question,
        String groundTruth
) {
}