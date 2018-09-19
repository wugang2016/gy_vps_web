/**
 * 
 */
package com.bj.controller;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bj.job.AdminExcelTask;
import com.bj.job.ExcelCenter;
import com.bj.util.BaseUtil;
import com.bj.util.ErrorDef;
import com.bj.util.ErrorMessage;

import net.sf.json.JSONObject;

/**
 * @author LQK
 *
 */
@Controller
public class ImportController {
    @Resource
    private ExcelCenter excelCenter;
    
    @Value("${bijie.excel.export-tmpdir}")
    private String excelExportTmpDir;
    
    @PostMapping("/data-import/ecue")
    public @ResponseBody String doImportEcueFile(
            HttpServletRequest request,
            @RequestParam(value = "file") MultipartFile file,
            Map<String, Object> model,
            final RedirectAttributes redirectAttributes) throws IOException {
        if (!file.isEmpty()) {
        	AdminExcelTask task = excelCenter.importEcueExcel(
		                            file.getOriginalFilename(),
		                            file.getInputStream());
            return "redirect:/data-import/task/" + task.getTaskId();
        }
        return ErrorMessage.getErrMsg(ErrorDef.ERR_FILE_CANNOT_NULL);
    }
    

    @GetMapping("/data-import/task/{taskId}")
    public String showImportTaskStatus(@PathVariable UUID taskId,
                                       Map<String, Object> model,
                                       HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "no-cache");

        AdminExcelTask task = excelCenter.getAdminImportTask(taskId);
        model.put("task", task);
        if (task != null) {
            if (task.getEndTime() == null) {
                model.put("autoRefresh", true);
            }
        }
        return "data-import-status";
    }

    @GetMapping("/data-import/ajax/{taskId}")
    public @ResponseBody String ajaxImportTaskStatus(@PathVariable UUID taskId,
                                       Map<String, Object> model,
                                       HttpServletResponse response) {
    	showImportTaskStatus(taskId, model, response);
    	if(model.get("task") != null){
    		AdminExcelTask task = (AdminExcelTask)model.get("task");
			JSONObject obj = new JSONObject();
			obj.put("taskId", task.getTaskId().toString());
			obj.put("status", task.getStatus());
			obj.put("startTime", task.getStartTime()==null?"-":BaseUtil.format(task.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("endTime", task.getEndTime()==null?"-":BaseUtil.format(task.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("autoRefresh", model.get("autoRefresh")==null?"":model.get("autoRefresh"));
	        return obj.toString();
    	}else {
            return "";
    	}
    }

}
