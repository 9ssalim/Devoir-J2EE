package org.example.microservicecommandes.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;

@FeignClient(name = "microservice-produit", path = "/produits")
public interface ProduitClient {

    @GetMapping("/{id}")
    ResponseEntity<?> checkProduitExistence(@PathVariable Long id);

    @GetMapping("/simulate-delay/{id}")
    ResponseEntity<String> simulateDelay(@PathVariable Long id);
}
