package br.com.stockapi.infrastructure.repository;

import br.com.stockapi.infrastructure.model.Product;
import br.com.stockapi.infrastructure.model.StatusProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByItemCode(String code);
    Page<Product> findAllByStatusProduct(Pageable pageable, StatusProduct statusProduct);
    Optional<Product> findByProduct(String product);
}
