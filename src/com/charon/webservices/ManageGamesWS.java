package com.charon.web.myaccount;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import com.cerberus.myaccount.dal.GamePlay;
import com.cerberus.myaccount.dal.ManageGames;
import com.cerberus.myaccount.dal.SystemLogs;
import com.cerberus.myaccount.dal.dto.DspFindGameByName;
import com.cerberus.myaccount.dal.dto.TblGameSources;
import com.cerberus.myaccount.dal.dto.TblGames;
import com.charon.util.CharonXML;

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

@WebService(name = "ManageGames", targetNamespace = "http://charon.net", serviceName = "Charon")
/*
 * @SOAPBinding indicates binding information of soap messages. Here we have
 * document-literal style of webservice and the parameter style is wrapped.
 */
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class ManageGamesWS {

	private static ManageGames _dal = new ManageGames();
	private static GamePlay _dalgameplay = new GamePlay();
	private static SystemLogs _log = new SystemLogs();
	private static CharonXML _xmlBuilder = new CharonXML();

	@WebMethod
	public String findGameDetails(@WebParam(name = "game") String game) {
		StringBuffer rtnXML = new StringBuffer();
		StringBuffer rowXML = new StringBuffer();
		DspFindGameByName _result[];

		try {

			_result = _dalgameplay.findGameDetails(game);
			if (_result.length > 0) {
				for (int i = 0; i < _result.length; i++) {
					rowXML.append(_result[i].toXMLRow());
				}

				rtnXML.append(_xmlBuilder.buildDatasetXML("Game_Detail_Results", rowXML.toString()));
			} else
				rtnXML.append(_xmlBuilder.buildErrorXML("ManageGamesWS.findGameDetails", 0, 10, "Failed to find Game", "", ""));
		} catch (Exception _e) {
			_log.insertError(0, 0, "ManageGamesWS.findGameDetails", _e.getMessage(), _e.toString());
		} finally {
			_result = null;
		}

		return rtnXML.toString();
	}

	/**
	 * Method 'findAll'
	 * 
	 */
	@WebMethod
	public String findAllGames() {
		String rtnXML = "";
		TblGames _result[];
		try {
			_result = _dal.findAllGames();
			if (_result.length > 0)
				rtnXML = buildReturnXML(_result);
			else
				rtnXML = _xmlBuilder.buildErrorXML("ManageGamesWS.findAllGames", 0, 10, "Failed to find Game", "", "");
		} catch (Exception _e) {
			_log.insertError(0, 0, "ManageGamesWS.findAllGames", _e.getMessage(), _e.toString());
		} finally {
			_result = null;
		}
		return rtnXML;
	}

	/**
	 * Method 'findWhereGameIDEquals'
	 * 
	 * @param gameID
	 */
	@WebMethod
	public String findGameByGameID(@WebParam(name = "gameID") int gameID) {
		String rtnXML = "";
		TblGames _result[];
		try {
			_result = _dal.findGameByGameID(gameID);
			if (_result.length > 0)
				rtnXML = buildReturnXML(_result);
			else
				rtnXML = _xmlBuilder.buildErrorXML("ManageGamesWS.findGameByGameID", 0, 10, "Failed to find Game", "", "");
		} catch (Exception _e) {
			_log.insertError(0, 0, "ManageGamesWS.findGameByGameID", _e.getMessage(), _e.toString());
		} finally {
			_result = null;
		}
		return rtnXML;
	}

	/**
	 * Method 'findWhereGameNameEquals'
	 * 
	 * @param gameName
	 */
	@WebMethod
	public String findGameByName(@WebParam(name = "gameName") String gameName) {
		String rtnXML = "";
		TblGames _result[];
		try {
			_result = _dal.findGameByName(gameName);
			if (_result.length > 0)
				rtnXML = buildReturnXML(_result);
			else
				rtnXML = _xmlBuilder.buildErrorXML("ManageGamesWS.findGameByName", 0, 10, "Failed to find Game", "", "");
		} catch (Exception _e) {
			_log.insertError(0, 0, "ManageGamesWS.findGameByName", _e.getMessage(), _e.toString());
		} finally {
			_result = null;
		}
		return rtnXML;
	}

	/**
	 * Method 'findWhereStartDateEquals'
	 * 
	 * @param startDate
	 */
	@WebMethod
	public String findGameByStartDate(@WebParam(name = "startDate") Date startDate) {
		String rtnXML = "";
		TblGames _result[];
		try {
			_result = _dal.findGameByStartDate(startDate);
			if (_result.length > 0)
				rtnXML = buildReturnXML(_result);
			else
				rtnXML = _xmlBuilder.buildErrorXML("ManageGamesWS.findGameByStartDate", 0, 10, "Failed to find Game", "", "");
		} catch (Exception _e) {
			_log.insertError(0, 0, "ManageGamesWS.findGameByStartDate", _e.getMessage(), _e.toString());
		} finally {
			_result = null;
		}
		return rtnXML;
	}

	/**
	 * Method 'findWhereEndDateEquals'
	 * 
	 * @param endDate
	 */
	@WebMethod
	public String findGameByEndDate(@WebParam(name = "endDate") Date endDate) {
		String rtnXML = "";
		TblGames _result[];
		try {
			_result = _dal.findGameByEndDate(endDate);
			if (_result.length > 0)
				rtnXML = buildReturnXML(_result);
			else
				rtnXML = _xmlBuilder.buildErrorXML("ManageGamesWS.findGameByEndDate", 0, 10, "Failed to find Game", "", "");
		} catch (Exception _e) {
			_log.insertError(0, 0, "ManageGamesWS.findGameByEndDate", _e.getMessage(), _e.toString());
		} finally {
			_result = null;
		}
		return rtnXML;
	}

	/**
	 * Method 'findWhereMasterIDEquals'
	 * 
	 * @param masterID
	 */
	@WebMethod
	public String findGamesByMasterID(@WebParam(name = "masterID") int masterID) {
		String rtnXML = "";
		TblGames _result[];
		try {
			_result = _dal.findGamesByMasterID(masterID);
			if (_result.length > 0)
				rtnXML = buildReturnXML(_result);
			else
				rtnXML = _xmlBuilder.buildErrorXML("ManageGamesWS.findGamesByMasterID", 0, 10, "Failed to find Game", "", "");
		} catch (Exception _e) {
			_log.insertError(0, 0, "ManageGamesWS.findGamesByMasterID", _e.getMessage(), _e.toString());
		} finally {
			_result = null;
		}
		return rtnXML;
	}

	/**
	 * Method 'findWhereActiveEquals'
	 * 
	 * @param active
	 */
	@WebMethod
	public String findActiveGames(@WebParam(name = "active") int active) {
		String rtnXML = "";
		TblGames _result[];
		try {
			_result = _dal.findActiveGames(active);
			if (_result.length > 0)
				rtnXML = buildReturnXML(_result);
			else
				rtnXML = _xmlBuilder.buildErrorXML("ManageGamesWS.findActiveGames", 0, 10, "Failed to find Game", "", "");
		} catch (Exception _e) {
			_log.insertError(0, 0, "ManageGamesWS.findActiveGames", _e.getMessage(), _e.toString());
		} finally {
			_result = null;
		}
		return rtnXML;
	}

	@WebMethod
	public String findAllSources() {
		String rtnXML = "";
		TblGameSources _result[];
		try {
			_result = _dal.findAllSources();
			if (_result.length > 0)
				rtnXML = buildSourceReturnXML(_result);
			else
				rtnXML = _xmlBuilder.buildErrorXML("ManageGamesWS.findAllSources", 0, 10, "Failed to find Source", "", "");
		} catch (Exception _e) {
			_log.insertError(0, 0, "ManageGamesWS.findAllSources", _e.getMessage(), _e.toString());
		} finally {
			_result = null;
		}
		return rtnXML;
	}

	/**
	 * Method 'findWhereGameIDEquals'
	 * 
	 * @param gameID
	 */
	@WebMethod
	public String findSourceByGameID(@WebParam(name = "gameID") int gameID) {
		String rtnXML = "";
		TblGameSources _result[];
		try {
			_result = _dal.findSourceByGameID(gameID);
			if (_result.length > 0)
				rtnXML = buildSourceReturnXML(_result);
			else
				rtnXML = _xmlBuilder.buildErrorXML("ManageGamesWS.findSourceByGameID", 0, 10, "Failed to find Source", "", "");
		} catch (Exception _e) {
			_log.insertError(0, 0, "ManageGamesWS.findSourceByGameID", _e.getMessage(), _e.toString());
		} finally {
			_result = null;
		}
		return rtnXML;
	}

	/**
	 * Method 'findWhereSourceEquals'
	 * 
	 * @param source
	 */
	@WebMethod
	public String findSourceBySourceName(@WebParam(name = "source") int source) {
		String rtnXML = "";
		TblGameSources _result[];
		try {
			_result = _dal.findSourceBySourceID(source);
			if (_result.length > 0)
				rtnXML = buildSourceReturnXML(_result);
			else
				rtnXML = _xmlBuilder.buildErrorXML("ManageGamesWS.findSourceBySourceName", 0, 10, "Failed to find Source", "", "");
		} catch (Exception _e) {
			_log.insertError(0, 0, "ManageGamesWS.findSourceBySourceName", _e.getMessage(), _e.toString());
		} finally {
			_result = null;
		}
		return rtnXML;
	}

	/**
	 * Method 'findWhereActiveEquals'
	 * 
	 * @param active
	 */
	@WebMethod
	public String findSourceByActiveStatus(@WebParam(name = "active") int active) {
		String rtnXML = "";
		TblGameSources _result[];
		try {
			_result = _dal.findSourceByActiveStatus(active);
			if (_result.length > 0)
				rtnXML = buildSourceReturnXML(_result);
			else
				rtnXML = _xmlBuilder.buildErrorXML("ManageGamesWS.findSourceByActiveStatus", 0, 10, "Failed to find Source", "", "");
		} catch (Exception _e) {
			_log.insertError(0, 0, "ManageGamesWS.findSourceByActiveStatus", _e.getMessage(), _e.toString());
		} finally {
			_result = null;
		}
		return rtnXML;
	}

	public String buildSourceReturnXML(TblGameSources dto[]) {
		StringBuffer rtnXML = new StringBuffer();
		StringBuffer rowXML = new StringBuffer();

		for (int i = 0; i < dto.length; i++) {
			rowXML.append(dto[i].toXMLRow());
		}

		rtnXML.append(_xmlBuilder.buildDatasetXML("Source_Detail_Results", rowXML.toString()));
		return rtnXML.toString();

	}

	public String buildReturnXML(TblGames dto[]) {
		StringBuffer rtnXML = new StringBuffer();
		StringBuffer rowXML = new StringBuffer();

		for (int i = 0; i < dto.length; i++) {
			rowXML.append(dto[i].toXMLRow());
		}

		rtnXML.append(_xmlBuilder.buildDatasetXML("Game_Detail_Results", rowXML.toString()));
		return rtnXML.toString();

	}

}
