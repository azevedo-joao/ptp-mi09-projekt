package mediathek;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Controller-Klasse für die Mediathek.
 * 
 * Hier werden die Aktionen aus der Benutzeroberfläche @MediathekGUI bearbeitet 
 * und an die Datenbank @MediathekData weitergegeben bzw. die Daten von der Datenbank
 * an die Benutzeroberfläche weitergereicht und umgekehrt.
 * 
 * @author João Azevedo
 * @version Juli 2022
 *
 */
public class MediathekController implements ActionListener, KeyListener{

	MediathekData data;
	MediathekGUI gui;
	
	Medien eintrag;
	
	/**
	 * Erzeugt ein Objekt der Klasse @MediathekGUI und übergibt ihr das DefaultTableModel
	 * 
	 * @param data das Datenmodell @MediathekData
	 */
	public MediathekController(MediathekData data) {	
		this.data = data;
		gui = new MediathekGUI("Mediathek", data.getTableModel(), this);
	}
	
	/**
	 * Prüft den übergebenen Befehl und führt die entsprechende Aktion aus
	 * 
	 * @param befehl der auszuführende Befehl
	 */
	public void befehlAusfuehren(String befehl) {	
		if(befehl.equals("beenden")) {
			beenden();
		} else if(befehl.equals("sortieren")) {
			sortieren("", gui.getTextComboSort());
		} else if(befehl.equals("neu")) {
			gui.initDialog(this);
		} else if(befehl.equals("hinzufuegen")) {
			eintragHinzufuegen();
		} else if(befehl.equals("bearbeiten")) {
			bearbeiten();
		} else if(befehl.equals("speichern")) {
			speichern(eintrag);
		} else if(befehl.equals("loeschen")) {
			eintragLoeschen();
		} else if(befehl.equals("exportieren")) {
			data.datenbankTxtExportieren();
		} else if(befehl.equals("zuruecksetzen")) {
			zuruecksetzen();
		}
	}
	
	/**
	 * Beendet die Mediathek.
	 * Die bzw. der Nutzende wird um Bestätigung gebeten
	 */
	private void beenden() {
		String dialog = "Möchtest du die Mediathek beenden?";
		String titel = "Mediathek beenden";
		
		if(gui.bestaetigungsDialog(dialog, titel) == 1) {	
			gui.dispose();
			System.exit(0);
		}
	}
	
	/**
	 * Fügt der Datenbank einen neuen Eintrag hinzu.
	 * Prüft nach der Richtigkeit der Eingaben.
	 * 
	 * Bei erfolgreicher Eingabe wird Nutzende benachrichtigt
	 */
	private void eintragHinzufuegen() {
		try {
			String titel = gui.getTextTitel();
			int jahr = Integer.parseInt(gui.getTextJahr());
			String plattform = gui.getTextPlattform();
			boolean durch = gui.istDurchgespielt();
			
			//Workaround für unerwartete Eingaben
			if(titel.equals("")) {
				titel = "Neuer Titel";
			}
			
			if(jahr < 1970 || jahr > 2022) {
				jahr = 1970;
			}
			
			if(plattform.equals("Plattform")) {
				plattform = "";
			}
			
			data.insertData(new Medien(titel, plattform, jahr, durch));
			data.tableUpdated("Mediathek");
			gui.dialogFensterSchliessen();
			gui.meldungsDialog(titel + " wurde hinzugefügt");
		} catch(NumberFormatException nfe) {
			//Nutzende wird bei falscher Eingabe benachrichtigt
			gui.meldungsDialog("Falsches Zahlenformat. Gib bitte eine vierstellige Ziffer ein");
		}
	}
	
	/**
	 * Öffnet den Bearbeiten-Dialog
	 * 
	 * Nutzende sieht die zu bearbeitenden Informationen zum Eintrag
	 * im Dialogfenster
	 * 
	 * Nutzende wird benachrichtigt, wenn kein Eintrag ausgewählt ist
	 */
	public void bearbeiten() {
		try {
			if(!gui.istEintragAusgewaehlt()) {
				throw new Exception("Bitte wähle einen Eintrag aus");
			}
			
			gui.starteBearbeiten(this);
			
			eintrag = data.getSelectedRow(gui.getAusgewaehlteZeile());
			gui.setTextTitel(eintrag.getName());
			gui.setTextPlattform(eintrag.getPlattform());
			gui.setTextJahr(eintrag.getVeroeffentlichung());
			gui.setTextDurchgespielt(eintrag.getDurchgespielt());
		} catch(Exception e) {
			gui.meldungsDialog(e.getMessage());
		}
	}
	
