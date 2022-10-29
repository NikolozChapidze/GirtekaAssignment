package eu.girteka.assignment.dto;

import eu.girteka.assignment.enums.CustomerType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {
    private Long id;
    private String fullName;
    private CustomerType customerType;
    private List<CardDto> cards = new ArrayList<>();
    private List<AccountDto> accounts = new ArrayList<>();

}
