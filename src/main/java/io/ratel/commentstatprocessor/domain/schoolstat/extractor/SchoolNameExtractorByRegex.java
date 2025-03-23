package io.ratel.commentstatprocessor.domain.schoolstat.extractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class SchoolNameExtractorByRegex implements SchoolNameExtractor {

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
        abbreviationMap.put("창현고", "창현고등학교");

        officialNames.addAll(abbreviationMap.values());
        officialNames.add("충암고등학교");
    }

    @Override
    public List<String> execute(String comment) {
        List<String> result = new ArrayList<>();

        // 1. 전처리: 특수문자 제거 및 공백 정리
        String cleaned = CommentPreprocessor.clean(comment);
        String noSpace = cleaned.replaceAll("\\s+", "");

        // 2. false positive 제거
        for (String phrase : falsePositivePhrases) {
            noSpace = noSpace.replace(phrase.replaceAll("\\s+", ""), "");
        }

        String source = noSpace;

        // 3. 정식명칭 우선 매칭
        for (String fullName : officialNames) {
            Matcher matcher = Pattern.compile(Pattern.quote(fullName)).matcher(source);
            while (matcher.find()) {
                result.add(fullName);
            }
            // 중복 매칭 방지를 위해 등장한 부분은 제거
            source = source.replaceAll(Pattern.quote(fullName), " ");
        }

        // 4. 약어 매칭 → 정식명칭으로 변환
        for (Map.Entry<String, String> entry : abbreviationMap.entrySet()) {
            String abbrev = entry.getKey();
            String full = entry.getValue();

            Matcher matcher = Pattern.compile(Pattern.quote(abbrev)).matcher(source);
            while (matcher.find()) {
                result.add(full);
            }
            source = source.replaceAll(Pattern.quote(abbrev), " ");
        }

        return result;
    }
}

