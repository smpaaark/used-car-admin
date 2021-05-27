package com.usedcar.admin.domain.release;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.usedcar.admin.domain.release.QRelease.release;

@RequiredArgsConstructor
public class ReleaseRepositoryImpl implements ReleaseRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Release> findAllDesc() {
        return queryFactory
                .selectFrom(release)
                .orderBy(release.id.desc())
                .fetch();
    }

}
