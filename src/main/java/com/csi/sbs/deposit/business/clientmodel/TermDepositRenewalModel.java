package com.csi.sbs.deposit.business.clientmodel;

import java.math.BigDecimal;


public class TermDepositRenewalModel {
	
	private String accountnumber;

    private String depositnumber;

    private BigDecimal depositamount;
        
    private String tdccy;

    private String termperiod;

    private BigDecimal maturityinterest;

    private BigDecimal maturityamount;

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

	public String getTermperiod() {
		return termperiod;
	}

	public void setTermperiod(String termperiod) {
		this.termperiod = termperiod;
	}

	public BigDecimal getMaturityinterest() {
		return maturityinterest;
	}

	public void setMaturityinterest(BigDecimal maturityinterest) {
		this.maturityinterest = maturityinterest;
	}

	public BigDecimal getMaturityamount() {
		return maturityamount;
	}

	public void setMaturityamount(BigDecimal maturityamount) {
		this.maturityamount = maturityamount;
	}
    
       

}
