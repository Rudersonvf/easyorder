package br.com.ruderson.easyorder.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ruderson.easyorder.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
     Page<Product> findAllByStore_Id(UUID storeId, Pageable pageable);
}
