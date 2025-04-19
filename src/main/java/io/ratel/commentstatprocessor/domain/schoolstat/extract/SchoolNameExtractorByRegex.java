package io.ratel.commentstatprocessor.domain.schoolstat.extract;

import io.ratel.commentstatprocessor.common.validation.CommentValidator;
import io.ratel.commentstatprocessor.domain.schoolstat.store.SchoolInfoStore;

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
    // 정규식: OO + 접미사
    private static final Pattern SCHOOL_NAME_PATTERN = Pattern.compile(
            "([가-힣]{2,10})(초교|중교|고교|초등학교|중학교|고등학교|대학교|초등|중등|고등|초|중|고|대)"
    );

    // 접미사 정규화 매핑
    private static final Map<String, String> SUFFIX_NORMALIZATION = Map.ofEntries(
            Map.entry("초", "초등학교"),
            Map.entry("초교", "초등학교"),
            Map.entry("초등", "초등학교"),
            Map.entry("초등학교", "초등학교"),
            Map.entry("중", "중학교"),
            Map.entry("중교", "중학교"),
            Map.entry("중등", "중학교"),
            Map.entry("중학교", "중학교"),
            Map.entry("고", "고등학교"),
            Map.entry("고교", "고등학교"),
            Map.entry("고등", "고등학교"),
            Map.entry("고등학교", "고등학교"),
            Map.entry("대", "대학교"),
            Map.entry("대학교", "대학교")
    );

    public List<String> execute(String comment) {
        List<String> result = new ArrayList<>();
        CommentValidator.validateNotNull(comment);

        String cleaned =  CommentPreprocessor.clean(comment);
        Matcher matcher = SCHOOL_NAME_PATTERN.matcher(cleaned);

        while (matcher.find()) {
            String prefix = matcher.group(1);  // ex: 창현
            String suffix = matcher.group(2);  // ex: 고, 고등, 고등학교
            String normalizedSuffix = SUFFIX_NORMALIZATION.getOrDefault(suffix, suffix);
            if(SchoolInfoStore.isContains(prefix + normalizedSuffix)) {
                result.add(prefix + normalizedSuffix);
            }
        }

        return result;
    }

}

