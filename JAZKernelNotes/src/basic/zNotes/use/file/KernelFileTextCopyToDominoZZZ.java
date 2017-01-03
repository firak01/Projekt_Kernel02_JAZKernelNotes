
package basic.zNotes.use.file;


import java.io.File;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zKernel.KernelZZZ;
import basic.zNotes.kernel.KernelNotesUseObjectZZZ;
import custom.zKernel.LogZZZ;
import custom.zKernel.file.FileTextCopyZZZ;
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
public class KernelFileTextCopyToDominoZZZ extends KernelNotesUseObjectZZZ {//ObjectZZZ implements AssetKernelNotesZZZ{
		//###########################################################################
	public String[] NameFileSourceInit(String sFlagControlIn) throws ExceptionZZZ {
		String stemp; int iCode; String sMethod;
			//String[] saFileSource = {"ln_konh.txt","ln_potential.txt",.....}
		main:{
				stemp = "this function has to be overwritten by a function of the include package";
				ExceptionZZZ ez = new ExceptionZZZ(stemp, iERROR_ZFRAME_METHOD, this, ReflectCodeZZZ.getMethodCurrentName()); 
				throw ez;	
			}          
			//return saFileSource;	
		}
	//################################################################
	public String[] NameFileTargetInit(String sFlagControl) throws ExceptionZZZ {
		String sMethod; int iCode; String stemp;
		//String[] saFileTarget = {"ln_konh.txt","ln_potential.txt",.....}
		main:{
			stemp = "this function has to be overwritten by a function of the include package";
			ExceptionZZZ ez = new ExceptionZZZ(stemp, iERROR_ZFRAME_METHOD, this, ReflectCodeZZZ.getMethodCurrentName()); 
			throw ez;	
			}          
			//return saFileSource;	
	}

	private boolean bFlagSourceRemove;
	private boolean bFlagSourceRename;
	private String sDirectoryTarget;
	private String[] saFileSource;
	private String sDirectorySource;
	//warum ??? private KernelZZZ objKernel;
	private KernelNotesLogZZZ objKernelNotesLog;
	private KernelNotesZZZ objKernelNotes;





	/**
	 * @param objKernel
	 * @param objLog
	 * @param saFlagControl
	 */
	public KernelFileTextCopyToDominoZZZ(KernelNotesZZZ objKernelIn, KernelNotesLogZZZ objLogIn, String[] saFlagControlIn)throws ExceptionZZZ {
		super(objKernelIn);
		main:{
		String stemp;
		boolean btemp;
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
		
		}//END main
	}//end constructor
	
	/**
	CONSTRUCTOR
	
	
	 @author 0823 , date: 18.10.2004
	 @param objKernelIn
	 @param objLogIn
	 @param sDirectorySourceIn
	 @param sDirectoryTargetIn
	 @param saFlagControlIn
	 @throws ExceptionZZZ
	 */
	public KernelFileTextCopyToDominoZZZ(KernelNotesZZZ objKernelIn, KernelNotesLogZZZ objLogIn, String sDirectorySourceIn, String sDirectoryTargetIn, String[] saFlagControlIn) throws ExceptionZZZ{
		this(objKernelIn, objLogIn, saFlagControlIn);
		main:{
		String stemp;
		boolean btemp;
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

		this.sDirectorySource = sDirectorySourceIn;
		this.sDirectoryTarget = sDirectoryTargetIn;
		}//End main
	}
	
