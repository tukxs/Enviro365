package com.eviro.assessment.grad001.tumelomalatji.ui;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eviro.assessment.grad001.tumelomalatji.business.CurrentAccount;
import com.eviro.assessment.grad001.tumelomalatji.business.SavingsAccount;
import com.eviro.assessment.grad001.tumelomalatji.data.SystemDB;

public class EnviroBank {

	private static final Logger logger = LogManager.getLogger(EnviroBank.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		logger.info("EnviroBank Application Starting...");
		SystemDB sysdb = SystemDB.getInstace();
		logger.info("Withdrawing from Savings Account...");
		SavingsAccount sa1 = new SavingsAccount();
		sa1.withdraw("1", new BigDecimal(500.00));
		sysdb.showAllDB();
		sa1.withdraw("1", new BigDecimal(500.00));
		sysdb.showAllDB();
		sa1.withdraw("1", new BigDecimal(500.00));
		sysdb.showAllDB();
		sa1.withdraw("1", new BigDecimal(500.00));
		sysdb.showAllDB();

		CurrentAccount ca1 = new CurrentAccount();
		logger.info("Withdrawing from Current Account...");
		ca1.withdraw("3", new BigDecimal(500.00));
		sysdb.showAllDB();
		ca1.withdraw("3", new BigDecimal(500.00));
		sysdb.showAllDB();
		ca1.withdraw("3", new BigDecimal(500.00));
		sysdb.showAllDB();
		ca1.withdraw("3", new BigDecimal(9000.00));
		sysdb.showAllDB();
		ca1.withdraw("3", new BigDecimal(500.00));
		sysdb.showAllDB();
		ca1.withdraw("3", new BigDecimal(500.00));
		sysdb.showAllDB();

		ca1.withdraw("4", new BigDecimal(5000.00));
		sysdb.showAllDB();
		ca1.withdraw("4", new BigDecimal(5000.00));
		sysdb.showAllDB();
		ca1.withdraw("4", new BigDecimal(5000.00));
		sysdb.showAllDB();
		ca1.withdraw("4", new BigDecimal(9000.00));
		sysdb.showAllDB();
		ca1.withdraw("4", new BigDecimal(5000.00));
		sysdb.showAllDB();
		ca1.withdraw("4", new BigDecimal(5000.00));
		sysdb.showAllDB();

		ca1.withdraw("5", new BigDecimal(5000.00));
		sysdb.showAllDB();

		try {
			sysdb.closeConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("EnviroBank Application Finished...");
	}

}
