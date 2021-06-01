package com.usedcar.admin.domain.release;

import java.util.List;

public interface ReleaseRepositoryCustom {

    public List<Release> findAllDesc();

    public List<Release> findByReleaseSearch(ReleaseSearch releaseSearch);

}
