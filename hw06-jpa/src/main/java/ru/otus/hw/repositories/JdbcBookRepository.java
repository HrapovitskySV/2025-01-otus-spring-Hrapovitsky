package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Book> findById(long id) {
        EntityGraph<?> entityGraph = em.getEntityGraph("genres-author-entity-graph");
        var query = em.createQuery("select b from Book b where b.id = :book_id", Book.class);
        query.setHint(FETCH.getKey(),entityGraph);

        query.setParameter("book_id",id);
        var r = query.getResultList();
        if (r.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(r.get(0));
        }
    }

    @Override
    public List<Book> findAll() {
        EntityGraph<?> entityGraph = em.getEntityGraph("author-entity-graph");
        var query = em.createQuery("select b from Book b", Book.class);
        query.setHint(FETCH.getKey(),entityGraph);
        return query.getResultList();

    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        Optional<Book> comment = findById(id);
        comment.ifPresent(value -> em.remove(value));
    }

    private Book insert(Book book) {
        em.persist(book);
        return book;
    }

    private Book update(Book book) {
        em.merge(book);
        return book;
    }
}

