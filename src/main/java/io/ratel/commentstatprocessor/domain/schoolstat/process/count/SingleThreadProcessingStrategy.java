package io.ratel.commentstatprocessor.domain.schoolstat.processor;

import io.ratel.commentstatprocessor.domain.schoolstat.store.SchoolStatResultStore;
import lombok.RequiredArgsConstructor;

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
public class SingleThreadProcessingStrategy implements CountSchoolProcessingStrategy {
    private final SchoolStatCounter counter = new SchoolStatCounter();

    @Override
    public void process(List<String> comments) {
        for (String comment : comments) {
            counter.countSchools(comment);
        }
    }
}
