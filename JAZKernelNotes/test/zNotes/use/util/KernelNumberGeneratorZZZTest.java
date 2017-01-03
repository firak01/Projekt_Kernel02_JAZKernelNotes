package zNotes.use.util;

import junit.framework.TestCase;
import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zKernel.KernelZZZ;
import basic.zNotes.kernel.NotesContextProviderZZZ;
import basic.zNotes.use.util.KernelNumberGeneratorZZZ;
import custom.zNotes.kernel.KernelNotesLogZZZ;
import custom.zNotes.kernel.KernelNotesZZZ;

public class KernelNumberGeneratorZZZTest extends TestCase{
//	+++ Test setup
	private static boolean doCleanup = true;		//default = true      false -> kein Aufr�umen um tearDown().
	
	//Kernel und Log-Objekt
	private KernelZZZ objKernel = null;
	private NotesContextProviderZZZ objContextNotes = null;
	private KernelNotesZZZ objKernelNotes = null;
	
	//Das zu testende Objekt
	private KernelNumberGeneratorZZZ objNumberTest = null;
 
	
	protected void setUp(){
		try {		
//			Kernel + Log - Object dem TestFixture hinzuf�gen. Siehe test.zzzKernel.KernelZZZTest
			this.objKernel = new KernelZZZ("TEST", "01", "", "ZKernelConfigKernelNotes_test.ini",(String)null);
			this.objContextNotes = new NotesContextProviderZZZ(objKernel, this.getClass().getName(), this.getClass().getName());
			this.objKernelNotes = new KernelNotesZZZ(objContextNotes, "JAZTest", "01", null);
			
			this.objNumberTest = new KernelNumberGeneratorZZZ(this.objKernelNotes);
						
		}catch(ExceptionZZZ ez){
			fail("Method throws an exception." + ez.getMessageLast());
		}
	
	}//END setup
	
	public void tearDown() throws Exception {
		if(doCleanup){
			cleanUp();
		}
	}
	
	/**************************************************************************/
	/**** diese Aufr�um-Methode muss mit Leben gef�llt werden *****************/
	/**************************************************************************/
	private void cleanUp() {
		//do your cleanup Work
		//this.objContextTest.recycle();
		
		
		//Merke: Es wird bei Erzeugung des DJAgentContext immer ein Noesdocument k�nstlich erzeugt. 
		//           Dies kann man ggf. hier l�schen.
		/*
		if (nlDoc != null) {
			try {
				nlDoc.remove(true);
			} catch (NotesException e) {
				e.printStackTrace();
			}
		}
		*/
	}
	
	
	//###################################################
	//Die Tests
	public void testContructor(){
		
		try{
				//+++ Hier wird ein Fehler erwarte
				
				//+++ This is not correct when using the test object
				boolean btemp = objNumberTest.getFlag("init");
				assertFalse("Unexpected: The init flag was expected NOT to be set", btemp);
				
				//+++ Nun eine Log-Ausgabe (Notes-Log)
				KernelNotesLogZZZ objKernelNotesLog = objNumberTest.getKernelNotesLogObject();
				assertNotNull(objKernelNotesLog);				
				objKernelNotesLog.writeLog("succesfully created", this, ReflectCodeZZZ.getMethodCurrentName(), 3);
					
		}catch(ExceptionZZZ ez){
			fail("Method throws an exception." + ez.getMessageLast());
		//}catch(NotesException ne){
		//	fail("Method throws a NotesException." + ne.text);
		}
	
	}//END testConstructor

	
	public void testReadGenerateNumber(){
		try{
			//Die alte Nummer auslesen
			String sNumberCur = objNumberTest.readNumberCurrent("Carrier");
			assertNotNull(sNumberCur);
			assertFalse(sNumberCur.equals(""));
			
			//Eine Nummer erstellen (das ist ein String Datentyp)
			String stemp = objNumberTest.generateNumberNew("Carrier");
			assertNotNull(stemp);
			assertFalse(stemp.equals(""));
			assertFalse(stemp.equals(sNumberCur));
			
						
		}catch(ExceptionZZZ ez){
			fail("Method throws an exception." + ez.getMessageLast());
		}
	}
}
