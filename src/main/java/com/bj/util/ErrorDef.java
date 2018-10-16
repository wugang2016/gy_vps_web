package com.bj.util;

public class ErrorDef {
	/*
	 * 
#admin page error
-4000=新密码不能与旧密码相同
-4001=新密码与重复新密码不相同
-4002=旧密码错误
-4003=缺少License文件
-4004=用户名或密码错误
-4005=验证码错误

#android real tempalte page error
-4100=签名界面效果图缺失
-4101=签名界面签名框图片缺失
-4102=签名界面背景视频缺失
-4103=至少新增一条切割区域
-4104=删除对应切割区域失败

#dispatch task page error
-4200=至少选择一个下发子任务

#file realplay task page error
-4300=缺少视频文件

#file split task page error
-4400=分发任务正使用该切割任务，禁止删除
-4401=切割任务缺少视频文件
-4402=所选切割模板未添加切割区域

#file split template page err
-4500=文件切割模板至少需定义一个切割区域
-4501=切割任务使用此模板，禁止删除
-4502=删除对应切割区域失败

#ecue conf page error
-4600=解码器IP地址与其它e:cue设备IP地址冲突
-4601=解码器IP地址与其它解码器IP地址冲突
-4602=e:cue设备IP地址与其它e:cue设备IP地址冲突
-4603=e:cue设备IP地址与其它解码器IP地址冲突

#common page error
-10001=参数不可为空
-10002=任务密码错误
-10003=文件不能为空
-10004=保存失败
-10005=删除失败

#come page info
1=修改成功
2=上传成功
3=保存成功
4=删除成功
	 */
	//admin page error
	public static final int ERR_ADMIN_PASS_ERR_SAMEWTIHOLD = -4000;
	public static final int ERR_ADMIN_PASS_ERR_NOTSAMEWTIHREPATE = -4001;
	public static final int ERR_ADMIN_PASS_ERR_OLDPASS_ERR = -4002;
	public static final int ERR_ADMIN_NO_LICENSE_FILE = -4003;
	public static final int ERR_LOGIN_USER_OR_PASS_ERR = -4004;
	public static final int ERR_LOGIN_VERIFY_CODE_ERR = -4005;
	
	//android real tempalte page error
	public static final int ERR_ANDROID_REAL_TEMPLATE_NO_BACKPIC = -4100;
	public static final int ERR_ANDROID_REAL_TEMPLATE_NO_LINEPIC = -4101;
	public static final int ERR_ANDROID_REAL_TEMPLATE_NO_BACKVIDEO = -4102;
	public static final int ERR_ANDROID_REAL_TEMPLATE_NO_SPLIT_AREA = -4103;
	public static final int ERR_ANDROID_REAL_TEMPLATE_DELETE_SPLIT_AREA_FAILED = -4104;
	public static final int ERR_ANDROID_REAL_TEMPLATE_DELETE_FAILED_EXITSE_REALTIME_TASK = -4105;

	
	//dispatch task page error
	public static final int ERR_DISPATCH_TASK_NO_SUBTASK = -4200;
	
	//file realplay task page error
	public static final int ERR_FILE_REALPLAY_NEW_NO_VIDEO = -4300;
	public static final int ERR_FILE_REALPLAY_VIDEO_DELETED = -4301;
	public static final int ERR_FILE_REALPLAY_TEMPLATE_DELETED = -4302;
	
	//file split task page err
	public static final int ERR_FILE_SPLIT_TASK_DELETE_FAILED_EXISTEDDISPATCHTASK = -4400;
	public static final int ERR_FILE_SPLIT_TASK_NEW_FAILED_NOVIDEO = -4401;
	public static final int ERR_FILE_SPLIT_TASK_NEW_FAILED_TEMPLATE_NO_AREA = -4402;
	
	//file split template page err
	public static final int ERR_FILE_SPLIT_TEMPLATE_NO_SPLIT_AREA = -4500;
	public static final int ERR_FILE_SPLIT_TEMPLATE_DELETE_FAILED_EXISTEDFILESPLITTASK =-4501;
	public static final int ERR_FILE_SPLIT_TEMPLATE_DELETE_SPLIT_AREA_FAILED =-4502;
	
	//ecue page err
	public static final int ERR_ECUE_BOXIP_SAMEWTIH_OTHER_ECUEIP = -4600;
	public static final int ERR_ECUE_BOXIP_SAMEWTIH_OTHER_BOXIP = -4601;
	public static final int ERR_ECUE_ECUEIP_SAMEWTIH_OTHER_ECUEIP = -4602;
	public static final int ERR_ECUE_ECUEIP_SAMEWTIH_OTHER_BOXIP = -4603;
	public static final int ERR_ECUE_DELETE_FAIELD_EXISTEDFILESPLITTEMPLATE = -4604;
	
	//common err
	public static final int ERR_PARA_NULL = -10001;
	public static final int ERR_TASK_PASS_ERROR =-10002;
	public static final int ERR_FILE_CANNOT_NULL = -10003;
	public static final int ERR_SAVE_FAILED = -10004;
	public static final int ERR_DELETE_FAILED = -10005;
	
	//common info
	public static final int INFO_MODIFY_SUCCESS = 1;
	public static final int INFO_UPLADE_SUCCESS = 2;
	public static final int INFO_SAVE_SUCCESS = 3;
	public static final int INFO_DELETE_SUCCESS = 4;

}
