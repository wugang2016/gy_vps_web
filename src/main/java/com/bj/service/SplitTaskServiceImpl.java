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
import com.bj.dao.mapper.SplitTaskMapper;
import com.bj.pojo.SplitTask;
import com.bj.util.Contants;

/**
 * @author LQK
 *
 */
@Service
public class SplitTaskServiceImpl implements SplitTaskService {

    @Resource
    private SplitTaskMapper splitTaskMapper;
    
    @Resource
    private SplitSubTaskMapper splitSubTaskMapper;
    
    @Resource
    private SendMessageService sendMessageService;
    
    @Value("${bijie.upload.file.path}")
    private String uploadFileDir;
    
	@Override
	public SplitTask findById(int id) {
		return splitTaskMapper.findById(id);
	}

	@Override
	public List<SplitTask> findAll(int offset, int rowCount) {
		return splitTaskMapper.findAll(offset, rowCount);
	}

	@Override
	public int insert(SplitTask splitTask) {
		int result = splitTaskMapper.insert(splitTask);
		if(result > 0) {
			File dir = new File(uploadFileDir + File.separator + Contants.SPLIT_FILE_SUB_PATH + File.separator + splitTask.getId());
			if(!dir.exists()) {
				dir.mkdirs();
			}
			sendMessageService.onlySendMessage(splitTask.format());
		}
		return result;
	}

	@Override
	public int countAll() {
		return splitTaskMapper.countAll();
	}

	@Override
	public int delete(int id) {
		return splitSubTaskMapper.deleteByTaskId(id) * splitTaskMapper.delete(id);
	}

	@Override
	public int countByTemplateId(int templateId) {
		return splitTaskMapper.countByTemplateId(templateId);
	}
    

}
