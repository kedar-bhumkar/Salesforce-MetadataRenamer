package com.pune;

import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.metadata.SaveResult;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sforce.soap.metadata.Error;

public class Controller {
	private static MetadataConnection connection;
	private static String username;
	private static String password;
	private static String instanceUrl;
	private static String sObjectApi;
	private static String suffix;
	private static Set<String> fieldApis;
	
	
	public static void main(String[] args) {
	        try{
	        	readfile(args[0]);
	        	getMetadataConnection();
	        	//call this iteratively
	        	fieldApis.forEach(field->renameMetadata("CustomField", sObjectApi+"."+field, sObjectApi+"."+getNewFieldName(field)));
	        
	        }catch(Exception e){
	        	System.out.println(e);
	        }
	    }
	
	private static String getNewFieldName(String field) {
		if(field.endsWith("__c")){
			field = field.substring(0, field.length()-3)+suffix+"__c";
		}
		return field;
	}

	private static void readfile(String filePath) {
		try
        {
			//FileReader reader = new FileReader("C:\\DDrive\\Programming\\Tools\\eclipse-neon-workspace\\MH12\\seedfile.txt");
			FileReader reader = new FileReader(filePath);
            Object obj = new JSONParser().parse(reader);
            parse(obj);
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
	}

	private static void parse(Object obj) {
        JSONObject jsonObj = (JSONObject) obj;
        System.out.println(jsonObj);
        
        username = (String)jsonObj.get("username");
        password = (String)jsonObj.get("password");
        instanceUrl = (String)jsonObj.get("instanceUrl");
        suffix = (String)jsonObj.get("suffix");
        sObjectApi = (String)jsonObj.get("sobjectApi");

       
        System.out.println("username = " + username);
        System.out.println("password = " + password);
        System.out.println("sObjectApi  = " + sObjectApi );
        System.out.println("instanceUrl = " + instanceUrl);
        System.out.println("suffix = " + suffix);

        JSONArray fieldsArray = (JSONArray)jsonObj.get("fields");
        System.out.println("fields = " + fieldsArray);
        fieldApis = new HashSet<String>();
        fieldsArray.forEach(field->fieldApis.add((String)field));
        System.out.println("fields Set= " + fieldApis);

	}
	
	

	private static void renameMetadata(String  metaType, String oldApiName, String newApiName){
		System.out.println("metaType = " + metaType + " oldApiName = "+ oldApiName + " newApiName = " + newApiName);
		
		try{
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
			
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	private  static void getMetadataConnection() throws ConnectionException {
		LoginResult lr = loginToSalesforce(username, password, instanceUrl);
		ConnectorConfig cc = new ConnectorConfig();
		cc.setUsername(username);
		cc.setPassword(password);
		cc.setSessionId(lr.getSessionId());
		cc.setServiceEndpoint(lr.getMetadataServerUrl());
		cc.setManualLogin(false);
		connection = com.sforce.soap.metadata.Connector.newConnection(cc);
		
	  }		
	
	private static LoginResult loginToSalesforce(final String username,final String password,final String loginUrl) throws ConnectionException {
	    final ConnectorConfig config = new ConnectorConfig();
	    config.setAuthEndpoint(loginUrl);
	    config.setServiceEndpoint(loginUrl);
	    config.setManualLogin(true);
	    return (new PartnerConnection(config)).login(username, password);
	}
	
	}




