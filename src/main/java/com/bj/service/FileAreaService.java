/**
 * 
 */
package com.bj.service;

import java.util.List;

import com.bj.pojo.FileArea;

/**
 * @author LQK
 *
 */
public interface FileAreaService {
	List<FileArea> findAll(int offset,  int rowCount);
	int countAll();
	int insert(FileArea fileArea);
	int update(FileArea fileArea);
	int detele(int id);
	FileArea findById(int id);
	int countBySysId(int sys_id);
	int countBySysIdExcept(int sys_id,int id);
	int deteleByTemplateId(int templateId);
	List<FileArea> findByTemplateId(int templateId);
	int batchInsert(List<FileArea> fileAreas);
}
