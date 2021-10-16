package medicine.demo.project.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TokenRefreshDto {
    @NotBlank
    private String refreshToken;

}
