package debug.zNotes.kernel;

import lotus.domino.*;

/**
 * @author 0823
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class DebugSimpleZZZ extends AgentBase {
	public void NotesMain() {
				System.out.println("Start Test Function.");	
				
				Session objSession = getSession();//Folgendes liefert keinen AgentContext !!!  NotesFactory.createSession();
				String stemp = null;
				try {
					stemp = objSession.getNotesVersion();
				} catch (NotesException e) {
				}			
				System.out.println(stemp);
	} 

}
