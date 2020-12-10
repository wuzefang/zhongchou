package com.wzf.crowd.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wzf.crowd.api.MySQLRemoteService;
import com.wzf.crowd.config.OSSProperties;
import com.wzf.crowd.constant.CrowdConstant;
import com.wzf.crowd.entity.vo.DetailProjectVO;
import com.wzf.crowd.entity.vo.MemberConfirmInfoVO;
import com.wzf.crowd.entity.vo.MemberLoginVO;
import com.wzf.crowd.entity.vo.ProjectVO;
import com.wzf.crowd.entity.vo.ReturnVO;
import com.wzf.crowd.util.CrowdUtil;
import com.wzf.crowd.util.ResultEntity;

@Controller
public class ProjectConsumerHandler {

	@Autowired
	OSSProperties ossProperties;

	@Autowired
	MySQLRemoteService mySQLRemoteService;

	@RequestMapping("/get/project/detail/{projectId}")
	public String getProjectDetail(@PathVariable("projectId") Integer projectId, Model model) {
		ResultEntity<DetailProjectVO> resultEntity = mySQLRemoteService.getDetailProjectVORemote(projectId);
		if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
			DetailProjectVO detailProjectVO = resultEntity.getData();
			model.addAttribute("detailProjectVO", detailProjectVO);
		}
		return "project-show-detail";
	}

	@RequestMapping("/create/confirm")
	public String saveConfirm(ModelMap modelMap, HttpSession session, MemberConfirmInfoVO memberConfirmInfoVO) {
		// 1.从 Session 域读取之前临时存储的 ProjectVO 对象
		ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);
		// 2.如果 projectVO 为 null
		if (projectVO == null) {
			throw new RuntimeException(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
		}
		// 3.将确认信息数据设置到 projectVO 对象中
		projectVO.setMemberConfirmInfoVO(memberConfirmInfoVO);
		// 4.从 Session 域读取当前登录的用户

		MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
		Integer memberId = memberLoginVO.getId();
		// 5.调用远程方法保存 projectVO 对象
		ResultEntity<String> saveResultEntity = mySQLRemoteService.saveProjectVORemote(projectVO, memberId);
		// 6.判断远程的保存操作是否成功
		String result = saveResultEntity.getResult();
		if (ResultEntity.ERROR.equals(result)) {
			modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, saveResultEntity.getMessage());
			return "project-confirm";
		}
		// 7.将临时的 ProjectVO 对象从 Session 域移除
		session.removeAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);
		// 8.如果远程保存成功则跳转到最终完成页面
		return "redirect:http://localhost/project/create/success";
	}

	@ResponseBody
	@RequestMapping("/create/save/return.json")
	public ResultEntity<String> saveReturn(ReturnVO returnVO, HttpSession session) {
		try {
			// 1. 从session中取出之前设置好的ProjectVO
			ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);

			// 2.判断projectVO对象是否存在，不存在返回异常信息
			if (projectVO == null) {
				ResultEntity.error(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
			}

			// 3.存在的话，取出ProjectVO中的returnList
			List<ReturnVO> returnVOList = projectVO.getReturnVOList();

			// 4.判断returnVOList是否存在，
			if (returnVOList == null) {
				// 如果不存在则创建一个新的List
				returnVOList = new ArrayList<>();

				// 并且设置回projectVO中
				projectVO.setReturnVOList(returnVOList);
			}
			// 5.如果存在，则将ReturnVO对象，添加到回报的集合中
			returnVOList.add(returnVO);

			// 6.将更新后的projectVO重新设置回session中
			session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT, projectVO);

			// 7.没出异常，给页面返回正常响应
			return ResultEntity.successWithoutData();
		} catch (Exception e) {
			e.printStackTrace();
			return ResultEntity.error(e.getMessage());
		}

	}

	@ResponseBody
	@RequestMapping("/create/upload/return/picture.json")
	public ResultEntity<String> uploadReturnPicture(
			// 接收用户上传的文件
			@RequestParam("returnPicture") MultipartFile returnPicture) throws IOException {
		// 1.执行文件上传
		ResultEntity<String> uploadReturnPictureResultEntity = CrowdUtil.uploadFileToOss(ossProperties.getEndPoint(),
				ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret(), returnPicture.getInputStream(),
				ossProperties.getBucketName(), ossProperties.getBucketDomain(), returnPicture.getOriginalFilename());
		// 2.返回上传的结果
		return uploadReturnPictureResultEntity;
	}

	@RequestMapping("/create/project/information")
	public String saveProjectBasicInfo(
			// 接收除了上传图片之外的其他普通数据
			ProjectVO projectVO,

			// 接收上传的头图
			MultipartFile headerPicture,

			// 接收上传的详情图片
			List<MultipartFile> detailPictureList,

			// 用来将收集了一部分数据的ProjectVO对象存入session域
			HttpSession session,

			// 用户接收后台传送过来的处理 成功/ 失败的处理信息
			ModelMap modelMap) throws IOException {
		/************************************* 上传头图 ***************************************************/
		// 1.判断头图文件是否为空
		boolean headPictureIsEmpty = headerPicture.isEmpty();

		// 2.如果是空的，返回上传页面
		if (headPictureIsEmpty) {
			// 如果没有上传头图，返回发起页面，并带回失败信息
			modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MEASSAGE_HEADER_PIC_EMPTY);
			return "project-launch";
		}

		// 3.如果不是空的，执行上传文件到oss上
		ResultEntity<String> uploadHeadPicResultEntity = CrowdUtil.uploadFileToOss(ossProperties.getEndPoint(),
				ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret(), headerPicture.getInputStream(),
				ossProperties.getBucketName(), ossProperties.getBucketDomain(), headerPicture.getOriginalFilename());
		// 4.获取上传的结果
		String result = uploadHeadPicResultEntity.getResult();

		// 5.判断是否上传成功
		if (ResultEntity.SUCCESS.equals(result)) {
			// 成功
			// 6.从返回的结果中获取图片的路径
			String headerPicturePath = uploadHeadPicResultEntity.getData();

			// 7.存入到ProjectVO对象中
			projectVO.setHeaderPicturePath(headerPicturePath);

		} else {
			// 8.如果上传结果为失败，返回发起页面，并带回失败信息
			modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MEASSAGE_HEADER_PIC_UPLOAD_FAILED);
			return "project-launch";
		}
		/***********************************************************************************************/
		/************************************* 上传详情图 ***************************************************/
		// 1.检查 detailPictureList 是否有效
		if (detailPictureList == null || detailPictureList.size() == 0) {
			modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAIL_PIC_EMPTY);
			return "project-launch";
		}
		// 创建一个用来存放详情图片路径的集合
		List<String> detailPicturePathList = new ArrayList<>();

		// 2.遍历detailPicture详情图片
		for (MultipartFile detailPicture : detailPictureList) {

			if (detailPicture.isEmpty()) {
				// 检查单个文件为空
				modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAIL_PIC_EMPTY);

				return "project-launch";
			}
			// 3.执行上传图片
			ResultEntity<String> uploadDetailPictureResultEntity = CrowdUtil.uploadFileToOss(
					ossProperties.getEndPoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret(),
					detailPicture.getInputStream(), ossProperties.getBucketName(), ossProperties.getBucketDomain(),
					detailPicture.getOriginalFilename());

			// 4.判断存入oss的结果是否是成功
			if (ResultEntity.SUCCESS.equals(uploadDetailPictureResultEntity.getResult())) {
				// 成功
				// 5.获取文件地址的路径
				String detailPicturePath = uploadDetailPictureResultEntity.getData();
				// 6.将获取到的地址存入集合中
				detailPicturePathList.add(detailPicturePath);
			}
		}
		// 7.遍历结束，将图片地址的集合存入ProjectVO对象
		projectVO.setDetailPicturePathList(detailPicturePathList);

		/***********************************************************************************************/
		/********************************* 存入Session返回 **************************************************/
		// 10.将ProjectVO存入Session中
		session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT, projectVO);

		return "redirect:http://localhost/project/return/info/page";
	}
}
