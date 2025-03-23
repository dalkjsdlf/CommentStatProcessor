package io.ratel.commentstatprocessor.module.context;

import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class SchoolStatContext {
    private static final Map<String, Integer> schoolCountMap = new ConcurrentHashMap<>();

    public static void put(String school) {
        schoolCountMap.merge(school, 1, Integer::sum);
    }

    public static Map<String, Integer> getMap() {
        return schoolCountMap;
    }

    public static Integer getCount(String schoolName) {
        if(schoolCountMap.isEmpty()) {
            throw new RuntimeException("통계된 학교명 목록이 없습니다.");
        }

        Integer count = schoolCountMap.get(schoolName);

        if(count.describeConstable().isEmpty()) return 0;
        else return count;
    }

    public static void displayMap() {
        for(String schoolName : schoolCountMap.keySet()) {
            int count = schoolCountMap.get(schoolName);
            log.debug("{}\t{}",schoolName, count);
        }
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
