package usuario;



import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import grupo.Grupo;
import grupo.IGrupo;
import pago.Pago;
import pago.IPago;
import gasto.Gasto;
import gasto.IGasto;

class UsuarioTest {
	// Fixtures
	Usuario usuario1, usuario2;
	ArrayList<String> notificaciones = new ArrayList<>();
	// No necesitamos mocks ni que las clases estén implementadas, porque solo los vamos a crear
	ArrayList<IGrupo> grupos = new ArrayList<>();
	ArrayList<IGasto> gastos = new ArrayList<>();
	ArrayList<IPago> pagos = new ArrayList<>();

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	
	
	@Nested
	@DisplayName("Pruebas de creación de usuarios")
	class Constructores{

		@ParameterizedTest
		@DisplayName("Verificación de correos electrónicos no válidos")
		@NullAndEmptySource
		@CsvSource({"@", ".", "@.", "nombre@", "nombre@.", "nombre@.dominio", "nombre@@dominio.com", "nombre.apellido@dominio", "nombre.apellido@dominio."})
		void testCorreoNoValido(String correo) {
			usuario1 = new Usuario(1, "nombreUsuario", "nombreReal", correo, LocalDate.of(2000, Month.JANUARY, 1), "contrasena", "datosBancarios", 
					notificaciones, grupos, gastos, pagos);
			usuario2 = new Usuario(2, "nombreUsuario", "nombreReal", correo, LocalDate.of(2000, Month.JANUARY, 1), "contrasena", "datosBancarios");
			
			assertAll( () ->{assertNotEquals(usuario1.getCorreoElectronico(), correo, "Un correo no válido ha sido validado (Constructor general)");},
					()->{assertNotEquals(usuario2.getCorreoElectronico(), correo, "Un correo no válido ha sido validado (Constructor específico)");}); 
			
		}
		
		@ParameterizedTest
		@DisplayName("Verificación de correos electrónicos válidos")
		@CsvSource({"nombre@dominio.com", "nombre.apellido@dominio.com", "nombre.....@dominio.es", "nombre@dominio......com", "·$%&|.#12{].-@dominio.es"})
		void testCorreoValido(String correo) {
			usuario1 = new Usuario(1, "nombreUsuario", "nombreReal", correo, LocalDate.of(2000, Month.JANUARY, 1), "contrasena", "datosBancarios", 
					notificaciones, grupos, gastos, pagos);
			usuario2 = new Usuario(2, "nombreUsuario", "nombreReal", correo, LocalDate.of(2000, Month.JANUARY, 1), "contrasena", "datosBancarios");
			
			assertAll( () ->{assertEquals(usuario1.getCorreoElectronico(), correo, "Un correo válido ha sido invalidado (Constructor general)");},
					()->{assertEquals(usuario2.getCorreoElectronico(), correo, "Un correo válido ha sido invalidado (Constructor específico)");}); 
			
		}
		
		@ParameterizedTest
		@DisplayName("Verificación de datos bancarios no válidos")
		@NullAndEmptySource
		@CsvSource({"111111", "¿?", "ES", "ES789456", "ES012345678901234567890", "E012345678901234567890123", "ES0123456789/01234567890", "E/01234567890123456789012"})
		void testDatosNoValido(String datos) {
			usuario1 = new Usuario(1, "nombreUsuario", "nombreReal", "correoElectronico", LocalDate.of(2000, Month.JANUARY, 1), "contrasena", datos, 
					notificaciones, grupos, gastos, pagos);
			usuario2 = new Usuario(2, "nombreUsuario", "nombreReal", "correoElectronico", LocalDate.of(2000, Month.JANUARY, 1), "contrasena", datos);
			
			assertAll( () ->{assertNotEquals(usuario1.getDatosBancarios(), datos, "Unos datos bancarios no válidos han sido validados (Constructor general)");},
					()->{assertNotEquals(usuario2.getDatosBancarios(), datos, "Unos datos bancarios no válidos han sido validados (Constructor específico)");}); 
			
		}
		
		@ParameterizedTest
		@DisplayName("Verificación de datos bancarios válidos")
		@NullAndEmptySource
		@CsvSource({"ES0123456789012345678901", "QW9876543210987654321098", "II1111111111111111111111"})
		void testDatosValido(String datos) {
			usuario1 = new Usuario(1, "nombreUsuario", "nombreReal", "correoElectronico", LocalDate.of(2000, Month.JANUARY, 1), "contrasena", datos, 
					notificaciones, grupos, gastos, pagos);
			usuario2 = new Usuario(2, "nombreUsuario", "nombreReal", "correoElectronico", LocalDate.of(2000, Month.JANUARY, 1), "contrasena", datos);
			
			assertAll( () ->{assertEquals(usuario1.getDatosBancarios(), datos, "Unos datos bancarios válidos han sido invalidados (Constructor general)");},
					()->{assertEquals(usuario2.getDatosBancarios(), datos, "Unos datos bancarios válidos han sido invalidados (Constructor específico)");}); 
			
		}
		
		@ParameterizedTest
		@DisplayName("Verificación de contraseñas no válidas")
		@NullAndEmptySource
		@CsvSource({"123", "QQQQQQ", "||||||", "Nombre123", "nombrE", "simbolo!"})
		void testContrasenaNoValida(String contrasena) {
			usuario1 = new Usuario(1, "nombreUsuario", "nombreReal", "correoElectronico", LocalDate.of(2000, Month.JANUARY, 1), contrasena, "datos", 
					notificaciones, grupos, gastos, pagos);
			usuario2 = new Usuario(2, "nombreUsuario", "nombreReal", "correoElectronico", LocalDate.of(2000, Month.JANUARY, 1), contrasena, "datos");
			
			assertAll( () ->{assertNotEquals(usuario1.getContrasena(), contrasena, "Una contraseña no válida ha sido validada (Constructor general)");},
					()->{assertNotEquals(usuario2.getContrasena(), contrasena, "Una contraseña no válida ha sido validada (Constructor específico)");}); 
			
		}
		
		@ParameterizedTest
		@DisplayName("Verificación de contraseñas válidas")
		@NullAndEmptySource
		@CsvSource({"Nombr€", "!Nombre!", "nombrE!", "Seg&uridad", "S!·$%&/()"})
		void testContrasenaValida(String contrasena) {
			usuario1 = new Usuario(1, "nombreUsuario", "nombreReal", "correoElectronico", LocalDate.of(2000, Month.JANUARY, 1), contrasena, "datos", 
					notificaciones, grupos, gastos, pagos);
			usuario2 = new Usuario(2, "nombreUsuario", "nombreReal", "correoElectronico", LocalDate.of(2000, Month.JANUARY, 1), contrasena, "datos");
			
			assertAll( () ->{assertEquals(usuario1.getContrasena(), contrasena, "Una contraseña válida ha sido invalidada (Constructor general)");},
					()->{assertEquals(usuario2.getContrasena(), contrasena, "Una contraseña válida ha sido invalidada (Constructor específico)");}); 
			
		}
		
		
		
		
	}
	
	
	


}
