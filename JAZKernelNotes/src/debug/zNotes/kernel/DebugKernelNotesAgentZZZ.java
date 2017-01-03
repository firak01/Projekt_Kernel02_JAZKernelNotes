package debug.zNotes.kernel;
import lotus.domino.AgentBase;
import lotus.domino.Session;
import basic.zBasic.ExceptionZZZ;
import basic.zKernel.KernelZZZ;
import basic.zNotes.kernel.NotesContextProviderZZZ;
import custom.zKernel.LogZZZ;
import custom.zNotes.kernel.KernelNotesZZZ;


/**
 * @author 0823
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class DebugKernelNotesAgentZZZ extends AgentBase{

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
				
				 NotesContextProviderZZZ objContext = new NotesContextProviderZZZ(objKernel, this.getClass().getName(), objSession);
				 System.out.println("NotesContextProvider erfolreich initialisiert.");
				 
				 String sSystemNumber = objContext.computeSystemNumber(); //a static function, contained in the custom-package
				 System.out.println("Systemnumber computed: " + sSystemNumber);
				 
				 //Hier der eigentliche NotesKernel
				 KernelNotesZZZ objKernelNotes = new KernelNotesZZZ(objSession, objKernel,"JAZTest", sSystemNumber,  null);
				 
				 //Hier das, was die Arbeit macht
		    	 DebugKernelNotesZZZ objDo = new DebugKernelNotesZZZ(objKernelNotes);
				 objDo.startit();
			}catch(ExceptionZZZ ez){
				System.out.println(ez.getDetailAllLast());
			}
		}//end main
		
		end:{
			System.out.println("End Test Function.");			
			//Bei NotesAgenten darf man das nicht ausführen !!! Fehlermeldungen !!! System.exit(0);					
		}
	} //end function notesmain()
	

	public static void main(String[] args) {
		try{
		KernelZZZ objKernel = new KernelZZZ("KernelNotes", "01", "", "ZKernelConfigKernelNotes_test.ini",(String)null);
		LogZZZ objLog = objKernel.getLogObject();
		
		//Der ContextProvider sorgt für die Session und die SystemNumber
		//Das soll unabhängig davon sein, wie dieser Code ausgeführt wird, ob als Agent oder als Servlet oder im "J2EE IDE Debugger".		
		 NotesContextProviderZZZ objContext = new NotesContextProviderZZZ(objKernel, "NotesKernel", "agtDebugKernelNotes2JAZ");
		 Session objSession = objContext.getSession();
		 String sSystemNumber = objContext.computeSystemNumber(); //a static function, contained in the custom-package
		 		 		
		 //Hier der eigentliche NotesKernel
		 KernelNotesZZZ objKernelNotes = new KernelNotesZZZ(objContext, "JAZTest", sSystemNumber,  null);
		
		 //Hier das, was die Arbeit macht
    	 DebugKernelNotesZZZ objDo = new DebugKernelNotesZZZ(objKernelNotes);
		 objDo.startit();
		}catch( ExceptionZZZ ez){
			System.out.println(ez.getDetailAllLast());
			ez.printStackTrace();
		}
		System.out.println("End of agent");
		System.exit(0);
	}//End main
}//END Class


