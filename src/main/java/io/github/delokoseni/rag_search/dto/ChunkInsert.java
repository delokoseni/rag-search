package io.github.delokoseni.rag_search.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChunkInsert {

    private Long documentId;

    private Integer chunkIndex;

    private String content;

    private String embedding;

}