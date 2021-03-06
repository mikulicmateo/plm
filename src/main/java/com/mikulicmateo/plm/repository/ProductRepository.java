package com.mikulicmateo.plm.repository;

import com.mikulicmateo.plm.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByCode(String code);
    long deleteByCode(String code);
    Page<Product> findByNameContainingOrderByNameAsc(String name, Pageable pageable);
}
