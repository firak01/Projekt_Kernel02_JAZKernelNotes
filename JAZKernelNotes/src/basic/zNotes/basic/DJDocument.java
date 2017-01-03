package basic.zNotes.basic;


import java.io.*;
import java.util.Vector;
import org.xml.sax.SAXException;
import lotus.domino.*;

/**
 * @author Thomas Ekert
 */
public class DJDocument {

	private Document doc = null;
	private boolean isRecycledOrRemoved = true;
	private static final int EXCEPTION_NO=NotesError.NOTES_ERR_DELETED;
	private static final String EXCEPTION_MSG="Document has been removed or recycled.";

	public DJDocument (Document initialDoc){
		setDoc (initialDoc);
	}

	public Document getDoc() {
		return doc;
	}
	public void setDoc(Document initialDoc) {
		if (doc!=null && !isRecycledOrRemoved) {
			try {
				doc.recycle();
			} catch (NotesException e) {
				e.printStackTrace();
			}
		}
		if (initialDoc != null) {isRecycledOrRemoved=false;}
		doc = initialDoc;
	}
	public void makeResponse(Document arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		doc.makeResponse(arg0);
	}
	public void makeResponse(DJDocument arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		doc.makeResponse(arg0.getDoc());
	}
	public void copyAllItems(Document arg0, boolean arg1) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		doc.copyAllItems(arg0,arg1);
	}
	public void copyAllItems(DJDocument arg0, boolean arg1) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		doc.copyAllItems(arg0.getDoc(),arg1);
	}
	public boolean remove(boolean arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		boolean result = doc.remove(arg0);
		isRecycledOrRemoved=true;
		return result;
	}
	public boolean removePermanently(boolean arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		boolean result = doc.removePermanently(arg0);
		isRecycledOrRemoved=true;
		return result;
	}
	public void recycle() throws NotesException {
		if (doc==null) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		doc.recycle();
		doc=null;
		isRecycledOrRemoved=true;
	}
	public void recycle(Vector arg0) throws NotesException {
		if (doc==null) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		Vector v = new Vector();
		boolean myself=false;
		for (int i = 0, size = arg0.size(); i < size; i++) {
			Object o = arg0.elementAt(i);
			if (o instanceof DJDocument) {
				Document thisDoc = ((DJDocument)o).getDoc();
				if (thisDoc != null) {
					v.add(thisDoc);
					if (thisDoc.equals (this.getDoc())) myself=true;
				}
			} else {
				v.add(o);
				if (o.equals (this.getDoc())) myself=true;
			}
		}
		doc.recycle(v);
		if (myself) {
			doc=null;
			isRecycledOrRemoved=true;
		}
	}
	public Item appendItemValue(String arg0, Object arg1) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.appendItemValue(arg0, arg1);
	}
	public Item appendItemValue(String arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.appendItemValue(arg0);
	}

	public Item appendItemValue(String arg0, int arg1) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.appendItemValue (arg0, arg1);
	}
	public Item appendItemValue(String arg0, double arg1) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.appendItemValue (arg0, arg1);
	}
	public boolean closeMIMEEntities() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.closeMIMEEntities();
	}
	public boolean closeMIMEEntities(boolean arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.closeMIMEEntities(arg0);
	}
	public boolean closeMIMEEntities(boolean arg0, String arg1) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.closeMIMEEntities(arg0, arg1);
	}
	public boolean computeWithForm(boolean arg0, boolean arg1) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.computeWithForm(arg0, arg1);
	}
	public Item copyItem(Item arg0, String arg1) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.copyItem(arg0,arg1);
	}
	public Item copyItem(Item arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.copyItem(arg0);
	}
	public Document copyToDatabase(Database arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.copyToDatabase(arg0);
	}
	public MIMEEntity createMIMEEntity() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.createMIMEEntity();
	}
	public MIMEEntity createMIMEEntity(String arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.createMIMEEntity(arg0);
	}
	public RichTextItem createRichTextItem(String arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.createRichTextItem(arg0);
	}
	public Document createReplyMessage(boolean arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.createReplyMessage(arg0);
	}
	public void encrypt() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		doc.encrypt();
	}
	public EmbeddedObject getAttachment(String arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getAttachment(arg0);
	}
	public Vector getAuthors() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getAuthors();
	}
	public Vector getColumnValues() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getColumnValues();
	}
	public DateTime getCreated() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getCreated();
	}
	public Vector getEmbeddedObjects() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getEmbeddedObjects();
	}
	public Vector getEncryptionKeys() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getEncryptionKeys();
	}
	public void setEncryptionKeys(Vector arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		doc.setEncryptionKeys(arg0);
	}
	public Item getFirstItem(String arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getFirstItem(arg0);
	}
	public int getFTSearchScore() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return getFTSearchScore();
	}
	public DateTime getLastAccessed() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getLastAccessed();
	}
	public DateTime getLastModified() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getLastModified();
	}
	public Vector getFolderReferences() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getFolderReferences();
	}
	public MIMEEntity getMIMEEntity() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getMIMEEntity();
	}
	public MIMEEntity getMIMEEntity(String arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getMIMEEntity(arg0);
	}
	public Vector getItems() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getItems();
	}
	public Vector getItemValue(String arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getItemValue(arg0);
	}
	public String getItemValueString(String arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getItemValueString(arg0);
	}
	public int getItemValueInteger(String arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getItemValueInteger(arg0);
	}
	public double getItemValueDouble(String arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getItemValueDouble(arg0);
	}
	public Object getItemValueCustomData(String arg0, String arg1) throws IOException, ClassNotFoundException, NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getItemValueCustomData(arg0, arg1);
	}
	public Object getItemValueCustomData(String arg0) throws IOException, ClassNotFoundException, NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getItemValueCustomData(arg0);
	}
	public byte[] getItemValueCustomDataBytes(String arg0, String arg1) throws IOException, NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getItemValueCustomDataBytes(arg0,arg1);
	}
	public Vector getItemValueDateTimeArray(String arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getItemValueDateTimeArray(arg0);
	}
	public String getKey() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getKey();
	}
	public String getNameOfProfile() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getNameOfProfile();
	}
	public String getNoteID() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getNoteID();
	}
	public Database getParentDatabase() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getParentDatabase();
	}
	public String getParentDocumentUNID() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getParentDocumentUNID();
	}
	public View getParentView() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getParentView();
	}
	public DocumentCollection getResponses() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getResponses();
	}
	public String getSigner() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getSigner();
	}
	public int getSize() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getSize();
	}
	public String getUniversalID() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getUniversalID();
	}
	public void setUniversalID(String arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		doc.setUniversalID(arg0);
	}
	public String getVerifier() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getVerifier();
	}
	public boolean hasEmbedded() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.hasEmbedded();
	}
	public boolean hasItem(String arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.hasItem(arg0);
	}
	public boolean isEncrypted() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.isEncrypted();
	}
	public boolean isEncryptOnSend() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.isEncryptOnSend();
	}
	public void setEncryptOnSend(boolean arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		doc.setEncryptOnSend(arg0);
	}
	public boolean isNewNote() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.isNewNote();
	}
	public boolean isProfile() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.isProfile();
	}
	public boolean isResponse() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.isResponse();
	}
	public boolean isSigned() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.isSigned();
	}
	public boolean isValid() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.isValid();
	}
	public boolean isSaveMessageOnSend() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.isSaveMessageOnSend();
	}
	public void setSaveMessageOnSend(boolean arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		doc.setSaveMessageOnSend(arg0);
	}
	public boolean isSentByAgent() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.isSentByAgent();
	}
	public boolean isSignOnSend() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.isSignOnSend();
	}
	public void setSignOnSend(boolean arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		doc.setSignOnSend(arg0);
	}
	public boolean isDeleted() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.isDeleted();
	}
	public void putInFolder(String arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		doc.putInFolder(arg0);
	}
	public void putInFolder(String arg0, boolean arg1) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		doc.putInFolder(arg0,arg1);
	}
	public void removeFromFolder(String arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		doc.removeFromFolder(arg0);
	}
	public void removeItem(String arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		doc.removeItem(arg0);
	}
	public boolean renderToRTItem(RichTextItem arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.renderToRTItem(arg0);
	}
	public Item replaceItemValue(String arg0, Object arg1) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.replaceItemValue(arg0,arg1);
	}
	public Item replaceItemValueCustomData(String arg0, String arg1, Object arg2) throws IOException, NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.replaceItemValueCustomData(arg0,arg1,arg2);
	}
	public Item replaceItemValueCustomData(String arg0, Object arg1) throws IOException, NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.replaceItemValueCustomData(arg0,arg1);
	}
	public Item replaceItemValueCustomDataBytes(String arg0, String arg1, byte[] arg2) throws IOException, NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.replaceItemValueCustomDataBytes(arg0,arg1,arg2);
	}
	public boolean save(boolean arg0, boolean arg1, boolean arg2) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.save (arg0,arg1,arg2);
	}
	public boolean save(boolean arg0, boolean arg1) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.save (arg0,arg1);
	}
	public boolean save(boolean arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.save(arg0);
	}
	public boolean save() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.save();
	}
	public void send(boolean arg0, Vector arg1) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		doc.send(arg0,arg1);
	}
	public void send(Vector arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		doc.send(arg0);
	}
	public void send(boolean arg0, String arg1) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		doc.send(arg0,arg1);
	}
	public void send(String arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		doc.send(arg0);
	}
	public void send(boolean arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		doc.send(arg0);
	}
	public void send() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		doc.send();
	}
	public void sign() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		doc.sign();
	}
	public String getURL() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getURL();
	}
	public String getNotesURL() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getNotesURL();
	}
	public String getHttpURL() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getHttpURL();
	}
	public String generateXML() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.generateXML();
	}
	public void generateXML(Writer arg0) throws NotesException, IOException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		doc.generateXML(arg0);
	}
	
