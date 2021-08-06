package com.usedcar.admin.web.dto.instalment;

import com.usedcar.admin.domain.instalment.Instalment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ToString
@NoArgsConstructor
@Getter
public class InstalmentSaveRequestDto {

    @NotNull
    private Long capitalId;

    @Min(0)
    private int instalmentAmount;

    @NotEmpty
    private String companyName;

    @NotEmpty
    private String staff;

    @NotEmpty
    private String carNumber;

    private String memo;

    @Builder
    public InstalmentSaveRequestDto(Long capitalId, int instalmentAmount, String companyName, String staff, String carNumber, String memo) {
        this.capitalId = capitalId;
        this.instalmentAmount = instalmentAmount;
        this.companyName = companyName;
        this.staff = staff;
        this.carNumber = carNumber;
        this.memo = memo;
    }

    public Instalment toEntity() {
        return Instalment.builder()
                .instalmentAmount(instalmentAmount)
                .companyName(companyName)
                .staff(staff)
                .carNumber(carNumber)
                .memo(memo)
                .build();
    }

}
