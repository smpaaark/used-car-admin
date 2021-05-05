package com.usedcar.admin.domain.release;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Release {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "release_id")
    private Long id;
    private String staff;
    private String salesStaff;
    private int price;
    private int deposit;
    private ReleaseStatus releaseStatus;
    private LocalDateTime releaseDate;

}
