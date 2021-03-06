/**
 * 
 */
package com.bj.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationHome;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bj.job.AdminExcelTask;
import com.bj.job.AdminStatusTask;
import com.bj.job.ExcelCenter;
import com.bj.service.JobService;
import com.bj.util.BaseUtil;
import com.bj.util.ComputerMonitorUtil;
import com.bj.util.Contants;

import net.sf.json.JSONObject;

/**
 * @author LQK
 *
 */
@Controller
public class ExportController {
    @Resource
    private JobService jobService ;
    
    @Value("${bjjie.vps.logs.path}")
    private String vpsLogsDir;
    
    @Value("${bijie.upload.file.path}")
    private String uploadFileDir;
    
    @Value("${bijie.excel.export-tmpdir}")
    private String excelExportTmpDir;
    
    @PostMapping("/system_status")
    public @ResponseBody String getStatus() {
    	String cpu = ComputerMonitorUtil.getCpuUsage();
    	String mem = ComputerMonitorUtil.getMemoryUsage();
    	String disk = ComputerMonitorUtil.getPatitionUsage(uploadFileDir);
        return "{\"cpu\":\"" + cpu + "\",\"mem\":\"" + mem + "\",\"disk\":\"" + disk + "\"}";
    }

    @GetMapping("/job/{uuid}")
    public @ResponseBody String jobStatus(@PathVariable String uuid,
                             Map<String, Object> model,
                             HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "no-cache");

        AdminStatusTask task = jobService.getAdminStatusTask(uuid);
        String result = "";
        if(task != null) {
    		JSONObject obj = JSONObject.fromObject(task);
    		result = obj.toString();
        }
        return result;
    }

    @PostMapping("/logs/package")
    public @ResponseBody String goPackage() throws IOException {
    	List<File> fileList = new ArrayList<>();
		ApplicationHome home = new ApplicationHome(getClass());
	    File jarFile = home.getSource();
//	    File webLogsDir = new File(jarFile.getParentFile().getPath() + File.separator + "logs");
//		fileList.add(webLogsDir);
    	File vpsFileDir = new File(vpsLogsDir);
    	if(vpsFileDir.exists()) {
    		fileList.add(vpsFileDir);
    	}
//    	}else if(!webLogsDir.exists()) {
//    		return "-1";
//    	}
		String zipPath = Contants.DOWNLOAD_TEMP_DIR + BaseUtil.getStrRandom(Contants.FILE_NAME_LENGTH) + ".zip";
		File zipFileDir = new File(Contants.DOWNLOAD_TEMP_DIR);
		if(!zipFileDir.exists()) {
			zipFileDir.mkdirs();
		}
    	return jobService.exportZipFile(fileList, zipPath).getTaskId().toString();
    }

    @GetMapping(value = "/download/{uuid}/{type}",
            produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @ResponseBody
    public FileSystemResource downloadExportFile(@PathVariable String uuid,
    												@PathVariable int type, //0:切割视频打包，1：日志打包
                                                  	HttpServletResponse response) throws UnsupportedEncodingException {
    	AdminStatusTask task = jobService.getAdminStatusTask(uuid);
    	String prefix = "Package_";
    	switch (type) {
		case 1:
    		prefix = "Logs_";
			break;
		default:
			break;
		}
    	String filename = prefix + BaseUtil.format(new Date(), "yyyyMMdd") + ".zip";
        response.setHeader("Content-Disposition", "attachment; " +
                "filename=\"" + URLEncoder.encode(filename, "UTF-8") + "\";" +
                "filename*=utf-8''" + URLEncoder.encode(filename, "UTF-8"));
        return new FileSystemResource(new File(task.getZipPath()));
    }

    @GetMapping(value = "logs/download",
            produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @ResponseBody
    public FileSystemResource downloadLogs(@PathVariable String uuid,
                                                  	HttpServletResponse response) throws UnsupportedEncodingException {
    	AdminStatusTask task = jobService.getAdminStatusTask(uuid);
    	String filename = "Logs_" + BaseUtil.format(new Date(), "yyyyMMdd") + ".zip";
        response.setHeader("Content-Disposition", "attachment; " +
                "filename=\"" + URLEncoder.encode(filename, "UTF-8") + "\";" +
                "filename*=utf-8''" + URLEncoder.encode(filename, "UTF-8"));
        return new FileSystemResource(new File(task.getZipPath()));
    }
    
    /*************************************/
    /*** Excel export/import manager *****/
    /*************************************/
    @Resource
    private ExcelCenter excelCenter;

    @GetMapping("/data-export/ecue")
    public String doExportEcueExcel() throws IOException {
    	AdminExcelTask exportTask = excelCenter.exportEcueExcel();
        return "redirect:/data-export/task/" + exportTask.getTaskId();
    }
    
    @GetMapping("/data-export/task/{taskId}")
    public String showExportTaskStatus(@PathVariable UUID taskId,
                                       Map<String, Object> model,
                                       HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "no-cache");

        AdminExcelTask task = excelCenter.getAdminExportTask(taskId);
        model.put("task", task);
        if (task != null) {
            if (task.getEndTime() == null) {
                model.put("autoRefresh", true);
            } else {
                File file = task.getFile();
                URI relativePath = new File(excelExportTmpDir).toURI().relativize(file.toURI());
                model.put("taskFilePath", relativePath.toString());
            }
        }
        return "data-export-status";
    }

    @GetMapping("/data-export/ajax/{taskId}")
    public @ResponseBody String ajaxExportTaskStatus(@PathVariable UUID taskId,
                                       Map<String, Object> model,
                                       HttpServletResponse response) {
    	showExportTaskStatus(taskId, model, response);
    	if(model.get("task") != null){
    		AdminExcelTask task = (AdminExcelTask)model.get("task");
			JSONObject obj = new JSONObject();
			obj.put("taskId", task.getTaskId().toString());
			obj.put("status", task.getStatus());
			obj.put("startTime", task.getStartTime()==null?"-":BaseUtil.format(task.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("endTime", task.getEndTime()==null?"-":BaseUtil.format(task.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("autoRefresh", model.get("autoRefresh")==null?"":model.get("autoRefresh"));
			obj.put("taskFilePath", model.get("taskFilePath")==null?"":model.get("taskFilePath"));
	        return obj.toString();
    	}else {
            return "";
    	}
    }

    @GetMapping(value = "/data-export/download/{taskId}/{name:.*}",
            produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @ResponseBody
    public FileSystemResource downloadExportFile(@PathVariable UUID taskId,
    													 @PathVariable("name") String name,
                                                         HttpServletResponse response) throws UnsupportedEncodingException {
    	AdminExcelTask task = excelCenter.getAdminExportTask(taskId);
    	String filename = "Object.xls";
        if (task != null) {
        	filename = task.getFilename();
        }
        response.setHeader("Content-Disposition", "attachment; " +
                "filename=\"" + URLEncoder.encode(filename, "UTF-8") + "\";" +
                "filename*=utf-8''" + URLEncoder.encode(filename, "UTF-8"));
        return new FileSystemResource(new File(excelExportTmpDir, name));
    }
}
