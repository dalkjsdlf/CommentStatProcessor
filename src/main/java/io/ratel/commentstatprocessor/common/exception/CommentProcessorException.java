package io.ratel.commentstatprocessor.common.exception;

/**
 * packageName  : io.ratel.commentstatprocessor.common.exception
 * fileName    : CommentProcessorException
 * author      : dorris
 * date        : 2025. 3. 23.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 23.          dorris             최초생성
 */
public class CommentProcessorException extends RuntimeException{

    private final CommentProcessorErrorCode commentProcessorErrorCode;

    public CommentProcessorException(CommentProcessorErrorCode commentProcessorErrorCode,
                                      Object... messageArgs) {
        super(commentProcessorErrorCode.formatMessage(messageArgs));
        this.commentProcessorErrorCode = commentProcessorErrorCode;
    }

    public CommentProcessorErrorCode getErrorCode() {
        return commentProcessorErrorCode;
    }
}
