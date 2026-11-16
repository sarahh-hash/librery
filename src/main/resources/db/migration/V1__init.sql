create table books (
                       isbn varchar(13) primary key,
                       title varchar(255) not null check (length(trim(title)) > 0),
                       year int not null check (year >= 1450),
  copies int not null check (copies >= 1)
);
create table authors (
                         id bigint generated always as identity primary key,
                         name varchar(255) not null unique
);
create table book_authors (
                              book_isbn varchar(13) not null references books(isbn),
                              author_id bigint not null references authors(id),
                              primary key (book_isbn, author_id)
);
create table readers (
                         card_number varchar(20) primary key,
                         full_name varchar(255) not null,
                         category varchar(10) not null check (category in ('STUDENT','TEACHER','GUEST'))
);
create table loans (
                       id bigint generated always as identity primary key,
                       book_isbn varchar(13) not null references books(isbn),
                       card_number varchar(20) not null references readers(card_number),
                       issued_on date not null,
                       due_on date not null,
                       returned_on date,
                       check (due_on >= issued_on),
                       check (returned_on is null or returned_on >= issued_on)
);
create index idx_loans_card_number on loans(card_number);
create index idx_loans_book_isbn on loans(book_isbn);