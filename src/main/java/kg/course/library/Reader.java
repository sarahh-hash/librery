package kg.course.library;

    public record Reader(
            String cardNumber,
            String fullName,
            ReaderCategory category
    ) {
        public Reader {
            if (cardNumber == null || cardNumber.length() != 7
                    || !cardNumber.startsWith("R-")) {
                throw new IllegalArgumentException("Номер билета: R-00000");
            }

            for (int i = 2; i < cardNumber.length(); i++) {
                char c = cardNumber.charAt(i);
                if (c < '0' || c > '9') {
                    throw new IllegalArgumentException("Номер билета: R-00000");
                }
            }

            if (fullName == null || fullName.isBlank()) {
                throw new IllegalArgumentException("ФИО не должно быть пустым");
            }

            if (category == null) {
                throw new IllegalArgumentException("Категория не задана");
            }

            fullName = fullName.strip();
        }
    }

