/**
 * 
 */
package com.bj.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bj.pojo.SysUser;
import com.bj.service.SysUserService;
import com.bj.util.BaseUtil;

/**
 * @author LQK
 *
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);
	private static final String LOGIN_NAME = "admin";

    @Resource
    private SysUserService sysUserService;
    
    @GetMapping(value= {"","/","/login","/setML","/setPwd","/setDoPwd"})
    public String goLogin(Map<String, Object> model) {
        return "admin/login";
    }
    
    @PostMapping("/login")
    public String doLogin(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "vrifyCode") String vrifyCode) {
    	String captchaId = (String) request.getSession().getAttribute("vrifyCode");    
        LOGGER.info("vrifyCode: {}/{}", vrifyCode, captchaId);
        if(captchaId.equals(vrifyCode)) {  
        	SysUser sysUser = sysUserService.findByUsername(LOGIN_NAME);
        	if(sysUser != null && BaseUtil.md5(password).equals(sysUser.getPassword())){
                LOGGER.info("成功登录管理页面");
            	return "admin/set";
        	}else {
            	model.put("hasError", true);
            	model.put("message", "错误的密码"); 
            	return "admin/login";
        	}
        }else{
        	model.put("hasError", true);
        	model.put("message", "错误的验证码"); 
        	return "admin/login";
        }
    }
    
    @PostMapping("/setPwd")
    public String setPwd(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "oldPassword") String oldPassword,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "password2") String password2) {
    	if(oldPassword != null && oldPassword.trim().length() > 0 && oldPassword.equals(password)) {
        	model.put("hasError", true);
        	model.put("message", "新密码不能与旧密码相同"); 
        	return "admin/set";
    	}
    	
    	if(password == null || password.trim().length() == 0 || !password.equals(password2)) {
        	model.put("hasError", true);
        	model.put("message", "新密码与重复新密码不相同"); 
        	return "admin/set";
    	}
    	SysUser sysUser = sysUserService.findByUsername(LOGIN_NAME);
    	if(sysUser != null && BaseUtil.md5(oldPassword).equals(sysUser.getPassword())) {
    		sysUserService.updatePassword(BaseUtil.md5(password));
        	model.put("message", "修改管理员密码成功"); 
    	}else {
        	model.put("hasError", true);
        	model.put("message", "错误的旧密码"); 
    	}
    	return "admin/set";
    }
    
    @PostMapping("/setDoPwd")
    public String setDoPwd(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "oldDoPassword") String oldDoPassword,
            @RequestParam(value = "doPassword") String doPassword,
            @RequestParam(value = "doPassword2") String doPassword2) {
    	if(oldDoPassword != null && oldDoPassword.trim().length() > 0 && oldDoPassword.equals(doPassword)) {
        	model.put("hasError", true);
        	model.put("message", "新密码不能与旧密码相同"); 
        	return "admin/set";
    	}
    	if(doPassword == null || doPassword.trim().length() == 0 || !doPassword.equals(doPassword2)) {
        	model.put("hasError", true);
        	model.put("message", "新密码与重复新密码不相同"); 
        	return "admin/set";
    	}
    	SysUser sysUser = sysUserService.findByUsername(LOGIN_NAME);
    	if(sysUser != null && BaseUtil.md5(oldDoPassword).equals(sysUser.getDoPassword())) {
    		sysUserService.updateDoPassword(BaseUtil.md5(doPassword));
        	model.put("message", "修改任务密码成功"); 
    	}else {
        	model.put("hasError", true);
        	model.put("message", "错误的旧密码"); 
    	}
    	return "admin/set";
    }
}
