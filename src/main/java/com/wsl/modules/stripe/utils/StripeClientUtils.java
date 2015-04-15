/**
 *
 * (c) 2015 WhiteSky Labs, Pty Ltd. This software is protected under international
 * copyright law. All use of this software is subject to WhiteSky Labs' Master
 * Subscription Agreement (or other Terms of Service) separately entered
 * into between you and WhiteSky Labs. If such an agreement is not in
 * place, you may not use the software.
 */

package com.wsl.modules.stripe.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Utilities for the Client Utils
 * @author WhiteSky Labs
 *
 */
public class StripeClientUtils {
	
	
	private StripeClientUtils(){
		super();
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public static Map<String, Object> removeOptionals(Map<String, Object> map){
    	Iterator<Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = it.next();
            if (pair.getValue() == null || pair.getValue().toString().equals("")){
            	it.remove();
            }             
        }
        return map;
    }
    
	/**
	 * 
	 * @param map
	 * @return
	 */
    public static Map<String, Object> removeOptionalsAndZeroes(Map<String, Object> map){
    	Iterator<Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = it.next();
            if (pair.getValue() == null || pair.getValue().toString().equals("") || pair.getValue().toString().equals("0") || pair.getValue().toString().equals("0.0")){
            	it.remove();
            }             
        }
        return map;
    }
}
