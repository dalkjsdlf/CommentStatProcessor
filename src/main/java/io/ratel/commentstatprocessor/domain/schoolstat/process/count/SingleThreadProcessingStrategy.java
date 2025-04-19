package io.ratel.commentstatprocessor.domain.schoolstat.process.count;

import io.ratel.commentstatprocessor.common.constant.AppConst;
import io.ratel.commentstatprocessor.domain.extern.fetcher.SchoolFetcher;
import io.ratel.commentstatprocessor.domain.schoolstat.process.SchoolStatCounter;
import io.ratel.commentstatprocessor.domain.schoolstat.store.SchoolInfoLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * packageName  : io.ratel.commentstatprocessor.domain.schoolstat.processor
 * fileName    : SingleThreadProcessingStrategy
 * author      : dorris
 * date        : 2025. 3. 23.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 23.          dorris             최초생성
 */
@RequiredArgsConstructor
@Slf4j
public class SingleThreadProcessingStrategy implements CountSchoolProcessingStrategy {
    private final SchoolStatCounter counter = new SchoolStatCounter();

    @Override
    public void process(List<String> comments) {

        // 학교정보 불러오기
        SchoolInfoLoader.loadOfficialSchoolNames();

        log.debug("======================학교명 추출 ========================");
        for (String comment : comments) {
            counter.countSchools(comment);
        }
        log.debug("======================학교명 추출 완료 ========================");
    }
}
