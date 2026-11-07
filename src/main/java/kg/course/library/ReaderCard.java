package kg.course.library;

public class ReaderCard {

    private final String number;
    private final String fullName;
    private final int maxItems;
    private int itemsOnHand;

    public ReaderCard(String number, String fullName, int maxItems) {
        if (number == null || number.length() != 7
                || !number.startsWith("R-")) {
            throw new IllegalArgumentException("Номер должен иметь формат R-00000");
        }

        for (int i = 2; i < number.length(); i++) {
            char c = number.charAt(i);
            if (c < '0' || c > '9') {
                throw new IllegalArgumentException("Номер должен иметь формат R-00000");
            }
        }

        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("ФИО не должно быть пустым");
        }

        if (maxItems < 1) {
            throw new IllegalArgumentException("Лимит должен быть не меньше 1");
        }

        this.number = number;
        this.fullName = fullName.strip();
        this.maxItems = maxItems;
        this.itemsOnHand = 0;
    }

    public void take() {
        if (itemsOnHand >= maxItems) {
            throw new IllegalStateException("Достигнут лимит выдачи книг");
        }
        itemsOnHand++;
    }

    public void giveBack() {
        if (itemsOnHand == 0) {
            throw new IllegalStateException("Нет книг для возврата");
        }
        itemsOnHand--;
    }

    public String getNumber() {
        return number;
    }

    public String getFullName() {
        return fullName;
    }

    public int getMaxItems() {
        return maxItems;
    }

    public int getItemsOnHand() {
        return itemsOnHand;
    }

    @Override
    public String toString() {
        return "Билет: " + number
                + ", читатель: " + fullName
                + ", книг на руках: " + itemsOnHand
                + ", лимит: " + maxItems;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        ReaderCard other = (ReaderCard) object;
        return number.equals(other.number);
    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }
}