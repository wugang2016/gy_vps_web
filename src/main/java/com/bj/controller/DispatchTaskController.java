package com.bj.controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bj.pojo.DispatchSubTask;
import com.bj.pojo.DispatchTask;
import com.bj.pojo.SplitSubTask;
import com.bj.pojo.SplitTask;
import com.bj.pojo.SubTaskStatus;
import com.bj.pojo.TaskStatus;
import com.bj.service.DispatchSubTaskService;
import com.bj.service.DispatchTaskService;
import com.bj.service.FileAreaService;
import com.bj.service.SplitTaskService;
import com.bj.service.SysParamService;
import com.bj.util.BaseUtil;
import com.bj.util.Pagination;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/task")
public class DispatchTaskController {
    @SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(DispatchTaskController.class);

    @Resource
    private DispatchTaskService dispatchTaskService;
    
    @Resource
    private DispatchSubTaskService dispatchSubTaskService;

    @Resource
    private SplitTaskService splitTaskService;
    
    @Resource
    private SysParamService sysParamService ;
    
    @Resource
    private FileAreaService fileAreaService ;
    
    @Value("${bijie.upload.file.path}")
    private String uploadFileDir;

    @GetMapping("/dispatch/list")
    public String goList(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "a", defaultValue = "0") int refresh,
            @RequestParam(value = "p", defaultValue = "1") int page) {
    	int count = dispatchTaskService.countAll();
    	List<DispatchTask> dispatchTasks = dispatchTaskService.findAll((page - 1) * Pagination.DEFAULT_PAGE_SIZE, Pagination.DEFAULT_PAGE_SIZE);
        Pagination pagination = new Pagination(request, page, count, Pagination.DEFAULT_PAGE_SIZE);
        model.put("dispatchTasks", dispatchTasks);
        model.put("pagination", pagination);
        if(refresh > 0) {
            model.put("refresh", refresh);
        }
        return "task/dispatch/list";
    }

    @GetMapping("/dispatch/new")
    public String goNew(Map<String, Object> model) {
    	List<SplitTask> splitTasks = splitTaskService.findAll(0, 200);
        model.put("splitTasks", splitTasks);
        return "task/dispatch/new";
    }

    @Transactional
    @PostMapping("/dispatch/new")
    public String doNew(@Valid DispatchTask dispatchTask,
    							Errors result,
    							Map<String, Object> model,
                	            @RequestParam(value = "splitSubTaskJson",defaultValue = "") String splitSubTaskJson,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	if(!sysParamService.validTaskPassword(dispatchTask.getTaskPassword())) {
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "任务密码错误！");
            redirectAttributes.addFlashAttribute("dispatchTask", dispatchTask);
            redirectAttributes.addFlashAttribute("splitSubTaskJson", splitSubTaskJson);
            return "redirect:/task/dispatch/new";
    	}
    	
    	if(result.getErrorCount() > 0){
        	for(FieldError error:result.getFieldErrors()){
            	model.put(error.getField()+"Err", error.getDefaultMessage());
        	}
        	List<SplitTask> splitTasks = splitTaskService.findAll(0, 200);
            model.put("splitTasks", splitTasks);
        	model.put("dispatchTask", dispatchTask);
        	model.put("splitSubTaskJson", splitSubTaskJson);
            return "task/dispatch/new";
    	}
    	
		List<SplitSubTask> dispatchSubTaskList = new ArrayList<SplitSubTask>();
    	if(splitSubTaskJson != null && splitSubTaskJson.length() > 10) {
    		JSONArray jsonArray = JSONArray.fromObject(splitSubTaskJson);
            for(int i=0 ; i<jsonArray.size(); i++){
            	JSONObject jsonObject = jsonArray.getJSONObject(i);  
            	SplitSubTask splitSubTask = (SplitSubTask)JSONObject.toBean(jsonObject,SplitSubTask.class);  
            	dispatchSubTaskList.add(splitSubTask);
            }
    	}else {
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "至少选择一条下发任务！");
            redirectAttributes.addFlashAttribute("dispatchTask", dispatchTask);
            redirectAttributes.addFlashAttribute("splitSubTaskJson", splitSubTaskJson);
            return "redirect:/task/dispatch/new";
    	}

    	dispatchTask.setStatus(TaskStatus.PENDING.index());
    	dispatchTask.setStartTime(BaseUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		if(dispatchTaskService.insert(dispatchTask) > 0){
			//存入子任务表
			for(int i=0; i<dispatchSubTaskList.size(); i++) {
				SplitSubTask splitSubTask = dispatchSubTaskList.get(i);
				DispatchSubTask subTask = new DispatchSubTask();
				subTask.setTaskId(dispatchTask.getId());
				subTask.setSubSystem(splitSubTask.getFileArea().getSubSystem());
				subTask.setSplitSubTask(splitSubTask);
				subTask.setStatus(SubTaskStatus.PENDING.index());
				dispatchSubTaskService.insert(subTask);
			}
            redirectAttributes.addFlashAttribute("message", "保存成功！");
    	}else{
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "保存失败！");
    	}
        return "redirect:/task/dispatch/list";
    }

    @GetMapping("/dispatch/{id}/view")
    public String goEdit(Map<String, Object> model,
            				@PathVariable("id") int id) {
    	DispatchTask dispatchTask = dispatchTaskService.findById(id);
    	model.put("dispatchTask", dispatchTask);
        return "task/dispatch/view";
    }

    @GetMapping("/dispatch/{taskId}/entity")
    public @ResponseBody String getEntity(@PathVariable("taskId") int taskId,
            								HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "no-cache");
        DispatchTask dispatchTask = dispatchTaskService.findById(taskId);
    	JSONObject obj = JSONObject.fromObject(dispatchTask);
        return obj.toString();
    }

    @Transactional
    @PostMapping("/dispatch/{id}/delete")
    public String doDelete(@PathVariable("id") int id,
    							final RedirectAttributes redirectAttributes) throws IOException {    	
		if(dispatchTaskService.delete(id) > 0){
            redirectAttributes.addFlashAttribute("message", "删除成功！");
    	}else{
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "删除失败！");
    	}
        return "redirect:/task/dispatch/list";
    }

    @PostMapping("/dispatch/{taskId}/subTask")
    public @ResponseBody String getAreas(@PathVariable("taskId") int taskId) throws IOException {
    	List<DispatchSubTask> subTasks = dispatchSubTaskService.findByTaskId(taskId);
    	JSONArray obj = JSONArray.fromObject(subTasks);
        return obj.toString();
    }

    @Transactional
    @PostMapping("/dispatch/{taskId}/failedAgain")
    public @ResponseBody String dispatchFailedAgain(@PathVariable("taskId") int taskId,
            final @RequestParam("taskPassword") String taskPassword) throws IOException {
    	if(!sysParamService.validTaskPassword(taskPassword)) {
            return "任务密码错误";
    	}
    	DispatchTask dispatchTask = dispatchTaskService.findById(taskId);
    	List<DispatchSubTask> dispatchSubTaskList = dispatchSubTaskService.findByTaskId(taskId);
    	dispatchTask.setStatus(TaskStatus.PENDING.index());
    	dispatchTask.setStartTime(BaseUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
    	dispatchTask.setEndTime(null);
		if(dispatchTaskService.insert(dispatchTask) > 0){
			//存入子任务表
			for(int i=0; i<dispatchSubTaskList.size(); i++) {
				DispatchSubTask subTask = dispatchSubTaskList.get(i);
				if(subTask.getIsFail()) {
					subTask.setTaskId(dispatchTask.getId());
					subTask.setStatus(SubTaskStatus.PENDING.index());
					dispatchSubTaskService.insert(subTask);
				}
			}
    	}
        return "1";
    }
    
    @Transactional
    @PostMapping("/dispatch/{taskId}/again")
    public @ResponseBody String dispatchAgain(@PathVariable("taskId") int taskId,
            final @RequestParam("taskPassword") String taskPassword) throws IOException {
    	if(!sysParamService.validTaskPassword(taskPassword)) {
            return "任务密码错误";
    	}
    	DispatchTask dispatchTask = dispatchTaskService.findById(taskId);
    	List<DispatchSubTask> dispatchSubTaskList = dispatchSubTaskService.findByTaskId(taskId);
    	dispatchTask.setStatus(TaskStatus.PENDING.index());
    	dispatchTask.setStartTime(BaseUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
    	dispatchTask.setEndTime(null);
		if(dispatchTaskService.insert(dispatchTask) > 0){
			//存入子任务表
			for(int i=0; i<dispatchSubTaskList.size(); i++) {
				DispatchSubTask subTask = dispatchSubTaskList.get(i);
				subTask.setTaskId(dispatchTask.getId());
				subTask.setStatus(SubTaskStatus.PENDING.index());
				dispatchSubTaskService.insert(subTask);
			}
    	}
        return "1";
    }
}
