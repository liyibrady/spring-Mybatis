package com.csi.sbs.deposit.business.clientmodel;

public class TransactionModel {

	private String transferOutAccountNumber;

	private String transferInAccountNumber;

	private String transferCcy;

	private String transferAmount;

	public String getTransferOutAccountNumber() {
		return transferOutAccountNumber;
	}

	public void setTransferOutAccountNumber(String transferOutAccountNumber) {
		this.transferOutAccountNumber = transferOutAccountNumber;
	}

	public String getTransferInAccountNumber() {
		return transferInAccountNumber;
	}

	public void setTransferInAccountNumber(String transferInAccountNumber) {
		this.transferInAccountNumber = transferInAccountNumber;
	}

	public String getTransferCcy() {
		return transferCcy;
	}

	public void setTransferCcy(String transferCcy) {
		this.transferCcy = transferCcy;
	}

	public String getTransferAmount() {
		return transferAmount;
	}

	public void setTransferAmount(String transferAmount) {
		this.transferAmount = transferAmount;
	}

}
