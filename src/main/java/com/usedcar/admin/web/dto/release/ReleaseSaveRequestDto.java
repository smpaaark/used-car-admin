package com.usedcar.admin.web.dto.release;

import com.usedcar.admin.domain.payment.Payment;
import com.usedcar.admin.domain.release.Release;
import com.usedcar.admin.domain.release.ReleaseStatus;
import com.usedcar.admin.web.dto.payment.PaymentRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class ReleaseSaveRequestDto {

    @NotEmpty
    private String staff;

    @NotEmpty
    private String salesStaff;

    @Min(0)
    private int price;

    @NotNull
    private Long carId;

    @NotNull
    private ReleaseStatus status;

    @NotEmpty
    @Valid
    private List<PaymentRequestDto> payments;

    @Builder
    public ReleaseSaveRequestDto(String staff, String salesStaff, int price, Long carId, ReleaseStatus status, List<PaymentRequestDto> payments) {
        this.staff = staff;
        this.salesStaff = salesStaff;
        this.price = price;
        this.carId = carId;
        this.status = status;
        this.payments = payments;
    }

    public Release toEntity() {
        List<Payment> payments = new ArrayList<>();
        for (PaymentRequestDto requestDto : this.payments) {
            payments.add(requestDto.toEntity());
        }

        return Release.builder()
                .staff(staff)
                .salesStaff(salesStaff)
                .price(price)
                .status(status)
                .payments(payments)
                .build();
    }

}
