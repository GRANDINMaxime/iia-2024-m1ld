package fr.formation.consumer;

import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import fr.formation.event.CommentaireCreatedEvent;
import fr.formation.model.Commentaire;
import fr.formation.model.Produit;
import fr.formation.repo.CommentaireRepository;
import fr.formation.repo.ProduitRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component("onCommentaireCreated")
@RequiredArgsConstructor
public class CommentaireCreatedConsumer implements Consumer<CommentaireCreatedEvent> {
    private final CommentaireRepository repository;
    private final ProduitRepository produitRepository;

    @Override
    @Transactional
    public void accept(CommentaireCreatedEvent evt) {
        Commentaire commentaire = new Commentaire();
        Produit produit = this.produitRepository.findById(evt.getProduitId()).orElseThrow();

        BeanUtils.copyProperties(evt, commentaire);

        commentaire.setProduit(produit);

        this.repository.save(commentaire);

        List<Commentaire> commentaires = this.repository.findAllByProduitId(evt.getProduitId());

        produit.setNote((int)commentaires.stream().mapToInt(Commentaire::getNote).average().orElse(-1));

        this.produitRepository.save(produit);
    }
}
