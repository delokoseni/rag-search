package io.github.delokoseni.rag_search.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RetrievedChunk {

    private Long documentId;

    private String fileName;

    private Integer chunkIndex;

    private String content;

    private Double distance;

}