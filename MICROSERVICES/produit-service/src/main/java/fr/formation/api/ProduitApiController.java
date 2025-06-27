package fr.formation.api;

import org.springframework.beans.BeanUtils;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.formation.broker.command.ProduitCreatedCommand;
import fr.formation.model.Produit;
import fr.formation.repo.ProduitRepository;
import fr.formation.request.CreateOrUpdateProduitRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/produit")
@RequiredArgsConstructor
public class ProduitApiController {
    private final ProduitRepository repository;
    private final StreamBridge streamBridge;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody CreateOrUpdateProduitRequest request) {
        Produit produit = new Produit();

        BeanUtils.copyProperties(request, produit);

        this.repository.save(produit);

        this.streamBridge.send("onProduitCreated-out-0", ProduitCreatedCommand.builder()
            .id(produit.getId())
            .nom(produit.getNom())
            .prix(produit.getPrix())
            .build()
        );

        return produit.getId();
    }
}
