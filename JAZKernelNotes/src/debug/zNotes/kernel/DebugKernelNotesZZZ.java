package debug.zNotes.kernel;

import lotus.domino.Database;
import lotus.domino.NotesException;
import basic.zBasic.ExceptionZZZ;
import custom.zNotes.kernel.KernelNotesZZZ;

public class DebugKernelNotesZZZ{
	private KernelNotesZZZ objKernelNotes;
	public DebugKernelNotesZZZ(KernelNotesZZZ objKernelNotes){
		this.objKernelNotes = objKernelNotes;
	}
	public boolean startit(){
		boolean bReturn = false;
		try{
		main:{
			String sSystemNumber = objKernelNotes.getSystemNumber();
			System.out.println("SystemNumber: " + sSystemNumber);
			
			String sSystemKey = objKernelNotes.getSystemKey();
			System.out.println("SystemKey: " + sSystemKey);
			
			
			//////GET NOTES-KERNEL - CONFIGURATION ////////////////////////		
			Database dbTemp = objKernelNotes.getDBByAlias("ApplicationLog","");
			System.out.println("Title of Application-Log-Database: '" + dbTemp.getTitle() +"'");

			dbTemp = objKernelNotes.getDBByAlias("Configuration","");
			System.out.println("Title of Configuration-Database: '" + dbTemp.getTitle() + "'");
			
			//////GET - NON - NOTES - KERNEL - CONFIGURATION //////////////////////////
			String stemp = objKernelNotes.getEnvironmentZ("KernelNotes","TestEnvironment1");
			System.out.println("Alias: 'KernelNotes', Property TestEnvironment1 = '" + stemp + "'" );
			
			String stemp2 = objKernelNotes.getAgentParameterCurrentZ("KernelNotes", "TestParameterAgent1");
			System.out.println("Alias: 'KernelNotes', Property TestParameter1 = '" + stemp2 + "'");
		}//End main:
		}catch(NotesException ne){
			System.out.println(ne.text);
			ne.printStackTrace();
		}catch(ExceptionZZZ ez){
			System.out.println(ez.getDetailAllLast());
			ez.printStackTrace();
		}
		return bReturn;
	}//END startIt()
}//END class
