package fr.formation.broker.command;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class ProduitCreatedCommand {
    private String id;
    private String nom;
    private BigDecimal prix;
}
