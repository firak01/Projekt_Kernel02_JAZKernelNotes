package debug.zNotes.kernel;

import lotus.domino.AgentBase;
import lotus.domino.Session;
import basic.zBasic.ExceptionZZZ;
import basic.zKernel.KernelZZZ;
import basic.zNotes.kernel.NotesContextProviderZZZ;
import custom.zKernel.LogZZZ;
import custom.zNotes.kernel.KernelNotesZZZ;

public class DebugKernelNotesLogAgentZZZ extends AgentBase{

	/** TODO What the method does.
	 * @return void
	 * @param args 
	 * 
	 * lindhaueradmin; 02.10.2006 09:06:49
	 */
	public static void main(String[] args) {
		try{
		main:{
		KernelZZZ objKernel = new KernelZZZ("KernelNotes", "01", "", "ZKernelConfigKernelNotes_test.ini",(String)null);
		LogZZZ objLog = objKernel.getLogObject();
		
		//Der ContextProvider sorgt für die Session und die SystemNumber
		//Das soll unabhängig davon sein, wie dieser Code ausgeführt wird, ob als Agent oder als Servlet oder im "J2EE IDE Debugger".		
		 NotesContextProviderZZZ objContext = new NotesContextProviderZZZ(objKernel, "NotesKernel", "agtDebugKernelNotesLog2JAZ");
		 Session objSession = objContext.getSession();
		 String sSystemNumber = objContext.computeSystemNumber(); //a static function, contained in the custom-package
		 		 		
		 //Hier der eigentliche NotesKernel
		 KernelNotesZZZ objKernelNotes = new KernelNotesZZZ(objContext, "JAZTest", sSystemNumber,  null);
		 
		 //Hier das, was die Arbeit macht
    	 DebugKernelNotesLogZZZ objDo = new DebugKernelNotesLogZZZ(objKernelNotes);
		 objDo.startit();
		}//end main
		}catch( ExceptionZZZ ez){
			System.out.println(ez.getDetailAllLast());
			ez.printStackTrace();
		}
		System.out.println("End of agent");
		System.exit(0);
		end:{
			System.out.println("End Test Function.");			
			//Bei NotesAgenten darf man das nicht ausführen !!! Fehlermeldungen !!! System.exit(0);					
		}
	} //end function main()
	
	public void NotesMain() {
		System.out.println("Start Test Function.");
		
		main:{
		try{								
				KernelZZZ objKernel = new KernelZZZ("KernelNotes", "01", "", "ZKernelConfigKernelNotes_test.ini",(String)null);
				LogZZZ objLog = objKernel.getLogObject();
				System.out.println("ZKernel erfolreich initialisiert.");
				
				//Der ContextProvider sorgt für die Session und die SystemNumber
				//Das soll unabhängig davon sein, wie dieser Code ausgeführt wird, ob als Agent oder als Servlet oder im "J2EE IDE Debugger".		
				Session objSession = this.getSession();
				System.out.println("Session erfolreich initialisiert.");
				
				 NotesContextProviderZZZ objContext = new NotesContextProviderZZZ(objKernel, "NotesKernel", objSession);
				 System.out.println("NotesContextProvider erfolreich initialisiert.");
				 
				 String sSystemNumber = objContext.computeSystemNumber(); //a static function, contained in the custom-package
				 System.out.println("Systemnumber computed: " + sSystemNumber);
				 
				 //Hier der eigentliche NotesKernel
				 KernelNotesZZZ objKernelNotes = new KernelNotesZZZ(objSession, objKernel,"JAZTest", sSystemNumber,  null);
				 
				 //Hier das, was die Arbeit macht
		    	 DebugKernelNotesLogZZZ objDo = new DebugKernelNotesLogZZZ(objKernelNotes);
				 objDo.startit();
			}catch(ExceptionZZZ ez){
				System.out.println(ez.getDetailAllLast());
			}
		}//end main
		
		end:{
			System.out.println("End Test Function.");			
			//Bei NotesAgenten darf man das nicht ausführen !!! Fehlermeldungen !!! System.exit(0);					
		}
	}//END notesmain()
}//END Class

