package com.trungvan.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trungvan.dao.ProductInStockDAO;
import com.trungvan.entity.ProductInStock;


@Repository
@Transactional(rollbackFor=Exception.class)
public class ProductInStockDAOImpl extends BaseDAOImpl<ProductInStock> implements ProductInStockDAO<ProductInStock>{

}
