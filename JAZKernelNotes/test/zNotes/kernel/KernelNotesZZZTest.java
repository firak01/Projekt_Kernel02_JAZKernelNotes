package zNotes.kernel;

import java.io.File;

import junit.framework.TestCase;
import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.NotesThread;
import lotus.domino.Session;
import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zKernel.KernelZZZ;
import basic.zNotes.kernel.NotesContextProviderZZZ;
import custom.zKernel.LogZZZ;
import custom.zNotes.kernel.KernelNotesZZZ;

public class KernelNotesZZZTest extends TestCase {
	//+++ Test setup
	private static boolean doCleanup = true;		//default = true      false -> kein Aufr�umen um tearDown().
	
	//Kernel und Log-Objekt
	private KernelZZZ objKernel = null;
	private LogZZZ objLog= null;
	
	//Der zu testende Domino Context
	private NotesContextProviderZZZ objNotesTestProvider = null;
	
	//Objekt, das getestet werden soll
	private KernelNotesZZZ objKernelNotesTest;
	private Document docCreatedNew=null;
	 
	protected void setUp(){
		try {		
			//Kernel + Log - Object dem TestFixture hinzuf�gen. Siehe test.zzzKernel.KernelZZZTest
			objKernel = new KernelZZZ("Test", "01", "", "ZKernelConfigKernelNotes_test.ini",(String) null);
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
			 NotesContextProviderZZZ objContext = new NotesContextProviderZZZ(objKernel,  this.getClass().getName(), this.getClass().getName());			
			objKernelNotesTest= new KernelNotesZZZ(objContext, "JAZTest", "01", null);
						
			/*
			//++ Proxy aufbauen'
			String sProxyHost = objKernel.getParameter("ProxyHost");
			if(StringZZZ.isEmpty(sProxyHost)){
				
			}else{
				String sProxyPort = objKernel.getParameter("ProxyPort");
				
				System.setProperty( "proxySet", "true" );
				System.setProperty( "proxyHost", sProxyHost);
				System.setProperty( "proxyPort", sProxyPort );
			}*/
			
		}catch(ExceptionZZZ ez){
			fail("Method throws an exception." + ez.getMessageLast());
		}
	
	}//END setup
	
	public void tearDown() throws Exception {
		NotesThread.stermThread();
		if(doCleanup){
			cleanUp();
		}
	}
	
	/**************************************************************************/
	/**** diese Aufr�um-Methode muss mit Leben gef�llt werden *****************/
	/**************************************************************************/
	private void cleanUp() {
		//do your cleanup Work
		try{
		if(docCreatedNew!=null)docCreatedNew.recycle();
		}catch(NotesException ne){
			System.out.println(ReflectCodeZZZ.getMethodCurrentName() + "#" + ne.text);
		}
	}
	
	
	//###################################################
	//Die Tests
	public void testContructor(){
		
	/* Wenn man das einkommentiert, werden auf der Console Fehler ausgegeben
		try{
				//+++ Hier wird ein Fehler erwartet
				try{
					KernelNotesZZZ objKernelNotesTemp = new KernelNotesZZZ( "BlaAgent", objKernel,"denKeyGibtsNicht","", null);
					fail("An error was expected, but nothing happende.");
				}catch(ExceptionZZZ ez){
					//Dieser Fehler wird erwartet
				}

			
				//+++An object just initialized
				String[] saFlag={"init"};
				KernelNotesZZZ objKernelNotesTemp = new KernelNotesZZZ( "blaagent", objKernel,"","", saFlag);
				boolean btemp = objKernelNotesTemp.getFlag("init");
				assertTrue("Unexpected: The init flag was expected to be set", btemp);
   */
			
				//+++ This is not correct when using the test object
				boolean btemp2 = objKernelNotesTest.getFlag("init");
				assertFalse("Unexpected: The init flag was expected NOT to be set", btemp2);
				
	/*	
		}catch(ExceptionZZZ ez){
			fail("Method throws an exception." + ez.getMessageLast());
		}
	*/
	}//END testConstructor
	
	public void testGetSystemNumber(){
		try {
			String stemp = objKernelNotesTest.getSystemNumber();
			assertEquals("01", stemp);
		} catch (ExceptionZZZ ez) {
			fail("Method throws an exception." + ez.getMessageLast());
		}
		
	}//END Test GetSystemNumber
	
	public void testProofModuleIsConfigured(){
		try{
			//Pr�fen, ob das Modul konfiguriert ist.
			//NotesContextProviderZZZ objContext = objKernelNotesTest.getNotesContextProvider(); 
			//String sModule = objContext.getModuleName();
			String sModule = this.getClass().getName();
			boolean btemp = objKernel.proofModuleFileIsConfigured(sModule);
			assertTrue(btemp);
		}catch(ExceptionZZZ ez){
			fail("Method throws an exception." + ez.getMessageLast());
		}
	}
	
	/**Merke: Das ApplicationStoreDokument ist der "Ort", an dem dynamische Informationen gespeichert werden.
	 *             z.B. fortlaufende Nummern.
	 *             
	 *             
	 * @return void
	 *
	 * javadoc created by: 0823, 08.12.2006 - 07:41:40
	 */
	public void testGetApplicationStore(){
		try{
			Document docStore = objKernelNotesTest.getApplicationStore();
			assertNotNull(docStore);
			
			
		}catch(ExceptionZZZ ez){
			fail("Method throws an exception." + ez.getMessageLast());
		}
	}
	
