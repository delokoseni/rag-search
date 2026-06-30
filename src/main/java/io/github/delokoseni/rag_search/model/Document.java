package io.github.delokoseni.rag_search.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "document")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя файла, загруженного пользователем.
     */
    @Column(nullable = false)
    private String fileName;

    /**
     * Дата и время загрузки.
     */
    @Column(nullable = false)
    private LocalDateTime uploadDate;

    /**
     * Все текстовые фрагменты документа.
     */
    @OneToMany(
            mappedBy = "document",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<DocumentChunk> chunks = new ArrayList<>();

}