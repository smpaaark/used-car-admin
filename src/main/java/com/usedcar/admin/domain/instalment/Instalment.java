package com.usedcar.admin.domain.instalment;

import com.usedcar.admin.domain.BaseTimeEntity;
import com.usedcar.admin.domain.capital.Capital;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Instalment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instalment_id")
    private Long id; // ID

    private String capitalCode; // 캐피탈 코드
    private String capitalName; // 캐피탈 명
    private int instalmentAmount; // 할부금액
    private String companyName; // 차량 상사명
    private String staff; // 담당딜러
    private String carNumber; // 차량번호

    @Enumerated(EnumType.STRING)
    private InstalmentStatus instalmentStatus; // 할부 상태

    private String memo; // 비고

    @Builder
    public Instalment(String capitalCode, String capitalName, int instalmentAmount, String companyName, String staff, String carNumber, InstalmentStatus instalmentStatus, String memo) {
        this.capitalCode = capitalCode;
        this.capitalName = capitalName;
        this.instalmentAmount = instalmentAmount;
        this.companyName = companyName;
        this.staff = staff;
        this.carNumber = carNumber;
        this.instalmentStatus = instalmentStatus;
        this.memo = memo;
    }

    /**
     * 할부 생성
     */
    public void create(Capital capital) {
        this.capitalCode = capital.getCapitalCode();
        this.capitalName = capital.getCapitalName();
        this.instalmentStatus = InstalmentStatus.COMPLETE;
    }

}
