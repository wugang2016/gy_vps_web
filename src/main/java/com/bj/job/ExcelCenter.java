package com.bj.job;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.commons.collections4.map.LRUMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bj.dao.mapper.SubSystemMapper;
import com.bj.job.writer.AdminExportEcueExcelWriter;
import com.bj.pojo.SubSystemInfo;

@Service
public class ExcelCenter{
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelCenter.class);
    @Value("${bijie.excel.export-tmpdir}")
    private String excelExportTmpDir;

    @Resource(name = "importExecutor")
    private ExecutorService importExecutor;

    @Resource(name = "exportExecutor")
    private ExecutorService exportExecutor;
    
    @Resource
    private SubSystemMapper subSystemMapper;

    // 保留最近*导入*任务
    private LRUMap<UUID, AdminExcelTask> lastImportJobs = new LRUMap<>(10);

    // 保留最近*导出*任务
    private LRUMap<UUID, AdminExcelTask> lastExportJobs = new LRUMap<>(10);

    public AdminExcelTask getAdminImportTask(UUID taskId) {
        return lastImportJobs.get(taskId);
    }

    public AdminExcelTask getAdminExportTask(UUID taskId) {
        return lastExportJobs.get(taskId);
    }

    private List<AdminExcelTask> tasks = new CopyOnWriteArrayList<>();

    private AtomicInteger nPendingImportJobs = new AtomicInteger(0);

    public ExportEcueJob exportEcueExcel() {
        File tmpFile = new File(excelExportTmpDir, "exportEcueExcel" + UUID.randomUUID());
        ExportEcueJob job = new ExportEcueJob(tmpFile, "导出ECUE数据（" + tmpFile.getName() + "）");
        exportExecutor.submit(job);
        return job;
    }

    @PreDestroy
    public void destroy() {
        importExecutor.shutdown();
    }

    private class ExportEcueJob implements AdminExcelTask, Runnable {
        private final File file;
        private final UUID taskId;
        private final String name;
        private String status;
        private Date startTime;
        private Date endTime;
        private String backURL;

        public ExportEcueJob(File file, String taskName) {
            this.file = file;
            this.name = taskName;
            this.taskId = UUID.randomUUID();
            this.status = "未开始";
            lastExportJobs.put(taskId, this);
        }

        @Override
        public void run() {
            this.status = "导出中";
            this.startTime = new Date();

            int offset = 0;
            int batchSize = 100;
            int count = subSystemMapper.countAll();
            int actualCount = 0;

            AdminExportEcueExcelWriter writer = new AdminExportEcueExcelWriter(file);
            try {
                while (true) {
                    this.status = "正在导出 " + offset + " / " + count;
                    List<SubSystemInfo> ecues = subSystemMapper.findAll(offset, batchSize);
                    offset += batchSize;
                    actualCount += ecues.size();
                    if (!ecues.isEmpty()) {
                        writer.write(ecues);
                    } else {
                        break;
                    }
                }
                this.status = "正在保存";
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.status = "导出完成，" + actualCount + " / " + count;
            } catch (Exception e) {
                LOGGER.error("导出Ecue资料失败", e);
                try {
                    writer.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                this.status = "导出失败，" + e.getMessage();
            } finally {
                this.endTime = new Date();
            }
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public UUID getTaskId() {
            return taskId;
        }

        @Override
        public Date getStartTime() {
            return startTime;
        }

        @Override
        public Date getEndTime() {
            return endTime;
        }

        @Override
        public String getStatus() {
            return status;
        }

        @Override
        public File getFile() {
            return file;
        }

        @Override
        public String getBackURL() {
            return backURL;
        }

        @Override
        public String getFilename() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmdd");
            return "导出ECUE数据[" + sdf.format(endTime) + "].xls";
        }
    }
}
