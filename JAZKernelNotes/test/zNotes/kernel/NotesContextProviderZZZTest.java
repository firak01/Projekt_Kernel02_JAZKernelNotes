package zNotes.kernel;

import junit.framework.TestCase;
import lotus.domino.NotesException;
import lotus.domino.Session;
import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zKernel.KernelZZZ;
import basic.zNotes.basic.AgentZZZ;
import basic.zNotes.kernel.NotesContextProviderZZZ;
import custom.zKernel.LogZZZ;

public class NotesContextProviderZZZTest extends TestCase {
	//+++ Test setup
	private static boolean doCleanup = true;		//default = true      false -> kein Aufr�umen um tearDown().
	
	//Kernel und Log-Objekt
	private KernelZZZ objKernel = null;
	private LogZZZ objLog= null;
	
	//Der zu testende Domino Context
	private NotesContextProviderZZZ objContextTest = null;
	

	protected void setUp(){
		try {		
			//Kernel + Log - Object dem TestFixture hinzuf�gen. Siehe test.zzzKernel.KernelZZZTest
			objKernel = new KernelZZZ("Test", "01", "", "ZKernelConfigKernelNotes_test.ini",(String)null);
			objLog = objKernel.getLogObject();
			
			//Hier gibt es sonst keine M�glichkeit den Agentennamen abzufragen
			objContextTest = new NotesContextProviderZZZ(objKernel, this.getClass().getName(), this.getClass().getName());
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
		this.objContextTest.recycle();
		
		
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
		System.out.println(ReflectCodeZZZ.getMethodCurrentName()+"#Start");
		
				//+++ This is not correct when using the test object
				boolean btemp = objContextTest.getFlag("init");
				assertFalse("Unexpected: The init flag was NOT expected to be set", btemp);
	}//END testConstructor
	

	
	public void testUserIdPathHandling(){
		System.out.println(ReflectCodeZZZ.getMethodCurrentName()+"#Start");

		try {
            //0. Lesen des alten Notes-id-Pfades
            String sIDOld = objContextTest.readUserIdPath();
            assertFalse(StringZZZ.isEmpty(sIDOld));
            
//			1. Setzen des Pfades in die Notes.ini
			String sID = objContextTest.getUserIdPath();
			assertFalse(StringZZZ.isEmpty(sID));
			
			//Falls der alte und der neue idstring identisch sind, so wird false zur�ckgeliefert
			boolean bFlagValueChanged = objContextTest.writeUserIdPath(sID);
			if(sIDOld.equals(sID)){
				assertFalse(bFlagValueChanged);
			}else{
				assertTrue(bFlagValueChanged);
			}
			
			//2. Nach dem Setzen des Wertes: Auslesen des Pfads aus der Notes.ini. Dann muss das gleich Ergebnis rauskommen.
			String stemp = objContextTest.readUserIdPath();
			assertEquals(sID, stemp);
			
			//3. Falls der Wert ge�ndert worden ist (ausser der Reihe), so diesen wert erneut �ndern
			if(bFlagValueChanged==true){
				bFlagValueChanged = objContextTest.writeUserIdPath(sIDOld);
				assertTrue(bFlagValueChanged);
			}
			
			
		} catch (ExceptionZZZ ez) {
			fail("Method throws an error: " + ez.getMessageAll());
		}
	}
	
	public void testComputeSystemNumber(){
		System.out.println(ReflectCodeZZZ.getMethodCurrentName()+"#Start");
		try{
			String sSystemNumber = objContextTest.computeSystemNumber();
			assertEquals("01", sSystemNumber);			
		} catch (ExceptionZZZ ez) {
			fail("Method throws an error: " + ez.getMessageAll());
		}
	}
	public void testGetSession(){
		System.out.println(ReflectCodeZZZ.getMethodCurrentName()+"#Start");
		
		try{
			Session objSession = objContextTest.getSession();
			assertNotNull(objSession);
			
			String stemp = objSession.getUserName();
			System.out.println(ReflectCodeZZZ.getMethodCurrentName()+"#Testing for the current session.getUsername()");
			assertEquals("CN=Fritz Lindhauer/O=fgl/C=DE", stemp);
		}catch(NotesException ne){
			fail("Method throws a NotesException: " + ne.text);
		} catch (ExceptionZZZ ez) {
			fail("Method throws an error: " + ez.getMessageAll());
		}
	}
	

	public void testReadingParameterFromConfigFile(){
		System.out.println(ReflectCodeZZZ.getMethodCurrentName()+"#Start");
		
		try {
			assertEquals("db\\fgl\\JAZ-Kernel\\Test\\ZKernel_JavaTest_Application.nsf", objContextTest.getDBCallingPath());
			assertEquals("", objContextTest.getServerCalling());
			assertEquals("c:\\lotus\\notes7", objContextTest.getPathNotesExe());
			assertEquals("c:\\fglkernel\\kernelcontext\\flindhauer_fgl.id", objContextTest.getUserIdPath());
			assertEquals("Fritz Lindhauer/fgl/DE", objContextTest.getUsername());
			assertEquals("", objContextTest.getPassword());
			 
			AgentZZZ objAgent = objContextTest.getKernelAgent();
			assertNotNull(objAgent);
			try{
				assertEquals("test.zNotes.Kernel.NotesContextProviderZZZTest", objAgent.getAgentName() );
			}catch(NotesException ne){
				fail("Method throws a NotesException: " + ne.text);
			}
		} catch (ExceptionZZZ ez) {
			fail("Method throws an error: " + ez.getMessageAll());
		}
	}
}//END Class
