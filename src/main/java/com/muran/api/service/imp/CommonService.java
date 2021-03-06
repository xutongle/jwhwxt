package com.muran.api.service.imp;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.muran.api.service.AbstractService;
import com.muran.api.service.ICommonService;
import com.muran.dao.IColumnItemDao;
import com.muran.dto.WxButton;
import com.muran.dto.WxMenu;
import com.muran.dto.WxSubButton;
import com.muran.model.ColumnItem;

@Service
public class CommonService extends AbstractService implements ICommonService {

	@Resource(name = "ColumnItemDao")
	private IColumnItemDao dao;

	@Override
	@Transactional
	public WxMenu getWxMenu() {
		// TODO Auto-generated method stub
		List<ColumnItem> listParent = new ArrayList<ColumnItem>();
		List<WxButton> listButtons = new ArrayList<WxButton>();
		listParent = dao.getList(Long.valueOf("0"));
		for (ColumnItem columnItem : listParent) {
			// 新建
			WxButton button = new WxButton();
			button.setName(columnItem.getName());
			List<ColumnItem> listChild = dao.getList(columnItem.getAutoId());

			if (listChild != null && listChild.size() > 0) {
				List<WxSubButton> listSubButtons = new ArrayList<WxSubButton>();
				for (ColumnItem columnItem2 : listChild) {
					WxSubButton subButton = new WxSubButton();
					subButton.setName(columnItem2.getName());
					subButton.setType(columnItem2.getType());
					subButton.setUrl(columnItem2.getUrl());
					listSubButtons.add(subButton);
				}
				button.setSub_button(listSubButtons);
			}else if(columnItem.getType().equals("click")){
				button.setType("click");
				button.setKey(columnItem.getUrl());
			}else if (columnItem.getType().equals("view")){
				button.setType("view");
				button.setUrl(columnItem.getUrl());
			}
			listButtons.add(button);
		}
		WxMenu menu = new WxMenu();
		menu.setButton(listButtons);
		return menu;
	}

}
