package com.bj.controller;

import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bj.pojo.AndroidRealplayTemplate;
import com.bj.pojo.AndroidRealplayArea;
import com.bj.pojo.SubSystemInfo;
import com.bj.service.AndroidRealplayAreaService;
import com.bj.service.AndroidRealplayTemplateService;
import com.bj.service.SubSystemService;
import com.bj.util.Pagination;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/manage")
public class AndroidRealplayTemplateController {
    @SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(AndroidRealplayTemplateController.class);

    @Resource
    private AndroidRealplayTemplateService androidRealplayTemplateService;
    
    @Resource
    private SubSystemService subSystemService;
    
    @Resource
    private AndroidRealplayAreaService androidRealplayAreaService ;
    
    @GetMapping("/android_templates/list")
    public String goList(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "p", defaultValue = "1") int page) {
    	int count = androidRealplayTemplateService.countAll();
    	List<AndroidRealplayTemplate> list = androidRealplayTemplateService.findAll((page - 1) * Pagination.DEFAULT_PAGE_SIZE, Pagination.DEFAULT_PAGE_SIZE);

        Pagination pagination = new Pagination(request, page, count, Pagination.DEFAULT_PAGE_SIZE);
        model.put("androidRealplayTemplate", list);
        model.put("pagination", pagination);
    	
        return "manage/android_templates/list";
    }

    @GetMapping("/android_templates/new")
    public String goNew(Map<String, Object> model) {
    	List<SubSystemInfo> lists = subSystemService.findAll(0, 200);
    	model.put("subSystems", lists);
        return "manage/android_templates/new";
    }

    @PostMapping("/android_templates/new")
    public String doNew(@Valid AndroidRealplayTemplate androidRealplayTemplate,
    							Errors result,
    							Map<String, Object> model,
                	            @RequestParam(value = "areaJson",defaultValue = "") String areaJson,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	if(result.getErrorCount() > 0){
        	for(FieldError error:result.getFieldErrors()){
            	model.put(error.getField()+"Err", error.getDefaultMessage());
        	}
        	model.put("androidRealplayTemplate", androidRealplayTemplate);
            return "manage/android_templates/new";
    	}

		List<AndroidRealplayArea> areaList = new ArrayList<AndroidRealplayArea>();
    	if(areaJson != null && areaJson.length() > 10) {
    		JSONArray jsonArray = JSONArray.fromObject(areaJson);
            for(int i=0 ; i<jsonArray.size(); i++){
            	JSONObject jsonObject = jsonArray.getJSONObject(i);  
            	AndroidRealplayArea area = (AndroidRealplayArea)JSONObject.toBean(jsonObject,AndroidRealplayArea.class);  
            	areaList.add(area);
            }
    	}else {
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "至少新增一条切割区域！");
            redirectAttributes.addFlashAttribute("androidRealplayTemplate", androidRealplayTemplate);
            return "redirect:/manage/android_templates/new";
    	}

		if(androidRealplayTemplateService.insert(androidRealplayTemplate) > 0){
			if(areaList.size() > 0) {
				for(int i=0; i<areaList.size(); i++) {
					AndroidRealplayArea area = areaList.get(i);
					area.setTemplateId(androidRealplayTemplate.getId());
				}
				androidRealplayAreaService.batchInsert(areaList);
			}
            redirectAttributes.addFlashAttribute("message", "保存成功！");
    	}else{
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "保存失败！");
    	}
    	
        return "redirect:/manage/android_templates/list";
    }

    @GetMapping("/android_templates/{id}/edit")
    public String goEdit(Map<String, Object> model,
            				@PathVariable("id") int id) {
    	AndroidRealplayTemplate androidRealplayTemplate = androidRealplayTemplateService.findById(id);
    	List<SubSystemInfo> subSystems = subSystemService.findAll(0, 200);
    	List<AndroidRealplayArea> fileAreas = androidRealplayAreaService.findByTemplateId(androidRealplayTemplate.getId());
    	model.put("fileAreas", fileAreas);
    	model.put("subSystems", subSystems);
    	model.put("androidRealplayTemplate", androidRealplayTemplate);
        return "manage/android_templates/edit";
    }

    @PostMapping("/android_templates/{id}/edit")
    public String doEdit(@Valid AndroidRealplayTemplate androidRealplayTemplate,
    							Errors result,
    							Map<String, Object> model,
                	            @RequestParam(value = "areaJson",defaultValue = "") String areaJson,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	if(result.getErrorCount() > 0){
        	for(FieldError error:result.getFieldErrors()){
            	model.put(error.getField()+"Err", error.getDefaultMessage());
        	}
        	model.put("androidRealplayTemplate", androidRealplayTemplate);
            return "manage/android_templates/edit";
    	}
    	
    	List<AndroidRealplayArea> areaList = new ArrayList<AndroidRealplayArea>();
    	if(areaJson != null && areaJson.length() > 10) {
    		JSONArray jsonArray = JSONArray.fromObject(areaJson);
            for(int i=0 ; i<jsonArray.size(); i++){
            	JSONObject jsonObject = jsonArray.getJSONObject(i);  
            	AndroidRealplayArea area = (AndroidRealplayArea)JSONObject.toBean(jsonObject,AndroidRealplayArea.class);  
            	areaList.add(area);
            }
    	}else {
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "至少新增一条切割区域！");
            redirectAttributes.addFlashAttribute("androidRealplayTemplate", androidRealplayTemplate);
            return "redirect:/manage/android_templates/"+androidRealplayTemplate.getId()+"/edit";
    	}
    	
		if(androidRealplayTemplate.getId() != null && androidRealplayTemplateService.update(androidRealplayTemplate) > 0){
			if(areaList.size() > 0) {
				androidRealplayAreaService.deteleByTemplateId(androidRealplayTemplate.getId());
				for(int i=0; i<areaList.size(); i++) {
					AndroidRealplayArea area = areaList.get(i);
					area.setTemplateId(androidRealplayTemplate.getId());
				}
				androidRealplayAreaService.batchInsert(areaList);
			}
            redirectAttributes.addFlashAttribute("message", "保存成功！");
    	}else{
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "保存失败！");
    	}
    	
        return "redirect:/manage/android_templates/list";
    }

    @PostMapping("/android_templates/{id}/delete")
    public String doDelete(@PathVariable("id") int id,
    							final RedirectAttributes redirectAttributes) throws IOException {    	
    	//先删除模板对应的切割区域
    	if(androidRealplayAreaService.deteleByTemplateId(id) >= 0) {
			if(androidRealplayTemplateService.delete(id) > 0){
	            redirectAttributes.addFlashAttribute("message", "删除成功！");
	    	}else{
	            redirectAttributes.addFlashAttribute("hasError", true);
	            redirectAttributes.addFlashAttribute("message", "删除失败！");
	    	}
    	}else {
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "删除对应切割区域失败！");
    	}
        return "redirect:/manage/android_templates/list";
    }

    @PostMapping("/android_templates/{templateId}/getAreas")
    public @ResponseBody String getAreas(@PathVariable("templateId") int templateId) throws IOException {
    	List<AndroidRealplayArea> areas = androidRealplayAreaService.findByTemplateId(templateId);
    	JSONArray obj = JSONArray.fromObject(areas);
        return obj.toString();
    }
    
}
