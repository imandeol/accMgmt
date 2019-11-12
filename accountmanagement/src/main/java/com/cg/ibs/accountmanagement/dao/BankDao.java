package com.cg.ibs.accountmanagement.dao;

import com.cg.ibs.accountmanagement.bean.BankAdmin;
import com.cg.ibs.accountmanagement.exception.IBSException;

public interface BankDao {
	public BankAdmin getByAdminId(String admin_id) throws IBSException;
}
