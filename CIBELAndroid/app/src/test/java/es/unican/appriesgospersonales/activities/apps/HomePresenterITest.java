package es.unican.appriesgospersonales.activities.apps;

import static org.mockito.Mockito.verify;

import android.os.Build;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import es.unican.cibel.activities.activos.CatalogoPresenter;
import es.unican.cibel.activities.activos.ICatalogoContract;
import es.unican.cibel.common.MyApplication;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1}, application = MyApplication.class)
public class HomePresenterITest {

    private CatalogoPresenter sut;

    @Mock
    private ICatalogoContract.View viewMock;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        sut = new CatalogoPresenter(viewMock);
    }

//    @Test
//    public void testInitCorrecto() {
//        // IAP.1a
//        when(viewMock.getMyApplication()).thenReturn(ApplicationProvider.getApplicationContext());
//
//        sut.init();
//        verify(viewMock).showLoadCorrect(0);
//    }
}
