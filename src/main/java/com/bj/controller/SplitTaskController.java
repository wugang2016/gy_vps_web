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

import com.bj.pojo.FileArea;
import com.bj.pojo.SplitSubTask;
import com.bj.pojo.SplitTask;
import com.bj.pojo.SplitTemplates;
import com.bj.pojo.SubTaskStatus;
import com.bj.pojo.TaskStatus;
import com.bj.service.DispatchTaskService;
import com.bj.service.FileAreaService;
import com.bj.service.JobService;
import com.bj.service.SplitSubTaskService;
import com.bj.service.SplitTaskService;
import com.bj.service.SplitTemplatesService;
import com.bj.service.SysParamService;
import com.bj.util.BaseUtil;
import com.bj.util.Contants;
import com.bj.util.Pagination;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/task")
public class SplitTaskController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SplitTaskController.class);

    @Resource
    private SplitTaskService splitTaskService;
    
    @Resource
    private SplitSubTaskService splitSubTaskService;
    
    @Resource
    private DispatchTaskService dispatchTaskService;

    @Resource
    private SplitTemplatesService splitTemplatesService;
    
    @Resource
    private SysParamService sysParamService ;
    
    @Resource
    private FileAreaService fileAreaService ;
    
    @Resource
    private JobService jobService ;
    
    @Value("${bijie.upload.file.path}")
    private String uploadFileDir;

    @GetMapping("/split/list")
    public String goList(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "a", defaultValue = "0") int refresh,
            @RequestParam(value = "p", defaultValue = "1") int page) {
    	int count = splitTaskService.countAll();
    	List<SplitTask> splitTasks = splitTaskService.findAll((page - 1) * Pagination.DEFAULT_PAGE_SIZE, Pagination.DEFAULT_PAGE_SIZE);
        Pagination pagination = new Pagination(request, page, count, Pagination.DEFAULT_PAGE_SIZE);
        model.put("splitTasks", splitTasks);
        model.put("pagination", pagination);
        if(refresh > 0) {
            model.put("refresh", refresh);
        }
        return "task/split/list";
    }

    @GetMapping("/split/new")
    public String goNew(Map<String, Object> model) {
    	List<SplitTemplates> templates = splitTemplatesService.findAll(0, 200);
        model.put("templates", templates);
        return "task/split/new";
    }

    @Transactional
    @PostMapping("/split/new")
    public String doNew(@Valid SplitTask splitTask,
    							Errors result,
    							Map<String, Object> model,
    				            final @RequestParam("file") MultipartFile file,
                	            @RequestParam(value = "areaJson",defaultValue = "") String areaJson,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	if(!sysParamService.validTaskPassword(splitTask.getTaskPassword())) {
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "任务密码错误！");
            redirectAttributes.addFlashAttribute("splitTask", splitTask);
            return "redirect:/task/split/new";
    	}
    	
    	if(result.getErrorCount() > 0){
        	for(FieldError error:result.getFieldErrors()){
            	model.put(error.getField()+"Err", error.getDefaultMessage());
        	}
        	List<SplitTemplates> templates = splitTemplatesService.findAll(0, 200);
            model.put("templates", templates);
        	model.put("splitTask", splitTask);
            return "task/split/new";
    	}
    	
    	if(file.getSize() <= 0) {
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "缺少切割视频文件！");
            redirectAttributes.addFlashAttribute("splitTask", splitTask);
            return "redirect:/task/split/new";
    	}else {
			//String newFileName = BaseUtil.getStrRandom(Contants.FILE_NAME_LENGTH);
			String path = Contants.VIDEO_FILE_SUB_PATH + File.separator + BaseUtil.format(new Date());
			BaseUtil.doSaveFile(uploadFileDir + File.separator + path, file, null);
			splitTask.setSrcFilePath(path + File.separator +  file.getOriginalFilename());
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
            redirectAttributes.addFlashAttribute("message", "所选模板未添加切割区域！");
            redirectAttributes.addFlashAttribute("splitTask", splitTask);
            return "redirect:/task/split/new";
    	}

    	splitTask.setStatus(TaskStatus.PENDING.index());
    	splitTask.setStartTime(BaseUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		if(splitTaskService.insert(splitTask) > 0){
			//存入子任务表
			for(int i=0; i<areaList.size(); i++) {
				FileArea area = areaList.get(i);
				SplitSubTask subTask = new SplitSubTask();
				subTask.setTaskId(splitTask.getId());
				subTask.setFileArea(area);
				if(area.getCustomFileName() != null && area.getCustomFileName().trim().length() > 0) {
					subTask.setFileName(area.getCustomFileName());
				}else {
					subTask.setFileName(splitTask.getDefaultFileName());
				}
				subTask.setStatus(SubTaskStatus.PENDING.index());
				splitSubTaskService.insert(subTask);
			}
            redirectAttributes.addFlashAttribute("message", "保存成功！");
    	}else{
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "保存失败！");
    	}
        return "redirect:/task/split/list";
    }

    @GetMapping("/split/{id}/view")
    public String goEdit(Map<String, Object> model,
            				@PathVariable("id") int id) {
    	SplitTask splitTask = splitTaskService.findById(id);
    	model.put("splitTask", splitTask);
        return "task/split/view";
    }

    @Transactional
    @PostMapping("/split/{id}/delete")
    public String doDelete(@PathVariable("id") int id,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	SplitTask task = splitTaskService.findById(id);
    	if(dispatchTaskService.countBySplitTaskId(id) == 0){
			if(splitTaskService.delete(id) > 0){
				//删除文件
				if(task != null) {
					int tid = task.getId();
					String dir = uploadFileDir + File.separator + Contants.SPLIT_FILE_SUB_PATH + File.separator + tid;
					BaseUtil.deleteDirectory(dir);
					LOGGER.info("delete dir:"+dir);
					
					String srcpath = task.getSrcFilePath();
					if(srcpath!=null && !srcpath.isEmpty()) {
						String filepath = uploadFileDir + File.separator + task.getSrcFilePath();
						BaseUtil.deleteFile(filepath);
						LOGGER.info("delete file:"+filepath);
					}
				}
				
	            redirectAttributes.addFlashAttribute("message", "删除成功！");
	    	}else{
	            redirectAttributes.addFlashAttribute("hasError", true);
	            redirectAttributes.addFlashAttribute("message", "删除失败！");
	    	}
    	}else {
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "有分发任务使用此切割任务，禁止删除！");
    	}
        return "redirect:/task/split/list";
    }

    @GetMapping("/split/{taskId}/entity")
    public @ResponseBody String getEntity(@PathVariable("taskId") int taskId,
            								HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "no-cache");
    	SplitTask splitTask = splitTaskService.findById(taskId);
    	JSONObject obj = JSONObject.fromObject(splitTask);
        return obj.toString();
    }

    @GetMapping("/split/{id}/dispatch")
    public String goDispatch(@PathVariable("id") int id,
			final RedirectAttributes redirectAttributes) throws IOException {
    	SplitTask splitTask = splitTaskService.findById(id);
    	redirectAttributes.addFlashAttribute("tz_splitTask", splitTask);
        return "redirect:/task/dispatch/new";
    }

    @PostMapping("/split/{taskId}/subTask")
    public @ResponseBody String getSubTask(@PathVariable("taskId") int taskId) throws IOException {
    	List<SplitSubTask> subTasks = splitSubTaskService.findByTaskId(taskId);
    	JSONArray obj = JSONArray.fromObject(subTasks);
        return obj.toString();
    }
    
    @PostMapping("/valid/pwd")
    public @ResponseBody String validTaskPwd(@RequestParam("taskPassword") String taskPassword) throws IOException {
        Boolean result = sysParamService.validTaskPassword(BaseUtil.md5(taskPassword));
        return "{\"valid\": " + result.toString() + "}";
    }
    
    @PostMapping("/split/{taskId}/package")
    public @ResponseBody String goPackage(@PathVariable("taskId") int taskId) throws IOException {
    	SplitTask splitTask = splitTaskService.findById(taskId);
    	List<File> fileList = new ArrayList<>();
    	File srcFile = new File(uploadFileDir + File.separator + splitTask.getSrcFilePath());
    	File splitFileDir = new File(uploadFileDir + File.separator + Contants.SPLIT_FILE_SUB_PATH + File.separator + taskId);
    	if(srcFile.exists() && splitFileDir.exists()) {
    		String zipPath = Contants.DOWNLOAD_TEMP_DIR + BaseUtil.getStrRandom(Contants.FILE_NAME_LENGTH) + ".zip";
    		fileList.add(srcFile);
    		fileList.add(splitFileDir);
    		File zipFileDir = new File(Contants.DOWNLOAD_TEMP_DIR);
    		if(!zipFileDir.exists()) {
    			zipFileDir.mkdirs();
    		}
        	return jobService.exportZipFile(fileList, zipPath).getTaskId().toString();
    	}else {
    		return "-1"; //文件已经不存在
    	}
    }
    
}
