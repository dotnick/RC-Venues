package com.nick.android.rcflyinglocations.messages;

public class SyncReply {

	byte[] db;
	
	public SyncReply(byte[] db) {
		this.db = db;
	}
	
	public byte[] getDB() {
		return this.db;
	}
}
