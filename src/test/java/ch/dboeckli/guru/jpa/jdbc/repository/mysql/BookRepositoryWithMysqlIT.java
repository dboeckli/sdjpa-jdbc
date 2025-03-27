package ch.dboeckli.guru.jpa.jdbc.repository.mysql;

import ch.dboeckli.guru.jpa.jdbc.domain.Book;
import ch.dboeckli.guru.jpa.jdbc.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test_mysql")
@DirtiesContext
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  // to assure that it is not replaced with h2
class BookRepositoryWithMysqlIT {

    @Autowired
    BookRepository bookRepository;

    @Test
    void testJpaTestSplice() {
        long countBefore = bookRepository.count();

        bookRepository.save(new Book("My Book", "1235555", "Self", null));

        long countAfter = bookRepository.count();

        assertEquals(2, countBefore);
        assertThat(countBefore).isLessThan(countAfter);
    }

}