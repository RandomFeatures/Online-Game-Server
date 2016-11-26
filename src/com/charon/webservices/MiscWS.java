package com.charon.web.myaccount;

import java.util.Date;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import com.cerberus.myaccount.dal.Misc;
import com.cerberus.myaccount.dal.SystemLogs;
import com.cerberus.myaccount.dal.dto.TblEvents;
import com.cerberus.myaccount.dal.dto.TblLookup;
import com.cerberus.myaccount.dal.dto.TblProducts;
import com.charon.util.CharonXML;
import com.charon.util.ValidIP;

/**
 * This is a webservice class exposing a method called greet which takes a input
 * parameter and greets the parameter with hello.
 * 
 * @author Allen Halsted
 */

/*
 * @WebService indicates that this is webservice interface and the name
 * indicates the webservice name.
 */

@WebService(name = "Misc", targetNamespace = "http://charon.net", serviceName = "Charon")
/*
 * @SOAPBinding indicates binding information of soap messages. Here we have
 * document-literal style of webservice and the parameter style is wrapped.
 */
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.BARE)
public class MiscWS {

	private static Misc _dal = new Misc();
	private static SystemLogs _log = new SystemLogs();
	private static CharonXML _xmlBuilder = new CharonXML();
	@Resource
	private WebServiceContext wsContext;

	private final boolean verifyIP() {
		MessageContext mc = wsContext.getMessageContext();
		String ipAddress = ((javax.servlet.http.HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST)).getRemoteAddr();
		ValidIP _ip = new ValidIP();

		return _ip.verifyIP(ipAddress);
	}

