package com.charon.web.myaccount;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import com.cerberus.myaccount.dal.GamePlay;
import com.cerberus.myaccount.dal.ManageUsers;
import com.cerberus.myaccount.dal.Misc;
import com.cerberus.myaccount.dal.SystemLogs;
import com.cerberus.myaccount.dal.dto.DspFindGameByName;
import com.cerberus.myaccount.dal.dto.DspFindUserByLogin;
import com.cerberus.myaccount.dal.dto.DspFindUserByXref;
import com.cerberus.myaccount.dal.dto.DspSetGameSession;
import com.cerberus.myaccount.dal.dto.DspValidateGameSession;
import com.cerberus.myaccount.dal.dto.TblUsers;
import com.cerberus.util.StringToDate;
import com.charon.exceptions.UserDataException;
import com.charon.util.CharonXML;
import com.charon.util.CharonXmlHeader;
import com.charon.util.CharonXmlUserData;
import com.charon.util.XmlDTO;

/*
 * @WebService indicates that this is webservice interface and the name
 * indicates the webservice name.
 */

@WebService(name = "GamePlay", targetNamespace = "http://charon.net", serviceName = "Charon")
/*
 * @SOAPBinding indicates binding information of soap messages. Here we have
 * document-literal style of webservice and the parameter style is wrapped.
 */
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class GamePlayWS {

	private static GamePlay _dal = new GamePlay();
	private static SystemLogs _log = new SystemLogs();
	private static CharonXML _xmlBuilder = new CharonXML();


	
	@WebMethod
	public String setGameSession(@WebParam(name = "userid") int userid, @WebParam(name = "gameid") int gameid, @WebParam(name = "source") int source,
			@WebParam(name = "expiration") Date expiration, @WebParam(name = "sessionid") String sessionid) {

		StringBuffer rtnXML = new StringBuffer();
		StringBuffer rowXML = new StringBuffer();
		DspSetGameSession _result[];
		try {

			_result = _dal.setGameSession(userid, gameid, source, expiration, sessionid);
			if (_result.length > 0) {
				for (int i = 0; i < _result.length; i++) {
					rowXML.append(_result[i].toXMLRow());
				}
				rtnXML.append(_xmlBuilder.buildDatasetXML("Game_Detail_Results", rowXML.toString()));
			} else
				rtnXML.append(_xmlBuilder.buildErrorXML("GamePlayWS.setGameSession", 0, 0, "No Results", "", ""));

		} catch (Exception e) {
			_log.insertError(0, 0, "GamePlayWS.setGameSession", e.getMessage(), e.toString());
		} finally {
			_result = null;
		}

		return rtnXML.toString();
	}

	@WebMethod
	public String validateGameSession(@WebParam(name = "userid") int UserID, @WebParam(name = "gameid") int GameID, @WebParam(name = "sourceid") int SourceID,
			@WebParam(name = "session") String Session) {
		DspValidateGameSession _result[];
		StringBuffer rtnXML = new StringBuffer();
		StringBuffer rowXML = new StringBuffer();

		try {
			_result = _dal.validateGameSession(UserID, GameID, SourceID, Session);

			if (_result.length > 0) {
				for (int i = 0; i < _result.length; i++) {
					rowXML.append(_result[i].toXMLRow());
				}
				rtnXML.append(_xmlBuilder.buildDatasetXML("Game_Detail_Results", rowXML.toString()));
			} else
				rtnXML.append(_xmlBuilder.buildErrorXML("GamePlayWS.validateGameSession", 0, 0, "No Results", "", ""));

		} catch (Exception e) {
			_log.insertError(0, 0, "GamePlayWS.validateGameSession", e.getMessage(), e.toString());
		} finally {
			_result = null;
		}

		return rtnXML.toString();
	}

	@WebMethod
	public String xRefLogin(@WebParam(name = "gamename") String gamename, @WebParam(name = "source") String source, 
			@WebParam(name = "xref") String xref, @WebParam(name = "session") String session,
			@WebParam(name = "expire") String expire, @WebParam(name = "format") String format) {

		int _gameid = 0;
		int _sourceid = 0;
		int _userid = 0;
		DspFindUserByXref rs_findxref[];
		TblUsers rs_finduser[];
		DspFindGameByName rs_findgame[];
		DspSetGameSession rs_session[];
		StringBuffer rtn_XML = new StringBuffer();
		ManageUsers _dal_users = new ManageUsers();
		Misc _dal_misc = new Misc();
		Date _sessionexpire = null;

		try {
			// find the game in question and get the gameid
			rs_findgame = _dal.findGameDetails(gamename);
			if (rs_findgame.length > 0) {
				_gameid = rs_findgame[0].getGameID();
			} else
				throw new UserDataException("Game not found.");

			// Get SourceID
			_sourceid = _dal_misc.getLookupCode("SourceType", source);
			if (_sourceid == -1)
				_sourceid = 1; // default to website
			
			//find the user
			rs_findxref = _dal_users.findUserDetailsFromXRef(xref, _gameid, _sourceid);
			if (rs_findxref.length > 0)
				_userid = rs_findxref[0].getUserID();
			else
				throw new UserDataException("User not found.");
			
			//check the account status
			rs_finduser = _dal_users.findUserByUserID(_userid);
			if (rs_finduser.length > 0) {
				if (rs_finduser[0].getAccountStatus() != 1)
					throw new UserDataException("User account is not active.");
			}
			
			// see if user can access this game
			if (!_dal.ValidateGameAccess(_userid, _gameid))
				_dal_users.insertUserGames(_userid, _gameid, 1, _sourceid, new Date());
				//TODO other game set call as required
			
			if (expire.equals("")) {
				try {
					// Give them one day
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DATE, 1);
					_sessionexpire = cal.getTime();
					cal = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else
				try {
					if (format.equals(""))
						format = "dd-MM-yyyy";
					_sessionexpire = new StringToDate().parse(expire, format);
				} catch (Exception e) {
					e.printStackTrace();
				}

			// set session
			rs_session = _dal.setGameSession(_userid, _gameid, _sourceid, _sessionexpire, session);
			if (rs_session.length > 0 && rs_session[0].getSuccess() > 0) {
				// build the return xml
				rtn_XML.append(_xmlBuilder.buildStatusXML("User_Login_Results", _gameid, _userid, _sourceid, session, source));
			} else
				rtn_XML.append(_xmlBuilder.buildErrorXML("GamePlayWS.login", 0, 10, "Failed to record the session", "", ""));

		} catch (UserDataException ue) {
			// somthing wrong with the user data so just kick it back
			rtn_XML.append(_xmlBuilder.buildErrorXML("GamePlayWS.login", 0, 10, "Login Failed", ue.getMessage(), ue.toString()));
		} catch (Exception e) {
			_log.insertError(_gameid, _userid, "GamePlayWS.login", e.getMessage(), "");
			rtn_XML.append(_xmlBuilder.buildErrorXML("GamePlayWS.login", 0, 10, "Login Failed", e.getMessage(), e.toString()));
		}

		return rtn_XML.toString();

	}

	
	
	@WebMethod
	public String login(@WebParam(name = "gamename") String gamename, @WebParam(name = "source") String source, @WebParam(name = "login") String login,
			@WebParam(name = "password") String password, @WebParam(name = "session") String session,
			@WebParam(name = "expire") String expire, @WebParam(name = "format") String format) {

		int _gameid = 0;
		int _sourceid = 0;
		int _userid = 0;
		DspFindUserByLogin rs_finduser[];
		DspFindGameByName rs_findgame[];
		DspSetGameSession rs_session[];
		StringBuffer rtn_XML = new StringBuffer();
		ManageUsers _dal_users = new ManageUsers();
		Misc _dal_misc = new Misc();
		Date _sessionexpire = null;

		try {
			// find the game in question and get the gameid
			rs_findgame = _dal.findGameDetails(gamename);
			if (rs_findgame.length > 0) {
				_gameid = rs_findgame[0].getGameID();
			} else
				throw new UserDataException("Game not found.");

			// Get SourceID
			_sourceid = _dal_misc.getLookupCode("SourceType", source);
			if (_sourceid == -1)
				_sourceid = 1; // default to website
			//find the user
			rs_finduser = _dal_users.findUserDetailsFromLogin(login, password);
			if (rs_finduser.length > 0) {
				if (rs_finduser[0].getAccountStatus() != 1)
					throw new UserDataException("User account is not active.");
				else
					_userid = rs_finduser[0].getUserID();
			} else
				throw new UserDataException("Login and Password do not match.");

			// see if user can access this game
			if (!_dal.ValidateGameAccess(_userid, _gameid))
				_dal_users.insertUserGames(_userid, _gameid, 1, _sourceid, new Date());
				//TODO other game set call as required
			
			if (expire.equals("")) {
				try {
					// Give them one day
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DATE, 1);
					_sessionexpire = cal.getTime();
					cal = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else
				try {
					if (format.equals(""))
						format = "dd-MM-yyyy";
					_sessionexpire = new StringToDate().parse(expire, format);
				} catch (Exception e) {
					e.printStackTrace();
				}

			// set session
			rs_session = _dal.setGameSession(_userid, _gameid, _sourceid, _sessionexpire, session);
			if (rs_session.length > 0 && rs_session[0].getSuccess() > 0) {
				// build the return xml
				rtn_XML.append(_xmlBuilder.buildStatusXML("User_Login_Results", _gameid, _userid, _sourceid, session, source));
			} else
				rtn_XML.append(_xmlBuilder.buildErrorXML("GamePlayWS.login", 0, 10, "Failed to record the session", "", ""));

		} catch (UserDataException ue) {
			// somthing wrong with the user data so just kick it back
			rtn_XML.append(_xmlBuilder.buildErrorXML("GamePlayWS.login", 0, 10, "Login Failed", ue.getMessage(), ue.toString()));
		} catch (Exception e) {
			_log.insertError(_gameid, _userid, "GamePlayWS.login", e.getMessage(), "");
			rtn_XML.append(_xmlBuilder.buildErrorXML("GamePlayWS.login", 0, 10, "Login Failed", e.getMessage(), e.toString()));
		}

		return rtn_XML.toString();

	}

	/*
	 * Example Login XML <?xml version="1.0"?> <Charon-XML> <header gameid=""
	 * gamename=""> <source id="" name="" /><user id="" session=""
	 * /><date></date> </header> <transaction event="login"> <userdata type=""
	 * autojoin=1> <login></login> <password></password> <xref></xref> <session
	 * expire="" format="dd-MMM-yy"></session> </userdata> </transaction>
	 * </Charon-XML>
	 */

	@WebMethod
	public String userXmlLogin(@WebParam(name = "loginxml") String loginxml) {

		StringBuffer rtn_XML = new StringBuffer();

		XmlDTO xml_root;
		XmlDTO xml_elem_transaction;
		DspSetGameSession rs_session[];
		ManageUsers _dal_users = new ManageUsers();
		CharonXmlHeader obj_Header = new CharonXmlHeader();
		CharonXmlUserData obj_UserData = new CharonXmlUserData();

		try {

			xml_root = new XmlDTO(loginxml, "Charon-XML");
			xml_elem_transaction = xml_root.child("transaction");
			// xml_elem_userdata = xml_elem_transaction.child("userdata");

			// make sure this is a login event xml
			if (xml_elem_transaction.string("event").toLowerCase().equals("login")) {
				obj_Header.ParseXML(loginxml);
				obj_UserData.ParseXML(loginxml, obj_Header);
				if (obj_UserData.GetAccountStatus() != 1) {
					throw new UserDataException("User account is not active.");
				}
				// see if user can access this game
				if (!_dal.ValidateGameAccess(obj_UserData.GetUserID(), obj_Header.GetGameID())) {

					if (obj_UserData.GetUserData().integer("autojoin") == 1) {
						try {
							_dal_users.insertUserGames(obj_UserData.GetUserID(), obj_Header.GetGameID(), 1, obj_Header.GetSourceID(), new Date());
						} catch (Exception e) {
							e.printStackTrace();
							throw new RuntimeException("Error adding game to the users list.");
						}
					} else
						throw new UserDataException("User does not have access to this game.");
				}

				// set session
				rs_session = _dal.setGameSession(obj_UserData.GetUserID(), obj_Header.GetGameID(), obj_Header.GetSourceID(), obj_UserData.GetSessionExpire(),
						obj_UserData.GetSession());
				if (rs_session.length > 0 && rs_session[0].getSuccess() > 0) {
					// build the return xml
					rtn_XML.append(_xmlBuilder.buildStatusXML("User_Login_Results", obj_Header.GetGameID(), obj_UserData.GetUserID(), obj_Header.GetSourceID(),
							obj_UserData.GetSession(), obj_Header.GetSource()));
				} else
					rtn_XML.append(_xmlBuilder.buildErrorXML("GamePlayWS.userLogin", 0, 10, "Failed to record the session", loginxml, ""));

			} else {

				rtn_XML.append(_xmlBuilder.buildErrorXML("GamePlayWS.userLogin", 0, 10, "The XML does not appear to be a valid Login XML", loginxml, ""));
			}
			xml_root = null;
			xml_elem_transaction = null;
			obj_Header = null;

		} catch (UserDataException ue) {
			// somthing wrong with the user data so just kick it back
			rtn_XML.append(_xmlBuilder.buildErrorXML("GamePlayWS.userLogin", 0, 10, "Login Failed", ue.getMessage(), ue.toString()));
		} catch (Exception e) {
			_log.insertError(obj_Header.GetGameID(), obj_UserData.GetUserID(), "GamePlayWS.userLogin", e.getMessage(), loginxml);
			rtn_XML.append(_xmlBuilder.buildErrorXML("GamePlayWS.userLogin", 0, 10, "Login Failed", e.getMessage(), e.toString()));
		}

		return rtn_XML.toString();
	}

	@WebMethod
	public String xRefRegister(@WebParam(name = "gamename") String gamename, @WebParam(name = "source") String source,
			@WebParam(name = "firstname") String firstname, @WebParam(name = "lastname") String lastname, @WebParam(name = "login") String login,
			@WebParam(name = "password") String password, @WebParam(name = "email") String email, @WebParam(name = "xref") String xref,
			@WebParam(name = "session") String session, @WebParam(name = "expire") String expire, @WebParam(name = "format") String format) {

		int _gameid = 0;
		int _sourceid = 0;
		int _userid = 0;
		DspFindGameByName rs_findgame[];
		DspSetGameSession rs_session[];
		StringBuffer rtn_XML = new StringBuffer();
		ManageUsers _dal_users = new ManageUsers();
		Misc _dal_misc = new Misc();
		Date _sessionexpire = null;
		UUID uuid;

		
		//Generate a Login and password if one is not provided
		uuid = UUID.randomUUID();
		if (login.equals(""))
			login = uuid.toString().substring(3, 11);
		if (password.equals(""))
			password = uuid.toString().substring(15, 23);
		
		
		try {
			// find the game in question and get the gameid
			rs_findgame = _dal.findGameDetails(gamename);
			if (rs_findgame.length > 0) {
				_gameid = rs_findgame[0].getGameID();
			} else
				throw new UserDataException("Game not found.");

			// Get SourceID
			_sourceid = _dal_misc.getLookupCode("SourceType", source);
			if (_sourceid == -1)
				_sourceid = 1; // default to website

			// assign xref account
			if (!xref.equals(""))
			{
				//Create the new user
				_userid = _dal_users.insertNewUser(firstname, lastname, login, password, email, 1);
				//Insert xRef record
				_dal_users.insertXref(_userid, _gameid, _sourceid, 1, xref);
			}
			else
				throw new UserDataException("xRef is required");
			
			// Give Access to this game
			_dal_users.insertUserGames(_userid, _gameid, 1, _sourceid, new Date());
				//TODO other game set call as required
			
			//Get Session Expire Date
			if (expire.equals("")) {
				try {
					// Give them one day
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DATE, 1);
					_sessionexpire = cal.getTime();
					cal = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else
				try {
					if (format.equals(""))
						format = "dd-MM-yyyy";
					_sessionexpire = new StringToDate().parse(expire, format);
				} catch (Exception e) {
					e.printStackTrace();
				}

			// set session
			rs_session = _dal.setGameSession(_userid, _gameid, _sourceid, _sessionexpire, session);
			if (rs_session.length > 0 && rs_session[0].getSuccess() > 0) {
				// build the return xml
				rtn_XML.append(_xmlBuilder.buildStatusXML("User_Register_Results", _gameid, _userid, _sourceid, session, source));
			} else
				rtn_XML.append(_xmlBuilder.buildErrorXML("GamePlayWS.xRefRegister", 0, 10, "Failed to record the session", "", ""));

		} catch (UserDataException ue) {
			// somthing wrong with the user data so just kick it back
			rtn_XML.append(_xmlBuilder.buildErrorXML("GamePlayWS.xRefRegister", 0, 10, "Register Failed", ue.getMessage(), ue.toString()));
		} catch (Exception e) {
			_log.insertError(_gameid, _userid, "GamePlayWS.xRefRegister", e.getMessage(), "");
			rtn_XML.append(_xmlBuilder.buildErrorXML("GamePlayWS.xRefRegister", 0, 10, "Register Failed", e.getMessage(), e.toString()));
		}

		return rtn_XML.toString();
		
	}

	
	
	@WebMethod
	public String register(@WebParam(name = "gamename") String gamename, @WebParam(name = "source") String source,
			@WebParam(name = "firstname") String firstname, @WebParam(name = "lastname") String lastname, @WebParam(name = "login") String login,
			@WebParam(name = "password") String password, @WebParam(name = "email") String email, 
			@WebParam(name = "session") String session, @WebParam(name = "expire") String expire, @WebParam(name = "format") String format) {

		int _gameid = 0;
		int _sourceid = 0;
		int _userid = 0;
		DspFindGameByName rs_findgame[];
		DspSetGameSession rs_session[];
		StringBuffer rtn_XML = new StringBuffer();
		ManageUsers _dal_users = new ManageUsers();
		Misc _dal_misc = new Misc();
		Date _sessionexpire = null;

		try {
			// find the game in question and get the gameid
			rs_findgame = _dal.findGameDetails(gamename);
			if (rs_findgame.length > 0) {
				_gameid = rs_findgame[0].getGameID();
			} else
				throw new UserDataException("Game not found.");

			// Get SourceID
			_sourceid = _dal_misc.getLookupCode("SourceType", source);
			if (_sourceid == -1)
				_sourceid = 1; // default to website
			
			//Create the new user
			_userid = _dal_users.insertNewUser(firstname, lastname, login, password, email, 1);

			// Give Access to this game
			_dal_users.insertUserGames(_userid, _gameid, 1, _sourceid, new Date());
				//TODO other game set call as required
			
			//Get Session Expire Date
			if (expire.equals("")) {
				try {
					// Give them one day
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DATE, 1);
					_sessionexpire = cal.getTime();
					cal = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else
				try {
					if (format.equals(""))
						format = "dd-MM-yyyy";
					_sessionexpire = new StringToDate().parse(expire, format);
				} catch (Exception e) {
					e.printStackTrace();
				}

			// set session
			rs_session = _dal.setGameSession(_userid, _gameid, _sourceid, _sessionexpire, session);
			if (rs_session.length > 0 && rs_session[0].getSuccess() > 0) {
				// build the return xml
				rtn_XML.append(_xmlBuilder.buildStatusXML("User_Register_Results", _gameid, _userid, _sourceid, session, source));
			} else
				rtn_XML.append(_xmlBuilder.buildErrorXML("GamePlayWS.register", 0, 10, "Failed to record the session", "", ""));

		} catch (UserDataException ue) {
			// somthing wrong with the user data so just kick it back
			rtn_XML.append(_xmlBuilder.buildErrorXML("GamePlayWS.register", 0, 10, "Register Failed", ue.getMessage(), ue.toString()));
		} catch (Exception e) {
			_log.insertError(_gameid, _userid, "GamePlayWS.register", e.getMessage(), "");
			rtn_XML.append(_xmlBuilder.buildErrorXML("GamePlayWS.register", 0, 10, "Register Failed", e.getMessage(), e.toString()));
		}

		return rtn_XML.toString();
		
	}

	/*
	 * <?xml version="1.0"?> <Charon-XML> <header gameid="" gamename=""> <source
	 * id="" name="" /> <user id="" session="" /> <date></date> </header>
	 * <transaction event="register"> <userdata type="new/existing">
	 * <login></login> <password></password> <firstname></firstname>
	 * <lastname></lastname> <email></email> <xref></xref> <session expire=""
	 * format="dd-MMM-yy"></session> </userdata> </transaction> </Charon-XML>
	 */
	@WebMethod
	public String userXmlRegister(@WebParam(name = "registerxml") String registerxml) {

		StringBuffer rtn_XML = new StringBuffer();
		CharonXmlHeader obj_Header = new CharonXmlHeader();
		CharonXmlUserData obj_UserData = new CharonXmlUserData();
		DspSetGameSession rs_session[];
		ManageUsers _dal_users = new ManageUsers();

		int int_UserID = 0;
		int s = 1;
		XmlDTO xml_root;
		XmlDTO xml_elem_transaction;

		xml_root = new XmlDTO(registerxml, "Charon-XML");
		xml_elem_transaction = xml_root.child("transaction");

		// make sure this is a login event xml
		if (xml_elem_transaction.string("event").toLowerCase().equals("register")) {
			try {
				obj_Header.ParseXML(registerxml);

				obj_UserData.ParseXML(registerxml, obj_Header);

				if (!obj_UserData.GetXRef().equals(""))
					// may be an xref so try to make a login and password just
					// incase
					obj_UserData.GenerateLoginDetails();

				// create the user account
				int_UserID = _dal_users.insertNewUser(obj_UserData.GetFirstName(), obj_UserData.GetLastName(), obj_UserData.GetLogin(), obj_UserData
						.GetPassword(), obj_UserData.GetEmail(), 1);
				// link the game and the user
				_dal_users.insertUserGames(int_UserID, obj_Header.GetGameID(), 1, obj_Header.GetSourceID(), Calendar.getInstance().getTime());
				// assign xref account
				if (!obj_UserData.GetXRef().equals(""))
					_dal_users.insertXref(int_UserID, obj_Header.GetGameID(), obj_Header.GetSourceID(), s, obj_UserData.GetXRef());

				// set the game session if I have it
				if (!obj_UserData.GetSession().equals("")) {
					// set session
					rs_session = _dal.setGameSession(obj_UserData.GetUserID(), obj_Header.GetGameID(), obj_Header.GetSourceID(), obj_UserData
							.GetSessionExpire(), obj_UserData.GetSession());

					if (rs_session.length > 0 && rs_session[0].getSuccess() > 1) {
						// build the return xml
						rtn_XML.append(_xmlBuilder.buildStatusXML("User_Register_Results", obj_Header.GetGameID(), obj_UserData.GetUserID(), obj_Header
								.GetSourceID(), obj_UserData.GetSession(), obj_Header.GetSource()));
					} else
						rtn_XML.append(_xmlBuilder.buildErrorXML("GamePlayWS.userXmlRegister", 0, 10, "Failed to record the session", registerxml, ""));
				} else
					rtn_XML.append(_xmlBuilder.buildStatusXML("User_Register_Results", obj_Header.GetGameID(), obj_UserData.GetUserID(), obj_Header
							.GetSourceID(), obj_UserData.GetSession(), obj_Header.GetSource()));
			} catch (UserDataException e) {
				rtn_XML.append(_xmlBuilder.buildErrorXML("GamePlayWS.userXmlRegister", 0, 10, "Login Failed", e.getMessage(), e.toString()));
			} catch (Exception e) {
				_log.insertError(obj_Header.GetGameID(), obj_UserData.GetUserID(), "GamePlayWS.userXmlRegister", e.getMessage(), registerxml);
				rtn_XML.append(_xmlBuilder.buildErrorXML("GamePlayWS.userXmlRegister", 0, 10, "Login Failed", e.getMessage(), e.toString()));
			}

		} else
			rtn_XML.append(_xmlBuilder.buildErrorXML("GamePlayWS.userXmlRegister", 0, 10, "Invalid XML", registerxml, ""));

		return rtn_XML.toString();
	}
	


}
