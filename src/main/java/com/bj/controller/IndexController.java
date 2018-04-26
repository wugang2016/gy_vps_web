/**
 * 
 */
package com.bj.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bj.util.ComputerMonitorUtil;

/**
 * @author LQK
 *
 */
@Controller
public class IndexController {
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
}
