package basic.zNotes.kernel;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Log;
import lotus.domino.NotesException;
import lotus.domino.Session;
import basic.javagently.Stream;
import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zKernel.KernelUseObjectZZZ;
import basic.zKernel.KernelZZZ;
import basic.zNotes.basic.AgentZZZ;
import custom.zNotes.kernel.KernelNotesZZZ;


/*
* Author 0823
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class KernelKernelNotesLogZZZ extends KernelUseObjectZZZ{ //ObjectZZZ{
	
	private Log objLogNotes = null;  //das Notes-Klassen-Objekt
	private KernelNotesZZZ objKernelNotes = null;
	
	private Stream objStream = null;                    //für die Ausgabe in eine Textdatei
	private KernelZZZ objKernel = null;
	private Database dbLog = null; 

	private String sAgentCallingName = null;
	private String sAgentCallingAlias = null;
	private int iLogLevel=-1;
	private String sLogMessageLast = null;
	private int iLogMessageLastCounter = 0; //der "mute"-counter
	private String sLogEntryLast = null;
	
	//+++++++++++++++++++++++++++++++++++++
	//Accessor - Function
	/*
	public AgentContext getAgentCallingContext(){
		return this.agtCallingContext;
	}
	*/
	
/**
 * @param sLogComment
 * @return
 * @throws ExceptionZZZ
 */
public boolean writeLog(String sLogComment) throws ExceptionZZZ{
	return writeLog_(sLogComment,null, null, 1);  //wg. der 1 wird dieser Eintrag als LogLevel 1 Eintrag erzeugt.
}

/**
 * @param sLogComment
 * @param iLogLevel
 * @return
 * @throws ExceptionZZZ
 */
public boolean writeLog(String sLogComment, int iLogLevel) throws ExceptionZZZ{
		return writeLog_(sLogComment, null, null,iLogLevel);
}

public boolean writeLog(String sLogComment, Object obj, String sMethod, int iLogLevel) throws ExceptionZZZ{
	return writeLog_(sLogComment, obj, sMethod, iLogLevel);	
}


