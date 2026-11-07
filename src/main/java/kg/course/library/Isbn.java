package kg.course.library;

public record Isbn(String value) {

    public Isbn {
        if (value == null) {
            throw new IllegalArgumentException("ISBN не задан");
        }

        StringBuilder cleaned = new StringBuilder();

        for (int i = 0; i < value.length(); i++) {
            char symbol = value.charAt(i);

            if (Character.isWhitespace(symbol) || symbol == '-') {
                continue;
            }

            if (symbol == 'x') {
                symbol = 'X';
            }

            cleaned.append(symbol);
        }

        value = cleaned.toString();

        if (value.length() != 10 && value.length() != 13) {
            throw new IllegalArgumentException(
                    "ISBN должен содержать 10 или 13 символов"
            );
        }

        for (int i = 0; i < value.length(); i++) {
            char symbol = value.charAt(i);
            boolean digit = symbol >= '0' && symbol <= '9';
            boolean lastX = value.length() == 10
                    && i == 9 && symbol == 'X';

            if (!digit && !lastX) {
                throw new IllegalArgumentException("Неверный формат ISBN");
            }
        }
    }

}
