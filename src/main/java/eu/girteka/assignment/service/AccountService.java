package eu.girteka.assignment.service;

import eu.girteka.assignment.exception.ResourceNotFoundException;
import eu.girteka.assignment.model.Account;
import eu.girteka.assignment.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Iterable<Account> getAll() {
        return accountRepository.findAll();
    }

    @Transactional
    public Account findById(final long id) {
        return accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account", id));
    }

    @Transactional
    public Account update(Account updated) {
        accountRepository.findById(updated.getId()).orElseThrow(() -> new ResourceNotFoundException("Account", updated.getId()));
        return accountRepository.save(updated);
    }

    @Transactional
    public Account create(Account newDomain) {
        return accountRepository.save(newDomain);
    }

    @Transactional
    public void delete(Long id) {
        accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account", id));
        accountRepository.deleteById(id);
    }


}
