package com.trungvan.dao.impl;


import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trungvan.dao.UserDAO;
import com.trungvan.entity.User;

@Repository
@Transactional(rollbackFor = Exception.class)
public class UserDAOImpl extends BaseDAOImpl<User> implements UserDAO<User>{

}
