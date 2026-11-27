package vn.rikkei.cinemabox.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomerRequest {

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Email is not valid"
    )
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String email;

    @Size(min = 10, max = 10, message = "Phone number must be exactly 10 digits")
    @Pattern(regexp = "^0\\d{9}$", message = "Phone number must start with 0 and contain only digits")
    private String phone;

    @Pattern(regexp = "^(STANDARD|GOLD)$", message = "Member level must be either STANDARD or GOLD")
    private String memberLevel;
}
