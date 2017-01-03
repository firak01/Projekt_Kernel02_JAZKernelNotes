package basic.zNotes.basic;
/*
 * Die Sourcecodes, die diesem Buch als Beispiele beiliegen, sind
 * Copyright (c) 2006 - Thomas Ekert. Alle Rechte vorbehalten.
 * 
 * Trotz sorgfältiger Kontrolle sind Fehler in Softwareprodukten nie vollständig auszuschließen.
 * Die Sourcodes werden in Ihrem Originalzustand ausgeliefert.
 * Ansprüche auf Anpassung, Weiterentwicklung, Fehlerbehebung, Support
 * oder sonstige wie auch immer gearteten Leistungen oder Haftung sind ausgeschlossen.
 * Sie dürfen kommerziell genutzt, weiterverarbeitet oder weitervertrieben werden.
 * Voraussetzung hierfür ist, dass für jeden beteiligten Entwickler, jeweils mindestens
 * ein Exemplar dieses Buches in seiner aktuellen Version als gekauftes Exemplar vorliegt.
 */
import java.util.Vector; 



import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zKernel.KernelUseObjectZZZ;
import basic.zKernel.KernelZZZ;
import basic.zNotes.kernel.NotesContextProviderZZZ;
import lotus.domino.*;
/**
 * 
 * @author Thomas Ekert
 *
 */ 
public class DJAgentContext extends KernelUseObjectZZZ implements AgentContext {
	private Session objSession = null;
	private AgentContext objAgentContextReal = null;
	private AgentZZZ objAgent = null;
	
	private Document docContext = null;
	private Database currentDB = null;
	private String effectiveUserName = null;

