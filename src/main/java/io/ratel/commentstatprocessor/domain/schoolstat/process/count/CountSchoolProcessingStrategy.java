package io.ratel.commentstatprocessor.domain.schoolstat.processor;

import java.util.List;

/**
 * packageName  : io.ratel.commentstatprocessor.domain.schoolstat.processor
 * fileName    : CountSchoolProcessingStrategy
 * author      : dorris
 * date        : 2025. 3. 23.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 23.          dorris             최초생성
 */
public interface CountSchoolProcessingStrategy {
    void process(List<String> comments);
}
