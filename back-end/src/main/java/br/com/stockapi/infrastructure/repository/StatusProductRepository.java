package br.com.stockapi.infrastructure.repository;

import br.com.stockapi.infrastructure.model.StatusProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusProductRepository extends JpaRepository<StatusProduct, Long> {
}
