package kg.course.library.service;

import kg.course.library.entity.Book;
import kg.course.library.entity.Reader;
import kg.course.library.entity.Loan;

import kg.course.library.repository.BookRepository;
import kg.course.library.repository.ReaderRepository;
import kg.course.library.repository.LoanRepository;

import kg.course.library.exception.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LibraryService {

    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final LoanRepository loanRepository;

    public LibraryService(
            BookRepository bookRepository,
            ReaderRepository readerRepository,
            LoanRepository loanRepository) {

        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.loanRepository = loanRepository;
    }

    // Получить все книги
    @Transactional(readOnly = true)
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    // Получить всех читателей
    @Transactional(readOnly = true)
    public List<Reader> findAllReaders() {
        return readerRepository.findAll();
    }

    // Получить все выдачи
    @Transactional(readOnly = true)
    public List<Loan> findAllLoans() {
        return loanRepository.findAll();
    }

    // Выдать книгу
    @Transactional
    public Loan issueBook(
            String cardNumber,
            String isbn,
            LocalDate date) {

        // Проверка A. Читатель зарегистрирован
        Reader reader = readerRepository
                .findByCardNumber(cardNumber)
                .orElseThrow(() ->
                        new NotFoundException("Читатель не найден"));

        // Проверка B. Книга существует
        Book book = bookRepository
                .findByIsbn(isbn)
                .orElseThrow(() ->
                        new NotFoundException("Книга не найдена"));

        // Проверка C. Нет просроченных выдач
        boolean overdue = loanRepository
                .existsOverdueLoan(cardNumber, date);

        if (overdue) {
            throw new OverdueException(
                    "У читателя есть просроченные выдачи");
        }

        // Проверка D. Лимит категории читателя
        long activeLoans = loanRepository
                .countActiveLoans(cardNumber);

        if (activeLoans >= reader.getCategory().maxBooks()) {
            throw new LimitExceededException(
                    "Превышен лимит книг на руках");
        }

        // Проверка E. Есть свободный экземпляр
        if (!book.isAvailable()) {
            throw new BookUnavailableException(
                    "Нет свободных экземпляров книги");
        }

        // Создание выдачи
        Loan loan = new Loan();

        loan.setBook(book);
        loan.setReader(reader);
        loan.setIssuedOn(date);
        loan.setDueOn(reader.getCategory().dueDate(date));
        loan.setReturnedOn(null);

        book.setAvailable(false);

        bookRepository.save(book);

        return loanRepository.save(loan);
    }

    // Возврат книги
    @Transactional
    public Loan returnBook(Long id, LocalDate date) {

        Loan loan = loanRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Выдача не найдена"));

        if (loan.getReturnedOn() != null) {
            throw new IllegalStateException(
                    "Книга уже возвращена");
        }

        loan.setReturnedOn(date);
        loan.getBook().setAvailable(true);

        bookRepository.save(loan.getBook());

        return loanRepository.save(loan);
    }

    // Массовая выдача
    @Transactional
    public List<Loan> issueBooksBatch(
            String cardNumber,
            List<String> isbns,
            LocalDate date) {

        List<Loan> result = new ArrayList<>();

        for (String isbn : isbns) {

            Loan loan = issueBook(cardNumber, isbn, date);

            result.add(loan);
        }

        return result;
    }
}