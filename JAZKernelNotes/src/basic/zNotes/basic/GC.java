package basic.zNotes.basic;
import lotus.domino.*;

/** Helferklasse zum recycle von Domino Objekten.
 *  Mögliche NotesExceptions werden gecatched, um das recycle einfacher im finally einsetzen zu können.
 *  Die Methoden geben immer null zurück, so kann ein recycltes Objekt einfacher null gesetzt werden.
 *  z.B. doc=GC.recyle(doc);
 *  Die null-Zuweisung ist hilfreich, damit nicht versehentlich mit einem recycleten Notesobjekt 
 *  weitergearbeitet wird, da das Verhalten von recycleten Objekten nicht vorhersehbar ist,
 *  NullPointerExceptions jedoch sicher behandelt werden können.
 * 
 * @author Thomas Ekert
 *
 */
public class GC {

	private GC() {
		//Hide from Public
	}

	public static Session recycle(Session session) {
		try {
			if (session != null) {
				session.recycle();
			}
		} catch (NotesException notesexception) {
			throw new RuntimeException ("Fatal: Notes Exception while recycling Session.");
		}
		return null;
	}
	public static Session recycle(Form form) {
		try {
			if (form != null) {
				form.recycle();
			}
		} catch (NotesException notesexception) {
			throw new RuntimeException ("Fatal: Notes Exception while recycling Form.");
		}
		return null;
	}
	public static Session recycle(ViewColumn viewColumn) {
		try {
			if (viewColumn != null) {
				viewColumn.recycle();
			}
		} catch (NotesException notesexception) {
			throw new RuntimeException ("Fatal: Notes Exception while recycling ViewColumn.");
		}
		return null;
	}

	public static ViewEntry recycle(ViewEntry viewentry) {
		try {
			if (viewentry != null) {
				viewentry.recycle();
			}
		} catch (NotesException notesexception) {
			throw new RuntimeException ("Fatal Error while recycling ViewEntry.");
		}
		return null;
	}
	
	public static ViewNavigator recycle(ViewNavigator viewnavigator) {
		try {
			if (viewnavigator != null) {
				viewnavigator.recycle();
			}
		} catch (NotesException notesexception) {
			throw new RuntimeException ("Fatal Error while recycling ViewNavigator.");
		}
		return null;
	}

	public static ViewEntryCollection recycle(
			ViewEntryCollection viewentrycollection) {
		try {
			if (viewentrycollection != null) {
				viewentrycollection.recycle();
			}
		} catch (NotesException notesexception) {
			throw new RuntimeException ("Fatal Error while recycling ViewEntryCollection.");
		}
		return null;
	}

	public static DocumentCollection recycle(
			DocumentCollection documentcollection) {
		try {
			if (documentcollection != null) {
				documentcollection.recycle();
			}
		} catch (NotesException notesexception) {
			throw new RuntimeException ("Fatal Error while recycling DocumentCollection.");
		}
		return null;
	}

	public static Document recycle(Document document) {
		try {
			if (document != null) {
				document.recycle();
			}
		} catch (NotesException notesexception) {
			throw new RuntimeException ("Fatal Error while recycling Document.");
		}
		return null;
	}

	public static View recycle(View view) {
		try {
			if (view != null) {
				view.recycle();
			}
		} catch (NotesException notesexception) {
			throw new RuntimeException ("Fatal Error while recycling View.");
		}
		return null;
	}

	public static DbDirectory recycle(DbDirectory dbdirectory) {
		try {
			if (dbdirectory != null) {
				dbdirectory.recycle();
			}
		} catch (NotesException notesexception) {
			throw new RuntimeException ("Fatal Error while recycling DbDirectory.");
		}
		return null;
	}

	public static Database recycle(Database database) {
		try {
			if (database != null) {
				database.recycle();
			}
		} catch (NotesException notesexception) {
			throw new RuntimeException ("Fatal Error while recycling Database.");
		}
		return null;
	}

	public static Agent recycle(Agent agent) {
		try {
			if (agent != null) {
				agent.recycle();
			}
		} catch (NotesException notesexception) {
			throw new RuntimeException ("Fatal Error while recycling Agent.");
		}
		return null;
	}

	public static RichTextItem recycle(RichTextItem richtextitem) {
		try {
			if (richtextitem != null) {
				richtextitem.recycle();
			}
		} catch (NotesException notesexception) {
			throw new RuntimeException ("Fatal Error while recycling RichtextItem.");
		}
		return null;
	}
	
	public static RichTextItem recycle(RichTextTable richtexttable) {
		try {
			if (richtexttable != null) {
				richtexttable.recycle();
			}
		} catch (NotesException notesexception) {
			throw new RuntimeException ("Fatal Error while recycling RichTextTable.");
		}
		return null;
	}
	
	public static RichTextItem recycle(RichTextNavigator richtextnav) {
		try {
			if (richtextnav != null) {
				richtextnav.recycle();
			}
		} catch (NotesException notesexception) {
			throw new RuntimeException ("Fatal Error while recycling RichTextNavigator.");
		}
		return null;
	}
	
	public static RichTextItem recycle(RichTextParagraphStyle richtextparstyle) {
		try {
			if (richtextparstyle != null) {
				richtextparstyle.recycle();
			}
		} catch (NotesException notesexception) {
			throw new RuntimeException ("Fatal Error while recycling RichTextParagraphStyle.");
		}
		return null;
	}
	
	public static RichTextItem recycle(RichTextRange richtextrange) {
		try {
			if (richtextrange != null) {
				richtextrange.recycle();
			}
		} catch (NotesException notesexception) {
			throw new RuntimeException ("Fatal Error while recycling RichTextRange.");
		}
		return null;
	}
	
	public static RichTextItem recycle(RichTextSection richtextsection) {
		try {
			if (richtextsection != null) {
				richtextsection.recycle();
			}
		} catch (NotesException notesexception) {
			throw new RuntimeException ("Fatal Error while recycling RichTextSection.");
		}
		return null;
	}
	
	public static RichTextItem recycle(RichTextStyle richtextstyle) {
		try {
			if (richtextstyle != null) {
				richtextstyle.recycle();
			}
		} catch (NotesException notesexception) {
			throw new RuntimeException ("Fatal Error while recycling RichTextStyle.");
		}
		return null;
	}


	public static Item recycle(DateTime dt) {
		try {
			if (dt != null) {
				dt.recycle();
			}
		} catch (NotesException notesexception) {
			throw new RuntimeException ("Fatal Error while recycling DateTime.");
		}
		return null;
	}

	public static Item recycle(Item item) {
		try {
			if (item != null) {
				item.recycle();
			}
		} catch (NotesException notesexception) {
			throw new RuntimeException ("Fatal Error while recycling Item.");
		}
		return null;
	}
	
	public static DJDocument recycle(DJDocument doc) {
		try {
			if (doc != null) {
				doc.recycle();
			}
		} catch (NotesException notesexception) {
			throw new RuntimeException ("Fatal Error while recycling DJDocument.");
		}
		return null;
	}
	
	public static DJAgentContext recycle(DJAgentContext agentContext) {
		try {
			if (agentContext != null) {
				agentContext.recycle();
			} 
		} catch (NotesException notesexception) {
			throw new RuntimeException ("Fatal Error while recycling DJAgentContext.");
		}
		return null;
	}

	public static Log recycle(Log log) {
		try {
			if (log != null) {
				log.recycle();
			}
		} catch (NotesException notesexception) {
			throw new RuntimeException ("Fatal Error while recycling Log.");
		}
		return null;
	}
}
