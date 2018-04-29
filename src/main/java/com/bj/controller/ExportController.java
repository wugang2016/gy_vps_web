/**
 * 
 */
package com.bj.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

import com.bj.job.AdminStatusTask;
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
	    File webLogsDir = new File(jarFile.getParentFile().getPath() + File.separator + "logs");
		fileList.add(webLogsDir);
    	File vpsFileDir = new File(vpsLogsDir);
    	if(vpsFileDir.exists()) {
    		fileList.add(vpsFileDir);
    	}else if(!webLogsDir.exists()) {
    		return "-1";
    	}
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
}
