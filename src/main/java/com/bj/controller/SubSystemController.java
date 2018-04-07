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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bj.pojo.SubSystemInfo;
import com.bj.service.FileAreaService;
import com.bj.service.RealtimeAreaService;
import com.bj.service.SubSystemService;
import com.bj.util.Pagination;

@Controller
public class SubSystemController {
    @SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(SubSystemController.class);

    @Resource
    private SubSystemService subSystemService;
    
    @Resource
    private RealtimeAreaService realtimeAreaService;
    
    @Resource
    private FileAreaService fileAreaService ;

    @GetMapping("/sub_system/list")
    public String goList(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "p", defaultValue = "1") int page) {
    	int count = subSystemService.countAll();
    	List<SubSystemInfo> subs = subSystemService.findAll((page - 1) * Pagination.DEFAULT_PAGE_SIZE, Pagination.DEFAULT_PAGE_SIZE);

        Pagination pagination = new Pagination(request, page, count, Pagination.DEFAULT_PAGE_SIZE);
        model.put("subSystems", subs);
        model.put("pagination", pagination);
    	
        return "sub_system/list";
    }

    @GetMapping("/sub_system/new")
    public String goNew(Map<String, Object> model) {
        return "sub_system/new";
    }

    @PostMapping("/sub_system/new")
    public String doNew(@Valid SubSystemInfo subSystem,
    							Errors result,
    							Map<String, Object> model,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	if(result.getErrorCount() > 0){
        	for(FieldError error:result.getFieldErrors()){
            	model.put(error.getField()+"Err", error.getDefaultMessage());
        	}
        	model.put("subSystem", subSystem);
            return "sub_system/new";
    	}  	

    	if(subSystemService.countByIp(subSystem.getIp()) > 0) {
    		redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "IP地址与其它系统冲突！");
    	}
    	else {
    		if(subSystemService.insert(subSystem) > 0){
                redirectAttributes.addFlashAttribute("message", "保存成功！");
        	}else{
                redirectAttributes.addFlashAttribute("hasError", true);
                redirectAttributes.addFlashAttribute("message", "保存失败！");
        	}
    	}
    	
        return "redirect:/sub_system/list";
    }

    @GetMapping("/sub_system/{id}/edit")
    public String goEdit(Map<String, Object> model,
            				@PathVariable("id") int id) {
    	SubSystemInfo subSystem = subSystemService.findById(id);
    	model.put("subSystem", subSystem);
        return "sub_system/edit";
    }

    @PostMapping("/sub_system/{id}/edit")
    public String doEdit(@Valid SubSystemInfo subSystem,
    							Errors result,
    							Map<String, Object> model,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	if(result.getErrorCount() > 0){
        	for(FieldError error:result.getFieldErrors()){
            	model.put(error.getField()+"Err", error.getDefaultMessage());
        	}
        	model.put("subSystem", subSystem);
            return "sub_system/edit";
    	}
    	
    	if(subSystemService.countByIpExcept(subSystem.getIp(),subSystem.getId()) > 0) {
    		redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "IP地址与其它系统冲突！");
    	}
    	else {
    		if(subSystem.getId() != null && subSystemService.update(subSystem) > 0){
                redirectAttributes.addFlashAttribute("message", "保存成功！");
        	}else{
                redirectAttributes.addFlashAttribute("hasError", true);
                redirectAttributes.addFlashAttribute("message", "保存失败！");
        	}
    	}
    	
        return "redirect:/sub_system/list";
    }

    @PostMapping("/sub_system/{id}/delete")
    public String doDelete(@PathVariable("id") int id,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	if(fileAreaService.countBySysId(id) > 0) {
    		redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "请先删除改系统所对应的文件切割模板区域！");
    	}else if(realtimeAreaService.countBySysId(id) > 0){
    		redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "请先删除改系统所对应的实时流件切割模板区域！");
    	}else {
    		if(subSystemService.detele(id) > 0){
                redirectAttributes.addFlashAttribute("message", "删除成功！");
        	}else{
                redirectAttributes.addFlashAttribute("hasError", true);
                redirectAttributes.addFlashAttribute("message", "删除失败！");
        	}
    	}
    	
        return "redirect:/sub_system/list";
    }

}
