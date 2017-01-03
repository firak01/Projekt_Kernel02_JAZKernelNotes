package debug.zNotes.kernel;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lotus.domino.Database;
import lotus.domino.NotesException;
import sun.servlet.http.HttpResponse;
import basic.zBasic.ExceptionZZZ;
import basic.zBasic.IConstantZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zKernel.KernelZZZ;
import basic.zNotes.kernel.NotesContextProviderZZZ;
import custom.zKernel.LogZZZ;
import custom.zNotes.kernel.KernelNotesLogZZZ;
import custom.zNotes.kernel.KernelNotesZZZ;

/**Debuggen des NotesKernels bzw. ein Servlet, welches den NotesKernel nutzt.
 * 
 *  1. Erstelle eine .jar Datei mit dem Namen KernelNotesDebugServlet.jar
 *  2. Konfiguriere diese .jar Datei mit dem absoluten Pfad im Serverdokument Reiter
 *  Internet Protocolls / Domino Web Engine
 *  
 *  Abschnitt Servlets / ClassPath
 *  
 *  3. Füge in der datei servlets.properties (notesdata-VErzeichnis auf dem Server) die Zeile hinzu:
 *  servlet.DebugKernelNotes.code=debug.zNotes.Kernel.DebugKernelNotesServletZZZ.class
 *  
 *  erster Teil ist der Action name im HTML-Formular (siehe diese Projekt)
 *  zweiter Teil ist der Pfad zu dieser Klasse (im .jar file)
 *  
 *  Voraussetzung für Servlets ist natürlich ein laufender http-task auf dem Domino - Server
 *  
 * @author lindhaueradmin
 *
 */
public class DebugKernelNotesServletZZZ extends HttpServlet implements IConstantZZZ{
	private KernelZZZ objKernel = null;
	private KernelNotesZZZ objKernelNotes = null;
	
