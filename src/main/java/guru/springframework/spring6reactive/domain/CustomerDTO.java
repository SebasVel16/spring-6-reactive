package guru.springframework.spring6reactive.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerDTO {


    private Integer id;
    @NotBlank
    @Size(min = 3,max = 255)
    private String name;

    @Size(min = 1,max = 255)
    @Email
    private String email;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;
}
