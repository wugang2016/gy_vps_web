package com.bj.job;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bj.job.rw.EcueExcelReader;
import com.bj.job.rw.EcueExcelWriter;
import com.bj.pojo.SubSystemInfo;
import com.bj.service.SubSystemService;

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
    private SubSystemService subSystemService;

    // 保留最近*导入*任务
    private LRUMap<UUID, AdminExcelTask> lastImportJobs = new LRUMap<>(10);

    // 保留最近*导出*任务
    private LRUMap<UUID, AdminExcelTask> lastExportJobs = new LRUMap<>(10);

    private AtomicInteger nPendingImportJobs = new AtomicInteger(0);

    public AdminExcelTask getAdminImportTask(UUID taskId) {
        return lastImportJobs.get(taskId);
    }

    public AdminExcelTask getAdminExportTask(UUID taskId) {
        return lastExportJobs.get(taskId);
    }

    public ExportEcueJob exportEcueExcel() {
        File tmpFile = new File(excelExportTmpDir, "exportEcueExcel" + UUID.randomUUID());
        ExportEcueJob job = new ExportEcueJob(tmpFile, "导出ECUE数据（" + tmpFile.getName() + "）");
        exportExecutor.submit(job);
        return job;
    }
    
	public ImportEcueJob importEcueExcel(String originalFilename, InputStream input)
			throws IOException {
        File tmpFile = File.createTempFile("bj.import.ecue.excel", Long.toString(System.nanoTime()));
        FileOutputStream fos = new FileOutputStream(tmpFile);
        BufferedOutputStream output = new BufferedOutputStream(fos);
        try {
            IOUtils.copy(input, output);
            ImportEcueJob job = new ImportEcueJob(tmpFile, "导入ECUE数据（" + originalFilename + "）");
            importExecutor.submit(job);
            nPendingImportJobs.incrementAndGet();
            return job;
        } catch (RejectedExecutionException e) {
            LOGGER.warn("import ecue task rejected!", e);
        } finally {
            IOUtils.closeQuietly(output);
            IOUtils.closeQuietly(fos);
        }
        return null;
	}

    @PreDestroy
    public void destroy() {
        importExecutor.shutdown();
    }
    
    private class ImportEcueJob implements AdminExcelTask, Runnable {
        private final File file;
        private final UUID taskId;
        private final String name;
        private String status;
        private Date startTime;
        private Date endTime;
        private String backURL;

        public ImportEcueJob(File file, String taskName) {
            this.file = file;
            this.name = taskName;
            this.taskId = UUID.randomUUID();
            this.status = "未开始";
            lastImportJobs.put(taskId, this);
            
        }

        @Override
        public void run(){
            this.status = "解析中...";
            this.startTime = new Date();

            InputStream input = null;
            EcueExcelReader reader = null;
            try {
                input = new BufferedInputStream(new FileInputStream(file));
                reader = new EcueExcelReader(input);
                doImport(reader);
            } catch (JobException e) {
            	this.status = "导入Ecue失败," + e.getMessage();
                LOGGER.error("导入Ecue失败", e);
            } catch (Exception e) {
                LOGGER.error("导入Ecue失败", e);
                this.status = "导入失败，请确认文件格式(xls,xlsx)是否正确。" + e.getMessage();
            } finally {
                try {
                    reader.close();
                } catch (Exception e) {
                    LOGGER.error("关闭Excel文件异常", e);
                }
                IOUtils.closeQuietly(input);
                try {
                    file.delete();
                } catch (Exception e) {
                    LOGGER.error("临时文件删除失败", e);
                }
                nPendingImportJobs.decrementAndGet();

                this.endTime = new Date();
                LOGGER.info("完成Ecue数据导入("+this.status+")，用时" + (endTime.getTime() - startTime.getTime()) + " milliseconds");
            }
        }

        /**
         * 
         * @param reader
         * @throws Exception
         */
        private void doImport(EcueExcelReader reader) throws Exception {
            List<SubSystemInfo> ecues = new LinkedList<>();
            SubSystemInfo ecue = reader.readLine();
            while (ecue != null) {
            	ecues.add(ecue);
            	ecue = reader.readLine();
            }
            if(ecues.size() > 0){
	            this.status = "解析完成，正在导入...";
	            //插入新数据
	            /*for (int i = 0; i < ecues.size(); i++) {
	            	SubSystemInfo one = ecues.get(i);
	            	if(checkData(one)) {
		            	subSystemService.insert(one);
	            	}else {
	        	    	throw new JobException(this.status);
	            	}
    	            this.status = "已导入 " + (i+1) + " 条记录";
	            }*/
	            subSystemService.checkAndBatchInsert(ecues);
            	this.status = "导入完成! (共" + ecues.size() + "条数据）";
            }else{
            	if(reader.getMessage() != null){
    	            this.status = reader.getMessage();
            	}else{
    	            this.status = "未解析到记录，请确认";
            	}
            }
		}
        
        
        
		@Override
        public String getStatus() {
            return status;
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
		public String getBackURL() {
			return backURL;
		}
		@Override
		public File getFile() {
			return file;
		}
		@Override
		public String getFilename() {
			return null;
		}
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
            int count = subSystemService.countAll();
            int actualCount = 0;

            EcueExcelWriter writer = new EcueExcelWriter(file);
            try {
                while (true) {
                    this.status = "正在导出 " + offset + " / " + count;
                    List<SubSystemInfo> ecues = subSystemService.findAll(offset, batchSize);
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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            return "导出ECUE数据[" + sdf.format(endTime) + "].xls";
        }
    }
}
