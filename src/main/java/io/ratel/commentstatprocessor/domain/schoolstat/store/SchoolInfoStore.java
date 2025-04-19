package io.ratel.commentstatprocessor.domain.schoolstat.store;

import io.ratel.commentstatprocessor.common.exception.CommentProcessorErrorCode;
import io.ratel.commentstatprocessor.common.exception.CommentProcessorException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * packageName  : io.ratel.commentstatprocessor.module
 * fileName    : SchoolStatContext
 * author      : dorris
 * date        : 2025. 3. 22.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 22.          dorris             최초생성
 */
public class SchoolInfoStore {
    private static final Set<String> schoolInfoSet = new HashSet<>();

    public static void add(String school) {
        schoolInfoSet.add(school);
    }

    public static Set<String> getSet() {
        return schoolInfoSet;
    }

    public static boolean isContains(String schoolName) {
        if(schoolInfoSet.isEmpty()) {
            throw new CommentProcessorException(CommentProcessorErrorCode.SCHOOL_NOT_EXIST_IN_INFO_SET);
        }

        return schoolInfoSet.contains(schoolName);
    }

    public static void clear() {
        schoolInfoSet.clear();
    }
}