	/**
	 * Speichert die bearbeiteten Daten aus dem Dialog
	 * im Datenmodell
	 * 
	 * Nutzende wird benachrichtigt bei falscher Zahleneingabe
	 * 
	 * @param eintrag der zu bearbeitende Eintrag
	 */
	public void speichern(Medien eintrag) {
		try {
			String titel = gui.getTextTitel();
			int jahr = Integer.parseInt(gui.getTextJahr());
			String plattform = gui.getTextPlattform();
			boolean durch = gui.istDurchgespielt();
			
			if(titel.equals("")) {
				titel = "Neuer Titel";
			}
			
			if(jahr < 1970 || jahr > 2022) {
				jahr = 1970;
			}
			
			if(plattform.equals("Plattform")) {
				plattform = "";
			}
			
			eintrag.setName(titel);
			eintrag.setPlattform(plattform);
			eintrag.setVeroeffentlichung(jahr);
			eintrag.setDurchgespielt(durch);
			
			data.updateData(data.getSelectedRow(gui.getAusgewaehlteZeile()), eintrag);
			data.tableUpdated("Mediathek");
			gui.dialogFensterSchliessen();
			gui.meldungsDialog(titel + " wurde bearbeitet");
		} catch(NumberFormatException nfe) {
			gui.meldungsDialog("Falsches Zahlenformat. Gib bitte eine vierstellige Ziffer ein");
		}
	}
	
	/**
	 * Löscht den die ausgewählten Einträge
	 * 
	 * Fragt Nutzende nach Bestätigung
	 * 
	 * Nutzende wird benachrichtigt, wenn kein Eintrag ausgewählt
	 */
	public void eintragLoeschen() {
		try {
			String dialog = "Möchtest du den Eintrag löschen?";
			String titel = "Eintrag löschen";
			
			if(!gui.istEintragAusgewaehlt()) {
				throw new Exception("Bitte wähle einen Eintrag aus");
			}
			
			if(gui.bestaetigungsDialog(dialog, titel) == 1) {
				data.deleteSelection(gui.getAusgewaehlteZeilen());
				gui.meldungsDialog("Eintrag wurde gelöscht");
			}
		} catch(Exception e) {
			gui.meldungsDialog(e.getMessage());
		}
	}
	
	/**
	 * Sortiert die Einträge nach dem eingegebenen Titel, Plattform oder beidem
	 * 
	 * @param titel der zu sortierende Titel
	 * @param console die zu sortierende Plattform
	 */
	public void sortieren(String titel, String console) {
		if(console.equals("Plattform")) {
			console = "";
		}
		data.searchTable("Mediathek", titel, console);	
	}
	
	/**
	 * Löscht alle Einträge in der Mediathek
	 * 
	 * Nutzende wird nach Bestätigung gefragt
	 * 
	 * Bei leerer Datenbank wird Nutzende benachrichtigt
	 */
	private void zuruecksetzen() {
		String dialog = "Möchtest du die Mediathek zurücksetzen? Alle Einträge werden gelöscht!";
		String titel = "Mediathek zurücksetzen";
		
		if(data.getMedien().isEmpty()) {
			gui.meldungsDialog("Die Mediathek ist bereits leer. Füge Einträge hinzu, bevor du sie löschen kannst.");
		} else {
			if(gui.bestaetigungsDialog(dialog, titel) == 1) {
				data.clearData();
				gui.meldungsDialog("Alle Einträge wurden gelöscht");
				data.tableUpdated("Mediathek");
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String befehl = e.getActionCommand();	
		befehlAusfuehren(befehl);
	}

	@Override
	public void keyReleased(KeyEvent e) {		
		sortieren(gui.getTextSuche(), "");	
	}

	@Override
	public void keyTyped(KeyEvent e) { }


	@Override
	public void keyPressed(KeyEvent e) { }
}
