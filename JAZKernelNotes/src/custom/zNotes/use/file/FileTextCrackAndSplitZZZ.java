/*
 * Created on 25.10.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package custom.zNotes.use.file;

import java.io.File;

import basic.zBasic.ExceptionZZZ;
import basic.zNotes.use.file.KernelFileTextCrackAndSplitZZZ;
import custom.zNotes.kernel.KernelNotesZZZ;

/**

@author 0823 ,date 25.10.2004
*/
public class FileTextCrackAndSplitZZZ extends KernelFileTextCrackAndSplitZZZ{

	/**
	CONSTRUCTOR
	
	
	 @author 0823 , date: 25.10.2004
	 @param objKernelIn
	 @param objLogIn
	 @param objFileIn
	 @param saFlagControlIn
	 @throws ExceptionZZZ
	 */
	public FileTextCrackAndSplitZZZ(KernelNotesZZZ objKernelIn, File objFileIn, String[] saFlagControlIn) throws ExceptionZZZ {
		super(objKernelIn, objFileIn, saFlagControlIn);
	}

}
