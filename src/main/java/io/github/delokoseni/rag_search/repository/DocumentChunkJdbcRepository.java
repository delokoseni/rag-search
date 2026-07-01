package io.github.delokoseni.rag_search.repository;

import io.github.delokoseni.rag_search.dto.ChunkInsert;
import io.github.delokoseni.rag_search.dto.RetrievedChunk;
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

    public List<RetrievedChunk> findSimilarChunks(
            String embedding,
            int limit
    ) {

        double maxDistance = 0.35;

        String sql = """
        SELECT *
        FROM (
            SELECT
                d.id,
                d.file_name,
                dc.chunk_index,
                dc.content,
                dc.embedding <=> CAST(? AS vector) AS distance
            FROM document_chunk dc
            JOIN document d
                ON d.id = dc.document_id
        ) result
        WHERE result.distance <= ?
        ORDER BY result.distance
        LIMIT ?
        """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) ->
                        new RetrievedChunk(
                                rs.getLong("id"),
                                rs.getString("file_name"),
                                rs.getInt("chunk_index"),
                                rs.getString("content"),
                                rs.getDouble("distance")
                        ),
                embedding,
                maxDistance,
                limit
        );
    }

}