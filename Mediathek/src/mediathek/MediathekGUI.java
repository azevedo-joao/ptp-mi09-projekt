package mediathek;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * View Klasse für die Mediathek.
 * Erzeugt die Benutzeroberfläche mit ihren Komponenten
 * samt Dialogsfenster
 * 
 * @author João Azevedo
 * 
 * @version August 2022
 *
 */
public class MediathekGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	// Hauptfenster
	private JPanel eingabe;

	private JTextField txtSuche;
	private JButton btnBearbeiten, btnLoeschen;
	private JScrollPane tblTabelle;
	private JTable tabelle;

	private JMenuBar menubar;
	private JMenu file;
	private JMenuItem neuerEintrag, export, zuruecksetzen, beenden;
	
	private JComboBox<String> comboSort;

	//Dialogfenster
	private JFrame dialogFenster;
	private JTextField txtTitel;
	private JTextField txtJahr;
	private JComboBox<String> comboPlattform;
	private JComboBox<String> comboDurchgespielt;
	
	private JButton btnHinzufuegen;
	private JButton btnSpeichern;
	
	private JLabel lblNachricht;
	
	private String[] konsolen;

	/**
	 * Initialisiert den Anfangszustand der Benutzeroberfläche.
	 * 
	 * Die <code>konsolen</code> Variable initialisiert eine Liste mit den Plattformen,
	 * die für die Sortierung im Hauptfenster benutzt werden
	 * 
	 * @param titel	der Titel, der im Anwendungsfenster angezeigt wird
	 * @param table	das TableModel, das im Anwendungsfenster dargestellt werden soll
	 * @param controller der Kontroller, der die Aktionen implementiert
	 * 			und diese an die Klasse @MediathekData ausführt
	 */
	public MediathekGUI(String titel, DefaultTableModel table, MediathekController controller) {	
		super(titel);
		
		konsolen = new String[] {
				"Plattform",
				"Game Boy (Color)", "Game Boy Advance",
				"Genesis/Mega Drive", "Master System", "Mobile",
				"NES", "Nintendo 64", "Nintendo DS/3DS", "Nintendo Switch",
				"PlayStation", "PSP", "PlayStation Vita",
				"SNES", "Steam", "XBOX"
		};
		
		this.init(table, controller);
	}

	/**
	 * Öffnet einen Bestätigungsdialog mit einer Ja/Nein Frage
	 * 
	 * @param dialog Die Frage, die der bzw. dem Nutzenden gestellt wird
	 * @param titel	der Titel, der im Dialogfenster angezeigt wird
	 * @return 1 bei einer positiven Antwort, 0 bei einer negativen
	 */
	public int bestaetigungsDialog(String dialog, String titel) {			
		int auswahl = JOptionPane.showConfirmDialog(this, dialog, titel, JOptionPane.YES_NO_OPTION);
		
		if(auswahl == JOptionPane.YES_OPTION) {	
			return 1;
		} else {	
			this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			return 0;
		}
	}
	
	/**
	 * Öffnet ein Fenster mit einer Meldung an die Nutzenden
	 * 
	 * @param meldung die Meldung, die angezeigt wird
	 */
	public void meldungsDialog(String meldung) {		
		JOptionPane.showMessageDialog(this, meldung);
	}

	/**
	 * Initialisiert die Komponente und baut das Hauptfenster
	 * der Mediathek auf
	 * 
	 * @param table	der Inhalt der Datenbank, die angezeigt wird
	 * @param controller der Kontroller, der die Aktionen implementiert
	 * 			und diese an die Klasse @MediathekData ausführt
	 */
	public void init(DefaultTableModel table, MediathekController controller) {

		eingabe = new JPanel();
		eingabe.setBackground(Color.white);
		eingabe.setBorder(new EmptyBorder(5, 5, 5, 5));
		txtSuche = new JTextField(25);

		menubar = new JMenuBar();

		file = new JMenu("File");
		neuerEintrag = new JMenuItem("Neuer Eintrag");
		export = new JMenuItem("Exportieren");
		zuruecksetzen = new JMenuItem("Zurücksetzen");
		beenden = new JMenuItem("beenden");
		
		comboSort = new JComboBox<String>(konsolen);
		comboSort.setSelectedIndex(0);
		
		btnBearbeiten = new JButton("Bearb.");
		btnLoeschen = new JButton("Löschen");

		//Verhindert, dass eine Zeile durch Doppelklick bearbeitet wird
		tabelle = new JTable(table) {
	         private static final long serialVersionUID = 1L;
	         public boolean editCellAt(int row, int column, java.util.EventObject e) {
	             return false;
	          }
	       };

		tblTabelle = new JScrollPane(tabelle);
		tabelle.getModel().addTableModelListener(tabelle);
		this.setLayout(new BorderLayout());
		this.add(eingabe, "North");
		this.add(tblTabelle, "Center");

		eingabe.setLayout(new FlowLayout(FlowLayout.CENTER));
		eingabe.add(txtSuche);
		eingabe.add(comboSort);
		eingabe.add(btnBearbeiten);
		eingabe.add(btnLoeschen);

		this.setJMenuBar(menubar);
		menubar.add(file);
		file.add(neuerEintrag);
		file.add(export);
		file.add(zuruecksetzen);
		file.add(beenden);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if(bestaetigungsDialog("Möchtest du die Mediathek beenden?", "Mediathekbeenden") == 1) {
					dispose();
					System.exit(0);
				}
			}
		});

		beenden.setActionCommand("beenden");
		beenden.addActionListener(controller);
		neuerEintrag.setActionCommand("neu");
		neuerEintrag.addActionListener(controller);
		export.setActionCommand("exportieren");
		export.addActionListener(controller);
		zuruecksetzen.setActionCommand("zuruecksetzen");
		zuruecksetzen.addActionListener(controller);
		btnBearbeiten.setActionCommand("bearbeiten");
		btnBearbeiten.addActionListener(controller);
		btnLoeschen.setActionCommand("loeschen");
		btnLoeschen.addActionListener(controller);
		comboSort.setActionCommand("sortieren");
		comboSort.addActionListener(controller);
		txtSuche.addKeyListener(controller);

		this.setSize(700, 500);
		this.setResizable(false);
		this.setVisible(true);
	}

	/**
	 * Öffnet ein Dialogfenster für das Hinzufügen eines neuen Eintrags
	 * Wird auch mit einigen Änderungen fürs Bearbeiten benutzt
	 * 
	 * @param controller der Kontroller, der die Aktionen implementiert
	 * 			und diese an die Klasse @MediathekData ausführt
	 */
	public void initDialog(MediathekController controller) {

		dialogFenster = new JFrame();
		dialogFenster.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialogFenster.setBounds(100, 100, 435, 350);
		
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		dialogFenster.setContentPane(contentPane);
		
		SpringLayout dialogLayout = new SpringLayout();
		contentPane.setLayout(dialogLayout);
		
		JLabel lblTitel = new JLabel("Titel:");
		contentPane.add(lblTitel);
		
		txtTitel = new JTextField();
		dialogLayout.putConstraint(SpringLayout.NORTH, txtTitel, -5, SpringLayout.NORTH, lblTitel);
		txtTitel.setColumns(10);
		contentPane.add(txtTitel);
		
		JLabel lblPlattform = new JLabel("Plattform:");
		dialogLayout.putConstraint(SpringLayout.WEST, lblPlattform, 10, SpringLayout.WEST, contentPane);
		dialogLayout.putConstraint(SpringLayout.WEST, lblTitel, 0, SpringLayout.WEST, lblPlattform);
		dialogLayout.putConstraint(SpringLayout.SOUTH, lblTitel, -34, SpringLayout.NORTH, lblPlattform);
		contentPane.add(lblPlattform);
		
		JLabel lblJahr = new JLabel("Veröffentlichung:");
		dialogLayout.putConstraint(SpringLayout.NORTH, lblJahr, 150, SpringLayout.NORTH, contentPane);
		dialogLayout.putConstraint(SpringLayout.WEST, lblJahr, 10, SpringLayout.WEST, contentPane);
		dialogLayout.putConstraint(SpringLayout.SOUTH, lblPlattform, -34, SpringLayout.NORTH, lblJahr);
		contentPane.add(lblJahr);
		
		comboPlattform = new JComboBox<String>(konsolen);
		dialogLayout.putConstraint(SpringLayout.WEST, txtTitel, 0, SpringLayout.WEST, comboPlattform);
		dialogLayout.putConstraint(SpringLayout.EAST, txtTitel, -15, SpringLayout.EAST, comboPlattform);
		dialogLayout.putConstraint(SpringLayout.WEST, comboPlattform, 60, SpringLayout.EAST, lblPlattform);
		dialogLayout.putConstraint(SpringLayout.EAST, comboPlattform, -60, SpringLayout.EAST, contentPane);
		dialogLayout.putConstraint(SpringLayout.NORTH, comboPlattform, -5, SpringLayout.NORTH, lblPlattform);
		contentPane.add(comboPlattform);
		comboPlattform.setSelectedIndex(0);
			
		lblNachricht = new JLabel("Neuer Eintrag. Mit \"Hinzufügen\" bestätigen.");
		dialogLayout.putConstraint(SpringLayout.NORTH, lblNachricht, 10, SpringLayout.NORTH, contentPane);
		dialogLayout.putConstraint(SpringLayout.WEST, lblNachricht, 70, SpringLayout.WEST, contentPane);
		contentPane.add(lblNachricht);
		
		comboDurchgespielt = new JComboBox<String>();
		dialogLayout.putConstraint(SpringLayout.WEST, comboDurchgespielt, 15, SpringLayout.EAST, lblJahr);
		dialogLayout.putConstraint(SpringLayout.EAST, comboDurchgespielt, 0, SpringLayout.EAST, comboPlattform);
		contentPane.add(comboDurchgespielt);
		
		comboDurchgespielt.addItem("Ja");
		comboDurchgespielt.addItem("Nein");
		comboDurchgespielt.setSelectedIndex(1);
		
		JLabel lblDurchgespielt = new JLabel("Durchgespielt:");
		dialogLayout.putConstraint(SpringLayout.NORTH, comboDurchgespielt, -4, SpringLayout.NORTH, lblDurchgespielt);
		dialogLayout.putConstraint(SpringLayout.NORTH, lblDurchgespielt, 34, SpringLayout.SOUTH, lblJahr);
		dialogLayout.putConstraint(SpringLayout.WEST, lblDurchgespielt, 0, SpringLayout.WEST, lblTitel);
		contentPane.add(lblDurchgespielt);
		
		txtJahr = new JTextField();
		dialogLayout.putConstraint(SpringLayout.NORTH, txtJahr, -5, SpringLayout.NORTH, lblJahr);
		dialogLayout.putConstraint(SpringLayout.WEST, txtJahr, 5, SpringLayout.WEST, txtTitel);
		dialogLayout.putConstraint(SpringLayout.EAST, txtJahr, 0, SpringLayout.EAST, txtTitel);
		txtJahr.setColumns(10);
		contentPane.add(txtJahr);
		
		JButton btnAbbrechen = new JButton("Abbrechen");
		dialogLayout.putConstraint(SpringLayout.WEST, btnAbbrechen, 60, SpringLayout.WEST, contentPane);
		contentPane.add(btnAbbrechen);
		
		btnHinzufuegen = new JButton("Hinzufügen");
		dialogLayout.putConstraint(SpringLayout.NORTH, btnAbbrechen, 0, SpringLayout.NORTH, btnHinzufuegen);
		dialogLayout.putConstraint(SpringLayout.SOUTH, btnHinzufuegen, -10, SpringLayout.SOUTH, contentPane);
		dialogLayout.putConstraint(SpringLayout.EAST, btnHinzufuegen, 0, SpringLayout.EAST, comboPlattform);
		contentPane.add(btnHinzufuegen);
		
		btnSpeichern = new JButton("Speichern");
		dialogLayout.putConstraint(SpringLayout.NORTH, btnAbbrechen, 0, SpringLayout.NORTH, btnSpeichern);
		dialogLayout.putConstraint(SpringLayout.SOUTH, btnSpeichern, -10, SpringLayout.SOUTH, contentPane);
		dialogLayout.putConstraint(SpringLayout.EAST, btnSpeichern, 0, SpringLayout.EAST, comboPlattform);
		contentPane.add(btnSpeichern);
		btnSpeichern.setVisible(false);
		
		btnHinzufuegen.setActionCommand("hinzufuegen");
		btnHinzufuegen.addActionListener(controller);
		btnAbbrechen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {			
				dialogFensterSchliessen();
			}
		});

		//Dialogfenster wird vor dem Hauptfenster angezeigt
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				dialogFenster.toFront();
				dialogFenster.repaint();
			}
		});
		dialogFenster.setVisible(true);
	}
	
	/**
	 * Öffnet ein Dialogfenster und passt die Komponente
	 * fürs Bearbeiten an
	 * 
	 * @param controller der Kontroller, der die Aktionen implementiert
	 * 			und diese an die Klasse @MediathekData ausführt
	 */
	public void starteBearbeiten(MediathekController controller) {
		initDialog(controller);
		dialogFenster.setTitle("Eintrag bearbeiten");
		lblNachricht.setText("Eintrag bearbeiten. Mit \"Speichern\" bestätigen.");
		
		btnHinzufuegen.setVisible(false);
		btnSpeichern.setVisible(true);
		
		btnSpeichern.setActionCommand("speichern");
		btnSpeichern.addActionListener(controller);
	}
	
	/**
	 * Prüft, ob der ausgewählte Eintrag den Wert "Ja"
	 * im Attribut Durchgespielt hat
	 * 
	 * @return	true, wenn Ja ausgewählt ist
	 */
	public boolean istDurchgespielt() {
		
		String durch = comboDurchgespielt.getSelectedItem().toString();
		
		return durch.equals("Ja");
	}
	
	/**
	 * Gibt den Inhalt von <code>txtSuche</code> als String zurück
	 * 
	 * @return der Inhalt aus dem Textfeld
	 */
	public String getTextSuche() {
		return txtSuche.getText();
	}
	
	/**
	 * Gibt den Inhalt von <code>comboSort</code> als String zurück
	 * 
	 * @return der Inhalt aus dem ausgewählten ComboBox
	 */
	public String getTextComboSort() {
		return comboSort.getSelectedItem().toString();
	}
	
	/**
	 * Gibt den Inhalt von <code>txtTitel</code> als String zurück
	 * 
	 * @return der Inhalt aus dem Textfeld
	 */
	public String getTextTitel() {
		return txtTitel.getText();
	}
	
	/**
	 * Setzt den Inhalt von <code>txtTitel</code>
	 * 
	 * @param titel der zu setzende Inhalt
	 */
	public void setTextTitel(String titel) {
		txtTitel.setText(titel);
	}
	
	/**
	 * Gibt den Inhalt von <code>comboPlattform</code> als String zurück
	 * 
	 * @return der Inhalt aus dem ausgewählten ComboBox
	 */
	public String getTextPlattform() {
		return comboPlattform.getSelectedItem().toString();
	}
	
	/**
	 * Setzt die Auswahl für <code>comboPlattform</code>
	 * 
	 * @param plattform die zu setzende Auswahl
	 */
	public void setTextPlattform(String plattform) {
		comboPlattform.setSelectedItem(plattform);
	}
	
	/**
	 * Gibt den Inhalt von <code>txtJahr</code> zurück
	 * 
	 * @return der Inhalt aus dem Textfeld
	 */
	public String getTextJahr() {
		return txtJahr.getText();		
	}
	
	/**
	 * Setzt den Inhalt von <code>txtJahr</code>
	 * 
	 * @param jahr der zu setzende Inhalt
	 */
	public void setTextJahr(int jahr) {
		txtJahr.setText(jahr + "");
	}
	
	/**
	 * Gibt den Inhalt von <code>comboDurchgespielt</code> als String zurück
	 * 
	 * @return der Inhalt aus dem ausgewählten ComboBox
	 */
	public String getTextDurchgespielt() {
		return comboDurchgespielt.getSelectedItem().toString();
	}
	
	/**
	 * Setzt die Auswahl für <code>comboDurchgespielt</code>
	 * 
	 * @param durchgespielt der Boolean-Wert, der die Auswahl definiert
	 */
	public void setTextDurchgespielt(boolean durchgespielt) {
		if(durchgespielt == true) {
			comboDurchgespielt.setSelectedItem("Ja");
		} else {
			comboDurchgespielt.setSelectedItem("Nein");
		}
	}
	
	/**
	 * Gibt die Nummer der ausgewählte Zeile zurück
	 * 
	 * @return die Position der ausgewählte Zeile als Integer
	 */
	public int getAusgewaehlteZeile() {
		return tabelle.getSelectedRow();
	}
	
	/**
	 * Gibt die Nummer der ausgewählten Zeilen zurück
	 * 
	 * @return die Positionen der ausgewählten Zeilen
	 */
	public int[] getAusgewaehlteZeilen() {
		return tabelle.getSelectedRows();
	}
	
	/**
	 * Prüft, ob eine Zeile ausgewählte ist
	 * 
	 * @return true, wenn eine Zeile ausgewählt ist
	 */
	public boolean istEintragAusgewaehlt() {
		return tabelle.getSelectedRow() >= 0;
	}
	
	/**
	 * Schließt das Dialogfenster (Hinzufügen/Bearbeiten)
	 */
	public void dialogFensterSchliessen() {
		dialogFenster.dispose();
	}
	
}
