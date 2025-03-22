package io.ratel.commentstatprocessor.module.Extractor;

import java.util.ArrayList;
import java.util.List;

/**
 * packageName  : io.ratel.commentstatprocessor.module
 * fileName    : SchoolNameExtractor
 * author      : dorris
 * date        : 2025. 3. 21.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 21.          dorris             최초생성
 */
public interface SchoolNameExtractor {

    List<String> execute(String comment);
}
