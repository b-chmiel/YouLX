package com.youlx.api.rest.offer;

import com.youlx.api.Routes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OfferControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void accessibleForUnauthenticatedUser() throws Exception {
        mockMvc.perform(get(Routes.Offer.OFFERS)).andDo(print()).andExpect(status().isOk());
    }
}


//    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
//
//    @Test
//    public void testInsertObject() throws Exception {
//        String url = BASE_URL + "/object";
//        ObjectBean anObject = new ObjectBean();
//        anObject.setObjectId("33");
//        anObject.setUserId("4268321");
//        //... more
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson=ow.writeValueAsString(anObject );
//
//        mockMvc.perform(post(url).contentType(APPLICATION_JSON_UTF8)
//                        .content(requestJson))
//                .andExpect(status().isOk());
//    }