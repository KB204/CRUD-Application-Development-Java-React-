package net.demo.backendservice.dao;

import net.demo.backendservice.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderRepository extends JpaRepository<Invoice, Long>, JpaSpecificationExecutor<Invoice> {
}
