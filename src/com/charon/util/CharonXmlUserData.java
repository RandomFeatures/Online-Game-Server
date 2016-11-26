/**
 * Convert the user data or xml into a Java object
 * At the time I was building it seemed like it would be more
 * useful than it really is. I have chosen to leave it here just incase
 * 
 */

package com.charon.util;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import com.cerberus.myaccount.dal.ManageUsers;
import com.cerberus.myaccount.dal.dto.DspFindUserByLogin;
import com.cerberus.myaccount.dal.dto.DspFindUserByXref;
import com.cerberus.myaccount.dal.dto.TblUsers;
import com.cerberus.util.StringToDate;
import com.charon.exceptions.UserDataException;

/*	
 <?xml version="1.0"?>
 <Charon-XML>
 <header gameid="" gamename="">
 <source id="" name="" />
 <user id="" session="" />
 <date></date>
 </header>
 <transaction event="register">
 <userdata type="new/existing">
 <login></login>
 <password></password>
 <firstname></firstname>
 <lastname></lastname>
 <email></email>
 <xref></xref>
 <session expire="" format="dd-MMM-yy"></session>
 </userdata>
 </transaction>
 </Charon-XML>

 */

public class CharonXmlUserData {

	private String _login = "";
	private String _password = "";
	private int _userid = 0;
	private String _firstname = "";
	private String _lastname = "";
	private String _email = "";
	private String _xref = "";
	private int _accountstatus = 0;
	private XmlDTO _xml_elem_userdata;
	private String _session = "";
	private Date _sessionexpire = null;
	private CharonXmlHeader _header = null;

	public CharonXmlUserData() {

	}

	public void SetXmlHeader(CharonXmlHeader header) {
		_header = header;
	}

	public String GetLogin() {
		return _login;
	}

	public String GetPassword() {
		return _password;
	}

	public int GetUserID() {
		return _userid;
	}

	public String GetFirstName() {
		return _firstname;
	}

	public String GetLastName() {
		return _lastname;
	}

	public String GetEmail() {
		return _email;
	}

	public String GetXRef() {
		return _xref;
	}

	public int GetAccountStatus() {
		return _accountstatus;
	}

	public XmlDTO GetUserData() {
		return _xml_elem_userdata;
	}

	public String GetSession() {
		return _session;
	}

	public Date GetSessionExpire() {
		return _sessionexpire;
	}

	public void GenerateLoginDetails() {
		UUID uuid;
		uuid = UUID.randomUUID();
		if (_login.equals(""))
			_login = uuid.toString().substring(3, 11);
		if (_password.equals(""))
			_password = uuid.toString().substring(15, 23);
	}

	public void ParseXML(String strXML, CharonXmlHeader header) throws UserDataException {
		_header = header;
		try {
			this.ParseXML(strXML);
		} catch (UserDataException e) {
			throw new UserDataException(e);
		}
	}

	public void ParseXML(String strXML) throws UserDataException {
		_login = "";
		_password = "";
		_userid = 0;
		_firstname = "";
		_lastname = "";
		_email = "";
		_xref = "";
		_accountstatus = 0;
		_session = "";
		_sessionexpire = null;

		XmlDTO xml_root;
		XmlDTO xml_elem_transaction;

		DspFindUserByLogin rs_finduser[];
		DspFindUserByXref rs_xref[];
		TblUsers rs_users[];
		ManageUsers _dal_users = new ManageUsers();

		xml_root = new XmlDTO(strXML, "Charon-XML");
		xml_elem_transaction = xml_root.child("transaction");
		_xml_elem_userdata = xml_elem_transaction.child("userdata");

		_login = _xml_elem_userdata.child("login").content();
		_password = _xml_elem_userdata.child("password").content();

		try {
			_firstname = _xml_elem_userdata.child("firstname").content();
			_lastname = _xml_elem_userdata.child("lastname").content();
			_email = _xml_elem_userdata.child("email").content();
			_xref = _xml_elem_userdata.child("xref").content();
		} catch (Exception e1) {
		}

		// ** SessionID **//
		_session = _xml_elem_userdata.child("session").content();
		if (_xml_elem_userdata.child("session").string("expire").equals("")) {
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
				_sessionexpire = new StringToDate().parse(_xml_elem_userdata.child("session").string("expire"), _xml_elem_userdata.child("session").string(
						"format"));
			} catch (Exception e) {
				e.printStackTrace();
			}

		if (xml_elem_transaction.string("event").toLowerCase().equals("login")) {
			// get user id from login
			if (_xml_elem_userdata.string("type").toLowerCase().equals("login")) {
				// validate login

				// ** UserID **//
				try {

					rs_finduser = _dal_users.findUserDetailsFromLogin(_login, _password);

					if (rs_finduser.length > 0 && rs_finduser[0].getAccountStatus() == 1) {
						_userid = rs_finduser[0].getUserID();
						_firstname = rs_finduser[0].getFirstName();
						_lastname = rs_finduser[0].getLastName();
						_email = rs_finduser[0].getEmail();
					} else {
						if (rs_finduser.length == 0) {

							try {
								rs_users = _dal_users.findUserByLogin(_login);
								// see if the user is actually in the database
								if (rs_users.length > 0) {
									// just the wrong password
									throw new UserDataException("Login and Password do not match.");
								} else {
									// wrong user login
									throw new UserDataException("Login and/or Password do not match.");
								}
							} catch (UserDataException ue) {
								throw new UserDataException(ue);
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								rs_users = null;
							}

						} else if (rs_finduser.length > 0 && rs_finduser[0].getAccountStatus() == 0)
							throw new UserDataException("User account is not active.");
					}

				} catch (UserDataException ue) {
					throw new UserDataException(ue);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					rs_finduser = null;
				}

			} // get user id from xref
			else if (_xml_elem_userdata.string("type").equals("reference")) {
				if (_header == null) {
					_header = new CharonXmlHeader();
					_header.ParseXML(strXML);
				}
				_xref = _xml_elem_userdata.child("xref").content();
				try {

					rs_xref = _dal_users.findUserDetailsFromXRef(_xref, _header.GetGameID(), _header.GetSourceID());
					if (rs_xref.length > 0) {
						_userid = rs_xref[0].getUserID();
					} else {
						throw new UserDataException("External reference does not match user data.");
					}

				} catch (UserDataException ue) {
					throw new UserDataException(ue);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					rs_xref = null;
				}

			}

			// Get the rest of the user data if possible
			try {
				if (_userid > 0) {
					rs_users = _dal_users.findUserByUserID(_userid);
					if (rs_users.length > 0) {
						_login = rs_users[0].getLogin();
						_password = rs_users[0].getPassword();
						_firstname = rs_users[0].getFirstName();
						_lastname = rs_users[0].getLastName();
						_email = rs_users[0].getEmail();
						_accountstatus = rs_users[0].getAccountStatus();
					} else
						throw new UserDataException("User not found.");
				}
			} catch (UserDataException ue) {
				throw new UserDataException(ue);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}
