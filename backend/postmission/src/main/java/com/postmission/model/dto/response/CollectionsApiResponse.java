package com.postmission.model.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CollectionsApiResponse {

    private String musicalId;
    private String musicalName;
    private String posterPath;

}
