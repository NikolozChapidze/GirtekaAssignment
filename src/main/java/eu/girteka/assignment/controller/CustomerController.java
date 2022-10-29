package eu.girteka.assignment.controller;

import eu.girteka.assignment.dto.CustomerDto;
import eu.girteka.assignment.model.Customer;
import eu.girteka.assignment.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("customers")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @Operation(method = "GET",
            description = "Get Customer by id"
    )
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved customer", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Server error")})
    @Cacheable(value = "customer", key = "#id")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable Long id) {
        return ResponseEntity.ok().body(customerService.findById(id));
    }

    @GetMapping("")
    @Operation(method = "GET",
            description = "Get paginated and sorted customers"
    )
    public ResponseEntity<Page<Customer>> getCustomers(Pageable pageable) {
        return ResponseEntity.ok().body(customerService.getPage(pageable));
    }

    @PutMapping("")
    @Operation(method = "PUT",
            description = "Update customer"
    )
    @CachePut(value = "customer", key = "#updated.id")
    public ResponseEntity<Customer> update(@RequestBody @Valid Customer updated) {
        return ResponseEntity.ok().body(customerService.update(updated));
    }

    @PostMapping("")
    @Operation(method = "POST",
            description = "Create new customer"
    )
    public ResponseEntity<Customer> create(@RequestBody @Valid Customer created) {
        return new ResponseEntity<>(customerService.create(created), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(method = "DELETE",
            description = "Delete customer"
    )
    @CacheEvict(value = "customer", key = "#id")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.ok("Customer deleted successfully");
    }


}
