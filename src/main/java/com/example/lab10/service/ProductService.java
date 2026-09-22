package com.example.lab10.service;

import com.example.lab10.model.Product;
import com.example.lab10.repository.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Mono<Product> getById(String id) {
        return repository.findById(id)
            .switchIfEmpty(Mono.error(new RuntimeException("Product not found: " + id)));
    }

    public Flux<Product> getAll() {
        return repository.findAll();
    }

    public Mono<Product> save(Product product) {
        return Mono.just(product)
                .map(savedProduct -> {
                    if (savedProduct.getId() == null || savedProduct.getId().isBlank()) {
                        savedProduct.setId(UUID.randomUUID().toString());
                    }
                    return savedProduct;
                })
                .flatMap(repository::save);
    }

    public Mono<Void> delete(String id) {
        return repository.deleteById(id);
    }

    public Flux<Product> getByCategory(String category) {
        return repository.findByCategory(category);
    }

    public Mono<Double> getDiscountedPrice(String id) {
        return getById(id)
                .map(Product::getDiscountedPrice);
    }
}
