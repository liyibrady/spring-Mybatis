package com.csi.sbs.deposit.business.clientmodel;

import java.math.BigDecimal;

public class TermDepositDrawDownModel {
   
    private String accountnumber;
  
    private String depositnumber;

    private BigDecimal depositamount;
    
    private String tdccy;

    private String creditAccountNumber;

	public String getAccountnumber() {
		return accountnumber;
	}

	public void setAccountnumber(String accountnumber) {
		this.accountnumber = accountnumber;
	}

	public String getDepositnumber() {
		return depositnumber;
	}

	public void setDepositnumber(String depositnumber) {
		this.depositnumber = depositnumber;
	}

	public BigDecimal getDepositamount() {
		return depositamount;
	}

	public void setDepositamount(BigDecimal depositamount) {
		this.depositamount = depositamount;
	}

	public String getTdccy() {
		return tdccy;
	}

	public void setTdccy(String tdccy) {
		this.tdccy = tdccy;
	}

	public String getCreditAccountNumber() {
		return creditAccountNumber;
	}

	public void setCreditAccountNumber(String creditAccountNumber) {
		this.creditAccountNumber = creditAccountNumber;
	}

    
}