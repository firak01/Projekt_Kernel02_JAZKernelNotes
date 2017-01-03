package basic.zNotes.kernel;

public interface IKernelNotesConstZZZ {
	
	//final String sCONFIG_KEY_KERNEL_DEFAULT = "ZZZ";	//Default Erweiterung des Kernel-Dokument-Keys,
	//Merke: Nun soll lediglich .getKernelKey verwendet werden. Diese MEthode ist dann ggf. in der custom-Klasse zu überschreiben. 
	final String sCONFIG_NUMBER_SYSTEM_DEFAULT = "01";             //z.B. für die Produktiv-Umgebung
	
	//Für die Application-Profile-Dokumente in jeder Datenbank, in der sie erstellt wurden.
	final String sFIELD_PREFIX_CONFIG_SERVER= "Server";	//Damit fangen alle Felder im Application-Profil-Dokument an: Server
	final String sFIELD_PREFIX_CONFIG_PATH= "PathDB";		// " : Pfad zur Datenbank
	
	
	//Folgende Desingelemente müssen sich dann in der Applikationsdatenbank befinden. Werden von KernelZZZ.refreshStoreApplication(...) verwendet.
	final String sFORM_PREFIX_STORE = "frmConfigStore";   //Maskenname des Dokuments (plus ApplicationKey), in dem z.B. die fortlaufende Nummer der Nummernvergabe gespeichert wird.
	final String sVIEW_STORE = "viwConfigZZZ";          //Ansichtsname, in der alle Dokumentarten, die für die Speicherung von "sich ändernden" Applikationseigenschaften verwendet werden.
	final String sFIELD_KEY_APPLICATION="ApplicationKeyZZZ"; //Der Name des Felds in dem die ApplicationKeys gespeichert werden
	
}
