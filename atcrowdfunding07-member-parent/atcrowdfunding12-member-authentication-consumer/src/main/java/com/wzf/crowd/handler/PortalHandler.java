package com.wzf.crowd.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wzf.crowd.api.MySQLRemoteService;
import com.wzf.crowd.constant.CrowdConstant;
import com.wzf.crowd.entity.vo.PortalTypeVO;
import com.wzf.crowd.util.ResultEntity;

@Controller
public class PortalHandler {

	@Autowired
	private MySQLRemoteService mySQLRemoteService;
	
	@RequestMapping("/")
	public String showPortalPage(ModelMap modelMap) {
		// 这里实际开发中需要加载数据……
		ResultEntity<List<PortalTypeVO>> portalTypeProjecResultEntity = mySQLRemoteService.getPortalTypeProjectDataRemote();
		String result = portalTypeProjecResultEntity.getResult();
		if (ResultEntity.SUCCESS.equals(result)) {
			List<PortalTypeVO> portalTypeVOList = portalTypeProjecResultEntity.getData();
			modelMap.addAttribute(CrowdConstant.ATTR_NAME_PORTAL_DATA,portalTypeVOList);
		}
		
		return "portal";
	}

}
