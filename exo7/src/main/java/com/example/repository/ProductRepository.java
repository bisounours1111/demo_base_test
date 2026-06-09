package com.example.repository;

import com.example.domain.Product;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findByReference(String reference);
}