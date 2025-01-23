package org.example.microservicecommandes.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.example.microservicecommandes.client.ProduitClient;
import org.example.microservicecommandes.client.ProduitServiceResilienceWrapper;
import org.example.microservicecommandes.entity.Commande;
import org.example.microservicecommandes.repository.CommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/commandes")
public class CommandeController {

    private final CommandeRepository commandeRepository;
    private final ProduitClient produitClient; // Injection du Feign Client
    private final Environment environment;
    private final ProduitServiceResilienceWrapper produitServiceResilienceWrapper;

    @Autowired
    public CommandeController(CommandeRepository commandeRepository, ProduitServiceResilienceWrapper produitServiceResilienceWrapper, ProduitClient produitClient, Environment environment) {
        this.commandeRepository = commandeRepository;
        this.produitClient = produitClient;
        this.produitServiceResilienceWrapper = produitServiceResilienceWrapper;
        this.environment = environment;
    }

    @PostMapping
    public ResponseEntity<?> createCommande(@RequestBody Commande commande) {
        ResponseEntity<?> response = produitClient.checkProduitExistence(commande.getIdProduit());

        if (!response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.badRequest().body("Le produit avec l'ID " + commande.getIdProduit() + " n'existe pas.");
        }

        Commande savedCommande = commandeRepository.save(commande);
        return ResponseEntity.ok(savedCommande);
    }


    // Get all commandes
    @GetMapping
    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    // Get recent commandes
    @GetMapping("/recent")
    public List<Commande> getRecentCommandes() {
        int commandesLast = Integer.parseInt(environment.getProperty("mes-config-ms.commandes-last", "10"));
        LocalDate cutoffDate = LocalDate.now().minusDays(commandesLast);
        return commandeRepository.findByDateAfter(cutoffDate);
    }

    // Update a commande
    @PutMapping("/{id}")
    public Commande updateCommande(@PathVariable Long id, @RequestBody Commande commande) {
        commande.setId(id);
        return commandeRepository.save(commande);
    }

    // Delete a commande
    @DeleteMapping("/{id}")
    public void deleteCommande(@PathVariable Long id) {
        commandeRepository.deleteById(id);
    }

    @GetMapping("/test-resilience/{id}")
    @CircuitBreaker(name = "produitService", fallbackMethod = "fallbackProduitService")
    @Retry(name = "produitService", fallbackMethod = "fallbackProduitService")
    public ResponseEntity<String> testResilience(@PathVariable Long id) {
        try {
            String response = produitServiceResilienceWrapper.callProduitServiceWithTimeout(id).get();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de l'appel du service: " + e.getMessage());
        }
    }

    public ResponseEntity<String> fallbackProduitService(Long id, Throwable throwable) {
        return ResponseEntity.status(500).body("Fallback: Service produit indisponible pour le produit ID: " + id);
    }
}
