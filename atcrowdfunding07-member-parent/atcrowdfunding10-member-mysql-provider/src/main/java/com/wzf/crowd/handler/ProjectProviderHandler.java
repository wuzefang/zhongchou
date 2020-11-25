package com.wzf.crowd.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.wzf.crowd.service.api.ProjectService;


@RestController
public class ProjectProviderHandler {
	
	@Autowired
	ProjectService projectService;

}
