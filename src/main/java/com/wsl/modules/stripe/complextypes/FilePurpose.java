package com.wsl.modules.stripe.complextypes;

/**
 * Represents the two options for a file purpose
 * @author WhiteSky Labs
 *
 */
public enum FilePurpose {
	IDENTITY_DOCUMENT, DISPUTE_EVIDENCE;
	
	/**
	 * 
	 */
	@Override
	public String toString(){
		switch(this){
		case IDENTITY_DOCUMENT: return "identity_document";
		case DISPUTE_EVIDENCE: return "dispute_evidence";
		default: throw new IllegalArgumentException();
		}
	}
}