private boolean writeLog_(String sLogComment, Object obj, String sMethodIn, int iLogLevel) throws ExceptionZZZ{
	boolean bFunction = false;
	String stemp = null;
	main:
	{
		String sMethod;
		
		paramcheck:{
			//proof the current log-level
			int iLogLevelCurrent = this.refreshLogLevelGlobal();
			if(iLogLevelCurrent < iLogLevel) break main;				
		}
		
		
	try {
		//Zunächst muss ggf. das NotesLog-Object erzeugt werden
		Log objLogNotes = this.refreshLogNotesObject();
		
		//TODO GOON die Meldung mit der vorherigen Meldung vergleichen. Ähnlich eMeldungen zählen, und ggf. das Schreiben verhindern.
		//Dann würde der Returnwert auf false bleiben.
		if(sLogComment.equals(this.sLogMessageLast)){			
			if(this.iLogMessageLastCounter + 1 >  this.getMuteCounterLimit()) 	break main;
			this.iLogMessageLastCounter++;
		}else{
			this.iLogMessageLastCounter = 1;
			
			//Nun speichern der neuen  "reinen" Meldung
			this.sLogMessageLast = sLogComment;
		}
		
		
		
		if(obj==null & sMethodIn == null){
			stemp = new String(this.generateLogString(sLogComment));		
			if(this.iLogMessageLastCounter >=  this.getMuteCounterLimit()){
				stemp = stemp + "(* further repeating entries will be suppressed by mute counter *)";
			}			
			this.sLogEntryLast = stemp;
			if(iLogLevel >= 1){
				objLogNotes.logAction(stemp);			
			}else{
				objLogNotes.logError(0,stemp);
			}										
		}else if(obj !=null & sMethodIn == null){
			stemp = new String(this.generateLogString(sLogComment, obj));	
			if(this.iLogMessageLastCounter >=  this.getMuteCounterLimit()){
				stemp = stemp + "(* further repeating entries will be suppressed by mute counter *)";
			}			
			this.sLogEntryLast = stemp;
			if(iLogLevel >= 1){
				objLogNotes.logAction(stemp);			
			}else{
				objLogNotes.logError(0,stemp);
			}
		
		}else if(obj != null & sMethodIn != null){
			if(sMethodIn.equals("")){
					sMethod = "no function spezified";
			}else{
					sMethod = sMethodIn;
			}
			stemp = new String(this.generateLogString(sLogComment, obj, sMethod));
			if(this.iLogMessageLastCounter >=  this.getMuteCounterLimit()){
				stemp = stemp + "(* further repeating entries will be suppressed by mute counter *)";
			}			
			this.sLogEntryLast = stemp;
			if(iLogLevel >= 1){
				objLogNotes.logAction(stemp);			
			}else{
				objLogNotes.logError(0,stemp);
			}		
		}
		
		//Returnwert der MEthode setzen, nur wenn das Schreiben nicht unterdrückt worden ist.
		bFunction = true;
		} catch (NotesException ne) {
			ExceptionZZZ ez = new ExceptionZZZ(ne.text, iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
		
	}//End main		
		return bFunction;
}



/**
 * @param sLogComment: the comment wich should be written to the log
 * @return Classname + Function + Comment
 * @throws ExceptionZZZ
 */
public String generateLogString(String sLogComment) throws ExceptionZZZ{
	//String sFunction = new String("");
	main:{
		ExceptionZZZ ez = new ExceptionZZZ("this function has to be overwritten by a function of the include package",iERROR_ZFRAME_METHOD, this,ReflectCodeZZZ.getMethodCurrentName()); 
		throw ez;
	}
	//return sFunction;
}

/**
 * @param sLogComment
 * @param obj
 * @return
 * @throws ExceptionZZZ
 */
public String generateLogString(String sLogComment, Object obj) throws ExceptionZZZ{
	main:{
	ExceptionZZZ ez = new ExceptionZZZ("this function has to be overwritten by a function of the include package", iERROR_ZFRAME_METHOD, this, ReflectCodeZZZ.getMethodCurrentName()); 
	throw ez;
	} 
	//return sFunction;	
}

/**
 * @param sLogComment
 * @param obj
 * @param e
 * @return
 * @throws ExceptionZZZ
 */
public String generateLogString(String sLogComment, Object obj, String sMethodIn) throws ExceptionZZZ{
	main:{	
	ExceptionZZZ ez = new ExceptionZZZ("this function has to be overwritten by a function of the include package", iERROR_ZFRAME_METHOD, this, ReflectCodeZZZ.getMethodCurrentName()); 
	throw ez;
		} 
		//return sFunction;	
}

public int getLogLevelGlobal() throws ExceptionZZZ{
	int iReturn = 0;
	if(this.iLogLevel== -1){
		this.iLogLevel = this.refreshLogLevelGlobal();
		iReturn = this.iLogLevel;
	}	
	return iReturn;
}

/**
 * @return the current log-level, which is configured
 * @throws ExceptionZZZ 
 */
public int refreshLogLevelGlobal() throws ExceptionZZZ{
	int iFunction = 0;
	main:{
			
		try {
			KernelNotesZZZ objKernel = this.getKernelNotesObject();
			if(objKernel==null) break main;
			
			Document docProfile = objKernel.ProfileApplicationRefresh();
			if(docProfile==null) break main;	
			
			String stemp = new String(docProfile.getItemValueString("LogLevelZZZ"));
			this.iLogLevel = Integer.parseInt(stemp);			
			iFunction = this.iLogLevel;
						
		} catch (NotesException ne) {
			ExceptionZZZ e = new ExceptionZZZ(ne.getMessage(),iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
			throw e;		
		}
	}	
	return iFunction;		
}

/** Returns the last written message string.
* @return String
* 
* lindhaueradmin; 25.09.2006 09:41:46
 */
public String getLogMessageLast(){
	return this.sLogMessageLast;
}

/** Returns the text of the last written message string plus additional informations written to the log (e.g. object-classname, etc.)
* @return String
* 
* lindhaueradmin; 25.09.2006 09:42:41
 */
public String getLogEntryLast(){
	return this.sLogEntryLast;
}
	
	/**Constructor
	 * internaly using the constructor with parameter flagControl = "INIT")
	 */
	public KernelKernelNotesLogZZZ() throws ExceptionZZZ{		
		this(null,"INIT");
	}
	
	//Constructor
	public KernelKernelNotesLogZZZ(KernelNotesZZZ objKernel, String[] saFlagControlIn) throws ExceptionZZZ{
		KernelKernelNotesLogNew_(objKernel, saFlagControlIn);
	}
	
	
	//Constructor
	public KernelKernelNotesLogZZZ(KernelNotesZZZ objKernel, String sFlagControlIn) throws ExceptionZZZ{
		String[] saFlagControl = {sFlagControlIn};
		KernelKernelNotesLogNew_(objKernel, saFlagControl);
	}
	
	//Basic for all Constructors
	private void KernelKernelNotesLogNew_(KernelNotesZZZ objKernelNotes, String[] saFlagControlIn) throws ExceptionZZZ{		
		main:{		
				//setzen der übergebenen Flags	
				if(saFlagControlIn != null){
					for(int iCount = 0;iCount<=saFlagControlIn.length-1;iCount++){
						String stemp = saFlagControlIn[iCount];
						if(!StringZZZ.isEmpty(stemp)){
							boolean btemp = setFlag(stemp, true);
							if(btemp==false){
								ExceptionZZZ ez = new ExceptionZZZ("the flag '" + stemp + "' is not available.", iERROR_FLAG_UNAVAILABLE, this, ReflectCodeZZZ.getMethodCurrentName()); 
								throw ez;		 
							}
						}
					}
					if(this.getFlag("INIT")==true)	break main; 
				}
				
			
				if(objKernelNotes == null){
					ExceptionZZZ ez = new ExceptionZZZ("KernelNotesObject", iERROR_PARAMETER_MISSING,  this, ReflectCodeZZZ.getMethodCurrentName()); 					
					throw ez;
				}else{
					if(objKernelNotes.getFlag("init")==true){
						ExceptionZZZ ez = new ExceptionZZZ("KernelNotesObject only initialized", iERROR_PARAMETER_VALUE,  this, ReflectCodeZZZ.getMethodCurrentName()); 
						throw ez;
					}						
				}			
			this.setKernelNotesObject(objKernelNotes);	
		} //end main	
	}


	
	public KernelNotesZZZ getKernelNotesObject(){
		return this.objKernelNotes;
	}
	
	public void setKernelNotesObject(KernelNotesZZZ objKernelNotesIn){
		this.objKernelNotes = objKernelNotesIn;
	}

/**
 * 
 * @return the NotesLog-Object used for generating documents in the Notes-Log-Database
 * @throws ExceptionZZZ 
 */
public Log getLogNotesObject() throws ExceptionZZZ{
	Log objReturn = null;
	if(this.objLogNotes==null){
		this.objLogNotes = this.refreshLogNotesObject();
	}
	objReturn = this.objLogNotes;
	return objReturn;
}
public Log refreshLogNotesObject() throws ExceptionZZZ{
	Log objReturn = null;
	try{	
		main:{
			String sAgentName = this.getAgentCallingName();
			if(this.objLogNotes!=null){				
				if(this.objLogNotes.getProgramName().equals(sAgentName)){
					break main;			
				}else{
					this.objLogNotes.recycle();					
				}
			}//this.objLogNotes!=null		
			
			Session objSession = this.getKernelNotesObject().getSession();
		
			//Datenbank für die Protokollierung
			Database dbLog = this.getDBLog();							
			String sServer = dbLog.getServer();
			String sPath = dbLog.getFilePath();    
						
			this.objLogNotes = objSession.createLog(sAgentName);
			this.objLogNotes.openNotesLog(sServer, sPath);
		}//END main
	}catch(NotesException ne){
		ExceptionZZZ ez = new ExceptionZZZ(ne.text, iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
		throw ez;
	}	
	objReturn = this.objLogNotes;
	return objReturn;
}

public Database getDBLog() throws ExceptionZZZ{
	Database objReturn=null;
	if(this.dbLog==null){
		this.dbLog = this.getKernelNotesObject().getDBLogCurrent();
		if(this.dbLog==null){
			ExceptionZZZ ez = new ExceptionZZZ("objKernel.DBLogCurrent not configured", iERROR_PROPERTY_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;		
		}
	}
	objReturn = this.dbLog;
	return objReturn;
}
public void setDBLog(Database dbLog2set) throws ExceptionZZZ{
	try{
		if(dbLog2set==null || dbLog2set.isOpen()==false){
			ExceptionZZZ ez = new ExceptionZZZ("Database empty or no access to database", iERROR_PARAMETER_VALUE, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
		this.dbLog = dbLog2set;
	}catch(NotesException ne){
		ExceptionZZZ ez = new ExceptionZZZ(ne.text, iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
		throw ez;
	}
}

public String getAgentCallingName() throws ExceptionZZZ{
	String sReturn = null;
	try{
		if(StringZZZ.isEmpty(this.sAgentCallingName)){
			AgentZZZ objAgent = this.getKernelNotesObject().getAgentCalling();
			if(objAgent==null){
				ExceptionZZZ ez = new ExceptionZZZ("objKernel.getAgentCalling() not available", iERROR_PROPERTY_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
			this.sAgentCallingName  = objAgent.getAgentName();					
		}
	}catch(NotesException ne){
		ExceptionZZZ ez = new ExceptionZZZ(ne.text, iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
		throw ez;
	}
	sReturn = this.sAgentCallingName;
	return sReturn;
}

/**The mute counter is the number of entries, which have the same value.
 * This method returns the number, when a log entry is suppressed, because it is the same as before.
 * The goal of this functionality is to avoid an overflowing log database.
 * 
 * @return int
 *
 * javadoc created by: 0823, 25.09.2006 - 11:04:50
 */
public int getMuteCounterLimit(){
	return 3;
}

public int getMuteCounter(){
	return this.iLogMessageLastCounter;
}




}//end class
