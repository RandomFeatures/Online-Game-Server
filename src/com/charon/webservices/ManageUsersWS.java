package com.charon.web.myaccount;

import java.util.Date;
import java.util.Properties;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import com.cerberus.myaccount.dal.ManageUsers;
import com.cerberus.myaccount.dal.Misc;
import com.cerberus.myaccount.dal.SystemLogs;
import com.cerberus.myaccount.dal.dto.DspFindUserByLogin;
import com.cerberus.myaccount.dal.dto.DspFindUserGameList;
import com.cerberus.myaccount.dal.dto.DspFindUserProductHistory;
import com.cerberus.myaccount.dal.dto.TblUserProducts;
import com.cerberus.myaccount.dal.dto.TblUserSession;
import com.cerberus.myaccount.dal.dto.TblUsers;
import com.cerberus.myaccount.dal.dto.TblXref;
import com.charon.util.CharonXML;
import com.charon.util.SMTPAuthenticator;
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

@WebService(name = "ManageUsers", targetNamespace = "http://charon.net", serviceName = "Charon")
/*
 * @SOAPBinding indicates binding information of soap messages. Here we have
 * document-literal style of webservice and the parameter style is wrapped.
 */
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class ManageUsersWS {

	private static ManageUsers _dal = new ManageUsers();
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
	public String findUserDetailsFromLogin(@WebParam(name = "login") String login, @WebParam(name = "password") String password) {
		StringBuffer rtnXML = new StringBuffer();
		StringBuffer rowXML = new StringBuffer();

		DspFindUserByLogin _result[];
		if (verifyIP()) {
			try {

				_result = _dal.findUserDetailsFromLogin(login, password);
				if (_result.length > 0) {
					for (int i = 0; i < _result.length; i++) {
						rowXML.append(_result[i].toXMLRow());
					}
					rtnXML.append(_xmlBuilder.buildDatasetXML("User_Detail_Results", rowXML.toString()));
				} else
					rtnXML.append(_xmlBuilder.buildErrorXML("ManageUsersWS.findUserDetailsFromLogin", 0, 10, "Failed to find User", "", ""));

			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.findUserDetailsFromLogin", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}

			return rtnXML.toString();
		} else
			return null;
	}

	@WebMethod
	public String findUserGameList(@WebParam(name = "userid") int userid) {
		StringBuffer rtnXML = new StringBuffer();
		StringBuffer rowXML = new StringBuffer();

		DspFindUserGameList _result[];
		if (verifyIP()) {
			try {
				_result = _dal.findUserGameList(userid);
				if (_result.length > 0) {
					for (int i = 0; i < _result.length; i++) {
						rowXML.append(_result[i].toXMLRow());
					}
					rtnXML.append(_xmlBuilder.buildDatasetXML("User_Game_Results", rowXML.toString()));
				} else
					rtnXML.append(_xmlBuilder.buildErrorXML("ManageUsersWS.findUserGameList", 0, 10, "Failed to find User", "", ""));

			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.findUserGameList", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}

			return rtnXML.toString();
		} else
			return null;
	}

	@WebMethod
	public String findUserProductHistory(@WebParam(name = "gameid") int gameid, @WebParam(name = "userid") int userid) {
		StringBuffer rtnXML = new StringBuffer();
		StringBuffer rowXML = new StringBuffer();

		DspFindUserProductHistory _result[];
		if (verifyIP()) {
			try {
				_result = _dal.findUserProductHistory(gameid, userid);
				if (_result.length > 0) {
					for (int i = 0; i < _result.length; i++) {
						rowXML.append(_result[i].toXMLRow());
					}
					rtnXML.append(_xmlBuilder.buildDatasetXML("User_Product_Results", rowXML.toString()));
				} else
					rtnXML.append(_xmlBuilder.buildErrorXML("ManageUsersWS.findUserProductHistory", 0, 10, "Failed to find User", "", ""));

			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.findUserProductHistory", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}

			return rtnXML.toString();
		} else
			return null;
	}

	@WebMethod
	public int updatePassword(@WebParam(name = "userid") int userid, @WebParam(name = "password") String password,
			@WebParam(name = "newpassword") String newpassword) {
		TblUsers _result[];
		String firstname;
		String lastname;
		String login;
		String email;
		String current_password;
		int status;
		int rtn = 0;
		if (verifyIP()) {
			try {
				_result = _dal.findUserByUserID(userid);
				if (_result.length > 0) {
					firstname = _result[0].getFirstName();
					lastname = _result[0].getLastName();
					login = _result[0].getLogin();
					email = _result[0].getEmail();
					current_password = _result[0].getPassword();
					status = _result[0].getAccountStatus();

					if (password.equals(current_password)) {
						_dal.updateUser(userid, firstname, lastname, login, newpassword, email, status);
						rtn = 1;
					}
				}
			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.updatePassword", _e.getMessage(), _e.toString());
			}
		}
		return rtn;
	}

	@WebMethod
	public void updateUser(@WebParam(name = "userid") int userid, @WebParam(name = "firstname") String firstname, @WebParam(name = "lastname") String lastname,
			@WebParam(name = "login") String login, @WebParam(name = "password") String password, @WebParam(name = "email") String email,
			@WebParam(name = "status") int status) {
		TblUsers _result[];

		if (verifyIP()) {
			try {
				if (password.equals("")) {
					// not updating password here. client wont have it anyway
					_result = _dal.findUserByUserID(userid);
					if (_result.length > 0)
						password = _result[0].getPassword();
				}

				_dal.updateUser(userid, firstname, lastname, login, password, email, status);
			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.updateUser", _e.getMessage(), _e.toString());
			}
		}
	}

	@WebMethod
	public int insertNewUser(@WebParam(name = "firstname") String firstname, @WebParam(name = "lastname") String lastname,
			@WebParam(name = "login") String login, @WebParam(name = "password") String password, @WebParam(name = "email") String email,
			@WebParam(name = "status") int status) {
		int rt = 0;

		if (verifyIP()) {
			try {
				rt = _dal.insertNewUser(firstname, lastname, login, password, email, status);
			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.insertNewUser", _e.getMessage(), _e.toString());
			}
		}
		return rt;

	}

	/**
	 * Method 'findAll'
	 * 
	 */
	@WebMethod
	public String findAllUsers() {
		String rtnXML = "";
		TblUsers _result[];
		if (verifyIP()) {
			try {

				_result = _dal.findAllUsers();
				if (_result.length > 0)
					rtnXML = buildUserReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("ManageUsersWS.findAllUsers", 0, 10, "Failed to find User", "", "");

			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.findAllUsers", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}
			return rtnXML;
		} else
			return null;
	}

	/**
	 * Method 'findWhereUserIDEquals'
	 * 
	 * @param userID
	 */
	@WebMethod
	public String findUserByUserID(@WebParam(name = "userid") int userid) {
		String rtnXML = "";
		TblUsers _result[];
		if (verifyIP()) {
			try {
				_result = _dal.findUserByUserID(userid);
				if (_result.length > 0)
					rtnXML = buildUserReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("ManageUsersWS.findUserByUserID", 0, 10, "Failed to find User", "", "");
			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.findUserByUSerID", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}
			return rtnXML;
		} else
			return null;
	}

	/**
	 * Method 'findWhereLastNameEquals'
	 * 
	 * @param lastName
	 */
	@WebMethod
	public String findUserByLastName(@WebParam(name = "lastName") String lastName) {
		String rtnXML = "";
		TblUsers _result[];
		if (verifyIP()) {
			try {

				_result = _dal.findUserByLastName(lastName);
				if (_result.length > 0)
					rtnXML = buildUserReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("ManageUsersWS.findUserByLastName", 0, 10, "Failed to find User", "", "");

			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.findUserByLastName", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}
			return rtnXML;
		} else
			return null;
	}

	/**
	 * Method 'findWhereLoginEquals'
	 * 
	 * @param login
	 */
	@WebMethod
	public String findUserByLogin(@WebParam(name = "login") String login) {
		String rtnXML = "";
		TblUsers _result[];
		if (verifyIP()) {
			try {

				_result = _dal.findUserByLogin(login);
				if (_result.length > 0)
					rtnXML = buildUserReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("ManageUsersWS.findUserByLogin", 0, 10, "Failed to find User", "", "");

			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.findUserByLogin", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}

			return rtnXML;
		} else
			return null;
	}

	/**
	 * Method 'findWhereEmailEquals'
	 * 
	 * @param email
	 */
	@WebMethod
	public String findUserByEmail(@WebParam(name = "email") String email) {
		String rtnXML = "";
		TblUsers _result[];
		if (verifyIP()) {
			try {

				_result = _dal.findUserByEmail(email);
				if (_result.length > 0)
					rtnXML = buildUserReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("ManageUsersWS.findUserByEmail", 0, 10, "Failed to find User", "", "");

			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.findUserByEmail", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}

			return rtnXML;
		} else
			return null;
	}

	/**
	 * Method 'findWhereAccountStatusEquals'
	 * 
	 * @param accountStatus
	 */
	@WebMethod
	public String findUsersByAccountStatus(@WebParam(name = "accountstatus") int accountstatus) {
		String rtnXML = "";
		TblUsers _result[];
		if (verifyIP()) {
			try {
				_result = _dal.findUsersByAccountStatus(accountstatus);
				if (_result.length > 0)
					rtnXML = buildUserReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("ManageUsersWS.findUsersByAccountStatus", 0, 10, "Failed to find User", "", "");

			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.findUsersByAccountStatus", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}

			return rtnXML;
		} else
			return null;
	}

	/****************************************************************
	 * TblUserProductsDao
	 ******************************************************************/

	@WebMethod
	public void updateUserPurchase(@WebParam(name = "id") int id, @WebParam(name = "userid") int userid, @WebParam(name = "gameid") int gameid,
			@WebParam(name = "productid") int productid, @WebParam(name = "purchasecode") int purchasecode, @WebParam(name = "purchasedate") Date purchasedate,
			@WebParam(name = "expirationdate") Date expirationdate, @WebParam(name = "productstatus") int productstatus) {

		if (verifyIP()) {
			try {
				_dal.updateUserPurchases(id, userid, gameid, productid, purchasecode, purchasedate, expirationdate, productstatus);

			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.updateUserProducts", _e.getMessage(), _e.toString());
			}
		}
	}

	@WebMethod
	public int insertNewUserPurchase(@WebParam(name = "userid") int userid, @WebParam(name = "gameid") int gameid, @WebParam(name = "productid") int productid,
			@WebParam(name = "purchasecode") int purchasecode, @WebParam(name = "purchasedate") Date purchasedate,
			@WebParam(name = "expirationdate") Date expirationdate, @WebParam(name = "productstatus") int productstatus) {
		int rt = 0;

		if (verifyIP()) {
			try {

				rt = _dal.insertNewUserPurchases(userid, gameid, productid, purchasecode, purchasedate, expirationdate, productstatus);
			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.insertNewUserProducts", _e.getMessage(), _e.toString());
			}
		}
		return rt;
	}

	@WebMethod
	public String findAllUserPurchases() {
		String rtnXML = "";
		TblUserProducts _result[];
		if (verifyIP()) {
			try {

				_result = _dal.findAllUserPurchases();
				if (_result.length > 0)
					rtnXML = buildProductReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("ManageUsersWS.findAllUserProducts", 0, 10, "Failed to find Product", "", "");

			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.findAllUserProducts", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}
			return rtnXML;
		} else
			return null;
	}

	/**
	 * Method 'findWhereUserIDEquals'
	 * 
	 * @param userID
	 */
	@WebMethod
	public String findPurchaseByUserID(@WebParam(name = "userid") int userid) {
		String rtnXML = "";
		TblUserProducts _result[];
		if (verifyIP()) {
			try {

				_result = _dal.findPurchasesByUserID(userid);
				if (_result.length > 0)
					rtnXML = buildProductReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("ManageUsersWS.findProductByUserID", 0, 10, "Failed to find Product", "", "");

			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.findProductByUserID", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}
			return rtnXML;
		} else
			return null;
	}

	/**
	 * Method 'findPurchaseByGameIDUserID'
	 * 
	 * @param userID
	 */
	@WebMethod
	public String findPurchaseByGameIDUserID(@WebParam(name = "gameid") int gameid, @WebParam(name = "userid") int userid) {
		String rtnXML = "";
		TblUserProducts _result[];
		if (verifyIP()) {
			try {

				_result = _dal.findPurchasesByGameIDUserID(gameid, userid);
				if (_result.length > 0)
					rtnXML = buildProductReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("ManageUsersWS.findProductByUserID", 0, 10, "Failed to find Product", "", "");

			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.findProductByUserID", _e.getMessage(), _e.toString());
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
	public String findPurchaseByGameID(@WebParam(name = "gameid") int gameid) {
		String rtnXML = "";
		TblUserProducts _result[];
		if (verifyIP()) {
			try {

				_result = _dal.findPurchasesByGameID(gameid);
				if (_result.length > 0)
					rtnXML = buildProductReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("ManageUsersWS.findProductByGameID", 0, 10, "Failed to find Product", "", "");

			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.findProductByGameID", _e.getMessage(), _e.toString());
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
	public String findPurchaseByID(@WebParam(name = "productid") int productid) {
		String rtnXML = "";
		TblUserProducts _result[];
		if (verifyIP()) {
			try {
				_result = _dal.findPurchasesByID(productid);
				if (_result.length > 0)
					rtnXML = buildProductReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("ManageUsersWS.findProductByID", 0, 10, "Failed to find Product", "", "");

			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.findProductByID", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}
			return rtnXML;
		} else
			return null;
	}

	/*********************************************************************
	 * TblUserSessionDao
	 **********************************************************************/
	@WebMethod
	public void updateUserSession(@WebParam(name = "id") int id, @WebParam(name = "userid") int userid, @WebParam(name = "gameid") int gameid,
			@WebParam(name = "source") int source, @WebParam(name = "expirationdate") Date expirationdate, @WebParam(name = "sessionid") String sessionid) {
		if (verifyIP()) {
			try {
				_dal.updateUserSession(id, userid, gameid, source, expirationdate, sessionid);
			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.updateUserSession", _e.getMessage(), _e.toString());
			}
		}

	}

	@WebMethod
	public int insertUserSession(@WebParam(name = "userid") int userid, @WebParam(name = "gameid") int gameid, @WebParam(name = "source") int source,
			@WebParam(name = "expirationdate") Date expirationdate, @WebParam(name = "sessionid") String sessionid) {
		int rt = 0;
		if (verifyIP()) {
			try {

				rt = _dal.insertUserSession(userid, gameid, source, expirationdate, sessionid);

			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.insertUserSession", _e.getMessage(), _e.toString());
			}
		}
		return rt;
	}

	/**
	 * Method 'findAll'
	 * 
	 */
	@WebMethod
	public String findAllSessions() {
		String rtnXML = "";
		TblUserSession _result[];
		if (verifyIP()) {
			try {
				_result = _dal.findAllSessions();
				if (_result.length > 0)
					rtnXML = buildSessionReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("ManageUsersWS.findAllSessions", 0, 10, "Failed to find Session", "", "");

			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.findAllSessions", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}
			return rtnXML;
		} else
			return null;
	}

	/**
	 * Method 'findWhereUserIDEquals'
	 * 
	 * @param userID
	 */
	@WebMethod
	public String findSessionByUserID(@WebParam(name = "userid") int userid) {
		String rtnXML = "";
		TblUserSession _result[];
		if (verifyIP()) {
			try {
				_result = _dal.findSessionByUserID(userid);
				if (_result.length > 0)
					rtnXML = buildSessionReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("ManageUsersWS.findSessionByUserID", 0, 10, "Failed to find Session", "", "");

			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.findSessionByUserID", _e.getMessage(), _e.toString());
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
	public String findSessionByGameID(@WebParam(name = "gameid") int gameid) {
		String rtnXML = "";
		TblUserSession _result[];
		if (verifyIP()) {
			try {

				_result = _dal.findSessionByGameID(gameid);
				if (_result.length > 0)
					rtnXML = buildSessionReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("ManageUsersWS.findSessionByGameID", 0, 10, "Failed to find Session", "", "");

			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.findSessionByGameID", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}
			return rtnXML;
		} else
			return null;
	}

	/**
	 * Method 'findWhereSourceEquals'
	 * 
	 * @param source
	 */
	@WebMethod
	public String findSessionBySource(@WebParam(name = "source") int source) {
		String rtnXML = "";
		TblUserSession _result[];
		if (verifyIP()) {
			try {
				_result = _dal.findSessionBySource(source);
				if (_result.length > 0)
					rtnXML = buildSessionReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("ManageUsersWS.findSessionBySource", 0, 10, "Failed to find Session", "", "");

			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.findSessionBySource", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}
			return rtnXML;
		} else
			return null;
	}

	/*********************************************************************
	 * TblXrefDao
	 **********************************************************************/
	@WebMethod
	public void updateXref(@WebParam(name = "xrefid") int xrefid, @WebParam(name = "userid") int userid, @WebParam(name = "gameid") int gameid,
			@WebParam(name = "source") int source, @WebParam(name = "active") int active, @WebParam(name = "xref") String xref) {
		if (verifyIP()) {
			try {
				_dal.updateXref(xrefid, userid, gameid, source, active, xref);
			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.updateXref", _e.getMessage(), _e.toString());
			}
		}

	}

	@WebMethod
	public int insertXref(@WebParam(name = "userid") int userid, @WebParam(name = "gameid") int gameid, @WebParam(name = "source") int source,
			@WebParam(name = "active") int active, @WebParam(name = "xref") String xref) {
		int rt = 0;
		if (verifyIP()) {
			try {

				rt = _dal.insertXref(userid, gameid, source, active, xref);
			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.insertXref", _e.getMessage(), _e.toString());
			}
		}
		return rt;
	}

	/**
	 * Method 'findAll'
	 * 
	 */
	@WebMethod
	public String findAllXRefs() {
		String rtnXML = "";
		TblXref _result[];
		if (verifyIP()) {
			try {
				_result = _dal.findAllXRefs();
				if (_result.length > 0)
					rtnXML = buildXRefReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("ManageUsersWS.findAllXRefs", 0, 10, "Failed to find XRef", "", "");

			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.findAllXRefs", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}
			return rtnXML;
		} else
			return null;
	}

	/**
	 * Method 'findWhereXrefidEquals'
	 * 
	 * @param xrefid
	 */
	@WebMethod
	public String findXrefByID(@WebParam(name = "xrefid") int xrefid) {
		String rtnXML = "";
		TblXref _result[];
		if (verifyIP()) {
			try {
				_result = _dal.findWhereXrefidEquals(xrefid);
				if (_result.length > 0)
					rtnXML = buildXRefReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("ManageUsersWS.findXrefByID", 0, 10, "Failed to find XRef", "", "");

			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.findXrefByID", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}
			return rtnXML;
		} else
			return null;
	}

	/**
	 * Method 'findWhereSourceEquals'
	 * 
	 * @param source
	 */
	@WebMethod
	public String findXrefBySource(@WebParam(name = "source") int source) {
		String rtnXML = "";
		TblXref _result[];
		if (verifyIP()) {
			try {
				_result = _dal.findWhereSourceEquals(source);
				if (_result.length > 0)
					rtnXML = buildXRefReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("ManageUsersWS.findXrefBySource", 0, 10, "Failed to find XRef", "", "");

			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.findXrefBySource", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}
			return rtnXML;
		} else
			return null;
	}

	/**
	 * Method 'findWhereXrefEquals'
	 * 
	 * @param xref
	 */
	@WebMethod
	public String findXrefByXref(@WebParam(name = "xref") String xref) {
		String rtnXML = "";
		TblXref _result[];
		if (verifyIP()) {
			try {
				_result = _dal.findWhereXrefEquals(xref);
				if (_result.length > 0)
					rtnXML = buildXRefReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("ManageUsersWS.findXrefByXref", 0, 10, "Failed to find XRef", "", "");

			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.findXrefByXref", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}
			return rtnXML;
		} else
			return null;
	}

	/**
	 * Method 'findWhereUserIDEquals'
	 * 
	 * @param userID
	 */
	@WebMethod
	public String findXrefByUserID(@WebParam(name = "userid") int userid) {
		String rtnXML = "";
		TblXref _result[];
		if (verifyIP()) {
			try {

				_result = _dal.findWhereUserIDEquals(userid);
				if (_result.length > 0)
					rtnXML = buildXRefReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("ManageUsersWS.findXrefByUserID", 0, 10, "Failed to find XRef", "", "");

			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.findXrefByUserID", _e.getMessage(), _e.toString());
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
	public String findXrefByGameID(@WebParam(name = "gameid") int gameid) {
		String rtnXML = "";
		TblXref _result[];
		if (verifyIP()) {
			try {
				_result = _dal.findWhereGameIDEquals(gameid);
				if (_result.length > 0)
					rtnXML = buildXRefReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("ManageUsersWS.findXrefByGameID", 0, 10, "Failed to find XRef", "", "");

			} catch (Exception _e) {
				_log.insertError(0, 0, "ManageUsersWS.findXrefByGameID", _e.getMessage(), _e.toString());
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
	public String findXrefByActiveStatus(@WebParam(name = "active") int active) {
		String rtnXML = "";
		TblXref _result[];
		if (verifyIP()) {
			try {

				_result = _dal.findWhereActiveEquals(active);
				if (_result.length > 0)
					rtnXML = buildXRefReturnXML(_result);
				else
					rtnXML = _xmlBuilder.buildErrorXML("ManageUsersWS.findXrefByActiveStatus", 0, 10, "Failed to find User", "", "");

			} catch (Exception _e) {
				_log.insertError(0, 0, "GamePlayWS.findXrefByActiveStatus", _e.getMessage(), _e.toString());
			} finally {
				_result = null;
			}
			return rtnXML;
		} else
			return null;
	}

	@WebMethod
	public void updateUserGames(@WebParam(name = "id") int id, @WebParam(name = "userid") int userid, @WebParam(name = "gameid") int gameid,
			@WebParam(name = "accountstatus") int accountstatus, @WebParam(name = "source") int source, @WebParam(name = "startdate") Date startdate) {
		if (verifyIP()) {
			try {
				_dal.updateUserGames(id, userid, gameid, accountstatus, source, startdate);
			} catch (Exception _e) {
				_log.insertError(0, 0, "GamePlayWS.updateUserGames", _e.getMessage(), _e.toString());
			}
		}
	}

	@WebMethod
	public int insertUserGames(int id, @WebParam(name = "userid") int userid, @WebParam(name = "gameid") int gameid,
			@WebParam(name = "accountstatus") int accountstatus, @WebParam(name = "source") int source, @WebParam(name = "startdate") Date startdate) {

		int rtn = 0;
		if (verifyIP()) {
			try {
				rtn = _dal.insertUserGames(userid, gameid, accountstatus, source, startdate);
			} catch (Exception _e) {
				_log.insertError(0, 0, "GamePlayWS.insertUserGames", _e.getMessage(), _e.toString());
			}
		}
		return rtn;
	}

	@WebMethod
	public void EmailAccountDetails(@WebParam(name = "email") String email) {
		TblUsers _results[];
		Misc _dal_misc = new Misc();
		String smtpHost = "";
		String smtpLogin = "";
		String smtpPass = "";
		String emailFrom = "";
		String userLogin = "";	
		String userPass = "";	
		
		//System.out.println(email);
		
		//lookup the users email
		_results = _dal.findUserByEmail(email);
		if (_results.length > 0) {
			
			//get the user details
			userLogin = "Account Login: " + _results[0].getLogin();
			userPass = "Account Password: " + _results[0].getPassword();
				
			//Lookup the SMTP info for sending the email
			smtpHost = _dal_misc.getConfigValue("smtp", 0, 0);
			smtpLogin = _dal_misc.getConfigValue("smtplogin", 0, 0);
			smtpPass = _dal_misc.getConfigValue("smtppass", 0, 0);
			emailFrom = _dal_misc.getConfigValue("mailfrom", 0, 0);
			
			//System.out.println(smtpHost);
			//System.out.println(smtpLogin);
			//System.out.println(smtpPass);
			//System.out.println(emailFrom);
			//System.out.println(userLogin);
			//System.out.println(userPass);
			
			try {
				Properties props = System.getProperties();
				props.put("mail.transport.protocol", "smtp");
				props.put("mail.smtp.starttls.enable", "false");
				props.put("mail.smtp.host", smtpHost);
				props.put("mail.smtp.auth", "true");
				Authenticator auth = new SMTPAuthenticator(smtpLogin, smtpPass);
				Session session = Session.getInstance(props, auth);
				// -- Create a new message --
				Message msg = new MimeMessage(session);
				// -- Set the FROM and TO fields --
				msg.setFrom(new InternetAddress(emailFrom));
				msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
				msg.setSubject("Account Details");
				
				
				msg.setText(userLogin + "  " + userPass);
				
				// -- Set some other header information --
				// msg.setHeader("MyMail", "Mr. XYZ" );
				msg.setSentDate(new Date());
				// -- Send the message --
				Transport.send(msg);
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("Exception " + ex);
			}
			
			_dal_misc = null;
			_results = null;
		}
	}

	public String buildUserReturnXML(TblUsers dto[]) {
		StringBuffer rtnXML = new StringBuffer();
		StringBuffer rowXML = new StringBuffer();

		for (int i = 0; i < dto.length; i++) {
			rowXML.append(dto[i].toXMLRow());
		}

		rtnXML.append(_xmlBuilder.buildDatasetXML("User_Detail_Results", rowXML.toString()));
		return rtnXML.toString();

	}

	public String buildProductReturnXML(TblUserProducts dto[]) {
		StringBuffer rtnXML = new StringBuffer();
		StringBuffer rowXML = new StringBuffer();

		for (int i = 0; i < dto.length; i++) {
			rowXML.append(dto[i].toXMLRow());
		}

		rtnXML.append(_xmlBuilder.buildDatasetXML("User_Products_Detail_Results", rowXML.toString()));
		return rtnXML.toString();

	}

	public String buildSessionReturnXML(TblUserSession dto[]) {
		StringBuffer rtnXML = new StringBuffer();
		StringBuffer rowXML = new StringBuffer();

		for (int i = 0; i < dto.length; i++) {
			rowXML.append(dto[i].toXMLRow());
		}

		rtnXML.append(_xmlBuilder.buildDatasetXML("User_Session_Detail_Results", rowXML.toString()));
		return rtnXML.toString();

	}

	public String buildXRefReturnXML(TblXref dto[]) {
		StringBuffer rtnXML = new StringBuffer();
		StringBuffer rowXML = new StringBuffer();

		for (int i = 0; i < dto.length; i++) {
			rowXML.append(dto[i].toXMLRow());
		}

		rtnXML.append(_xmlBuilder.buildDatasetXML("User_XRef_Detail_Results", rowXML.toString()));
		return rtnXML.toString();

	}

}
