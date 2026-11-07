package kg.course.library;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainLab1 {

    public static void main(String[] args) {

        System.out.println("=== Задание 1. Базовые конструкции ===");

        int[] values = {8, 3, 10, 1, 5};

        System.out.println("Среднее: " + LibraryUtils.average(values));
        System.out.println("Максимум: " + LibraryUtils.max(values));
        int[] sorted = LibraryUtils.sortedCopy(values);

        System.out.print("Отсортированная копия: ");
        for (int value : sorted) {
            System.out.print(value + " ");
        }

        System.out.print("\nИсходный массив: ");
        for (int value : values) {
            System.out.print(value + " ");
        }
        System.out.println();

        String[] phones = {
                "0555 123 456",
                "+996 (555) 123-456",
                "996555123456"
        };

        for (String phone : phones) {
            System.out.println(LibraryUtils.normalizePhone(phone));
        }

        System.out.println("\n=== Задание 2. Инкапсуляция ===");

        ReaderCard card = new ReaderCard("R-00001", "Уланова Сара", 2);

        System.out.println(card);

        card.take();
        card.take();
        System.out.println("После выдачи двух книг: " + card);

        try {
            card.take();
        } catch (IllegalStateException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        card.giveBack();
        System.out.println("После возврата одной книги: " + card);

        card.giveBack();

        try {
            card.giveBack();
        } catch (IllegalStateException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        System.out.println("\n=== Задания 3 и 4. Наследование и полиморфизм ===");

        Item[] items = {
                new PaperBook("Джамиля", 1958, "Чингиз Айтматов", 120),
                new PaperBook("Война и мир", 1869, "Лев Толстой", 1300),
                new Magazine("Наука и жизнь", 2024, 5)
        };

        int bookCount = 0;

        for (Item item : items) {
            System.out.println(item.describe());

            if (item instanceof PaperBook) {
                bookCount++;
            }
        }

        System.out.println("Количество книг: " + bookCount);

        Describable describable = items[0];
        System.out.println("Через интерфейс: " + describable.describe());

        System.out.println("\n=== Задание 5. Сравнение объектов ===");

        ReaderCard first = new ReaderCard("R-00002", "Сара Уланова", 3);
        ReaderCard second = new ReaderCard("R-00002", "Алина Иванова", 5);

        System.out.println("equals(): " + first.equals(second));
        System.out.println("==: " + (first == second));

        HashSet<ReaderCard> cards = new HashSet<>();
        cards.add(first);
        cards.add(second);

        System.out.println("Размер HashSet: " + cards.size());

        System.out.println("\n=== Задание 6. Перечисления и записи ===");

        LocalDate issuedOn = LocalDate.of(2026, 10, 10);

        for (ReaderCategory category : ReaderCategory.values()) {
            System.out.println(
                    category + ": срок — " + category.getLoanDays()
                            + " дн., лимит — " + category.maxBooks()
                            + ", дата возврата — " + category.dueDate(issuedOn)
            );
        }

        Isbn isbn1 = new Isbn("978-5-4461-0914-9");
        Isbn isbn2 = new Isbn("9785446109149");

        System.out.println("ISBN равны: " + isbn1.equals(isbn2));

        List<String> authors = new ArrayList<>();
        authors.add("Иван Петров");

        Book book = new Book(isbn1, "Основы Java", authors, 2024);
        System.out.println(book);

        authors.add("Анна Сидорова");
        System.out.println("Авторы в исходном списке: " + authors);
        System.out.println("Авторы в Book: " + book.authors());

        try {
            book.authors().add("Другой автор");
        } catch (UnsupportedOperationException e) {
            System.out.println("Список авторов в Book нельзя изменить");
        }
    }
}