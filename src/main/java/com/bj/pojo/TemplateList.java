package com.bj.pojo;

import java.util.List;

public class TemplateList {
	public TemplateList() {
		super();
	}

	public TemplateList(List<Template> templates) {
		super();
		this.templates = templates;
	}

	@Override
	public String toString() {
		return "TemplateList [templates=" + templates + "]";
	}

	private List<Template> templates;

	public List<Template> getTemplates() {
		return templates;
	}

	public void setTemplates(List<Template> templates) {
		this.templates = templates;
	}
}
