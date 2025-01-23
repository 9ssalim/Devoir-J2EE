package org.example.microservicecommandes.health;

import org.example.microservicecommandes.repository.CommandeRepository;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CommandeHealthIndicator implements HealthIndicator {

    private final CommandeRepository commandeRepository;

    public CommandeHealthIndicator(CommandeRepository commandeRepository) {
        this.commandeRepository = commandeRepository;
    }

    @Override
    public Health health() {
        long count = commandeRepository.count();
        if (count == 0) {
            return Health.down().withDetail("message", "No commandes available").build();
        }
        return Health.up().withDetail("commandes-count", count).build();
    }
}
