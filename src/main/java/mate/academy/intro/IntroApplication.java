package mate.academy.intro;

import java.math.BigDecimal;
import mate.academy.intro.model.Book;
import mate.academy.intro.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IntroApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntroApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(BookService bookService) {
        return args -> {
            Book book = new Book();
            book.setTitle("The Hobbit");
            book.setAuthor("J.R.R. Tolkien");
            book.setIsbn("978-0-395-00000-0");
            book.setPrice(new BigDecimal("12.99"));
            bookService.save(book);
            System.out.println("Books saved in database: " + bookService.findAll());
        };
    }

}
