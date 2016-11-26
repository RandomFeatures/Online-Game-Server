/**
 * Use this to verfiy that the charon call is comming from the server hosting pythia
 * This process will have to be redesigned to handle porting to the iPhone
 * perhaps the iPhone port will not be allowed to do account management. 
 */
package com.charon.util;

import com.cerberus.myaccount.dal.dao.DspLookupIpAddressDao;
import com.cerberus.myaccount.dal.dto.DspLookupIpAddress;
import com.cerberus.myaccount.dal.dto.DspLookupIpAddressParam;
import com.cerberus.myaccount.dal.exceptions.DspLookupIpAddressDaoException;
import com.cerberus.myaccount.dal.factory.DspLookupIpAddressDaoFactory;

/**
 * @author allen
 *
 */
public class ValidIP {

	
	public final boolean verifyIP(String ipAddress)
	{
		DspLookupIpAddress _result[] = null;
		
		//make sure it is in the correct format
		//no funny business
		if (validateFormat(ipAddress))
		{
			_result = lookupIP(ipAddress);
			if (_result.length > 0)
				return true; //found the iP so its all good
			else
				return false; 
		}else
			return false;
	}
	
	
	private final static boolean validateFormat( String  ipAddress )
	{
	    String[] parts = ipAddress.split( "\\." );

	    if ( parts.length != 4 )
	    {
	        return false;
	    }

	    for ( String s : parts )
	    {
	        int i = Integer.parseInt( s );

	        if ( (i < 0) || (i > 255) )
	        {
	            return false;
	        }
	    }

	    return true;
	}
	
	//lookup the ip int he database to make sure it is a valid one
	private final static DspLookupIpAddress[] lookupIP(String ipAddress) 
	{

		DspLookupIpAddressDao _sproc = DspLookupIpAddressDaoFactory.create();
		DspLookupIpAddressParam _param = new DspLookupIpAddressParam();
		DspLookupIpAddress _result[] = null;
		try {
		
			_param.setIp_address(ipAddress);
			_result =_sproc.execute(_param);
		
		} catch (DspLookupIpAddressDaoException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			_param = null;
			_sproc = null;
		}

		return _result;
		
	}

}
