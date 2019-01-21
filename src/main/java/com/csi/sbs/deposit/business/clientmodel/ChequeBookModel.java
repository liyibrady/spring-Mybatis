package com.csi.sbs.deposit.business.clientmodel;

import io.swagger.annotations.ApiModel;

@ApiModel
public class ChequeBookModel {

	private String accountNumber;
	private String chequeBookType;
	private String chequeBookSize;

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getChequeBookType() {
		return chequeBookType;
	}

	public void setChequeBookType(String chequeBookType) {
		this.chequeBookType = chequeBookType;
	}

	public String getChequeBookSize() {
		return chequeBookSize;
	}

	public void setChequeBookSize(String chequeBookSize) {
		this.chequeBookSize = chequeBookSize;
	}

}
