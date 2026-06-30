package io.github.delokoseni.rag_search.repository;

import io.github.delokoseni.rag_search.dto.ChunkInsert;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DocumentChunkJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public void batchInsert(List<ChunkInsert> chunks) {

        jdbcTemplate.batchUpdate("""
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
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(
                            PreparedStatement ps,
                            int i
                    ) throws SQLException {

                        ChunkInsert chunk = chunks.get(i);

                        ps.setLong(1, chunk.getDocumentId());
                        ps.setInt(2, chunk.getChunkIndex());
                        ps.setString(3, chunk.getContent());
                        ps.setString(4, chunk.getEmbedding());

                    }

                    @Override
                    public int getBatchSize() {
                        return chunks.size();
                    }

                });

    }

}