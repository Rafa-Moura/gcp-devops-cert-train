package br.com.stockapi.infrastructure.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_estoque")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_produto")
    private String itemCode;

    @Column(name = "produto")
    private String product;

    @Column(name = "quantidade")
    private Integer quantity;

    @Column(name = "valor")
    private BigDecimal price;

    @Column(name = "usuario_cadastro")
    private String registerUser;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private StatusProduct statusProduct;

    @CreationTimestamp
    @Column(name = "data_criacao")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime updatedAt;

}
