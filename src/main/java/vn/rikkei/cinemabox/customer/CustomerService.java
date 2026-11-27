package vn.rikkei.cinemabox.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.rikkei.cinemabox.common.ConflictException;
import vn.rikkei.cinemabox.common.PageResponse;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public PageResponse<CustomerResponse> getAllCustomers(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page is not valid. It must be greater than or equal to 0");
        }
        if (size < 1 || size > 50) {
            throw new IllegalArgumentException("Size is not valid. It must be between 1 and 50");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        Page<CustomerResponse> customerPage = customerRepository.findAll(pageable)
                .map(c -> CustomerResponse.builder()
                        .id(c.getId())
                        .fullName(c.getFullName())
                        .email(c.getEmail())
                        .phone(c.getPhone())
                        .memberLevel(c.getMemberLevel())
                        .createdAt(c.getCreatedAt())
                        .build());

        return PageResponse.from(customerPage);
    }

    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email đã tồn tại trong hệ thống");
        }

        Customer customer = new Customer();
        customer.setFullName(request.getFullName().trim());
        customer.setEmail(request.getEmail().trim());
        customer.setPhone(request.getPhone());
        customer.setMemberLevel(request.getMemberLevel() == null ? "STANDARD" : request.getMemberLevel());

        Customer saved = customerRepository.save(customer);
        return CustomerResponse.builder()
                .id(saved.getId())
                .fullName(saved.getFullName())
                .email(saved.getEmail())
                .phone(saved.getPhone())
                .memberLevel(saved.getMemberLevel())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        return CustomerResponse.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .memberLevel(customer.getMemberLevel())
                .createdAt(customer.getCreatedAt())
                .build();
    }

}
