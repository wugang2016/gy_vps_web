package com.bj.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bj.pojo.AndroidRealplayArea;
import com.bj.pojo.AndroidRealplayTemplate;
import com.bj.pojo.SubSystemInfo;
import com.bj.service.AndroidRealplayAreaService;
import com.bj.service.AndroidRealplayTemplateService;
import com.bj.service.SubSystemService;
import com.bj.util.BaseUtil;
import com.bj.util.Contants;
import com.bj.util.FileTypeUtil;
import com.bj.util.FileTypeUtil.FileType;
import com.bj.util.Pagination;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/manage")
public class AndroidRealplayTemplateController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AndroidRealplayTemplateController.class);

    @Resource
    private AndroidRealplayTemplateService androidRealplayTemplateService;
    
    @Resource
    private SubSystemService subSystemService;
    
    @Resource
    private AndroidRealplayAreaService androidRealplayAreaService ;
    
    @Value("${bijie.upload.file.path}")
    private String uploadFileDir;
    
    @GetMapping("/android_template/list")
    public String goList(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "p", defaultValue = "1") int page) {
    	int count = androidRealplayTemplateService.countAll();
    	List<AndroidRealplayTemplate> list = androidRealplayTemplateService.findAll((page - 1) * Pagination.DEFAULT_PAGE_SIZE, Pagination.DEFAULT_PAGE_SIZE);

        Pagination pagination = new Pagination(request, page, count, Pagination.DEFAULT_PAGE_SIZE);
        model.put("androidRealplayTemplates", list);
        model.put("pagination", pagination);
    	
        return "manage/android_template/list";
    }

    @GetMapping("/android_template/new")
    public String goNew(Map<String, Object> model) {
    	List<SubSystemInfo> lists = subSystemService.findAll(0, 200);
    	model.put("subSystems", lists);
        return "manage/android_template/new";
    }

    @Transactional
    @PostMapping("/android_template/new")
    public String doNew(@Valid AndroidRealplayTemplate androidRealplayTemplate,
    							Errors result,
    							Map<String, Object> model,
    				            final @RequestParam("file1") MultipartFile file1,
    				            final @RequestParam("file2") MultipartFile file2,
    				            final @RequestParam("file3") MultipartFile file3,
    				            final @RequestParam("file4") MultipartFile file4,
                	            @RequestParam(value = "areaJson",defaultValue = "") String areaJson,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	if(result.getErrorCount() > 0){
        	for(FieldError error:result.getFieldErrors()){
        		redirectAttributes.addFlashAttribute(error.getField()+"Err", error.getDefaultMessage());
        	}
            redirectAttributes.addFlashAttribute("areaJson", areaJson);
            redirectAttributes.addFlashAttribute("androidRealplayTemplate", androidRealplayTemplate);
            return "redirect:/manage/android_template/new";
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
            redirectAttributes.addFlashAttribute("areaJson", areaJson);
            redirectAttributes.addFlashAttribute("androidRealplayTemplate", androidRealplayTemplate);
            return "redirect:/manage/android_template/new";
    	}
    	
    	if(file1.getSize() <= 0){//签名界面效果图
    		redirectAttributes.addFlashAttribute("hasError", true);
    		redirectAttributes.addFlashAttribute("message", "签名界面效果图缺失！");
            redirectAttributes.addFlashAttribute("areaJson", areaJson);
        	redirectAttributes.addFlashAttribute("androidRealplayTemplate", androidRealplayTemplate);
            return "redirect:/manage/android_template/new";
    	} else {
			String ext = file1.getOriginalFilename().substring(file1.getOriginalFilename().lastIndexOf("."));
			String newFileName = BaseUtil.getStrRandom(Contants.FILE_NAME_LENGTH) + ext;
			String path = Contants.PIC_FILE_SUB_PATH + File.separator + BaseUtil.format(new Date());
			BaseUtil.doSaveFile(uploadFileDir + File.separator + path, file1, newFileName);
			androidRealplayTemplate.setPicPath(path + File.separator +  newFileName);
    	}
    	if(file2.getSize() <= 0){//签名界面缩略图
    		redirectAttributes.addFlashAttribute("hasError", true);
    		redirectAttributes.addFlashAttribute("message", "签名界面缩略图缺失！");
            redirectAttributes.addFlashAttribute("areaJson", areaJson);
        	redirectAttributes.addFlashAttribute("androidRealplayTemplate", androidRealplayTemplate);
            return "redirect:/manage/android_template/new";
    	} else {
			String ext = file2.getOriginalFilename().substring(file2.getOriginalFilename().lastIndexOf("."));
			String newFileName = BaseUtil.getStrRandom(Contants.FILE_NAME_LENGTH) + ext;
			String path = Contants.PIC_FILE_SUB_PATH + File.separator + BaseUtil.format(new Date());
			BaseUtil.doSaveFile(uploadFileDir + File.separator + path, file2, newFileName);
			androidRealplayTemplate.setMiniPicPath(path + File.separator +  newFileName);
    	}
    	if(file3.getSize() <= 0){//签名界面签名框图片
    		redirectAttributes.addFlashAttribute("hasError", true);
    		redirectAttributes.addFlashAttribute("message", "签名界面签名框图片缺失！");
            redirectAttributes.addFlashAttribute("areaJson", areaJson);
        	redirectAttributes.addFlashAttribute("androidRealplayTemplate", androidRealplayTemplate);
            return "redirect:/manage/android_template/new";
    	} else {
			String ext = file3.getOriginalFilename().substring(file3.getOriginalFilename().lastIndexOf("."));
			String newFileName = BaseUtil.getStrRandom(Contants.FILE_NAME_LENGTH) + ext;
			String path = Contants.PIC_FILE_SUB_PATH + File.separator + BaseUtil.format(new Date());
			BaseUtil.doSaveFile(uploadFileDir + File.separator + path, file3, newFileName);
			androidRealplayTemplate.setSigPicBoderPath(path + File.separator +  newFileName);
    	}
    	if(file4.getSize() <= 0){//签名界面背景视频
    		redirectAttributes.addFlashAttribute("hasError", true);
    		redirectAttributes.addFlashAttribute("message", "签名界面背景视频缺失！");
            redirectAttributes.addFlashAttribute("areaJson", areaJson);
        	redirectAttributes.addFlashAttribute("androidRealplayTemplate", androidRealplayTemplate);
            return "redirect:/manage/android_template/new";
    	} else {
			String path = Contants.VIDEO_FILE_SUB_PATH + File.separator + BaseUtil.format(new Date());
			BaseUtil.doSaveFile(uploadFileDir + File.separator + path, file4, null);
			androidRealplayTemplate.setBackgroudVideo(path + File.separator +  file4.getOriginalFilename());
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
        return "redirect:/manage/android_template/list";
    }

    @GetMapping("/android_template/{id}/edit")
    public String goEdit(Map<String, Object> model,
            				@PathVariable("id") int id) {
    	AndroidRealplayTemplate androidRealplayTemplate = androidRealplayTemplateService.findById(id);
    	List<SubSystemInfo> subSystems = subSystemService.findAll(0, 200);
    	List<AndroidRealplayArea> realplayAreas = androidRealplayAreaService.findByTemplateId(androidRealplayTemplate.getId());
    	model.put("realplayAreas", realplayAreas);
    	model.put("subSystems", subSystems);
    	model.put("androidRealplayTemplate", androidRealplayTemplate);
        return "manage/android_template/edit";
    }

    @Transactional
    @PostMapping("/android_template/{id}/edit")
    public String doEdit(@Valid AndroidRealplayTemplate androidRealplayTemplate,
    							Errors result,
    							Map<String, Object> model,
    				            final @RequestParam("file1") MultipartFile file1,
    				            final @RequestParam("file2") MultipartFile file2,
    				            final @RequestParam("file3") MultipartFile file3,
    				            final @RequestParam("file4") MultipartFile file4,
                	            @RequestParam(value = "areaJson",defaultValue = "") String areaJson,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	if(result.getErrorCount() > 0){
        	for(FieldError error:result.getFieldErrors()){
        		redirectAttributes.addFlashAttribute(error.getField()+"Err", error.getDefaultMessage());
        	}
            redirectAttributes.addFlashAttribute("areaJson", areaJson);
            redirectAttributes.addFlashAttribute("androidRealplayTemplate", androidRealplayTemplate);
            return "redirect:/manage/android_template/edit";
    	}
    	
    	AndroidRealplayTemplate oldAndroidRealplayTemplate = androidRealplayTemplateService.findById(androidRealplayTemplate.getId());
    	
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
            redirectAttributes.addFlashAttribute("areaJson", areaJson);
            redirectAttributes.addFlashAttribute("androidRealplayTemplate", androidRealplayTemplate);
            return "redirect:/manage/android_template/edit";
    	}
    	
    	if(file1.getSize() > 0){//签名界面效果图
			String ext = file1.getOriginalFilename().substring(file1.getOriginalFilename().lastIndexOf("."));
			String newFileName = BaseUtil.getStrRandom(Contants.FILE_NAME_LENGTH) + ext;
			String path = Contants.PIC_FILE_SUB_PATH + File.separator + BaseUtil.format(new Date());
			BaseUtil.doSaveFile(uploadFileDir + File.separator + path, file1, newFileName);
			androidRealplayTemplate.setPicPath(path + File.separator +  newFileName);
    	}
    	if(file2.getSize() > 0){//签名界面缩略图
			String ext = file2.getOriginalFilename().substring(file2.getOriginalFilename().lastIndexOf("."));
			String newFileName = BaseUtil.getStrRandom(Contants.FILE_NAME_LENGTH) + ext;
			String path = Contants.PIC_FILE_SUB_PATH + File.separator + BaseUtil.format(new Date());
			BaseUtil.doSaveFile(uploadFileDir + File.separator + path, file2, newFileName);
			androidRealplayTemplate.setMiniPicPath(path + File.separator +  newFileName);
    	}
    	if(file3.getSize() > 0){//签名界面签名框图片
			String ext = file3.getOriginalFilename().substring(file3.getOriginalFilename().lastIndexOf("."));
			String newFileName = BaseUtil.getStrRandom(Contants.FILE_NAME_LENGTH) + ext;
			String path = Contants.PIC_FILE_SUB_PATH + File.separator + BaseUtil.format(new Date());
			BaseUtil.doSaveFile(uploadFileDir + File.separator + path, file3, newFileName);
			androidRealplayTemplate.setSigPicBoderPath(path + File.separator +  newFileName);
    	}
    	if(file4.getSize() > 0){//签名界面背景视频
			String path = Contants.VIDEO_FILE_SUB_PATH + File.separator + BaseUtil.format(new Date());
			BaseUtil.doSaveFile(uploadFileDir + File.separator + path, file4, null);
			androidRealplayTemplate.setBackgroudVideo(path + File.separator +  file4.getOriginalFilename());
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
			
			if(oldAndroidRealplayTemplate != null) {
				if(oldAndroidRealplayTemplate.getBackgroudVideo() != null && androidRealplayTemplate.getBackgroudVideo() != null) {
					if(!oldAndroidRealplayTemplate.getBackgroudVideo().equalsIgnoreCase(androidRealplayTemplate.getBackgroudVideo())){
						String fullpath = uploadFileDir + File.separator + oldAndroidRealplayTemplate.getBackgroudVideo();
						BaseUtil.deleteFile(fullpath);
						LOGGER.info("delete old BackgroudVideo:"+fullpath + " BackgroudVideo:"+androidRealplayTemplate.getBackgroudVideo());
					}
				}
				
				if(oldAndroidRealplayTemplate.getMiniPicPath() != null && androidRealplayTemplate.getMiniPicPath() != null) {
					if(!oldAndroidRealplayTemplate.getMiniPicPath().equalsIgnoreCase(androidRealplayTemplate.getMiniPicPath())){
						String fullpath = uploadFileDir + File.separator + oldAndroidRealplayTemplate.getMiniPicPath();
						BaseUtil.deleteFile(fullpath);
						LOGGER.info("delete old MiniPicPath:"+fullpath+ " MiniPicPath"+androidRealplayTemplate.getMiniPicPath());
					}
				}
				
				if(oldAndroidRealplayTemplate.getPicPath() != null && androidRealplayTemplate.getPicPath() != null) {
					if(!oldAndroidRealplayTemplate.getPicPath().equalsIgnoreCase(androidRealplayTemplate.getPicPath())){
						String fullpath = uploadFileDir + File.separator + oldAndroidRealplayTemplate.getPicPath();
						BaseUtil.deleteFile(fullpath
								);
						LOGGER.info("delete old PicPath:"+fullpath + " PicPath"+androidRealplayTemplate.getPicPath());
					}
				}
				
				if(oldAndroidRealplayTemplate.getSigPicBoderPath()!=null && androidRealplayTemplate.getSigPicBoderPath() != null) {
					if(!oldAndroidRealplayTemplate.getSigPicBoderPath().equalsIgnoreCase(androidRealplayTemplate.getSigPicBoderPath())){
						String fullpath = uploadFileDir + File.separator + oldAndroidRealplayTemplate.getSigPicBoderPath();
						BaseUtil.deleteFile(fullpath);
						LOGGER.info("delete old SigPicBoderPath:"+fullpath+ " new:SigPicBoderPath"+androidRealplayTemplate.getSigPicBoderPath());
					}
				}
			}
		
            redirectAttributes.addFlashAttribute("message", "保存成功！");
    	}else{
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "保存失败！");
    	}
    	
        return "redirect:/manage/android_template/list";
    }

    @Transactional
    @PostMapping("/android_template/{id}/delete")
    public String doDelete(@PathVariable("id") int id,
    							final RedirectAttributes redirectAttributes) throws IOException {   
    	
    	AndroidRealplayTemplate oldAndroidRealplayTemplate = androidRealplayTemplateService.findById(id);
    	//先删除模板对应的切割区域
    	if(androidRealplayAreaService.deteleByTemplateId(id) >= 0) {
			if(androidRealplayTemplateService.delete(id) > 0){
				
				//delete files
				if(oldAndroidRealplayTemplate != null) {
					if(oldAndroidRealplayTemplate.getBackgroudVideo() != null) {
						String fullpath = uploadFileDir + File.separator + oldAndroidRealplayTemplate.getBackgroudVideo();
						BaseUtil.deleteFile(fullpath);
						LOGGER.info("delete old BackgroudVideo:"+fullpath);
					}
					
					if(oldAndroidRealplayTemplate.getMiniPicPath() != null) {
						String fullpath = uploadFileDir + File.separator + oldAndroidRealplayTemplate.getMiniPicPath();
						BaseUtil.deleteFile(fullpath);
						LOGGER.info("delete old MiniPicPath:"+fullpath);	
					}
					
					if(oldAndroidRealplayTemplate.getPicPath() != null) {
						String fullpath = uploadFileDir + File.separator + oldAndroidRealplayTemplate.getPicPath();
						BaseUtil.deleteFile(fullpath);
						LOGGER.info("delete old PicPath:"+fullpath);
					}
					
					if(oldAndroidRealplayTemplate.getSigPicBoderPath()!=null) {
						String fullpath = uploadFileDir + File.separator + oldAndroidRealplayTemplate.getSigPicBoderPath();
						BaseUtil.deleteFile(fullpath);
						LOGGER.info("delete old SigPicBoderPath:"+fullpath);
					}
				}
				
	            redirectAttributes.addFlashAttribute("message", "删除成功！");
	    	}else{
	            redirectAttributes.addFlashAttribute("hasError", true);
	            redirectAttributes.addFlashAttribute("message", "删除失败！");
	    	}
    	}else {
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "删除对应切割区域失败！");
    	}
        return "redirect:/manage/android_template/list";
    }

    @PostMapping("/android_template/{templateId}/getAreas")
    public @ResponseBody String getAreas(@PathVariable("templateId") int templateId) throws IOException {
    	List<AndroidRealplayArea> areas = androidRealplayAreaService.findByTemplateId(templateId);
    	JSONArray obj = JSONArray.fromObject(areas);
        return obj.toString();
    }

    /** 
     * 获取效果图 
     * @param request 
     * @return 
     */  
    @GetMapping("/android_template/{id}/{type}/pic")
    public ResponseEntity<byte[]> getBomImg(HttpServletRequest request,
    											HttpServletResponse response,
									    		@PathVariable Integer id,
									    		@PathVariable Integer type) {  
    	AndroidRealplayTemplate template = androidRealplayTemplateService.findById(id);
    	String path = uploadFileDir + File.separator;
    	switch(type) {
    	case 1:
    		path += template.getPicPath();
    		break;
    	case 2:
    		path += template.getMiniPicPath();
    		break;
    	case 3:
    		path += template.getSigPicBoderPath();
    		break;
    	}
        HttpHeaders he = new HttpHeaders();
    	byte[] pic = FileTypeUtil.getBytes(path);
        try {
            FileType imgType = FileTypeUtil.getType(path);
            switch(imgType.name()){
                case "PNG":  
                    he.setContentType(MediaType.IMAGE_PNG);  
                    break;  
                case "JPG":
                    he.setContentType(MediaType.IMAGE_JPEG);  
                    break;  
                case "JPEG":  
                    he.setContentType(MediaType.IMAGE_JPEG);  
                    break;  
                case "GIF":  
                    he.setContentType(MediaType.IMAGE_GIF);  
                    break;  
                case "BMP":  
                    he.setContentType(MediaType.valueOf("image/bmp"));  
                    break;  
                default:  
                    he.setContentType(MediaType.IMAGE_JPEG);  
                    break;  
            }
        } catch (IOException e) {
        	LOGGER.error(e.toString());
        } catch (Exception e) {
        	LOGGER.error(e.getMessage(),e);
		}
        return new ResponseEntity<>(pic,he,HttpStatus.OK);  
    }
}
