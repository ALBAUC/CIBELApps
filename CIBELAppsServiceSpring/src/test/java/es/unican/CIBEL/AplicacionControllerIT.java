package es.unican.CIBEL;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AplicacionControllerIT {
	
//	@Autowired
//    private MockMvc mockMvc;
//	
//	@Test
//	public void testGetAplicaciones() throws Exception {
//		// IAC.1a
//		mockMvc.perform(get("/apps?categoria=Redes Sociales")
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(5))));
//		
//		// IAC.1b
//		mockMvc.perform(get("/apps?categoria=XXX")
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$", hasSize(0)));
//		
//		// IAC.1c
//		mockMvc.perform(get("/apps")
//				.contentType(MediaType.APPLICATION_JSON))
//        		.andExpect(status().isOk())
//        		.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(50))));
//	}
//	
//	@Test
//	public void testGetAplicacion() throws Exception {
//		// IAC.2a
//		mockMvc.perform(get("/apps/Facebook"))
//        		.andExpect(status().isOk())
//        		.andExpect(jsonPath("$.nombre", is("Facebook")));
//		
//		// IAC.2b
//		mockMvc.perform(get("/apps/XXX"))
//        		.andExpect(status().isNotFound());
//	}
}
