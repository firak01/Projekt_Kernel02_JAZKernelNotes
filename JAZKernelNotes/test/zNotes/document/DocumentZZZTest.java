package zNotes.document;

import basic.zBasic.ExceptionZZZ;
import basic.zNotes.document.DocumentZZZ;
import junit.framework.TestCase;

public class DocumentZZZTest extends TestCase{
	
    public void testIsValidFieldname(){
    	String sFieldname = null;
    	try{
    		boolean bErg=false;
    		sFieldname = "";    
    		try{  
    			bErg = DocumentZZZ.isValidFieldname(sFieldname); //Beim debuggen dauert das erstmalige ausf�hren seeeehr lange. warum ????  
    			fail("Method was expected to throw an error. Fieldname: '" + sFieldname + "'");
    		}catch(ExceptionZZZ ez){
    			//alles o.k.
    		}
    		
    		//+++ L�nge pr�fen
    		for(int iLength=1;iLength <= 34; iLength++){
    			sFieldname="";  //Merke: Das ist nicht der beste Algorithmus. Aber er trennt sehr sch�n zwischen Erstellung der Testeingabe und der Auswertung !!!
	    		for (int icount = 1; icount <= iLength; icount++){
	    			sFieldname += "a";
	    		}
	    		
	    		bErg = DocumentZZZ.isValidFieldname(sFieldname);
	    		if(iLength<=DocumentZZZ.iFIELDNAME_LENGTH){
	    			assertTrue("Feldname: '" + sFieldname + "'", bErg);
	    		}else{
	    			assertFalse("Feldname: '" + sFieldname + "'",bErg);
	    		}
    		}
    		
    		//+++ Andere Buchstaben
    		//Leerzeichen
    		sFieldname = "a a";
    		bErg = DocumentZZZ.isValidFieldname(sFieldname);
    		assertFalse("Feldname: '" + sFieldname + "'",bErg);
    		
    		//Punkt
    		sFieldname = "a.a";
    		bErg = DocumentZZZ.isValidFieldname(sFieldname);
    		assertTrue("Feldname: '" + sFieldname + "'",bErg);
    		
    		//Minus
    		sFieldname = "a-a";
    		bErg = DocumentZZZ.isValidFieldname(sFieldname);
    		assertFalse("Feldname: '" + sFieldname + "'",bErg);
    		
    		
    		
    	} catch (ExceptionZZZ ez) {
    		fail("Method throws an exception." + ez.getMessageLast() + " at variable 'fieldname' = '" + sFieldname + "'");
    	} 
    }
}
