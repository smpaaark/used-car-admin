package com.usedcar.admin.domain.capital;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CapitalRepositoryTest {

    @Autowired
    private CapitalRepository capitalRepository;

    private Long capitalId;

    @BeforeEach
    public void setUp() {
        String[] capitalCodeList = {"JANGAN", "SIMON", "KOOKMIN", "HANA", "ALPHERA", "A"};
        String[] capitalNameList = {"장안", "시몬", "국민", "하나", "신한", "알페라", "A"};
        Capital saveCapital = null;
        for (int i = 0; i < capitalCodeList.length; i++) {
            Capital capital = Capital.builder()
                    .capitalCode(capitalCodeList[i])
                    .capitalName(capitalNameList[i])
                    .build();
            saveCapital = capitalRepository.save(capital);
        }

        capitalId = saveCapital.getId();
    }

    @AfterEach
    public void clean() {
        capitalRepository.deleteAll();
    }

    @Test
    public void 캐피탈_1개_조회() throws Exception {
        // given
        Long capitalId = this.capitalId;
        LocalDateTime now = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);

        // when
        Capital findCapital = capitalRepository.findById(capitalId).get();

        // then
        assertThat(findCapital.getId()).isEqualTo(capitalId);
        assertThat(findCapital.getCapitalCode()).isNotEmpty();
        assertThat(findCapital.getCapitalName()).isNotEmpty();
        assertThat(findCapital.getCreatedDate()).isAfter(now);
        assertThat(findCapital.getModifiedDate()).isAfter(now);
    }

}