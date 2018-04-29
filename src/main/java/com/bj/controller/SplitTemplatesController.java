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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bj.pojo.FileArea;
import com.bj.pojo.SplitTemplates;
import com.bj.pojo.SubSystemInfo;
import com.bj.service.FileAreaService;
import com.bj.service.RealplayTaskService;
import com.bj.service.SplitTaskService;
import com.bj.service.SplitTemplatesService;
import com.bj.service.SubSystemService;
import com.bj.util.Pagination;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/manage")
public class SplitTemplatesController {
    @SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(SplitTemplatesController.class);

    @Resource
    private SplitTemplatesService splitTemplatesService;
    
    @Resource
    private SubSystemService subSystemService;
    
    @Resource
    private FileAreaService fileAreaService ;
    
    @Resource
    private SplitTaskService splitTaskService;
    
    @Resource
    private RealplayTaskService realplayTaskService;
    
    @GetMapping("/split_templates/list")
    public String goList(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "p", defaultValue = "1") int page) {
    	int count = splitTemplatesService.countAll();
    	List<SplitTemplates> subs = splitTemplatesService.findAll((page - 1) * Pagination.DEFAULT_PAGE_SIZE, Pagination.DEFAULT_PAGE_SIZE);

        Pagination pagination = new Pagination(request, page, count, Pagination.DEFAULT_PAGE_SIZE);
        model.put("splitTemplates", subs);
        model.put("pagination", pagination);
    	
        return "manage/split_templates/list";
    }

    @GetMapping("/split_templates/new")
    public String goNew(Map<String, Object> model) {
    	List<SubSystemInfo> lists = subSystemService.findAll(0, 200);
    	model.put("subSystems", lists);
        return "manage/split_templates/new";
    }

    @Transactional
    @PostMapping("/split_templates/new")
    public String doNew(@Valid SplitTemplates splitTemplates,
    							Errors result,
    							Map<String, Object> model,
                	            @RequestParam(value = "areaJson",defaultValue = "") String areaJson,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	if(result.getErrorCount() > 0){
        	for(FieldError error:result.getFieldErrors()){
            	model.put(error.getField()+"Err", error.getDefaultMessage());
        	}
        	model.put("splitTemplates", splitTemplates);
            return "manage/split_templates/new";
    	}

		List<FileArea> areaList = new ArrayList<FileArea>();
    	if(areaJson != null && areaJson.length() > 10) {
    		JSONArray jsonArray = JSONArray.fromObject(areaJson);
            for(int i=0 ; i<jsonArray.size(); i++){
            	JSONObject jsonObject = jsonArray.getJSONObject(i);  
            	FileArea area = (FileArea)JSONObject.toBean(jsonObject,FileArea.class);  
            	areaList.add(area);
            }
    	}else {
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "至少新增一条切割区域！");
            redirectAttributes.addFlashAttribute("splitTemplates", splitTemplates);
            return "redirect:/manage/split_templates/new";
    	}

		if(splitTemplatesService.insert(splitTemplates) > 0){
			if(areaList.size() > 0) {
				for(int i=0; i<areaList.size(); i++) {
					FileArea area = areaList.get(i);
					area.setTemplateId(splitTemplates.getId());
				}
				fileAreaService.batchInsert(areaList);
			}
            redirectAttributes.addFlashAttribute("message", "保存成功！");
    	}else{
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "保存失败！");
    	}
    	
        return "redirect:/manage/split_templates/list";
    }

    @GetMapping("/split_templates/{id}/edit")
    public String goEdit(Map<String, Object> model,
            				@PathVariable("id") int id) {
    	SplitTemplates splitTemplates = splitTemplatesService.findById(id);
    	List<SubSystemInfo> subSystems = subSystemService.findAll(0, 200);
    	List<FileArea> fileAreas = fileAreaService.findByTemplateId(splitTemplates.getId());
    	model.put("fileAreas", fileAreas);
    	model.put("subSystems", subSystems);
    	model.put("splitTemplates", splitTemplates);
        return "manage/split_templates/edit";
    }

    @Transactional
    @PostMapping("/split_templates/{id}/edit")
    public String doEdit(@Valid SplitTemplates splitTemplates,
    							Errors result,
    							Map<String, Object> model,
                	            @RequestParam(value = "areaJson",defaultValue = "") String areaJson,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	if(result.getErrorCount() > 0){
        	for(FieldError error:result.getFieldErrors()){
            	model.put(error.getField()+"Err", error.getDefaultMessage());
        	}
        	model.put("splitTemplates", splitTemplates);
            return "manage/split_templates/edit";
    	}
    	
    	List<FileArea> areaList = new ArrayList<FileArea>();
    	if(areaJson != null && areaJson.length() > 10) {
    		JSONArray jsonArray = JSONArray.fromObject(areaJson);
            for(int i=0 ; i<jsonArray.size(); i++){
            	JSONObject jsonObject = jsonArray.getJSONObject(i);  
            	FileArea area = (FileArea)JSONObject.toBean(jsonObject,FileArea.class);  
            	areaList.add(area);
            }
    	}else {
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "至少新增一条切割区域！");
            redirectAttributes.addFlashAttribute("splitTemplates", splitTemplates);
            return "redirect:/manage/split_templates/"+splitTemplates.getId()+"/edit";
    	}
    	
		if(splitTemplates.getId() != null && splitTemplatesService.update(splitTemplates) > 0){
			if(areaList.size() > 0) {
				fileAreaService.deteleByTemplateId(splitTemplates.getId());
				for(int i=0; i<areaList.size(); i++) {
					FileArea area = areaList.get(i);
					area.setTemplateId(splitTemplates.getId());
				}
				fileAreaService.batchInsert(areaList);
			}
            redirectAttributes.addFlashAttribute("message", "保存成功！");
    	}else{
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "保存失败！");
    	}
    	
        return "redirect:/manage/split_templates/list";
    }

    @Transactional
    @PostMapping("/split_templates/{id}/delete")
    public String doDelete(@PathVariable("id") int id,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	if(splitTaskService.countByTemplateId(id) > 0) {
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "有切割任务使用此模板，禁止删除！");
            return "redirect:/manage/split_templates/list";
    	}
    	if(realplayTaskService.countByTemplateId(id) > 0) {
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "有实时文件播放任务使用此模板，禁止删除！");
            return "redirect:/manage/split_templates/list";
    	}
    	
    	//先删除模板对应的切割区域
    	if(fileAreaService.deteleByTemplateId(id) >= 0) {
			if(splitTemplatesService.delete(id) > 0){
	            redirectAttributes.addFlashAttribute("message", "删除成功！");
	    	}else{
	            redirectAttributes.addFlashAttribute("hasError", true);
	            redirectAttributes.addFlashAttribute("message", "删除失败！");
	    	}
    	}else {
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "删除对应切割区域失败！");
    	}
        return "redirect:/manage/split_templates/list";
    }

    @PostMapping("/split_templates/{templateId}/getAreas")
    public @ResponseBody String getAreas(@PathVariable("templateId") int templateId) throws IOException {
    	List<FileArea> areas = fileAreaService.findByTemplateId(templateId);
    	JSONArray obj = JSONArray.fromObject(areas);
        return obj.toString();
    }
    
}
