package basic.zNotes.document;


import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ObjectZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import lotus.domino.Document;
import lotus.domino.Item;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.DocumentCollection;
import lotus.domino.local.DateTime;

/** Klasse bietet diveser Methoden zur Behandlung/Analyse von Notes-Dokumenten
 * @author lindhauer
 *
 */
public class DocumentZZZ  extends ObjectZZZ {
	
	/*Die maximal erlaubte länge an zeichen für einen Feldnamen. Achtung: Es muss nicht immer 32 Zeichen = 32 Byte gelten.
	 * 
	 */
	public static final int iFIELDNAME_LENGTH=32;
	
	/**Entfernt alle instancen eines Items. 
	 *  Merke: Mit Document.appendItemValue() werden normalerweise zusätzliche Items angehängt. Und nicht Mehrfachwerte erzeugt.
	 *  
	* @param session
	* @param doc
	* @param sItemName2Remove
	* @return true, falls ein Item vorher schon drin war und nun entfernt worden ist
	* @throws ExceptionZZZ
	* 
	* lindhauer; 10.02.2008 10:33:58
	 */
	public static boolean itemInstanceAllRemove(Session session, Document doc, String sItemName2Remove) throws ExceptionZZZ{
		boolean bReturn = false;
		main:{
			try{
				if(doc == null){
						ExceptionZZZ ez = new ExceptionZZZ("Document provided", iERROR_PARAMETER_MISSING, DocumentZZZ.class.getName(), ReflectCodeZZZ.getMethodCurrentName());
						throw ez;
					}
					if(StringZZZ.isEmpty(sItemName2Remove)){
						ExceptionZZZ ez = new ExceptionZZZ("No Itemname provided", iERROR_PARAMETER_MISSING, DocumentZZZ.class.getName(), ReflectCodeZZZ.getMethodCurrentName());
						throw ez;
					}
					
					//##########################################
					if(doc.hasItem(sItemName2Remove)){
						bReturn = true;
						while(doc.hasItem(sItemName2Remove)){
							doc.removeItem(sItemName2Remove);
						}
					}
			}catch(NotesException ne){
				ExceptionZZZ ez = new ExceptionZZZ(ne.text, iERROR_RUNTIME, DocumentZZZ.class.getName(), ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
		}//end main:
		return bReturn;
	}
	
	/** Prüft, ob die übergebene DocId der Form entspricht:
	 * 
	 * The universal ID, which uniquely identifies a document across all replicas of a database. In character format, the universal ID is a 32-character combination of hexadecimal digits (0-9, A-F).
		The universal ID is also known as the unique ID or UNID.
	* @param sDocId
	* @return
	* 
	* lindhaueradmin; 13.04.2008 15:35:31
	 * @throws ExceptionZZZ 
	 */
	public static boolean isValidDocId(String sDocId) throws ExceptionZZZ{
		boolean bReturn =false;
		main:{
			String sPattern = "[0-9A-G]+";
			bReturn = StringZZZ.matchesPattern(sDocId, sPattern, 1);							
		}//end main
		return bReturn;
	}
	
	
	/**Prüft, ob der angegebene Feldname ein gültiger Name für ein Notes-Feld ist.
	 * 
	 * Aus der Dokumentation: <BR><BR>
	 * Naming a field <BR>
A field name must begin with a letter and can include letters, numbers, and the symbols _ and $.<BR>
 The name can contain up to 32 bytes. (If you’re using multibyte characters, remember that 32 bytes is different than 32 characters.)<BR> 
 Use short, descriptive field names that you will remember when you write formulas that refer to the fields. <BR>
Field names cannot contain spaces. Run multiple words together, for example, ModifiedDate, or separate them with an underscore: Modified_Date.<BR>
The Designer templates use the naming convention of an initial capital letter followed by lowercase letters, for example, SendCopyTo.

	* @param sFieldname
	* @return
	* @throws ExceptionZZZ
	* 
	* lindhauer; 25.04.2008 14:39:29
	 */
	public static boolean isValidFieldname(String sFieldname) throws ExceptionZZZ{
		boolean bReturn = false;
		main:{
			if(StringZZZ.isEmpty(sFieldname)){
				ExceptionZZZ ez = new ExceptionZZZ("No fieldname", iERROR_PARAMETER_MISSING, DocumentZZZ.class.getName(), ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
			
			//Länge des Feldnamens
			int iLen = sFieldname.length();
			if(iLen> DocumentZZZ.iFIELDNAME_LENGTH || iLen <= 0) {
				bReturn = false;
				break main;
			}
						
			
			//Zeichen im Feldnamen
			String sPattern = "[0-9A-Za-z{_.$~}]+";
			bReturn = StringZZZ.matchesPattern(sFieldname, sPattern, 1);
		}
		return bReturn;
	}
	
	
	/** Heuristische Lösung: Die Items eines Notesdocuments werden in eine HashMap überführt.
	 *  Dabei werden Datentypen und weitere Eigenschaften des Notesdocuments nicht berücksichtigt.
	 *  HashMap soll sein <String, Vector>
	 *  
	 *  TODO: Idee: Die Werte filtern
	 *  a) Hinsichtlich ItemName (mit RegEx)
	 *  b) Hinsichtlich Item-Datentyp (auch Notes-Datentypen wie NAMES, READER, etc.)
	 *  
	 * @param doc
	 * @return HashMap
	 * @throws Exception
	 */
	public static HashMap documentValues2HashMap(Document doc)throws ExceptionZZZ{
		HashMap hmDocumentField=new HashMap();
		if(doc==null){
			ExceptionZZZ ez = new ExceptionZZZ("No notesdocument", iERROR_PARAMETER_MISSING, DocumentZZZ.class.getName(), ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
		
		main:{
			try{
			Vector vecItems = doc.getItems();
			
			Iterator it = vecItems.iterator();
			while(it.hasNext()){
				Item itemTemp = (Item)it.next();
				hmDocumentField.put(itemTemp.getName(), itemTemp.getValues());				
			}
						
			}catch(NotesException ne){
				ExceptionZZZ ez = new ExceptionZZZ("NotesException: " + ne.text, iERROR_RUNTIME, DocumentZZZ.class.getName(), ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
			
		}//end main
		return hmDocumentField;
	}//End function
	
	/** Heuristische Lösung: Die Items eines Notesdocuments werden in eine HashMap überführt.
	 *  Dabei werden Datentypen und weitere Eigenschaften des Notesdocuments nicht berücksichtigt.
	 *  AUSSER: Sie sind nicht serialisierbar. 
	 *                 Dies ist bei NotesDateTime der Fall. 
	 *                 Wird solch ein Wert erkannt, wird er in ein Java-Date umgewandelt.
	 *                 
	 *  HashMap soll sein <String, Vector>
	 *  
	 *  TODO: Idee: Die Werte filtern
	 *  a) Hinsichtlich ItemName (mit RegEx)
	 *  b) Hinsichtlich Item-Datentyp (auch Notes-Datentypen wie NAMES, READER, etc.)
	 *  
	 * @param doc
	 * @return HashMap
	 * @throws Exception
	 */
	public static HashMap documentValues2HashMapSerializable(Document doc)throws ExceptionZZZ{
		HashMap hmDocumentField=new HashMap();
		if(doc==null){
			ExceptionZZZ ez = new ExceptionZZZ("No notesdocument", iERROR_PARAMETER_MISSING, DocumentZZZ.class.getName(), ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}
		
		main:{
			try{
			Vector vecItems = doc.getItems();
			
			Iterator it = vecItems.iterator();
			while(it.hasNext()){
				Item itemTemp = (Item)it.next();
				
				String sValueDataType=null;
				//Hole den Datentyp aus einem Notesitem +++++++				
				try{
				Vector vectemp = itemTemp.getValues();
				Object objtemp = vectemp.firstElement();				
				if(objtemp!=null){
					sValueDataType = objtemp.getClass().getName();
				}else{
					sValueDataType = "NULL Objekt";
				}				
				//System.out.println("Config.transferNotesDocument2HashMap(...). Feldname '" + sField + "'. Datentyp '" + sValueDataType + "'");
				}catch(Exception e){
					//nix
				}
				//++++++++++++++++++++++++++++++++++++++++++++
				if(sValueDataType.equalsIgnoreCase("lotus.domino.local.DateTime")){
					//Ändere den DataTime Vector ab in einen Date - Vector					
					//Vector<Date> vecDateJava = new Vector<Date>();
					Vector vecDateJava = new Vector();
					Iterator itDateNotes = itemTemp.getValues().iterator();
					while(itDateNotes.hasNext()){
						DateTime objDateNotes = (DateTime)itDateNotes.next();
						Date objDateJava = objDateNotes.toJavaDate();
						vecDateJava.add(objDateJava);
					}

					hmDocumentField.put(itemTemp.getName(), vecDateJava);		
				}else{
	
					hmDocumentField.put(itemTemp.getName(), itemTemp.getValues());		
				}			
				
			}
			
			}catch(NotesException ne){
				ExceptionZZZ ez = new ExceptionZZZ("NotesException: " + ne.text, iERROR_RUNTIME, DocumentZZZ.class.getName(), ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
			
		}//end main
		return hmDocumentField;
	}//End function
	
	 public static String getItemDataType(Item objItem) throws ExceptionZZZ{
     	String sReturn = new String("");
     	if(objItem==null){
     		ExceptionZZZ ez = new ExceptionZZZ("No notesitem", iERROR_PARAMETER_MISSING, DocumentZZZ.class.getName(), ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
     	}
     		
     	main:{      
     		try{
				String sField = objItem.getName();
				
				//Hole den Datentyp aus einem Notesitem +++++++
				try{
				Vector vectemp = objItem.getValues();
				Object objtemp = vectemp.firstElement();
				if(objtemp!=null){
					sReturn = objtemp.getClass().getName();
				}else{
					sReturn = "NULL";
				}				
				//System.out.println("Config.transferNotesDocument2HashMap(...). Feldname '" + sField + "'. Datentyp '" + sValueDataType + "'");
				}catch(Exception e){
					//nix
				}
				
     		}catch(NotesException ne){
				ExceptionZZZ ez = new ExceptionZZZ("NotesException: " + ne.text, iERROR_RUNTIME, DocumentZZZ.class.getName(), ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
     	}//end main
     	return sReturn;
     }
	
	
	/** Methode gibt alle Antwortdokument ( $ REF ) eines Notesdokuments zurück UND auch wiederrum alle Antwortdokumente von diesen, usw.
	 *  Verwendet intern "addDocument", um ein Dokument zu einer DokumentCollections hinzuzufügen. (daher erst ab Notes 8.0 einsetzen.)
	* @param doc
	* @return
	* @throws ExceptionZZZ
	* 
	* lindhauer; 19.07.2012 15:30:59
	 */
	public static int collectResponses(Document doc, DocumentCollection colReturn) throws ExceptionZZZ{
		int iReturn = 0;
		main:{
		try{
			 DocumentCollection colTemp = doc.getResponses();
			 int ifound = 0; int icount=0;
			 if(colTemp!=null){
				 if(colTemp.getCount()>=1){
					//System.out.println("rekursion: response found: " + colTemp.getCount());
					Document docTemp = colTemp.getFirstDocument();					
					do{
						icount++;
						if(colReturn!=null){
							//System.out.println(icount + ". Dokument hinzufügen zur collection");
							try{
							colReturn.addDocument(docTemp);
							//System.out.println(icount + ". Dokument hinzugefügt zur collection");
							}catch(NotesException ne){									
								//nix machen, tritt z.B. auf, wenn ein Dokument hinzugefügt wird zu einer collection, in der es schon vorhanden ist.
								//System.out.println("Exception beim Dokument Hinzufügen zur Collection abgefangen.");
							}

						}else{
							//System.out.println("Dokumentcollection hinzuclonen zur collection");
							//!!! das ist erst ab Notes 8 möglich und auch gar nicht notwendig						colReturn=colTemp.cloneCollection();
							//System.out.println("Dokumentcollection hinzugecloned zur collection");
						}
						
						ifound =DocumentZZZ.collectResponses(docTemp, colReturn);							
						docTemp = colTemp.getNextDocument(docTemp);
					}while(docTemp!=null);
					
					iReturn = colTemp.getCount()+ifound;
					//System.out.println("rekursion: iReturn so far: " + iReturn);
					//System.out.println("rekursion: colReturn so far: " + colReturn.getCount());
				 }				 				 	
			 }//end if colTemp!=null						 			
		}catch(NotesException ne){
			ExceptionZZZ ez = new ExceptionZZZ("NotesException: " + ne.text, iERROR_RUNTIME, DocumentZZZ.class.getName(), ReflectCodeZZZ.getMethodCurrentName());
			throw ez;
		}	
		}//end main:			
		return iReturn;			
	}
}