	public void testCreateAccessFieldAllDefault(){
		try{
		//Zu Testzwecken ein Dokument in der ApplicationDB erstellen und mit den Feldern versorgen.
		Session session = this.objKernelNotesTest.getSession();
		Database db = this.objKernelNotesTest.getDBApplicationCurrent();
		 
		this.docCreatedNew = db.createDocument();         //globales Objekt !!!
		this.docCreatedNew.replaceItemValue("Form", "frmTest");
		this.docCreatedNew.replaceItemValue("TestDescription", "In diesem Dokument wurden default Leser und Autorenfelder erstellt und mit Rollen gef�llt.");
		
		String sApplicationKey = this.objKernelNotesTest.getApplicationKeyCurrent();
		KernelNotesZZZ.createAccessFieldAllDefault(session, this.docCreatedNew, sApplicationKey);
		this.docCreatedNew.save();  //merke: wird im teardown wieder recycled
		System.out.println("New document ("+this.docCreatedNew.getUniversalID()+") saved in '"+ db.getFilePath() + "'");
		
		}catch(NotesException ne){
			fail("Method throws a NotesException: " + ne.text);
		}catch(ExceptionZZZ ez){
			fail("Method throw an ExceptionZZZ: " + ez.getDetailAllLast());
		}
	}
	
	
	public void testGetAgentDetailXYZ(){
		try{
			//Also: Den Modulalias �bergeben und anhand des aktullen "gefakten" Agentennamens den Namen der Section erhalten
			String stemp = objKernelNotesTest.getAgentAliasCurrentZ(this.getClass().getName());
			assertEquals("MyFakedAgent", stemp);
			
			stemp = objKernelNotesTest.getAgentParameterCurrentZ(this.getClass().getName(), "NotAvailableParameter");
			assertNull("A parameter which is not configured should return 'null' as value", stemp);
			
			stemp = objKernelNotesTest.getAgentParameterCurrentZ(this.getClass().getName(), "TestParameter1");
			assertEquals(stemp, "TestParameterValue1");
			
			
		} catch (ExceptionZZZ ez) {
			fail("Method throws an exception." + ez.getMessageLast());
		}
	}
	
	public void testGetEnvironmentZ(){
		try{
			String stemp = objKernelNotesTest.getEnvironmentZ(this.getClass().getName(), "NotAvailableEnvironment");
			assertNull("A parameter which is not configured should return 'null' as value.", stemp);
			
			 stemp = objKernelNotesTest.getEnvironmentZ(this.getClass().getName(), "TestEnvironment1");
			assertEquals("TestEnvironmentValue1", stemp);
		} catch (ExceptionZZZ ez) {
			fail("Method throws an exception." + ez.getMessageLast());
		}
	}
	
	public void testGetDBByAlias(){
		try{
			Database dbtemp = objKernelNotesTest.getDBByAlias("Application","");
			assertNotNull("unable to receive database by alias 'Application'", dbtemp);
			
			
			String stemp = dbtemp.getTitle();
			assertEquals("ZKernel - JavaTest - Application [Keine Entwicklung]", stemp);
		} catch (NotesException ne) {
			fail("Method throws a notes exception. " + ne.text);
		} catch (ExceptionZZZ ez) {
			fail("Method throws an exception." + ez.getMessageLast());
		}
	}//END testGetDB
	
	public void testGetPathDirectoryDefault(){
		try{
			//Dieser Pfad ist von Notes-Installation zu NotesInstallation unterschiedlich. Es sollte nur immer fileZZZ am Schluss stehen
			String stemp = objKernelNotesTest.getPathDirectoryDefault();
			//System.out.println(stemp);
			
			String stemp2 = objKernelNotesTest.getNotesContextProvider().getPathDirectoryDefault();
			if(StringZZZ.isEmpty(stemp2)){
				boolean btemp = stemp.endsWith("fileZZZ");
				assertTrue("unexpected directory as default directory of the notes-kernel object (default, non configureded case).", btemp);	
			}else{
				assertEquals("unexpected: directory configured in the configuration of the notescontext must be equal to the testresult (configured case).",stemp, stemp2);
			}
			
			File objFile = new File(stemp);
			if (objFile.exists()==false){
				fail("Das Verzeichnis '"+stemp+"' ist zwar konfiguriert, existiert aber nicht.");
			}
			
			
			
		} catch (ExceptionZZZ ez) {
			fail("Method throws an exception." + ez.getMessageLast());
		}
	}//END testGetPathDirectoryDefault()

	public void testHasAgentContextBySession(){
		try{
			boolean btemp = objKernelNotesTest.hasAgentContextBySession();
			assertFalse(btemp); //Hier in den Unit Tests k�nnen wir den AgentContext nur simulieren. Jedes andere Ergebnis w�re dann wohl ein Fehler.
		} catch (ExceptionZZZ ez) {
			fail("Method throws an exception." + ez.getMessageLast());
		}
	}
	
}//END class KernelNotesZZZTest