	/** TODO What the method does.
	 * @return void
	 * @param args 
	 * 
	 * lindhaueradmin; 03.10.2006 08:32:14
	 */
	public static void main(String[] args) {
		
		try {
		DebugKernelNotesServletZZZ objServlet = new DebugKernelNotesServletZZZ();
		objServlet.init();
		KernelZZZ objKernel =  new KernelZZZ("KernelNotes", "01", "", "ZKernelConfigKernelNotesDebugServlet.ini",(String) null);
		LogZZZ objLog = objKernel.getLogObject();
		
		//Was sonst im doGet() gemacht wird..... , aber kein HttpRequest und HttpResponse Objekt benötigt
		//NotesKernelObjekt
		NotesContextProviderZZZ objContext = new NotesContextProviderZZZ(objKernel, "NotesKernel", "KernelNotesDebugServlet");
	   KernelNotesZZZ objKernelNotes = new KernelNotesZZZ(objContext, "JAZTest", "01", null);
	    objLog.WriteLineDate("KernelNotesObject initialized.");
		Database dbLog = objKernelNotes.getDBLogCurrent();			
		
		KernelNotesLogZZZ objLogNotes = objKernelNotes.getKernelNotesLogObject();
		objLogNotes.writeLog("Das ist ein Test. Erstellt von einem Servlet");
		
		
		objLog.WriteLineDate("Response creation: Start");
		//res.setContentType("text/html");
		
		System.out.println("<html><head>\n");			
		System.out.println("<title> Domino Servlet KernelZZZ</title>");
		
		System.out.println("</head><body>");			
		System.out.println("FGL rulez" + "<br></br>");
		System.out.println("Parameter aus Kernel-Config File:" + objKernel.getParameter("TestEnvironment1") + "<br/>");
		System.out.println("Name des NotesSession-Users: '" + objKernelNotes.getSession().getUserName() + "'" + "<br/>");
		System.out.println("Titel der Log Datenbank: '"+ dbLog.getTitle() +"'"+ "<br/>");
	

				
		System.out.println("</body></html>");
		
		}catch(NotesException ne){
			System.out.println(ne.text);
			ne.printStackTrace();
		} catch (ExceptionZZZ e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void init(){
		/* Authorisierung des KernelZZZ sicherstellen
		try{
			NotesContextProviderZZZ objContext = new NotesContextProviderZZZ(this.objKernel, "KernelInformationServlet");
		    this.objKernelNotes = new KernelNotesZZZ(objContext, this.objKernel, "JAZTest", "01", null);
			
//			1. Objekt, das dann später an einen ContentWriter übergeben werden kann
			this.objInfo =  new ContentKernelInformationPageZZZ(objKernel);
			//In jedem doGet() erneut ausrechnen   objContentStore.compute();
			
	
			//In jedem doGet() erneut schreiben  objWriterHTML.addContent(objContentStore);

			
		}catch(ExceptionZZZ ez){
			if(this.objKernel!=null){
				LogZZZ objLog = this.objKernel.getLogObject();
				if(objLog!=null){
					objLog.WriteLineDate(ez.getDetailAllLast());
					ez.printStackTrace();
				}else{
					System.out.println(ez.getDetailAllLast()+"\n");
					ez.printStackTrace();
				}
			}else{
				System.out.println(ez.getDetailAllLast()+"\n");
				ez.printStackTrace();
			}
		}
					*/
	}
	
	public void destroy(){
		
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException{
		//Session session = null;
		PrintWriter out = res.getWriter();
		
		try{
			main:{
				try{
			//Wenn die Session Authentifizierung Disabled ist, dann soll hiermit durch das Servlet eine Anmeldung erzwungen werden.
			HttpSession httpSession = null;
			LogZZZ objLog = null;
			if(req.getHeader("Authorization")==null){
				this.objKernel =  new KernelZZZ("KernelNotes", "01", "", "ZKernelConfigKernelNotesDebugServlet.ini",(String) null);
				objLog = objKernel.getLogObject();
				objLog.WriteLineDate("KernelObject initialized.");
				objLog.WriteLineDate("Authorization check: No authorization-header available in HttpServletRequest.");
				
				httpSession = req.getSession(true);
				if(httpSession.isNew()){
					objLog.WriteLineDate("Authorization check: HttpSession available in HttpServletRequest is new !!!.");					
					res.setStatus(HttpResponse.SC_UNAUTHORIZED);
					res.setHeader("WWW-Authentificate", "Basic");
					//res.sendError(401); //SC_UNAUTHORIZED
					//break main;
				}else{
					objLog.WriteLineDate("Authorization check: HttpSession available in HttpServletRequest is NOT new. Previous Authorization ?");				
				}
			}else{
				this.objKernel =  new KernelZZZ("KernelNotes", "01", "", "ZKernelConfigKernelInformationServlet.ini",(String)null);
				objLog = objKernel.getLogObject();
				objLog.WriteLineDate("KernelObject initialized.");
				objLog.WriteLineDate("Authorization check: Authorization available in HttpServletRequest.");
			}
		
			objLog.WriteLineDate("Authorization check: Authorization successfull");
			
			//NotesKernelObjekt
			NotesContextProviderZZZ objContext = new NotesContextProviderZZZ(this.objKernel, this.getClass().getName(), "KernelInformationServlet");
		    this.objKernelNotes = new KernelNotesZZZ(objContext, "JAZTest", "01", null);
		    objLog.WriteLineDate("KernelNotesObject initialized.");
			Database dbLog = this.objKernelNotes.getDBLogCurrent();			
			
			KernelNotesLogZZZ objLogNotes = this.objKernelNotes.getKernelNotesLogObject();
			objLogNotes.writeLog("Das ist ein Test. Erstellt von einem Servlet");
			
			
			objLog.WriteLineDate("Response creation: Start");
			res.setContentType("text/html");
			
			out.println("<html><head>\n");			
			out.println("<title> Domino Servlet KernelZZZ</title>");
			
			out.println("</head><body>");			
			out.println("FGL rulez" + "<br></br>"+
			"Request Method: " + req.getMethod() + "<br/>"  +
			"Parameter CarrierID: " + req.getParameter("carrierid") + "<br/>" +
			"Parameter aus Kernel-Config File:" + this.objKernel.getParameter("TestEnvironment1") + "<br/>" +
			"Name des NotesSession-Users: '" + this.objKernelNotes.getSession().getUserName() + "'" + "<br/>" +
			"Name des HttpServletRequest-RemoteUsers: '" + req.getRemoteUser() + "'" + "<br/>" +
			 "Titel der Log Datenbank: '"+ dbLog.getTitle() +"'"+ "<br/>" +
			"");
					
			out.println("</body></html>");
			
			}catch(NotesException ne){
				ExceptionZZZ ez = new ExceptionZZZ(ne.text, iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
			}//END main:
		}catch( ExceptionZZZ ez){
			LogZZZ objLog = this.objKernel.getLogObject();
			if(objLog!=null){
				objLog.WriteLineDate(ez.getDetailAllLast());
				ez.printStackTrace();
				out.println(ez.getDetailAllLast());
				out.println("</body></html>");
			}else{
				System.out.println(ez.getDetailAllLast());
				ez.printStackTrace();
				out.println(ez.getDetailAllLast());
				out.println("</body></html>");
			}
		}
	}//END doGet
	
	public void doPost(HttpServletRequest req, HttpServletResponse res)throws IOException{
		doGet(req, res);
	}//END doPost
	public KernelZZZ getKernelObject() {
		return objKernel;
	}
	public void setKernelObject(KernelZZZ objKernel) {
		this.objKernel = objKernel;
	}
	public KernelNotesZZZ getKernelNotesObject(){
		return this.objKernelNotes;
	}
	public void setKernelNotesObject(KernelNotesZZZ objKernelNotes){
		this.objKernelNotes = objKernelNotes;
	}
}
