package com.nick.android.rcflyinglocations.messages;

import java.io.Serializable;

public class SyncReply implements Serializable {

	private static final long serialVersionUID = 1L;
	byte[] db;
	
	public SyncReply(byte[] db) {
		this.db = db;
	}
	
	public byte[] getDB() {
		return this.db;
	}
}
