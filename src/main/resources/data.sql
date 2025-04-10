INSERT INTO users (id, username, email, password) VALUES (1001, 'user1', 'user1@email.com', '$2a$12$GZPXcqkNmk7bvjXks2fk4e8cDnDsXdgTHDSyPQoUXVc0U0aupqQm6'),
                                                          (1002, 'admin', 'admin@email.com', '$2a$12$JnhYjHkKU/5Ll4n/8CKk4ugwHirne2uIs2SCo547Q2Z43I5UBYxVu'),
                                                          (1003, 'user2', 'user2@email.com', '$2a$12$nhGbuEAvUYXJm/iG3iju.uouucsd5U5cwN9/cYOw03eMXEfeyfl8y');

INSERT INTO profiles (id, firstname, lastname, about, user_id) VALUES (1001, 'John', 'Doe', 'Love reading and books', 1001);

INSERT INTO authorities (authority) VALUES ('USER'),('ADMIN');

INSERT INTO user_authorities (user_id, authority_id) VALUES (1001, 'USER'),
                                                            (1002, 'ADMIN'),
                                                            (1003, 'USER');