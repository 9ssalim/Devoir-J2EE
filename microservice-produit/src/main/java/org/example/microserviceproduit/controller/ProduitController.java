package org.example.microserviceproduit.controller;

import org.example.microserviceproduit.entity.Produit;
import org.example.microserviceproduit.repository.ProduitRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/produits")
public class ProduitController {

    private final ProduitRepository produitRepository;

    public ProduitController(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    // Get all products
    @GetMapping
    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    // Get a product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Produit> getProduitById(@PathVariable Long id) {
        return produitRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // Create a new product
    @PostMapping
    public Produit createProduit(@RequestBody Produit produit) {
        return produitRepository.save(produit);
    }

    // Update a product
    @PutMapping("/{id}")
    public Produit updateProduit(@PathVariable Long id, @RequestBody Produit produit) {
        Produit existingProduit = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit not found"));
        existingProduit.setNom(produit.getNom());
        existingProduit.setDescription(produit.getDescription());
        existingProduit.setPrix(produit.getPrix());
        return produitRepository.save(existingProduit);
    }

    // Delete a product
    @DeleteMapping("/{id}")
    public void deleteProduit(@PathVariable Long id) {
        produitRepository.deleteById(id);
    }

    // Simulate a slow response
    @GetMapping("/simulate-delay/{id}")
    public ResponseEntity<String> simulateDelay(@PathVariable Long id) throws InterruptedException {
        long delay = 6000; // Simulate 1-second delay
        Thread.sleep(delay);

        if (delay < 2000) {
            return ResponseEntity.ok("Produit " + id + " traité rapidement.");
        } else {
            return ResponseEntity.ok("Produit " + id + " traité après un long délai.");
        }
    }



}
