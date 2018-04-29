/**
 * 
 */
package com.bj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author LQK
 *
 */
@Controller
public class IndexController {
	
    @GetMapping({"", "/"})
    public String index() {
        return "redirect:/task/split/list";
    }
	
    @GetMapping("/manage/logs_download")
    public String logsDownload() {
        return "manage/logs_download/logs";
    }
}
