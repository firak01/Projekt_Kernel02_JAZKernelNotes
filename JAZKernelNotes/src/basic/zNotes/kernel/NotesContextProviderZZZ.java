package basic.zNotes.kernel;
import java.io.File;

import custom.zKernel.file.ini.FileIniZZZ;
import lotus.domino.*;
import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.file.FileEasyZZZ;
import basic.zKernel.IKernelZZZ;
import basic.zKernel.KernelUseObjectZZZ;
import basic.zNotes.basic.*;


/**
 * Stellt h�ufig ben�tigte Domino Objekte f�r einen 
 * Test bereit.
 * @author Thomas Ekert
 * 
 * FGL: 20060922
 * Erweitert um den AgentContext, der f�r Servlets "gefaked" werden muss.
 *
 */
public class NotesContextProviderZZZ extends KernelUseObjectZZZ{
	private Session objSession = null;
	private Database testDb = null;
	private Document docContext = null;
	private DJAgentContext objAgentContext = null;	
	
	//FGL Erweiterung, wichtig zum "Konstruieren einer Session"
	private String sHost = null;
	private String sServer = null;
	private String sDatabase = null;
	private String sUsername = null;
	private String sPassword = null;
	
	//Wichtig zum Finden der Parameter in der konfigurierten ini-Datei. Werden ggf. im Konstruktor �bergeben.
	private String sAgentName = null;
	private String sModuleName = null;
	
	//Zum Bearbeiten der Notes.ini-Datei notwendige Variablen
	private String sUserIdPath = null;
	private String sUserIdPathOld = null;
	private String sNotesIniPath = null;
	
	//Pfad der als default Verzeichnis f�r alles m�gliche genutzt werden kann
	private String sPathDirectoryDefault = null;
	

	private static final String sFORM_CONTEXT_DUMMY = "frmContextDummyZZZ";
	private static final int NOTESEXCEPTION_NO = 999;
	

	//Flags
	boolean bFlagHasAgentContextReal=false;
	boolean bFlagUseStaticNotesThread=false; //Wird beim erzeugen einer lokalen notessesion auf true gesetzt. Notesthread.sinit wird ausgef�hrt.
	                                                                 //Dies ist wichtig, da im Destruktor Notesthread.sterm ausgef�hrt werden soll.
	boolean bFlagUseConfiguredKeyFileName=false; //Bei lokalen Sessions kann man ggf. einen notesid gezielt verwenden.
	
