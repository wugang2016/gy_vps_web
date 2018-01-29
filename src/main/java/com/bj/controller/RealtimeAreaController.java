/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this realtime except in compliance with the License.
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
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

import com.bj.job.SendMessageJob;
import com.bj.pojo.RealtimeArea;
import com.bj.pojo.SubSystemInfo;
import com.bj.service.RealtimeAreaService;
import com.bj.service.SendMessageService;
import com.bj.service.SubSystemService;
import com.bj.util.Pagination;

@Controller
public class RealtimeAreaController {
    @SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(RealtimeAreaController.class);

    @Resource
    private SendMessageService sendCommandService;
    
    @Resource
    private RealtimeAreaService realtimeAreaService;

    @Resource
    private SubSystemService subSystemService;

    @GetMapping("/realtime_area/list")
    public String goList(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "p", defaultValue = "1") int page) {
    	int count = realtimeAreaService.countAll();
    	List<RealtimeArea> list = realtimeAreaService.findAll((page - 1) * Pagination.DEFAULT_PAGE_SIZE, Pagination.DEFAULT_PAGE_SIZE);

        Pagination pagination = new Pagination(request, page, count, Pagination.DEFAULT_PAGE_SIZE);
        model.put("realtimeAreas", list);
        model.put("pagination", pagination);
    	
        return "realtime_area/list";
    }

    @GetMapping("/realtime_area/new")
    public String goNew(Map<String, Object> model) {
    	List<SubSystemInfo> lists = subSystemService.findAll(0, 200);
    	model.put("subSystems", lists);
        return "realtime_area/new";
    }

    @PostMapping("/realtime_area/new")
    public String doNew(@Valid RealtimeArea realtimeArea,
    							Errors result,
    							Map<String, Object> model,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	if(result.getErrorCount() > 0){
        	for(FieldError error:result.getFieldErrors()){
            	model.put(error.getField()+"Err", error.getDefaultMessage());
        	}
        	model.put("realtimeArea", realtimeArea);
        	List<SubSystemInfo> lists = subSystemService.findAll(0, 200);
        	model.put("subSystems", lists);
            return "realtime_area/new";
    	}
    	
    	if(realtimeAreaService.countBySysId(realtimeArea.getSubSystem().getId()) > 0) {
    		redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "切割区域对应的分系统冲突");
    	}else {
    		if(realtimeAreaService.insert(realtimeArea) > 0){
                redirectAttributes.addFlashAttribute("message", "保存成功！");
        	}else{
                redirectAttributes.addFlashAttribute("hasError", true);
                redirectAttributes.addFlashAttribute("message", "保存失败！");
        	}
    	}
    	
        return "redirect:/realtime_area/list";
    }

    @GetMapping("/realtime_area/{id}/edit")
    public String goEdit(Map<String, Object> model,
            				@PathVariable("id") int id) {
    	RealtimeArea realtimeArea = realtimeAreaService.findById(id);
    	model.put("realtimeArea", realtimeArea);
    	List<SubSystemInfo> lists = subSystemService.findAll(0, 200);
    	model.put("subSystems", lists);
        return "realtime_area/edit";
    }

    @PostMapping("/realtime_area/{id}/edit")
    public String doEdit(@Valid RealtimeArea realtimeArea,
    							Errors result,
    							Map<String, Object> model,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	if(result.getErrorCount() > 0){
        	for(FieldError error:result.getFieldErrors()){
            	model.put(error.getField()+"Err", error.getDefaultMessage());
        	}
        	model.put("realtimeArea", realtimeArea);
        	List<SubSystemInfo> lists = subSystemService.findAll(0, 200);
        	model.put("subSystems", lists);
            return "realtime_area/edit";
    	}
    	if(realtimeAreaService.countBySysIdExcept(realtimeArea.getSubSystem().getId(),realtimeArea.getId()) > 0) {
    		redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "切割区域对应的分系统冲突");
    	}else {
    		if(realtimeArea.getId() != null && realtimeAreaService.update(realtimeArea) > 0){
                redirectAttributes.addFlashAttribute("message", "保存成功！");
        	}else{
                redirectAttributes.addFlashAttribute("hasError", true);
                redirectAttributes.addFlashAttribute("message", "保存失败！");
        	}
    	}
        return "redirect:/realtime_area/list";
    }

    @PostMapping("/realtime_area/{id}/delete")
    public String doDelete(@PathVariable("id") int id,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	if(realtimeAreaService.detele(id) > 0){
            redirectAttributes.addFlashAttribute("message", "删除成功！");
    	}else{
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "删除失败！");
    	}
        return "redirect:/realtime_area/list";
    }

    @PostMapping("/realtime_area/upload")
    public String doUploadAndSend(
            HttpServletRequest request,
            final @RequestParam("file") MultipartFile file,
            Map<String, Object> model,
            final RedirectAttributes redirectAttributes) throws IOException {
        if (!file.isEmpty()) {
        	LOGGER.info(">>>>>>>>>>>>>>>>>>>>>>file size={}", file.getSize());
        	SendMessageJob task = sendCommandService.sendMessage("上传PAD背景图案("+file.getOriginalFilename()+")", null, file);
            return "redirect:/status/task/" + task.getTaskId();
        }else{
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "文件不能为空！");
        }
        return "redirect:/realtime_area/list";
    }


}
