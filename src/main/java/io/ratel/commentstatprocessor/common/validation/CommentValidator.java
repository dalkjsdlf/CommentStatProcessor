package io.ratel.commentstatprocessor.common.validation;

import io.ratel.commentstatprocessor.common.exception.CommentProcessorErrorCode;
import io.ratel.commentstatprocessor.common.exception.CommentProcessorException;

/**
 * packageName  : io.ratel.commentstatprocessor.common.validation
 * fileName    : CommentValidator
 * author      : dorris
 * date        : 2025. 3. 23.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 23.          dorris             최초생성
 */
public class CommentValidator {
    public static void validateNotNull(String comment) {
        if (comment == null) {
            throw new CommentProcessorException(CommentProcessorErrorCode.COMMENT_NULL_INPUT);
        }
    }
}
