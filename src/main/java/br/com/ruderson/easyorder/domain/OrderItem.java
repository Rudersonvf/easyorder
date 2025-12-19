package br.com.ruderson.easyorder.domain;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderItem {

    @EmbeddedId
    private OrderItemId id;

    private Integer quantity;
    
    private BigDecimal unitPrice;

    @Column(columnDefinition = "TEXT")
    private String observations;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    private Order order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    @ToString.Exclude
    private Product product;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OrderItem other = (OrderItem) o;
        if (id == null || other.id == null)
            return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
