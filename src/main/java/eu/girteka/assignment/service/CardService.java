package eu.girteka.assignment.service;

import eu.girteka.assignment.exception.ResourceNotFoundException;
import eu.girteka.assignment.model.Card;
import eu.girteka.assignment.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CardService {
    private final CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Transactional
    public Iterable<Card> getAll() {
        return cardRepository.findAll();
    }

    @Transactional
    public Card findById(final long id) {
        return cardRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Card", id));
    }

    @Transactional
    public Card update(Card updated) {
        cardRepository.findById(updated.getId()).orElseThrow(() -> new ResourceNotFoundException("Card", updated.getId()));
        return cardRepository.save(updated);
    }

    @Transactional
    public Card create(Card newDomain) {
        return cardRepository.save(newDomain);
    }

    @Transactional
    public void delete(Long id) {
        cardRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Card", id));
        cardRepository.deleteById(id);
    }
}
