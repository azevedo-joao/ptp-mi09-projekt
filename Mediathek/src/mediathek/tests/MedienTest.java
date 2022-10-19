package mediathek.tests;



import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import mediathek.Medien;

public class MedienTest {
	
	private Medien medien;
	String name = "Mario Kart 8 Deluxe";
	String plattform = "Nintendo Switch";
	int jahr = 2017;
	boolean durch = false;
	
	@BeforeEach
	public void init() {
		
		medien = new Medien(name, plattform, jahr, durch);
	}
	
	
	@Test
	public void test_Medien() {
		
		assertNotNull(medien);
	}
	
	@Test
	public void test_getName() {
		
		assertEquals(name, medien.getName());
	}
	
	@Test
	public void test_setName() {
		
		String test = "Diablo III";
		
		assertEquals(name, medien.getName());
		medien.setName(test);
		
		assertNotEquals(name, medien.getName());
		assertEquals(test, medien.getName());
	}
	
	@Test
	public void test_getPlattform() {
		
		assertEquals(plattform, medien.getPlattform());
		assertNotEquals("Pokémon Snap", medien.getPlattform());
	}
	
	@Test
	public void test_setPlattform() {
		
		String test = "XBOX One";
		
		assertEquals(plattform, medien.getPlattform());
		
		medien.setPlattform(test);
		
		assertNotEquals(plattform, medien.getPlattform());
		assertEquals(test, medien.getPlattform());
	}
	
	@Test
	public void test_getVeroeffentlichung() {
		
		assertEquals(jahr, medien.getVeroeffentlichung());
		assertNotEquals(2022, medien.getVeroeffentlichung());
	}
	
	@Test
	public void test_setVeroeffentlichung() {
		
		int test = 1998;
		
		assertEquals(jahr, medien.getVeroeffentlichung());
		
		medien.setVeroeffentlichung(test);
		
		assertNotEquals(jahr, medien.getVeroeffentlichung());
		assertEquals(test, medien.getVeroeffentlichung());
	}
}
