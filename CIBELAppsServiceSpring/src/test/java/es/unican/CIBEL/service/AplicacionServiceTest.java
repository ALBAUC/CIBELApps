package es.unican.CIBEL.service;


public class AplicacionServiceTest {
	
//	@Mock
//	private AplicacionRepository repository;
//	
//	@InjectMocks
//	private AplicacionService sut;
//	
//	@BeforeEach
//	public void setUp() {
//		MockitoAnnotations.openMocks(this);
//	}
//	
//	@Test
//	public void testAplicacionesExistentes() {
//		// UAS.1a
//		Aplicacion a1 = new Aplicacion();
//		Aplicacion a2 = new Aplicacion();
//		List<Aplicacion> apps = new ArrayList<Aplicacion>();
//		apps.add(a1); apps.add(a2);
//		
//		when(repository.findAll()).thenReturn(apps);
//		
//		assertEquals(apps, sut.aplicaciones());
//	}
//	
//	@Test
//	public void testAplicacionesNoExistentes() {
//		// UAS.1b
//		when(repository.findAll()).thenReturn(Collections.emptyList());
//		
//		assertEquals(Collections.emptyList(), sut.aplicaciones());
//	}
//	
//	@Test
//	public void testAplicacionesPorCategoria() {
//		// UAS.2a
//		Categoria categoria = new Categoria();
//		categoria.setNombre("Redes Sociales");
//		Aplicacion a1 = new Aplicacion();
//		Aplicacion a2 = new Aplicacion();
//		List<Aplicacion> apps = new ArrayList<Aplicacion>();
//		apps.add(a1); apps.add(a2);
//		
//		when(repository.findByCategoria(categoria)).thenReturn(apps);
//		
//		assertEquals(apps, sut.aplicacionesPorCategoria(categoria));
//		
//		// UAS.2b
//		when(repository.findByCategoria(null)).thenReturn(Collections.emptyList());
//		
//		assertEquals(Collections.emptyList(), sut.aplicacionesPorCategoria(null));
//	}
//	
//	@Test
//	public void testBuscaAplicacion() {
//		// UAS.3a
//		String nombreAplicacion = "Facebook";
//		Aplicacion a1 = new Aplicacion();
//		a1.setNombre(nombreAplicacion);
//		
//		when(repository.findByNombre(nombreAplicacion)).thenReturn(a1);
//		
//		assertEquals(a1, sut.buscaAplicacion(nombreAplicacion));
//		
//		// UAS.3b
//		String nombreAplicacionNoExistente = "XXX";
//		
//		when(repository.findByNombre(nombreAplicacionNoExistente)).thenReturn(null);
//		
//		assertNull(sut.buscaAplicacion(nombreAplicacionNoExistente));
//	}

}
