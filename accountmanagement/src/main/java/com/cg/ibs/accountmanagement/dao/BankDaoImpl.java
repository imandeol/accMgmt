package com.cg.ibs.accountmanagement.dao;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.cg.ibs.accountmanagement.bean.BankAdmin;
import com.cg.ibs.accountmanagement.exception.IBSException;
import com.cg.ibs.accountmanagement.exception.IBSExceptionInt;
import com.cg.ibs.accountmanagement.util.DBUtil;

@Repository("bankDao")
public class BankDaoImpl implements BankDao {

	private static Logger logger = Logger.getLogger(BankDaoImpl.class);

	private EntityManager entityManager;
	
	public BankDaoImpl() {
		entityManager= DBUtil.getEntityManger();
	}

	@Override
	public BankAdmin getByAdminId(String admin_id) throws IBSException {
		BankAdmin bankBean = null;
		bankBean = entityManager.find(BankAdmin.class, admin_id);
		if (bankBean == null) {
			logger.error(IBSExceptionInt.WRONG_BANKERID_AND_PASSWORD);
			throw new IBSException(IBSExceptionInt.WRONG_BANKERID_AND_PASSWORD);
		}
		logger.info(" Details of bank admin returned");
		return bankBean;
	}
}
