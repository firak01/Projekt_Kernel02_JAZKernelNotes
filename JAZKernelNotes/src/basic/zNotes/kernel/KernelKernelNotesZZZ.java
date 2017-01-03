package basic.zNotes.kernel;
import java.io.File;
import java.util.Vector;

import lotus.domino.AgentContext;
import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.Item;
import lotus.domino.Name;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.View;
import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.datatype.string.StringArrayZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zKernel.KernelUseObjectZZZ;
import basic.zKernel.KernelZZZ;
import basic.zNotes.basic.AgentZZZ;
import basic.zNotes.basic.DJAgentContext;
import custom.zKernel.file.ini.FileIniZZZ;
import custom.zNotes.kernel.KernelNotesLogZZZ;
import custom.zNotes.kernel.KernelNotesZZZ;

/**
 * @author 0823
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class KernelKernelNotesZZZ extends KernelUseObjectZZZ implements IKernelNotesConstZZZ{ //ObjectZZZ{
	private NotesContextProviderZZZ objContextProvider=null;
	private KernelNotesLogZZZ objKernelNotesLog = null;
	private Database dbLogCurrent = null;
	
	
	//Variablen
	private String sLocationCurrent = null;	
	private String sKeyApplication = null;	
	protected String sSystemNumber = null;
 
	//Session & Context	
	private Session objSession = null;            //Session, die durch AgentBase.getSession() gewonnen wird. Enthält Context Informationen
	//20060921 nun soll der NotesContextProvider alles liefern, darum keine spezielle DebugSession mehr 
	//private Session objSessionDebug = null;  //Session, die durch NotesFactory.createSession(Username, Password) generiert wird. Enthält KEINE Context Informationen.
																		 //Ermöglicht aber im Debug Modus eines externen Debuggers den Zugriff auf andere Datenbanken.
																		 //Besser: Eine Notes-ID-Datei verwenden, die Kein Kennwort besitzt.
	
	private DJAgentContext agtKernelCallingContext; //FGL 20060922 Mit dieser Hilfklasse wird nun der AgentContext z.B. im Servlet "gefaked"
	private String sAgentNameFaked=null; //Falls ein Servlet verwendet wird, ist dieser Name zu übergeben.
	
	//Databases
	private Database dbKernelCalling = null;
	private Database dbMailSupportCurrent = null;
	private Database dbDirectoryMasterCurrent = null;
	private Database dbInterchangeCurrent = null;
	private Database dbConfigurationCurrent = null;
	private Database dbInterchangeCurrentArchive = null;
	
	
	private Database dbApplicationCurrent = null;
	private Database dbApplicationCurrentArchive = null;
	private Database dbDirectoryCurrent = null;
	private Database dbHelpdeskSupportCurrent = null;
	private Database dbApplicationCurrentHelp = null;
	private Database dbServerCatalogCurrent = null;
	
	//Documents
	private Document docProfileApplication = null; // Das Profildokument der Applikation
	private Document docStoreApplication = null; // Das Storedokument der Applikation, um sich ändernde Werte festzuhalten. Z.B. fortlaufende Nummern.

	//Flags
	boolean bFlagUseContextByKernel=false;
	

	/* (non-Javadoc)
	@see zzzKernel.basic.KernelObjectZZZ#getFlag(java.lang.String)
	Flags used:<CR>
	- HasContextNatural
	 */
	public boolean getFlag(String sFlagName){
		boolean bFunction = false;
		main:{
			if(StringZZZ.isEmpty(sFlagName)) break main;
			bFunction = super.getFlag(sFlagName);
			if(bFunction==true) break main;
			
			//getting the flags of this object
			String stemp = sFlagName.toLowerCase();
			if(stemp.equals("usecontextbykernel")){
				bFunction = bFlagUseContextByKernel;
				break main;
			}
			/*
			else if(stemp.equals("fileunsaved")){
				bFunction = bFlagFileUnsaved;
				break main;
			}else if(stemp.equals("filenew")){
				bFunction = bFlagFileNew;
				break main;
			}
			*/
		}//end main:
		return bFunction;
	}

	/**
	 * @see zzzKernel.basic.KernelUseObjectZZZ#setFlag(java.lang.String, boolean)
	 * @param sFlagName
	 * Flags used:<CR>
	 * - FileChanged, if a value is written to the file the flag is changed to true,
	 * - FileUnsaved, if a value is written to the file the flag is changed to true, if the save() method is used it is reset to false.
	 * 			  source_remove: after copying the source_files will be removed.
	 */
	public boolean setFlag(String sFlagName, boolean bFlagValue){
		boolean bFunction = false; 
		main:{
			if(StringZZZ.isEmpty(sFlagName)) break main;
			bFunction = super.setFlag(sFlagName, bFlagValue);
			if(bFunction==true) break main;
	
		//setting the flags of this object
		String stemp = sFlagName.toLowerCase();
		if(stemp.equals("usecontextbykernel")){
			bFlagUseContextByKernel = bFlagValue;
			bFunction = true;
			break main;
		}
		/*
		else if(stemp.equals("fileunsaved")){
			bFlagFileUnsaved = bFlagValue;
			bFunction = true;
			break main;
		}else if(stemp.equals("filenew")){
			bFlagFileNew = bFlagValue;
			bFunction = true;
			break main;
		}
		*/
		}//end main:
		return bFunction;
	}
	
	
	//+++ getter & setter - Methods
	public NotesContextProviderZZZ getNotesContextProvider() throws ExceptionZZZ{
		//Merke: Im Fall, das es ein echter notesagent ist, wird der Contextprovider entweder sofort im Konstruktor übergeben
		//            oder der Context ist nun aus der Session heraus zu ermitteln.
		//Merke2: Bei Servlets, etc. ohne echten Context, muss der Agentenname (gefaked) übergeben werden.
		
		if (this.objContextProvider==null){
			System.out.println(ReflectCodeZZZ.getMethodCurrentName() + ", objContextProvider==null");
			if(StringZZZ.isEmpty(this.sAgentNameFaked)){			
				System.out.println(ReflectCodeZZZ.getMethodCurrentName() + ", Kein Fakename vorhanden");
				this.objContextProvider = new NotesContextProviderZZZ(this.getKernelObject(), this.getClass().getName(), this.objSession);
			}else{
				System.out.println(ReflectCodeZZZ.getMethodCurrentName() + ", Verwende Fakename '" + this.sAgentNameFaked + "'");
				this.objContextProvider = new NotesContextProviderZZZ(this.getKernelObject(), this.getClass().getName(), this.sAgentNameFaked);
			}
		}else{
			System.out.println(ReflectCodeZZZ.getMethodCurrentName() + ", objContextProvider!=null");
		}
		return this.objContextProvider;
	}
	
	
	/**
	 * @return if the debug-flag is true ==>a session made by NotesFactory(null, null, Password)
	 * if the debug-flag is false ==> a session made by AgentBase.getSession()
	 * @throws ExceptionZZZ 
	 * @throws NotesException 
	 */
	public Session getSession() throws ExceptionZZZ{
		//Merke: Normalerweise soll die Session schon im Konsturktor des Kernel-Objekts mitgeliefert werden.		
		if(this.objSession==null){
			System.out.println(ReflectCodeZZZ.getMethodCurrentName() + ", objSession==null");
			
			 //Dies ist dann die Lösung für servlets und alles weitere, das kein echter Agent ist.
            NotesContextProviderZZZ objContextProvider = this.getNotesContextProvider();	
            this.objSession = objContextProvider.getSession();	            	
            if(this.objSession == null){
				ExceptionZZZ ez = new ExceptionZZZ("Missing Parameter: Session",iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName()); 				
				throw ez;	
            }         
		}else{
			System.out.println(ReflectCodeZZZ.getMethodCurrentName() + ", objSession!=null");
		}
		return this.objSession;	
	}
	
	/**
	 * 
	 * @return the path of the notes-data directory
	 */
		public String getPathNotesData()throws ExceptionZZZ{
			String sReturn = null;			
			main:{
				try{
				
					//Session-Objekt holen
					Session objSession = this.getSession(); 

					//Notes-Data-Verzeichnis
				    sReturn = objSession.getEnvironmentString("Directory", true);		 
					 //objKernelNotesLog.LogWrite("Notes-Data-Directory: " + sReturn, this, "getPathNotesData", 3);
				
					 if(sReturn.length() <= 0){
						String stemp = "unable to receive notes-data directory.";
						ExceptionZZZ ez = new ExceptionZZZ(stemp, iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName()); 							
						throw ez;	
					 }							  
				}catch(NotesException e){
								String stemp = "NotesException: "+ e.getMessage();
								ExceptionZZZ ez = new ExceptionZZZ(stemp, iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName()); 
								throw ez;	
				}
			}//end main:
			return sReturn;
		}
	
	
	
	
	/** 
  	 * Constructor,
	 * internal calling the Constructor with the parameter flagControl = "INIT"
	 * @throws ExceptionZZZ 
	 */	
  public KernelKernelNotesZZZ() throws ExceptionZZZ{
  
  	 // Einen anderen Konstruktor aufrufen
  	 this(null,null,null, "INIT");
  } //end Constructor
  

  /** Use this constructor in notesagents, when there is an agent context available
  * lindhaueradmin; 19.09.2006 07:51:43
   * @param objSessionIn
   * @param objKernel
   * @param saFlagControlIn
   * @throws ExceptionZZZ
   */
  public KernelKernelNotesZZZ(Session objSessionIn, String sApplicationKeyIn, String sSystemNumberIn, String[] saFlagControlIn) throws ExceptionZZZ{
		KernelNotesNew_(objSessionIn, sApplicationKeyIn, sSystemNumberIn, saFlagControlIn);	
  }//end Constructor
   
  /** Use this Constructor in NotesAgents, when you want to use the normal KernelZZZ-Object
* lindhaueradmin; 19.09.2006 07:51:43
 * @param objSessionIn
 * @param objKernel
 * @param saFlagControlIn
 * @throws ExceptionZZZ
 */
