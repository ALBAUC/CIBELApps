package es.unican.appriesgospersonales.activities.apps;

import static org.mockito.Mockito.verify;

public class AppsPresenterTest {
//
//    private AppsPresenter sut;
//
//    @Mock
//    private IAppsContract.View viewMock;
//    @Mock
//    private AplicacionDao aplicacionDaoMock;
//    @Mock
//    private CategoriaDao categoriaDaoMock;
//    @Mock
//    private PerfilDao perfilDaoMock;
//    @Mock
//    private IAppWiseRepository repositoryMock;
//    @Mock
//    private DaoSession daoSessionMock;
//    @Mock
//    private MyApplication myApplicationMock;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        sut = new AppsPresenter(viewMock, repositoryMock);
//    }
//
//    private void configInitCorrecto() {
//        Control[] controles = new Control[1];
//        Riesgo[] riesgos = new Riesgo[1];
//        Categoria[] categorias = new Categoria[1];
//        Aplicacion[] aplicaciones = new Aplicacion[1];
//
//        when(viewMock.getMyApplication()).thenReturn(myApplicationMock);
//        when(myApplicationMock.getDaoSession()).thenReturn(daoSessionMock);
//        when(daoSessionMock.getAplicacionDao()).thenReturn(aplicacionDaoMock);
//        when(repositoryMock.getControles()).thenReturn(controles);
//        when(repositoryMock.getRiesgos()).thenReturn(riesgos);
//        when(repositoryMock.getCategorias()).thenReturn(categorias);
//        when(repositoryMock.getElementosDigitales(null)).thenReturn(aplicaciones);
//    }
//
//    @Test
//    public void testInitCorrecto() {
//        // UAP.1a
//        configInitCorrecto();
//
//        sut.init();
//        verify(viewMock).showLoadCorrect(0);
//    }
//
//    @Test
//    public void testInitErrorControles() {
//        // UAP.1b
//        Riesgo[] riesgos = new Riesgo[1];
//        Categoria[] categorias = new Categoria[1];
//        Aplicacion[] aplicaciones = new Aplicacion[1];
//
//        when(viewMock.getMyApplication()).thenReturn(myApplicationMock);
//        when(myApplicationMock.getDaoSession()).thenReturn(daoSessionMock);
//        when(daoSessionMock.getAplicacionDao()).thenReturn(aplicacionDaoMock);
//        when(repositoryMock.getControles()).thenReturn(null);
//        when(repositoryMock.getRiesgos()).thenReturn(riesgos);
//        when(repositoryMock.getCategorias()).thenReturn(categorias);
//        when(repositoryMock.getElementosDigitales(null)).thenReturn(aplicaciones);
//
//        sut.init();
//        verify(viewMock).showLoadError();
//    }
//
//    @Test
//    public void testInitErrorRiesgos() {
//        // UAP.1c
//        Control[] controles = new Control[1];
//        Categoria[] categorias = new Categoria[1];
//        Aplicacion[] aplicaciones = new Aplicacion[1];
//
//        when(viewMock.getMyApplication()).thenReturn(myApplicationMock);
//        when(myApplicationMock.getDaoSession()).thenReturn(daoSessionMock);
//        when(daoSessionMock.getAplicacionDao()).thenReturn(aplicacionDaoMock);
//        when(repositoryMock.getControles()).thenReturn(controles);
//        when(repositoryMock.getRiesgos()).thenReturn(null);
//        when(repositoryMock.getCategorias()).thenReturn(categorias);
//        when(repositoryMock.getElementosDigitales(null)).thenReturn(aplicaciones);
//
//        sut.init();
//        verify(viewMock).showLoadError();
//    }
//
//    @Test
//    public void testInitErrorCategorias() {
//        // UAP.1d
//        Control[] controles = new Control[1];
//        Riesgo[] riesgos = new Riesgo[1];
//        Aplicacion[] aplicaciones = new Aplicacion[1];
//
//        when(viewMock.getMyApplication()).thenReturn(myApplicationMock);
//        when(myApplicationMock.getDaoSession()).thenReturn(daoSessionMock);
//        when(daoSessionMock.getAplicacionDao()).thenReturn(aplicacionDaoMock);
//        when(repositoryMock.getControles()).thenReturn(controles);
//        when(repositoryMock.getRiesgos()).thenReturn(riesgos);
//        when(repositoryMock.getCategorias()).thenReturn(null);
//        when(repositoryMock.getElementosDigitales(null)).thenReturn(aplicaciones);
//
//        sut.init();
//        verify(viewMock).showLoadError();
//    }
//
//    @Test
//    public void testInitErrorApps() {
//        // UAP.1e
//        Control[] controles = new Control[1];
//        Riesgo[] riesgos = new Riesgo[1];
//        Categoria[] categorias = new Categoria[1];
//
//        when(viewMock.getMyApplication()).thenReturn(myApplicationMock);
//        when(myApplicationMock.getDaoSession()).thenReturn(daoSessionMock);
//        when(daoSessionMock.getAplicacionDao()).thenReturn(aplicacionDaoMock);
//        when(repositoryMock.getControles()).thenReturn(controles);
//        when(repositoryMock.getRiesgos()).thenReturn(riesgos);
//        when(repositoryMock.getCategorias()).thenReturn(categorias);
//        when(repositoryMock.getElementosDigitales(null)).thenReturn(null);
//
//        sut.init();
//        verify(viewMock).showLoadError();
//    }
//
//    @Test
//    public void testGetCategoriasExisten() {
//        // UAP.2a
//        configInitCorrecto();
//        List<Categoria> categorias = new ArrayList<>();
//        categorias.add(new Categoria());
//
//        when(daoSessionMock.getCategoriaDao()).thenReturn(categoriaDaoMock);
//        when(categoriaDaoMock.loadAll()).thenReturn(categorias);
//
//        sut.init();
//        List<Categoria> categoriasSut = sut.getCategorias();
//        assert(categorias.size() == categoriasSut.size());
//        assert(categoriasSut.containsAll(categorias));
//        verify(categoriaDaoMock).loadAll();
//    }
//
//    @Test
//    public void testGetCategoriasNoExisten() {
//        // UAP.2b
//        configInitCorrecto();
//        List<Categoria> categorias = new ArrayList<>();
//
//        when(daoSessionMock.getCategoriaDao()).thenReturn(categoriaDaoMock);
//        when(categoriaDaoMock.loadAll()).thenReturn(categorias);
//
//        sut.init();
//        assert(sut.getCategorias().isEmpty());
//        verify(categoriaDaoMock).loadAll();
//    }
//
//    @Test
//    public void testGetCategoriasError() {
//        // UAP.2c
//        configInitCorrecto();
//
//        when(daoSessionMock.getCategoriaDao()).thenReturn(categoriaDaoMock);
//        when(categoriaDaoMock.loadAll()).thenThrow(new SQLiteException());
//
//        sut.init();
//        assert(sut.getCategorias() == null);
//        verify(categoriaDaoMock).loadAll();
//        verify(viewMock).showLoadError();
//    }
//
//    @Test
//    public void testGetPerfilAppsExist() {
//        // UAP.3a
//        configInitCorrecto();
//        List<Aplicacion> apps = new ArrayList<>();
//        apps.add(new Aplicacion());
//        Perfil p = new Perfil(1L);
//        p.setAppsAnhadidas(apps);
//
//        when(daoSessionMock.getPerfilDao()).thenReturn(perfilDaoMock);
//        when(perfilDaoMock.load(1L)).thenReturn(p);
//
//        sut.init();
//        assert(sut.getPerfilDElements() == apps);
//    }
//
//    @Test
//    public void testGetPerfilAppsNoExist() {
//        // UAP.3b
//        configInitCorrecto();
//        Perfil p = new Perfil(1L);
//
//        when(daoSessionMock.getPerfilDao()).thenReturn(perfilDaoMock);
//        when(perfilDaoMock.load(1L)).thenReturn(p);
//
//        sut.init();
//        assert(sut.getPerfilDElements().isEmpty());
//    }
//
//    @Test
//    public void testGetPerfilAppsError() {
//        // UAP.3c
//        configInitCorrecto();
//        Perfil p = new Perfil(1L);
//
//        when(daoSessionMock.getPerfilDao()).thenReturn(perfilDaoMock);
//        when(perfilDaoMock.load(1L)).thenThrow(new SQLiteException());
//
//        sut.init();
//        assert(sut.getPerfilDElements() == null);
//        verify(viewMock).showLoadError();
//    }

//    @Test
//    public void testGetAppByName() {
//        // UAP.4a
//        configInitCorrecto();
//        String nombreApp = "Facebook";
//        Aplicacion a = new Aplicacion();
//        a.setNombre(nombreApp);
//
//        when(aplicacionDao.queryBuilder().where
//                (AplicacionDao.Properties.Nombre.like(nombreApp)).unique())
//                .thenReturn(a);
//
//        sut.init();
//        assert(sut.getAppByName(nombreApp) == a);
//        verify(aplicacionDao).queryBuilder().where
//                (AplicacionDao.Properties.Nombre.like(nombreApp)).unique();
//    }

}
