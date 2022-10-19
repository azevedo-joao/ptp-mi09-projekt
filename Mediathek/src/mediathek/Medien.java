package mediathek;

/**
 * Die Klasse Medien definiert einen Eintrag für die Mediathek
 * 
 * @author João Azevedo
 * @author Jonah Lüdemann
 * 
 * @version August 2022
 *
 */
public class Medien {
	
	private String name;
	private String plattform;
	private int veroeffentlichung;
	private boolean durchgespielt;
	
	/**
	 * Initialisiert das Objekt der Klasse @Medien
	 * 
	 * @param name	der Titel des Spiels
	 * @param plattform	die Plattform des Spiels
	 * @param veroeffentlichung	das Veröfffentlichungsjahr des Spiels
	 * @param durchgespielt	Information, ob das Spiel durchgespielt wurde
	 */
	public Medien(String name, String plattform, int veroeffentlichung, boolean durchgespielt) {
		this.name = name;
		this.plattform = plattform;
		this.veroeffentlichung = veroeffentlichung;
		this.durchgespielt = durchgespielt;
	}

	/**
	 * Gibt den Titel des Spiels zurück
	 * 
	 * @return	der Titel des Spiels
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setzt den Titel des Spiels
	 * 
	 * @param name	der zu setzende Titel des Spiels
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gibt die Plattform des Spiels zurück
	 * 
	 * @return	die Plattform des Spiels
	 */
	public String getPlattform() {
		return plattform;
	}
	
	/**
	 * Setzt die Plattform des Spiels
	 * 
	 * @param plattform	die zu setzende Plattform
	 */
	public void setPlattform(String plattform) {
		this.plattform = plattform;
	}

	/**
	 * Gibt das Veröffentlichungsjahr des Spiels zurück
	 * 
	 * @return	das Veröffentlichungsjahr
	 */
	public int getVeroeffentlichung() {
		return veroeffentlichung;
	}

	/**
	 * Setzt das Veröffentlichungsjahr des Spiels
	 * 
	 * @param veroeffentlichung	das zu setzende Veröffentlichungsjahr
	 */
	public void setVeroeffentlichung(int veroeffentlichung) {
		this.veroeffentlichung = veroeffentlichung;
	}

	/**
	 * Gibt zurück, ob das Spiel zurückgespielt wurde
	 * 
	 * @return	true-Wert, wenn das Spiel durchgespielt wurde
	 */
	public boolean getDurchgespielt() {
		return durchgespielt;
	}

	/**
	 * Setzt den Wert Durchgespielt für das Spiel
	 * 
	 * @param durchgespielt	ob das Spiel durchgespielt wurde
	 */
	public void setDurchgespielt(boolean durchgespielt) {
		this.durchgespielt = durchgespielt;
	}
}
