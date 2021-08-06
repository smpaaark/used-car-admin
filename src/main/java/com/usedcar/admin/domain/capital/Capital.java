package com.usedcar.admin.domain.capital;

import com.usedcar.admin.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Capital extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "capital_id")
    private Long id; // ID

    private String capitalCode; // 캐피탈 코드
    private String capitalName; // 캐피탈 명

    @Builder
    public Capital(String capitalCode, String capitalName) {
        this.capitalCode = capitalCode;
        this.capitalName = capitalName;
    }

}
