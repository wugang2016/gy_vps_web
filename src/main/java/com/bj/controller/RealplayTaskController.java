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
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bj.pojo.FileResource;
import com.bj.pojo.PlayStatus;
import com.bj.pojo.RealplayTask;
import com.bj.pojo.SplitTemplates;
import com.bj.pojo.SubSystemInfo;
import com.bj.pojo.TaskStatus;
import com.bj.service.FileAreaService;
import com.bj.service.FileResourceService;
import com.bj.service.RealplayTaskService;
import com.bj.service.SplitSubTaskService;
import com.bj.service.SplitTemplatesService;
import com.bj.service.SubSystemService;
import com.bj.service.SysParamService;
import com.bj.util.BaseUtil;
import com.bj.util.Contants;
import com.bj.util.Pagination;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/task")
public class RealplayTaskController {
    @SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(RealplayTaskController.class);
    private static final String SUCCESS = "1";

    @Resource
    private RealplayTaskService realplayTaskService;

    @Resource
    private FileResourceService fileResourceService;
    
    @Resource
    private SplitSubTaskService splitSubTaskService;

    @Resource
    private SplitTemplatesService splitTemplatesService;
    
    @Resource
    private SysParamService sysParamService ;
    
    @Resource
    private FileAreaService fileAreaService ;
    
    @Resource
    private SubSystemService subSystemService ;
    
    @Value("${bijie.upload.file.path}")
    private String uploadFileDir;