	public KernelFileTextCopyToDominoZZZ(KernelNotesZZZ objKernelIn, KernelNotesLogZZZ objLogIn, String sDirectoryTargetIn, String[]saFileSourceWithTotalPath, String[] saFlagControlIn) throws ExceptionZZZ{
		this(objKernelIn, objLogIn, saFlagControlIn);
		main:{
		String stemp;
		boolean btemp;
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
		
				this.saFileSource = saFileSourceWithTotalPath;
				this.sDirectorySource = null;
				this.sDirectoryTarget = sDirectoryTargetIn;
		}//END main
	}




	
	//### Accessor-Function
	
	
	//### Constructor
	public KernelFileTextCopyToDominoZZZ() throws ExceptionZZZ{
		super(null, "init"); 
	}


	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public boolean startit() throws ExceptionZZZ{
		boolean bFunction = false;
		main:{
			String stemp;int iCode; String sMethod;
			int itemp = 0;
			boolean btemp = false;
			FileTextCopyZZZ objCopy;
			paramcheck:{
				if(objKernelNotes == null){
					stemp = "Missing property 'KernelNotesObject'";	
					ExceptionZZZ ez = new ExceptionZZZ(stemp, iERROR_PROPERTY_MISSING, this, ReflectCodeZZZ.getMethodCurrentName()); 					
					throw ez;	
				}
			}
			
		//	try{
				
				/*
				 * 1. Kopieren der Dateien in das Zielverzeichnis
				 */																
				//TODO Ein Konstruktor bekommt die entsprechenden Dateinamen in einem Array übergeben. Hier werden dann diese Dateien Kopiert und verschoben.
				
				//Das Ausgangsverzeichnis, ausgehend von dem Data-Directory, falls nichts anders konfiguriert.
				if(saFileSource == null){
					String sDirectorySource = getDirectorySource();
				}
				if(sDirectorySource==null & this.saFileSource==null){
						   stemp = "missing property 'Source directory' / missing property 'Source-File with total path-Array";							
						   ExceptionZZZ ez = new ExceptionZZZ(stemp, iERROR_PROPERTY_MISSING, this,ReflectCodeZZZ.getMethodCurrentName()); 						
						   throw ez;	
				}
				if(sDirectorySource!=null){
					objKernelNotesLog.writeLog("Filepath - source: " + sDirectorySource,this,"",2);		
				}else{
					objKernelNotesLog.writeLog("Using 'Source-File with total path-Array' passed by constructor",this,"",2);
				}
					
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Use only one Log-File for all relevant entries
		KernelZZZ objK = objKernelNotes.getKernelObject();
		LogZZZ objL = objK.getLogObject();
		objL.WriteLineDate("Output generated by the following class -  " + this.getClass().getName());
		String sDirLog = objL.getDirectory();
		String sFileLog = objL.getFilenameExpanded();
	   
		//Write to the notes-database:
		this.objKernelNotesLog.writeLog("for more details look in the directory: '" + sDirLog + "'", this, "startit", 1);
		this.objKernelNotesLog.writeLog("LogFile-Name: '" + sFileLog + "'", this, "startit", 1);
	   
		//FlagControl !!!   
		String[] saFlag = new String[1];// = new String("");
		 if(getFlag("source_remove")==true){
			 stemp = new String("source_remove");
			 saFlag[0] = stemp;
		 }else if(getFlag("source_rename")==true){ 
			 stemp =  new String("source_rename");
			 saFlag[0] = stemp;
		 } 
	
	
		//Das Zielverzeichnis, ausgehend von dem Data-Directory, falls nichts anders konfiguriert.
	   String sDirectoryTarget = getDirectoryTarget();
	   objKernelNotesLog.writeLog("Filepath - target: " + sDirectoryTarget,this,"",2);

	 if(this.saFileSource==null){
	 	//Fall: +++ Dateinamen werden in einer speziellen Funktion initalisiert oder es werden alle Dateien eines Verzeichnisses gewählt
	 	this.saFileSource = NameFileSourceInit("");
		 if(this.saFileSource == null){
		 	this.saFileSource = NameFileSourceInitByDirectory(sDirectorySource, "");
		 } 
		 if(this.saFileSource.length<=0){
			objKernelNotesLog.writeLog("No file found to process",this,"",1);
			bFunction = false;
			break main;
		 }
		 
		objCopy = new FileTextCopyZZZ(
			 objK, 
			 objL, 
			 sDirectorySource,
			 sDirectoryTarget,
			 this.saFileSource,
			 (String[])null,
			 saFlag);
	}else{
		//+++ Fall: Die Dateinamen wurden mit dem gesamten Pfad übergeben
		if(this.saFileSource.length<=0){
		   objKernelNotesLog.writeLog("No file found to process",this,"",1);
		   bFunction = false;
		   break main;
		}
		objCopy = new FileTextCopyZZZ(
			objK,
			objL,
			null,
			sDirectoryTarget,
			saFileSource,
			(String[])null,
			saFlag);
	}
	 
	 objKernelNotesLog.writeLog("start copying",this,"",3);				
	 objCopy.startit((String[])null);
	
		}//end main:
			
		return bFunction;	
	}//end startit()

	

	
	
	
	/**
	 * @param sDirectorySource
	 * @param string
	 * @return String - Array of all files in a directory
	 */
	public String[] NameFileSourceInitByDirectory(String sDirectorySource, String string) throws ExceptionZZZ {
		/*
		 * Remark: if you don´t delete the processed files after copying them,
		 * means that you might copy them again and again.
		 * A solution could be using a filter for the files !!!
		 */		
		String[] saFunction = null;
		main:{
			check:{
				if(StringZZZ.isEmpty(sDirectorySource)){					
						ExceptionZZZ ez = new ExceptionZZZ("Source Directory", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName()); 					
						throw ez;				
				}
			}//end check:
			
			//get all files of the directory
			File objFile = new File(sDirectorySource);
			if(objFile.exists()==false){
							String stemp = "Directory not exists Source Directory='" + sDirectorySource + "'";							
							ExceptionZZZ ez = new ExceptionZZZ(stemp, iERROR_PARAMETER_VALUE, this, ReflectCodeZZZ.getMethodCurrentName()); 							
							throw ez;	
			}else if(objFile.isDirectory()==false){
							String stemp = "this is not a directory Source Directory='" + sDirectorySource + "'";
							ExceptionZZZ ez = new ExceptionZZZ(stemp, iERROR_PARAMETER_VALUE, this, ReflectCodeZZZ.getMethodCurrentName()); 						
							throw ez;	
			}
			
			saFunction = objFile.list();
			
		}// end main:
		return saFunction;
	}

	

	
	public String getDirectoryTarget() throws ExceptionZZZ{
		String sFunction=null;
		if(StringZZZ.isEmpty(this.sDirectoryTarget)){
			sFunction = this.getKernelNotesObject().getPathDirectoryDefault();
			this.sDirectoryTarget = sFunction;
		}else{
			sFunction = this.sDirectoryTarget;
		} 
		return sFunction;
	}
	
