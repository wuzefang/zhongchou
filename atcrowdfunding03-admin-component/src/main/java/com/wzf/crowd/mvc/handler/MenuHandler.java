package com.wzf.crowd.mvc.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wzf.crowd.entity.Menu;
import com.wzf.crowd.service.api.MenuService;
import com.wzf.crowd.util.ResultEntity;

@Controller
public class MenuHandler {
	
	@Autowired
	MenuService menuService;
	
	@ResponseBody
	@RequestMapping("/menu/remove.json")
	public ResultEntity<String> removeMenu(@RequestParam("id") Integer id) {
	menuService.removeMenu(id);
	return ResultEntity.successWithoutData();
	}
	
	@ResponseBody
	@RequestMapping("/menu/update.json")
	public ResultEntity<String> updateMenu(Menu menu) {
		menuService.updateMenu(menu);
		return ResultEntity.successWithoutData();
		}
	
	@ResponseBody
	@RequestMapping("/menu/save.json")
	public ResultEntity<String> saveMenu(Menu menu){
		menuService.saveMenu(menu);
		return ResultEntity.successWithoutData();
	}
	
	
	@ResponseBody
	@RequestMapping("/menu/get/whole/tree.json")
	public ResultEntity<Menu> getWholeTreeNew(){
		
		// 1、查询全部的menu对象
		List<Menu> menuList = menuService.getAll();
		
		// 2、声明一个变量用来存储找到的根节点
		Menu root = null;
		
		// 3、创建map对象用来存储id和menu对象的对应关系，便于查找父节点
		Map<Integer,Menu> menuMap = new HashMap<>();
		
		// 4、遍历menuList填充menuMap ,保存数据库中的 id  和  行 之间的关系
		for(Menu menu : menuList){
			Integer id = menu.getId();
			menuMap.put(id, menu);
		}
		
		// 5、再次遍历，查找根节点，或 非跟几点对应的父节点
		
		for (Menu menu :menuList){
			// 如果pid为 null 就表示是根节点
			Integer pId = menu.getPid();
			if (pId == null) {
				// 如果当前节点没有pid就保存为根节点
				root = menu;
				// 跳出当 次 循环
				continue;
			}
			// 如果pid 不是null ，说明当前节点有父节点，则从  上一次  遍历的所有的结果中  获取到父亲节点的对象
			Menu father = menuMap.get(pId);
			// 将当前对象 封装到其对应的父亲对象的children集合中
			father.getChildren().add(menu);
		}
		// 将遍历后的结果返回给页面
		return ResultEntity.successWithData(root);
	}
}
