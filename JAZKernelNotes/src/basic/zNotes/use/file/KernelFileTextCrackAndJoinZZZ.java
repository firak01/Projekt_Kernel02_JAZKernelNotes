/*
 * Created on 08.10.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package basic.zNotes.use.file;

import java.io.File;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zKernel.IKernelZZZ;
import basic.zNotes.kernel.KernelNotesUseObjectZZZ;
import custom.zKernel.file.FileTextImportCrackZZZ;
import custom.zKernel.file.FileTextJoinZZZ;
import custom.zNotes.kernel.KernelNotesLogZZZ;
import custom.zNotes.kernel.KernelNotesZZZ;


/**

@author 0823 ,date 08.10.2004
*/
public class KernelFileTextCrackAndJoinZZZ  extends KernelNotesUseObjectZZZ{ 
	private File objFile;
	private String[] saFileToJoin;
	private KernelNotesLogZZZ objKernelNotesLog;
	private String sDirectoryTarget;
	private KernelNotesZZZ objKernelNotes;
	
	public KernelFileTextCrackAndJoinZZZ(KernelNotesZZZ objKernelIn, String[] saFileToJoinIn, String sDirTargetIn, String sFileTargetIn, String[] saFlagControlIn) throws ExceptionZZZ{
		super(objKernelIn);
		boolean btemp = KernelFileTextCrackAndJoinNew_(saFileToJoinIn, null, sDirTargetIn, sFileTargetIn, saFlagControlIn);		
	}
	 
	
	/** ++++++++++++++++++++++++++++++++++++++++++
	
	 @author 0823 , date: 08.10.2004
	 @return
	 */
	private boolean KernelFileTextCrackAndJoinNew_(
	String[] saFileToJoinIn,
	File objFileIn,
	String sDirectoryTargetIn,
	String sFileTargetIn, 
	String[] saFlagControlIn) throws ExceptionZZZ {
		
		boolean bReturn = false;
		main:{
		String stemp;boolean btemp;String sMethod;int iCode;
		File objFile = null;
		
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
				
				if(objFileIn==null & sFileTargetIn ==null){
					stemp = "parameter missing: 'Target filename / Target file-object'";				
				   ExceptionZZZ ez = new ExceptionZZZ(stemp, iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName()); 				
				   throw ez;		
				}else if(objFileIn==null){
					if(StringZZZ.isEmpty(sDirectoryTargetIn)){
						objFile = new File(sFileTargetIn);						
					}else{
						objFile = new File(sDirectoryTargetIn + File.separator + sFileTargetIn);
					}
				}else{
					objFile = objFileIn;
				}
				this.objFile = objFile;
				File objDirTemp = new File(this.objFile.getParent());
				this.sDirectoryTarget = objDirTemp.getAbsolutePath();
				

				if(saFileToJoinIn == null){
					stemp = "parameter missing: 'String Array of files to join'";					
				   ExceptionZZZ ez = new ExceptionZZZ(stemp, iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName()); 
				   throw ez;		
				}
				this.saFileToJoin = saFileToJoinIn;
				 
				
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
				if(this.saFileToJoin==null){
					stemp = "missing property  'Array of filename to join'.";
				   ExceptionZZZ ez = new ExceptionZZZ(stemp, iERROR_PROPERTY_MISSING, this, ReflectCodeZZZ.getMethodCurrentName()); 
				   throw ez;	
				}
				if(this.objFile==null){
					stemp = "missing property 'File Object'";				
				   ExceptionZZZ ez = new ExceptionZZZ(stemp, iERROR_PROPERTY_MISSING, this, ReflectCodeZZZ.getMethodCurrentName()); 				 
				   throw ez;	
				}
				
				//proof any file existance
				for(int iCount = 0; iCount <= this.saFileToJoin.length-1; iCount++){
					File objFile = new File(this.saFileToJoin[iCount]);
					if(objFile.exists() & objFile.isFile()){
						bFlagAnyFileProcessed=true;
						break;
					}
				}
				if(bFlagAnyFileProcessed==false){
					objKernelNotesLog.writeLog("No file found to process.", this, "startit",1);
					bReturn = false;
					break main;
				}
			}//end check
			
			
			//1. create Joining object, remember: This will create an output - stream, which will create a file, also if there are no files to process.
			KernelNotesZZZ objKernelNotes = this.getKernelNotesObject();
			IKernelZZZ objKernel = objKernelNotes.getKernelObject();
			FileTextJoinZZZ objJoin = new FileTextJoinZZZ(objKernel, objLog, objFile, null);
			
			//2. for all filenames, crack them and add them to the joining - object
			objKernelNotesLog.writeLog("start joining",this,"",3);
			for(int iCount = 0; iCount <= this.saFileToJoin.length-1; iCount++){
					File objFile = new File(this.saFileToJoin[iCount]);
					if(objFile.exists() & objFile.isFile()){
						FileTextImportCrackZZZ objCrack = new FileTextImportCrackZZZ(objKernel, objLog, objFile, null);
	                    objCrack.load();
	                    objCrack.crackCustom();	                    
						objJoin.append(objCrack);							
					}
			}
			bReturn = true;
				
		}//end main
		return bReturn;
	}

}
