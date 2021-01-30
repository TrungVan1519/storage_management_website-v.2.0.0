package com.trungvan.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trungvan.dao.UserRoleDAO;
import com.trungvan.entity.UserRole;
@Repository
@Transactional(rollbackFor=Exception.class)
public class UserRoleDAOImpl extends BaseDAOImpl<UserRole>  implements UserRoleDAO<UserRole>{

}
