package com.wzf.crowd.service.api;

import java.util.List;

import com.wzf.crowd.entity.Menu;

public interface MenuService {

	public  List<Menu> getAll();

	public void saveMenu(Menu menu);

	public void updateMenu(Menu menu);

	public void removeMenu(Integer id);
}