	public DJAgentContext(KernelZZZ objKernel, String user, Session objSession, Database db, Document doc, String sAgentNameFaked) throws ExceptionZZZ {
		super(objKernel);
		try{
		this.objSession = objSession;
		if(db == null){
			ExceptionZZZ ez = new ExceptionZZZ("Database object", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}else if(db.isOpen()==false){
			String sLog = "Database object: Unable to open database. " + db.getServer() + "!!" + db.getFilePath() + ". Access ?";
			System.out.println(sLog);
			ExceptionZZZ ez = new ExceptionZZZ(sLog, iERROR_PARAMETER_VALUE, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
		currentDB = db;
		
		if(StringZZZ.isEmpty(sAgentNameFaked)){
			ExceptionZZZ ez = new ExceptionZZZ("Agent name", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
		objAgent = new AgentZZZ(objKernel, sAgentNameFaked);
		
		//?????
		effectiveUserName = user;		
		docContext = doc;
		}catch(NotesException ne){
			ExceptionZZZ ez = new ExceptionZZZ(ne.text, iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
	}
	public DJAgentContext(KernelZZZ objKernel, String user, Session objSession, Database db, Document doc, Agent agent) throws ExceptionZZZ {
		super(objKernel);
		this.objSession = objSession;
		docContext = doc;
		currentDB = db;
		effectiveUserName = user;
		objAgent = new AgentZZZ(objKernel, agent);
	}
	public DJAgentContext(KernelZZZ objKernel, Session objSession) throws ExceptionZZZ{
		super(objKernel);
		try{		
		this.objSession = objSession;
		this.objAgentContextReal = objSession.getAgentContext();
		if(this.objAgentContextReal==null){
			ExceptionZZZ ez = new ExceptionZZZ("Real agent context", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}

		Agent objAgent = objAgentContextReal.getCurrentAgent();
		this.objAgent = new AgentZZZ(objKernel, objAgent);
		}catch(NotesException ne){
			ExceptionZZZ ez = new ExceptionZZZ(ne.text, iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
	}
	public String getEffectiveUserName() throws NotesException {
		if(this.objAgentContextReal==null){
			return effectiveUserName;	
		}else{
			return this.objAgentContextReal.getEffectiveUserName();
		}		
	}
	public Database getCurrentDatabase() throws NotesException {
		if(this.objAgentContextReal==null){
			return currentDB;
		}else{
			return this.objAgentContextReal.getCurrentDatabase();
		}
		
	}
	public Document getDocumentContext() throws NotesException {
		if(this.objAgentContextReal==null){
			return docContext;
		}else{
			return this.objAgentContextReal.getDocumentContext();
		}
		
	}

	public void updateProcessedDoc(Document arg0) throws NotesException {
		if(this.objAgentContextReal==null){
			throw new NotesException(999, "not yet implemented.");
		}else{
			this.objAgentContextReal.updateProcessedDoc(arg0);
		}		
	}
	public DocumentCollection unprocessedFTSearch(String arg0, int arg1)throws NotesException {
		if(this.objAgentContextReal==null){
			throw new NotesException(999, "not yet implemented.");
		}else{
			return this.objAgentContextReal.unprocessedFTSearch(arg0, arg1);
		}
	}
	public DocumentCollection unprocessedFTSearch(String arg0, int arg1,	int arg2, int arg3) throws NotesException {
		if(this.objAgentContextReal==null){
			throw new NotesException(999, "not yet implemented.");
		}else{
			return this.objAgentContextReal.unprocessedFTSearch(arg0, arg1, arg2, arg3);
		}
	}
	public DocumentCollection unprocessedSearch(String arg0, DateTime arg1,
			int arg2) throws NotesException {
		if(this.objAgentContextReal==null){
			throw new NotesException(999, "not yet implemented.");
		}else{
			return this.objAgentContextReal.unprocessedSearch(arg0, arg1, arg2);
		}
	}
	public int getLastExitStatus() throws NotesException {
		if(this.objAgentContextReal==null){
			throw new NotesException(999, "not yet implemented.");
		}else{
			return this.objAgentContextReal.getLastExitStatus();
		}
	}
	public DateTime getLastRun() throws NotesException {
		if(this.objAgentContextReal==null){
			throw new NotesException(999, "not yet implemented.");
		}else{
			return this.objAgentContextReal.getLastRun();
		}
		
	}
	public Document getSavedData() throws NotesException {
		if(this.objAgentContextReal==null){
			throw new NotesException(999, "not yet implemented.");
		}else{
			return this.objAgentContextReal.getSavedData();
		}
		
	}
	public DocumentCollection getUnprocessedDocuments() throws NotesException {
		if(this.objAgentContextReal==null){
			throw new NotesException(999, "not yet implemented.");
		}else{
			return this.objAgentContextReal.getUnprocessedDocuments();
		}
		
	}
	public void recycle(Vector arg0) throws NotesException {
		if(this.objAgentContextReal==null){
			throw new NotesException(999, "not yet implemented.");
		}else{
			this.objAgentContextReal.recycle(arg0);	
		}
		
	}
	public DocumentCollection unprocessedFTSearchRange(String arg0, int arg1,
			int arg2) throws NotesException {
		if(this.objAgentContextReal==null){
			throw new NotesException(999, "not yet implemented.");
		}else{
			return this.objAgentContextReal.unprocessedFTSearchRange(arg0, arg1, arg2);
		}
		
	}
	public DocumentCollection unprocessedFTSearchRange(String arg0, int arg1,
			int arg2, int arg3, int arg4) throws NotesException {
		if(this.objAgentContextReal==null){
			throw new NotesException(999, "not yet implemented.");	
		}else{
			return this.objAgentContextReal.unprocessedFTSearch(arg0, arg1);
		}
		
	}
	public void recycle() throws NotesException {
		if(this.docContext!=null) GC.recycle (docContext);
		if(this.currentDB!=null) GC.recycle(currentDB);
		if(this.objAgent!=null) {
			if(this.objAgent.getAgent()!=null)	GC.recycle(this.objAgent.getAgent());
		}
	}
	public Agent getCurrentAgent() throws NotesException {
		if(this.objAgent!=null){
			return this.objAgent.getAgent();
		}else{
			if(this.objAgentContextReal==null){
				throw new NotesException(999, "not yet implemented.");	
			}else{
				return this.objAgentContextReal.getCurrentAgent();
			}
		}
	}
		
		public AgentZZZ getKernelAgent() throws NotesException{
			if(this.objAgent==null){
				throw new NotesException(999, "There is no AgentZZZ-Object available.");	
			}
			return this.objAgent;
		}
	}//END Class