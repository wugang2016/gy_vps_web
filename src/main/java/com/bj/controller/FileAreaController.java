/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bj.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections4.map.LRUMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bj.job.AdminStatusTask;
import com.bj.job.SendMessageJob;
import com.bj.pojo.FileArea;
import com.bj.pojo.SubSystemInfo;
import com.bj.service.FileAreaService;
import com.bj.service.SendMessageService;
import com.bj.service.SubSystemService;
import com.bj.util.Pagination;

@Controller
public class FileAreaController {
    @SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(FileAreaController.class);

    @Resource
    private FileAreaService fileAreaService;

    @Resource
    private SubSystemService subSystemService;

    @Resource
    private SendMessageService sendCommandService;
    
    @GetMapping("/file_area/list")
    public String goList(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "p", defaultValue = "1") int page) {
    	int count = fileAreaService.countAll();
    	List<FileArea> list = fileAreaService.findAll((page - 1) * Pagination.DEFAULT_PAGE_SIZE, Pagination.DEFAULT_PAGE_SIZE);

        Pagination pagination = new Pagination(request, page, count, Pagination.DEFAULT_PAGE_SIZE);
        model.put("fileAreas", list);
        model.put("pagination", pagination);
    	
        return "file_area/list";
    }

    @GetMapping("/file_area/new")
    public String goNew(Map<String, Object> model) {
    	List<SubSystemInfo> lists = subSystemService.findAll(0, 200);
    	model.put("subSystems", lists);
        return "file_area/new";
    }

    @PostMapping("/file_area/new")
    public String doNew(@Valid FileArea fileArea,
    							Errors result,
    							Map<String, Object> model,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	if(result.getErrorCount() > 0){
        	for(FieldError error:result.getFieldErrors()){
            	model.put(error.getField()+"Err", error.getDefaultMessage());
        	}
        	model.put("fileArea", fileArea);
        	List<SubSystemInfo> lists = subSystemService.findAll(0, 200);
        	model.put("subSystems", lists);
            return "file_area/new";
    	}
    	
    	if(fileAreaService.countBySysId(fileArea.getSubSystem().getId()) > 0) {
    		redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "切割区域对应的分系统冲突");
    	}else {
    		if(fileAreaService.insert(fileArea) > 0){
                redirectAttributes.addFlashAttribute("message", "保存成功！");
        	}else{
                redirectAttributes.addFlashAttribute("hasError", true);
                redirectAttributes.addFlashAttribute("message", "保存失败！");
        	}
    	}
    	
        return "redirect:/file_area/list";
    }

    @GetMapping("/file_area/{id}/edit")
    public String goEdit(Map<String, Object> model,
            				@PathVariable("id") int id) {
    	FileArea fileArea = fileAreaService.findById(id);
    	model.put("fileArea", fileArea);
    	List<SubSystemInfo> lists = subSystemService.findAll(0, 200);
    	model.put("subSystems", lists);
        return "file_area/edit";
    }

    @PostMapping("/file_area/{id}/edit")
    public String doEdit(@Valid FileArea fileArea,
    							Errors result,
    							Map<String, Object> model,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	if(result.getErrorCount() > 0){
        	for(FieldError error:result.getFieldErrors()){
            	model.put(error.getField()+"Err", error.getDefaultMessage());
        	}
        	model.put("fileArea", fileArea);
        	List<SubSystemInfo> lists = subSystemService.findAll(0, 200);
        	model.put("subSystems", lists);
            return "file_area/edit";
    	}
    	
    	if(fileAreaService.countBySysIdExcept(fileArea.getSubSystem().getId(), fileArea.getId()) > 0) {
    		redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "切割区域对应的分系统冲突");
    	}else {
    		if(fileArea.getId() != null && fileAreaService.update(fileArea) > 0){
                redirectAttributes.addFlashAttribute("message", "保存成功！");
        	}else{
                redirectAttributes.addFlashAttribute("hasError", true);
                redirectAttributes.addFlashAttribute("message", "保存失败！");
        	}
    	}
    	
        return "redirect:/file_area/list";
    }

    @PostMapping("/file_area/{id}/delete")
    public String doDelete(@PathVariable("id") int id,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	if(fileAreaService.detele(id) > 0){
            redirectAttributes.addFlashAttribute("message", "删除成功！");
    	}else{
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "删除失败！");
    	}
        return "redirect:/file_area/list";
    }

    @PostMapping("/file_area/upload_and_send")
    public String doUploadAndSend(
            HttpServletRequest request,
            @RequestParam(value = "newFileName") String newFileName,
            @RequestParam(value = "radio", defaultValue = "0") int radio,
            final @RequestParam("file") MultipartFile file,
            Map<String, Object> model,
            final RedirectAttributes redirectAttributes) throws IOException {
    	if(radio <= 0){
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "必须选择一种类型！");
            return "redirect:/file_area/list";
    	}
        if (!file.isEmpty()) {
        	LOGGER.info("文件名称：{} --> {}",file.getOriginalFilename(),newFileName);
        	String command = "{\"opt\":\"start_file_task\",\"filename\":\""+ newFileName + "\"}";
        	SendMessageJob task = sendCommandService.sendMessage("切割文件上传及通知("+newFileName+")", command, file, newFileName, true);
            return "redirect:/status/task/" + task.getTaskId();
        }else{
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "文件不能为空！");
        }
        return "redirect:/file_area/list";
    }
    
    @GetMapping("/status/task")
    public String getStatus(){
        return "redirect:/status/task/0-0-0-0-0";
    }

    @GetMapping("/status/task/{taskId}")
    public String getStatus(@PathVariable UUID taskId,
                             Map<String, Object> model,
                             HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "no-cache");
        List<AdminStatusTask> lastTasks  = new ArrayList<AdminStatusTask>();
        LRUMap<UUID, AdminStatusTask> taskMap = sendCommandService.getTaskMap();
        for(AdminStatusTask task:taskMap.values()){
        	lastTasks.add(task);
        }
        model.put("lastTasks", lastTasks);
        if("00000000-0000-0000-0000-000000000000".equals(taskId.toString())
        		&& lastTasks.size() > 0){
        	taskId = lastTasks.get(lastTasks.size()-1).getTaskId();
        }

	    AdminStatusTask task = sendCommandService.getAdminStatusTask(taskId);
        model.put("task", task);

        if (task != null && task.getEndTime() == null) {
            model.put("autoRefresh?", true);
        }
        return "task-status";
    }
}
