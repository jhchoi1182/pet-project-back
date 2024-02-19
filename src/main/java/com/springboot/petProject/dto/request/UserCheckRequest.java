package com.springboot.petProject.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserCheckRequest {

    @Size(min = 2, max = 16, message = "ID should have between 2 and 16 characters")
    private String username;

}
