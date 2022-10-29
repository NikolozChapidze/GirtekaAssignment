package eu.girteka.assignment.service;

import eu.girteka.assignment.dto.AccountDto;
import eu.girteka.assignment.dto.CardDto;
import eu.girteka.assignment.dto.CustomerDto;
import eu.girteka.assignment.enums.CardType;
import eu.girteka.assignment.enums.CustomerType;
import eu.girteka.assignment.exception.ResourceNotFoundException;
import eu.girteka.assignment.model.Customer;
import eu.girteka.assignment.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Iterable<Customer> getAll() {
        return customerRepository.findAll();
    }

    @Transactional
    public Page<Customer> getPage(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Transactional
    public CustomerDto findById(final long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("customer", id));
        List<AccountDto> accounts = getCustomerAccounts(customer);
        List<CardDto> cards = getCustomerCard(customer);

        return new CustomerDto(customer.getId(), customer.getFullName(), customer.getCustomerType(), cards, accounts);
    }

    @Transactional
    public Customer update(Customer updated) {
        customerRepository.findById(updated.getId())
                .orElseThrow(() -> new ResourceNotFoundException("customer", updated.getId()));
        return customerRepository.save(updated);
    }

    @Transactional
    public Customer create(Customer newDomain) {
        return customerRepository.save(newDomain);
    }

    @Transactional
    public void delete(Long id) {
        customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("customer", id));
        customerRepository.deleteById(id);
    }

    private List<CardDto> getCustomerCard(Customer customer) {
        return customer.getCards()
                .stream()
                .filter(card -> (customer.getCustomerType() != CustomerType.Business || card.getCardType() != CardType.Credit))
                .map(card -> new CardDto(card.getId(),
                        String.format("xxxx xxxx xxxx %s - %s", card.getCardNumber().substring(card.getCardNumber().length() - 4), card.getCardType())))
                .collect(Collectors.toList());
    }

    private List<AccountDto> getCustomerAccounts(Customer customer) {
        return customer.getAccounts()
                .stream()
                .map(account -> new AccountDto(account.getId(),
                        String.format("%s - %s %s", account.getIban(), account.getBalance(), account.getCurrency())))
                .collect(Collectors.toList());
    }
}
