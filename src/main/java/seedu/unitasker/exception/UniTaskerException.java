package seedu.unitasker.exception;

import seedu.unitasker.UniTasker;

public class UniTaskerException extends Exception{
    public UniTaskerException(String description) {
        super(description);
    }
}
