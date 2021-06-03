package com.usedcar.admin.domain.release;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.usedcar.admin.domain.release.QRelease.release;

@RequiredArgsConstructor
public class ReleaseRepositoryImpl implements ReleaseRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Release> findAllDesc() {
        return queryFactory
                .selectDistinct(release)
                .from(release)
                .innerJoin(release.car)
                .innerJoin(release.payments)
                .fetchJoin()
                .orderBy(release.id.desc())
                .fetch();
    }

    @Override
    public List<Release> findByReleaseSearch(ReleaseSearch releaseSearch) {
        return queryFactory
                .selectFrom(release)
                .where(modelLike(releaseSearch.getModel()),
                        status(releaseSearch.getStatus()),
                        date(releaseSearch.getStartDate(), releaseSearch.getEndDate()))
                .orderBy(release.id.desc())
                .fetch();
    }

    private BooleanExpression modelLike(String model) {
        if(!StringUtils.hasText(model)) {
            return null;
        }
        return release.car.model.contains(model);
    }

    private BooleanExpression status(ReleaseStatus status) {
        if(status == null) {
            return null;
        }
        return release.status.eq(status);
    }

    private BooleanExpression date(String startDate, String endDate) {
        if (!StringUtils.hasText(startDate) && !StringUtils.hasText(endDate)) {
            return null;
        } else if (StringUtils.hasText(startDate) && !StringUtils.hasText(endDate)) {
            LocalDate sLocalDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDateTime sLocalDateTime = LocalDateTime.of(sLocalDate, LocalTime.MIN);
            return release.releaseDate.goe(sLocalDateTime);
        } else if (!StringUtils.hasText(startDate) && StringUtils.hasText(endDate)) {
            LocalDate eLocalTime = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDateTime eLocalDateTime = LocalDateTime.of(eLocalTime, LocalTime.MAX);
            return release.releaseDate.loe(eLocalDateTime);
        }

        LocalDate sLocalDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime sLocalDateTime = LocalDateTime.of(sLocalDate, LocalTime.MIN);
        LocalDate eLocalTime = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime eLocalDateTime = LocalDateTime.of(eLocalTime, LocalTime.MAX);

        return release.releaseDate.between(sLocalDateTime, eLocalDateTime);
    }

}
