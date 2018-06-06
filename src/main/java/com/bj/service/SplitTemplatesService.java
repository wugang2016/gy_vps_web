/**
 * 
 */
package com.bj.service;

import java.util.List;

import com.bj.pojo.SplitTemplates;

/**
 * @author LQK
 *
 */
public interface SplitTemplatesService {
	List<SplitTemplates> findAll(int offset,  int rowCount);
	int countAll();
	int insert(SplitTemplates splitTemplates);
	int update(SplitTemplates splitTemplates);
	int delete(int id);
	SplitTemplates findById(int id);
    List<SplitTemplates> findDefaultTemplatesByType(int type);
 
}
