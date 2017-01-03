/*
 * Created on 25.10.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package basic.zNotes.use.file;

import java.io.File;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zKernel.KernelZZZ;
import basic.zNotes.kernel.KernelNotesUseObjectZZZ;
import custom.zKernel.file.FileTextExportCrackZZZ;
import custom.zNotes.kernel.KernelNotesLogZZZ;
import custom.zNotes.kernel.KernelNotesZZZ;

/**

@author 0823 ,date 25.10.2004
*/
public class KernelFileTextCrackAndSplitZZZ  extends KernelNotesUseObjectZZZ{ 

	private File objFileToSplit;
	private KernelNotesZZZ objKernelNotes;
	private KernelNotesLogZZZ objKernelNotesLog;
	
	public KernelFileTextCrackAndSplitZZZ(KernelNotesZZZ objKernelNotesIn, File objFileIn, String[] saFlagControlIn)throws ExceptionZZZ{
		super(objKernelNotesIn);
		boolean btemp = KernelFileTextCrackAndSplitNew_(objFileIn, saFlagControlIn);
	}

	/** 
	
	 @author 0823 , date: 25.10.2004
	 @param objKernelIn
	 @param objLogIn
	 @param saFileToSplitIn
	 @param object
	 @param sDirTargetIn
	 @param sFileTargetIn
	 @param saFlagControlIn
	 @return
	 */
	private boolean KernelFileTextCrackAndSplitNew_(
	File objFileToSplitIn,
	String[] saFlagControlIn) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			String stemp;boolean btemp;
			File objFileToSplit = null;
			if(saFlagControlIn != null){
				for(int iCount = 0;iCount<=saFlagControlIn.length-1;iCount++){
					stemp = saFlagControlIn[iCount];
					btemp = setFlag(stemp, true);
					if(btemp==false){
							 stemp = "the flag '" + stemp + "' is not available.";								
							ExceptionZZZ ez = new ExceptionZZZ(stemp, iERROR_FLAG_UNAVAILABLE, this, ReflectCodeZZZ.getMethodCurrentName()); 							
							throw ez;							 
					}
				}
				if(this.getFlag("init")==true) break main;
			}		
			
			if(objFileToSplitIn==null){
				stemp = "parameter missing: 'Source file-object to split'";
			   ExceptionZZZ ez = new ExceptionZZZ(stemp, iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName()); 					 
			   throw ez;		
			}					
			this.objFileToSplit = objFileToSplitIn;
					
		}//end main:
		return bReturn;
	}

	/* (non-Javadoc)
	@see zzzKernelNotes.custom.AssetKernelNotesZZZ#getKernelNotesObject()
	 */
	public KernelNotesZZZ getKernelNotesObject() {
		return this.objKernelNotes;
	}

	/* (non-Javadoc)
	@see zzzKernelNotes.custom.AssetKernelNotesZZZ#setKernelNotesObject(zzzKernelNotes.custom.KernelNotesZZZ)
	 */
	public void setKernelNotesObject(KernelNotesZZZ objKernelNotes) {
	this.objKernelNotes = objKernelNotes;
	}

	/* (non-Javadoc)
	@see zzzKernelNotes.basic.KernelAssetKernelNotesZZZ#getKernelNotesLogObject()
	 */
	public KernelNotesLogZZZ getKernelNotesLogObject() {
		return this.objKernelNotesLog;
	}

	/* (non-Javadoc)
	@see zzzKernelNotes.basic.KernelAssetKernelNotesZZZ#setKernelNotesLogObject(zzzKernelNotes.custom.KernelNotesLogZZZ)
	 */
	public void setKernelNotesLogObject(KernelNotesLogZZZ objKernelNotesLogIn) {
		this.objKernelNotesLog = objKernelNotesLogIn;
	}
	
	public boolean startit() throws ExceptionZZZ{
		boolean bReturn = false;
		main:{
			String stemp; int iCode; String sMethod;
			boolean bFlagAnyFileProcessed=false;
			check:{
				if(this.objFileToSplit==null){
					stemp = "missing property 'File Object to split'";
				   ExceptionZZZ ez = new ExceptionZZZ(stemp, iERROR_PROPERTY_MISSING, this, ReflectCodeZZZ.getMethodCurrentName()); 				
				   throw ez;	
				}
				
			}//end check
			
			//0. proof file existance
			KernelNotesZZZ objKernelNotes = this.getKernelNotesObject();
			KernelZZZ objKernel = objKernelNotes.getKernelObject();
			File objFile = this.getFileObjectToSplit();
			if(objFile.exists()==false|objFile.isFile()==false){
				objKernelNotesLog.writeLog("No file exists '" + objFile.getPath() + "'",this,"startit",1);
				bReturn = false;
			}
			
			//1. create Cracking and splitting object
			String sProgramAlias = objKernelNotes.getAgentAliasCurrentZ("Export");
			FileTextExportCrackZZZ objCrack = new FileTextExportCrackZZZ(objKernel, objLog, objFile, sProgramAlias, null);
			
			//2. for all filenames, crack them
			objKernelNotesLog.writeLog("start cracking file '" + objFile.getPath() + "'",this,"",3);
			bReturn = objCrack.crackCustom();
		
		}//end main
		return bReturn;
	}
	
	/** 
	
	 @author 0823 , date: 25.10.2004
	 @return
	 */
	public File getFileObjectToSplit() {
		return this.objFileToSplit;
	}

	/** 
	
	 @author 0823 , date: 25.10.2004
	 @param file
	 */
	public void setFileObjectToSplit(File file) {
		this.objFileToSplit = file;
	}

}//end class KernelFileTextCrackAndSplitZZZ
