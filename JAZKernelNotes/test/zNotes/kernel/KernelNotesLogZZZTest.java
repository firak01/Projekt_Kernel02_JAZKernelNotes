package zNotes.kernel;

import junit.framework.TestCase;
import lotus.domino.Database;
import lotus.domino.NotesException;
import basic.zBasic.ExceptionZZZ;
import basic.zKernel.KernelZZZ;
import basic.zNotes.kernel.NotesContextProviderZZZ;
import custom.zKernel.LogZZZ;
import custom.zNotes.kernel.KernelNotesLogZZZ;
import custom.zNotes.kernel.KernelNotesZZZ;

public class KernelNotesLogZZZTest extends TestCase {
	//+++ Test setup
	private static boolean doCleanup = true;		//default = true      false -> kein Aufr�umen um tearDown().
	
	//Kernel und Log-Objekt
	private KernelZZZ objKernel = null;
	private LogZZZ objLog= null;
	
	//Der zu testende Domino Context
	private KernelNotesZZZ objKernelNotes=null;
	
	//Objekt, das getestet werden soll
	private KernelNotesLogZZZ objLogTest=null;

	protected void setUp(){
		try {		
			//Kernel + Log - Object dem TestFixture hinzuf�gen. Siehe test.zzzKernel.KernelZZZTest
				objKernel = new KernelZZZ("TEST", "01", "", "ZKernelConfigKernelNotes_test.ini",(String) null);
				objLog = objKernel.getLogObject();
			
			/*Wird nun im Konstruktor des NotesKernel-Objekts gemacht
			//Notes-Context
			objNotesTestProvider = new NotesContextProviderZZZ(objKernel);
			Session objSession= objNotesTestProvider.getSession();
			Database objDB = objNotesTestProvider.getDb();
			System.out.println("Test setUp(): Database used for context '" + objDB.getTitle() + "'");
			*/
			
			//### Die TestObjecte						
			//+++The main object used for testing:
//			TODO GOON: Handle auf die zu verwendende Notesdatenbank bereitstellen & und dies ber�cksichtigen
		//IM Construktor-Test m�ssen dann ggf, die verschiedenen Wege simuliert werden.
			//a) per DB, sprich eine DB explizit �bergeben.
			//b) per Session.currentdatabase, spich einen Context aufbauen.	
				NotesContextProviderZZZ objContext = new NotesContextProviderZZZ(objKernel, this.getClass().getName(), "ServletTest");
				
				objKernelNotes= new KernelNotesZZZ(objContext , "JAZTest", "01", null);
				objLogTest = objKernelNotes.getKernelNotesLogObject();

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
		/*
		try{
		DJAgentContext objContext = this.objKernelNotes.getAgentContextCurrent();
		if (objContext != null){			
			objContext.recycle();  //erledigt implizit das nlDoc.recycle(); laut Buch
		}
		
		this.objLogTest=null;
		this.objKernelNotes=null;
		this.objLog=null;
		this.objKernel=null;
		
		}catch(ExceptionZZZ ez){
			System.out.println(ez.getDetailAllLast());
		} catch (NotesException ne) {
			ne.printStackTrace();
		}
		*/
	}
	
	
	//###################################################
	//Die Tests
	
	public void testContructor(){
		
		//try{
				//+++ Hier wird ein Fehler erwartet
				KernelNotesZZZ objKernelNotesTemp=null;
				KernelNotesLogZZZ objKernelNotesLogTemp = null;
				
				/*   //*DAs gibt unerw�nschte Ausgaben aus. WARUM ???
				try{
					String[] saFlag={"init"};
					objKernelNotesTemp = new KernelNotesZZZ( objKernel,"","", saFlag);
					//Der Handle auf die NotesDB muss fehlschlagen......
					objKernelNotesLogTemp = new KernelNotesLogZZZ(objKernelNotesTemp, (String)null);
					fail("An error was expected, but nothing happend.");
				}catch(ExceptionZZZ ez){
					//Dieser Fehler wird erwartet
					//objKernelNotesTemp=null;
					//objKernelNotesLogTemp=null;
				}
		
			
				//+++An object just initialized
				String[] saFlag={"init"};
				objKernelNotesTemp = new KernelNotesZZZ( objKernel,"","", saFlag);
				boolean btemp = objKernelNotesTemp.getFlag("init");
				assertTrue("Unexpected: The init flag was expected to be set", btemp);
				DJAgentContext objContext = objKernelNotesTemp.getAgentContextCurrent();
				if (objContext != null){			
					objContext.recycle();  //erledigt implizit das nlDoc.recycle(); laut Buch
				}
				objKernelNotesTemp = null;
						*/
				
				//+++ This is not correct when using the test object
				boolean btemp = objLogTest.getFlag("init");
				assertFalse("Unexpected: The init flag was expected NOT to be set", btemp);
				
		/*		
		}catch(ExceptionZZZ ez){
			fail("Method throws an exception." + ez.getMessageLast());
		}catch(NotesException ne){
			fail("Method throws a NotesException." + ne.text);
		}
		*/
	}//END testConstructor
//*/

	public void testGetAgentCallingName(){
		try{
			String sAgentName = objLogTest.getAgentCallingName();
			assertEquals("ServletTest", sAgentName);
		}catch(ExceptionZZZ ez){
			fail("Method throws an exception." + ez.getMessageLast());
		}
	}
	

	public void testRefreshLogLevelGlobal(){
		try{
			int itemp = objLogTest.refreshLogLevelGlobal();
			assertEquals(3,itemp);
		}catch(ExceptionZZZ ez){
			fail("Method throws an exception." + ez.getMessageLast());
		}
	}
	
	public void testGetDBLog(){
		try{
			Database dbLog = objLogTest.getDBLog();
			assertNotNull(dbLog);
			
			String stemp = dbLog.getTitle();
			assertEquals("ZKernel - Java Test - Log [Keine Entwicklung]",stemp);
		}catch(NotesException ne){
			fail("Method throws a NotesException. " + ne.text);
		}catch(ExceptionZZZ ez){
			fail("Method throws an exception." + ez.getMessageLast());
		}
	}
	
	
	public void testWriteLog(){
		try{
			//1. Mal den gleichen Eintrag schreiben
			boolean btemp = objLogTest.writeLog("das ist ein test");
			assertTrue(btemp);			
			String stemp = objLogTest.getLogMessageLast();
			assertEquals("das ist ein test", stemp);
			
			assertEquals(3, objLogTest.getMuteCounterLimit());
			
			//TODO GOON: Z�hler einbauen, der die Anzahl der Log-Eintr�ge z�hlt und maximal x-eintr�ge erlaubt. Das soll ein �berquellen des Logs-holen, in z.B. einer Endlosschleife.
			int itemp = objLogTest.getMuteCounter();
			assertEquals(1, itemp);
			
			//2. Mal den gleichen Eintrag schreiben
			btemp = objLogTest.writeLog("das ist ein test");
			assertTrue(btemp);
			itemp = objLogTest.getMuteCounter();
			assertEquals(2, itemp);
			
			//3. Mal den gleichen Eintrag schreiben
			btemp = objLogTest.writeLog("das ist ein test");
			assertTrue(btemp);
			itemp = objLogTest.getMuteCounter();
			assertEquals(3, itemp);
			
			//4. Mal den gleichen Eintrag schreiben
			btemp = objLogTest.writeLog("das ist ein test");
			assertFalse(btemp);                                               //das schreiben wird unterdr�ckt
			itemp = objLogTest.getMuteCounter();
			assertEquals(3, itemp);                                           //der Z�hler darf nicht erh�ht werden
			
			
			
			
			
		}catch(ExceptionZZZ ez){
			fail("Method throws an exception." + ez.getMessageLast());
		}
	}
	
}//END Class KernelNotesLogZZZTest
