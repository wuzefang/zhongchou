package com.wzf.crowd.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wzf.crowd.entity.vo.DetailProjectVO;
import com.wzf.crowd.entity.vo.PortalTypeVO;
import com.wzf.crowd.entity.vo.ProjectVO;
import com.wzf.crowd.service.api.ProjectService;
import com.wzf.crowd.util.ResultEntity;

@RestController
public class ProjectProviderHandler {

	@Autowired
	ProjectService projectService;

	@RequestMapping("/get/project/detail/remote/{projectId}")
	public ResultEntity<DetailProjectVO> getDetailProjectVORemote(@PathVariable("projectId") Integer projectId) {
		try {
			DetailProjectVO detailProjectVO = projectService.getDetailProjectVO(projectId);
			return ResultEntity.successWithData(detailProjectVO);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultEntity.error(e.getMessage());
		}
	}

	@RequestMapping("/get/portal/type/project/data")
	public ResultEntity<List<PortalTypeVO>> getPortalTypeProjectDataRemote() {

		try {
			List<PortalTypeVO> portalTypeVO = projectService.getPortalTypeVO();

			return ResultEntity.successWithData(portalTypeVO);
		} catch (Exception e) {

			return ResultEntity.error(e.getMessage());
		}
	}

	@RequestMapping("/save/project/vo/remote")
	public ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO,
			@RequestParam("memberId") Integer memberId) {
		try {
			// 调用“本地”Service 执行保存

			projectService.saveProject(projectVO, memberId);
			return ResultEntity.successWithoutData();
		} catch (Exception e) {
			e.printStackTrace();
			return ResultEntity.error(e.getMessage());
		}
	}
}
