import junit.framework.Test;
import junit.framework.TestSuite;
import zNotes.document.DocumentZZZTest;
import zNotes.kernel.KernelNotesLogZZZTest;
import zNotes.kernel.KernelNotesZZZTest;
import zNotes.kernel.NotesContextProviderZZZTest;

public class KernelNotesAllTest {
	public static Test suite(){
		TestSuite objReturn = new TestSuite();
		//Merke: Die Tests bilden in ihrer Reihenfolge in etwa die Hierarchie im Framework ab. 
		//            Dies beim Einf�gen weiterer Tests bitte beachten.         
		 
		objReturn.addTestSuite(KernelNotesLogZZZTest.class);
		objReturn.addTestSuite(KernelNotesZZZTest.class);
		objReturn.addTestSuite(NotesContextProviderZZZTest.class);
		objReturn.addTestSuite(DocumentZZZTest.class);

		return objReturn;
	}
	
	/**
	 * Hiermit eine Swing-Gui starten.
	 * Das ist bei eclipse aber nicht notwendig, au�er man will alle hier eingebundenen Tests durchf�hren.
	 * @param args
	 */
	public static void main(String[] args) {
		//Ab Eclipse 4.4 ist junit.swingui sogar nicht mehr Bestandteil des Bundles
		//also auch nicht mehr unter der Eclipse Variablen JUNIT_HOME/junit.jar zu finden
		//junit.swingui.TestRunner.run(KernelNotesAllTest.class);
	}

}
