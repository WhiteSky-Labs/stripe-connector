/**
 *
 * (c) 2015 WhiteSky Labs, Pty Ltd. This software is protected under international
 * copyright law. All use of this software is subject to WhiteSky Labs' Master
 * Subscription Agreement (or other Terms of Service) separately entered
 * into between you and WhiteSky Labs. If such an agreement is not in
 * place, you may not use the software.
 */

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
