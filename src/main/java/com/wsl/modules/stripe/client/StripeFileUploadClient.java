/**
 *
 * (c) 2015 WhiteSky Labs, Pty Ltd. This software is protected under international
 * copyright law. All use of this software is subject to WhiteSky Labs' Master
 * Subscription Agreement (or other Terms of Service) separately entered
 * into between you and WhiteSky Labs. If such an agreement is not in
 * place, you may not use the software.
 */

package com.wsl.modules.stripe.client;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.FileUpload;
import com.wsl.modules.stripe.complextypes.FilePurpose;
import com.wsl.modules.stripe.exceptions.StripeConnectorException;

/**
 * Encapsulates File Upload-related Stripe API calls.
 * @author WhiteSky Labs
 *
 */
public class StripeFileUploadClient {
    /**
     * Create a file upload
     * To upload a file to Stripe, you'll need to send a request of type multipart/form-data. The request should contain the file you would like to upload, as well as the parameters for creating a file.
	 * 
     * @param file A file to upload. The file should follow the specifications of RFC 2388 (which defines file transfers for the multipart/form-data protocol).
     * @param purpose The purpose of the uploaded file. Possible values are identity_document, dispute_evidence.
     * @return Returns the file object.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public FileUpload createFileUpload(String file, FilePurpose purpose)
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("file", new File(file));
    	params.put("purpose", purpose.toString());
    	try {
    		return FileUpload.create(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not create the File Upload", e);
		}
    }
}
