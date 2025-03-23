package io.ratel.commentstatprocessor.domain.schoolstat.store;

import io.ratel.commentstatprocessor.common.exception.CommentProcessorErrorCode;
import io.ratel.commentstatprocessor.common.exception.CommentProcessorException;

import java.util.Map;
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
public class SchoolStatResultStore {
    private static final Map<String, Integer> schoolCountMap = new ConcurrentHashMap<>();

    public static void put(String school) {
        schoolCountMap.merge(school, 1, Integer::sum);
    }

    public static Map<String, Integer> getMap() {
        return schoolCountMap;
    }

    public static Integer getCount(String schoolName) {
        if(schoolCountMap.isEmpty()) {
            throw new CommentProcessorException(CommentProcessorErrorCode.SCHOOL_NOT_FILE_IN_RESULT);
        }

        Integer count = schoolCountMap.get(schoolName);

        if(count.describeConstable().isEmpty()) return 0;
        else return count;
    }

    public static void displayMap() {
        System.out.println();
        for(String schoolName : schoolCountMap.keySet()) {
            int count = schoolCountMap.get(schoolName);
            System.out.printf("%s\t%d \n", schoolName, count);
        }
        System.out.println();
    }

    public static void clear() {
        schoolCountMap.clear();
    }

    public static String statMapToString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : schoolCountMap.entrySet()) {
            sb.append(entry.getKey()).append("\t").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}
