package medicine.demo.project.dto.Response;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TokenRefreshResponse {
    private @NonNull String accessToken;
    private @NonNull String refreshToken;
    private String tokenType = "Bearer";

}
