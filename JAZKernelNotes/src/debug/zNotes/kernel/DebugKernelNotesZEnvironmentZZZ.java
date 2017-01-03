/*
 * Created on 06.10.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package debug.zNotes.kernel;

import lotus.domino.AgentBase;
import lotus.domino.AgentContext;
import lotus.domino.AgentRunner;
import lotus.domino.Database;
import lotus.domino.NotesException;
import lotus.domino.Session;
import basic.zBasic.ExceptionZZZ;
import basic.zKernel.KernelZZZ;
import basic.zNotes.kernel.NotesContextProviderZZZ;
import custom.zNotes.kernel.KernelNotesZZZ;

/**

@author 0823 ,date 06.10.2004
*/
public class DebugKernelNotesZEnvironmentZZZ extends AgentBase {

	public static void main(String[] args) {
		try{
				System.out.println("Start des Agenten. Main-Part");

				//lokal auf dem Notebook			
				String[] argsnew = {"(agtDebugKernelZEnvironmentJAZ)|agtDebugKernelZEnvironmentJAZ","C:\\Lotus\\Notes\\Data\\Javatest.ns5"};
			
				//Versuch auf dem Server zu debuggen
				//String[] argsnew = {"agtDebugKernelJAZ|agtDebugKernelJAZ","Dbs\\fgl\\JAZ-Kernel\\Dev\\Javatest.ns5"};
						
				AgentRunner.main(argsnew);
				//Das "Ende des Agenten wird im Notesmain ausgegeben
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	public void NotesMain() {
			//Variable
			Session objSession = null;
			KernelNotesZZZ objKernelNotes = null;
			Database dbTemp = null;
			String stemp;
			
		Class objClassCur = this.getClass();
				Class objClassSup = objClassCur.getSuperclass();  //herausfinden, obj DebugAgentBase oder AgentBase zugrunde liegt.

		
			//################
			start:
				System.out.println("Start Test Function.");
			
			main:{
				try{
					objSession = getSession();//Folgendes liefert keinen AgentContext !!!  NotesFactory.createSession();
				
					//durch das debug-Flag sollte die objSession-Debug mit dem notesfactory.createsession erzeugt werden.
					//dadurch bekommt man aber kein Context dokument !!!!
				
					//if (objClassSup.getName().toLowerCase().equals("lotus.domino.debugagentbase")){
							// Das gesetzte Debug-Flag bewirkt, dass eine SessionDebug erzeugt wird. Mit der NotesFactory.CreateSession(Username, Password)
							// Diese SessionDebug wird mit den entsprechenden Userangaben versehen.
							// Die SessionDebug wird dann bei jedem Zugriff auf das Session-Objekt verwendet.
							// Auf diese Art kann dann in Eclipse der Zugriff auf andere Datenbanken debuggt werden !!!
					//  objKernel = new KernelNotesZZZ(objSession, null,"FGL","debug");						
					//}else{
				
					//Das ist der Normalfall
					// get agentContext
					AgentContext agentContext = objSession.getAgentContext();

					 // get Handle on Database, the current Agent and the Passed Data
					 Database db = agentContext.getCurrentDatabase();
				
				  //String sSystemNumber =KernelNotesZZZ.computeSystemNumberCustom(objSession);
				   String sSystemNumber = NotesContextProviderZZZ.computeSystemNumber(db);
				   KernelZZZ objKernel = new KernelZZZ("FGL", sSystemNumber, (String)null);
				   objKernelNotes = new KernelNotesZZZ(objSession, objKernel, "", "", null);

					//////////////////////////////		
					stemp = objKernelNotes.getEnvironmentZ("Import","SourcePath");
					System.out.println(stemp);
					
					stemp = objKernelNotes.getEnvironmentZ("Import","SourceFile");
					System.out.println(stemp);
					
					
				}catch(NotesException ne){
					System.out.println(ne.getMessage());
				}catch(ExceptionZZZ ez){
					System.out.println(ez.getDetailAllLast());
				}	
			}//end main
		
			end:{
				System.out.println("End Test Function.");
				if (objClassSup.getName().toLowerCase().equals("lotus.domino.debugagentbase")){
					System.out.println("Ende des Agenten. Main-Part");
					System.exit(0);					
				}	
			}
		} //end function main()

}
