package com.eviro.assessment.grad001.tumelomalatji.business;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eviro.assessment.grad001.tumelomalatji.data.SystemDB;

public class SavingsAccount implements AccountService {
	private static final Logger logger = LogManager.getLogger(SavingsAccount.class);
	private BigDecimal minimumBalance = new BigDecimal(1000.00);
	SystemDB sysdb = SystemDB.getInstace();;

	@Override
	public void withdraw(String accountNum, BigDecimal amountToWithdraw) {
		// TODO Auto-generated method stub
		BigDecimal newBalance = new BigDecimal(0.00);
		BigDecimal oldBalance = new BigDecimal(0.00);

		logger.info("Withdrawing...");
		// 1. Get the account from the DB and check if the account has minimum balance
		// required.
		String accountInfo = sysdb.getAccount(Integer.parseInt(accountNum));
		String accountInfoArray[] = accountInfo.split(",");
		oldBalance = new BigDecimal(accountInfoArray[2]);
		// Check if the account was found in the database;
		if (accountInfo != null) {

			if (oldBalance.compareTo(minimumBalance) < 0) {
				logger.info("Account is less than R1000.00\nUnable to withdraw R" + amountToWithdraw
						+ "\nPlease deposit more funds.");
			} else {
				logger.info("The account meets minimum balance requirements.");
				newBalance = oldBalance.subtract(amountToWithdraw);
				logger.info("Account withdrawn successfully.");
				// Update the account with the new balance.
				sysdb.updateAccountBalance(Integer.parseInt(accountNum), newBalance);
			}
		}

	}

}
