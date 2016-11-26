package com.charon.web.myaccount;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import com.cerberus.myaccount.dal.SystemLogs;
import com.cerberus.myaccount.dal.dto.TblErrorLog;
import com.cerberus.myaccount.dal.dto.TblMsgArchive;
import com.cerberus.myaccount.dal.dto.TblUserEvents;
import com.cerberus.myaccount.dal.dto.TblUserGameActivity;

/*
 * @WebService indicates that this is webservice interface and the name
 * indicates the webservice name.
 */

@WebService(name = "SystemLogs", targetNamespace = "http://charon.net", serviceName = "Charon")
/*
 * @SOAPBinding indicates binding information of soap messages. Here we have
 * document-literal style of webservice and the parameter style is wrapped.
 */
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class SystemLogsWS {

	private static SystemLogs _dal = new SystemLogs();

	/**
	 * Method 'insertMessage'
	 * 
	 * @param iGameID
	 * @param iUserID
	 * @param XMLMsg
	 */
	@WebMethod
	public  void insertMessage(@WebParam(name = "GameID") int iGameID, @WebParam(name = "UserID") int iUserID, @WebParam(name = "XMLMsg") String XMLMsg) {
		try {
			_dal.insertMessage(iGameID, iUserID, XMLMsg);

		} catch (Exception _e) {
			_dal.insertError(0, 0, "SystemLogsWS.insertMessage", _e.getMessage(), _e.toString());
		}

	}

	/**
	 * Method 'findAllArchiveMsgs'
	 * 
	 * 
	 */
	@WebMethod
	public  String findAllArchiveMsgs() {
		StringBuffer rtnXML = new StringBuffer();
		try {
			TblMsgArchive _result[] = _dal.findAllArchiveMsgs();
			rtnXML.append("<?xml version=\"1.0\"?><Charon-XML><header id=\"MsgArchive_Detail_Results\"><source>tbl_msg_archive</source><date>");
			rtnXML.append(new Date().toString());
			rtnXML.append("</date></header><dataset>");

			for (int i = 0; i < _result.length; i++) {
				rtnXML.append(_result[i].toXMLRow());
			}

			rtnXML.append("</dataset></Charon-XML>");
			_result = null;
		} catch (Exception _e) {
			_dal.insertError(0, 0, "SystemLogsWS.findAllArchiveMsgs", _e.getMessage(), _e.toString());
		}

		return rtnXML.toString();
	}

	/**
	 * Method 'findWhereArchiveIDEquals'
	 * 
	 * @param archiveID
	 */
	@WebMethod
	public  String findWhereArchiveIDEquals(@WebParam(name = "archiveID") int archiveID) {
		StringBuffer rtnXML = new StringBuffer();

		try {
			TblMsgArchive _result[] = _dal.findWhereArchiveIDEquals(archiveID);
			rtnXML.append("<?xml version=\"1.0\"?><Charon-XML><header id=\"MsgArchive_Detail_Results\"><source>tbl_msg_archive</source><date>");
			rtnXML.append(new Date().toString());
			rtnXML.append("</date></header><dataset>");

			for (int i = 0; i < _result.length; i++) {
				rtnXML.append(_result[i].toXMLRow());
			}

			rtnXML.append("</dataset></Charon-XML>");
			_result = null;

		} catch (Exception _e) {
			_dal.insertError(0, 0, "SystemLogsWS.findWhereArchiveIDEquals", _e.getMessage(), _e.toString());
		}
		return rtnXML.toString();
	}

	/**
	 * Method 'findWhereMsgDateEquals'
	 * 
	 * @param msgDate
	 */
	@WebMethod
	public  String findWhereMsgDateEquals(@WebParam(name = "msgDate") Date msgDate) {
		StringBuffer rtnXML = new StringBuffer();

		try {
			TblMsgArchive _result[] = _dal.findWhereMsgDateEquals(msgDate);
			rtnXML.append("<?xml version=\"1.0\"?><Charon-XML><header id=\"MsgArchive_Detail_Results\"><source>tbl_msg_archive</source><date>");
			rtnXML.append(new Date().toString());
			rtnXML.append("</date></header><dataset>");

			for (int i = 0; i < _result.length; i++) {
				rtnXML.append(_result[i].toXMLRow());
			}

			rtnXML.append("</dataset></Charon-XML>");
			_result = null;

		} catch (Exception _e) {
			_dal.insertError(0, 0, "SystemLogsWS.findWhereMsgDateEquals", _e.getMessage(), _e.toString());
		}
		return rtnXML.toString();
	}

	/**
	 * Method 'findWhereGameIDEquals'
	 * 
	 * @param gameID
	 */
	@WebMethod
	public  String findWhereMsgGameIDEquals(@WebParam(name = "gameID") int gameID) {
		StringBuffer rtnXML = new StringBuffer();

		try {
			TblMsgArchive _result[] = _dal.findWhereMsgGameIDEquals(gameID);
			rtnXML.append("<?xml version=\"1.0\"?><Charon-XML><header id=\"MsgArchive_Detail_Results\"><source>tbl_msg_archive</source><date>");
			rtnXML.append(new Date().toString());
			rtnXML.append("</date></header><dataset>");

			for (int i = 0; i < _result.length; i++) {
				rtnXML.append(_result[i].toXMLRow());
			}

			rtnXML.append("</dataset></Charon-XML>");
			_result = null;

		} catch (Exception _e) {
			_dal.insertError(0, 0, "SystemLogsWS.findWhereMsgGameIDEquals", _e.getMessage(), _e.toString());
		}
		return rtnXML.toString();
	}

	/**
	 * Method 'findWhereUserIDEquals'
	 * 
	 * @param userID
	 */
	@WebMethod
	public  String findWhereMsgUserIDEquals(@WebParam(name = "userID") int userID) {
		StringBuffer rtnXML = new StringBuffer();

		try {
			TblMsgArchive _result[] = _dal.findWhereMsgUserIDEquals(userID);
			rtnXML.append("<?xml version=\"1.0\"?><Charon-XML><header id=\"MsgArchive_Detail_Results\"><source>tbl_msg_archive</source><date>");
			rtnXML.append(new Date().toString());
			rtnXML.append("</date></header><dataset>");

			for (int i = 0; i < _result.length; i++) {
				rtnXML.append(_result[i].toXMLRow());
			}

			rtnXML.append("</dataset></Charon-XML>");
			_result = null;

		} catch (Exception _e) {
			_dal.insertError(0, 0, "SystemLogsWS.findWhereMsgUserIDEquals", _e.getMessage(), _e.toString());
		}
		return rtnXML.toString();
	}

	/****************************************************************
	 * TblErrorLogDao
	 ******************************************************************/

	/**
	 * Method 'insertError'
	 * 
	 * @param iGameID
	 * @param iUserID
	 * @param strProcessName
	 * @param strErrorMessage
	 * @param XMLMsg
	 */
	@WebMethod
	public  void insertError(@WebParam(name = "GameID") int iGameID, @WebParam(name = "UserID") int iUserID,
			@WebParam(name = "ProcessName") String strProcessName, @WebParam(name = "ErrorMessage") String strErrorMessage,
			@WebParam(name = "XMLMsg") String XMLMsg) {

		try {

			_dal.insertError(iGameID, iUserID, strProcessName, strErrorMessage, XMLMsg);

		} catch (Exception _e) {
			System.out.println(_e.toString());
		}

	}

	/**
	 * Method 'findAll'
	 * 
	 */
	@WebMethod
	public  String findAllErrorLog() {
		StringBuffer rtnXML = new StringBuffer();
		try {
			TblErrorLog _result[] = _dal.findAllErrorLog();
			rtnXML.append("<?xml version=\"1.0\"?><Charon-XML><header id=\"ErrorLog_Detail_Results\"><source>tbl_errror_log</source><date>");
			rtnXML.append(new Date().toString());
			rtnXML.append("</date></header><dataset>");

			for (int i = 0; i < _result.length; i++) {
				rtnXML.append(_result[i].toXMLRow());
			}

			rtnXML.append("</dataset></Charon-XML>");
			_result = null;

		} catch (Exception _e) {
			_dal.insertError(0, 0, "SystemLogsWS.findAllErrorLog", _e.getMessage(), _e.toString());
		}
		return rtnXML.toString();
	}

	/**
	 * Method 'findWhereErrorIDEquals'
	 * 
	 * @param errorID
	 */
	@WebMethod
	public  String findWhereErrorIDEquals(@WebParam(name = "errorID") int errorID) {
		StringBuffer rtnXML = new StringBuffer();
		try {
			TblErrorLog _result[] = _dal.findWhereErrorIDEquals(errorID);
			rtnXML.append("<?xml version=\"1.0\"?><Charon-XML><header id=\"ErrorLog_Detail_Results\"><source>tbl_errror_log</source><date>");
			rtnXML.append(new Date().toString());
			rtnXML.append("</date></header><dataset>");

			for (int i = 0; i < _result.length; i++) {
				rtnXML.append(_result[i].toXMLRow());
			}

			rtnXML.append("</dataset></Charon-XML>");
			_result = null;

		} catch (Exception _e) {
			_dal.insertError(0, 0, "SystemLogsWS.findWhereErrorIDEquals", _e.getMessage(), _e.toString());
		}
		return rtnXML.toString();
	}

	/**
	 * Method 'findWhereErrorDateEquals'
	 * 
	 * @param errorDate
	 */
	@WebMethod
	public  String findWhereErrorDateEquals(@WebParam(name = "errorDate") Date errorDate) {
		StringBuffer rtnXML = new StringBuffer();
		try {
			TblErrorLog _result[] = _dal.findWhereErrorDateEquals(errorDate);
			rtnXML.append("<?xml version=\"1.0\"?><Charon-XML><header id=\"ErrorLog_Detail_Results\"><source>tbl_errror_log</source><date>");
			rtnXML.append(new Date().toString());
			rtnXML.append("</date></header><dataset>");

			for (int i = 0; i < _result.length; i++) {
				rtnXML.append(_result[i].toXMLRow());
			}

			rtnXML.append("</dataset></Charon-XML>");
			_result = null;

		} catch (Exception _e) {
			_dal.insertError(0, 0, "SystemLogsWS.findWhereErrorDateEquals", _e.getMessage(), _e.toString());
		}
		return rtnXML.toString();
	}

	/**
	 * Method 'findWhereGameIDEquals'
	 * 
	 * @param gameID
	 */
	@WebMethod
	public  String findWhereErrorGameIDEquals(@WebParam(name = "gameID") int gameID) {
		StringBuffer rtnXML = new StringBuffer();
		try {
			TblErrorLog _result[] = _dal.findWhereErrorGameIDEquals(gameID);
			rtnXML.append("<?xml version=\"1.0\"?><Charon-XML><header id=\"ErrorLog_Detail_Results\"><source>tbl_errror_log</source><date>");
			rtnXML.append(new Date().toString());
			rtnXML.append("</date></header><dataset>");

			for (int i = 0; i < _result.length; i++) {
				rtnXML.append(_result[i].toXMLRow());
			}

			rtnXML.append("</dataset></Charon-XML>");
			_result = null;

		} catch (Exception _e) {
			_dal.insertError(0, 0, "SystemLogsWS.findWhereErrorGameIDEquals", _e.getMessage(), _e.toString());
		}
		return rtnXML.toString();
	}

	/**
	 * Method 'findWhereUserIDEquals'
	 * 
	 * @param userID
	 */
	@WebMethod
	public  String findWhereErrorUserIDEquals(@WebParam(name = "userID") int userID) {
		StringBuffer rtnXML = new StringBuffer();
		try {
			TblErrorLog _result[] = _dal.findWhereErrorUserIDEquals(userID);
			rtnXML.append("<?xml version=\"1.0\"?><Charon-XML><header id=\"ErrorLog_Detail_Results\"><source>tbl_errror_log</source><date>");
			rtnXML.append(new Date().toString());
			rtnXML.append("</date></header><dataset>");

			for (int i = 0; i < _result.length; i++) {
				rtnXML.append(_result[i].toXMLRow());
			}

			rtnXML.append("</dataset></Charon-XML>");
			_result = null;

		} catch (Exception _e) {
			_dal.insertError(0, 0, "SystemLogsWS.findWhereErrorUserIDEquals", _e.getMessage(), _e.toString());
		}
		return rtnXML.toString();
	}

	/**
	 * Method 'findWhereProcessNameEquals'
	 * 
	 * @param processName
	 */
	@WebMethod
	public  String findWhereProcessNameEquals(@WebParam(name = "processName") String processName) {
		StringBuffer rtnXML = new StringBuffer();
		try {
			TblErrorLog _result[] = _dal.findWhereProcessNameEquals(processName);
			rtnXML.append("<?xml version=\"1.0\"?><Charon-XML><header id=\"ErrorLog_Detail_Results\"><source>tbl_errror_log</source><date>");
			rtnXML.append(new Date().toString());
			rtnXML.append("</date></header><dataset>");

			for (int i = 0; i < _result.length; i++) {
				rtnXML.append(_result[i].toXMLRow());
			}

			rtnXML.append("</dataset></Charon-XML>");
			_result = null;

		} catch (Exception _e) {
			_dal.insertError(0, 0, "SystemLogsWS.findWhereProcessNameEquals", _e.getMessage(), _e.toString());
		}
		return rtnXML.toString();
	}

	/****************************************************************
	 * TblUserEventsDao
	 ******************************************************************/

	@WebMethod
	public  void insertUserEvent(@WebParam(name = "EventCode") int iEventCode, @WebParam(name = "UserID") int iUserID,
			@WebParam(name = "Comments") String strComments) {
		try {
			_dal.insertUserEvent(iEventCode, iUserID, strComments);

		} catch (Exception _e) {
			_dal.insertError(0, 0, "SystemLogsWS.insertUserEvent", _e.getMessage(), _e.toString());
		}

	}

	/**
	 * Method 'findAll'
	 * 
	 */
	@WebMethod
	public  String findAllUserEvents() {
		StringBuffer rtnXML = new StringBuffer();
		try {
			TblUserEvents _result[] = _dal.findAllUserEvents();
			rtnXML.append("<?xml version=\"1.0\"?><Charon-XML><header id=\"UserEvents_Detail_Results\"><source>tbl_User_events</source><date>");
			rtnXML.append(new Date().toString());
			rtnXML.append("</date></header><dataset>");

			for (int i = 0; i < _result.length; i++) {
				rtnXML.append(_result[i].toXMLRow());
			}

			rtnXML.append("</dataset></Charon-XML>");
			_result = null;

		} catch (Exception _e) {
			_dal.insertError(0, 0, "SystemLogsWS.findAllUserEvents", _e.getMessage(), _e.toString());
		}
		return rtnXML.toString();
	}

	/**
	 * Method 'findWhereEventCodeEquals'
	 * 
	 * @param eventCode
	 */
	@WebMethod
	public  String findWhereEventCodeEquals(@WebParam(name = "eventCode") int eventCode) {
		StringBuffer rtnXML = new StringBuffer();
		try {
			TblUserEvents _result[] = _dal.findWhereEventCodeEquals(eventCode);
			rtnXML.append("<?xml version=\"1.0\"?><Charon-XML><header id=\"UserEvents_Detail_Results\"><source>tbl_User_events</source><date>");
			rtnXML.append(new Date().toString());
			rtnXML.append("</date></header><dataset>");

			for (int i = 0; i < _result.length; i++) {
				rtnXML.append(_result[i].toXMLRow());
			}

			rtnXML.append("</dataset></Charon-XML>");
			_result = null;

		} catch (Exception _e) {
			_dal.insertError(0, 0, "SystemLogsWS.findWhereEventCodeEquals", _e.getMessage(), _e.toString());
		}
		return rtnXML.toString();
	}

	/**
	 * Method 'findWhereUserIDEquals'
	 * 
	 * @param userID
	 */
	@WebMethod
	public  String findWhereEventUserIDEquals(@WebParam(name = "userID") int userID) {
		StringBuffer rtnXML = new StringBuffer();
		try {
			TblUserEvents _result[] = _dal.findWhereEventUserIDEquals(userID);
			rtnXML.append("<?xml version=\"1.0\"?><Charon-XML><header id=\"UserEvents_Detail_Results\"><source>tbl_User_events</source><date>");
			rtnXML.append(new Date().toString());
			rtnXML.append("</date></header><dataset>");

			for (int i = 0; i < _result.length; i++) {
				rtnXML.append(_result[i].toXMLRow());
			}

			rtnXML.append("</dataset></Charon-XML>");
			_result = null;
		} catch (Exception _e) {
			_dal.insertError(0, 0, "SystemLogsWS.findWhereEventUserIDEquals", _e.getMessage(), _e.toString());
		}
		return rtnXML.toString();
	}

	/**
	 * Method 'findWhereEventDateEquals'
	 * 
	 * @param eventDate
	 */
	@WebMethod
	public  String findWhereEventDateEquals(@WebParam(name = "eventDate") Date eventDate) {
		StringBuffer rtnXML = new StringBuffer();
		try {
			TblUserEvents _result[] = _dal.findWhereEventDateEquals(eventDate);
			rtnXML.append("<?xml version=\"1.0\"?><Charon-XML><header id=\"UserEvents_Detail_Results\"><source>tbl_User_events</source><date>");
			rtnXML.append(new Date().toString());
			rtnXML.append("</date></header><dataset>");

			for (int i = 0; i < _result.length; i++) {
				rtnXML.append(_result[i].toXMLRow());
			}

			rtnXML.append("</dataset></Charon-XML>");
			_result = null;
		} catch (Exception _e) {
			_dal.insertError(0, 0, "SystemLogsWS.findWhereEventDateEquals", _e.getMessage(), _e.toString());
		}
		return rtnXML.toString();
	}

	/****************************************************************
	 * TblUserGameActivity
	 ******************************************************************/

	@WebMethod
	public  void insertUserGameActivities(@WebParam(name = "GameID") int iGameID, @WebParam(name = "UserID") int iUserID,
			@WebParam(name = "Source") int iSource) {
		try {
			_dal.insertUserGameActivities(iGameID, iUserID, iSource);

		} catch (Exception _e) {
			_dal.insertError(0, 0, "SystemLogsWS.insertUserGameActivities", _e.getMessage(), _e.toString());
		}
	}

	/**
	 * Method 'findAll'
	 * 
	 */
	@WebMethod
	public  String findAllGameActivity() {
		StringBuffer rtnXML = new StringBuffer();
		try {
			TblUserGameActivity _result[] = _dal.findAllGameActivity();
			rtnXML.append("<?xml version=\"1.0\"?><Charon-XML><header id=\"UserGameActivity_Detail_Results\"><source>tbl_User_Game_Activity</source><date>");
			rtnXML.append(new Date().toString());
			rtnXML.append("</date></header><dataset>");

			for (int i = 0; i < _result.length; i++) {
				rtnXML.append(_result[i].toXMLRow());
			}

			rtnXML.append("</dataset></Charon-XML>");
			_result = null;
		} catch (Exception _e) {
			_dal.insertError(0, 0, "SystemLogsWS.findAllGameActivity", _e.getMessage(), _e.toString());
		}
		return rtnXML.toString();
	}

	/**
	 * Method 'findWhereUserIDEquals'
	 * 
	 * @param userID
	 */
	@WebMethod
	public  String findWhereUserIDEquals(@WebParam(name = "userID") int userID) {
		StringBuffer rtnXML = new StringBuffer();
		try {
			TblUserGameActivity _result[] = _dal.findWhereUserIDEquals(userID);
			rtnXML.append("<?xml version=\"1.0\"?><Charon-XML><header id=\"UserGameActivity_Detail_Results\"><source>tbl_User_Game_Activity</source><date>");
			rtnXML.append(new Date().toString());
			rtnXML.append("</date></header><dataset>");

			for (int i = 0; i < _result.length; i++) {
				rtnXML.append(_result[i].toXMLRow());
			}

			rtnXML.append("</dataset></Charon-XML>");
			_result = null;
		} catch (Exception _e) {
			_dal.insertError(0, 0, "SystemLogsWS.findWhereUserIDEquals", _e.getMessage(), _e.toString());
		}
		return rtnXML.toString();
	}

	/**
	 * Method 'findWhereGameIDEquals'
	 * 
	 * @param gameID
	 */
	@WebMethod
	public  String findWhereGameIDEquals(@WebParam(name = "gameID") int gameID) {
		StringBuffer rtnXML = new StringBuffer();
		try {

			TblUserGameActivity _result[] = _dal.findWhereGameIDEquals(gameID);
			rtnXML.append("<?xml version=\"1.0\"?><Charon-XML><header id=\"UserGameActivity_Detail_Results\"><source>tbl_User_Game_Activity</source><date>");
			rtnXML.append(new Date().toString());
			rtnXML.append("</date></header><dataset>");

			for (int i = 0; i < _result.length; i++) {
				rtnXML.append(_result[i].toXMLRow());
			}

			rtnXML.append("</dataset></Charon-XML>");
			_result = null;
		} catch (Exception _e) {
			_dal.insertError(0, 0, "SystemLogsWS.findWhereGameIDEquals", _e.getMessage(), _e.toString());
		}
		return rtnXML.toString();
	}

	/**
	 * Method 'findWhereSourceEquals'
	 * 
	 * @param source
	 */
	@WebMethod
	public  String findWhereSourceEquals(@WebParam(name = "source") int source) {
		StringBuffer rtnXML = new StringBuffer();
		try {

			TblUserGameActivity _result[] = _dal.findWhereSourceEquals(source);
			rtnXML.append("<?xml version=\"1.0\"?><Charon-XML><header id=\"UserGameActivity_Detail_Results\"><source>tbl_User_Game_Activity</source><date>");
			rtnXML.append(new Date().toString());
			rtnXML.append("</date></header><dataset>");

			for (int i = 0; i < _result.length; i++) {
				rtnXML.append(_result[i].toXMLRow());
			}

			rtnXML.append("</dataset></Charon-XML>");
			_result = null;
		} catch (Exception _e) {
			_dal.insertError(0, 0, "SystemLogsWS.findWhereSourceEquals", _e.getMessage(), _e.toString());
		}
		return rtnXML.toString();
	}


}
