package com.cg.ibs.accountmanagement.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class DBUtil {
	private static EntityManagerFactory entityManagerFactory;
	private static EntityManager entityManager;

	static {
		entityManagerFactory = Persistence.createEntityManagerFactory("Gandhi");
	}
	
	public static EntityManager getEntityManger() {
		
		if(null==entityManager || (!entityManager.isOpen())) {
			entityManager=entityManagerFactory.createEntityManager();
		}
		return entityManager;
	}
	
	public static EntityTransaction getTransaction() {
		return getEntityManger().getTransaction();
	}

}
