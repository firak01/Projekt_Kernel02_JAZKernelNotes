/*
 * Created on 08.10.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package custom.zNotes.use.file;

import basic.zBasic.ExceptionZZZ;
import basic.zNotes.use.file.KernelFileTextCrackAndJoinZZZ;
import custom.zNotes.kernel.KernelNotesZZZ;

/**

@author 0823 ,date 08.10.2004
*/
public class FileTextCrackAndJoinZZZ extends KernelFileTextCrackAndJoinZZZ{

	/**
	CONSTRUCTOR
	
	
	 @author 0823 , date: 08.10.2004
	 @param objKernelIn
	 @param objLogIn
	 @param saFileToJoinIn
	 @param sFileTargetIn
	 @param saFlagControlIn
	 @throws ExceptionZZZ
	 */
	public FileTextCrackAndJoinZZZ(KernelNotesZZZ objKernelIn, String[] saFileToJoinIn, String sDirTargetIn, String sFileTargetIn, String[] saFlagControlIn) throws ExceptionZZZ {
		super(objKernelIn, saFileToJoinIn, sDirTargetIn, sFileTargetIn, saFlagControlIn);
	}

}
