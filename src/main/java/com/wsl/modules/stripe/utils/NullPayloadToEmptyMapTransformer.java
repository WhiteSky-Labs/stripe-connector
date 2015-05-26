/**
 * 
 */
package com.wsl.modules.stripe.utils;

import java.util.HashMap;
import java.util.Map;

import org.mule.api.transformer.DataType;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractDiscoverableTransformer;
import org.mule.transformer.types.DataTypeFactory;
import org.mule.transport.NullPayload;

/**
 * @author WhiteSky Labs
 *
 */
public class NullPayloadToEmptyMapTransformer extends AbstractDiscoverableTransformer {
	
	@SuppressWarnings("unchecked")
    private static final DataType<NullPayload> SOURCE_DATA_TYPE = DataTypeFactory.create(NullPayload.class);
 
    /**
     *  Constructor initializing source type and result type.
     */
    public NullPayloadToEmptyMapTransformer() {
        this.registerSourceType(SOURCE_DATA_TYPE);
        this.returnType = DataTypeFactory.create(Map.class);
    }
 
    /**
     *  Transforms the given input object which is a NullPayload instance to an instance of Map<String, Object>
     * that is empty
     *
     * @param src - source object
     * @param enc - encoding to use while transforming
     * @return emtpy map
     * @throws TransformerException in case that something goes wrong
     */
    @Override
    @SuppressWarnings("unchecked")
    protected Object doTransform(Object src, String enc) throws TransformerException {
        return new HashMap<String, Object>();
    }
}
