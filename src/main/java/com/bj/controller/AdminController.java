/**
 * 
 */
package com.bj.controller;

import java.io.IOException;
import java.net.SocketException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bj.pojo.SysUser;
import com.bj.service.SysParamService;
import com.bj.service.SysUserService;
import com.bj.util.BaseUtil;
import com.bj.util.Contants;
import com.bj.util.ErrorDef;
import com.bj.util.ErrorMessage;

/**
 * @author LQK
 *
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);
	private static final String LOGIN_NAME = "admin";
	private static final String LOGIN_PASS = "login_pass";

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysParamService sysParamService;
    
    @Value("${bijie.upload.file.path}")
    private String uploadFileDir;
    
    @Value("${bijie.vps.home.path}")
    private String vpsHomeDir;
    
    @GetMapping(value= {"","/","/login","/setML","/setPwd","/setDoPwd"})
    public String goLogin(HttpServletRequest request) {
		//request.getSession().removeAttribute(LOGIN_PASS);
		//request.getSession().removeAttribute(Contants.SESSION_COMMON_KEY);
        return "admin/login_old";
    }
    
    @PostMapping({"/login","login_old"})
    public String doLogin(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "vrifyCode") String vrifyCode) {
    	String captchaId = (String) request.getSession().getAttribute("vrifyCode");    
        LOGGER.info("vrifyCode: {}/{}", vrifyCode, captchaId);
        if(captchaId != null && captchaId.equals(vrifyCode)) {  
        	SysUser sysUser = sysUserService.findByUsername(username);
        	if(sysUser != null && BaseUtil.md5(password).equals(sysUser.getPassword())){
                LOGGER.info("成功登录高级设置页面");
        		request.getSession().setAttribute(Contants.SESSION_COMMON_KEY, sysUser);
        		request.getSession().setAttribute(LOGIN_PASS, true);
        		request.getSession().setAttribute("vrifyCode","");  
                return "redirect:/task/split/list";
        	}else {
            	model.put("hasError", true);
//            	model.put("message", "错误的用户名或密码"); 
            	model.put("message", ErrorMessage.getErrMsg(ErrorDef.ERR_LOGIN_USER_OR_PASS_ERR)); 
            	
            	return "admin/login_old";
        	}
        }else{
        	model.put("hasError", true);
//        	model.put("message", "错误的验证码"); 
        	model.put("message", ErrorMessage.getErrMsg(ErrorDef.ERR_LOGIN_VERIFY_CODE_ERR)); 
        	return "admin/login_old";
        }
    }
    
    @GetMapping("/logout")
    public String doLogout(Map<String, Object> model,
            HttpServletRequest request) {
		request.getSession().removeAttribute(LOGIN_PASS);
		request.getSession().removeAttribute(Contants.SESSION_COMMON_KEY);
        return "admin/login_old";
	}
    
    @GetMapping("/set")
    public String goSet(Map<String, Object> model,
            HttpServletRequest request) {
        loginedInit(model);
        return "admin/set";
	}
    
    @PostMapping("/setML")
    public String setML(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "b1080") String b1080,
            @RequestParam(value = "b720") String b720,
            @RequestParam(value = "b4cif") String b4cif) {
    	//if(!isLogin(request)) {return "admin/login";}
    	if(BaseUtil.isEmpty(b1080) || BaseUtil.isEmpty(b720) || BaseUtil.isEmpty(b4cif)) {
        	model.put("hasError", true);
//        	model.put("message", "参数不可为空"); 
        	model.put("message", ErrorMessage.getErrMsg(ErrorDef.ERR_PARA_NULL)); 
        	return "admin/set";
    	}
		updateAndSendMsg(Contants.KEY_BITERATE_1080P, b1080);
		updateAndSendMsg(Contants.KEY_BITERATE_720P, b720);
		updateAndSendMsg(Contants.KEY_BITERATE_4CIF, b4cif);
//    	model.put("message", "修改成功"); 
    	model.put("message", ErrorMessage.getErrMsg(ErrorDef.INFO_MODIFY_SUCCESS)); 
    	loginedInit(model);
    	return "admin/set";
    }
    
    @PostMapping("/setAndroidTsMode")
    public String setAndroidTsMode(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "tsModeLeftwidth") String tsModeLeftwidth,
            @RequestParam(value = "tsModeShiftwidth") String tsModeShiftwidth) {
    	//if(!isLogin(request)) {return "admin/login";}
    	if(BaseUtil.isEmpty(tsModeLeftwidth) || BaseUtil.isEmpty(tsModeShiftwidth)) {
        	model.put("hasError", true);
//        	model.put("message", "参数不可为空"); 
        	model.put("message", ErrorMessage.getErrMsg(ErrorDef.ERR_PARA_NULL)); 
        	return "admin/set";
    	}
		updateAndSendMsg(Contants.KEY_ANDROID_TSMODE_LEFTWIDTH, tsModeLeftwidth);
		updateAndSendMsg(Contants.KEY_ANDROID_TSMODE_SHIFTWIDTH, tsModeShiftwidth);
//    	model.put("message", "修改成功"); 
    	model.put("message", ErrorMessage.getErrMsg(ErrorDef.INFO_MODIFY_SUCCESS)); 
    	loginedInit(model);
    	return "admin/set";
    }
    
    @PostMapping("/reboot")
    public @ResponseBody String reboot(Map<String, Object> model,
    		HttpServletRequest request)
    {
    	//if(!isLogin(request)) {return "admin/login";}
    	return BaseUtil.exec("reboot");
    }
    
    @PostMapping("/setPwd")
    public String setPwd(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "oldPassword") String oldPassword,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "password2") String password2) {
    	//if(!isLogin(request)) {return "admin/login";}
    	if(oldPassword != null && oldPassword.trim().length() > 0 && oldPassword.equals(password)) {
        	model.put("hasError", true);
//        	model.put("message", "新密码不能与旧密码相同"); 
        	model.put("message", ErrorMessage.getErrMsg(ErrorDef.ERR_ADMIN_PASS_ERR_SAMEWTIHOLD)); 
        	return "admin/set";
    	}
    	
    	if(password == null || password.trim().length() == 0 || !password.equals(password2)) {
        	model.put("hasError", true);
//        	model.put("message", "新密码与重复新密码不相同"); 
        	model.put("message", ErrorMessage.getErrMsg(ErrorDef.ERR_ADMIN_PASS_ERR_NOTSAMEWTIHREPATE)); 
        	return "admin/set";
    	}
    	SysUser sysUser = sysUserService.findByUsername(LOGIN_NAME);
    	if(sysUser != null && BaseUtil.md5(oldPassword.trim()).equals(sysUser.getPassword())) {
    		sysUserService.updatePassword(BaseUtil.md5(password.trim()));
//        	model.put("message", "修改成功"); 
        	model.put("message", ErrorMessage.getErrMsg(ErrorDef.INFO_MODIFY_SUCCESS)); 
    	}else {
        	model.put("hasError", true);
//        	model.put("message", "错误的旧密码"); 
        	model.put("message", ErrorMessage.getErrMsg(ErrorDef.ERR_ADMIN_PASS_ERR_OLDPASS_ERR)); 
    	}
    	return "admin/set";
    }
    
    @PostMapping("/setTaskPwd")
    public String setTaskPwd(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "oldTaskPassword") String oldTaskPassword,
            @RequestParam(value = "taskPassword") String taskPassword,
            @RequestParam(value = "taskPassword2") String taskPassword2) {
    	//if(!isLogin(request)) {return "admin/login";}
    	if(oldTaskPassword != null && oldTaskPassword.trim().length() > 0 && oldTaskPassword.equals(taskPassword)) {
        	model.put("hasError", true);
//        	model.put("message", "新密码不能与旧密码相同"); 
        	model.put("message", ErrorMessage.getErrMsg(ErrorDef.ERR_ADMIN_PASS_ERR_SAMEWTIHOLD)); 
        	return "admin/set";
    	}
    	if(taskPassword == null || taskPassword.trim().length() == 0 || !taskPassword.equals(taskPassword2)) {
        	model.put("hasError", true);
//        	model.put("message", "新密码与重复新密码不相同"); 
        	model.put("message", ErrorMessage.getErrMsg(ErrorDef.ERR_ADMIN_PASS_ERR_NOTSAMEWTIHREPATE)); 
        	return "admin/set";
    	}
    	String value = sysParamService.findByKey(Contants.KEY_TASK_PASSWORD);
    	if(BaseUtil.md5(oldTaskPassword.trim()).equals(value)) {
    		updateAndSendMsg(Contants.KEY_TASK_PASSWORD, BaseUtil.md5(taskPassword.trim()));
//        	model.put("message", "修改成功"); 
    		model.put("message", ErrorMessage.getErrMsg(ErrorDef.INFO_MODIFY_SUCCESS)); 
    	}else {
        	model.put("hasError", true);
//        	model.put("message", "错误的旧密码"); 
        	model.put("message", ErrorMessage.getErrMsg(ErrorDef.ERR_ADMIN_PASS_ERR_OLDPASS_ERR)); 
    	}
    	return "admin/set";
    }
    
    @PostMapping("/upload_license")
    public String upload(Map<String, Object> model,
            HttpServletRequest request,
            final @RequestParam("file") MultipartFile file) throws IOException {
    	//if(!isLogin(request)) {return "admin/login";}
    	if(file.getSize() <= 0) {
    		model.put("hasError", true);
//    		model.put("message", "缺少License文件");
    		model.put("message", ErrorMessage.getErrMsg(ErrorDef.ERR_ADMIN_NO_LICENSE_FILE)); 
        	return "admin/set";
    	}else {
			//BaseUtil.doSaveFile(vpsHomeDir + File.separator + Contants.LICENSE_FILE_SUB_PATH, file, null);
    		BaseUtil.doSaveFile(vpsHomeDir, file, null);
    	}
//    	model.put("message", "上传成功"); 
    	model.put("message", ErrorMessage.getErrMsg(ErrorDef.INFO_UPLADE_SUCCESS)); 
    	return "admin/set";
    }

    /**
     * 同步获取License
     * @param request
     * @return
     */
    @Value("${bijie.send.udp.ip}")
    private String ip;
    @Value("${bijie.send.udp.port}")
    private int port;
    @PostMapping("/loadSQ")
    public @ResponseBody String loadSQ(HttpServletRequest request) {
    	if(!isLogin(request)) {return "-1";}
    	try {
    		return BaseUtil.udpSend(ip, port, "{\"opt\":\"query_license_info\"}");
		} catch (SocketException e) {
        	LOGGER.error(e.getMessage(),e);
		} catch (IOException e) {
        	LOGGER.error(e.getMessage(),e);
		}
    	return "{\"opt\":\"query_license_info_rsp\",\"active\":0,\"expire_time\":\"2018-10-30 23:25:00\",\"file_split\":0,\"file_realtime_play\":0,\"android_realtime_play\":0,\"uuid\":\"xxxx-xxxx\"}";
    }
    /**
     * 
     * @param model
     */
    private void loginedInit(Map<String, Object> model) {
        model.put("b1080", sysParamService.findByKey(Contants.KEY_BITERATE_1080P));
        model.put("b720", sysParamService.findByKey(Contants.KEY_BITERATE_720P));
        model.put("b4cif", sysParamService.findByKey(Contants.KEY_BITERATE_4CIF));
        model.put("tsModeLeftwidth", sysParamService.findByKey(Contants.KEY_ANDROID_TSMODE_LEFTWIDTH));
        model.put("tsModeShiftwidth", sysParamService.findByKey(Contants.KEY_ANDROID_TSMODE_SHIFTWIDTH));
    }
    
    /**
     * 登陆验证
     * @param request
     * @return
     */
    private boolean isLogin(HttpServletRequest request) {
    	Object obj = request.getSession().getAttribute(LOGIN_PASS);
    	return (obj != null && Boolean.parseBoolean(obj.toString()));
    }
    
    /**
     * 
     * @param key
     * @param value
     */
    private void updateAndSendMsg(String key, String value) {
		sysParamService.updateValue(key,value);
    }
    
    @GetMapping("/about")
    public String goAbout(Map<String, Object> model,
            HttpServletRequest request) {
        return "admin/about";
	}
}
