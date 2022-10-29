package eu.girteka.assignment.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    @Column(name = "id")
    private long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
//    @JsonBackReference
    private Customer customer;

    @Column(name = "iban", length = 30)
    private String iban;

    @Column(name = "currency", length = 3)
    private String currency;

    @Column(name = "balance", columnDefinition = "Numeric (19,2)")
    private double balance;
}
