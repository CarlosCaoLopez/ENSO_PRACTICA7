package pago;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.internal.util.reflection.FieldInitializer;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import gasto.Gasto;
import gasto.IGasto;
import grupo.Grupo;
import grupo.IGrupo;
import usuario.IUsuario;
import usuario.Usuario;

class PagoTest {
	
	IPago pago1, pago2, pago3;
	
	/*
	 * AutoCloseable acl; // Clases simuladas
	 * 
	 * @Mock IGasto gasto;
	 * 
	 * @Mock IUsuario usuario;
	 * 
	 * @Mock IGrupo grupo;
	 * 
	 * @InjectMocks IPago pago;
	 */

	@BeforeEach
	void setUp() throws Exception {
		//acl = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void tearDown() throws Exception {
		//acl.close();
	}

	@Nested
	@DisplayName("Pruebas de creación de pagos")
	class Constructores{
		
		@Test
		@DisplayName("Verificación con grupo no nulo y vacío")
		void testGrupoNoValido() {
			IGrupo grupoMock = mock(Grupo.class);
			
			pago1 = new Pago(1, null);
			pago2 = new Pago(2, grupoMock);
			
			assertAll( ()->{assertNull(pago1.getGrupoGasto(), "Se crea un pago con un grupo nulo");},
					()->{assertNull(pago1.getGrupoGasto(), "Se crea un pago con un grupo vacío");}); 
			
		}
		
		@Test
		@DisplayName("Verificación con grupo sin gastos")
		void testGrupoSinGastos() {
			IUsuario usuarioMock = mock(Usuario.class);
			IGrupo grupoMock = mock(Grupo.class);

			// Añadimos el mock usuario al mock grupo para que el grupo no esté vacío
			grupoMock.anadirMiembro(usuarioMock);
			pago1 = new Pago(1, grupoMock);
			
			assertAll( ()->{assertNull(pago1.getGrupoGasto(), "Se crea un pago sin gastos");},
				    ()->{verify(grupoMock, times(1)).anadirMiembro(usuarioMock);}); 
		}
		
		@Test
		@DisplayName("Verificación con grupo sin personas")
		void testGrupoSinPersonas() {
			IGasto gastoMock = mock(Gasto.class);
			IGrupo grupoMock = mock(Grupo.class);

			// Añadimos el mock gasto al mock grupo para que el grupo tenga gastos
			grupoMock.anadirGasto(gastoMock);
			pago1 = new Pago(1, grupoMock);
			
			assertAll( ()->{assertNull(pago1.getGrupoGasto(), "Se crea un pago sin personas");},
				    ()->{verify(grupoMock, times(1)).anadirGasto(gastoMock);}); 
		}
		
		
	}
		
		
	//la función debe tomar los pagos de un usuario, obtener de cada pago lo que debe dar y hacer un sumatorio
		Double sumarvalores(IUsuario user) {
			Double resul=0.0;
			
			for (IPago elm : user.getPagos()) {
				for(Double num : elm.getCuotas().get(user).values()) {
					resul+=num;
				}
			}
			
			return resul;
		}
		
		//la función chequea si para un sumatorio de pagos dado es similar a uno calculado a mano
		boolean enrango(IUsuario user, double cantidad) {
			Double sumatorio_guardado=sumarvalores(user);
			return (sumatorio_guardado <= cantidad+0.01 && sumatorio_guardado >= cantidad-0.01);
		}
			
			
		@Test
		@DisplayName("Verificación de la funcion repartirgastos con inputs válidos")
		void testcorrecto() {
			
			//generacion de los usuarios
			IUsuario eva=new Usuario(1, "evauser", "eva", "nombre@dominio.com", LocalDate.of(2000, Month.JANUARY, 1), "Nombr€", "ES0000000000000000000000");
			IUsuario luis=new Usuario(2, "luisuser", "luis", "nombre@dominio.com", LocalDate.of(2000, Month.JANUARY, 1), "Nombr€", "ES0000000000000000000000");
			IUsuario marta=new Usuario(3, "martauser", "marta", "nombre@dominio.com", LocalDate.of(2000, Month.JANUARY, 1), "Nombr€", "ES0000000000000000000000");
			IUsuario juan=new Usuario(4, "juanuser", "juan", "nombre@dominio.com", LocalDate.of(2000, Month.JANUARY, 1), "Nombr€", "ES0000000000000000000000");
			
			//se crea la lista a introducir en el grupo
			ArrayList<IUsuario> lista=new ArrayList<IUsuario>();
			lista.add(eva);
			lista.add(luis);
			lista.add(marta);
			lista.add(juan);
			IGrupo loscuatro= eva.crearGrupo(99, "LosCuatro", "grupo", lista);
			
			//se imputan los gastos al grupo
			eva.anadirGasto(loscuatro, 11.30);
			eva.anadirGasto(loscuatro, 23.15);
			eva.anadirGasto(loscuatro, 2.05);
			luis.anadirGasto(loscuatro, 12.0);
			luis.anadirGasto(loscuatro, 17.49);
			marta.anadirGasto(loscuatro, 20.22);
			juan.anadirGasto(loscuatro, 5.75);
			
			//se dividen los gastos del grupo
			eva.dividirGastos(loscuatro);
			
			
			assertAll( 
					
					()->{assertTrue(enrango(eva,13.86),"La cantidad de eva está mal");},
					()->{assertTrue(enrango(luis,15.62),"La cantidad de luis está mal");},
					()->{assertTrue(enrango(marta,17.94),"La cantidad de marta está mal");},
					()->{assertTrue(enrango(juan,21.56),"La cantidad de juan está mal");},
					()->{assertFalse(luis.getNotificaciones().isEmpty(), "No se notifica a luis");},
					()->{assertFalse(marta.getNotificaciones().isEmpty(), "No se notifica a marta");},
					()->{assertFalse(juan.getNotificaciones().isEmpty(), "No se notifica a juan");},
					()->{assertFalse(eva.getNotificaciones().isEmpty(), "No se notifica a eva");});
			
		}
		
		
		
		
		
		
		// TODO no funciona este test
		@Test
		@DisplayName("Verificación de grupo con personas y gastos")
		void testGrupoValido() {
			IGasto gastoMock = mock(Gasto.class);
			IUsuario usuarioMock = mock(Usuario.class);
			IGrupo grupoMock = mock(Grupo.class);
			
			ArrayList<IGasto> gastos = new ArrayList<>();
			gastos.add(gastoMock);
			ArrayList<IUsuario> usuarios = new ArrayList<>();
			usuarios.add(usuarioMock);
			when(grupoMock.getUsuarios()).thenReturn(usuarios);
			when(grupoMock.getGastos()).thenReturn(gastos);

			pago1 = new Pago(1, grupoMock);
			
			assertAll( 
					()->{assertEquals(gastos, grupoMock.getGastos(), "Se crea un pago sin gastos");},
					()->{assertEquals(usuarios, grupoMock.getUsuarios(), "Se crea un pago sin personas");},
					()->{assertNotNull(pago1.getGrupoGasto(), "Pago válido no genera pago");});
		}
		
		
		
		
	}


