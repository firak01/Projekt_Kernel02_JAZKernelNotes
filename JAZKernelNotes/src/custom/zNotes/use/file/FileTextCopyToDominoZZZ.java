package custom.zNotes.use.file;




import basic.zBasic.ExceptionZZZ;
import basic.zNotes.use.file.KernelFileTextCopyToDominoZZZ;
import custom.zNotes.kernel.KernelNotesLogZZZ;
import custom.zNotes.kernel.KernelNotesZZZ;
/**
 * @author Lindhauer
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class FileTextCopyToDominoZZZ extends KernelFileTextCopyToDominoZZZ{
	/**
	 * @param objKernel
	 * @param objLog
	 * @param saFlagControl
	 */
	public FileTextCopyToDominoZZZ(KernelNotesZZZ objKernelNotes, KernelNotesLogZZZ objKernelNotesLog, String[] saFlagControl) throws ExceptionZZZ {
		super(objKernelNotes, objKernelNotesLog, saFlagControl);
	}
	public FileTextCopyToDominoZZZ(KernelNotesZZZ objKernelIn, KernelNotesLogZZZ objLogIn, String sDirectoryTargetIn, String[]saFileSourceWithTotalPath, String[] saFlagControlIn) throws ExceptionZZZ{
		super(objKernelIn, objLogIn, sDirectoryTargetIn, saFileSourceWithTotalPath, saFlagControlIn);
	}
	public FileTextCopyToDominoZZZ() throws ExceptionZZZ{
		super();
	}

	
	
	
	//###########################################################################
public String[] NameFileSourceInit(String sFlagControlIn) {
		//String[] saFileSource = {"ln_konh.txt","ln_potential.txt",...}
		//return saFileSource;
		/*
		 * todo: get all filenames of the source-directory and return them
		 */
		 
		 
		 
		return null;	
	}
	
	//################################################################
	public String[] NameFileTargetInit(String sFlagControl) {
		//String[] saFileTarget = null; //{"ln_konh_01.txt","ln_potential_01.txt",....}
		//return saFileTarget;
		return null;  //null means: the TargetName = SourceName
	}
	
}//end class
