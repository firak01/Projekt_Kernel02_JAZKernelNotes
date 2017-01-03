package basic.zNotes.use.util;

import lotus.domino.Document;
import lotus.domino.Item;
import lotus.domino.NotesException;
import lotus.domino.Session;
import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zNotes.kernel.KernelNotesUseObjectZZZ;
import custom.zNotes.kernel.KernelNotesZZZ;

/**Erstellt eine fortlaufende Nummer:
 *   - Diese Nummer basiert auf einem Eintrag in einem "ApplicationStore"-Dokument (siehe objKernel.getApplicationStore).
 *   - Sie wird um 1 erhöht und in ein Feld in einem Notesdokument eingetragen.
 * @author lindhaueradmin
 *
 */
public class KernelNumberGeneratorZZZ extends KernelNotesUseObjectZZZ{
	Document docApplicationStore = null;
	String sFieldnameTargetUsed = null;              //Der Feldname im zu verarbeitenden Notesdokument, der die Nummer aufnehmen soll
	String sFieldnameSourceUsed = null;              //Der Feldname im ApplicationStore Dokument
	
	public KernelNumberGeneratorZZZ(KernelNotesZZZ objKernelNotes) {
		super(objKernelNotes);
	}
	
	/** Holt die aktuelle Nummer, addiert 1 drauf
	 *   und speichert diese neue Nummer im ApplicationStore Dokument ab.
	* @param sFieldnameIn
	* @return
	* @throws ExceptionZZZ
	* 
	* lindhaueradmin; 12.12.2006 08:30:43
	 */
	public String generateNumberNew(String sFieldnameIn) throws ExceptionZZZ{
		String objReturn = null;
		main:{
			String sNumber = this.readNumberCurrent(sFieldnameIn);
			long lNumber;
			if(StringZZZ.isEmpty(sNumber)){
				lNumber = 1;
				objReturn = "1";				
			}else{
				lNumber = Long.parseLong(sNumber);	
				lNumber++;
				objReturn = Long.toString(lNumber);				
			}
			
//			Diese Nummer nun in das Dokument speichern
			Session session = this.getKernelNotesObject().getSession();
			Document docStore = this.getApplicationStoreUsed();
			if(docStore==null){
				ExceptionZZZ ez = new ExceptionZZZ("Unable to receive ApplicationStoreUsed", iERROR_PROPERTY_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
			this.getKernelNotesLogObject().writeLog("Neuer Zähler für '" +  sFieldnameIn +"' auf '" + objReturn + "' gesetzt.");
			this.getKernelNotesLogObject().writeLog("Methode wurde aufgerufen von: '" + ReflectCodeZZZ.getCaller(0)+ "'");
			this.getKernelNotesLogObject().writeLog("Methode wurde aufgerufen von: '" + ReflectCodeZZZ.getCaller(1) + "'");
			String sApplicationAlias = this.getKernelNotesObject().getApplicationKeyCurrent();
			KernelNumberGeneratorZZZ.storeNumber(session, docStore, sFieldnameIn, sApplicationAlias, lNumber, true);
		}//ENd main
		return objReturn;
	}
	
	public String readNumberCurrent(String sFieldnameIn) throws ExceptionZZZ{
		String objReturn = null;
		main:{
			try{
				if(StringZZZ.isEmpty(sFieldnameIn)){
					ExceptionZZZ ez = new ExceptionZZZ("Fieldname", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
					throw ez;
				}
								
				KernelNotesZZZ objKernel = this.getKernelNotesObject();
				Document doc = this.getApplicationStoreUsed();
				//1.Versuch: Das item entsprechend des genauen Feldnamens finden
				String sFieldname = this.getFieldnameSourceUsed(sFieldnameIn);
				Item item = doc.getFirstItem(sFieldname);
				if(item==null){					
						//!!! Keine Fehlermeldung ausgeben. Statt dessen wird angenommen, es sei eine neue Nummer. Ergo ist aktuell Null.
						objReturn = "0";
						break main;
				}
				objReturn = item.getValueString();
			}catch(NotesException ne){
				ExceptionZZZ ez = new ExceptionZZZ(ne.text, iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
		}//END main:
		return objReturn;
	}
	
	public String getFieldnameTargetUsed(String sFieldname){
		String sReturn = null;
		main:{
			if(this.sFieldnameTargetUsed!=null){
				sReturn = this.sFieldnameTargetUsed;
			}else{
				sReturn = KernelNumberGeneratorZZZ.computeFieldnameTargetUsed(sFieldname, this.getKernelNotesObject().getApplicationKeyCurrent());
				this.setFieldnameTargetUsed(sReturn);
			}
		}//END Main;
		return sReturn;
	}
	public void setFieldnameTargetUsed(String sFieldname){
		this.sFieldnameTargetUsed = sFieldname;
	}
	
	public String getFieldnameSourceUsed(String sFieldname){
		String sReturn = null;
		main:{
			if(this.sFieldnameSourceUsed!=null){
				sReturn = this.sFieldnameSourceUsed;
			}else{
				sReturn = KernelNumberGeneratorZZZ.computeFieldnameSourceUsed(sFieldname, this.getKernelNotesObject().getApplicationKeyCurrent());
				this.setFieldnameSourceUsed(sReturn);
			}
		}//END Main;
		return sReturn;
	}
	public void setFieldnameSourceUsed(String sFieldname){
		this.sFieldnameSourceUsed = sFieldname;
	}
	
	
	
	
	
	public static String computeFieldnameTargetUsed(String sFieldname, String sApplicationKey){
		String sReturn = null;
		if(StringZZZ.isEmpty(sApplicationKey)){
			String stemp = null;
//			Analyse des Feldnamens, wenn dort schun "Number" am Anfang vorkommt, nichts tun.
			if(sFieldname.toLowerCase().startsWith("number")){
				stemp = sFieldname;
			}else{
				stemp = "Number" + sFieldname;
			}			
			sReturn = stemp;
		}else{		
			String stemp = null;
			//Analyse des Feldnamens, wenn dort schun "Number" am Anfang vorkommt, nichts tun.
			if(sFieldname.toLowerCase().startsWith("number")){
				stemp = sFieldname;
			}else{
				stemp = "Number" + sFieldname;
			}			
			
			//Analyse des Feldnamens, wenn dort schon der ApplicationAlias am Ende vorkommt, nichts tun
			if(sFieldname.toLowerCase().endsWith(sApplicationKey.toLowerCase())){
				sReturn = stemp;
			}else{
				sReturn = stemp + sApplicationKey;
			}
		}
		return sReturn;
	}
	
	public static String computeFieldnameSourceUsed(String sFieldname, String sApplicationKey){
		String sReturn = null;
		if(StringZZZ.isEmpty(sApplicationKey)){
			String stemp = null;
//			Analyse des Feldnamens, wenn dort schun "Number" am Anfang vorkommt, nichts tun.
			if(sFieldname.toLowerCase().startsWith("number")){
				stemp = sFieldname;
			}else{
				stemp = "Number" + sFieldname;
			}			
			sReturn = stemp;
		}else{		
			String stemp = null;
			//Analyse des Feldnamens, wenn dort schun "Number" am Anfang vorkommt, nichts tun.
			if(sFieldname.toLowerCase().startsWith("number")){
				stemp = sFieldname;
			}else{
				stemp = "Number" + sFieldname;
			}			
			
			//Analyse des Feldnamens, wenn dort schon der ApplicationAlias am Ende vorkommt, nichts tun
			if(sFieldname.toLowerCase().endsWith(sApplicationKey.toLowerCase())){
				sReturn = stemp;
			}else{
				sReturn = stemp + sApplicationKey;
			}
		}
		return sReturn;
	}
	
	public static boolean storeNumber(Session session, Document docApplicationStore, String sFieldname, String sApplicationAlias, long lNumber2Store, boolean bFlagStoreImmediate) throws ExceptionZZZ{
		boolean bReturn = false;
		main:{
			try{
				if(docApplicationStore==null){
					ExceptionZZZ ez = new ExceptionZZZ("ApplicationStore Document", iERROR_PARAMETER_MISSING, "KernelNumberGeneratorZZZ", ReflectCodeZZZ.getMethodCurrentName());
					throw ez;					
				}
				if(StringZZZ.isEmpty(sFieldname)){
					ExceptionZZZ ez = new ExceptionZZZ("Fieldname", iERROR_PARAMETER_MISSING, "KernelNumberGeneratorZZZ", ReflectCodeZZZ.getMethodCurrentName());
					throw ez;		
				}
								
				String sSourceName = KernelNumberGeneratorZZZ.computeFieldnameSourceUsed(sFieldname, sApplicationAlias);
				String sSourceValue = Long.toString(lNumber2Store);
				docApplicationStore.replaceItemValue(sSourceName, sSourceValue);
				if(bFlagStoreImmediate==true){
					bReturn = docApplicationStore.save();
				}else{
					bReturn = true;
				}
				
			}catch(NotesException ne){
				ExceptionZZZ ez = new ExceptionZZZ(ne.text, iERROR_RUNTIME, "KernelNumberGeneratorZZZ", ReflectCodeZZZ.getMethodCurrentName());
				throw ez;	
			}
		}//end main:
		return bReturn;
	}
	
	
	//#### Getter/Setter
	public Document getApplicationStoreUsed() throws ExceptionZZZ{
		if(this.docApplicationStore==null){
			this.docApplicationStore = this.getKernelNotesObject().getApplicationStore();
		}
		return this.docApplicationStore;
	}
	public void setApplicationStoreUsed(Document docApplicationStore){
		this.docApplicationStore = docApplicationStore;
	}

}
