package io.ratel.commentstatprocessor.module;

import io.ratel.commentstatprocessor.module.extractor.SchoolNameExtractor;
import io.ratel.commentstatprocessor.module.extractor.SchoolNameExtractorByRegex;

import java.util.List;
import java.util.Map;

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
public class SchoolStatCounter {

    private SchoolNameExtractor extractor = new SchoolNameExtractorByRegex();

    public void countSchools(String comment) {
        List<String> schools = extractor.execute(comment);

        System.out.println("Comments :  " + comment);
        System.out.println("추출한 학교");
        for (String s : schools) {
            System.out.println(s);
        }
        System.out.println();

        for (String school : schools) {
            SchoolStatContext.put(school);
        }
    }
}
