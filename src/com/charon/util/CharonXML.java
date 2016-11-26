/**
 * put database results into a standar XML format for use outside of charon 
 * 
 */
package com.charon.util;

import java.util.Date;

public class CharonXML {
	
	public enum Status {Success, Failure}

	//
	private String buildXMLHeader(String id, Status status)
	{
		StringBuffer rtn_XML = new StringBuffer();
		
		rtn_XML.append("<header id=\"");
		rtn_XML.append(id);
		rtn_XML.append("\"><date>");
		rtn_XML.append(new Date().toString());
		rtn_XML.append("</date><status>");
		rtn_XML.append(status.toString());
		rtn_XML.append("</status></header>");
		
		return rtn_XML.toString();
	}
	//XML header and footer
	private String wrapCharonXML(String xml)
	{
		StringBuffer rtn_XML = new StringBuffer();
		
		rtn_XML.append("<?xml version=\"1.0\"?><Charon-XML>");
		rtn_XML.append(xml);
		rtn_XML.append("</Charon-XML>");

		return rtn_XML.toString();

	}
	//wrap the rows into a dataset tag
	private String wrapDatasetXML(String xml)
	{
		StringBuffer rtn_XML = new StringBuffer();
		
		rtn_XML.append("<dataset>");
		rtn_XML.append(xml);
		rtn_XML.append("</dataset>");

		return rtn_XML.toString();

	}
	
	public String buildDatasetXML(String id, String rows)
	{
		StringBuffer rtn_XML = new StringBuffer();
		
		rtn_XML.append(buildXMLHeader(id, Status.Success));
		
		rtn_XML.append(wrapDatasetXML(rows));
		
		return wrapCharonXML(rtn_XML.toString());
	}
	
	
	//standard XML that charon returns when it encounters an error
	public String buildErrorXML(String id, int iErrorID, int iEventCode, String strMessage, String strSystemMsg, String strStack) {
		/*
		 * <?xml version="1.0"?> <Charon-XML> <header gameid="" gamename="">
		 * <date></date> <game id="" name="" /> <user id="" sourceid="" />
		 * </header> <transaction event="Error"> <error id="" eventcode="">
		 * <message></message> <system-msg></system-msg>
		 * <stack-trace><stack-trace> </error> </transaction> </Charon-XML>
		 */
		StringBuffer rtn_XML = new StringBuffer();
		StringBuffer row_XML = new StringBuffer();
		
		rtn_XML.append(buildXMLHeader(id,Status.Failure));

		row_XML.append("<row><fieldlist>");

		row_XML.append("<field name=\"ErrorID\" type=\"int\">");
		row_XML.append(iErrorID);
		row_XML.append("</field>");
		
		row_XML.append("<field name=\"EventCode\" type=\"int\">");
		row_XML.append(iEventCode);
		row_XML.append("</field>");

		row_XML.append("<field name=\"Message\" type=\"string\"><![CDATA[");
		row_XML.append(strMessage);
		row_XML.append("]]></field>");
		
		row_XML.append("<field name=\"SystemMessage\" type=\"string\"><![CDATA[");
		row_XML.append(strSystemMsg);
		row_XML.append("]]></field>");

		row_XML.append("<field name=\"StackTrace\" type=\"string\"><![CDATA[");
		row_XML.append(strStack);
		row_XML.append("]]></field>");

		row_XML.append("</fieldlist></row>");

		rtn_XML.append(wrapDatasetXML(row_XML.toString()));
		
		return wrapCharonXML(rtn_XML.toString());
	}

	
	//insert the standard XML status block so that the calling agent(pythia) can quickly see if their results are present before 
	//parsing the entire xml
	public String buildStatusXML(String strID, int iGameID, int iUserID, int iSourceID, String strSession, String strSource){
		StringBuffer rtn_XML = new StringBuffer();
		StringBuffer row_XML = new StringBuffer();
		
		rtn_XML.append(buildXMLHeader(strID,Status.Success));
		
		row_XML.append("<row><fieldlist>");

		row_XML.append("<field name=\"GameID\" type=\"int\">");
		row_XML.append(iGameID);
		row_XML.append("</field>");

		row_XML.append("<field name=\"UserID\" type=\"int\">");
		row_XML.append(iUserID);
		row_XML.append("</field>");

		row_XML.append("<field name=\"SourceID\" type=\"int\">");
		row_XML.append(iSourceID);
		row_XML.append("</field>");

		row_XML.append("<field name=\"SessionID\" type=\"int\">");
		row_XML.append(strSession);
		row_XML.append("</field>");

		row_XML.append("</fieldlist></row>");


		rtn_XML.append(wrapDatasetXML(row_XML.toString()));
		
		return wrapCharonXML(rtn_XML.toString());
	}

}
