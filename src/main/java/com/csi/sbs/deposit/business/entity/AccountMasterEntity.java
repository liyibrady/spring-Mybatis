package com.csi.sbs.deposit.business.entity;

import java.math.BigDecimal;
import java.util.Date;

public class AccountMasterEntity {
	
	private String id;

    private String accountnumber;

    private String accounttype;

    private String accountstatus;

    private String currencycode;

    private BigDecimal balance;

    private Date lastupdateddate;

    private String chequebooktype;

    private BigDecimal chequebooksize;
    
    private String customerprimarykeyid;
    
    

	public BigDecimal getChequebooksize() {
		return chequebooksize;
	}

	public void setChequebooksize(BigDecimal chequebooksize) {
		this.chequebooksize = chequebooksize;
	}

	public String getCustomerprimarykeyid() {
		return customerprimarykeyid;
	}

	public void setCustomerprimarykeyid(String customerprimarykeyid) {
		this.customerprimarykeyid = customerprimarykeyid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccountnumber() {
		return accountnumber;
	}

	public void setAccountnumber(String accountnumber) {
		this.accountnumber = accountnumber;
	}

	public String getAccounttype() {
		return accounttype;
	}

	public void setAccounttype(String accounttype) {
		this.accounttype = accounttype;
	}

	public String getAccountstatus() {
		return accountstatus;
	}

	public void setAccountstatus(String accountstatus) {
		this.accountstatus = accountstatus;
	}

	public String getCurrencycode() {
		return currencycode;
	}

	public void setCurrencycode(String currencycode) {
		this.currencycode = currencycode;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Date getLastupdateddate() {
		return lastupdateddate;
	}

	public void setLastupdateddate(Date lastupdateddate) {
		this.lastupdateddate = lastupdateddate;
	}

	public String getChequebooktype() {
		return chequebooktype;
	}

	public void setChequebooktype(String chequebooktype) {
		this.chequebooktype = chequebooktype;
	}

	
    
}