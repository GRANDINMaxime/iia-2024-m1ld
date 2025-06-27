package fr.formation.broker.consumer;

import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import fr.formation.broker.command.RejectCommentaireCommand;
import fr.formation.broker.command.ValidateCommentaireCommand;
import fr.formation.broker.event.CommentaireCreatedEvent;
import fr.formation.model.Produit;
import fr.formation.repo.ProduitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component("onCommentaireCreated")
@RequiredArgsConstructor
@Log4j2
public class CommentaireCreatedConsumer implements Consumer<CommentaireCreatedEvent> {
    private final ProduitRepository repository;
    private final StreamBridge streamBridge;

    @Override
    public void accept(CommentaireCreatedEvent evt) {
        log.debug("Commentaire {} for product {} created! Checking product...", evt.getCommentaireId(), evt.getProduitId());
        
        Optional<Produit> optProduit = this.repository.findById(evt.getProduitId());
        
        if (optProduit.isPresent() && optProduit.get().isNotable()) {
            log.debug("{} is notable!", evt.getProduitId());
            
            this.streamBridge.send("onCommentaireCreated-out-0", ValidateCommentaireCommand.builder()
                .commentaireId(evt.getCommentaireId())
                .produitId(evt.getProduitId())
                .build()
            );
        }
        
        else {
            log.debug("{} is not notable!", evt.getProduitId());

            this.streamBridge.send("onCommentaireCreated-out-1", RejectCommentaireCommand.builder()
                .commentaireId(evt.getCommentaireId())
                .produitId(evt.getProduitId())
                .build()
            );
        }
    }
}
