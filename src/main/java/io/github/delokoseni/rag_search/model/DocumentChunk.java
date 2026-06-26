package io.github.delokoseni.rag_search.model;

import com.pgvector.PGvector;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "document_chunk")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentChunk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    /**
     * Вектор embedding (768-dim для nomic-embed-text)
     */
    @Column(columnDefinition = "vector(768)")
    private PGvector embedding;
}