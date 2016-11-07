package com.oomoqu.rest.wrapper;

import com.oomoqu.rest.model.Address;
import com.oomoqu.rest.model.Transaction;

public class TransactionDetail {
	private Transaction transaction;
	private Address address;
	public Transaction getTransaction() {
		return transaction;
	}
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
}