	//FGL Erweiterung: Konstruktoren
	/** use this Contruktor in a real notesagent
	* lindhaueradmin; 28.09.2006 08:52:53
	 * @param objKernel
	 * @throws ExceptionZZZ 
	 */
	public  NotesContextProviderZZZ(IKernelZZZ objKernel, String sModuleName, Session objSession) throws ExceptionZZZ{		
		super(objKernel);	
		objKernel.getLogObject().WriteLineDate(ReflectCodeZZZ.getMethodCurrentName() + "# Kontextdaten basieren auf Modul: '" + sModuleName + "'.");
		if(objSession==null){
			objKernel.getLogObject().WriteLineDate(ReflectCodeZZZ.getMethodCurrentName() + "# Kein Session - Objekt �bergeben.");
			ExceptionZZZ ez = new ExceptionZZZ("Session object", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
		if(StringZZZ.isEmpty((sModuleName))){
			objKernel.getLogObject().WriteLineDate(ReflectCodeZZZ.getMethodCurrentName() + "# Kein Modulname  �bergeben.");
			ExceptionZZZ ez = new ExceptionZZZ("Module name", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
		this.objSession = objSession;
		this.sModuleName = sModuleName;
		//Merke: Der Agentname wird nicht durch den KernelZZZ gefaked, weil davon ausgegangen wird, das dieser Code im Context eines echten Agenten l�uft
	   	this.setFlag("UseContextByKernel", false);
	}
	
	/**  use this contructor in a servlet, when you have to "fake" the agentname. The Session will be a local session.
	* lindhaueradmin; 28.09.2006 08:54:37
	 * @param objKernel
	 * @param sAgentName
	 * @throws ExceptionZZZ 
	 */
	public NotesContextProviderZZZ(IKernelZZZ objKernel, String sModuleName, String sAgentName) throws ExceptionZZZ{
		super(objKernel);
		objKernel.getLogObject().WriteLineDate(ReflectCodeZZZ.getMethodCurrentName() + "# Kontextdaten basieren auf Modul: '" + sModuleName + "', Agent: '" + sAgentName + "'.");
		if(StringZZZ.isEmpty(sAgentName)){
			ExceptionZZZ ez = new ExceptionZZZ("Faked agent name", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
		if(StringZZZ.isEmpty((sModuleName))){
			ExceptionZZZ ez = new ExceptionZZZ("Module name", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
		this.sModuleName = sModuleName;
		this.sAgentName = sAgentName;
	   	this.setFlag("UseContextByKernel", true);
	}
	
	/**  use this contructor in a servlet, when you have to "fake" the agentname. (e.g. in a servlet).  You have to create a session bevore. (e.g. in a servlet by using the HttpRequest Objekt to create as session).
	* lindhauer; 27.01.2008 09:13:17
	 * @param objKernel
	 * @param sModuleName
	 * @param sAgentName
	 * @param session
	 * @throws ExceptionZZZ
	 */
	public NotesContextProviderZZZ(IKernelZZZ objKernel, String sModuleName, String sAgentName, Session session) throws ExceptionZZZ{
		super(objKernel);
		
		objKernel.getLogObject().WriteLineDate(ReflectCodeZZZ.getMethodCurrentName() + "# Kontextdaten basieren auf Modul: '" + sModuleName + "', Agent: '" + sAgentName + "'.");
		if(StringZZZ.isEmpty(sAgentName)){
			objKernel.getLogObject().WriteLineDate(ReflectCodeZZZ.getMethodCurrentName() + "# Kein Agentenname  �bergeben.");
			ExceptionZZZ ez = new ExceptionZZZ("Faked agent name", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
		if(StringZZZ.isEmpty((sModuleName))){
			objKernel.getLogObject().WriteLineDate(ReflectCodeZZZ.getMethodCurrentName() + "# Kein Modulname  �bergeben.");
			ExceptionZZZ ez = new ExceptionZZZ("Module name", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
		if(session == null){
			objKernel.getLogObject().WriteLineDate(ReflectCodeZZZ.getMethodCurrentName() + "# Kein Session - Objekt �bergeben.");
			ExceptionZZZ ez = new ExceptionZZZ("Notessession not provided", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
		this.sModuleName = sModuleName;
		this.sAgentName = sAgentName;
		this.objSession = session;
	   	this.setFlag("UseContextByKernel", true);
	}
	
	
	/**  use this contructor in a servlet, when you have to "fake" the agentname.
	* lindhaueradmin; 28.09.2006 08:54:37
	 * @param objKernel
	 * @param sAgentName
	 * @throws ExceptionZZZ 
	 */
	public NotesContextProviderZZZ(IKernelZZZ objKernel, String sModuleAndAgentName) throws ExceptionZZZ{
		super(objKernel);
		
		objKernel.getLogObject().WriteLineDate(ReflectCodeZZZ.getMethodCurrentName() + "# Kontextdaten basieren auf Modul: '" + sModuleName + "', Agent: '" + sAgentName + "'.");
		if(StringZZZ.isEmpty(sAgentName)){
			ExceptionZZZ ez = new ExceptionZZZ("Faked agent name AND module name", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
		objKernel.getLogObject().WriteLineDate(ReflectCodeZZZ.getMethodCurrentName() + "# Kontextdaten basieren auf Modul und Agent : '" + sModuleAndAgentName + "'.");
		this.sModuleName = sModuleAndAgentName;
		this.sAgentName = sModuleAndAgentName;
	   	this.setFlag("UseContextByKernel", true);
	}
	
	//########### DESTRUCTOR
	protected void finalize(){
		recycle();
	}

	/* (non-Javadoc)
	@see zzzKernel.basic.KernelObjectZZZ#getFlag(java.lang.String)
	Flags used:<CR>
	- UseStaticNotesThread
	 */
	public boolean getFlag(String sFlagName){
		boolean bFunction = false;
		main:{
			if(StringZZZ.isEmpty(sFlagName)) break main;
			bFunction = super.getFlag(sFlagName);
			if(bFunction==true) break main;
		
			//getting the flags of this object
			String stemp = sFlagName.toLowerCase();
			if(stemp.equals("usestaticnotesthread")){
				bFunction = bFlagUseStaticNotesThread;
				break main;
			}else if(stemp.equals("hasagentcontextreal")){
				bFunction = bFlagHasAgentContextReal;
				break main;
			}else if(stemp.equals("useconfiguredkeyfilename")){
				bFunction = bFlagUseConfiguredKeyFileName;
				break main;
			}
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
		if(stemp.equals("usestaticnotesthread")){
			bFlagUseStaticNotesThread = bFlagValue;
			bFunction = true;
			break main;
		}else if(stemp.equals("hasagentcontextreal")){
			bFlagHasAgentContextReal = bFlagValue;
			bFunction = true;
			break main;
		}else if(stemp.equals("useconfiguredkeyfilename")){
			bFlagUseConfiguredKeyFileName = bFlagValue;
			bFunction = true;
			break main;
		}
		}//end main:
		return bFunction;
	}
	
	//######################################################
	public DJAgentContext getAgentContext() throws ExceptionZZZ{
		DJAgentContext objReturn = null;
		try{
			IKernelZZZ objKernel = this.getKernelObject();
		if(this.objAgentContext==null){
			objKernel.getLogObject().WriteLineDate(ReflectCodeZZZ.getMethodCurrentName() + "#Versuche neues Notes-Context-Objekt zu bekommen.");
			
			Session objSession = this.getSession();
			if(objSession==null){
				objKernel.getLogObject().WriteLineDate(ReflectCodeZZZ.getMethodCurrentName() + "# Kein Session Objekt");
				ExceptionZZZ ez = new ExceptionZZZ("Session-Object", iERROR_PROPERTY_EMPTY, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
	
			
			AgentContext objAgentContextReal = objSession.getAgentContext();
			if(objAgentContextReal != null){
				//Das gefakte Context-Objekt nun mit dem echten AgentContext konstruieren
				objKernel.getLogObject().WriteLineDate(ReflectCodeZZZ.getMethodCurrentName()+ "#Real agent context available" );
	
				this.setFlag("HasAgentContextReal", true);
				objReturn = new DJAgentContext(objKernel, objSession);
			}else{
				//Das gefakte Context-Objekt nun mit dem Kernel-Object Konstruieren und allen anderen Informationen, die der Notes-Context-Provider zur Verf�gung stellen kann.
				objKernel.getLogObject().WriteLineDate(ReflectCodeZZZ.getMethodCurrentName()+ "#Agent context NOT available- FAKE IT !!!" );
				
				this.setFlag("HasAgentContextReal", false);
				objReturn = new DJAgentContext(objKernel, this.getUsername(), objSession, this.getDb(), this.getDocumentContext(), this.getAgentName());
			}

		}else{
			objReturn = this.objAgentContext;
		}
		}catch(NotesException ne){
			ExceptionZZZ ez = new ExceptionZZZ(ne.text, iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
		return objReturn;
	}
	
	/** Returns the given ModuleName (provided in the Konstruktor
	* @return String
	* @return 
	* 
	* lindhaueradmin; 31.10.2006 10:29:39
	 */
	public String getModuleName(){
		return this.sModuleName;
	}
	
	public String getAgentName() throws ExceptionZZZ{
		String sReturn = "";
		try{
		main:{
			if(StringZZZ.isEmpty(this.sAgentName)){
				Session objSession = this.getSession();
				AgentContext objAgentContextReal = objSession.getAgentContext();
				if(objAgentContextReal!=null){
					this.setFlag("HasAgentContextReal", true);
					this.sAgentName = objAgentContextReal.getCurrentAgent().getName();
				}else{
					this.setFlag("HasAgentContextReal", false);
					ExceptionZZZ ez = new ExceptionZZZ("A faked agent name has to be provided in the constructor of the NotesContextProviderZZZ-Object", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
					throw ez;
				}
			}
			sReturn = this.sAgentName;		
		}//End main
		}catch(NotesException ne){
			ExceptionZZZ ez = new ExceptionZZZ(ne.text, iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
		return sReturn;
	}
	
	public AgentZZZ getKernelAgent() throws ExceptionZZZ{
		AgentZZZ objReturn = null;
		DJAgentContext objContext = this.getAgentContext();
		try{
		objReturn = objContext.getKernelAgent();
		}catch(NotesException ne){
			ExceptionZZZ ez = new ExceptionZZZ(ne.text, iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
		return objReturn;
	}
	
	public static String computeSystemNumber(Session objSession) throws ExceptionZZZ{
		String sReturn = new String("");
		try{
		if(objSession==null){
			ExceptionZZZ ez = new ExceptionZZZ("Notessession", iERROR_PARAMETER_MISSING, "NotesContextProviderZZZ/" + ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
		AgentContext objContext = objSession.getAgentContext();
		if(objContext == null){
			ExceptionZZZ ez = new ExceptionZZZ("Unable to receive agent context. Use this method only in real notesagent !!!", iERROR_RUNTIME, "NotesContextProviderZZZ/" + ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
		Database dbCur = objContext.getCurrentDatabase();		
		sReturn = NotesContextProviderZZZ.computeSystemNumber(dbCur);
		
		}catch(NotesException ne){
			ExceptionZZZ ez = new ExceptionZZZ(ne.text, iERROR_RUNTIME, "NotesContextProviderZZZ/" + ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
		return sReturn;
	}
	public static String computeSystemNumber(Database dbCur) throws ExceptionZZZ{
		String sReturn = new String("");
		try{
		main:{
			if(dbCur==null){
				ExceptionZZZ ez = new ExceptionZZZ("Database", iERROR_PARAMETER_MISSING, "NotesContextProviderZZZ/" + ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
			
//		Hier ergibt sich die SystemNumber aus dem Datenbanktitel
		String stemp = dbCur.getTitle();
		stemp = stemp.toLowerCase();
		if(stemp.endsWith("[test]")){
			sReturn = new String("02");
		}else if(stemp.endsWith("[entwicklung]")){
			sReturn = new String("03");
		}else{
			sReturn = new String("01");
		}
	}//END main
		}catch(NotesException ne){
			ExceptionZZZ ez = new ExceptionZZZ(ne.text, iERROR_RUNTIME,  "NotesContextProviderZZZ/" + ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
		return sReturn;
	}
	
	public String computeSystemNumber() throws ExceptionZZZ{
		String sReturn = new String("");
		main:{
			try{
				DJAgentContext objContext = this.getAgentContext();
				if(objContext==null){
					ExceptionZZZ ez = new ExceptionZZZ("unable to receive wrapper object for agent context", iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
					throw ez;
				}
				
				//Nun ist zu differenzieren zwischen dem echten AgentContext und dem "gefakten" Agent Context.
				//Der gefakte Agent Context wird z.B. beim Servlet verwendet.
				if(this.getFlag("HasAgentContextReal")==true){
					//FALL: ECHTER CONTEXT
					Database dbCur = objContext.getCurrentDatabase();
					if(dbCur==null){
						ExceptionZZZ ez = new ExceptionZZZ("no current database found !!!", iERROR_PROPERTY_MISSING, "KernelNotesZZZ/" + ReflectCodeZZZ.getMethodCurrentName()); 
						throw ez;
					}		
					
					sReturn = NotesContextProviderZZZ.computeSystemNumber(dbCur);
				}else{
					//FALL: GEFAKTER CONTEXT
					IKernelZZZ objKernel = this.getKernelObject();					
					sReturn = objKernel.getSystemNumber();					
				}
			}catch(NotesException ne){
				ExceptionZZZ ez = new ExceptionZZZ(ne.text, iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
		}//end main
		return sReturn;	
	}//end function
	
	/**
	 * Gibt eine vordefinierte Testdatenbank zur�ck
	 * @return ge�ffnete Test Datenbank
	 * @throws NotesException
	 */
	public Database getDb() throws NotesException, ExceptionZZZ {
		String sServer = null;
		String sDBPath = null;
		if (testDb == null) {
			sServer = this.getServerCalling();
			sDBPath = this.getDBCallingPath();
			
			Session objSession = this.getSession();
			if(objSession==null) throw new NotesException(NOTESEXCEPTION_NO, "No handle on session.");
			
			System.out.println(ReflectCodeZZZ.getMethodCurrentName() + ", testDb-Objekt==null. Verwende alternativ diese DB: sServer='" + sServer + "' | sDBPath='" + sDBPath + "'");
			testDb = objSession.getDatabase(sServer, sDBPath);
		}
		if (testDb == null || !testDb.isOpen()) {
			String sLog = "Die Testdatenbank wurde nicht gefunden oder kann nicht ge�ffnet werden: " + sServer + "!!" + sDBPath;
			System.out.println("NotesContextProvider.getDb(): " + sLog);
			throw new NotesException(NOTESEXCEPTION_NO, sLog);
		}
		String sLog = "Folgende Testdatenbank wird verwendet: " + testDb.getServer() + "!!" + testDb.getFilePath();
		System.out.println(ReflectCodeZZZ.getMethodCurrentName() + ", " + sLog);
		return testDb;
	}

	/**
	 * Erzeugt ein Kontext Dokument, falls noch keines existiert.
	 * @return - not Null
	 * @throws NotesException
	 */
	public Document getDocumentContext () throws NotesException, ExceptionZZZ {
		if (docContext == null) {
			docContext = getDb().createDocument();
			docContext.replaceItemValue("Form", sFORM_CONTEXT_DUMMY);
			//TODO: Einen Parameter (flag) anbieten, dass dies auch speichern l�sst
			//docContext.save(true,false);
		}
		if (docContext == null) {
			throw new NotesException(NOTESEXCEPTION_NO,
			"Das Kontext Document konnte nicht erzeugt werden.");			
		}
		return docContext;
	}
	
	/**
	 * Gibt die Session zur�ck, �ber die der Server erreichbar ist.
	 * Merke: Bei einem echten Notesagenten wird die Session �ber den Konstruktor mitgegeben.
	 *             Bei Verwendung des Konstruktors mit dem "gefakten" Agentennamen, wird versucht eine lokale oder einen Internetsession herzustellen.
	 *             Dazu werden Informationen aus der Kernel-Konfigurationsdate verwendet.
	 *              
	 *              ;F�r eine ggf. aufzubauende Internet Session
	 *              HostConfiguration=
	 *              
	 *             ;rein Domino basierend. Ein leerer ServerCalling-Wert bedeutet, dass eine lokale notessession aufgebaut werden soll
ServerCalling=
PathDBCalling=db\fgl\JAZ-Kernel\Test\ZKernel_JavaTest_Application.nsf

;F�r lokale Notessession ist der userid-pfad in der notes.ini ausschlaggebend. �ber diesen Parameter wird der Pfad zur notes.ini Datei bestimmt
;Diese Notes.ini ist diejenige, die im Notes-executable Verzeichnis liegt.
;TODO: Falls dies leer ist, soll der Pfad zur Executable aus der Registry ausgelesen werden
NotesExePath=c:\lotus\notes7


;F�r lokale Notessessions ist der Pfad, welcher in der Notes.ini angegeben ist ausschlaggebend. Dieser wird in dem Fall gesetzt.
;Dann wird dieser Wert als KeyFileName in die Notes.ini gesetzt.
UserIDPath=c:\fglkernel\kernelcontext\flindhauer_fgl.id

;Falls keine lokale notessession verwendet wird, soll diese ID verwendet werden
Username=Fritz Lindhauer/fgl/DE

;Sowohl f�r lokale Notessession , als auch f�r alle anderen Vorgehensweisen, das ben�tigte Kennwort ist hier zu hinterlegen.
;TODO: Das Passwort sollte hier verschl�sselt abgelegt werden
Password=
		
	 * @return notnull
	 * @throws NotesException
	 */
	public Session getSession() throws ExceptionZZZ {
		//TODO GOON Es wird die Userrid des lokalen notes.ini - files genommen. Diese ist ggf. anzupassen !!!
		/*
		Session session = NotesFactory.createSession((String)null,(String)null,(String)null);
	    String sIDPathOrg = session.getEnvironmentString("KeyFileName", true); 				
	    session.recycle();
	    System.out.println(ReflectionZZZ.getMethodCurrentName() + "#" + sIDPathOrg);
		*/
		/*Der Versuch eine falsche ID zu verwenden bringt einen Fehler mit sich. Es muss daher:
		 * 1. der Pfad zur Notes.ini ausgelesen werden
		 * 2. ohne session.setEnvironmentString zu nutzen, sollte FileIniZZZ verwendet werden, um die entsprechende "WunschID" einzustellen.
		 * 3. Es sollte einen Destruktor geben, der dies wieder R�ckg�ngig macht.
		 * 
		 * 4. Das alles ist in einer Methode zu kapseln. Die zu testen ist. 
		 * 
		 */
		/* DENKANSTOSS
		 * 	Class objClassCur = this.getClass();
		Class objClassSup = objClassCur.getSuperclass();  //herausfinden, obj DebugAgentBase oder AgentBase zugrunde liegt.
		 */
		
		
		Session objReturn = null;
		try{
//		Merke: Bei einem echten Notes-Agenten soll die Session schon im Konstruktor mitgegeben werden. Bzw. bei einem Servlet kann eine Session auch generiert werden aus dem HttpRequest-Objekt.
		
		if (this.objSession == null) {			
			//FGL: Das ist eine Erweiterung von mir, basierend auf den verschiedenen Konstruktoren
			if(StringZZZ.isEmpty(this.getHost())){	
//				Fall: Erzeugen einer lokalen Notessession
				
				//DIe lokale Notessession wird immer ausgehend von dem KeyFilenameEintrag in der Notes.ini Datei eirzeugt.
				//a) Bei einem Server ist dieser vermutlich leer
				//b) Bei einem NotesClient oder einer Applikation, die den NotesClient bedingt, steht dort der Username drin, der zuletzt am Client angemeldet war.
				//    Dieser Username ist ggf. nicht im Kernel konfiguriert oder wird nicht gew�nscht.
				//    Daher kann man f�r den Agenten einen anderen Usernamen ausw�hlen (bzw. den Pfad zu einer anderen NotesID).
				//    Diese wird dann in die Notes.ini eingetragen und gut ist es. ...
				this.getLogObject().WriteLineDate(ReflectCodeZZZ.getMethodCurrentName() + "#Hoststring ist leer, muss versuchen eine lokale Notessession zu erzeugen");
				String sIDPath = this.getUserIdPath();				
				if(StringZZZ.isEmpty(sIDPath)==false){
//					0. Pr�fen, ob diese konfigurierte Datei �berhaupt exisitiert
					File objFile = new File(sIDPath);
					if(objFile.exists()==false){
						ExceptionZZZ ez = new ExceptionZZZ("Notes ID configured for local session does not exist: '" + sIDPath + "'", iERROR_CONFIGURATION_VALUE, this, ReflectCodeZZZ.getMethodCurrentName());
						throw ez;
					}
					if(objFile.isDirectory()){
						ExceptionZZZ ez = new ExceptionZZZ("Notes ID configured for local session is a directory: '" + sIDPath + "'", iERROR_CONFIGURATION_VALUE, this, ReflectCodeZZZ.getMethodCurrentName());
						throw ez;
					}					
					
					//1. Den alten UserID-Path speichern, um dies am Ende ggf. zu korregieren
					String stemp = this.readUserIdPath();
					if(sIDPath.equals(stemp)==false && StringZZZ.isEmpty(stemp)==false){
						this.setUserIdPathOld(stemp);
						this.setFlag("UseConfiguredKeyFileName", true);
						
//						2. den neuen UserIDPath schreiben
						this.writeUserIdPath(sIDPath);
						this.getLogObject().WriteLineDate(ReflectCodeZZZ.getMethodCurrentName() + "KeyFileName in notes.ini set to: '"+ sIDPath + "'");
						
					}else{
						this.setFlag("UseConfiguredKeyFileName", false); //Wenn der alte Wert nicht ersetzt wurde, braucht man auch nix zu schreiben
					}
				}
				
				//3. Erzeugen eines lokalen notesthreads
				//Merke: Ohne diesen statischen Thread gibt es die Fehlermeldung "java.lang.UnsatisfiedLinkError: NCreateSession"
				//Merke: Der NotesThread darf nat�rlich erst erzeugt werden, wenn die ini-datei der konfiguration entsprechend manipuliert worden ist.
				this.getLogObject().WriteLineDate(ReflectCodeZZZ.getMethodCurrentName() + "Muss einen statischen NotesThread erzeugen (NotesThread.sinitThread()).");
				NotesThread.sinitThread(); // start thread
				this.setFlag("UseStaticNotesThread", true);
				this.getLogObject().WriteLineDate(ReflectCodeZZZ.getMethodCurrentName() + "Statischer NotesThread erfolgreich erzeugt.");				
				
				//4. die eigentliche lokale Anmeldung
				if(StringZZZ.isEmpty(this.getPassword())){	
					this.getLogObject().WriteLineDate(ReflectCodeZZZ.getMethodCurrentName() + "Versuche Notessession OHNE Kennwort zu erzeugen.");
					this.objSession = NotesFactory.createSession();
				}else{
					this.getLogObject().WriteLineDate(ReflectCodeZZZ.getMethodCurrentName() + "Versuche Notessession MIT Kennwort zu erzeugen.");
					this.objSession = NotesFactory.createSession((String)null, (String)null, this.getPassword());
				}
			}else{
				//Fall: Erzeugen einer Internet NotesSession
				this.getLogObject().WriteLineDate(ReflectCodeZZZ.getMethodCurrentName() + "Host ist: '" + this.getHost() + "', erzeuge Notessession mit Benutzernamen: '" + this.getUsername() + "'  und Kennwort.");
				this.objSession = NotesFactory.createSession(this.getHost(), this.getUsername(), this.getPassword());
			}
			if (this.objSession == null) {
				this.getLogObject().WriteLineDate(ReflectCodeZZZ.getMethodCurrentName() + "Unable to provide a notessession");
				throw new NotesException(NOTESEXCEPTION_NO, "Unable to provide a notessession.");
			}						
		}//END if this.session == null
		}catch(NotesException ne){
			this.getLogObject().WriteLineDate(ReflectCodeZZZ.getMethodCurrentName() + "NotesException: " + ne.text);
			ExceptionZZZ ez = new ExceptionZZZ("NotesException: " + ne.text, iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;					
		}
		objReturn = this.objSession;
		return objReturn;
	}
	
	
	

	/**
	 * l�scht (!) das Kontext Document, falls vorhanden
	 * und recycled alle internen Domino Objekte
	 */
	public void recycle() {
		try{		
			if (testDb !=null) testDb = GC.recycle(testDb);
			if( objSession != null) GC.recycle(objSession);
			if( objAgentContext != null) GC.recycle(objAgentContext);
			
			if(this.getFlag("useStaticNotesThread")==true){
				NotesThread.stermThread();
				this.setFlag("useStaticNotesThread", false);
			}
			if(this.getFlag("UseConfiguredKeyFileName")==true){
				if(StringZZZ.isEmpty(this.sUserIdPathOld)==false){
					String sIDPathOld = this.getUserIdPathOld();
					this.writeUserIdPath(sIDPathOld);		
					this.getLogObject().WriteLineDate(ReflectCodeZZZ.getMethodCurrentName() + "KeyFileNeam in notes.ini set to: '"+ sIDPathOld + "'");
				}else{
					this.writeUserIdPath("");
				}
			}
		}catch(ExceptionZZZ ez){
			this.getLogObject().WriteLineDate(ReflectCodeZZZ.getMethodCurrentName() + "#" + ez.getDetailAllLast());
			ez.printStackTrace();
		}
		
		if (docContext != null) {
				//docContext.remove(true);
				//docContext = GC.recycle (docContext);
				
			
			//} catch (NotesException e) {
			//	e.printStackTrace();
			//}
		}
	}
	
	/**returns the Internet Hostname provided by the KernelZZZ-Module configuration file
	* @return String
	* 
	* lindhaueradmin; 17.09.2006 09:32:39
	 * @throws ExceptionZZZ 
	 */
	public String getHost() throws ExceptionZZZ{
		if(this.sHost==null){
			//TODO: Momentan ist es damit ein Pflichtparameter. Aber er sollte nicht Pflicht sein.
			//           Ergo: Das Kernelobjekt um eine Methode erweitern, die zus�tzlich zu der hier genannten Methode einen boolschen PAraemter hat, der besagt, ob ein Wert Pflicht ist oder nicht.
			//                   Wenn ein Wert nicht Pflicht ist, dann wird er (auch wenn er ganz fehlt) keine Exception werfen. 
			//                   Default sollten aber alle Werte Pflicht sein......
			this.sHost = this.getKernelObject().getParameterByProgramAlias(this.getModuleName(),this.getAgentName(), "HostConfiguration");
		}
		return this.sHost;
	}
	
	/**returns the DominoServerName provided by the KernelZZZ-Module configuration file
	* @return String
	* 
	* lindhaueradmin; 17.09.2006 09:32:39
	 * @throws ExceptionZZZ 
	 */
	public String getServerCalling() throws ExceptionZZZ{
		if(this.sServer==null){
			//this.sServer=this.getKernelObject().getParameterByProgramAlias("KernelNotes","ProgContext","ServerCalling");			
			this.sServer=this.getKernelObject().getParameterByProgramAlias(this.getModuleName(),this.getAgentName(),"ServerCalling");
		}
		return this.sServer;
	}
	/**returns the Path to the configuration Database provided by the KernelZZZ-Module configuration file
	* @return String
	* 
	* lindhaueradmin; 17.09.2006 09:32:39
	 * @throws ExceptionZZZ 
	 */
	public String getDBCallingPath() throws ExceptionZZZ{
		if(this.sDatabase==null){
			this.sDatabase = this.getKernelObject().getParameterByProgramAlias(this.getModuleName(),this.getAgentName(),"PathDBCalling");
		}
		return this.sDatabase;
	}
	
	
	/**
	 * @return String
	 *
	 * javadoc created by: 0823, 22.09.2006 - 08:33:02
	 * @throws ExceptionZZZ 
	 */
	public String getUsername() throws ExceptionZZZ{
		if(this.sUsername==null){
			//this.sUsername = this.getKernelObject().getParameterByProgramAlias("KernelNotes", "ProgContext", "Username");
			this.sUsername = this.getKernelObject().getParameterByProgramAlias(this.getModuleName(), this.getAgentName(), "Username");
		}
		return this.sUsername;
	}
	
	public String getPassword() throws ExceptionZZZ{
		if(this.sPassword==null){
			this.sPassword = this.getKernelObject().getParameterByProgramAlias(this.getModuleName(), this.getAgentName(), "Password");
			//TODO: Das Passwort sollte verschl�sselt hinterlegt werden und m�sste hier wieder entschl�sselt werden			
		}
		return this.sPassword;
	}
	
	/** Reads the UserID PAth which should be used for this context form the notes.ini-file
	* @return String
	* @throws ExceptionZZZ 
	* 
	* lindhaueradmin; 26.09.2006 09:06:46
	 */
	public String getUserIdPath() throws ExceptionZZZ{
		if(this.sUserIdPath==null){
			this.sUserIdPath = this.getKernelObject().getParameterByProgramAlias(this.getModuleName(), this.getAgentName(), "UserIDPath");			
		}
		return this.sUserIdPath;
	}
	
	/**The old 'user id path' is stored, for restoring it at the end.
	* @return String

	* lindhaueradmin; 26.09.2006 09:07:22
	 */
	public String getUserIdPathOld(){
		return this.sUserIdPathOld;
	}
	public void setUserIdPathOld(String sID){
		this.sUserIdPathOld = sID;
	}
	
	/**Reads the current used userid - path from the local notes.ini - file.
	 * Remember: For local notes-session this describes what user will be used.
	 * 
	 * @return String
	 *
	 * javadoc created by: 0823, 25.09.2006 - 15:13:23
	 * @throws ExceptionZZZ 
	 */
	public String readUserIdPath() throws ExceptionZZZ{
		String sReturn=".";  //Default das aktuelle Verzeichnis annehmen
		
		//1. den Pfad zur notes.ini-Datei auslesen
		String sPath = this.getPathNotesExe();
		if(StringZZZ.isEmpty(sPath)){
			ExceptionZZZ ez = new ExceptionZZZ("Unable to receive notes-executable-path", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
		
		//2. FileIni-Objekt: Aus der Notes.ini-Datei den entsprechenden Pfad auslesen
		FileIniZZZ objIni = new FileIniZZZ(objKernel, sPath, "notes.ini", (String[])null );
		sReturn = objIni.getPropertyValue("Notes", "KeyFileName");				
		return sReturn;
	}
	
	/**Writes the 'path of the current used userid' to the local notes.ini - file.
	 * Remember: For local notes-session this describes what user will be used.
	 * 
	 * @param sIdPath
	 * @return boolean, true if the idpath was changed. Remember this will not be changed if the new entry would equal the old one.
	 *
	 * javadoc created by: 0823, 25.09.2006 - 15:15:06
	 * @throws ExceptionZZZ 
	 */
	public boolean writeUserIdPath(String sIdPath) throws ExceptionZZZ{
		boolean bReturn = false;
		
		//		1. den Pfad zur notes.ini-Datei auslesen
		String sPath = this.getPathNotesExe();
		if(StringZZZ.isEmpty(sPath)){
			ExceptionZZZ ez = new ExceptionZZZ("Unable to receive notes-executable-path", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
		
		//2. FileIni-Objekt
		FileIniZZZ objIni = new FileIniZZZ(objKernel, sPath, "notes.ini", (String[]) null );
		String stemp = objIni.getPropertyValue("Notes", "KeyFileName");
		if(!sIdPath.equals(stemp) && !StringZZZ.isEmpty(stemp)){		
			bReturn = objIni.setPropertyValue("Notes", "KeyFileName", sIdPath, true); //true = save immidiateley				
			objIni = null;
		}
		return bReturn;
	}
	
	public String getPathNotesExe() throws ExceptionZZZ{		
		if(StringZZZ.isEmpty(this.sNotesIniPath)){
			this.sNotesIniPath = this.readNotesExePath();
		}			
		return this.sNotesIniPath;
	}
	public String readNotesExePath() throws ExceptionZZZ{
		//TODO: Als Alternative die Notes.ini aus der Registry auslesen
		//Oder nachsehen mit welchem Pfad die Dateiendung .nsf verbunden ist !!!
		 
		//Aber als einfache Variante den Pfad zur Notes.ini in der Konfigurationsdatei hinterlegen
		//this.sNotesIniPath = this.getKernelObject().getParameterByProgramAlias("KernelNotes", "ProgContext", "NotesExePath");
		this.sNotesIniPath = this.getKernelObject().getParameterByProgramAlias(this.getModuleName(), this.getAgentName(), "NotesExePath");
		return this.sNotesIniPath;
	}
	
	/** Gibt einen Pfad zur�ck, der konfiguriert werden kann. In diesem so konfigurierten Verzeichnis kann alles m�glich liegen. Z.B. Import/Export Daten, etc.
	* @return
	* @throws ExceptionZZZ
	* 
	* lindhauer; 04.01.2008 15:18:11
	 */
	public String getPathDirectoryDefault() throws ExceptionZZZ{
		if(StringZZZ.isEmpty(this.sPathDirectoryDefault)){
			this.sPathDirectoryDefault = this.readPathDirectoryDefault();
		}
		return this.sPathDirectoryDefault;
	}
	
	/** Gibt den default-definierten Pfad aus. Falls der nicht existiert, dann wird das Verzeichnis angelegt.
	* @throws ExceptionZZZ
	* 
	* lindhauer; 04.01.2008 15:59:20
	 */
	public String readPathDirectoryDefault() throws ExceptionZZZ{
		this.sPathDirectoryDefault = this.getKernelObject().getParameterByProgramAlias(this.getModuleName(), this.getAgentName(), "DirectoryDefaultPath");
		if(StringZZZ.isEmpty(this.sPathDirectoryDefault)){
			this.sPathDirectoryDefault = ".";
		}else{		
			if (! FileEasyZZZ.exists(this.sPathDirectoryDefault) ){
				FileEasyZZZ.makeDirectory(this.sPathDirectoryDefault);
			}
		}
		
		return this.sPathDirectoryDefault;
	}
	
}//END Class