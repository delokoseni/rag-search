package io.github.delokoseni.rag_search.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DocumentChunkJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public void insert(
            Long documentId,
            Integer chunkIndex,
            String content,
            String embedding
    ) {

        jdbcTemplate.update("""
                INSERT INTO document_chunk
                (
                    document_id,
                    chunk_index,
                    content,
                    embedding
                )
                VALUES
                (
                    ?,
                    ?,
                    ?,
                    CAST(? AS vector)
                )
                """,
                documentId,
                chunkIndex,
                content,
                embedding
        );
    }

}