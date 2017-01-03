package basic.zNotes.basic.util;

import java.io.PrintWriter;
import java.util.Vector;

import basic.zBasic.util.math.RandomZZZ;
import basic.zNotes.basic.GC;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.Item;
import lotus.domino.Name;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.ViewEntry;
import lotus.domino.ViewNavigator;

public class NotesToolZZZ {
	private static final int NUM_OF_DOC_ITEMS = 10;		// Anzahl von (dummy) Items in den Dokumenten
	private static final int SIZE_OF_DOC_ITEMS = 100;	// Datengröße in diesen Items in Byte.
	
	private NotesToolZZZ(){
		//Zum "Verstecken" des Konstruktors
	}



/**
 * @see createDoc (Database, String, String, String, Document)
 * @param db
 * @param cat
 * @param title
 * @param parent
 * @return
 * @throws NotesException
 */
public static Document createDoc (Database db, String cat, String title, Document parent) throws NotesException {
	return (createDoc(db,"FO_dokument_k6",cat,title,parent));
}

/**
 * Erzeugt ein neues (Test-) Dokument. Das Dokument wird durch das Item
 * F_marker_k6=1 markiert, damit es in cleanup wieder gelöcht werden kann.
 * @see cleanup (Database)
 * @param db - Datenbank, in der das Document erstellt werden soll,
 * @param form - Maskenname für dieses Document (item Form)
 * @param cat - Inhalt des Items "F_Category"
 * @param title - Inhalt des Items "F_titel"
 * @param parent - Optionales Mutterdokument (dieses neue Doc wird
 * 		Antwortdokument). Darf auch null sein.
 * @return - neue Document
 * @throws NotesException
 */
public static Document createDoc (Database db, String form, String cat, String title, Document parent) throws NotesException {
	Document doc = db.createDocument();
	doc.replaceItemValue ("Form", form);
	doc.replaceItemValue ("F_Category", cat);
	doc.replaceItemValue ("F_titel", title);
	//Anhand dieses Markers kann erkannt werden, dass createDoc ein Dokument erstellt hat.
	//Wird fürs Aufräumen benötigt - siehe cleanup.
	doc.replaceItemValue ("F_marker_k6",new Integer (1));
	for (int i = 0; i < NUM_OF_DOC_ITEMS; i++) {
		doc.replaceItemValue ("Item_" + i, RandomZZZ.getRandomString(SIZE_OF_DOC_ITEMS));
	}
	if (parent != null) {
		doc.makeResponse(parent);
	}
	doc.save (true,false);
	return doc;
}

/**
 * Löscht alle Dokumente der Datenbank <b>db</b>, für die F_marker_k6=1.
 * @param db
 * @throws NotesException
 */
private static final void cleanup (Database db) throws NotesException {
	DocumentCollection col = null;
	try {
		col = db.search ("F_marker_k6=1");
		System.out.println ("Aufräumen: Lösche " + col.getCount() + " Dokumente.");
		col.removeAll(true);
	} finally {
		if (col!=null) {
			col.recycle();
		}
	}
}

/**
 * gibt ein Document als Name-Value Paare auf die Console aus.
 * @param doc
 * @throws NotesException
 */
public static final void dumpDoc (Document doc) throws NotesException {
	Vector v = doc.getItems();
	for (int i=0, s=v.size(); i<s; i++) {
		Item item = (Item)v.elementAt(i);
		System.out.print (" " + item.getName()+"=");
		Vector val=item.getValues();
		System.out.println (val!=null?val.toString():"null");
	}
}

/**
 * Gibt ein Document als Name-Value Paare auf einen PrintWriter aus.
 * @param doc
 * @param out
 * @throws NotesException
 */
public static final void dumpDoc (Document doc, PrintWriter out) throws NotesException {
	Vector v = doc.getItems();
	for (int i=0, s=v.size(); i<s; i++) {
		Item item = (Item)v.elementAt(i);
		out.print (" " + item.getName()+"=");
		Vector val=item.getValues();
		out.println (val!=null?val.toString():"null");
	}
}


/**
 * @see getServerName (Session, boolean)
 * @param mSession
 * @return - getServerName (mSession, false)
 */
public static final String getServerName(Session mSession) {
	return getServerName (mSession, false);
}

/**
 * liefert den Servernamen des tatsächlichen Servers auf dem die Session läuft.
 * Im Gegensatz zu session.getServerName, liefert diese Methode auch den korrekten
 * Servernamen (und nicht ""), auf einer lokalen serverbasierten Session.
 * @param mSession
 * @param abbreviated - falls true, wird der Name vereinfacht, also ohne das CN= usw. zurückgegeben.
 * @return - den kanonischen oder vereinfachten (abbreviated) Servernamen des Servers, auf den die Session sich bezieht oder "Exception_noServerFound" im Fehlerfall.
 */
public static final String getServerName(Session mSession, boolean abbreviated) {
	try {
		String servername = null;
		Name n = mSession.getUserNameObject();
		if (n.isHierarchical()) {
			if (abbreviated) {
				servername = n.getAbbreviated();
			} else {
				servername = n.getCanonical();
			}
		} else {
			System.err.println ("Konnte den Servernamen nicht kanonisch aufloesen.");
		}
		return servername;
	}
	catch (NotesException e) {
		e.printStackTrace();
		return "Exception_noServerFound";
	}
}


/**
 * Einfache Ausgabe Routine für ViewNavigator
 * @param nav
 */
public static final void printNavigator (ViewNavigator nav) {
	ViewEntry entry = null, nextEntry = null;
	try {
		entry = nav.getFirst();
		while (entry != null) {
			if (entry.isDocument()) {
				System.out.println ("\tDokument: " + entry.getDocument().getItemValueString("F_titel"));
			}
			if (entry.isCategory()) {
				System.out.println ("Kategorie: " + entry.getColumnValues().elementAt(0));
			}
			if (entry.isTotal()) {
				System.out.println ("Total: " + entry.getColumnValues().elementAt(3));
			}
			if (entry.isConflict()) {
				System.out.println ("\t\tSpeicherkonflikt: " + entry.getDocument().getItemValueString("F_titel"));
			}
			nextEntry = nav.getNext();
			GC.recycle(entry);
			entry=nextEntry;
		}
	} catch (NotesException e) {
		e.printStackTrace();
	} finally {
		GC.recycle (entry);
		GC.recycle (nextEntry);
	}
}

}//END class
