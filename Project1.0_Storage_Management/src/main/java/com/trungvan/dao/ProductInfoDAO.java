package com.trungvan.dao;

import java.io.IOException;

import com.trungvan.entity.ProductInfo;

public interface ProductInfoDAO<E> extends BaseDAO<E>{
	
	String uploadFile(ProductInfo productInfo) throws IllegalStateException, IOException;
}
