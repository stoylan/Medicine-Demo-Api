package medicine.demo.project.core.utilities.results;

import lombok.*;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    @NonNull
    private boolean success;

    private String message;



}