    @GetMapping("/realplay/list")
    public String goList(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "a", defaultValue = "0") int refresh,
            @RequestParam(value = "p", defaultValue = "1") int page) {
    	//最近任务
    	int showNum = 5;
    	int count = realplayTaskService.countAll();
    	List<RealplayTask> realplayTasks = realplayTaskService.findAll(0, showNum);
        model.put("showMore?", count > showNum);
        model.put("realplayTasks", realplayTasks);
        
        //文件管理
    	count = fileResourceService.countAll();
    	List<FileResource> fileResources = fileResourceService.findAll((page - 1) * Pagination.DEFAULT_PAGE_SIZE, Pagination.DEFAULT_PAGE_SIZE);
        Pagination pagination = new Pagination(request, page, count, Pagination.DEFAULT_PAGE_SIZE);
        model.put("fileResources", fileResources);
        model.put("pagination", pagination);
        
        //default now
    	List<SplitTemplates> templates = splitTemplatesService.findDefaultTemplates();
    	templates.addAll(splitTemplatesService.findAll(0, 200));
        
        //List<SplitTemplates> templates = splitTemplatesService.findAll(0, 200);
    	List<SubSystemInfo> lists = subSystemService.findAll(0, 200);
    	model.put("subSystems", lists);
        model.put("templates", templates);
        if(refresh > 0) {
            model.put("refresh", refresh);
        }
        return "task/realplay/list";
    }

    @GetMapping("/realplay/history")
    public String goHistory(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "a", defaultValue = "0") int refresh,
            @RequestParam(value = "p", defaultValue = "1") int page) {
    	List<SubSystemInfo> lists = subSystemService.findAll(0, 200);
    	model.put("subSystems", lists);
    	int count = realplayTaskService.countAll();
    	List<RealplayTask> realplayTasks = realplayTaskService.findAll((page - 1) * Pagination.DEFAULT_PAGE_SIZE, Pagination.DEFAULT_PAGE_SIZE);
        Pagination pagination = new Pagination(request, page, count, Pagination.DEFAULT_PAGE_SIZE);
        model.put("realplayTasks", realplayTasks);
        model.put("pagination", pagination);
        if(refresh > 0) {
            model.put("refresh", refresh);
        }
        return "task/realplay/history";
    }

    @GetMapping("/realplay/new")
    public String goNew(Map<String, Object> model) {
    	//have default now
    	List<SplitTemplates> templates = splitTemplatesService.findDefaultTemplates();
    	templates.addAll(splitTemplatesService.findAll(0, 200));
    	//List<SplitTemplates> templates = splitTemplatesService.findAll(0, 200);
    	List<SubSystemInfo> lists = subSystemService.findAll(0, 200);
    	model.put("subSystems", lists);
        model.put("templates", templates);
        return "task/realplay/new";
    }

    @Transactional
    @PostMapping("/realplay/new")
    public String doNew(@Valid RealplayTask realplayTask,
    							Errors result,
    							Map<String, Object> model,
    				            final @RequestParam("file") MultipartFile file,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	if(!sysParamService.validTaskPassword(realplayTask.getTaskPassword())) {
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "任务密码错误！");
            redirectAttributes.addFlashAttribute("realplayTask", realplayTask);
            return "redirect:/task/realplay/new";
    	}
    	if(file.getSize() <= 0) {
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "缺少视频文件！");
            redirectAttributes.addFlashAttribute("realplayTask", realplayTask);
            return "redirect:/task/realplay/new";
    	}else {
			//String newFileName = BaseUtil.getStrRandom(Contants.FILE_NAME_LENGTH);
			String path = Contants.VIDEO_FILE_SUB_PATH + File.separator + BaseUtil.format(new Date());
			BaseUtil.doSaveFile(uploadFileDir + File.separator + path, file, null);
			realplayTask.getFileResource().setFilePath(path + File.separator +  file.getOriginalFilename());
			realplayTask.getFileResource().setType(0);
    	}
    	
    	Boolean goPlay = realplayTask.getFileResource().getGoPlay();
    	if(goPlay != null && goPlay.booleanValue()) {
    		//新建任务
        	realplayTask.setStatus(TaskStatus.PENDING.index());
        	realplayTask.setStartTime(BaseUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			fileResourceService.insert(realplayTask.getFileResource());
    		if(realplayTaskService.insert(realplayTask) <= 0){
                redirectAttributes.addFlashAttribute("hasError", true);
                redirectAttributes.addFlashAttribute("message", "保存失败！");
                return "redirect:/task/realplay/list";
        	}
    	}else {
			fileResourceService.insert(realplayTask.getFileResource());
    	}    	
        redirectAttributes.addFlashAttribute("message", "保存成功！");
        return "redirect:/task/realplay/list";
    }

    @GetMapping("/realplay/{id}/view")
    public String goEdit(Map<String, Object> model,
            				@PathVariable("id") int id) {
    	RealplayTask realplayTask = realplayTaskService.findById(id);
    	model.put("realplayTask", realplayTask);
        return "task/realplay/view";
    }

    @GetMapping("/realplay/{taskId}/entity")
    public @ResponseBody String getEntity(@PathVariable("taskId") int taskId,
            								HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "no-cache");
        RealplayTask realplayTask = realplayTaskService.findById(taskId);
    	JSONObject obj = JSONObject.fromObject(realplayTask);
        return obj.toString();
    }

    @Transactional
    @PostMapping("/realplay/{id}/delete")
    public String doDelete(@PathVariable("id") int id,
    							final RedirectAttributes redirectAttributes) throws IOException {
		if(realplayTaskService.delete(id) > 0){
            redirectAttributes.addFlashAttribute("message", "删除成功！");
    	}else{
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "删除失败！");
    	}
        return "redirect:/task/realplay/list";
    }

    @Transactional
    @PostMapping("/realplay/file/{id}/delete")
    public String doDeleteFile(@PathVariable("id") int id,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	if(realplayTaskService.findByFileId(id).size() <= 0){
    		FileResource file = fileResourceService.findById(id);
			if(fileResourceService.delete(id) > 0){
				if(file != null) {
					String filepath = uploadFileDir + File.separator + file.getFilePath();
					BaseUtil.deleteFile(filepath);
					LOGGER.info("delete file:"+filepath);
				}
	            redirectAttributes.addFlashAttribute("message", "删除成功！");
	    	}else{
	            redirectAttributes.addFlashAttribute("hasError", true);
	            redirectAttributes.addFlashAttribute("message", "删除失败！");
	    	}
    	}else {
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "存在播放任务，删除失败！");
    	}
        return "redirect:/task/realplay/list";
    }

    @PostMapping("/realplay/{id}/stop")
    public @ResponseBody String goStop(@PathVariable("id") int id,
            final @RequestParam("taskPassword") String taskPassword,
			final RedirectAttributes redirectAttributes) throws IOException {
    	if(!sysParamService.validTaskPassword(taskPassword)) {
            return "任务密码错误";
    	}
    	RealplayTask realplayTask = realplayTaskService.findById(id);
    	realplayTaskService.stopPlay(realplayTask);
        return SUCCESS;
    }

    @Transactional
    @PostMapping("/realplay/{id}/replay")
    public @ResponseBody String goReplay(@PathVariable("id") int id,
            final @RequestParam("repeate") Boolean repeate,
            final @RequestParam("taskPassword") String taskPassword,
            final @RequestParam(value = "subSystemIds", defaultValue = "") Integer[] subSystemIds,
			final RedirectAttributes redirectAttributes) throws IOException {
    	if(!sysParamService.validTaskPassword(taskPassword)) {
            return "任务密码错误";
    	}
    	RealplayTask realplayTask = realplayTaskService.findById(id);
    	realplayTask.setRepeate(repeate);
    	realplayTask.setSubSystemIds(subSystemIds);
    	realplayTask.setStatus(TaskStatus.PENDING.index());
    	realplayTask.setStartTime(BaseUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
    	realplayTask.setEndTime(null);
    	realplayTaskService.insert(realplayTask);
        return SUCCESS;
    }

    @Transactional
    @PostMapping("/realplay/{fileId}/goplay")
    public @ResponseBody String goPlay(@PathVariable("fileId") int fileId,
            final @RequestParam("templateId") int templateId,
            final @RequestParam("repeate") Boolean repeate,
            final @RequestParam("taskPassword") String taskPassword,
            final @RequestParam(value = "subSystemIds", defaultValue = "") Integer[] subSystemIds,
			final RedirectAttributes redirectAttributes) throws IOException {
    	if(!sysParamService.validTaskPassword(taskPassword)) {
            return "任务密码错误";
    	}
    	FileResource fileResource = fileResourceService.findById(fileId);
    	SplitTemplates splitTemplates = splitTemplatesService.findById(templateId);
    	RealplayTask realplayTask = new RealplayTask();
    	realplayTask.setFileResource(fileResource);
    	realplayTask.setSplitTemplate(splitTemplates);
    	realplayTask.setRepeate(repeate);
    	realplayTask.setSubSystemIds(subSystemIds);
    	realplayTask.setStartTime(BaseUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
    	realplayTask.setStatus(PlayStatus.PLAYING.index());
    	realplayTaskService.insert(realplayTask);
        return SUCCESS;
    }
}
