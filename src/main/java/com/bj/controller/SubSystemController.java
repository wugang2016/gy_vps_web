package com.bj.controller;

import java.io.File;
import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bj.job.ExcelCenter;
import com.bj.pojo.SubSystemInfo;
import com.bj.service.FileAreaService;
import com.bj.service.SubSystemService;
import com.bj.util.BaseUtil;
import com.bj.util.Contants;
import com.bj.util.ErrorDef;
import com.bj.util.ErrorMessage;
import com.bj.util.FileTypeUtil;
import com.bj.util.FileTypeUtil.FileType;
import com.bj.util.Pagination;

@Controller
@RequestMapping("/manage")
public class SubSystemController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SubSystemController.class);

    @Resource
    private SubSystemService subSystemService;
        
    @Resource
    private FileAreaService fileAreaService ;

    @Resource
    private ExcelCenter excelCenter;
    
    @Value("${bijie.upload.file.path}")
    private String uploadFileDir;

    @GetMapping("/sub_system/list")
    public String goList(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "p", defaultValue = "1") int page) {
    	int count = subSystemService.countAll();
    	List<SubSystemInfo> subs = subSystemService.findAll((page - 1) * Pagination.DEFAULT_PAGE_SIZE, Pagination.DEFAULT_PAGE_SIZE);

        Pagination pagination = new Pagination(request, page, count, Pagination.DEFAULT_PAGE_SIZE);
        model.put("subSystems", subs);
        model.put("pagination", pagination);
    	
        return "manage/sub_system/list";
    }

    @GetMapping("/sub_system/new")
    public String goNew(Map<String, Object> model) {
        return "manage/sub_system/new";
    }

    @Transactional
    @PostMapping("/sub_system/new")
    public String doNew(@Valid SubSystemInfo subSystem,
    							Errors result,
    							Map<String, Object> model,
    				            final @RequestParam("file") MultipartFile file,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	if(result.getErrorCount() > 0){
        	for(FieldError error:result.getFieldErrors()){
            	model.put(error.getField()+"Err", error.getDefaultMessage());
        	}
        	model.put("subSystem", subSystem);
            return "manage/sub_system/new";
    	}
    	
    	if(!subSystem.getBoxIp().isEmpty()) {
    		if(subSystemService.countByIp(subSystem.getBoxIp()) > 0) {
        		redirectAttributes.addFlashAttribute("hasError", true);
//                redirectAttributes.addFlashAttribute("message", "解码器IP地址与其它e:cue设备IP地址冲突！");
        		redirectAttributes.addFlashAttribute("message", ErrorMessage.getErrMsg(ErrorDef.ERR_ECUE_BOXIP_SAMEWTIH_OTHER_ECUEIP));
                return "redirect:/manage/sub_system/list";
        	}
    		else if(subSystemService.countByBoxIp(subSystem.getBoxIp()) > 0)
        	{
        		redirectAttributes.addFlashAttribute("hasError", true);
                redirectAttributes.addFlashAttribute("message", ErrorMessage.getErrMsg(ErrorDef.ERR_ECUE_BOXIP_SAMEWTIH_OTHER_BOXIP));
                return "redirect:/manage/sub_system/list";
        	}
    	}

    	if(subSystemService.countByIp(subSystem.getIp()) > 0) {
    		redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", ErrorMessage.getErrMsg(ErrorDef.ERR_ECUE_ECUEIP_SAMEWTIH_OTHER_ECUEIP));
    	}
    	else if(subSystemService.countByBoxIp(subSystem.getIp()) > 0)
    	{
    		redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", ErrorMessage.getErrMsg(ErrorDef.ERR_ECUE_ECUEIP_SAMEWTIH_OTHER_BOXIP));
    	}
    	else {
    		if(file.getSize() > 0) {
    			String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
    			String newFileName = BaseUtil.getStrRandom(Contants.FILE_NAME_LENGTH) + ext;
    			String path = Contants.PIC_FILE_SUB_PATH + File.separator + BaseUtil.format(new Date());
    			BaseUtil.doSaveFile(uploadFileDir + File.separator + path, file, newFileName);
    			subSystem.setPicPath(path + File.separator +  newFileName);
    		}
    		if(subSystemService.insert(subSystem) > 0){
//                redirectAttributes.addFlashAttribute("message", "保存成功！");
                redirectAttributes.addFlashAttribute("message", ErrorMessage.getErrMsg(ErrorDef.INFO_SAVE_SUCCESS));
        	}else{
                redirectAttributes.addFlashAttribute("hasError", true);
//                redirectAttributes.addFlashAttribute("message", "保存失败！");
                redirectAttributes.addFlashAttribute("message", ErrorMessage.getErrMsg(ErrorDef.ERR_SAVE_FAILED));
        	}
    	}
    	
        return "redirect:/manage/sub_system/list";
    }

    @GetMapping("/sub_system/{id}/edit")
    public String goEdit(Map<String, Object> model,
            				@PathVariable("id") int id) {
    	SubSystemInfo subSystem = subSystemService.findById(id);
    	model.put("subSystem", subSystem);
        return "manage/sub_system/edit";
    }

    @Transactional
    @PostMapping("/sub_system/{id}/edit")
    public String doEdit(@Valid SubSystemInfo subSystem,
    							Errors result,
    							Map<String, Object> model,
    				            final @RequestParam("file") MultipartFile file,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	if(result.getErrorCount() > 0){
        	for(FieldError error:result.getFieldErrors()){
            	model.put(error.getField()+"Err", error.getDefaultMessage());
        	}
        	model.put("subSystem", subSystem);
            return "manage/sub_system/edit";
    	}
    	
    	if(!subSystem.getBoxIp().isEmpty()) {
    		if(subSystemService.countByIp(subSystem.getBoxIp()) > 0) {
        		redirectAttributes.addFlashAttribute("hasError", true);
//                redirectAttributes.addFlashAttribute("message", "解码器IP地址与其它e:cue设备IP地址冲突！");
                redirectAttributes.addFlashAttribute("message", ErrorMessage.getErrMsg(ErrorDef.ERR_ECUE_BOXIP_SAMEWTIH_OTHER_ECUEIP));
                return "redirect:/manage/sub_system/list";
        	}
    		else if(subSystemService.countByBoxIpExcept(subSystem.getBoxIp(),subSystem.getId()) > 0)
        	{
        		redirectAttributes.addFlashAttribute("hasError", true);
//                redirectAttributes.addFlashAttribute("message", "解码器IP地址与其它系统解码器IP地址冲突！");
        		redirectAttributes.addFlashAttribute("message", ErrorMessage.getErrMsg(ErrorDef.ERR_ECUE_BOXIP_SAMEWTIH_OTHER_BOXIP));
                return "redirect:/manage/sub_system/list";
        	}
    	}
    	
    	if(subSystemService.countByIpExcept(subSystem.getIp(),subSystem.getId()) > 0) {
    		redirectAttributes.addFlashAttribute("hasError", true);
//            redirectAttributes.addFlashAttribute("message", "设备IP地址与其它e:cue设备IP地址冲突！");
            redirectAttributes.addFlashAttribute("message", ErrorMessage.getErrMsg(ErrorDef.ERR_ECUE_ECUEIP_SAMEWTIH_OTHER_ECUEIP));
    	}
    	else if(subSystemService.countByBoxIp(subSystem.getIp())> 0)
    	{
    		redirectAttributes.addFlashAttribute("hasError", true);
//            redirectAttributes.addFlashAttribute("message", "设备IP地址与其它解码器IP地址冲突！");
    		redirectAttributes.addFlashAttribute("message", ErrorMessage.getErrMsg(ErrorDef.ERR_ECUE_ECUEIP_SAMEWTIH_OTHER_BOXIP));
    	}
    	else {
    		if(file.getSize() > 0) {
    			String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
    			String newFileName = BaseUtil.getStrRandom(Contants.FILE_NAME_LENGTH) + ext;
    			String path = Contants.PIC_FILE_SUB_PATH + File.separator + BaseUtil.format(new Date());
    			BaseUtil.doSaveFile(uploadFileDir + File.separator + path, file, newFileName);
    			subSystem.setPicPath(path + File.separator +  newFileName);
    		}
    		if(subSystem.getId() != null && subSystemService.update(subSystem) > 0){
//                redirectAttributes.addFlashAttribute("message", "保存成功！");
                redirectAttributes.addFlashAttribute("message", ErrorMessage.getErrMsg(ErrorDef.INFO_SAVE_SUCCESS));
        	}else{
                redirectAttributes.addFlashAttribute("hasError", true);
//                redirectAttributes.addFlashAttribute("message", "保存失败！");
                redirectAttributes.addFlashAttribute("message", ErrorMessage.getErrMsg(ErrorDef.ERR_SAVE_FAILED));
        	}
    	}
        return "redirect:/manage/sub_system/list";
    }

    @Transactional
    @PostMapping("/sub_system/{id}/delete")
    public String doDelete(@PathVariable("id") int id,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	if(fileAreaService.countBySysId(id) > 0) {
    		redirectAttributes.addFlashAttribute("hasError", true);
//            redirectAttributes.addFlashAttribute("message", "该设备被文件切割模板占用，请先调整对应模板后再操作!");
    		redirectAttributes.addFlashAttribute("message", ErrorMessage.getErrMsg(ErrorDef.ERR_ECUE_DELETE_FAIELD_EXISTEDFILESPLITTEMPLATE));
    	} else {
    		if(subSystemService.detele(id) > 0){
//                redirectAttributes.addFlashAttribute("message", "删除成功！");
    			redirectAttributes.addFlashAttribute("message", ErrorMessage.getErrMsg(ErrorDef.INFO_DELETE_SUCCESS));
        	}else{
                redirectAttributes.addFlashAttribute("hasError", true);
//                redirectAttributes.addFlashAttribute("message", "删除失败！");
                redirectAttributes.addFlashAttribute("message", ErrorMessage.getErrMsg(ErrorDef.ERR_DELETE_FAILED));
        	}
    	}
        return "redirect:/manage/sub_system/list";
    }

    /** 
     * 获取效果图 
     * @param request 
     * @return 
     */  
    @GetMapping("/sub_system/{id}/pic")
    public ResponseEntity<byte[]> getBomImg(HttpServletRequest request,HttpServletResponse response,
									    		@PathVariable Integer id) {  
    	SubSystemInfo subSystem = subSystemService.findById(id);
    	String path = uploadFileDir + File.separator + subSystem.getPicPath();
        HttpHeaders he = new HttpHeaders();
    	byte[] pic = FileTypeUtil.getBytes(path);
        try {
            FileType imgType = FileTypeUtil.getType(path);
            if(null != imgType) {
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
            }
        } catch (IOException e) {
        	LOGGER.error(e.toString());
        } catch (Exception e) {
        	LOGGER.error(e.getMessage(),e);
		}
        return new ResponseEntity<>(pic,he,HttpStatus.OK);  
    }
}
