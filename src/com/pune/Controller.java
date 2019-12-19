package com.pune;

import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.metadata.SaveResult;
import com.sforce.soap.metadata.Error;

public class Controller {
	private static MetadataConnection connection;
	
	public static void main(String[] args) {
	        ConnectorConfig config = new ConnectorConfig();
	        config.setUsername("<Your username>");
	        config.setPassword("<Your password>");
	        //I am not using sec token. Bypass by defining IP range to be wider.
	        try{
	        	connection = getMetadataConnection(config);
	        	//call this iteratively
	        	renameMetadata("CustomField", "MyCustomObject1New__c.Fatigue_OLD__c","MyCustomObject1New__c.Fatigue_NEW__c");
		        
	        }catch(Exception e){
	        	System.out.println(e);
	        }
	    }
	
	private static void renameMetadata(String  metaType, String oldApiName, String newApiName) throws ConnectionException{
		SaveResult result = connection.renameMetadata(metaType,oldApiName,newApiName);
        if (result.isSuccess()) {
    		System.out.println("Renamed component: " + result.getFullName());
        }
        else {
    		System.out.println("Errors were encountered while renaming " + result.getFullName() + " " + result.getErrors().toString());
    		for(Error e : result.getErrors()){
    			System.out.println("Msg = " + e.getMessage());
    		}
        }
	}
	
	private  static MetadataConnection getMetadataConnection(ConnectorConfig config) throws ConnectionException {
		LoginResult lr = loginToSalesforce(config.getUsername(), config.getPassword(), "https://login.salesforce.com/services/Soap/u/45.0");
		ConnectorConfig cc = new ConnectorConfig();
		cc.setUsername(config.getUsername());
		cc.setPassword(config.getPassword());
		cc.setSessionId(lr.getSessionId());
		cc.setServiceEndpoint(lr.getMetadataServerUrl());
		cc.setManualLogin(false);
		MetadataConnection connection = com.sforce.soap.metadata.Connector.newConnection(cc);
		return connection;
	  }		
	
	private static LoginResult loginToSalesforce(final String username,final String password,final String loginUrl) throws ConnectionException {
	    final ConnectorConfig config = new ConnectorConfig();
	    config.setAuthEndpoint(loginUrl);
	    config.setServiceEndpoint(loginUrl);
	    config.setManualLogin(true);
	    return (new PartnerConnection(config)).login(username, password);
	}
	
	}




