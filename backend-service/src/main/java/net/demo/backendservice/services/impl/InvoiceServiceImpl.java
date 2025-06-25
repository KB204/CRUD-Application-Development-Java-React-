package net.demo.backendservice.services.impl;

import net.demo.backendservice.dao.CustomerRepository;
import net.demo.backendservice.dao.InvoiceRepository;
import net.demo.backendservice.dao.ProductRepository;
import net.demo.backendservice.dtos.invoice.InvoiceRequest;
import net.demo.backendservice.dtos.invoice.InvoiceResponse;
import net.demo.backendservice.dtos.invoice.InvoiceResponseDto;
import net.demo.backendservice.entities.Invoice;
import net.demo.backendservice.entities.Product;
import net.demo.backendservice.enums.InvoiceStatus;
import net.demo.backendservice.exceptions.ResourceNotFoundException;
import net.demo.backendservice.mappers.CustomerMapper;
import net.demo.backendservice.mappers.InvoiceMapper;
import net.demo.backendservice.services.InvoiceService;
import net.demo.backendservice.utils.InvoiceSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final InvoiceMapper invoiceMapper;
    private final CustomerMapper customerMapper;

    private static final Logger logger = LoggerFactory.getLogger(InvoiceServiceImpl.class);

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, CustomerRepository customerRepository, ProductRepository productRepository, InvoiceMapper invoiceMapper, CustomerMapper customerMapper) {
        this.invoiceRepository = invoiceRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.invoiceMapper = invoiceMapper;
        this.customerMapper = customerMapper;
    }

    // method to fetch data with pagination, sorting, and dynamic filtering (data can be fetched with and without filtering)
    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceResponse> findAllInvoices(String invoiceIdentifier, String date, String status, Double price, String customerEmail, Pageable pageable) {
        Specification<Invoice> specification = InvoiceSpecification.filterWithoutConditions()
                .and(InvoiceSpecification.identifierEqual(invoiceIdentifier))
                .and(InvoiceSpecification.statusEqual(status))
                .and(InvoiceSpecification.dateLike(date))
                .and(InvoiceSpecification.amountEqual(price))
                .and(InvoiceSpecification.customerEmailEqual(customerEmail));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("invoiceDate").descending());

        List<InvoiceResponse> invoices = invoiceRepository.findAll(specification,pageable)
                .stream()
                .map(invoiceMapper::invoiceToInvoiceResponse)
                .toList();

        return new PageImpl<>(invoices);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponseDto findInvoiceDetails(String invoiceIdentifier) {
        var invoice = invoiceRepository.findByInvoiceIdentifier(invoiceIdentifier)
                .orElseThrow(() -> new ResourceNotFoundException(format("Invoice with identifier %s not found", invoiceIdentifier)));

        return invoiceMapper.invoiceToInvoiceResponseDto(invoice);
    }

    @Override
    public InvoiceResponse saveNewInvoice(InvoiceRequest request) {
        // Check if products exist
        Set<Product> products = productRepository.findAllById(request.productId())
                .stream()
                .collect(Collectors.toSet());

        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products found with the provided IDs");
        }

        // Check if customer already exists, otherwise create a new one
        var customer = customerRepository.findCustomerByEmailIgnoreCase(request.email())
                .orElseGet(() -> {
                    var newCustomer = customerMapper.requestToCustomer(request);
                    customerRepository.save(newCustomer);
                    logger.info("Created new customer: {}", newCustomer);
                    return newCustomer;
                });

        var invoice = Invoice.builder()
                .invoiceIdentifier(UUID.randomUUID().toString().substring(0, 8))
                .invoiceDate(LocalDateTime.now())
                .invoiceStatus(InvoiceStatus.PENDING)
                .products(products)
                .customer(customer)
                .build();

        double totalPrice = calculateTotalPrice(invoice);
        invoice.setTotalPrice(totalPrice);

        invoiceRepository.save(invoice);
        logger.info("Saved new invoice:");

        return invoiceMapper.invoiceToInvoiceResponse(invoice);
    }

    @Override
    public void cancelInvoice(String invoiceIdentifier) {
        var invoice = invoiceRepository.findByInvoiceIdentifier(invoiceIdentifier)
                .orElseThrow(() -> new ResourceNotFoundException(format("Invoice with identifier %s not found", invoiceIdentifier)));

        invoice.setInvoiceStatus(InvoiceStatus.CANCELED);
        invoiceRepository.save(invoice);
        logger.info("Canceled Invoice");
    }

    @Override
    public void completeInvoice(String invoiceIdentifier) {
        var invoice = invoiceRepository.findByInvoiceIdentifier(invoiceIdentifier)
                .orElseThrow(() -> new ResourceNotFoundException(format("Invoice with identifier %s not found", invoiceIdentifier)));

        invoice.setInvoiceStatus(InvoiceStatus.COMPLETED);
        invoiceRepository.save(invoice);
        logger.info("Completed Invoice");
    }

    private double calculateTotalPrice(Invoice invoice) {
        return invoice.getProducts()
                .stream()
                .mapToDouble(Product::getPrice)
                .sum();
    }
}
