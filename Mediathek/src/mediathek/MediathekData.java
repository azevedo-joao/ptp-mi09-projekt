package mediathek;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.table.DefaultTableModel;

/**
 * Datenmodell-Klasse für die Mediathek
 * 
 *  Hier wird die Verbindung zur Datenbank hergestellt
 *  und die Aktionen implementiert
 * 
 * @author Jonah Lüdemann
 * 
 * @version August 2022
 */
public class MediathekData {
	private Connection con = null;
	private Statement stmt = null;
	private DefaultTableModel dm;

	private String search;
	private String console;
	
	private ArrayList<String> eintraege;

	/**
	 * Herstelltung der Verbindung zur Datenbank
	 */
	public MediathekData() {
		
		dm = new DefaultTableModel();
		search = "";
		console = "";

		eintraege = new ArrayList<String>();
		
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			con = DriverManager.getConnection("jdbc:hsqldb:file:datenbank//; shutdown=true", "SA", "");
			createTable("Mediathek");
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Erstellt eine neue Table in der Datenbank sofern noch keine mit dem Namen
	 * existiert
	 * 
	 * @param name Der Name der zu erstellenden Table
	 */
	public void createTable(String name) {
		name = name.toUpperCase();
		try {
			DatabaseMetaData meta = con.getMetaData();
			ResultSet res = meta.getTables(null, null, name, null);

			if (!res.next()) {
				stmt = con.createStatement();
				stmt.executeUpdate("CREATE TABLE " + name
						+ " (Name VARCHAR(255) NOT NULL, Plattform VARCHAR(255), Veroeffentlichung INT, Durchgespielt BOOLEAN)");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Fügt ein Medium in die Datenbank ein
	 * 
	 * @param medium Das Medium, das eingef�gt werden soll
	 */
	public void insertData(Medien medium) {
		try {
			String sql = "INSERT INTO Mediathek VALUES ('" + medium.getName() + "' , '" + medium.getPlattform() + "' , "
					+ medium.getVeroeffentlichung() + ", " + medium.getDurchgespielt() + ")";
			stmt = con.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Fügt eine Liste von Medien in die Datenbank ein
	 * 
	 * @param medien Die Liste, die eingef�gt werden soll
	 */
	public void insertList(List<Medien> medien) {
		for (Iterator<Medien> it = medien.iterator(); it.hasNext();) {
			insertData(it.next());
		}
	}

	/**
	 * Aktualisiert einen Eintrag in der Datenbank
	 * 
	 * @param medium Der Eintrag, der aktualisiert werden soll
	 * @param update Die Daten, die eingetragen werden sollen
	 */
	public void updateData(Medien medium, Medien update) {
		try {
			String sql = "UPDATE Mediathek SET Name = '" + update.getName() + "', Plattform = '" + update.getPlattform()
					+ "', Veroeffentlichung = " + update.getVeroeffentlichung() + ", Durchgespielt = "
					+ update.getDurchgespielt() + " WHERE Name = '" + medium.getName() + "' AND Plattform = '"
					+ medium.getPlattform() + "' AND Veroeffentlichung = " + medium.getVeroeffentlichung()
					+ " AND Durchgespielt = " + medium.getDurchgespielt();
			
			stmt = con.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gibt alle Einträge aus der Datenbank in der Konsole aus
	 */
	public void selectAll() {

		try {
			String sql = "SELECT * FROM Mediathek";
			stmt = con.createStatement();
			ResultSet res = stmt.executeQuery(sql);
			while (res.next()) {
				String name = res.getString(1);
				String plattform = res.getString(2);
				String veroeffentlichung = res.getString(3);				
				String eintrag = name + ", " + plattform + ", " + veroeffentlichung;
				eintraege.add(eintrag);
				System.out.println(eintrag);
			}
			res.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gibt eine Liste mit allen Medien aus der Datenbank aus
	 * 
	 * @return Liste mit den Eintr�gen aus der Datenbank
	 */
	public List<Medien> getMedien() {
		List<Medien> medien = new ArrayList<Medien>();
		try {
			String sql = "SELECT * FROM Mediathek";
			stmt = con.createStatement();
			ResultSet res = stmt.executeQuery(sql);
			while (res.next()) {
				medien.add(new Medien(res.getString(1), res.getString(2), res.getInt(3), res.getBoolean(4)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return medien;

	}

	/**
	 * Löscht ein Medium aus der Datenbank
	 * 
	 * @param medium Das Medium, dass gelöscht werden soll
	 */
	public void deleteData(Medien medium) {
		try {
			String sql = "DELETE FROM Mediathek WHERE Name = '" + medium.getName() + "' AND Plattform = '"
					+ medium.getPlattform() + "' AND Veroeffentlichung = " + medium.getVeroeffentlichung()
					+ " AND Durchgespielt = " + medium.getDurchgespielt();
			stmt = con.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Löscht alle Einträge aus der Datenbank
	 */
	public void clearData() {
		try {
			String sql = "DELETE FROM Mediathek";
			stmt = con.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Löscht alle Einträge aus der Datenbank und schreibt eine Liste mit
	 * Testdaten in die Datenbank
	 */
	public void insertTestList() {
		clearData();
		List<Medien> medien = new ArrayList<Medien>();
		medien.add(new Medien("Dark Souls", "PC", 2017, true));
		medien.add(new Medien("Mario Party", "Playstation 4", 2018, false));
		medien.add(new Medien("Mario Kart", "Nintendo Switch", 2019, true));
		medien.add(new Medien("Elden Ring", "Toaster", 2020, false));
		medien.add(new Medien("Monster Hunter World", "Playstation 5", 2021, false));
		medien.add(new Medien("World of Warcraft", "PC", 2022, true));
		insertList(medien);

	}

	/**
	 * Fügt dem DefaultTableModel den Inhalt der Datenbank hinzu
	 * 
	 * @param name der Name der Tabelle
	 */
	public void getTable(String name) {

		try {
			stmt = con.createStatement();
			ResultSet res = stmt.executeQuery("SELECT * FROM " + name);
			ResultSetMetaData rsmd = res.getMetaData();

			int spalten = rsmd.getColumnCount();
			String sp[] = new String[spalten];

			for (int i = 0; i < spalten; i++) {
				sp[i] = rsmd.getColumnName(i + 1);
				dm.addColumn(sp[i]);
			}

			Object zeile[] = new Object[spalten];

			while (res.next()) {

				for (int i = 0; i < spalten; i++) {

					zeile[i] = res.getString(i + 1);
				}
				dm.addRow(zeile);
			}
			dm.fireTableDataChanged();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Aktualisiert das TableModel und fügt alle Einträge aus der Datenbank ein
	 */
	public void tableUpdated(String tableName) {
		while (dm.getRowCount() > 0) {
			dm.removeRow(0);
		}

		try {
			stmt = con.createStatement();
			String sql = "SELECT * FROM " + tableName + " WHERE UPPER(Name) LIKE UPPER('%" + search
					+ "%') AND Plattform LIKE '%" + console + "%'";
			ResultSet res = stmt.executeQuery(sql);
			
			Object zeile[] = new Object[dm.getColumnCount()];
			while (res.next()) {
				for (int i = 0; i < dm.getColumnCount(); i++) {
					zeile[i] = res.getString(i + 1);
				}

				dm.addRow(zeile);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dm.fireTableDataChanged();
	}

	/*
	 * Gibt das TableModel zur�ck
	 */
	public DefaultTableModel getTableModel() {
		getTable("Mediathek");
		
		return dm;
	}

	/*
	 * Fgt alle Eintrge, die den String aus search im Namen haben und auf der
	 * angegebenen Konsole sind zurck. Wenn search oder console ein leerer String
	 * bergeben wird, werden alle in der Datenbank vorhandenen Eintrge zurckgegeben.
	 * 
	 * @param tableName Der Name der Table in der Datenbank
	 * 
	 * @param search Der gesuchte Name eines Spiels in der Datenbank
	 * 
	 * @param console Die Plattform auf der das Spiel sein soll
	 */
	public void searchTable(String tableName, String search, String console) {

		this.search = search;
		this.console = console;
		tableUpdated(tableName);
	}

	public void closeConnection() {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gibt eine Liste mit allen Konsolen, die in der Datenbank aufgelistet sind,
	 * zurck
	 * 
	 * @return Die Liste der Konsolen
	 */
	public String[] getConsoles() {
		int rows = 0;
		String sql = "SELECT DISTINCT Plattform FROM Mediathek";
		ResultSet res;
		try {
			stmt = con.createStatement();
			res = stmt.executeQuery(sql);
			
			while (res.next()) {
				++rows;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		String[] consoles = new String[rows];
		try {
			stmt = con.createStatement();
			res = stmt.executeQuery(sql);

			while (res.next()) {
				consoles[res.getRow() - 1] = res.getString(1);
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return consoles;

	}

	/**
	 * Löscht ausgewählte Einträge von der Datenbank
	 * 
	 * @param rows die ausgewählten Zeilen
	 */
	public void deleteSelection(int[] rows) {
		for (int i = 0; i < rows.length; i++) {
			String name = (String) dm.getValueAt(rows[i], 0);
			String plattform = (String) dm.getValueAt(rows[i], 1);
			int veroeffentlichung = Integer.parseInt((String) dm.getValueAt(rows[i], 2));
			boolean durchgespielt = Boolean.parseBoolean((String) dm.getValueAt(rows[i], 3));
			deleteData(new Medien(name, plattform, veroeffentlichung, durchgespielt));
		}
		tableUpdated("Mediathek");
	}
	
	/**
	 * Gibt den Medieneintrag zur ausgewählten Zeile zurück
	 * 
	 * @param row die ausgewählte Zeile
	 * @return der Eintrag, der der ausgewählten Zeile entspricht
	 */
	public Medien getSelectedRow(int row) {
		String name = (String) dm.getValueAt(row, 0);
		String plattform = (String) dm.getValueAt(row, 1);
		int veroeffentlichung = Integer.parseInt((String) dm.getValueAt(row, 2));
		boolean durchgespielt = Boolean.parseBoolean((String) dm.getValueAt(row, 3));
		return new Medien(name, plattform, veroeffentlichung, durchgespielt);
	}

	/**
	 * Aktualisiert den ausgewählten Eintrag
	 * 
	 * @param row die ausgewählte Zeile
	 * @param medium der zu aktualisierende Eintrag
	 */
	public void updateSelectedRow(int row, Medien medium) {
		String name = (String) dm.getValueAt(row, 0);
		String plattform = (String) dm.getValueAt(row, 1);
		int veroeffentlichung = Integer.parseInt((String) dm.getValueAt(row, 2));
		boolean durchgespielt = Boolean.parseBoolean((String) dm.getValueAt(row, 3));
		updateData(new Medien(name, plattform, veroeffentlichung, durchgespielt), medium);
	}
	
	/**
	 * Exportiert den Inhalt der Datenbank als Text
	 */
	public void datenbankTxtExportieren() {	
		eintraege.removeAll(eintraege);
		selectAll();
		Collections.sort(eintraege);
		
		try {
			String homeDir = System.getProperty("user.home");
			@SuppressWarnings("unused")
			File datei = new File(homeDir + "/datenbank.txt");
			FileWriter writer = new FileWriter(homeDir + "/datenbank.txt");
			
			writer.write("Unten werden alle Einträge aus der Mediathek aufgeführt:\n");
			writer.write("==============================================================\n\n");
			
			for(String eintrag: eintraege) {
				writer.write(eintrag + "\n");
			}
			
			writer.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}