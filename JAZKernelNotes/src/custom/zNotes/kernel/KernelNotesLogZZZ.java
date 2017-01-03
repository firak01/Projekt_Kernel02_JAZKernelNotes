package custom.zNotes.kernel;


import basic.zBasic.ExceptionZZZ;
import basic.zNotes.kernel.KernelKernelNotesLogZZZ;
import basic.zNotes.kernel.KernelKernelNotesZZZ;



/**
 * @author 0823
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class KernelNotesLogZZZ extends KernelKernelNotesLogZZZ {
	
	/**
	 * @param notesZZZ
	 * @param string
	 */
	public KernelNotesLogZZZ(KernelKernelNotesZZZ notesZZZ, String string) throws ExceptionZZZ {
		this((KernelNotesZZZ)notesZZZ, string);
	}
	/**
	 * @param objKernel
	 * @param saFlagControl
	 */
	public KernelNotesLogZZZ(KernelNotesZZZ objKernel, String[] saFlagControl) throws ExceptionZZZ {
		super(objKernel, saFlagControl); 
	}
	//Constructor
	public KernelNotesLogZZZ()throws ExceptionZZZ{
		super();
	}
	public KernelNotesLogZZZ(KernelNotesZZZ objKernel, String sFlagControl) throws ExceptionZZZ{
		super(objKernel, sFlagControl); 
	}
	
	public String generateLogString(String sLogComment) throws ExceptionZZZ{
		String sFunction = new String("");
		main:{		
				sFunction = "No class spezified\\No function specified" + "#" + sLogComment;
		}//end main
		return sFunction;
	}
	
	public String generateLogString(String sLogComment, Object obj) throws ExceptionZZZ{
		String sFunction = new String("");
		main:{
					sFunction = obj.getClass().getName() + "\\No function spezified" + "#" + sLogComment;
		}
		 return sFunction;
	}
	public String generateLogString(String sLogComment, Object obj, String sMethod) throws ExceptionZZZ{
		String sFunction = new String("");
		main:{	
			KernelNotesZZZ objKernel = this.getKernelNotesObject();
			sFunction = obj.getClass() + "\\" + sMethod + "#" + sLogComment;
		}
		return sFunction;
	}
	

	
}//end class KernelLogZZZ
