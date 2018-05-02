/**
 * 
 */
package com.bj.service;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bj.dao.mapper.SplitSubTaskMapper;
import com.bj.pojo.SplitSubTask;
import com.bj.util.Contants;

/**
 * @author LQK
 *
 */
@Service
public class SplitSubTaskServiceImpl implements SplitSubTaskService {

    @Resource
    private SplitSubTaskMapper splitSubTaskMapper;
    
    @Value("${bijie.upload.file.path}")
    private String uploadFileDir;

	@Override
	public SplitSubTask findById(int id) {
		return splitSubTaskMapper.findById(id);
	}

	@Override
	public List<SplitSubTask> findAll(int offset, int rowCount) {
		return splitSubTaskMapper.findAll(offset,rowCount);
	}

	@Override
	public int insert(SplitSubTask SplitSubTask) {
		int result = splitSubTaskMapper.insert(SplitSubTask);
		
		String ip = SplitSubTask.getFileArea().getSubSystem().getIp();
		ip = ip.replaceAll("//.", "_");
		if(result > 0) {
			File dir = new File(uploadFileDir + File.separator + Contants.SPLIT_FILE_SUB_PATH + File.separator + SplitSubTask.getId() +'_' +ip);
			if(!dir.exists()) {
				dir.mkdirs();
			}
			//sendMessageService.onlySendMessage(splitTask.format());
		}
		
		return result;
	}

	@Override
	public int countAll() {
		return splitSubTaskMapper.countAll();
	}

	@Override
	public int delete(int id) {
		return splitSubTaskMapper.delete(id);
	}

	@Override
	public List<SplitSubTask> findByTaskId(int taskId) {
		return splitSubTaskMapper.findByTaskId(taskId);
	}

}
