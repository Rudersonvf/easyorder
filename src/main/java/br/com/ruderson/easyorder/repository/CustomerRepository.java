package br.com.ruderson.easyorder.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ruderson.easyorder.domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
