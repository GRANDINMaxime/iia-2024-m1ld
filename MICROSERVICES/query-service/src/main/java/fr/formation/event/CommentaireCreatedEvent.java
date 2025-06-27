package fr.formation.event;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentaireCreatedEvent {
    private String id;
    private String texte;
    private int note;
    private String produitId;
}
