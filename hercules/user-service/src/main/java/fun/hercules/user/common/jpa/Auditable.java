package fun.hercules.user.common.jpa;

import java.time.Instant;

public interface Auditable {

    Instant getCreatedAt();

    Instant getUpdatedAt();

    String getCreatedBy();

    String getUpdatedBy();

    boolean isDeleted();

    void setDeleted(boolean deleted);
}
