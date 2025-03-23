package io.ratel.commentstatprocessor.module;

import io.ratel.commentstatprocessor.module.context.SchoolStatContext;
import io.ratel.commentstatprocessor.module.extractor.SchoolNameExtractor;
import io.ratel.commentstatprocessor.module.extractor.SchoolNameExtractorByRegex;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * packageName  : io.ratel.commentstatprocessor.module
 * fileName    : SchoolStatCounter
 * author      : dorris
 * date        : 2025. 3. 21.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 21.          dorris             최초생성
 */
@Slf4j
public class SchoolStatCounter {

    private SchoolNameExtractor extractor = new SchoolNameExtractorByRegex();

    public void countSchools(String comment) {
        List<String> schools = extractor.execute(comment);

        log.debug("======================학교명 추출 ========================");
        log.debug("Comments :  {}", comment);
        log.debug("추출한 학교 목록");
        for (String school : schools) {
            log.debug("학교이름 > {}",school);
        }

        log.debug("\n");
        log.debug("============================================================");
        for (String school : schools) {
            SchoolStatContext.put(school);
        }
    }
}
