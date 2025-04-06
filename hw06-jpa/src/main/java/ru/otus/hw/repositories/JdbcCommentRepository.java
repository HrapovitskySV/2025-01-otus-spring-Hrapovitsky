package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;
import java.util.Optional;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcCommentRepository implements CommentRepository {

    @PersistenceContext
    private final EntityManager em;


    @Override
    public Optional<Comment> findById(long id) {
        var c = em.find(Comment.class, id);
        return Optional.ofNullable(c);
    }

    @Override
    public List<Comment> findByBookId(long bookId) {
        var query = em.createQuery("select c from Comment c where c.book.id = :book_id", Comment.class);
        query.setParameter("book_id",bookId);
        return query.getResultList();
    }


    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == 0) {
            em.persist(comment);
            return comment;
        }
        return em.merge(comment);
    }


    @Override
    public void deleteById(long id) {
        Optional<Comment> comment = findById(id);
        comment.ifPresent(value -> em.remove(value));
    }
}