	@WebMethod
	public String getConfigValue(@WebParam(name = "keyname") String keyname, @WebParam(name = "gameid") int gameid, @WebParam(name = "source") int source) {
		StringBuffer rtnXML = new StringBuffer();
		StringBuffer rowXML = new StringBuffer();
		String _result;

		if (verifyIP()) {

			try {
				_result = _dal.getConfigValue(keyname, gameid, source);
				if (!_result.equals("")) {
					rowXML.append("<row><fieldlist><field name=\"ConfigValue\" type=\"string\">");
					rowXML.append(_result);
					rowXML.append("</field></fieldlist></row>");
					rtnXML.append(_xmlBuilder.buildDatasetXML("Config_Detail_Results", rowXML.toString()));
				} else
					rtnXML.append(_xmlBuilder.buildErrorXML("MiscWS.getConfigValue", 0, 10, "Failed to find Config", "", ""));

			} catch (Exception _e) {
				_log.insertError(0, 0, "MiscWS.getConfigValue", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}
			return rtnXML.toString();
		} else
			return null;
	}

	@WebMethod
	public String getLookupValue(@WebParam(name = "lookuptype") String lookuptype, @WebParam(name = "lookupcode") int lookupcode) {
		StringBuffer rtnXML = new StringBuffer();
		StringBuffer rowXML = new StringBuffer();

		String _result;

		if (verifyIP()) {
			try {

				_result = _dal.getLookupValue(lookuptype, lookupcode);
				if (!_result.equals("")) {
					rowXML.append("<row><fieldlist><field name=\"LookupDesc\" type=\"string\">");
					rowXML.append(_result);
					rowXML.append("</field></fieldlist></row>");
					rtnXML.append(_xmlBuilder.buildDatasetXML("Lookup_Detail_Results", rowXML.toString()));
				} else
					rtnXML.append(_xmlBuilder.buildErrorXML("MiscWS.getLookupValue", 0, 10, "Failed to find Lookup Value", "", ""));

			} catch (Exception _e) {
				_log.insertError(0, 0, "MiscWS.getLookupValue", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}

			return rtnXML.toString();
		} else
			return null;

	}

	@WebMethod
	public String getLookupCodes(@WebParam(name = "lookuptype") String lookuptype) {

		StringBuffer rtnXML = new StringBuffer();
		StringBuffer rowXML = new StringBuffer();

		TblLookup _result[];

		if (verifyIP()) {
			try {
				_result = _dal.getLookupCodes(lookuptype);
				if (_result.length > 0) {
					for (int i = 0; i < _result.length; i++) {
						rowXML.append(_result[i].toXMLRow());
					}
					rtnXML.append(_xmlBuilder.buildDatasetXML("Lookup_Detail_Results", rowXML.toString()));
				} else
					rtnXML.append(_xmlBuilder.buildErrorXML("MiscWS.getLookupCodes", 0, 10, "Failed to find Lookup Code", "", ""));

			} catch (Exception _e) {
				_log.insertError(0, 0, "MiscWS.getLookupCodes", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}

			return rtnXML.toString();
		} else
			return null;
	}

	/**
	 * Method 'findAll'
	 * 
	 */
	@WebMethod
	public String findAllEvents() {
		String rtnXML = "";
		TblEvents _result[];

		if (verifyIP()) {
			try {
				_result = _dal.findAllEvents();
				if (_result.length > 0)
					rtnXML = buildEventReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("MiscWS.findAllEvents", 0, 10, "Failed to find Event", "", "");

			} catch (Exception _e) {
				_log.insertError(0, 0, "MiscWS.findAllEvents", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}
			return rtnXML;
		} else
			return null;
	}

	/**
	 * Method 'findWhereEventCodeEquals'
	 * 
	 * @param eventCode
	 */
	@WebMethod
	public String findEventByEventCode(@WebParam(name = "eventCode") int eventCode) {
		String rtnXML = "";
		TblEvents _result[];

		if (verifyIP()) {
			try {
				_result = _dal.findEventByEventCode(eventCode);
				if (_result.length > 0)

					rtnXML = buildEventReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("MiscWS.findEventByEventCode", 0, 10, "Failed to find Event", "", "");

			} catch (Exception _e) {
				_log.insertError(0, 0, "MiscWS.findEventByEventCode", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}
			return rtnXML;
		} else
			return null;
	}

	/****************************************************************
	 * TblProductsDao
	 ******************************************************************/

	@WebMethod
	public void updateProducts(@WebParam(name = "ProductID") int iProductID, @WebParam(name = "ProductName") String strProductName,
			@WebParam(name = "GameID") int iGameID, @WebParam(name = "Price") long lPrice, @WebParam(name = "PurchaseDate") Date dtPurchaseDate,
			@WebParam(name = "ProductType") int iProductType, @WebParam(name = "Active") int Active) {

		if (verifyIP()) {

			try {

				_dal.updateProducts(iProductID, strProductName, iGameID, lPrice, dtPurchaseDate, iProductType, Active);

			} catch (Exception _e) {
				_log.insertError(0, 0, "MiscWS.updateProducts", _e.getMessage(), _e.toString());
			}
		}
	}

	@WebMethod
	public int insertProducts(@WebParam(name = "ProductName") String strProductName, @WebParam(name = "GameID") int iGameID,
			@WebParam(name = "Price") long lPrice, @WebParam(name = "PurchaseDate") Date dtPurchaseDate, @WebParam(name = "ProductType") int iProductType,
			@WebParam(name = "Active") int Active) {

		int rt = 0;

		if (verifyIP()) {
			try {

				rt = _dal.insertProducts(strProductName, iGameID, lPrice, dtPurchaseDate, iProductType, Active);
			} catch (Exception _e) {
				_log.insertError(0, 0, "MiscWS.insertProducts", _e.getMessage(), _e.toString());
			}
		}
		return rt;
	}

	/**
	 * Method 'findAll'
	 * 
	 */
	@WebMethod
	public String findAllProducts() {
		String rtnXML = "";
		TblProducts _result[];

		if (verifyIP()) {
			try {
				_result = _dal.findAllProducts();
				if (_result.length > 0)
					rtnXML = buildProductReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("MiscWS.findAllProducts", 0, 10, "Failed to find Product", "", "");

			} catch (Exception _e) {
				_log.insertError(0, 0, "MiscWS.findAllProducts", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}
			return rtnXML;
		} else
			return null;
	}

	/**
	 * Method 'findWhereProductIDEquals'
	 * 
	 * @param productID
	 */
	@WebMethod
	public String findProductByID(@WebParam(name = "productID") int productID) {
		String rtnXML = "";
		TblProducts _result[];

		if (verifyIP()) {
			try {
				_result = _dal.findWhereProductIDEquals(productID);
				if (_result.length > 0)
					rtnXML = buildProductReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("MiscWS.findProductByID", 0, 10, "Failed to find Product", "", "");

			} catch (Exception _e) {
				_log.insertError(0, 0, "MiscWS.findProductByID", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}
			return rtnXML;
		} else
			return null;
	}

	/**
	 * Method 'findWhereProductNameEquals'
	 * 
	 * @param productName
	 */
	@WebMethod
	public String findProductByName(@WebParam(name = "productName") String productName) {
		String rtnXML = "";
		TblProducts _result[];

		if (verifyIP()) {
			try {
				_result = _dal.findWhereProductNameEquals(productName);
				if (_result.length > 0)
					rtnXML = buildProductReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("MiscWS.findProductByName", 0, 10, "Failed to find Product", "", "");

			} catch (Exception _e) {
				_log.insertError(0, 0, "MiscWS.findProductByName", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}
			return rtnXML;
		} else
			return null;
	}

	/**
	 * Method 'findWhereGameIDEquals'
	 * 
	 * @param gameID
	 */
	@WebMethod
	public String findProductByGameID(@WebParam(name = "gameID") int gameID) {
		String rtnXML = "";
		TblProducts _result[];
		try {
			_result = _dal.findWhereGameIDEquals(gameID);
			if (_result.length > 0)
				rtnXML = buildProductReturnXML(_result);
			else
				rtnXML = _xmlBuilder.buildErrorXML("MiscWS.findProductByGameID", 0, 10, "Failed to find Product", "", "");

		} catch (Exception _e) {
			_log.insertError(0, 0, "MiscWS.findProductByGameID", _e.getMessage(), _e.toString());
		} finally {
			_result = null;
		}
		return rtnXML;
	}

	/**
	 * Method 'findWherePurchaseDateEquals'
	 * 
	 * @param purchaseDate
	 */
	@WebMethod
	public String findProductByPurchaseDate(@WebParam(name = "purchaseDate") Date purchaseDate) {
		String rtnXML = "";
		TblProducts _result[];

		if (verifyIP()) {
			try {
				_result = _dal.findWherePurchaseDateEquals(purchaseDate);
				if (_result.length > 0)
					rtnXML = buildProductReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("MiscWS.findProductByPurchaseDate", 0, 10, "Failed to find PurchaseDate", "", "");
			} catch (Exception _e) {
				_log.insertError(0, 0, "MiscWS.findProductByPurchaseDate", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}
			return rtnXML;
		} else
			return null;
	}

	/**
	 * Method 'findWhereProductTypeEquals'
	 * 
	 * @param productType
	 */
	@WebMethod
	public String findProductByType(@WebParam(name = "productType") int productType) {
		String rtnXML = "";
		TblProducts _result[];

		if (verifyIP()) {
			try {
				_result = _dal.findWhereProductTypeEquals(productType);
				if (_result.length > 0)
					rtnXML = buildProductReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("MiscWS.findProductByType", 0, 10, "Failed to find Product", "", "");
			} catch (Exception _e) {
				_log.insertError(0, 0, "MiscWS.findProductByType", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}
			return rtnXML;
		} else
			return null;
	}

	/**
	 * Method 'findWhereActiveEquals'
	 * 
	 * @param active
	 */
	@WebMethod
	public String findProductByActiveStatus(@WebParam(name = "active") int active) {
		String rtnXML = "";
		TblProducts _result[];

		if (verifyIP()) {
			try {
				_result = _dal.findWhereActiveEquals(active);
				if (_result.length > 0)
					rtnXML = buildProductReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("MiscWS.findProductByActiveStatus", 0, 10, "Failed to find Product", "", "");
			} catch (Exception _e) {
				_log.insertError(0, 0, "MiscWS.findProductByActiveStatus", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}
			return rtnXML.toString();
		} else
			return null;
	}

	private String buildEventReturnXML(TblEvents dto[]) {
		StringBuffer rtnXML = new StringBuffer();
		StringBuffer rowXML = new StringBuffer();

		for (int i = 0; i < dto.length; i++) {
			rowXML.append(dto[i].toXMLRow());
		}

		rtnXML.append(_xmlBuilder.buildDatasetXML("Event_Detail_Results", rowXML.toString()));
		return rtnXML.toString();

	}

	private String buildProductReturnXML(TblProducts dto[]) {
		StringBuffer rtnXML = new StringBuffer();
		StringBuffer rowXML = new StringBuffer();

		for (int i = 0; i < dto.length; i++) {
			rowXML.append(dto[i].toXMLRow());
		}

		rtnXML.append(_xmlBuilder.buildDatasetXML("Product_Detail_Results", rowXML.toString()));
		return rtnXML.toString();

	}

}
