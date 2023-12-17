package es.unican.CIBEL.controller;


public class AplicacionControllerTest {
	
//	@Mock
//	private AplicacionService service;
//	
//	@Mock
//	private CategoriaService catService;
//	
//	@InjectMocks
//	private AplicacionController sut;
//	
//	@BeforeEach
//	public void setUp() {
//		MockitoAnnotations.openMocks(this);
//	}
//	
//	@Test
//	public void testGetAplicaciones() {
//		// UAC.1a
//		String nombreCategoria = "Redes sociales";
//		Categoria c = new Categoria();
//		c.setNombre(nombreCategoria);
//		Aplicacion a1 = new Aplicacion();
//		Aplicacion a2 = new Aplicacion();
//		List<Aplicacion> appsEsperadas = new ArrayList<Aplicacion>();
//		appsEsperadas.add(a1); appsEsperadas.add(a2);
//		
//		when(catService.buscaCategoriaPorNombre(nombreCategoria)).thenReturn(c);
//		when(service.aplicacionesPorCategoria(c)).thenReturn(appsEsperadas);
//		
//		assertEquals(appsEsperadas, sut.getAplicaciones(nombreCategoria));
//		
//		// UAC.1b
//		String nombreCategoriaNoExist = "XXX";
//		
//		when(catService.buscaCategoriaPorNombre(nombreCategoriaNoExist)).thenReturn(null);
//		when(service.aplicacionesPorCategoria(null)).thenReturn(Collections.emptyList());
//		
//		assertEquals(Collections.emptyList(), sut.getAplicaciones(nombreCategoriaNoExist));
//		
//		// UAC.1c
//		when(service.aplicaciones()).thenReturn(appsEsperadas);
//		
//		assertEquals(appsEsperadas, sut.getAplicaciones(null));
//	}
//	
//	@Test
//	public void testGetAplicacion() {
//		// UAC.2a
//		String nombreApp = "Facebook";
//		Aplicacion a1 = new Aplicacion();
//		a1.setNombre(nombreApp);
//		
//		when(service.buscaAplicacion(nombreApp)).thenReturn(a1);
//		
//		ResponseEntity<Aplicacion> response = sut.getAplicacion(nombreApp);
//		assertEquals(HttpStatus.OK, response.getStatusCode());
//		assertEquals(a1, response.getBody());
//		
//		// UAC.2b
//		String nombreAppNoExist = "XXX";
//		
//		when(service.buscaAplicacion(nombreAppNoExist)).thenReturn(null);
//		
//		ResponseEntity<Aplicacion> response2 = sut.getAplicacion(nombreAppNoExist);
//		assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
//	}
}
