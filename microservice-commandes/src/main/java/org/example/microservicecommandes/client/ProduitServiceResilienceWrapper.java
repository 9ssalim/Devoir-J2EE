package org.example.microservicecommandes.client;

import io.github.resilience4j.timelimiter.TimeLimiter;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

@Service
public class ProduitServiceResilienceWrapper {

    private final ProduitClient produitClient;
    private final TimeLimiter timeLimiter;

    public ProduitServiceResilienceWrapper(ProduitClient produitClient, TimeLimiter timeLimiter) {
        this.produitClient = produitClient;
        this.timeLimiter = timeLimiter;
    }

    public CompletableFuture<String> callProduitServiceWithTimeout(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return produitClient.checkProduitExistence(id).getBody().toString();
            } catch (Exception e) {
                return "Service produit indisponible";
            }
        }).thenApply(response -> {
            try {
                return timeLimiter.executeFutureSupplier(() -> CompletableFuture.completedFuture(response));
            } catch (Exception e) {
                return "Timeout atteint, veuillez réessayer plus tard.";
            }
        });
    }
}
