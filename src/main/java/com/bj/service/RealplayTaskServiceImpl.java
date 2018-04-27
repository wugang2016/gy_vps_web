/**
 * 
 */
package com.bj.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bj.dao.mapper.RealplayTaskMapper;
import com.bj.pojo.RealplayTask;

/**
 * @author LQK
 *
 */
@Service
public class RealplayTaskServiceImpl implements RealplayTaskService {
    private static final String OPT_PLAY = "start_file_realplay_task";
    private static final String OPT_STOP = "stop_file_realplay_task";

    @Resource
    private RealplayTaskMapper realplayTaskMapper;

    @Resource
    private SendMessageService sendMessageService;

	@Override
	public RealplayTask findById(int id) {
		return realplayTaskMapper.findById(id);
	}

	@Override
	public List<RealplayTask> findAll(int offset, int rowCount) {
		return realplayTaskMapper.findAll(offset, rowCount);
	}

	@Override
	public int insert(RealplayTask realplayTask) {
		int result = realplayTaskMapper.insert(realplayTask);
		if(result > 0) {
			sendMessageService.onlySendMessage(realplayTask.format(OPT_PLAY));
		}
		return result;
	}

	@Override
	public int update(RealplayTask realplayTask) {
		int result = realplayTaskMapper.update(realplayTask);
		if(result > 0) {
			sendMessageService.onlySendMessage(realplayTask.format(OPT_PLAY));
		}
		return result;
	}

	@Override
	public int countAll() {
		return realplayTaskMapper.countAll();
	}

	@Override
	public int delete(int id) {
		return realplayTaskMapper.delete(id);
	}

	@Override
	public int countByTemplateId(int templateId) {
		return realplayTaskMapper.countByTemplateId(templateId);
	}

	@Override
	public List<RealplayTask> findByFileId(int fileId) {
		return realplayTaskMapper.findByFileId(fileId);
	}

	@Override
	public void stopPlay(RealplayTask realplayTask) {
		sendMessageService.onlySendMessage(realplayTask.format(OPT_STOP));
	}
	
}
