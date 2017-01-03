
package basic.zNotes.use.file;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import lotus.domino.Agent;
import lotus.domino.AgentBase;
import lotus.domino.AgentContext;
import lotus.domino.AgentRunner;
import lotus.domino.Database;
import lotus.domino.DateTime;
import lotus.domino.NotesException;
import lotus.domino.Session;
import basic.zBasic.ExceptionZZZ;
import basic.zKernel.KernelZZZ;
import custom.zNotes.kernel.KernelNotesZZZ;
import custom.zUtil.io.FileZZZ;



/**
 * @author Lindhauer
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class KernelFileTextCopyToExternalZZZ extends AgentBase{

	public static void main(String[] args) {
		 
	try{
			System.out.println("Start des Agenten. Main-Part");
			
			//String[] argsnew = {"(agtDataFileTextCopyToExtJAZ)|agtDataFileTextCopyToExtJAZ",
			//	"db\\itelligence\\Albis\\VersionTest\\CISA_Notes_SAP_Configuration.ns5"};
			String[] argsnew = {"(agtDataFileTextCopyToExtJAZ)|agtDataFileTextCopyToExtJAZ",
			"h:\\notesdata5\\db\\itelligence\\Albis\\VersionTest\\CISA_Notes_SAP_Configuration.ns5"};
			
			AgentRunner.main(argsnew);
			System.out.println("Ende des Agenten. Main-Part");
			System.exit(0);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void NotesMain() {
 
startpoint:
 	System.out.println("Start des Agenten. Agenten-Part");

    String sFuncCur = "NotesMain";  //Merke: Todo: Eine Klasse für das Handeln der Codeposition schreiben;
    PrintWriter out = getAgentOutput(); // For Output  //Merke: Todo - Eine Klasse für das Handeln des Otuputs schreiben.
                                                            //Diese soll sowohl Exceptions verarbeiten können
                                                            //als auch Codepositionsklassen
                                                            //als auch normale Outputs
                                                            //die Ausgabe soll erfolgen:
                                                            //a) in der Systemkonsole
                                                            //b) in dem Noteslog
                                                            //c) in eine Textdatei

    out.println("Function: '" + sFuncCur + "'");
    out.flush(); //Sicherstellen, dass der Outputcache leer ist.

main:
			try{                                            //Merke: Eine Klasse schreiben, die sich von Exception ableitet
                                                      //       und dann quasi als ZZZException immer alle main: - Teile umschliesst
                                                      //Merke2: Alle Z-Objekte müssen von einer Klasse ZZZObject erben.
                                                      //        Alle Funktionen müssen die ZZZException auswerfen.
				// get the session
				Session session = getSession();

         // Merke: wird mal wichtig, wenn es darum geht Profildokumente zu holen !!!
				// create Name Object from username
				// Name user = session.createName(session.getUserName());
				
				
			// get agentContext
			AgentContext agentContext= session.getAgentContext();


			 // get Handle on Database, the current Agent and the Passed Data
			 Database db = agentContext.getCurrentDatabase();
			 

			 //TODO write an static function to get the SystemNumber by the database.
			 String sTitle = db.getTitle().toLowerCase();
			 String sSystemNumber = null;
			 if(sTitle.endsWith("[test]")){
			 	sSystemNumber = "02";
			 }else if(sTitle.endsWith("[entwicklung]")){
			 	sSystemNumber = "03";
			 }else{
			 	sSystemNumber = "01";
			 }
			  
 
			KernelZZZ objKernel = new KernelZZZ("FGL", sSystemNumber, (String)null);
			KernelNotesZZZ objKernelNotes = new KernelNotesZZZ(session, objKernel,"","", (String[])null);
			

             out.println("Agent läuft in der Datenbank: '" + sTitle + "'");
             out.flush(); //Sicherstellen, dass der Outputcache leer ist.

			 String sUsername = session.getUserName();
			 out.println("Session für : '" + sUsername + "'");
             out.flush(); //Sicherstellen, dass der Outputcache leer ist.


				Agent agent = agentContext.getCurrentAgent();
				DateTime date = agent.getLastRun();
				if (date != null)
				out.println("Agent " + agent.getName() +
				" last ran on " + date.getDateOnly() +
				" at " + date.getTimeOnly());
				else
				out.println("Agent " + agent.getName() +
				" hasn't run yet");

/*
      	     // Get document used for passing data
      		   //Document doc = db.getDocumentByID(agent.getParameterDocID());
      		   Document doc = agentContext.getDocumentContext();
			   if(doc != null){
					out.println("Zugriff auf Dokument mit der DokID '" + doc.getUniversalID() + "'");
					out.flush(); //Sicherstellen, dass der Outputcache leer ist.
			   }else{
			   		out.println("KEIN Zugriff auf Dokument mit der DokID '" + doc.getUniversalID() + "'");
					out.flush(); //Sicherstellen, dass der Outputcache leer ist.
 					break main;
			   }
*/


		   // Die Dateinamen
        	String[] saFileSource = NameFileSourceNotesInit();
        String[] saFileTarget = NameFileTargetExternalInit();
		
		// Das Ausgangsverzeichnis, ausgehend von dem Data-Directory + "fileZZZ"
		String sDirectorySource = new String("");
		sDirectorySource = session.getEnvironmentString("Directory", true);
			if(sDirectorySource.length() <= 0){
					ExceptionZZZ ez = new ExceptionZZZ("Verzeichnis des Domino-Directory nicht ermittelbar.");					
					throw ez;
				}
				sDirectorySource = sDirectorySource + "\\fileZZZ\\";
		
	

		// Das Zielverzeichnis, ausgehend von dem Data-Directory + "fileZZZ"						
		String sDirectoryTarget = "\\\\SAP-Storage\\SAP2Notes";       
		//für den Test auf fgl01: 		String sDirectoryTarget = new String("e:\\temp\\albis\\");
								
		File dir = new File(sDirectoryTarget);
		if (dir.exists() == false){
				
					//Verzeichnis erstellen, falls nicht vorhanden.
					out.println("Verzeichnis nicht vorhanden: '" + sDirectoryTarget + "'");
		            out.flush(); //Sicherstellen, dass der Outputcache leer ist.
		            
		            boolean btemp = dir.mkdir();
		            if(btemp == true){
		            	out.println("Verzeichnis erstellt: '" + sDirectoryTarget + "'");
		            	out.flush(); //Sicherstellen, dass der Outputcache leer ist.		           	
		            } else {
		            	ExceptionZZZ ez = new ExceptionZZZ("Verzeichnis kann nicht erstellt werden: '" + sDirectoryTarget + "'");					
		            	throw ez;
		            }
				}else{
					//Das liefert bei isDirectory in HH immer ein false, wenn es mit Backslsh endet, keine Ahnung warum
					//if(dir.isDirectory() == false){
					//	ExceptionZZZ e = new ExceptionZZZ("Eine Datei unter dem Namen existiert bereits: '" + sDirectoryTarget + "'","");					
		            //	throw e;	
					//}
				}
			sDirectoryTarget = sDirectoryTarget + "\\";	
				

        //Den Ausgangsdateinamen
        //String sFileSource = doc.getItemValueString("FileNameZZZ");
         //o.k. wenn der Server als Dienst unter dem Administratorennamen läuft: String sFileSource = "\\\\Atilla\\temp\\test.txt";

       
     //Mappen der Dateinamen und kopieren               
        String sFileSource = new String("");
        String sPathSource = new String("");
        String sFileTarget = new String("");
        String sFileCurrent = new String("");
        String sPathTarget = new String("");
        String sPathTargetNew = new String("");
        
        boolean bTemp = false;
                        
        for(int i = 0; i <= saFileSource.length-1; i++){
       
       if(saFileSource[i] != null){ 	
        sFileSource = saFileSource[i];
        sFileTarget = saFileTarget[i];
        if(sFileTarget.length()==0){
        	sFileTarget = sFileSource;
        }
        // ToDo: Hier nach dem Targetnamen fragen und ggf. eine Zahl von 001-999 anhängen.
        
        
        if(sFileSource.length() > 0){	
        	sPathSource = sDirectorySource + sFileSource;             
       
       
			//Den Zieldateinamen
			sFileTarget = NameFileTargetCompute(sDirectoryTarget, sFileTarget, 3); // hier erfolgt ggf. die Umbenennung
   			sPathTarget = sDirectoryTarget + sFileTarget;
			
			out.println("Zieldateiname '" + sPathTarget + "'");
			out.flush(); //Sicherstellen, dass der Outputcache leer ist.
        
          	// Ziel: Es soll der Agent nicht abbrechen, nur weil er eine Datei nicht findet
			//Nun eine Datei kopieren

				try{
				sFileCurrent = sPathSource;
				File inputFile = new File(sFileCurrent);
				if(inputFile.exists()){
					 System.out.println("Folgende Datei wird kopiert '" + sFileCurrent + "'");
			
															
				sFileCurrent = sPathTarget;
				File outputFile = new File(sFileCurrent);
				
				FileReader fin = new FileReader(inputFile);
				BufferedReader bfin = new BufferedReader(fin);
				
   			    //try{
   				FileWriter fout = new FileWriter(outputFile);
   				BufferedWriter bfout = new BufferedWriter(fout);


   				int c;
          //ToDo: Verlauf der Kopieraktion protokollieren.
  				while ((c = bfin.read()) != -1) bfout.write(c);
			//Schliessen der Dateiströme
 				 bfin.close();
 				 bfout.close();
 				 fin.close();
 				 fout.close();
 				 
 			//umbenennen der Ausgangsdatei
 			GregorianCalendar d = new GregorianCalendar();
 			Integer iDateYear = new Integer(d.get(Calendar.YEAR));
 			Integer iDateMonth = new Integer(d.get(Calendar.MONTH) + 1);
 			Integer iDateDay = new Integer(d.get(Calendar.DAY_OF_MONTH));
 			Integer iTimeHour = new Integer(d.get(Calendar.HOUR_OF_DAY));
 			Integer iTimeMinute = new Integer(d.get(Calendar.MINUTE)); 			
 			String sDate = iDateYear.toString() + "-" + iDateMonth.toString() + "-" + iDateDay.toString()
 			+ "_" + iTimeHour.toString() + "_" + iTimeMinute.toString(); 
 			File inputFileProcessed = new File(sDirectorySource + "von Notes kopiert am " + sDate + " " + sFileSource);
 			inputFile.renameTo(inputFileProcessed);	 
				}else{
					 System.out.println("Quelldatei '" + sFileCurrent + "' ist nicht vorhanden.");
			
 				}//end if inputFile.exists()
 			
				}
				//Dateibehandlung - Exceptions 				 
       			catch(FileNotFoundException e){
				System.out.println(e.getMessage());
				//Es soll weiterhehenSystem.exit(0);
				}
				
				//Schreib-/LeseExceptions
				catch(IOException e){
				System.out.println(e.getMessage());
				//System.exit(0);
        		}	
        }//end if saFileSource[i] != null
        	}//end if Dateiname <> ""
        }//end for über alle Dateien

	}//end try FileNotFoundException
     //Alle weiteren Exceptions      
    catch(NotesException e) {
		System.out.println(e.id + " " + e.text);
		e.printStackTrace(out);
        System.exit(0);
		} //NotesException
	catch(ExceptionZZZ e){
		System.out.println("JAZ-Kernel Message: " + e.getMessage());
		System.out.println("Java-Excepton Msg.: " + e.getMessage());
		e.printStackTrace(out);
		System.exit(0);
		} 
	catch(Exception e){
		e.printStackTrace(out);
	    System.exit(0);
		}

