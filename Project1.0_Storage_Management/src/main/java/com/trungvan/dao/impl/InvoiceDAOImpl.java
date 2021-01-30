package com.trungvan.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trungvan.dao.InvoiceDAO;
import com.trungvan.entity.Invoice;
@Repository
@Transactional(rollbackFor=Exception.class)
public class InvoiceDAOImpl extends BaseDAOImpl<Invoice> implements InvoiceDAO<Invoice>{

}