//R7 public void generateXML(Object arg0, XSLTResultTarget arg1) throws IOException, NotesException {
//R6: FGL: Hiier von doc.generateXML noch die SAXException geworfen, die dann diese Methode auch weitergibt.
public void generateXML(Object arg0, XSLTResultTarget arg1) throws IOException, SAXException, NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		doc.generateXML(arg0,arg1);
	}
	public Vector getReceivedItemText() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getReceivedItemText();
	}
	public Vector getLockHolders() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.getLockHolders();
	}
	public boolean lock() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.lock();
	}
	public boolean lock(boolean arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.lock(arg0);
	}
	public boolean lock(String arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.lock(arg0);
	}
	public boolean lock(String arg0, boolean arg1) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.lock(arg0,arg1);
	}
	public boolean lock(Vector arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.lock (arg0);
	}
	public boolean lock(Vector arg0, boolean arg1) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.lock (arg0,arg1);
	}
	public boolean lockProvisional() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.lockProvisional();
	}
	public boolean lockProvisional(String arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.lockProvisional(arg0);
	}
	public boolean lockProvisional(Vector arg0) throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		return doc.lockProvisional(arg0);
	}
	public void unlock() throws NotesException {
		if (doc==null || isRecycledOrRemoved) throw new NotesException (EXCEPTION_NO,EXCEPTION_MSG);
		doc.unlock();
	}
}

