package com.bj.job;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * Created by sha on 2017-8-23.
 */
public interface AdminExcelTask {
    String getName();

    UUID getTaskId();

    Date getStartTime();

    Date getEndTime();

    String getStatus();

    File getFile();

    String getFilename();

    String getBackURL();
}
