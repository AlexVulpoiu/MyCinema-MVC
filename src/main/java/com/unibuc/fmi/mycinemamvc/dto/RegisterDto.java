package com.unibuc.fmi.mycinemamvc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {

    @NotBlank(message = "Username must be provided!")
    private String username;

    @NotBlank(message = "Password must be provided!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",
            message = "Password must be minimum 8 characters, must contain at least one uppercase letter, " +
                    "one lowercase letter and number!")
    private String password;
}
