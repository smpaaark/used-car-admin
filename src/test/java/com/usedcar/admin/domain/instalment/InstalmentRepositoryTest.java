package com.usedcar.admin.domain.instalment;

import com.usedcar.admin.domain.capital.Capital;
import com.usedcar.admin.domain.capital.CapitalRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class InstalmentRepositoryTest {

    @Autowired
    private CapitalRepository capitalRepository;

    @Autowired
    private InstalmentRepository instalmentRepository;

    @BeforeEach
    public void setUp() {
        String[] capitalCodeList = {"JANGAN", "SIMON", "KOOKMIN", "HANA", "ALPHERA", "A"};
        String[] capitalNameList = {"장안", "시몬", "국민", "하나", "신한", "알페라", "A"};
        for (int i = 0; i < capitalCodeList.length; i++) {
            Capital capital = Capital.builder()
                    .capitalCode(capitalCodeList[i])
                    .capitalName(capitalNameList[i])
                    .build();
            capitalRepository.save(capital);
        }
    }

    @AfterEach
    public void clean() {
        instalmentRepository.deleteAll();
        capitalRepository.deleteAll();
    }

    @Test
    public void 할부_등록_메모_포함() throws Exception {
        // given
        Capital capital = capitalRepository.findAll().get(0);
        String capitalCode = capital.getCapitalCode();
        String capitalName = capital.getCapitalName();
        int instalmentAmount = 50000000;
        String companyName = "장안상사";
        String staff = "이재광";
        String carNumber = "04구4716";
        InstalmentStatus instalmentStatus = InstalmentStatus.COMPLETE;
        String memo = "메모입니다.";

        Instalment instalment = Instalment.builder()
                .capitalCode(capitalCode)
                .capitalName(capitalName)
                .instalmentAmount(instalmentAmount)
                .companyName(companyName)
                .staff(staff)
                .carNumber(carNumber)
                .instalmentStatus(instalmentStatus)
                .memo(memo)
                .build();

        LocalDateTime now = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);

        // when
        Instalment saveInstalment = instalmentRepository.save(instalment);

        // then
        assertThat(saveInstalment.getId()).isGreaterThan(0L);
        assertThat(saveInstalment.getCapitalCode()).isEqualTo(capitalCode);
        assertThat(saveInstalment.getCapitalName()).isEqualTo(capitalName);
        assertThat(saveInstalment.getInstalmentAmount()).isEqualTo(instalmentAmount);
        assertThat(saveInstalment.getCompanyName()).isEqualTo(companyName);
        assertThat(saveInstalment.getStaff()).isEqualTo(staff);
        assertThat(saveInstalment.getCarNumber()).isEqualTo(carNumber);
        assertThat(saveInstalment.getInstalmentStatus()).isEqualTo(instalmentStatus);
        assertThat(saveInstalment.getMemo()).isEqualTo(memo);
        assertThat(saveInstalment.getCreatedDate()).isAfter(now);
        assertThat(saveInstalment.getModifiedDate()).isAfter(now);
    }

    @Test
    public void 할부_등록_메모_미포함() throws Exception {
        // given
        Capital capital = capitalRepository.findAll().get(0);
        String capitalCode = capital.getCapitalCode();
        String capitalName = capital.getCapitalName();
        int instalmentAmount = 50000000;
        String companyName = "장안상사";
        String staff = "이재광";
        String carNumber = "04구4716";
        InstalmentStatus instalmentStatus = InstalmentStatus.COMPLETE;

        Instalment instalment = Instalment.builder()
                .capitalCode(capitalCode)
                .capitalName(capitalName)
                .instalmentAmount(instalmentAmount)
                .companyName(companyName)
                .staff(staff)
                .carNumber(carNumber)
                .instalmentStatus(instalmentStatus)
                .build();

        LocalDateTime now = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);

        // when
        Instalment saveInstalment = instalmentRepository.save(instalment);

        // then
        assertThat(saveInstalment.getId()).isGreaterThan(0L);
        assertThat(saveInstalment.getCapitalCode()).isEqualTo(capitalCode);
        assertThat(saveInstalment.getCapitalName()).isEqualTo(capitalName);
        assertThat(saveInstalment.getInstalmentAmount()).isEqualTo(instalmentAmount);
        assertThat(saveInstalment.getCompanyName()).isEqualTo(companyName);
        assertThat(saveInstalment.getStaff()).isEqualTo(staff);
        assertThat(saveInstalment.getCarNumber()).isEqualTo(carNumber);
        assertThat(saveInstalment.getInstalmentStatus()).isEqualTo(instalmentStatus);
        assertThat(saveInstalment.getMemo()).isNull();
        assertThat(saveInstalment.getCreatedDate()).isAfter(now);
        assertThat(saveInstalment.getModifiedDate()).isAfter(now);
    }

}