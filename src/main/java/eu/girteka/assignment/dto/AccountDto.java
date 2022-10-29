package eu.girteka.assignment.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {
    private long id;
    private String value;
}
