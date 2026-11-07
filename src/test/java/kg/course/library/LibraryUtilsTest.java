package kg.course.library;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LibraryUtilsTest {

    @Test
    void averageReturnsCorrectResult() {
        assertEquals(2.5, LibraryUtils.average(new int[]{1, 2, 3, 4}));
    }

    @Test
    void averageRejectsEmptyAndNull() {
        assertThrows(IllegalArgumentException.class,
                () -> LibraryUtils.average(new int[]{}));

        assertThrows(IllegalArgumentException.class,
                () -> LibraryUtils.average(null));
    }

    @Test
    void averageHandlesLargeValues() {
        assertEquals((double) Integer.MAX_VALUE,
                LibraryUtils.average(
                        new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE}
                ));
    }

    @Test
    void maxReturnsCorrectResult() {
        assertEquals(-2, LibraryUtils.max(new int[]{-8, -2, -10}));
    }

    @Test
    void maxRejectsEmptyAndNull() {
        assertThrows(IllegalArgumentException.class,
                () -> LibraryUtils.max(new int[]{}));

        assertThrows(IllegalArgumentException.class,
                () -> LibraryUtils.max(null));
    }

    @Test
    void sortedCopySortsWithoutChangingOriginal() {
        int[] original = {3, -1, 3, 0};
        int[] result = LibraryUtils.sortedCopy(original);

        assertArrayEquals(new int[]{-1, 0, 3, 3}, result);
        assertArrayEquals(new int[]{3, -1, 3, 0}, original);
        assertNotSame(original, result);
    }

    @Test
    void sortedCopyAcceptsEmptyArrayAndRejectsNull() {
        assertArrayEquals(new int[]{},
                LibraryUtils.sortedCopy(new int[]{}));

        assertThrows(IllegalArgumentException.class,
                () -> LibraryUtils.sortedCopy(null));
    }

    @Test
    void normalizePhoneAcceptsAllExamples() {
        assertEquals("+996555123456",
                LibraryUtils.normalizePhone("0555 123 456"));

        assertEquals("+996555123456",
                LibraryUtils.normalizePhone("+996 (555) 123-456"));

        assertEquals("+996555123456",
                LibraryUtils.normalizePhone("996555123456"));
    }

    @Test
    void normalizePhoneRejectsInvalidNumbers() {
        String[] invalid = {
                "", "abc", "12345", "995555123456", "9965551234567"
        };

        for (String phone : invalid) {
            assertThrows(IllegalArgumentException.class,
                    () -> LibraryUtils.normalizePhone(phone));
        }

        assertThrows(IllegalArgumentException.class,
                () -> LibraryUtils.normalizePhone(null));
    }
}