end:
		System.out.println("Ende des Agenten. Agent-Part");
		//System.exit(0);
	} // end NotesMain
	
	
	
	
	
	/**
	 * Method NameFileTargetInit.
	 * @param saFileTarget
	 */
	private String[] NameFileTargetExternalInit() {
		String[] saFileTarget = {
			"sap_anwendungen.txt",			
			"sap_anwendungen_test.txt",
			
			"sap_kna1.txt",
			"sap_kna1_test.txt",
			
			"sap_rejection.txt",
			"sap_rejection_test.txt"
			};		             
		return saFileTarget;
	}


	/**
	 * Method NameFileSourceInit.
	 * @param saFileSource
	 * @return boolean
	 */
	private String[] NameFileSourceNotesInit() {
		String[] saFileSource = {
			"sap_anwendungen_01.txt",			
			"sap_anwendungen_test_01.txt",
			
			"sap_kna1_01.txt",
			"sap_kna1_test_01.txt",
			
			"sap_rejection_01.txt",
			"sap_rejection_test_01.txt"
			};
		             
		return saFileSource;	
	}
	
	/**
	 * Method NameFileTargetCompute.
	 * @param sPathTarget
	 * @return String
	 */
	public String NameFileTargetCompute(String sPathTarget, String sFileTarget, int iExpandLengthIn) throws ExceptionZZZ {
		/*
		 * Anhängen einer 3-stelligen Ziffer (von 001 - 999) an die Datei,
		 * falls der Dateiname schon vorhanden ist.
		 * Falls diese schon vorhanden ist, wird zur nächsten Ziffer gegangen		 * 
		 */
		 String sFunction = "";
		 String sEnding = new String("");   //die Endung z.B. txt
		 String sFileOnly = new String(""); //nur der Dateinamsbestandteil (also ohne Suffix)
		 int iFileOnlyLength = 0; // Länge des Dateinamens ohne das Suffix z.B. .txt
		 int iFileTargetLength = 0; //Länge des Dateinamens inklusive Suffix
		 int iExpandLength = 0;   //Länge der angestrebten Dateinamensendung
		 Integer intExpandValue = new Integer(0); //Wert der aktuellen Dateinamensendung		 
		 int iExpandValue;                        //dito
		 String sExpandValue = new String("");    //dito als String 		 
		 boolean bFlagAppend = false;
main:
{

	paramcheck:
	{
		//Leere Dateinamen können nicht "expanidert" werden.
		iFileTargetLength = sFileTarget.length();
		if(iFileTargetLength <= 0){
			break main;
		}				
		
		if(iExpandLengthIn <= 0 || iExpandLengthIn >= 6){
			iExpandLength = 3; //Default
		} else {
			iExpandLength = iExpandLengthIn;				
		}
						 
 	}//end paramcheck
 	
 	
 	
		FileZZZ objFile = new FileZZZ(sPathTarget, sFileTarget, (String[]) null);
		sExpandValue = objFile.getExpansionNext(iExpandLength);
		bFlagAppend = objFile.getFlag("ExpansionAppend");
		sFileOnly = objFile.getNameOnly();
		sEnding = objFile.getNameEnd();
		if(sEnding.length() > 0){
			sEnding = "." + sEnding;
		}
		
			if(objFile.exists() && bFlagAppend == true){
				sFunction = sFileOnly + sExpandValue + sEnding;		
				break main;
			}else{
				if(objFile.exists()==false && bFlagAppend == true){
				sFunction = sFileOnly + sExpandValue + sEnding;
				break main;
				}else{
					sFunction = sFileTarget;
				}
			}			 
		 
}//end main:
end:{	 
		return sFunction;
}//end end:
	}// end function

}
