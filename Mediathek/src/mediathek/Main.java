package mediathek;

/**
 * 
 * Applikation für die Mediathek
 * Initialisiert die Mediathek.
 * 
 * @author João Azevedo
 * @author Jonah Lüdemann
 * 
 * @version August 2022
 *
 */
public class Main {

	public static void main(String[] args) {	
		MediathekData data = new MediathekData();
		@SuppressWarnings("unused")
		MediathekController controller = new MediathekController(data);
	}
}
