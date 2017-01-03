package custom.zNotes.kernel;


import lotus.domino.Session;
import basic.zBasic.ExceptionZZZ;
import basic.zKernel.KernelZZZ;
import basic.zNotes.kernel.KernelKernelNotesZZZ;
import basic.zNotes.kernel.NotesContextProviderZZZ;
/**
 * @author 0823
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class KernelNotesZZZ extends KernelKernelNotesZZZ{// implements AssetObjectZZZ{

	/**
	 * Constructor KernelZZZ.
	 * @param strings 
	 * @param objKernel 
	 * @param session 
	 */
	public KernelNotesZZZ(NotesContextProviderZZZ objContext, String sApplicationKeyNotes, String sSystemNumberNotes, String[] saFlagControlIn) throws ExceptionZZZ{
		super(objContext, sApplicationKeyNotes, sSystemNumberNotes, saFlagControlIn);
	}
	
	public KernelNotesZZZ(Session sessionIn, KernelZZZ objKernel, String sApplicationKeyNotes, String sSystemKeyNotes, String[] saFlagControlIn) throws ExceptionZZZ{
		super(sessionIn, objKernel, sApplicationKeyNotes, sSystemKeyNotes, saFlagControlIn);
	}
	
	//Constructor	
	public KernelNotesZZZ(Session sessionIn, String sKeyApplicationIn, String sSystemNumber, String[] saFlagControlIn) throws ExceptionZZZ{
		super(sessionIn, sKeyApplicationIn, sSystemNumber, saFlagControlIn);  //Merke: Der Aufruf des Konstruktors der Super-Klasse muss am Anfang stehen, 
															 	//       Die print Anweisung kann also nur dahinter kommen.
		//System.out.println("Bin im Konstruktor des Custom-Kernel-Objektes");		
	}
	


	/**
	 * Constructor KernelZZZ.
	 * @param objSession
	 * @param string
	 * @param string1
	 */
	public KernelNotesZZZ(Session objSession,  String sApplicationKey, String sSystemNumber,  String sFlagControl) throws ExceptionZZZ {
		super(objSession, sApplicationKey, sSystemNumber, sFlagControl);  //Merke: Der Aufruf des Konstruktors der Super-Klasse muss am Anfang stehen, 
															 	//       Die print Anweisung kann also nur dahinter kommen.
		//System.out.println("Bin im Konstruktor des Custom-Kernel-Objektes");
	}

 
	

	
	//Overwritten Function: Getting the default-Key
	public String getApplicationKeyDefault() {
	//Merke: Diese Function muss immer vom customizing Teil überschreiben werden.
	String sFunction = null;
	
	main:{
		sFunction = "FGL";	
	}//end main
	end:{
		return sFunction;
	}
	}//end function
	
	
	


	

	
	
	

}
