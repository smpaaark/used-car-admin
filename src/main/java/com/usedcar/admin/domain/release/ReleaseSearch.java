package com.usedcar.admin.domain.release;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReleaseSearch {

    private String model;
    private String startDate;
    private String endDate;
    private ReleaseStatus status;

}
