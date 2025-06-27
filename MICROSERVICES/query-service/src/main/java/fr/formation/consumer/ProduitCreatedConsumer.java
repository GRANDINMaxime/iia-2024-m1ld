package fr.formation.consumer;

import java.util.function.Consumer;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import fr.formation.event.ProduitCreatedEvent;
import fr.formation.model.Produit;
import fr.formation.repo.ProduitRepository;
import lombok.RequiredArgsConstructor;

@Component("onProduitCreated")
@RequiredArgsConstructor
public class ProduitCreatedConsumer implements Consumer<ProduitCreatedEvent> {
    private final ProduitRepository repository;

    @Override
    public void accept(ProduitCreatedEvent evt) {
        Produit produit = new Produit();

        BeanUtils.copyProperties(evt, produit);

        this.repository.save(produit);
    }
}
