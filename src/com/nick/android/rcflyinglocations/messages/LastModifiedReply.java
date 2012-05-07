package com.nick.android.rcflyinglocations.messages;

import java.io.Serializable;

public class LastModifiedReply implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long lastModified;
	
	public LastModifiedReply(Long lastModified) {
		this.lastModified = lastModified;
	}
	
	public Long getLastModified() {
		return this.lastModified;
	}
}
