package eu.girteka.assignment.model;

import eu.girteka.assignment.enums.CardType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "card")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {
    @Id
    @Column(name = "id")
    private long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
//    @JsonBackReference
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private CardType cardType;

    @Column(name = "card_number", length = 20)
    private String cardNumber;

    @Column(name = "expiry")
    private LocalDateTime expiry;
}
