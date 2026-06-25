/**
 * Слой доступа к данным (Repository).
 *
 * Отвечает за взаимодействие с базой данных PostgreSQL:
 * - сохранение документов и их фрагментов
 * - хранение векторных представлений (embeddings)
 * - выполнение поисковых запросов через pgvector
 *
 * Использует Spring Data JPA и SQL-расширения для работы с векторами.
 */
package io.github.delokoseni.rag_search.repository;