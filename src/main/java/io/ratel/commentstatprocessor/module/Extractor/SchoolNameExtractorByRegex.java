package io.ratel.commentstatprocessor.module.Extractor;

import io.ratel.commentstatprocessor.module.CommentPreprocessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * packageName  : io.ratel.commentstatprocessor.module.Extractor
 * fileName    : SchoolNameExtractorByRegex
 * author      : dorris
 * date        : 2025. 3. 22.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 22.          dorris             최초생성
 */
public class SchoolNameExtractorByRegex implements SchoolNameExtractor{
    private static final Map<String, String> abbreviationMap = new HashMap<>();
    private static final Set<String> officialNames = new HashSet<>();
    private static final Set<String> falsePositivePhrases = Set.of(
            "서울대공원", "서울 대공원", "서울대 공원"
    );

    static {
        abbreviationMap.put("서울대", "서울대학교");
        abbreviationMap.put("연대", "연세대학교");
        abbreviationMap.put("고대", "고려대학교");
        abbreviationMap.put("부산고", "부산고등학교");
        abbreviationMap.put("충암고", "충암고등학교");

        officialNames.addAll(abbreviationMap.values());
        officialNames.add("충암고등학교");
    }

    @Override
    public List<String> execute(String comment) {
        Set<String> result = new LinkedHashSet<>();
        String cleaned = CommentPreprocessor.clean(comment); // 특수문자 제거 및 정제
        String noSpace = cleaned.replaceAll("\\s+", "");

        // 🚫 예외 문구가 포함되어 있으면 제거
        for (String phrase : falsePositivePhrases) {
            noSpace = noSpace.replace(phrase.replaceAll("\\s+", ""), "");
        }

        // 1. 약어 매핑
        for (Map.Entry<String, String> entry : abbreviationMap.entrySet()) {
            if (noSpace.contains(entry.getKey())) {
                result.add(entry.getValue());
            }
        }

        // 2. 정식 명칭 매핑
        for (String full : officialNames) {
            if (noSpace.contains(full)) {
                result.add(full);
            }
        }

        return new ArrayList<>(result);
    }

}
