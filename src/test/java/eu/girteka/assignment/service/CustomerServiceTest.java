package eu.girteka.assignment.service;

import eu.girteka.assignment.dto.AccountDto;
import eu.girteka.assignment.dto.CardDto;
import eu.girteka.assignment.dto.CustomerDto;
import eu.girteka.assignment.enums.CardType;
import eu.girteka.assignment.enums.CustomerType;
import eu.girteka.assignment.exception.ResourceNotFoundException;
import eu.girteka.assignment.model.Account;
import eu.girteka.assignment.model.Card;
import eu.girteka.assignment.model.Customer;
import eu.girteka.assignment.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class CustomerServiceTest {
    Customer customer0;
    Account account0;
    Card card0;
    Account account1;
    Card card1;


    @InjectMocks
    CustomerService customerService;

    @Mock
    CustomerRepository customerRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        customer0 = Customer.builder().id(0L).fullName("John Smith").customerType(CustomerType.Personal)
                .cards(new ArrayList<>())
                .accounts(new ArrayList<>()).build();

        account0 = Account.builder().id(0L).customer(customer0).iban("DK9520000123456789")
                .currency("DKK").balance(1258.34).build();
        card0 = Card.builder().id(0L).customer(customer0).cardType(CardType.Debit).cardNumber("5236 5484 2365 4125")
                .expiry(LocalDateTime.parse("2020-10-05 14:01:10", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        account1 = Account.builder().id(1L).customer(customer0).iban("LT601010012345678901")
                .currency("EUR").balance(516.25).build();
        card1 = Card.builder().id(1L).customer(customer0).cardType(CardType.Credit).cardNumber("8542 8974 2315 3254")
                .expiry(LocalDateTime.parse("2021-02-24 13:05:26", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        customer0.getAccounts().add(account0);
        customer0.getCards().add(card0);
    }

    @Test
    void findByIdSizeTest() {
        Mockito.when(customerRepository.findById(0L)).thenReturn(Optional.of(customer0));
        CustomerDto customerDto = customerService.findById(0L);
        Assert.notNull(customerDto, "Customer is null");

        assertEquals(1, customerDto.getAccounts().size());
        assertEquals(1, customerDto.getCards().size());

        customer0.getAccounts().add(account1);
        customerDto = customerService.findById(0L);
        assertEquals(2, customerDto.getAccounts().size());

        Mockito.verify(customerRepository, Mockito.times(2)).findById(Mockito.anyLong());
    }

    @Test
    void findByIdPersonalTypeTest() {
        Mockito.when(customerRepository.findById(0L)).thenReturn(Optional.of(customer0));
        customer0.getCards().add(card1);
        customer0.getAccounts().add(account1);
        CustomerDto customerDto = customerService.findById(0L);

        //Generate expected customer dto
        CustomerDto expected = CustomerDto.builder()
                .id(0L).fullName("John Smith").customerType(CustomerType.Personal)
                .cards(Arrays.asList(CardDto.builder().id(0L).value("xxxx xxxx xxxx 4125 - Debit").build(),
                        CardDto.builder().id(1L).value("xxxx xxxx xxxx 3254 - Credit").build()))
                .accounts(Arrays.asList(AccountDto.builder().id(0L).value("DK9520000123456789 - 1258.34 DKK").build(),
                        AccountDto.builder().id(1L).value("LT601010012345678901 - 516.25 EUR").build()))
                .build();

        assertEquals(expected, customerDto);
    }

    @Test
    void findByIdBusinessTypeTest() {
        Mockito.when(customerRepository.findById(0L)).thenReturn(Optional.of(customer0));
        //change customer 0 type to business
        customer0.setCustomerType(CustomerType.Business);

        customer0.getCards().add(card1);
        customer0.getAccounts().add(account1);

        CustomerDto customerDto = customerService.findById(0L);

        //Generate expected customer dto
        CustomerDto expected = CustomerDto.builder()
                .id(0L).fullName("John Smith").customerType(CustomerType.Business)
                .cards(new ArrayList<>(Collections.singletonList(CardDto.builder().id(0L).value("xxxx xxxx xxxx 4125 - Debit").build())))
                .accounts(Arrays.asList(AccountDto.builder().id(0L).value("DK9520000123456789 - 1258.34 DKK").build(),
                        AccountDto.builder().id(1L).value("LT601010012345678901 - 516.25 EUR").build()))
                .build();

        assertEquals(expected, customerDto);

        expected.getCards().add(CardDto.builder().id(1L).value("xxxx xxxx xxxx 3254 - Credit").build());
        assertNotEquals(expected, customerDto);
    }

    @Test
    void findById_ResourceNotFoundException() {
        Mockito.when(customerRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            customerService.findById(1L);
        });
    }
}