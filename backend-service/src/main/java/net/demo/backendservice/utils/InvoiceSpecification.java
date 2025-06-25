package net.demo.backendservice.utils;

import net.demo.backendservice.entities.Invoice;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public class InvoiceSpecification {

    public static Specification<Invoice> filterWithoutConditions() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    public static Specification<Invoice> identifierEqual(String identifier) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(identifier)
                        .map(reservation -> criteriaBuilder.equal(root.get("invoiceIdentifier"),identifier))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Invoice> dateLike(String date) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(date)
                        .map(reservation -> criteriaBuilder.like(criteriaBuilder.function("TO_CHAR", String.class,
                                root.get("invoiceDate"), criteriaBuilder.literal("YYYY-MM-DD")), date + "%"))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Invoice> statusEqual(String status) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(status)
                        .map(reservation -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("invoiceStatus")),
                                status.toLowerCase()))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Invoice> amountEqual(Double amount) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(amount)
                        .map(reservation -> criteriaBuilder.equal(root.get("totalPrice"),amount))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Invoice> customerEmailEqual(String customerEmail) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(customerEmail)
                        .map(customer -> criteriaBuilder.equal(root.get("customer").get("email"),customerEmail))
                        .orElse(criteriaBuilder.conjunction());
    }
}
