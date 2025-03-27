package ch.dboeckli.guru.jpa.jdbc.dao;

import ch.dboeckli.guru.jpa.jdbc.domain.Author;

public interface AuthorDao {

    Author getById(Long id);

    Author createAuthor(Author author);

}
