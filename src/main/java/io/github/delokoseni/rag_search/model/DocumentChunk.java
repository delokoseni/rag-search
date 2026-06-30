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

    /**
     * Документ, которому принадлежит данный фрагмент.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    /**
     * Порядковый номер чанка внутри документа.
     */
    @Column(nullable = false)
    private Integer chunkIndex;

    /**
     * Текст чанка.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    /**
     * Векторное представление текста.
     * nomic-embed-text -> 768 измерений.
     */
    @Column(columnDefinition = "vector(768)", nullable = false)
    private String embedding;

}