package com.eviro.assessment.grad001.tumelomalatji.business;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eviro.assessment.grad001.tumelomalatji.data.SystemDB;

public class CurrentAccount implements AccountService {
	private static final Logger logger = LogManager.getLogger(CurrentAccount.class);
	private BigDecimal maximumOverdraft = new BigDecimal(100000.00);
	private BigDecimal maximumWithdrawalLimit = new BigDecimal(0.00);
	private BigDecimal newBalance = new BigDecimal(0.00);
	private BigDecimal oldBalance = new BigDecimal(0.00);
	private BigDecimal currentOverDraft = new BigDecimal(0.00);

	SystemDB sysdb = SystemDB.getInstace();

	@Override
	public void withdraw(String accountNum, BigDecimal amountToWithdraw) {
		// TODO Auto-generated method stub
		String accountInfo = sysdb.getAccount(Integer.parseInt(accountNum));
		String accountInfoArray[] = accountInfo.split(",");

		oldBalance = new BigDecimal(accountInfoArray[2]);
		currentOverDraft = new BigDecimal(accountInfoArray[3]);

		// Check if the account was found in the database;
		if (accountInfo != null) {
			logger.info("Account is valid...");
			// Check for the maximum overdraft limit anormally.
			// This can also be done when loading data into the database.
			if (currentOverDraft.compareTo(maximumOverdraft) > 0) {
				logger.info("This account has abnormal overdraft limit. Please investigate.");
			} else {
				maximumWithdrawalLimit = new BigDecimal(accountInfoArray[2]).add(new BigDecimal(accountInfoArray[3]));
				// Check if the withdrawal is not over the maximum limit.
				if (amountToWithdraw.compareTo(maximumWithdrawalLimit) <= 0) {
					logger.info("You have enough funds to cover the withdrawal.");
					newBalance = oldBalance.subtract(amountToWithdraw);
					// update the account with the new balance.
					sysdb.updateAccountBalance(Integer.parseInt(accountNum), newBalance);
				} else {
					logger.info("\tYou have insufficient funds to make the withdrawal");
					logger.info("\tYour account balance is : [ " + oldBalance + " ]");
					logger.info("\tYour overdraft limit is : [ " + accountInfoArray[3] + " ]");
					logger.info("\tYour maximum Withdrawal limit: [ " + maximumWithdrawalLimit + " ]");
				}
			}

		}
	}
}
