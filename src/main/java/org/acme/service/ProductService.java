package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.entity.Product;
import org.acme.repository.ProductRepository;

import java.util.List;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository productRepository;

    public List<Product> getAllProducts() {

        return productRepository.listAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id);
    }

//    public List<Product> searchProductsByName(String name) {
//        return productRepository.find("name LIKE ?1", "%" + name + "%").list();
//    }
public List<Product> searchProductsByName(String name) {
    return productRepository.searchProductsByName(name);
}
    @Transactional
    public Product addProduct(Product product) {
        productRepository.persist(product);
        return product;
    }

    @Transactional
    public Product updateProduct(Long id, Product updatedProduct) {
        Product product = getProductById(id);
        if (product == null) {
            return null; // or throw exception
        }
        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setCategory(updatedProduct.getCategory());
        return product;
    }

    @Transactional
    public boolean deleteProduct(Long id) {
        Product product = getProductById(id);
        if (product == null) {
            return false; // or throw exception
        }
        productRepository.delete(product);
        return true;
    }
}