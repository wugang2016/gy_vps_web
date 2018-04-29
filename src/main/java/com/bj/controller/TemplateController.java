package com.bj.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bj.pojo.AndroidRealplayArea;
import com.bj.pojo.AndroidRealplayTemplate;
import com.bj.pojo.Template;
import com.bj.pojo.TemplateList;
import com.bj.service.AndroidRealplayAreaService;
import com.bj.service.AndroidRealplayTemplateService;


@RestController
public class TemplateController {
	@Resource
	private AndroidRealplayTemplateService androidRealplayTemplateService;
	  
	@RequestMapping("api/v1/get_realtime_template")
    public TemplateList templateList() {
		int c = androidRealplayTemplateService.countAll();
		int page = c/20 + 1;
		String ip = "192.168.9.21";
		List<Template> templates = new ArrayList<Template>();
	    for(int i=0; i<page; ++i) {
	    	List<AndroidRealplayTemplate> androidRealplayTemplates =  androidRealplayTemplateService.findAll(i*20,20);
	    	for (Iterator iterator = androidRealplayTemplates.iterator(); iterator.hasNext();) {
				AndroidRealplayTemplate androidRealplayTemplate = (AndroidRealplayTemplate) iterator.next();
				
				
				Template tem1 = new Template(androidRealplayTemplate.getId(),
						androidRealplayTemplate.getName(),
						"http://"+ip+"/dl/"+androidRealplayTemplate.getMiniPicPath(),
						"http://"+ip+"/dl/"+androidRealplayTemplate.getPicPath(),
						"http://"+ip+"/dl/"+androidRealplayTemplate.getBackgroudVideo(),
						"http://"+ip+"/dl/"+androidRealplayTemplate.getSigPicBoderPath(),
						androidRealplayTemplate.getLongitude(),
						androidRealplayTemplate.getLatitude()
						);
				
				
				templates.add(tem1);
			}
	    }
		
//		List<Template> templates = new ArrayList<Template>();
//		Template tem1 = new Template(1,
//				"1080P太升国际模板",
//				"http://223.112.102.245:8081/tsgj_2.jpeg",
//				"http://223.112.102.245:8081/tsgj_2.jpg",
//				"http://223.112.102.245:8081/23_1M.mp4",
//				"http://223.112.102.245:8081/1920.png",
//				"120.0111",
//				"40.01111"
//				);
//		Template tem2 = new Template(2,
//				"太升国际模板_三星PAD",
//				"http://223.112.102.245:8081/tsgj_2.jpeg",
//				"http://223.112.102.245:8081/tsgj_2.jpg",
//				"http://223.112.102.245:8081/23_1M.mp4",
//				"http://223.112.102.245:8081/2048.png",
//				"120.0111",
//				"40.01111"
//				);
//		templates.add(tem1);
//		templates.add(tem2);
		
        return new TemplateList(templates);
    }
}
