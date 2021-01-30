package com.trungvan.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.trungvan.entity.History;
import com.trungvan.service.HistoryService;
import com.trungvan.utils.Constant;

@Controller
@RequestMapping("/history")
public class HistoryController {

	static final Logger log = Logger.getLogger(HistoryController.class);
	
	@Autowired
	private HistoryService historyService;

	// Retrieve
	/**
	 * > Do trong endpoint /history/list ta tao san Form tim kiem nen khong can phai tao 1 endpoint khac
	 * 		de show Form
	 * > Do khi truy van binh thuong den /history/list su dung GET, nhung neu tim kiem History thi truy
	 * 		van den /history/list su dung POST nen o day khong chi dinh cu the @GetMapping hay @PostMapping duoc
	 * 		ma phai su dung @RequestMapping va ke ca khi su dung @RequestMapping ta cung khong duoc phep
	 * 		chi ro la su dung method=GET hay method=POST
	 * 
	 * @param model
	 * @param session
	 * @param history
	 * @return
	 */
	@RequestMapping("/list")
	public String showHistoryList(Model model,
			@ModelAttribute("historySearchForm") History history) {

		log.info("<<==>> historyService search history list - historySearchForm: " + history);
		boolean searchingMode = false;

		Map<String,String> mapTypes = new HashMap<>();
		mapTypes.put(String.valueOf(Constant.TYPE_ALL), "All");
		mapTypes.put(String.valueOf(Constant.TYPE_GOODS_RECEIPT), "Goods Receipt");
		mapTypes.put(String.valueOf(Constant.TYPE_GOODS_ISSUES), "Goods Issues");
		model.addAttribute("mapTypes", mapTypes);

		// Do su dung kieu nay nen @ModelAttribute("historySearchForm") History history luon != null, vi the chi can 
		//		kiem tra cac truong can search co empty hay khong thoi la duoc
		if(history != null) {
			
			if(history.getProductInfo() != null) {
				
				if(!StringUtils.isEmpty(history.getProductInfo().getCategory().getName()) ) {
					
					searchingMode = true;
				}
				if(!StringUtils.isEmpty(history.getProductInfo().getCode())) {

					searchingMode = true;
				}
				if( !StringUtils.isEmpty(history.getProductInfo().getName()) ) {

					searchingMode = true;
				}
			}
			if(!StringUtils.isEmpty(history.getActionName()) ) {

				searchingMode = true;
			}
			if(history.getType() != null && history.getType() != 0) {

				searchingMode = true;
			}
		}
		
		
		if(searchingMode) {
			
			model.addAttribute("histories", historyService.searchAll(history));
		} else {

			model.addAttribute("histories", historyService.findAll());
		}
		
		return "historyListView.definition";
	}
}
