/**
 * 
 */
package com.eviro.assessment.grad001.tumelomalatji.business;

import java.math.BigDecimal;

/**
 * @author tumelo
 *
 */
public interface AccountService {
	public void withdraw(String accountNum, BigDecimal amountToWithdraw);
}
