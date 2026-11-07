package kg.course.library;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReaderCardTest {

    @Test
    void takeAllowsBooksUpToLimit() {
        ReaderCard card = new ReaderCard("R-00001", "Сара Уланова", 3);

        assertEquals(0, card.getItemsOnHand());

        card.take();
        card.take();
        card.take();

        assertEquals(3, card.getItemsOnHand());
    }

    @Test
    void takeRejectsExceedingLimit() {
        ReaderCard card = new ReaderCard("R-00001", "Сара Уланова", 1);
        card.take();

        assertThrows(IllegalStateException.class, card::take);
        assertEquals(1, card.getItemsOnHand());
    }

    @Test
    void giveBackDecreasesBookCount() {
        ReaderCard card = new ReaderCard("R-00001", "Сара Уланова", 2);
        card.take();
        card.giveBack();

        assertEquals(0, card.getItemsOnHand());

        // После возврата книгу можно выдать снова.
        card.take();
        assertEquals(1, card.getItemsOnHand());
    }

    @Test
    void giveBackRejectsZeroBooks() {
        ReaderCard card = new ReaderCard("R-00001", "Сара Уланова", 2);

        assertThrows(IllegalStateException.class, card::giveBack);
        assertEquals(0, card.getItemsOnHand());
    }

    @Test
    void constructorRejectsInvalidData() {
        assertThrows(IllegalArgumentException.class,
                () -> new ReaderCard("R-1234", "Сара", 3));

        assertThrows(IllegalArgumentException.class,
                () -> new ReaderCard("R-12A45", "Сара", 3));

        assertThrows(IllegalArgumentException.class,
                () -> new ReaderCard("R-00001", " ", 3));

        assertThrows(IllegalArgumentException.class,
                () -> new ReaderCard("R-00001", "Сара", 0));
    }

    @Test
    void cardsWithSameNumberAreEqual() {
        ReaderCard first = new ReaderCard("R-00001", "Сара", 3);
        ReaderCard second = new ReaderCard("R-00001", "Алина", 5);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertNotSame(first, second);
    }
}