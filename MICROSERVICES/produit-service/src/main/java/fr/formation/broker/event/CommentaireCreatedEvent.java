package fr.formation.broker.event;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentaireCreatedEvent {
    private String commentaireId;
    private String produitId;
}
