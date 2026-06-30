package io.github.delokoseni.rag_search.repository;

import io.github.delokoseni.rag_search.model.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentChunkRepository
        extends JpaRepository<DocumentChunk, Long> {
}