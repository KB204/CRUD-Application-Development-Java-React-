package net.demo.backendservice.dao;

import net.demo.backendservice.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer>, JpaSpecificationExecutor<Invoice> {
    Optional<Invoice> findByInvoiceIdentifier(String invoiceIdentifier);
}
