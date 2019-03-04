package fun.hercules.user.common.constants;

import fun.hercules.user.common.errors.ErrorCode;
import fun.hercules.user.common.exceptions.BadRequestException;

public enum Status {
    ENABLED, DISABLED;

    public static boolean contains(String name) {
        Status[] statuses = values();
        for (Status s : statuses) {
            if (s.name().equals(name.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    public static Status get(String name) {
        if (contains(name)) {
            return Status.valueOf(name);
        } else {
            throw new BadRequestException(ErrorCode.STATUS_NOT_FOUND);
        }
    }
}
