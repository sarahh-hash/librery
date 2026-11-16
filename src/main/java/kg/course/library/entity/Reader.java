package kg.course.library.entity;

import jakarta.persistence.*;
import kg.course.library.ReaderCategory;

@Entity
@Table(name = "readers")
public class Reader {
    @Id
    @Column(name = "card_number", length = 7)
    private String cardNumber;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReaderCategory category;

    protected Reader() { }

    public Reader(String cardNumber, String fullName, ReaderCategory category) {
        if (cardNumber == null || !cardNumber.matches("R-\\d{5}"))
            throw new IllegalArgumentException("Номер билета должен иметь вид R-00001");
        if (fullName == null || fullName.isBlank())
            throw new IllegalArgumentException("ФИО не может быть пустым");
        if (category == null) throw new IllegalArgumentException("Категория не задана");
        this.cardNumber = cardNumber;
        this.fullName = fullName.trim();
        this.category = category;
    }

    public String getCardNumber() { return cardNumber; }
    public String getFullName() { return fullName; }
    public ReaderCategory getCategory() { return category; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reader other)) return false;
        return getCardNumber() != null && getCardNumber().equals(other.getCardNumber());
    }
    @Override public int hashCode() { return Reader.class.hashCode(); }
    @Override public String toString() { return "Reader[" + cardNumber + ", " + fullName + "]"; }
}