public KernelKernelNotesZZZ(Session objSessionIn, KernelZZZ objKernel, String sApplicationKeyNotes, String sSystemNumberNotes, String[] saFlagControlIn) throws ExceptionZZZ{
	Session objSession = null;
	String sApplicationKey = null;
	String sSystemNumber = null;

	main:{
			if(objKernel==null){
				ExceptionZZZ ez = new ExceptionZZZ("KernelObject missing", iERROR_PARAMETER_MISSING, ReflectCodeZZZ.getMethodCurrentName(), "");
				throw ez;
			}else{
				this.objKernel = objKernel;
			}
		
		if(StringZZZ.isEmpty(sApplicationKeyNotes)){
			this.sKeyApplication =objKernel.getApplicationKey();
		}else{
			this.sKeyApplication =sApplicationKeyNotes;
		}
		
		if(StringZZZ.isEmpty(sApplicationKeyNotes)){
			this.sSystemNumber = objKernel.getSystemNumber();
		}else{
			this.sSystemNumber = sSystemNumberNotes;
		}
  		
		boolean btemp = KernelNotesNew_(objSessionIn, this.sKeyApplication, this.sSystemNumber, saFlagControlIn);
	}//end main
  			
  }//end constructor
  
/** Use this Construktor if you want to fake the agent context. E.g. when you are in a servlet.
 * @param objContextNotes
 * @param sApplicationKeyNotes
 * @param sSystemNumberNotes
 * @param saFlagControlIn
 * @throws ExceptionZZZ
 * lindhaueradmin; 06.11.2006 10:05:37
 */
