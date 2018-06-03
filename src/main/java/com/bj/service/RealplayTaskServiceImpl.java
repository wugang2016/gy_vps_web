/**
 * 
 */
package com.bj.service;

import java.io.File;
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
    private JobService sendMessageService;

	@Override
	public RealplayTask findById(int id) {
		return realplayTaskMapper.findById(id);
	}

	@Override
	public List<RealplayTask> findAll(int order, String sort, int offset, int rowCount) {
		sort = "up".equals(sort)?"asc":"desc";
		String property = "task_id";
		switch (order) {
		case 1:
			property = "SUBSTRING_INDEX(b.file_path,'" + (File.separator.equals("\\")?"\\\\":File.separator) + "',-1)"; //file
			break;
		case 2:
			property = "c.name"; //split_template
			break;
		case 3:
			property = "status";
			break;
		case 4:
			property = "error_code";
			break;
		case 5:
			property = "repeate";
			break;
		case 6:
			property = "max_play_time";
			break;
		case 7:
			property = "start_time";
			break;
		default:
			break;
		}
		if(order == 1 || order == 2) {
			return realplayTaskMapper.findAllByOrderGLB(property, sort, offset, rowCount);
		}else {
			return realplayTaskMapper.findAllByOrder(property, sort, offset, rowCount);
		}
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
		return realplayTaskMapper.update(realplayTask);
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
