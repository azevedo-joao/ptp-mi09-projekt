package mediathek.tests;


import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mediathek.MediathekData;
import mediathek.Medien;

public class MediathekDataTest {
	
	static MediathekData d;
	static Medien m, n;
	
	@BeforeAll
	public static void init() {
		 d = new MediathekData();
		 m = new Medien("Test", "PC", 2022, false);
		 n = new Medien("Test 2", "Playstation 5", 2002, true);
	}
	
	@BeforeEach
	public void clear() {
		d.clearData();
	}
	
	@Test
	public void test_insert() {
		d.insertData(m);
		assertEquals(m.getName(), d.getMedien().get(0).getName());
		assertEquals(m.getPlattform(), d.getMedien().get(0).getPlattform());
		assertEquals(m.getVeroeffentlichung(), d.getMedien().get(0).getVeroeffentlichung());
		assertEquals(m.getDurchgespielt(), d.getMedien().get(0).getDurchgespielt());
	}

	@Test
	public void test_delete() {
		d.insertData(m);
		assertEquals(false, d.getMedien().isEmpty());
		d.deleteData(m);
		assertEquals(true, d.getMedien().isEmpty());
	}
	
	@Test
	public void test_update() {
		d.insertData(n);
		assertNotEquals(m.getName(), d.getMedien().get(0).getName());
		assertNotEquals(m.getPlattform(), d.getMedien().get(0).getPlattform());
		assertNotEquals(m.getVeroeffentlichung(), d.getMedien().get(0).getVeroeffentlichung());
		assertNotEquals(m.getDurchgespielt(), d.getMedien().get(0).getDurchgespielt());
		
		d.updateData(n, m);
		assertEquals(m.getName(), d.getMedien().get(0).getName());
		assertEquals(m.getPlattform(), d.getMedien().get(0).getPlattform());
		assertEquals(m.getVeroeffentlichung(), d.getMedien().get(0).getVeroeffentlichung());
		assertEquals(m.getDurchgespielt(), d.getMedien().get(0).getDurchgespielt());
	}
}
