package com.wzf.crowd.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wzf.crowd.entity.po.MemberConfirmInfoPO;
import com.wzf.crowd.entity.po.MemberLaunchInfoPO;
import com.wzf.crowd.entity.po.ProjectPO;
import com.wzf.crowd.entity.po.ReturnPO;
import com.wzf.crowd.entity.vo.DetailProjectVO;
import com.wzf.crowd.entity.vo.MemberConfirmInfoVO;
import com.wzf.crowd.entity.vo.MemberLauchInfoVO;
import com.wzf.crowd.entity.vo.PortalTypeVO;
import com.wzf.crowd.entity.vo.ProjectVO;
import com.wzf.crowd.entity.vo.ReturnVO;
import com.wzf.crowd.mapper.MemberConfirmInfoPOMapper;
import com.wzf.crowd.mapper.MemberLaunchInfoPOMapper;
import com.wzf.crowd.mapper.ProjectItemPicPOMapper;
import com.wzf.crowd.mapper.ProjectPOMapper;
import com.wzf.crowd.mapper.ReturnPOMapper;
import com.wzf.crowd.service.api.ProjectService;

@Service
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {
	@Autowired
	private ProjectPOMapper projectPOMapper;

	@Autowired
	private MemberLaunchInfoPOMapper memberLaunchInfoPOMapper;

	@Autowired
	private MemberConfirmInfoPOMapper memberConfirmInfoPOMapper;

	@Autowired
	private ReturnPOMapper returnPOMapper;
	@Autowired
	private ProjectItemPicPOMapper projectItemPicPOMapper;

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	@Override
	public void saveProject(ProjectVO projectVO, Integer memberId) {
		/**************************** 保存ProjectPO对象 **********************/
		// 1.创建ProjectPO对象
		ProjectPO projectPO = new ProjectPO();

		// 2.赋值ProjectVO对象到ProjectPO中
		BeanUtils.copyProperties(projectVO, projectPO);
		// bug修复 存入memberID
		projectPO.setMemberid(memberId);

		// bug修复 保存时间
		String createdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		projectPO.setCreatedate(createdate);

		// bug修复，status设置成0
		projectPO.setStatus(0);

		// 3.保存ProjectPO对象到数据库
		// 需要获取保存后的自增主键，所以要在对应的xml中进行设置
		// <insert id="insertSelective" useGeneratedKeys="true" keyProperty="id"
		// parameterType="com.wzf.crowd.entity.po.ProjectPO" >
		projectPOMapper.insertSelective(projectPO);

		// 4.从projectPo中获取自增的id
		Integer projectId = projectPO.getId();

		/**************************** 保存项目、分类的关联关信息 **********************/
		// 从projectVO对象中获取项目分类
		List<Integer> typeIdList = projectVO.getTypeIdList();
		projectPOMapper.insertTypeRelationship(typeIdList, projectId);

		/**************************** 保存项目、标签的关联关信息 **********************/
		List<Integer> tagIdList = projectVO.getTagIdList();
		projectPOMapper.insertTagRelationship(tagIdList, projectId);

		/**************************** 保存项目中详情图片的路径信息 **********************/
		List<String> detailPicturePathList = projectVO.getDetailPicturePathList();
		projectItemPicPOMapper.insertPathList(detailPicturePathList, projectId);

		/**************************** 保存项目发起人的信息 **********************/
		MemberLauchInfoVO memberLauchInfoVO = projectVO.getMemberLauchInfoVO();
		MemberLaunchInfoPO memberLaunchInfoPO = new MemberLaunchInfoPO();
		BeanUtils.copyProperties(memberLauchInfoVO, memberLaunchInfoPO);
		memberLaunchInfoPO.setMemberid(memberId);

		memberLaunchInfoPOMapper.insert(memberLaunchInfoPO);
		/**************************** 保存项目项目回报信息 **********************/
		List<ReturnVO> returnVOList = projectVO.getReturnVOList();
		ArrayList<ReturnPO> returnPOList = new ArrayList<>();
		for (ReturnVO returnVO : returnVOList) {
			ReturnPO returnPO = new ReturnPO();
			BeanUtils.copyProperties(returnVO, returnPO);
			returnPOList.add(returnPO);
		}
		returnPOMapper.insertReturnPOBatch(returnPOList, projectId);

		/**************************** 保存项目确认信息 **********************/
		MemberConfirmInfoVO memberConfirmInfoVO = projectVO.getMemberConfirmInfoVO();
		MemberConfirmInfoPO memberConfirmInfoPO = new MemberConfirmInfoPO();
		BeanUtils.copyProperties(memberConfirmInfoVO, memberConfirmInfoPO);
		memberConfirmInfoPO.setMemberid(memberId);
		memberConfirmInfoPOMapper.insert(memberConfirmInfoPO);

	}

	@Override
	public List<PortalTypeVO> getPortalTypeVO() {

		return projectPOMapper.selectPortalTypeVOList();

	}

	@Override
	public DetailProjectVO getDetailProjectVO(Integer projectId) {
		// 1.查询得到 DetailProjectVO 对象
		DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(projectId);
		// 2.根据 status 确定 statusText

		Integer status = detailProjectVO.getStatus();
		switch (status) {
		case 0:
			detailProjectVO.setStatusText("审核中");
			break;
		case 1:
			detailProjectVO.setStatusText("众筹中");
			break;
		case 2:
			detailProjectVO.setStatusText("众筹成功");
			break;
		case 3:
			detailProjectVO.setStatusText("已关闭");
			break;
		default:
			break;
		}
		// 3.根据 deployeDate 计算 lastDay
		// 2020-10-15
		String deployDate = detailProjectVO.getDeployDate();
		// 获取当前日期
		Date currentDay = new Date();
		// 把众筹日期解析成 Date 类型
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date deployDay = format.parse(deployDate);
			// 获取当前当前日期的时间戳
			long currentTimeStamp = currentDay.getTime();
			// 获取众筹日期的时间戳
			long deployTimeStamp = deployDay.getTime();
			// 两个时间戳相减计算当前已经过去的时间
			long pastDays = (currentTimeStamp - deployTimeStamp) / 1000 / 60 / 60 / 24;
			// 获取总的众筹天数

			Integer totalDays = detailProjectVO.getDay();
			// 使用总的众筹天数减去已经过去的天数得到剩余天数
			Integer lastDay = (int) (totalDays - pastDays);
			detailProjectVO.setLastDay(lastDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return detailProjectVO;
	}

}
