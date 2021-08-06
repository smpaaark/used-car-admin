package com.usedcar.admin.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usedcar.admin.domain.capital.Capital;
import com.usedcar.admin.domain.capital.CapitalRepository;
import com.usedcar.admin.domain.instalment.Instalment;
import com.usedcar.admin.domain.instalment.InstalmentRepository;
import com.usedcar.admin.domain.instalment.InstalmentStatus;
import com.usedcar.admin.web.dto.instalment.InstalmentSaveRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "USER")
public class InstalmentApiControllerTest {

    @Autowired
    private CapitalRepository capitalRepository;

    @Autowired
    private InstalmentRepository instalmentRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

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
        Long capitalId = capital.getId();
        int instalmentAmount = 50000000;
        String companyName = "장안상사";
        String staff = "이재광";
        String carNumber = "04구4716";
        String memo = "메모입니다.";
        InstalmentSaveRequestDto requestDto = InstalmentSaveRequestDto.builder()
                .capitalId(capitalId)
                .instalmentAmount(instalmentAmount)
                .companyName(companyName)
                .staff(staff)
                .carNumber(carNumber)
                .memo(memo)
                .build();

        LocalDateTime now = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);

        // when
        mvc.perform(post("/api/instalment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("201"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.responseDate").exists())
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.instalmentStatus").value(InstalmentStatus.COMPLETE.name()));

        // then
        Instalment findInstalment = instalmentRepository.findAll().get(0);
        assertThat(findInstalment.getId()).isGreaterThan(0L);
        assertThat(findInstalment.getCapitalCode()).isEqualTo(capital.getCapitalCode());
        assertThat(findInstalment.getCapitalName()).isEqualTo(capital.getCapitalName());
        assertThat(findInstalment.getInstalmentAmount()).isEqualTo(instalmentAmount);
        assertThat(findInstalment.getCompanyName()).isEqualTo(companyName);
        assertThat(findInstalment.getStaff()).isEqualTo(staff);
        assertThat(findInstalment.getCarNumber()).isEqualTo(carNumber);
        assertThat(findInstalment.getInstalmentStatus()).isEqualTo(InstalmentStatus.COMPLETE);
        assertThat(findInstalment.getMemo()).isEqualTo(memo);
        assertThat(findInstalment.getCreatedDate()).isAfter(now);
        assertThat(findInstalment.getModifiedDate()).isAfter(now);
    }

    @Test
    public void 할부_등록_메모_미포함() throws Exception {
        // given
        Capital capital = capitalRepository.findAll().get(0);
        Long capitalId = capital.getId();
        int instalmentAmount = 50000000;
        String companyName = "장안상사";
        String staff = "이재광";
        String carNumber = "04구4716";
        InstalmentSaveRequestDto requestDto = InstalmentSaveRequestDto.builder()
                .capitalId(capitalId)
                .instalmentAmount(instalmentAmount)
                .companyName(companyName)
                .staff(staff)
                .carNumber(carNumber)
                .build();

        LocalDateTime now = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);

        // when
        mvc.perform(post("/api/instalment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("201"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.responseDate").exists())
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.instalmentStatus").value(InstalmentStatus.COMPLETE.name()));

        // then
        Instalment findInstalment = instalmentRepository.findAll().get(0);
        assertThat(findInstalment.getId()).isGreaterThan(0L);
        assertThat(findInstalment.getCapitalCode()).isEqualTo(capital.getCapitalCode());
        assertThat(findInstalment.getCapitalName()).isEqualTo(capital.getCapitalName());
        assertThat(findInstalment.getInstalmentAmount()).isEqualTo(instalmentAmount);
        assertThat(findInstalment.getCompanyName()).isEqualTo(companyName);
        assertThat(findInstalment.getStaff()).isEqualTo(staff);
        assertThat(findInstalment.getCarNumber()).isEqualTo(carNumber);
        assertThat(findInstalment.getInstalmentStatus()).isEqualTo(InstalmentStatus.COMPLETE);
        assertThat(findInstalment.getMemo()).isNull();
        assertThat(findInstalment.getCreatedDate()).isAfter(now);
        assertThat(findInstalment.getModifiedDate()).isAfter(now);
    }

    @Test
    public void 필수값_오류로_인한_할부_등록_실패() throws Exception {
        // given
        InstalmentSaveRequestDto requestDto = InstalmentSaveRequestDto.builder()
                .instalmentAmount(-100)
                .build();

        // when
        mvc.perform(post("/api/instalment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").value("필수값 오류"))
                .andExpect(jsonPath("$.responseDate").exists())
                .andExpect(jsonPath("$.data").exists());

        // then
        List<Instalment> instalmentList = instalmentRepository.findAll();
        assertThat(instalmentList.size()).isEqualTo(0);
    }

    @Test
    public void 존재하지_않는_캐피탈_ID로_인한_할부_등록_실패() throws Exception {
        // given
        Long capitalId = -1L;
        int instalmentAmount = 50000000;
        String companyName = "장안상사";
        String staff = "이재광";
        String carNumber = "04구4716";
        String memo = "메모입니다.";
        InstalmentSaveRequestDto requestDto = InstalmentSaveRequestDto.builder()
                .capitalId(capitalId)
                .instalmentAmount(instalmentAmount)
                .companyName(companyName)
                .staff(staff)
                .carNumber(carNumber)
                .memo(memo)
                .build();

        // when
        mvc.perform(post("/api/instalment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.responseDate").exists())
                .andExpect(jsonPath("$.data").isEmpty());

        // then
        List<Instalment> instalmentList = instalmentRepository.findAll();
        assertThat(instalmentList.size()).isEqualTo(0);
    }

}