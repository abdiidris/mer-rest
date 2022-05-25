package com.ame.rest.extension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
// import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import io.netty.util.internal.StringUtil;
import net.minidev.json.JSONObject;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc

// allow non static @BeforeEach methods
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

// clear context after every test
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ExtensionIntegrationTest {

    @Autowired
    private MockMvc mvc;

    JSONObject validExtension = null;
    String developerToken = "";
    String writerToken = "";
    List<JSONObject> registeredExtensions = new ArrayList<JSONObject>();

    @BeforeEach
    public void start() throws Exception {
        // create the users

        JSONObject developer = new JSONObject();

        developer.put("email", "dave@gmail.com");
        developer.put("password", "JnKing23%%");
        developer.put("type", "developer");

        mvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON).content(developer.toJSONString()))
                .andExpect(status().isOk());

        // login and get the access token
        MvcResult mvcResult = mvc
                .perform(post("/user/login").contentType(MediaType.APPLICATION_JSON).content(developer.toJSONString()))
                .andExpect(status().isOk()).andReturn();

        this.developerToken = mvcResult.getResponse().getContentAsString();

        // create writer
        JSONObject writer = new JSONObject();

        writer.put("email", "writer@gmail.com");
        writer.put("password", "JnKing23%%");
        writer.put("type", "writer");

        mvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON).content(writer.toJSONString()))
                .andExpect(status().isOk());

        // login as the writer
        this.writerToken = mvc
                .perform(post("/user/login").contentType(MediaType.APPLICATION_JSON).content(writer.toJSONString()))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        // create a valid extension
        validExtension = new JSONObject();
        validExtension.put("name", "valid test extension");
        JSONObject links = new JSONObject();
        links.put(Extension.LINK_TYPE.EXECUTE.toString(), "www.validextension.com");
        links.put(Extension.LINK_TYPE.EDIT.toString(), "www.validextension.com");
        validExtension.put("links", links);

        // create and register some extensions
        JSONObject tableExtension = new JSONObject();
        tableExtension.put("name", "table extension");
        JSONObject tableExtensionLinks = new JSONObject();
        tableExtensionLinks.put(Extension.LINK_TYPE.EXECUTE.toString(), "www.tableExtension.com");
        tableExtensionLinks.put(Extension.LINK_TYPE.EDIT.toString(), "www.tableExtension.com");
        tableExtension.put("links", tableExtensionLinks);
        registerExtension(tableExtension);
        registeredExtensions.add(tableExtension);

        JSONObject graphExtension = new JSONObject();
        graphExtension.put("name", "graph extension");
        JSONObject graphExtensionLinks = new JSONObject();
        graphExtensionLinks.put(Extension.LINK_TYPE.EXECUTE.toString(), "www.graphExtension.com");
        graphExtensionLinks.put(Extension.LINK_TYPE.EDIT.toString(), "www.graphExtension.com");
        graphExtension.put("links", graphExtensionLinks);
        registerExtension(graphExtension);
        registeredExtensions.add(graphExtension);

    }

    public void registerExtension(JSONObject extension) throws Exception {
        mvc.perform(post("/extension/register").header(HttpHeaders.AUTHORIZATION, this.developerToken)
                .contentType(MediaType.APPLICATION_JSON).content(extension.toJSONString()))
                .andExpect(status().isOk());
    }

    @Test
    public void createExtension() throws Exception {
        MvcResult mvcResult = mvc
                .perform(post("/extension/register").header(HttpHeaders.AUTHORIZATION, this.developerToken)
                        .contentType(MediaType.APPLICATION_JSON).content(validExtension.toJSONString()))
                .andExpect(status().isOk()).andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains(ExtensionService.REGISTRATION_SUCCESS));
    }

    @Test
    public void badUserType() throws Exception {
        // attempt to create an extension as writer
        mvc.perform(post("/extension/register").header(HttpHeaders.AUTHORIZATION, this.writerToken)
                .contentType(MediaType.APPLICATION_JSON).content(validExtension.toJSONString()))
                .andExpect(status().isForbidden());

    }

    @Test
    public void missingExecutionLink() throws Exception {

        JSONObject badExtension = new JSONObject();
        badExtension.put("name", "test extension");

        JSONObject links = new JSONObject();
        links.put(Extension.LINK_TYPE.EDIT.toString(), "www.test.com");

        badExtension.put("links", links);

        MvcResult mvcResult = mvc
                .perform(post("/extension/register").header(HttpHeaders.AUTHORIZATION, this.developerToken)
                        .contentType(MediaType.APPLICATION_JSON).content(badExtension.toJSONString()))
                .andExpect(status().isBadRequest()).andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains(ExtensionService.MISSING_LINKS));

    }

    @Test
    public void nameNotUnique() throws Exception {

        // create an extension with a name of an extension that exists
        JSONObject badExtension = new JSONObject();
        badExtension.put("name", registeredExtensions.get(0).get("name"));
        JSONObject links = new JSONObject();
        links.put(Extension.LINK_TYPE.EDIT.toString(), "www.badExtension.com");
        links.put(Extension.LINK_TYPE.EXECUTE.toString(), "www.badExtension.com");
        badExtension.put("links", links);

        MvcResult mvcResult = mvc
                .perform(post("/extension/register").header(HttpHeaders.AUTHORIZATION, this.developerToken)
                        .contentType(MediaType.APPLICATION_JSON).content(badExtension.toJSONString()))
                .andExpect(status().isBadRequest()).andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains(ExtensionService.NAME_IS_TAKEN));

    }

    @Test
    public void getExtensionsAsBrowser() throws Exception {

        // get all extensions as writer
        MvcResult result = mvc.perform(get("/extension/all").header(HttpHeaders.AUTHORIZATION, this.writerToken))
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();

        assertFalse(StringUtil.isNullOrEmpty(jsonResponse));

        for (JSONObject jsonObject : registeredExtensions) {
            assertTrue(jsonResponse.contains(jsonObject.get("name").toString()));
        }
    }


}
