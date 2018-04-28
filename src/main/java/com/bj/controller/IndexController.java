/**
 * 
 */
package com.bj.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
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

import net.sf.json.JSONObject;

/**
 * @author LQK
 *
 */
@Controller
public class IndexController {
    @Resource
    private JobService jobService ;
    
    @Value("${bijie.upload.file.path}")
    private String uploadFileDir;
    
    @GetMapping({"", "/"})
    public String index() {
        return "redirect:/task/split/list";
    }

    @PostMapping("/system_status")
    public @ResponseBody String getStatus() {
    	String cpu = ComputerMonitorUtil.getCpuUsage();
    	String mem = ComputerMonitorUtil.getMemoryUsage();
    	String disk = ComputerMonitorUtil.getPatitionUsage(uploadFileDir);
        return "{\"cpu\":\"" + cpu + "\",\"mem\":\"" + mem + "\",\"disk\":\"" + disk + "\"}";
    }

    @GetMapping("/job/{uuid}")
    public @ResponseBody String dataImport(@PathVariable String uuid,
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

    @GetMapping(value = "/download/{uuid}",
            produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @ResponseBody
    public FileSystemResource downloadExportFile(@PathVariable String uuid,
                                                  	HttpServletResponse response) throws UnsupportedEncodingException {
    	AdminStatusTask task = jobService.getAdminStatusTask(uuid);
    	String filename = "Package_" + BaseUtil.format(new Date(), "yyyyMMdd") + ".zip";
        response.setHeader("Content-Disposition", "attachment; " +
                "filename=\"" + URLEncoder.encode(filename, "UTF-8") + "\";" +
                "filename*=utf-8''" + URLEncoder.encode(filename, "UTF-8"));
        return new FileSystemResource(new File(task.getZipPath()));
    }
}
