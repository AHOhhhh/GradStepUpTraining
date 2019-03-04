package fun.hercules.order.order.platform.exports;

import java.time.Instant;

public interface Auditable {

    Instant getCreatedAt();

    Instant getUpdatedAt();

    String getCreatedBy();

    String getUpdatedBy();

    boolean isDeleted();

    void setDeleted(boolean deleted);
}
