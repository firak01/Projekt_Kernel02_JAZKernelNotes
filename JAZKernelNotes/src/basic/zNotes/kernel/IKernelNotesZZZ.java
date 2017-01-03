package basic.zNotes.kernel;

import basic.zBasic.ExceptionZZZ;
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
public interface IKernelNotesZZZ { 
	public abstract KernelNotesZZZ getKernelNotesObject();
	public abstract void setKernelNotesObject(KernelNotesZZZ objKernelNotes);
	public abstract KernelNotesLogZZZ getKernelNotesLogObject() throws ExceptionZZZ;
	public abstract void setKernelNotesLogObject(KernelNotesLogZZZ objKernelNotesLogIn);
}


