package org.example.microserviceproduit.repository;

import org.example.microserviceproduit.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
}
