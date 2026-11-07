package kg.course.library;

public class LibraryUtils {

    private static void checkArray(int[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Массив не должен быть пустым");
        }
    }

    public static double average(int[] values) {
        checkArray(values);

        long sum = 0;
        for (int value : values) {
            sum += value;
        }

        return (double) sum / values.length;
    }

    public static int max(int[] values) {
        checkArray(values);

        int maximum = values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i] > maximum) {
                maximum = values[i];
            }
        }

        return maximum;    }

    public static int[] sortedCopy(int[] values) {
        if (values == null) {
            throw new IllegalArgumentException("Массив не должен быть null");
        }

        int[] copy = new int[values.length];

        for (int i = 0; i < values.length; i++) {
            copy[i] = values[i];
        }

        // Сортировка вставками
        for (int i = 1; i < copy.length; i++) {
            int current = copy[i];
            int j = i - 1;

            while (j >= 0 && copy[j] > current) {
                copy[j + 1] = copy[j];
                j--;
            }

            copy[j + 1] = current;
        }

        return copy;
    }

    public static String normalizePhone(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("Номер телефона не задан");
        }

        StringBuilder digits = new StringBuilder();

        for (int i = 0; i < raw.length(); i++) {
            char symbol = raw.charAt(i);

            if (symbol >= '0' && symbol <= '9') {
                digits.append(symbol);
            }
        }

        String phone = digits.toString();

        if (phone.length() == 10 && phone.charAt(0) == '0') {
            phone = "996" + phone.substring(1);
        }

        if (phone.length() != 12 || !phone.startsWith("996")) {
            throw new IllegalArgumentException("Неверный номер телефона");
        }

        return "+" + phone;
    }
}
