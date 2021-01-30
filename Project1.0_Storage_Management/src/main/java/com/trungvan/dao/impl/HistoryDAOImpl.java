package com.trungvan.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trungvan.dao.HistoryDAO;
import com.trungvan.entity.History;
@Repository
@Transactional(rollbackFor=Exception.class)
public class HistoryDAOImpl extends BaseDAOImpl<History> implements HistoryDAO<History>{

}
