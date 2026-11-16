package kg.course.library;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Library {

    private final Map<Isbn, Book> books = new LinkedHashMap<>();
    private final Map<Isbn, Integer> copies = new LinkedHashMap<>();
    private final Map<String, Reader> readers = new LinkedHashMap<>();
    private final List<Loan> loans = new ArrayList<>();

    private long nextLoanId = 1;

    public void addBook(Book book, int count) {
        if (book == null) {
            throw new IllegalArgumentException("Книга не задана");
        }
        if (count < 1) {
            throw new IllegalArgumentException("Количество должно быть не меньше 1");
        }

        int total = Math.addExact(copies.getOrDefault(book.isbn(), 0), count);
        books.putIfAbsent(book.isbn(), book);
        copies.put(book.isbn(), total);
    }

    public Optional<Book> findBook(Isbn isbn) {
        return Optional.ofNullable(books.get(isbn));
    }

    public Book getBook(Isbn isbn) {
        return findBook(isbn).orElseThrow(
                () -> new NotFoundException("Книга не найдена: " + isbn)
        );
    }

    public int totalCopies(Isbn isbn) {
        getBook(isbn);
        return copies.get(isbn);
    }

    public int availableCopies(Isbn isbn) {
        int active = (int) loans.stream()
                .filter(Loan::isActive)
                .filter(loan -> loan.isbn().equals(isbn))
                .count();

        return totalCopies(isbn) - active;
    }

    public void registerReader(Reader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("Читатель не задан");
        }

        if (readers.containsKey(reader.cardNumber())) {
            throw new IllegalArgumentException("Билет уже зарегистрирован");
        }

        readers.put(reader.cardNumber(), reader);
    }

    public Optional<Reader> findReader(String cardNumber) {
        return Optional.ofNullable(readers.get(cardNumber));
    }

    public Reader getReader(String cardNumber) {
        return findReader(cardNumber).orElseThrow(
                () -> new NotFoundException("Читатель не найден: " + cardNumber)
        );
    }

    public List<Book> findAllBooks() {
        return List.copyOf(books.values());
    }

    public List<Reader> findAllReaders() {
        return List.copyOf(readers.values());
    }

    public List<Loan> findAllLoans() {
        return List.copyOf(loans);
    }

    public List<Loan> activeLoans(String cardNumber) {
        getReader(cardNumber);

        return loans.stream()
                .filter(Loan::isActive)
                .filter(loan -> loan.cardNumber().equals(cardNumber))
                .toList();
    }

    public Loan issueBook(String cardNumber, Isbn isbn, LocalDate date) {
        // a) Читатель зарегистрирован.
        Reader reader = getReader(cardNumber);

        // b) Книга есть в каталоге.
        getBook(isbn);

        if (date == null) {
            throw new IllegalArgumentException("Дата выдачи не задана");
        }

        List<Loan> active = activeLoans(cardNumber);

        // c) Нет просроченных выдач.
        boolean overdue = active.stream()
                .anyMatch(loan -> loan.isOverdue(date));

        if (overdue) {
            throw new OverdueException("У читателя есть просроченные книги");
        }

        // d) Не достигнут лимит.
        if (active.size() >= reader.category().maxBooks()) {
            throw new LimitExceededException("Достигнут лимит книг");
        }

        // e) Есть свободный экземпляр.
        if (availableCopies(isbn) < 1) {
            throw new BookUnavailableException("Нет свободных экземпляров");
        }

        Loan loan = new Loan(
                nextLoanId,
                isbn,
                cardNumber,
                date,
                reader.category().dueDate(date),
                null
        );

        loans.add(loan);
        nextLoanId++;
        return loan;
    }

    public Loan returnBook(long loanId, LocalDate date) {
        for (int i = 0; i < loans.size(); i++) {
            Loan loan = loans.get(i);

            if (loan.id() == loanId) {
                Loan returned = loan.returned(date);
                loans.set(i, returned);
                return returned;
            }
        }

        throw new NotFoundException("Выдача не найдена: " + loanId);
    }

    public void removeBook(Isbn isbn) {
        Book book = getBook(isbn);

        boolean hasActiveLoans = loans.stream()
                .anyMatch(loan ->
                        loan.isbn().equals(isbn) && loan.isActive()
                );

        if (hasActiveLoans) {
            throw new IllegalStateException(
                    "Нельзя удалить книгу: есть активные выдачи"
            );
        }

        books.remove(isbn);
        copies.remove(isbn);
    }
    public Book updateBook(
            Isbn isbn,
            String title,
            List<String> authors,
            Integer year) {

        Book oldBook = getBook(isbn);

        Book updatedBook = new Book(
                oldBook.isbn(),
                title != null ? title : oldBook.title(),
                authors != null ? authors : oldBook.authors(),
                year != null ? year : oldBook.year()
        );

        books.put(isbn, updatedBook);

        return updatedBook;
    }
}