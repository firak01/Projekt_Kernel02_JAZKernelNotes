package basic.zNotes.kernel;

import basic.zBasic.ExceptionZZZ;
import basic.zKernel.KernelUseObjectZZZ;
import custom.zNotes.kernel.KernelNotesLogZZZ;
import custom.zNotes.kernel.KernelNotesZZZ;

public class KernelNotesUseObjectZZZ extends KernelUseObjectZZZ implements IKernelNotesZZZ{
	private KernelNotesZZZ objKernelNotes = null;
	public KernelNotesUseObjectZZZ(){
		//Wird benötigt, um auch mal einfach so per Reflection-API ein Objekt zu erzeugen
	}
	public KernelNotesUseObjectZZZ(KernelNotesZZZ objKernelNotes){
		super(objKernelNotes.getKernelObject());
		this.objKernelNotes = objKernelNotes;
	}
	public KernelNotesUseObjectZZZ(KernelNotesZZZ objKernelNotes, String[] saFlag){
		super(objKernelNotes.getKernelObject(), saFlag);
		
		//TODO: Auswerten des Control - Flags per .getFlag/setFlag - Methoden
		this.objKernelNotes = objKernelNotes;
		
	}
	public KernelNotesUseObjectZZZ(KernelNotesZZZ objKernelNotes, String sFlag){
		super(objKernelNotes.getKernelObject(), sFlag);
		
		//TODO Auswerten des Control - Flags per .getFlag/setFlag - Methoden
		this.objKernelNotes = objKernelNotes;
	}

	public KernelNotesZZZ getKernelNotesObject() {
		return this.objKernelNotes;
	}
	public void setKernelNotesObject(KernelNotesZZZ objKernelNotes) {
		this.objKernelNotes = objKernelNotes;
	}
	public KernelNotesLogZZZ getKernelNotesLogObject() throws ExceptionZZZ {
		KernelNotesLogZZZ objReturn=null;
		main:{
			if(this.objKernelNotes==null) break main;
			objReturn = this.objKernelNotes.getKernelNotesLogObject();
		}
		return objReturn; 
	}
	public void setKernelNotesLogObject(KernelNotesLogZZZ objKernelNotesLogIn) {
		main:{
			if (this.objKernelNotes == null) break main;
			this.objKernelNotes.setKernelNotesLogObject(objKernelNotesLogIn);
		}
	}
}
