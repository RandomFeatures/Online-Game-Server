/**
 * Convert the header of the base charon xml into a Java object
 * At the time I was building it seemed like it would be more
 * useful than it really is. I have chosen to leave it here just incase
 * 
 */

package com.charon.util;

import com.cerberus.myaccount.dal.GamePlay;
import com.cerberus.myaccount.dal.Misc;
import com.cerberus.myaccount.dal.dto.DspValidateGameSession;



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
public class CharonXmlHeader {

	
	private static GamePlay _dal = new GamePlay();
	private static Misc _dal_misc = new Misc();
	private int _gameid = 0;
	private String _gamename = "";
	private int _sourceid = 0;
	private String _source = "";
	private int _userid = 0;
	private String _session = "";
	private boolean _sessionvalid = false;
	
	public CharonXmlHeader(String strXML)
	{
	
		this.ParseXML(strXML);
	}
	public CharonXmlHeader()
	{
		
	}

	
	private boolean validateSession()
	{
		DspValidateGameSession _result[];
		boolean rtn = false;
		
		try {
			_result = _dal.validateGameSession(_userid, _gameid, _sourceid, _session);
			if (_result.length > 0 && _result[0].getSuccess() == 1) 
				rtn = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rtn;
	}
	
	public void ParseXML(String strXML)
	{
		_gameid = 0;
		_gamename = "";
		_sourceid = 0;
		_source = "";
		_userid = 0;
		_session = "";
		_sessionvalid = false;
		
		XmlDTO xml_root;
		XmlDTO xml_elem_header;
		
		xml_root = new XmlDTO(strXML, "Charon-XML");
		xml_elem_header = xml_root.child("header");
		// ** GameID **//
		_gameid = xml_elem_header.integer("gameid");
		_gamename = xml_elem_header.string("gamename");
		if (_gameid == 0) {
			// Get game id from game name
			try {
				_gameid = _dal.findGameDetails(_gamename)[0].getGameID();
			} catch (Exception e) {
				throw new RuntimeException("Game not found.");
			}
		}

		// ** SourceID **//
		_sourceid = xml_elem_header.child("source").integer("id");
		_source = xml_elem_header.child("source").string("name");
		
		try {
			_userid = xml_elem_header.child("user").integer("id");
			_session = xml_elem_header.child("user").string("session");
		} catch (Exception e1) {
		}
		
		if (_sourceid == 0) {
			// get source id from tbl lookup
			try {
				_sourceid = _dal_misc.getLookupCode("SourceType", _source);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Game not found.");
			}
		}
		
		_sessionvalid = validateSession(); 
		
	}
	
	public boolean GetSessionValid()
	{
		return _sessionvalid;
	}
	
	public int GetUserID()
	{
		return _userid;
	}
	
	public String GetSession()
	{
		return _session;
	}
	
	public int GetGameID()
	{
		return _gameid;
	}
	
	public String GetGame()
	{
		return _gamename;
	}
	
	public String GetSource()
	{
		return _source;
	}
	
	public int GetSourceID()
	{
		return _sourceid;
	}

}
