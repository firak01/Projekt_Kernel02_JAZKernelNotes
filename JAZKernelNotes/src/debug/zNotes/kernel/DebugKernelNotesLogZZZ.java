package debug.zNotes.kernel;

import lotus.domino.AgentBase;
import lotus.domino.Database;
import lotus.domino.NotesException;
import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
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
public class DebugKernelNotesLogZZZ extends AgentBase {
	private KernelNotesZZZ objKernelNotes;
	public DebugKernelNotesLogZZZ(KernelNotesZZZ objKernelNotes){
		this.objKernelNotes = objKernelNotes;
	}
	
	public boolean startit(){
		
	boolean bReturn = false;
	try{
	main:{
		
		String sMethod =  ReflectCodeZZZ.getMethodCurrentName();
		KernelNotesLogZZZ objLog = this.objKernelNotes.getKernelNotesLogObject();
		
		Database dbLog = objLog.getDBLog();
		System.out.println("Title of Log-Database: '" + dbLog.getTitle() +"'");
		
	    objLog.writeLog("DAS ist ein TEST", this,sMethod, 3);
	    objLog.writeLog("DAS ist ein TEST auf Fehler", this, sMethod, 0);
	  		
	}//End main:
	}catch(NotesException ne){
		System.out.println(ne.text);
		ne.printStackTrace();
	}catch(ExceptionZZZ ez){
		System.out.println(ez.getDetailAllLast());
		ez.printStackTrace();
	}
	return bReturn;
}//END startit()
}//END class
