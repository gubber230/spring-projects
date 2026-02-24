INSERT INTO books (id, title, author, isbn, price, is_deleted)
VALUES (1, 'Book1', 'Author1', '1', 10.00, 0);

INSERT INTO books (id, title, author, isbn, price, is_deleted)
VALUES (2, 'Book2', 'Author2', '2', 20.00, 0);

INSERT INTO books (id, title, author, isbn, price, is_deleted)
VALUES (3, 'Book3', 'Author3', '3', 30.00, 0);

INSERT INTO books_categories (books_id, categories_id) VALUES (1, 1);
INSERT INTO books_categories (books_id, categories_id) VALUES (2, 2);
INSERT INTO books_categories (books_id, categories_id) VALUES (3, 2);