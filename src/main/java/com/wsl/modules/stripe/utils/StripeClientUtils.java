/**
 * (c) 2018 WhiteSky Labs, Pty Ltd. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
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
