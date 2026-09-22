package com.example.lab10.repository;

import com.example.lab10.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProductRepository {

    private final Map<String, Product> store = new ConcurrentHashMap<>();

    public ProductRepository() {
        store.put("1", new Product("1", "iPhone 15 Pro (673380035-5 SEC 2)",
                "Electronics", "Apple", 50, 39900.0, "MEMBER"));
        store.put("2", new Product("2", "MacBook Air M3",
                "Electronics", "Apple", 20, 49900.0, "NONE"));
        store.put("3", new Product("3", "Samsung Galaxy S24",
                "Electronics", "Samsung", 30, 29900.0, "SEASONAL"));
    }

    public Mono<Product> findById(String id) {
        return Mono.defer(() -> Mono.justOrEmpty(store.get(id)));
    }

    public Flux<Product> findAll() {
        return Flux.defer(() -> Flux.fromIterable(store.values()));
    }

    public Mono<Product> save(Product product) {
        return Mono.defer(() -> {
            store.put(product.getId(), product);
            return Mono.just(product);
        });
    }

    public Mono<Void> deleteById(String id) {
        return Mono.defer(() -> {
            store.remove(id);
            return Mono.empty();
        });
    }

    public Flux<Product> findByCategory(String category) {
        return findAll()
                .filter(product -> product.getCategory() != null
                        && product.getCategory().equalsIgnoreCase(category));
    }
}
