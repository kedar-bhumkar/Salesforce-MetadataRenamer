# Salesforce-MetadataRenamer
Base code to get a metadata connection and rename a metadata

1.Download the file renamer.jar which contains the main code to run the 'rename' operation
2.Download the seedFile.txt which contains a JSON represnetation of the config needed
3.Ensure 'java' command is in your 'path'
4.Modify the seedFile.txt as discussed below 
5.Run the following command java -jar renamer.jar <FULL escape path to seedFile.txt"
  E.g. java -jar renamer.jar "C:\\India\\seedFile.txt"
  
# SeedFile.txt config variable
username - SF username (needs admin)
password - SF password (Note security token not supported)
sobjectApi - The object name whose fields need to be renamed
suffix - The suffix to be used for replacement E.g. _old
instanceUrl- https://login.salesforce.com/services/Soap/u/45.0
fields: Field names defined as JSON array
#If latest version throws error use 45.0


E.g.
{
	"username": "uname@hellow.com",
	"password": "upass",
	"sobjectApi": "uObject__c",
	"suffix": "_old",
	"instanceUrl":"https://login.salesforce.com/services/Soap/u/45.0"
	"fields": ["Fatigue__c","Earpain__c","Backpain__c"]
}
