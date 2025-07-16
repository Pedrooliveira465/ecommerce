package com.api.ecommerce.models;

import com.api.ecommerce.models.dto.AverageTicketDTO;
import com.api.ecommerce.models.dto.MonthlyRevenueDTO;
import com.api.ecommerce.models.dto.TopCustomerDTO;
import com.api.ecommerce.models.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "TopCustomerDTOResult",
                classes = @ConstructorResult(
                        targetClass = TopCustomerDTO.class,
                        columns = {
                                @ColumnResult(name = "customerId", type = UUID.class),
                                @ColumnResult(name = "name", type = String.class),
                                @ColumnResult(name = "totalSpent", type = BigDecimal.class)
                        }
                )
        ),
        @SqlResultSetMapping(
                name = "AverageTicketMapping",
                classes = @ConstructorResult(
                        targetClass = AverageTicketDTO.class,
                        columns = {
                                @ColumnResult(name = "userEmail", type = String.class),
                                @ColumnResult(name = "averageTicket", type = BigDecimal.class)
                        }
                )
        ),
        @SqlResultSetMapping(
                name = "MonthlyRevenueMapping",
                classes = @ConstructorResult(
                        targetClass = MonthlyRevenueDTO.class,
                        columns = {
                                @ColumnResult(name = "revenue", type = BigDecimal.class)
                        }
                )
        )
})
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();
}

