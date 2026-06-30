package io.github.delokoseni.rag_search.repository;

import io.github.delokoseni.rag_search.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository
        extends JpaRepository<Document, Long> {
}