	public String getDirectorySource() throws ExceptionZZZ{
		return this.sDirectorySource;
	}
	
	public void setDirectorySource(String sDir){
		this.sDirectorySource = sDir;	
	}
	
	public void setDirectoryTarget(String sDir){
		this.sDirectoryTarget = sDir;	
	}
	
	









	/**
	 * @return
	 */
	//public KernelZZZ getKernelObject() {
	//	return objKernel;
	//}
	
	public KernelNotesZZZ getKernelNotesObject(){
		return objKernelNotes;		
	}
	
	public void setKernelNotesObject(KernelNotesZZZ objkernelnotes){
		objKernelNotes = objkernelnotes;
	}

	public KernelNotesLogZZZ getKernelNotesLogObject() {
		return objKernelNotesLog;
	}

	/**
	 * @param logZZZ
	 */
	public void setKernelNotesLogObject(KernelNotesLogZZZ objLogIn) {
		this.objKernelNotesLog = objLogIn;
	}

	/**
	 * @return
	 */
	public ExceptionZZZ getExceptionObject() {
		return objException;
	}

	/**
	 * @param exceptionZZZ
	 */
	public void setExceptionObject(ExceptionZZZ exceptionZZZ) {
		objException = exceptionZZZ;
	}
	
	public boolean setFlag(String sFlagName, boolean bFlagValue){
		boolean bFunction = false;
		
		main:{ 
			if(StringZZZ.isEmpty(sFlagName)) break main;
			bFunction = super.setFlag(sFlagName, bFlagValue);
			if(bFunction==true) break main;
		
			//setting the flags of this object
			String stemp = sFlagName.toLowerCase();
			if(stemp.equals("source_rename")){
				bFlagSourceRename = bFlagValue;
				bFunction = true;
				break main;
			}else if(stemp.equals("source_remove")){
				bFlagSourceRemove = bFlagValue;
				bFunction = true;
				break main;
			}
			}//end main:
			return bFunction;
	}
	
	
	public boolean getFlag(String sFlagName){
			boolean bFunction = false;
			main:{
				if(StringZZZ.isEmpty(sFlagName)) break main;
				bFunction = super.getFlag(sFlagName);
				if(bFunction==true) break main;
			
				//getting the flags of this object
				String stemp = sFlagName.toLowerCase();
				if(stemp.equals("source_rename")){
					bFunction = bFlagSourceRename;
					break main;
				}else if(stemp.equals("source_remove")){
					bFunction = bFlagSourceRemove;
					break main;
				}
			}//end main:
			return bFunction;
		}
	
}//end class
