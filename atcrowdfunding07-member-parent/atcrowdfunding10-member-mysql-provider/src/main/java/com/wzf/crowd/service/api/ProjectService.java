package com.wzf.crowd.service.api;

import java.util.List;

import com.wzf.crowd.entity.vo.DetailProjectVO;
import com.wzf.crowd.entity.vo.PortalTypeVO;
import com.wzf.crowd.entity.vo.ProjectVO;

public interface ProjectService {

	void saveProject(ProjectVO projectVO, Integer memberId);
	
	List<PortalTypeVO> getPortalTypeVO();
	
	DetailProjectVO getDetailProjectVO(Integer projectId);

}