public KernelKernelNotesZZZ(NotesContextProviderZZZ objContextNotes, String sApplicationKeyNotes, String sSystemNumberNotes, String[] saFlagControlIn) throws ExceptionZZZ{
	 if(objContextNotes == null){
	    	ExceptionZZZ ez = new ExceptionZZZ("NotesContextProviderZZZ missing", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
	    	throw ez;
	    }
	    if(objContextNotes.getSession()==null){
	    	ExceptionZZZ ez = new ExceptionZZZ("NotesContextProviderZZZ has no session", iERROR_PARAMETER_VALUE, this, ReflectCodeZZZ.getMethodCurrentName());
		   	throw ez;
	    }
	  
	 
	 this.objContextProvider = objContextNotes;
	 this.objKernel = objContextNotes.getKernelObject();
	 if(this.objKernel==null){
			ExceptionZZZ ez = new ExceptionZZZ("KernelObject missing", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
	 
		if(StringZZZ.isEmpty(sApplicationKeyNotes)){
			this.sKeyApplication =objKernel.getApplicationKey();
		}else{
			this.sKeyApplication =sApplicationKeyNotes;
		}
		
		if(StringZZZ.isEmpty(sSystemNumberNotes)){
			this.sSystemNumber = objKernel.getSystemNumber();
		}else{
			this.sSystemNumber = sSystemNumberNotes;
		}				  
	boolean btemp = KernelNotesNew_(null, this.sKeyApplication, this.sSystemNumber, saFlagControlIn);
}

  /** use this constructor in a notesagent, when an agent context is available
   * 
 @author 0823 , date: 18.10.2004
 @param objSessionIn, the current notessession. Used to get the agent context, the current username (remember, the username is part of the key used to get the kernel profile document)
 @param sKernelKeyIn, the namespace used for all applications. This is part of the key used to get the kernel profile document
 @param sApplicationKeyIn, the namespace used. This is part of the key used to get the application profile document 
 @param sSystemNumberIn, the System number of the Application. This offers the possibility to use different configurations, e.g. for the productive system and the developing system.  
 Inside of a notesagent, you can get the systemnumber by using the static method KernelNotesZZZ.ComputeSystemNumberCustom(Session)
 @param sFlagControlIn
 * @throws ExceptionZZZ 
 */
public KernelKernelNotesZZZ(Session objSessionIn,String sApplicationKeyIn, String sSystemNumberIn, String sFlagControlIn) throws ExceptionZZZ{  	  		
  	String[] saFlagControl ={""};
	saFlagControl[0]= sFlagControlIn;
	boolean bFunction = KernelNotesNew_(objSessionIn, sApplicationKeyIn, sSystemNumberIn, saFlagControl);	
  }//end constructor
  			
/**
 * Method ApplicationKeyDefaultGet.
 * @return String
 */
public String getApplicationKeyDefault() {
	//Merke: Diese Function muss immer vom customizing Teil überschreiben werden.
	String sFunction = null;
	
	main:{
		sFunction = null;	
	}//end main
	end:{
		return sFunction;
	}
}
  
  //++++++++++++++++++++++++++++++
  public String ProfileKernelKeyCompute() throws ExceptionZZZ{
  	String sFunction = null;
  	main:{
  	String sLocation = this.getLocationCurrent();
  	
  	sFunction = sLocation + this.getKernelKey();
  	//"frmAdminKey" & sKeyKernelInput, sNameServer & sKeyKernelInput
  	}//end main
  	end:{
  		
  	return sFunction;
  	}
  } //end function
  
  //+++++++++++++++++++++++++++++++
  public String ProfileKernelMaskCompute(){
  	String sFunction = null;
  	main:{
  		sFunction = "frmAdminKey" + this.getKernelKey();
  	}//end main
  	end:{
  		return sFunction;
  	}
  } //end function

	/**
	 * @return the application profile document, which can only be found if the application is configured by the 
	 * configuration mask of the ZKernel configuration notesdatabase.
	 * 
	 * @throws ExceptionZZZ, if no application profile document can be found.
	 */
	public Document getApplicationProfile() throws ExceptionZZZ{
			Document docFunction = null;
			String stemp;
			int iCode; String sMethod;
			main:{
				if(this.docProfileApplication == null){
					docFunction = this.ProfileApplicationRefresh();					
				}else{
					docFunction = this.docProfileApplication;	
				}	
			} //end main

			end:{
				return docFunction;	
			}			
	}

	/**
	 * Method ApplicationProfileRefresh.
	 * @return Document
	 * @throws ExceptionZZZ 
	 */
	public Document ProfileApplicationRefresh() throws ExceptionZZZ {
		Document docFunction = null;
		main:{
			String sKey = null;
			try{
				if(this.docProfileApplication==null){
					Database dbCur = this.getDBKernelCalling();
					if(dbCur==null) {
						ExceptionZZZ ez = new ExceptionZZZ("Context database missing. Property .getDBKernelCalling()==null !!!", iERROR_PROPERTY_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
					  throw ez;
					}
					
					String sMask = this.ProfileApplicationMaskCompute();
					sKey = this.ProfileApplicationKeyCompute();
					
					System.out.println("KernelKernelNotesZZZ." + ReflectCodeZZZ.getMethodCurrentName() + ", Hole Profildokument mit Schlüssel: Mask='" + sMask + "' und Key='" + sKey + "' aus Datenbank " + dbCur.getServer() + "!!" + dbCur.getFilePath());
					this.docProfileApplication = dbCur.getProfileDocument(sMask, sKey);
					if(this.docProfileApplication==null){
						String sLog = "No applicationprofile-document could be found. Not configured properly for the kernel key '" + sKey + "' and the location '" + this.getLocationCurrent() + "' ?";
						System.out.println("KernelKernelNotesZZZ." + ReflectCodeZZZ.getMethodCurrentName() + ", " + sLog);
						ExceptionZZZ e = new ExceptionZZZ(sLog,iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
						throw e;	
					}else{
						System.out.println("KernelKernelNotesZZZ." + ReflectCodeZZZ.getMethodCurrentName() + ", Profildokument gefunden");
					}
				}//END if this.docProfileApplication == null
				
				//Auch wenn da schon ein Dokument ist, prüfen, ob es ein Konfigurationsdokument ist
				if(this.docProfileApplication.hasItem("LogLevelZZZ") == false){
					String sLog = "LogLevelZZZ als Item nicht gefunden. Not configured properly for the kernel key '" + sKey + "' and the location '" + this.getLocationCurrent() + "' ?";
					System.out.println("KernelKernelNotesZZZ." + ReflectCodeZZZ.getMethodCurrentName() + ", " + sLog);
					ExceptionZZZ e = new ExceptionZZZ(sLog ,iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
					throw e;				
				}else{
					System.out.println("KernelKernelNotesZZZ." + ReflectCodeZZZ.getMethodCurrentName() + ", Profildokument hat LogLevelZZZ - Item.");
				}
				
			}catch(NotesException ne){
				String sLog = "Error: " + ne.getMessage() + ". For the kernel key '" + sKey + "' and the location '" + this.getLocationCurrent() + "' ?";
				System.out.println("KernelKernelNotesZZZ." + ReflectCodeZZZ.getMethodCurrentName() + ", " + sLog);
				ExceptionZZZ e = new ExceptionZZZ(ne.getMessage(),iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
				throw e;		
			}
		}//end main
		end:{
			docFunction = this.docProfileApplication;
			return docFunction;	
		}
	}

	public Database getDBKernelCalling() throws ExceptionZZZ{
		Database dbFunction = null;

  	main:{
  		try{
			if(this.dbKernelCalling == null){
				if(getFlag("UseContextByKernel")==true){
					String sLog = ReflectCodeZZZ.getMethodCurrentName() + ", dbKernelCalling==null, UseContextByKernel-Flag==true; Hole die Datenbank über den NotesContextProviderZZZ";
					System.out.println(sLog);
					dbFunction = this.getNotesContextProvider().getDb();				
				}else{
					String sLog = ReflectCodeZZZ.getMethodCurrentName() + ", dbKernelCalling==null, UseContextByKernel-Flag==false; Hole die Datenbank über den NotesContextProviderZZZ";
					System.out.println(sLog);
					dbFunction= this.getAgentContextCurrent().getCurrentDatabase();	
				}				
			}else{
				dbFunction = this.dbKernelCalling;	
			}	
			this.dbKernelCalling = dbFunction;
		}catch(NotesException e){
			ExceptionZZZ ez = new ExceptionZZZ(e.text, iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
  	}//end main
  	end:{
		return dbFunction;
  	}
	} //end function
	
	
	
	
	public Document getApplicationStore() throws ExceptionZZZ{
		if(this.docStoreApplication==null){
			Session session= this.getSession();
			Database dbApplication = this.getDBApplicationCurrent();
			String sApplicationKey = this.getApplicationKeyCurrent();
			return this.docStoreApplication = KernelKernelNotesZZZ.refreshApplicationStore(session, dbApplication, sApplicationKey);
		}else{
			return this.docStoreApplication;
		}
	}
	
	
	/** Holt für den Key aus der Applikation ein Notesdokument, in dem applikationsspezifische Informationen gespeichert werden.
	 *    Z.B. eine vergebene Nummer.
	 *    
	* @param session
	* @param dbApplication
	* @param sApplicationKey
	* @return
	* @throws ExceptionZZZ
	* 
	* lindhaueradmin; 12.12.2006 08:22:07
	 */
	public static Document refreshApplicationStore(Session session, Database dbApplication, String sApplicationKey) throws ExceptionZZZ{
		Document objReturn = null;
		main:{
			try{
				if(dbApplication==null){
					ExceptionZZZ ez = new ExceptionZZZ("Application database", iERROR_PARAMETER_MISSING, "KernelKernelNotesZZZ", ReflectCodeZZZ.getMethodCurrentName());
					throw ez;
				}
				if(StringZZZ.isEmpty(sApplicationKey)){
					ExceptionZZZ ez = new ExceptionZZZ("ApplicationKey", iERROR_PARAMETER_MISSING, "KernelKernelNotesZZZ", ReflectCodeZZZ.getMethodCurrentName());
					throw ez;
				}
				
				//Ansicht holen
				View viwConfig = dbApplication.getView(KernelKernelNotesZZZ.sVIEW_STORE);
				if(viwConfig==null){
					ExceptionZZZ ez = new ExceptionZZZ("View not found: " + KernelKernelNotesZZZ.sVIEW_STORE,  iERROR_ZFRAME_DESIGN, "KernelKernelNotesZZZ", ReflectCodeZZZ.getMethodCurrentName());
					throw ez;
				}
				
				
				//Das Dokument mit dem ApplicationKeyHolen			
				DocumentCollection col = viwConfig.getAllDocumentsByKey(sApplicationKey);
				if(col.getCount()==0){
					//!!! Falls kein Dokument gefunden wird, dann muss automatisch ein Dokument erstellt werden.
					objReturn = dbApplication.createDocument();
					objReturn.replaceItemValue("Form", KernelKernelNotesZZZ.sFORM_PREFIX_STORE + sApplicationKey);
					objReturn.replaceItemValue("FormZZZ", KernelKernelNotesZZZ.sFORM_PREFIX_STORE);
					
					//Wichtig, den ApplicationKey setzen, unter dem wird das Dokument wiedergefunden.
					objReturn.replaceItemValue("ApplicationKeyZZZ", sApplicationKey);
					
					
					//Nun die default-Zugriffsfelder setzen
					KernelNotesZZZ.createAccessFieldAllDefault(session, objReturn, sApplicationKey);
					
					
					
					//Speichern des neuen Dokuments nicht vergessen
					objReturn.save();
				}else{
					objReturn = col.getFirstDocument();
				}
				
				
				
				
				
			}catch(NotesException ne){
				ExceptionZZZ ez = new ExceptionZZZ(ne.text, iERROR_RUNTIME, "KernelKernelNotesZZZ", ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
		}//END main:
		return objReturn;			
	}
	
	/**Analog zum LotusScript-Kernel wird nun eine Methode angeboten, mit der ein Dokument default Reader/Author - Einträge bekommen kann.
	 *  Es werden die Felder AuthorZZZ/ReaderZZZ, Author + ApplicationKey, bzw. Reader + ApplicationKey angelegt und mit den Standardrollen gefüllt.
	 *  
	 *  Standardrollen:
	 *  [ZZZAuthor]
	 *  [ZZZReader]
	 *  [ZZZAdmin]
	 *  [ZZZServer]
	 *  
	 *  entsprechend für die Application z.B. beim ApplicationKey VIA
	 *  [VIAAuthor]
	 *  
	 *  
	 *  
	 * @param session
	 * @param doc
	 * @param sApplicationKey, 
	 *
	 * @return void
	 *
	 * javadoc created by: 0823, 08.12.2006 - 09:02:00
	 * @throws ExceptionZZZ 
	 */
	public static void createAccessFieldAllDefault(Session session, Document doc, String sApplicationKey) throws ExceptionZZZ{
		/*abgearbeitete Lotusscript version
		  '### Main 
	Dim saRoleAuthor(2) As String
	saRoleAuthor(0) = "Admin"
	saRoleAuthor(1) = "Support"
	saRoleAuthor(2) = "Server"
	
	Dim saRoleReader(0) As String
	saRoleReader(0) = "Reader"
	
	Dim saKey(1) As String
	saKey(0) = objKernel.KernelKey
	saKey(1) = objKernel.ApplicationKey
	
	Dim iCount As Integer
	Dim vsaRoleReader As Variant
	Dim vsaRoleAuthor As Variant	
	Dim sItem As String
	Dim item As notesitem
		 */
		
		main:{
			try{
			if(doc==null){
				ExceptionZZZ ez = new ExceptionZZZ("Notesdocument", iERROR_PARAMETER_MISSING, "KernelKernelNotesZZZ", ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
			if(StringZZZ.isEmpty(sApplicationKey)){
				ExceptionZZZ ez = new ExceptionZZZ("ApplicationKey", iERROR_PARAMETER_MISSING, "KernelKernelNotesZZZ", ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
	
			
		
		String[] saRoleAuthor = {"Admin", "Support", "Server", "Author"};
		String[] saRoleReader = {"Reader"};
		
		String[] saKey = new String[2];
		saKey[0]=KernelKernelNotesZZZ.getKernelKey();
		saKey[1]=sApplicationKey;
		  
		//Collections objCol = new Collections(saRoleAuthor);
		
		
		String sItem = null; Item objItem = null; StringArrayZZZ objStringArray = null;
		for(int icount = 0; icount <= saKey.length -1; icount++){
			
			//Autoren
			objStringArray = new StringArrayZZZ(saRoleAuthor);
			objStringArray.plusString("[" + saKey[icount], "BEFORE");
			objStringArray.plusString("]", "BEHIND");
			
			Vector vecAuthor = objStringArray.toVector();
		
			sItem = "Author" +  saKey[icount];
			objItem = doc.replaceItemValue(sItem, vecAuthor);
			objItem.setAuthors(true);
			objItem.setSummary(true);
			 
			//Leser
			objStringArray = new StringArrayZZZ(saRoleReader);
			objStringArray.plusString("[" + saKey[icount], "BEFORE");
			objStringArray.plusString("]", "BEHIND");
						
			Vector vecReader = objStringArray.toVector();
			sItem = "Reader" + saKey[icount];
			objItem = doc.replaceItemValue(sItem, vecReader);
			objItem.setReaders(true);
			objItem.setSummary(true);
		}
		
		/* LotusScript-Version	
	For iCount = 0 To Ubound(saKey)
		
	'+++++++ Autoren	
		sItem = "Author" & saKey(iCount)
		Call DocItemInstanceAllRemoveZZZ(objKernel.session, doc, sItem, tpcall, "")
		If tpcall.ierr > 0 Then Error iERR_FUNCTION_CALL, tpcall.serr
		
		vsaRoleAuthor = ArrayStringPlusStringZZZ( objKernel.session, saRoleAuthor, "[" & objKernel.KernelKey, tpcall, "BEFORE")	
		If tpcall.ierr > 0 Then Error iERR_FUNCTION_CALL, tpcall.serr
		vsaRoleAuthor = ArrayStringPlusStringZZZ( objKernel.session, vsaRoleAuthor, "]", tpcall, "BEHIND")
		If tpcall.ierr > 0 Then Error iERR_FUNCTION_CALL, tpcall.serr
		Set item = New notesitem(doc, sItem, vsaRoleAuthor, AUTHORS)
		item.IsSummary = True
		
	'++++++  Leser		
		sItem = "Reader" & saKey(iCount)
		Call DocItemInstanceAllRemoveZZZ(objKernel.session, doc, sItem, tpcall, "")
		If tpcall.ierr > 0 Then Error iERR_FUNCTION_CALL, tpcall.serr
		
		vsaRoleReader = ArrayStringPlusStringZZZ( objKernel.session, saRoleAuthor, "[" & objKernel.KernelKey, tpcall, "BEFORE")			
		If tpcall.ierr > 0 Then Error iERR_FUNCTION_CALL, tpcall.serr
		vsaRoleReader = ArrayStringPlusStringZZZ( objKernel.session, vsaRoleAuthor, "]", tpcall, "BEHIND")	
		If tpcall.ierr > 0 Then Error iERR_FUNCTION_CALL, tpcall.serr
		Set item = New notesitem(doc, sItem, vsaRoleReader, READERS)
		item.IsSummary = True
	Next iCount
		 */
		 
			}catch(NotesException ne){
				ExceptionZZZ ez = new ExceptionZZZ(ne.text, iERROR_RUNTIME, "KernelKernelNotesZZZ", ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
		}//END main:
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public boolean setDBKernelCalling(Database objDBin) throws ExceptionZZZ{
		//boolean bReturn = false;
		if(objDBin==null){			
			ExceptionZZZ ez = new ExceptionZZZ("Database'", this.iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
		this.dbKernelCalling = objDBin;
		return true;
	}
	
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//standard Kernel-databases
	
	public Database getDBApplicationCurrent(){
		try{
		if(this.dbApplicationCurrent == null){
			this.dbApplicationCurrent = this.getDBByAlias("Application","");
		}
		}catch(ExceptionZZZ e){
			System.out.println(e.getMessage());	
		}
		
		return this.dbApplicationCurrent;
	}

	public Database getDBInterchangeArchive(){
		try{
		if(this.dbInterchangeCurrentArchive == null){
			this.dbInterchangeCurrentArchive = this.getDBByAlias("InterchangeArchive","");
		}
		}catch(ExceptionZZZ e){
			System.out.println(e.getMessage());	
		}
		
		return this.dbInterchangeCurrentArchive;
	}
	
	
	public Database getDBConfigurationCurrent(){
		
		try{
		if(this.dbConfigurationCurrent == null){
			this.dbConfigurationCurrent = this.getDBByAlias("Configuration","");
			if(this.dbConfigurationCurrent == null){
				this.dbConfigurationCurrent = this.getAgentContextCurrent().getCurrentDatabase();				
			}
		}
		}catch(ExceptionZZZ e){
			System.out.println(e.getMessage());	
		}catch(NotesException e){
			System.out.println(e.getMessage());	
		}

		return this.dbConfigurationCurrent;
	}
	
	public DJAgentContext getAgentContextCurrent() throws ExceptionZZZ{
		DJAgentContext objFunction = null;			
		if(this.agtKernelCallingContext == null){
			//objFunction = this.getSession().getAgentContext();
			NotesContextProviderZZZ objContextProvider = this.getNotesContextProvider();
			if (objContextProvider!=null){
				this.agtKernelCallingContext = objContextProvider.getAgentContext();
			}
		}
		objFunction = this.agtKernelCallingContext;	
		return objFunction;
	}
	
	
	public Database getDBHelpdeskSupportCurrent(){
		try{
		if(this.dbHelpdeskSupportCurrent == null){
			this.dbHelpdeskSupportCurrent = this.getDBByAlias("SupportHelp","");
		}
		}catch(ExceptionZZZ e){
			System.out.println(e.getMessage());	
		}
		
		return this.dbHelpdeskSupportCurrent;
		
	}
	
	public Database getDBApplicationCurrentHelp(){
		try{
		if(this.dbApplicationCurrentHelp == null){
			this.dbApplicationCurrentHelp = this.getDBByAlias("ApplicationHelp","");
		}
		}catch(ExceptionZZZ e){
			System.out.println(e.getMessage());	
		}
		
		return this.dbApplicationCurrentHelp;				
	}
	
	public Database getDBServerCatalogCurrent(){
		try{
		if(this.dbServerCatalogCurrent == null){
			this.dbServerCatalogCurrent = this.getDBByAlias("DatabaseCatalog","");
		}
		}catch(ExceptionZZZ e){
			System.out.println(e.getMessage());	
		}
		
		return this.dbServerCatalogCurrent;
				
	}
	
	public Database getDBDirectoryMasterCurrent(){
		try{
		if(this.dbDirectoryMasterCurrent == null){
			this.dbDirectoryMasterCurrent = this.getDBByAlias("DirectoryMaster","");
		}
		}catch(ExceptionZZZ e){
			System.out.println(e.getMessage());	
		}
		
		return this.dbDirectoryMasterCurrent;
		
	}
	
	
	public Database getDBDirectoryCurrent(){
		
		try{
		if(this.dbDirectoryCurrent == null){
			this.dbDirectoryCurrent = this.getDBByAlias("Directory","");
		}
		}catch(ExceptionZZZ e){
			System.out.println(e.getMessage());	
		}
		
		return this.dbDirectoryCurrent;
	}
	
	public Database getDBInterchangeCurrent(){
		try{
		if(this.dbInterchangeCurrent == null){
			this.dbInterchangeCurrent = this.getDBByAlias("Interchange","");
		}
		}catch(ExceptionZZZ e){
			System.out.println(e.getMessage());	
		}
		
		return this.dbInterchangeCurrent;
		
	}
	
	
	public Database getDBMailSupportCurrent(){
		
		try{
		if(this.dbMailSupportCurrent == null){
			this.dbMailSupportCurrent = this.getDBByAlias("SupportMail","");
		}
		}catch(ExceptionZZZ e){
			System.out.println(e.getMessage());	
		}
		
		return this.dbMailSupportCurrent;
	}
	
	public Database getDBApplicationCurrentArchive(){
		try{
		if(this.dbApplicationCurrentArchive == null){
			this.dbApplicationCurrentArchive = this.getDBByAlias("ApplicationArchive","");
		}
		}catch(ExceptionZZZ e){
			System.out.println(e.getMessage());	
		}
		
		return this.dbApplicationCurrent;
	}
	
	/**
	 * @return handle on the configured notes-database 
	 */
	public Database getDBLogCurrent() throws ExceptionZZZ{
		Database dbFunction = null;
		if (this.dbLogCurrent == null){
			dbFunction = this.getDBByAlias("ApplicationLog","");
			if(dbFunction!=null){
				System.out.println(ReflectCodeZZZ.getMethodCurrentName() + ", LogDatabase found");
			}else{
				System.out.println(ReflectCodeZZZ.getMethodCurrentName() + ", NO LogDatabase found");
			}
			this.dbLogCurrent = dbFunction;
		}else{
			dbFunction = this.dbLogCurrent;
		}
		return dbFunction; 		
	}
	

	
	public String getLocationCurrent() throws ExceptionZZZ{
		String sFunction = null;
  		main:{
  			try{
  			if(this.sLocationCurrent == null){
  			    // Server oder Benutzername ermitteln, ist der Key für das Profildokument      
            String sNameKeyTemp = "";
            Database dbCur = this.getDBKernelCalling();
            sNameKeyTemp = dbCur.getServer();
            if(sNameKeyTemp.equals("")){
            	sNameKeyTemp = this.getSession().getUserName();	
            }            	

			Name nNameKey = this.getSession().createName(sNameKeyTemp);
			String sNameKey = nNameKey.getCanonical();
			
			this.sLocationCurrent = sNameKey;
  			}	
  		}catch(NotesException e){
  			ExceptionZZZ ez = new ExceptionZZZ(e.getMessage(), iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
  		}
  		} //end main:  		
  		end:{
  			sFunction = this.sLocationCurrent;
  			return sFunction;
  		}
  }	//end  Function

  
  /**!!! this method returns as Kernel-Object, remember there is a original noteslog-class. This original object can be received by objKernelNotesLog.getNotesLogObject(...);
* @return KernelNotesLogZZZ
* @return
* @throws ExceptionZZZ 
* 
* lindhaueradmin; 24.09.2006 11:07:40
 */
public KernelNotesLogZZZ getKernelNotesLogObject() throws ExceptionZZZ{
	  if(this.objKernelNotesLog==null){
		  //+++ Das Kernel-Log
		this.objKernelNotesLog = new KernelNotesLogZZZ(this, "");  //Merke: in diesem Spezialkonstruktor wird dann ein Typecast auf KernelNotesZZZ durchgeführt
	  }
	return this.objKernelNotesLog;
  }
public void setKernelNotesLogObject(KernelNotesLogZZZ objKernelNotesLog){
	this.objKernelNotesLog = objKernelNotesLog;
}

   //++++++++++++++++++++++++++++++
   public String ProfileApplicationKeyCompute() throws ExceptionZZZ{
  	String sFunction = null;
  	main:{
  	String sLocation = this.getLocationCurrent();  	

  	sFunction = sLocation + this.getApplicationKeyCurrent();
  	}//end main
  	end:{  		
  	return sFunction;
  	}
  	  	
  	/*
 	//+++ Das Profildokument für die Applikation in der aktuellen Datenbank, wird kein Key angegeben, so muß der ApplicationKey explizit gesetzt werden, andernfalls werden Default-Werte verwendet.
	If(this.sKeyApplicationInput == 0)
		sKeyApplication = objKernel.ApplicationKeyDefault
		If Len(sKeyApplication) = 0 Then
			sKeyApplication = objKernel.ApplicationKey
			If Len(objKernel.Errormessage) > 0 Then
				sError = objKernel.Errormessage & Chr(13) & Chr(10) & "Default ApplicationKey nicht definiert und Application Key nicht bei der Initialisierung des KernelObjekts übergeben."
				Goto EndeError
			End If	
		End If
	Else
		sKeyApplication = sKeyApplicationInput
	End If
	*/
  }//end function
  
  public String ProfileApplicationMaskCompute(){
  	String sFunction = null;
  	main:{ 	
  	sFunction = "frmAdminPath" + this.getKernelKey();
  	}//end main
  	end:{
  		
  	return sFunction;
  	}
  	  	
  	/*
 	//+++ Das Profildokument für die Applikation in der aktuellen Datenbank, wird kein Key angegeben, so muß der ApplicationKey explizit gesetzt werden, andernfalls werden Default-Werte verwendet.
	If(this.sKeyApplicationInput == 0)
		sKeyApplication = objKernel.ApplicationKeyDefault
		If Len(sKeyApplication) = 0 Then
			sKeyApplication = objKernel.ApplicationKey
			If Len(objKernel.Errormessage) > 0 Then
				sError = objKernel.Errormessage & Chr(13) & Chr(10) & "Default ApplicationKey nicht definiert und Application Key nicht bei der Initialisierung des KernelObjekts übergeben."
				Goto EndeError
			End If	
		End If
	Else
		sKeyApplication = sKeyApplicationInput
	End If
	*/	
  }

/**
 * Method KernelKeyCurrentGet.
 * @return String, which could be overwritten by a custom class
 */
public static String getKernelKey() {
	return "ZZZ";
}


/**
 * Method ApplicationKeyCurrentGet.
 * @return String
 */
public String getApplicationKeyCurrent() {
	String sFunction = null;
	main:{
		if(this.sKeyApplication ==""){
			sFunction = this.getApplicationKeyDefault();
		}else{
			sFunction = this.sKeyApplication;
		}
	}//end main
	end:{
		return sFunction;	
	}
}


	public Database getDB(String sServerin, String sPathin, String sFlagControlin)throws ExceptionZZZ{
	Database dbFunction = null;
	String sServer = null;
	String sPath = null;
	String sFlagControl = "";
	String stemp; int iCode; String sMethod;
	main:{
		try{
			paramcheck:{
				if(sServerin == null | StringZZZ.isEmpty(sPathin)){
					stemp = "Parameter Server/Path isnull.";
					ExceptionZZZ ez = new ExceptionZZZ(stemp, iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName()); 					
					throw ez;			
				}
				if(sFlagControlin != null){
					sFlagControl = sFlagControlin;
				}
				
				//diese Werte sind in der Configuration - erlaubt
				if(sServerin.equals("local") || sServerin.equals("-")){
					sServer = "";
				}else{
					sServer = sServerin;
				}
				sPath = sPathin;
				
			}//end paramcheck:

			//Get or Create the Database
			Session objSession = this.getSession();
			if(sFlagControl.toLowerCase().equals("createonfail")){			
				System.out.println(ReflectCodeZZZ.getMethodCurrentName() + ", createonfail");
				dbFunction = objSession.getDatabase(sServer, sPath, true);				
			}else{
				System.out.println(ReflectCodeZZZ.getMethodCurrentName() + ", NICHT createonfail");
				dbFunction = objSession.getDatabase(sServer, sPath);
			}	
			System.out.println(ReflectCodeZZZ.getMethodCurrentName() + ", Databasehandle for: '" + dbFunction.getServer() + "!!" + dbFunction.getFilePath());
	
		}catch(NotesException ne){
				System.out.println(ReflectCodeZZZ.getMethodCurrentName() + ", Error:" + ne.text);
				ExceptionZZZ ez = new ExceptionZZZ(ne.text, iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
		}	
		
	}//end main:
	end:{
		return dbFunction;
	}	
}

public Database getDBByAlias(String sAlias, String sFlagcontrol) throws ExceptionZZZ{
	Database dbFunction = null;
	int iCode; String sMethod; String stemp;
	main:{
		try{
		paramcheck:{
			if(StringZZZ.isEmpty(sAlias)){
				stemp = "ParameterAlias is NULL";				
				ExceptionZZZ ez = new ExceptionZZZ(stemp, iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName()); 			
				throw ez;	
			}			
		} //end paramcheck
		
		Document docProfileAppl = this.getApplicationProfile();
		String sApplicationKey = this.getApplicationKeyCurrent();
		
		Item objItemServer = docProfileAppl.getFirstItem(sFIELD_PREFIX_CONFIG_SERVER + sAlias + sApplicationKey);
		Item objItemDBPath =  docProfileAppl.getFirstItem(sFIELD_PREFIX_CONFIG_PATH + sAlias + sApplicationKey);

		if(objItemServer == null | objItemDBPath == null){
			stemp = "Profile Document Fields not available. Alias not configured ?";	
			System.out.println(ReflectCodeZZZ.getMethodCurrentName() + ", " + stemp);
			ExceptionZZZ ez = new ExceptionZZZ(stemp, iERROR_CONFIGURATION_MISSING, this, ReflectCodeZZZ.getMethodCurrentName()); 			
			throw ez;	
		}
		
		String sServer = objItemServer.getValueString();		
		String sDBPath = objItemDBPath.getValueString();
		System.out.println(ReflectCodeZZZ.getMethodCurrentName() + ", Hole Datenbankhandle für " + sServer + "!!" + sDBPath);
		if(sServer.equalsIgnoreCase("local") | sServer.equals("-")) sServer = "";
		
		//Get Handle on searched DB
		dbFunction = this.getDB(sServer, sDBPath, null);
		break main;
		}
		catch(NotesException e){
			System.out.println(ReflectCodeZZZ.getMethodCurrentName() + ", Error: " + e.getMessage());
			ExceptionZZZ ez = new ExceptionZZZ(e.text, iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
	}//end main:
	end:{
	return dbFunction;	
	}
}
	
	
	//++++++++++++++++++++++
	//Interface
	//++++++++++++++++++++++
	
	public AgentZZZ getAgentCalling() throws ExceptionZZZ{
		AgentZZZ objReturn = null;
		try{
			NotesContextProviderZZZ objContextProvider = this.getNotesContextProvider();
			DJAgentContext objContext = objContextProvider.getAgentContext();
			objReturn = objContext.getKernelAgent();
		}catch(NotesException ne){
			ExceptionZZZ ez = new ExceptionZZZ(ne.text, iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
		return objReturn;
	}
	
	/** Also: Den Modulalias übergeben und anhand des aktuellen (ggf. "faked") Agentennamens den Namen der Section erhalten
	
	 @author 0823 , date: 26.10.2004
	 @param sZAlias, the Alias of the Application-Part (Module Alias). E.g. 'Export' when you try to get an AgentAlias in the export-configuration	(this means a section name)                                                       	       
	 @return String, Agent alias name
	 @throws ExceptionZZZ
	 */
	public String getAgentAliasCurrentZ(String sZAlias)throws ExceptionZZZ{
		String sReturn = null;
	    KernelZZZ objKernel=null;
		main:{		
			try{
				String sEnvironmentVariable = null;
				check:{
					if(StringZZZ.isEmpty(sZAlias)){
						ExceptionZZZ ez = new ExceptionZZZ("missing parameter ZAlias-String", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
						throw ez;
					}
					objKernel = this.getKernelObject();
					if(objKernel == null){									
						ExceptionZZZ ez = new ExceptionZZZ("missing property 'KernelObject'", iERROR_PROPERTY_MISSING, this, ReflectCodeZZZ.getMethodCurrentName()); 						
						throw ez;
					}
					
					//Ermitteln des aktuellen Agenten
					//KernelNotesLogZZZ objLogNotes = this.getKernelNotesLogObject();
					//String sEnvironmentVariableIn = objLogNotes.getAgentCallingAlias();
					AgentZZZ objAgent = this.getAgentCalling();
					if(objAgent==null){
						ExceptionZZZ ez = new ExceptionZZZ("objKernel.getAgentCalling() does not return anything.", iERROR_PROPERTY_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
						throw ez;
					}
					
					sEnvironmentVariable = objAgent.getAgentAliasFirst();
					 if(StringZZZ.isEmpty(sEnvironmentVariable)){
						ExceptionZZZ ez = new ExceptionZZZ("objAgent.getAgentAlias() does not return anything", iERROR_PROPERTY_MISSING, this, ReflectCodeZZZ.getMethodCurrentName()); 						
						throw ez;				
					}
				}//end check
				
				File objFileConfig = objKernel.getFileConfigByAlias(sZAlias);
				if(objFileConfig == null){
					ExceptionZZZ ez = new ExceptionZZZ("No configuration file configured for the module '" + sZAlias + "'");
					throw ez;
				}
				if(objFileConfig.exists()==false){
						ExceptionZZZ ez = new ExceptionZZZ("file not found '" + objFileConfig.getPath(), iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName()); 					
						throw ez;
				}
				if(objFileConfig.isDirectory()){
					ExceptionZZZ ez = new ExceptionZZZ("configuration file is a directory '" + objFileConfig.getPath(), iERROR_PARAMETER_VALUE, this, ReflectCodeZZZ.getMethodCurrentName()); 		
					throw ez;
				}
				
				
				//find the Environment-Variable in the ini-File
				FileIniZZZ objIni = new FileIniZZZ(objKernel, objFileConfig, null);
				String sSystemKey = objKernel.getSystemKey();
				sReturn = objIni.getPropertyValue(sSystemKey, sEnvironmentVariable);
			}catch(NotesException ne){
				ExceptionZZZ ez = new ExceptionZZZ(ne.text, iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
		}//end main:
		return sReturn;
	}//end function
	
	/** 
	
	 @author 0823 , date: 19.10.2004
	 @param sZAliasIn
	 @param sParamIn
	 @return String entry in the configuration file, specified by sZAliasIn. To configure an agent parameter, you first must configure an z-kernel-agent-alias.
	This agent alias is used as another section name. Below this section you can find a property which is the parameter.
	<CR>Example:<CR>
	[FGL#03]
;the section of application # systemnumber combination   (called systemkey)
;the following property is an notes-agent alias name. The Value is the z-kernel-agent-alias
agtDataFileTextCopyToDomJAZ=FGL#03;Copy1

[FGL#03;Copy1]
;this is the section for the notes-agent 'agtDataFileTextCopyToDomJAZ'. The properties are the agent parameters.
SourcePath=C:\tempfgl\SI\EDM_Interface\temp
TargetPath=c:\tempfgl\SI\EDM_Interface\file00ZZZ


	 @throws ExceptionZZZ
	 */
	public String getAgentParameterCurrentZ(String sZAliasIn, String sParamIn)throws ExceptionZZZ{
		//TODO gleich methode, bei der das Object FileIniZZZ schon übergeben wird, soll bei wiederholten Aufrufen die Performance steigern.
		String sReturn = null;
		String sMethod; KernelZZZ objKernel=null;
		main:{
			try{
			String sZAlias;
			String sEnvironmentVariable; //wird hier nicht als parameter übergeben sondern muss aus dem AgentenContext ermittelt werden.
			String sParam;    
			check:{
				objKernel = this.getKernelObject();
				if(objKernel == null){
					ExceptionZZZ ez = new ExceptionZZZ("missing property 'Z-KernelObject'", iERROR_PROPERTY_MISSING, this, ReflectCodeZZZ.getMethodCurrentName()); 	
					throw ez;									
				}
				if(StringZZZ.isEmpty(sZAliasIn)){
					ExceptionZZZ ez = new ExceptionZZZ("missing/empty paramter 'Z-Configuration-Alias'",  iERROR_PARAMETER_MISSING, ReflectCodeZZZ.getMethodCurrentName()); 					
					throw ez;	
				}else{
					sZAlias = sZAliasIn;
				}
				
				if(StringZZZ.isEmpty(sParamIn)){
					ExceptionZZZ ez = new ExceptionZZZ("missing/empty paramter 'Z-Configuration-Parameter'", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName()); 					
					throw ez;	
				}else{
					sParam = sParamIn;
				}
				
				AgentZZZ objAgent = this.getAgentCalling();
				if(objAgent==null){
					ExceptionZZZ ez = new ExceptionZZZ("objKernel.getAgentCalling() does not return anything.", iERROR_PROPERTY_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
					throw ez;
				}
				
				sEnvironmentVariable = objAgent.getAgentAliasFirst();
				 if(StringZZZ.isEmpty(sEnvironmentVariable)){
					ExceptionZZZ ez = new ExceptionZZZ("objAgent.getAgentAlias() does not return anything", iERROR_PROPERTY_MISSING, this, ReflectCodeZZZ.getMethodCurrentName()); 				
					throw ez;				
				}			
			}//end check

			
			String sAgentAlias = this.getAgentAliasCurrentZ(sZAlias);
			if(StringZZZ.isEmpty(sAgentAlias)){
				ExceptionZZZ ez = new ExceptionZZZ("The agent seems not to be configured in the configuration file. objKernelNotes.getAgentAliasCurrentZ(..) can´t find an entry for the current agent.'",iERROR_CONFIGURATION_MISSING, this, ReflectCodeZZZ.getMethodCurrentName()); 				
				throw ez;
			}
			
			//### Zum Auslesen der weiteren Informationen brauchen wir ein Handle auf das Configurationsfile selbst
			File objFileConfig = objKernel.getFileConfigByAlias(sZAlias);
			if(objFileConfig == null){
				ExceptionZZZ ez = new ExceptionZZZ("No configuration file configured for the module '" + sZAlias + "'");
				throw ez;
			}
			if(objFileConfig.exists()==false){
					ExceptionZZZ ez = new ExceptionZZZ("file not found '" + objFileConfig.getPath(), iERROR_CONFIGURATION_MISSING, this, ReflectCodeZZZ.getMethodCurrentName()); 					
					throw ez;
			}
			if(objFileConfig.isDirectory()){
				ExceptionZZZ ez = new ExceptionZZZ("configuration file is a directory '" + objFileConfig.getPath(), iERROR_CONFIGURATION_VALUE, this, ReflectCodeZZZ.getMethodCurrentName()); 				
				throw ez;
			}
			
			//find the Environment-Variable in the ini-File
			FileIniZZZ objIni = new FileIniZZZ(objKernel, objFileConfig, null);
			sReturn = objIni.getPropertyValue(sAgentAlias, sParam);
			
			}catch(NotesException ne){
				ExceptionZZZ ez = new ExceptionZZZ(ne.text, iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
		}//end main:
		return sReturn;
	}
	

	
	/**  This method will return a value which will be found in the SystemKey-Section of the configured module file.
	 * 
	 * Similar method: getAgentParameterCurrentZ(...).
	 * 
	* @return String
	* @param sZAliasIn (the module name)
	* @param sEnvironmentVariableIn (the property to search for in the systemkey-section)
	* @return
	* @throws ExceptionZZZ 
	* 
	* lindhaueradmin; 29.09.2006 08:35:34
	 */
	public String getEnvironmentZ(String sZAliasIn, String sEnvironmentVariableIn) throws ExceptionZZZ{
		//TODO eine methode, bei der das FileIniZZZ-Object schon übergeben wird, Performance.
		String sReturn = null;
		String sMethod; KernelZZZ objKernel=null;
		main:{
			String sEnvironmentVariable;
			String sZAlias;
			check:{
				objKernel = this.getKernelObject();
				if(objKernel == null){
					ExceptionZZZ ez = new ExceptionZZZ("missing property 'KernelObject'", iERROR_PROPERTY_MISSING, this, ReflectCodeZZZ.getMethodCurrentName()); 				
					throw ez;
				}
				
				if(StringZZZ.isEmpty(sZAliasIn)){
					ExceptionZZZ ez = new ExceptionZZZ("missing paramter 'Z-Configuration-Alias'", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName()); 				
					throw ez;				
				}else{
					sZAlias = sZAliasIn;
				}
				
				if(sEnvironmentVariableIn==null){
					ExceptionZZZ ez = new ExceptionZZZ("missing parameter 'EnvironmentVariable'", iERROR_PARAMETER_MISSING,  this, ReflectCodeZZZ.getMethodCurrentName()); 
					throw ez;			
				}else{
					sEnvironmentVariable = sEnvironmentVariableIn;
				}
				
				objLog = this.getLogObject();
				if(objLog == null){
					objLog = objKernel.getLogObject();
				}
			}//end check
			
			File objFileConfig = objKernel.getFileConfigByAlias(sZAlias);
			if(objFileConfig.exists()==false){					
					ExceptionZZZ ez = new ExceptionZZZ("file not found '" + objFileConfig.getPath(), iERROR_CONFIGURATION_MISSING, this, ReflectCodeZZZ.getMethodCurrentName()); 					
					throw ez;
			}
			if(objFileConfig.isDirectory()){
				ExceptionZZZ ez = new ExceptionZZZ("configuration file is a directory '" + objFileConfig.getPath(), iERROR_CONFIGURATION_MISSING, this, ReflectCodeZZZ.getMethodCurrentName()); 				
				throw ez;
			}
			
			//find the ini-File for the alias
			FileIniZZZ objIni = new FileIniZZZ(objKernel, objFileConfig, null);
			String sSystemKey = objKernel.getSystemKey();
			
			//TODO: Prüfen auf korrektheit der neuen Methode
			//sReturn = objIni.getStringProperty(sSystemKey, sEnvironmentVariable);
	sReturn = objIni.getPropertyValue(sSystemKey, sEnvironmentVariable);
			
		}//end main:
		return sReturn;
	}//end functionon
	
	
	public String getPathDirectoryDefault() throws ExceptionZZZ{
		String sReturn = null;
		main:{ 
			if (this.getNotesContextProvider()!=null){
				sReturn = this.getNotesContextProvider().getPathDirectoryDefault(); 				
			}
			
			if(StringZZZ.isEmpty(sReturn)){
					//+++ lies den Pfad aus dem Notesdata-Verzeichnis
					//get the notes-data directory 
					String sDirectoryNotes = this.getPathNotesData();
					//System.out.println("getPathDirectoryDefault#"+ sDirectoryNotes);
			
					//Compute and test the default directory
					sReturn = DirectoryCompute_(sDirectoryNotes, "fileZZZ", "");
					//System.out.println("getPathDirectoryDefault#"+ sReturn);
			}
		}//end main
		return sReturn;
	}
	
	private String DirectoryCompute_(String sBaseDirectory, String sDirectory, String sFlagControl) throws ExceptionZZZ{
			String sReturn = null;
			main:{
				check:{
					if(StringZZZ.isEmpty(sBaseDirectory)){								
							ExceptionZZZ ez = new ExceptionZZZ("missing parameter 'base directory'", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName()); 									
							throw ez;
			       }
		
				//does the base-directory exists ?
				File dir = new File(sBaseDirectory);
				if (dir.exists() == false){
					//Verzeichnis erstellen, falls nicht vorhanden.
		     		objKernelNotesLog.writeLog("'base directory '" + sBaseDirectory + "' does not exist. It will be created.");
					boolean btemp = dir.mkdir();
					if(btemp == true){
						objKernelNotesLog.writeLog("base directory '" + sBaseDirectory + "' successfully created", 3);
					} else {
						String stemp = "'base directory could not be created: '" + sBaseDirectory + "'";																
						ExceptionZZZ ez = new ExceptionZZZ(stemp, iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName()); 														
						throw ez;
					}
				}else{
						
					//Vorhandenen Namen prüfen, ob er auch tatsächlich ein Verzeichnis ist.						
					if(dir.isDirectory() == false){
							String stemp = "a file exists with the 'base directory' name: '" + sBaseDirectory + "'";																	
							ExceptionZZZ ez = new ExceptionZZZ(stemp, iERROR_PARAMETER_VALUE, this, ReflectCodeZZZ.getMethodCurrentName()); 														
							throw ez;
					}
				}//end if dir.exists				
			} //end check:

			
			//Das Verzeichnis (Falls nicht anders in dem KernelZZZ, über die ini-Datei, definiert)
			// so wird beispielsweise der UNC-Pfad angegeben   String sDirectorySource = "\\\\SAP-Storage\\SAP2Notes";
			if(StringZZZ.isEmpty(sDirectory)){
				sReturn = sBaseDirectory;
				break main;
			}else{
				sReturn = sBaseDirectory + "\\" + sDirectory;
				
				//Check the new directory
				File dir = new File(sReturn);
				if (dir.exists() == true){
					if(dir.isDirectory() == false){
						String stemp = "a file exists with the 'directory' name: '" + sReturn + "'";					
						ExceptionZZZ ez = new ExceptionZZZ(stemp, iERROR_PARAMETER_VALUE, this, ReflectCodeZZZ.getMethodCurrentName()); 											
						throw ez;
					}
					break main;
				}else{
					boolean btemp = dir.mkdir();
					if(btemp == true){
						objKernelNotesLog.writeLog("directory successfully created: '" + sReturn + "'", 3);										
					} else {
						String stemp = "'directory could not be created: '" + sReturn + "'";															
						ExceptionZZZ ez = new ExceptionZZZ(stemp, iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName()); 										
						throw ez;
					}
					break main;
				}					
			}//End if .isEmpty(sDirectory)										
			}//END main		
			return sReturn;
		}
	
  

//++++++++++++++++++++++++++++++
	// Konstruktor
//++++++++++++++++++++++++++++++  
  private boolean KernelNotesNew_(Session sessionIn, String sKeyApplicationIn, String sSystemNumberIn, String[] saFlagControlIn) throws ExceptionZZZ{
  	boolean bReturn = false;
  	boolean btemp = false;
  	String sMethod; String stemp; int iCode;
  	Document docProfileApplication = null;
  	//Session objSessionFactory = null;
  	
  	start:
  			//System.out.println("Start: Konstruktor des Basic-Kernels");
main:{
            paramcheck:{

            //Application-Key
            if(StringZZZ.isEmpty(sKeyApplicationIn)){
            		this.sKeyApplication = this.getApplicationKeyDefault();	
            	}else{
            		this.sKeyApplication = sKeyApplicationIn;
            	}
     
            
            //SystemNumber
            if(StringZZZ.isEmpty(sSystemNumberIn)){
            		this.sSystemNumber = this.sCONFIG_NUMBER_SYSTEM_DEFAULT;
            	}else{
            		this.sSystemNumber= sSystemNumberIn;
            	}		
            }//end paramcheck
                  
		//setzen der übergebenen Flags	
		if(saFlagControlIn != null){
			for(int iCount = 0;iCount<=saFlagControlIn.length-1;iCount++){
				stemp = saFlagControlIn[iCount];
				btemp = setFlag(stemp, true);
				if(btemp==false){
					ExceptionZZZ ez = new ExceptionZZZ("the flag '" + stemp + "' is not available.", iERROR_FLAG_UNAVAILABLE, this, ReflectCodeZZZ.getMethodCurrentName()); 					
					throw ez;		 
				}
			}
			if(this.getFlag("INIT")==true){
				bReturn = true;
				break main; 
			}
		}
		
		
		   if(sessionIn != null){
           	this.objSession = sessionIn;
           }else{
	            // Corba.ORB wird als nicht stabil angesehen !!!
	            // Darum kann man es nicht einfach hier erzeugen !
	            // this.session = NotesFactory.createSession();           		
	            
	            //Das geht nur in einer Klasse, die AgentBase erweitert.
	            //get the session
				//Session session = getSession();
	            
           	//Oder wenn wir uns das selbst konstruiert haben
           	this.objSession = this.getSession();
           }  
		
		//Analyse im Vorfeld, ob es einen AgentContext gibt. Aber nur, wenn das "UseContextByKernel" - Flag nicht schon auf "true" steht.
		if(this.getFlag("UseContextByKernel")==false){
			boolean btemp2 = this.hasAgentContextBySession();
			if(btemp2==false){
				this.setFlag("UseContextByKernel", true);
			}
		}
		
		//+++ Handle auf die Profildokumente
		this.docProfileApplication = this.getApplicationProfile();
		bReturn = true;
}//END main:
  		return bReturn;
  }//end function 

  
  //**************************************
  //+++ DESTRUKTOR
  //**************************************
protected void finalize(){
	this.recycle();
	
	/* dat ist nicht die lösung
	if(this.docProfileApplication!=null){
		GC.recycle(this.docProfileApplication);
	}
	if(this.dbLogCurrent!=null){
		GC.recycle(this.dbLogCurrent);
	}
	if(this.dbKernelCalling!=null){
		GC.recycle(this.dbKernelCalling);
	}
	if(this.dbMailSupportCurrent!=null){
		GC.recycle(this.dbMailSupportCurrent);
	}
	if(this.dbDirectoryMasterCurrent!=null){
		GC.recycle(this.dbDirectoryMasterCurrent);
	}
	if(this.dbKernelCalling!=null){
		GC.recycle(this.dbKernelCalling);
	}
	if(this.dbInterchangeCurrent!=null){
		GC.recycle(this.dbInterchangeCurrent);
	}
	if(this.dbInterchangeCurrentArchive!=null){
		GC.recycle(this.dbInterchangeCurrentArchive);
	}
	if(this.dbApplicationCurrent!=null){
		GC.recycle(this.dbApplicationCurrent);
	}
	if(this.dbApplicationCurrentArchive!=null){
		GC.recycle(this.dbApplicationCurrentArchive);
	}
	if(this.dbDirectoryCurrent!=null){
		GC.recycle(this.dbDirectoryCurrent);
	}
	if(this.dbHelpdeskSupportCurrent!=null){
		GC.recycle(this.dbHelpdeskSupportCurrent);
	}
	if(this.dbApplicationCurrentHelp!=null){
		GC.recycle(this.dbApplicationCurrentHelp);
	}
	if(this.dbServerCatalogCurrent!=null){
		GC.recycle(this.dbServerCatalogCurrent);
	}
	if(this.dbKernelCalling!=null){
		GC.recycle(this.dbKernelCalling);
	}
	if(this.agtKernelCallingContext!=null){
		GC.recycle(this.agtKernelCallingContext);
	}
	if(this.objContextProvider!=null){
		this.objContextProvider.recycle();
	}
	if(this.objSession!=null){
		GC.recycle(this.objSession);		
	}
	*/
}

public void recycle(){
	if(this.objContextProvider!=null) this.objContextProvider.recycle();
}
  
public String getSystemKey() throws ExceptionZZZ{
	return this.getApplicationKeyCurrent()+"#"+ this.getSystemNumber();	
}

public String getSystemNumber() throws ExceptionZZZ{
	try{
		if(StringZZZ.isEmpty(this.sSystemNumber)){
			//Falls es sich z.B. um ein Servlet handelt, wird der Context gefaked, ansonsten soll man die SystemNumber aus dem Applikation-Profildokument auslesen können.
			if(this.hasAgentContextBySession()==false){
				NotesContextProviderZZZ objContext = this.getNotesContextProvider();
				this.sSystemNumber = objContext.computeSystemNumber();
			}else{
				Document docProfileAppl = this.getApplicationProfile();
				String sSystemNumber = docProfileAppl.getItemValueString("SystemNumberZZZ");
				if(StringZZZ.isEmpty(sSystemNumber)){
					this.sSystemNumber = "01";
				}else{
					this.sSystemNumber = sSystemNumber;
				}
			}
		}
	}catch(NotesException ne){
		ExceptionZZZ ez = new ExceptionZZZ(ne.getMessage(), iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
		throw ez;
	}
	return this.sSystemNumber;
}

/**Proofes, if there is an agent context available
* @return boolean
* @return 
* 
* lindhaueradmin; 19.09.2006 08:35:02
 * @throws NotesException 
 */
public boolean hasAgentContextBySession() throws ExceptionZZZ{
	boolean bReturn = false;
	try{
		Session objSession = this.getSession();
		AgentContext objAgentContext = objSession.getAgentContext();
		if(objAgentContext!=null) bReturn = true;
	}catch(NotesException ne){
		ExceptionZZZ ez = new ExceptionZZZ(ne.getMessage(), iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
		throw ez;
	}
	return bReturn;
}


} //end class KernelKernelZZZ


