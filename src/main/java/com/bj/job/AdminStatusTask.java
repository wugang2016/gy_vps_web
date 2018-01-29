package com.bj.job;

import java.util.Date;
import java.util.UUID;

/**
 * Created by liqingkun on 2018-01-16.
 */
public interface AdminStatusTask {
    String getName();

    UUID getTaskId();

    Date getStartTime();

    Date getEndTime();

    String getStatus();

    String getBackURL();
}
