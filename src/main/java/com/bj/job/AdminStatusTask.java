package com.bj.job;

import java.util.Date;

/**
 * Created by liqingkun on 2018-01-16.
 */
public interface AdminStatusTask {
    String getName();

    String getTaskId();

    Date getStartTime();

    Date getEndTime();

    String getStatus();

    String getBackURL();

    String getZipPath();

    Integer getState();
}
