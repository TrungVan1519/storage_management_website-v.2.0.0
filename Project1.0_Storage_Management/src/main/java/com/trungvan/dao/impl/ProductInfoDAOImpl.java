package com.trungvan.dao.impl;


import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.trungvan.dao.ProductInfoDAO;
import com.trungvan.entity.ProductInfo;
import com.trungvan.utils.Constant;

@Repository
@Transactional(rollbackFor = Exception.class)
public class ProductInfoDAOImpl extends BaseDAOImpl<ProductInfo> implements ProductInfoDAO<ProductInfo>{

	private final Logger log = Logger.getLogger(this.getClass());
	
	@Override
	public String uploadFile(ProductInfo productInfo) throws IllegalStateException, IOException {
		
		String fileName = "";
		MultipartFile multipartFile = productInfo.getMultipartFile();
		
		if(multipartFile != null && !multipartFile.getOriginalFilename().isEmpty()) {

			File dir = new File(Constant.SERVER_STATIC_RESOURCES);
			if(!dir.exists()) {
				
				log.info(">> Folder is not existing, so have to create a new one");
				dir.mkdirs();
			}
			
			fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
			File file = new File(Constant.SERVER_STATIC_RESOURCES + fileName);
			multipartFile.transferTo(file);
		}
		System.out.println(">> fileName Img: " + fileName);
		return fileName;
	}
}
