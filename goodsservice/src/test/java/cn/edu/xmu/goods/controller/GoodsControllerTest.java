package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.GoodsServiceApplication;
import cn.edu.xmu.ooad.util.JwtHelper;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest(classes = GoodsServiceApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GoodsControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void getGoodsSpuStates() throws Exception {
        String responseString=this.mvc.perform(get("/skus/states"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"code\": 0,\n" +
                "      \"name\": \"未上架\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 4,\n" +
                "      \"name\": \"上架\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 6,\n" +
                "      \"name\": \"已删除\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

//    @Test
//    public void getGoodsSkus() {
//
//    }
//
//    @Test
//    void getSkuDetailedById() {
//    }
//
//    @Test
//    void createSku() {
//    }
//
//    @Test
//    void uploadSkuImg() {
//    }
//
//    @Test
//    void deleteSku() {
//    }
//
//    @Test
//    void updateSku() {
//    }
//
//    @Test
//    void getSpuById() {
//    }
//
//    @Test
//    void getSkuBySid() {
//    }
//
//    @Test
//    void createSpu() {
//    }
//
//    @Test
//    void uploadSpuImg() {
//    }
//
//    @Test
//    void updateSpu() {
//    }
//
//    @Test
//    void deleteSpu() {
//    }
//
//    @Test
//    void putGoodsOnSale() {
//    }
//
//    @Test
//    void putOffGoodsOnSale() {
//    }
//
//    @Test
//    void addFloatingPrice() {
//    }
//
//    @Test
//    void invalidFloatPrice() {
//    }
private String login(String userName, String password) throws Exception{
    String token = new JwtHelper().createToken(1L, 0L, 3600);
    return token;
}
}