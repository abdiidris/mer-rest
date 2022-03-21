package com.ame.rest.extension;

import org.junit.Test;
import org.junit.runner.RunWith;

import net.minidev.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class extensionIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void registerExtension(){

    }

    @Test
    public void  onlyDeveloperCanRegisterExtension(){

    }

    @Test
    public void getAllExtensionByDeveloper(){

    }

    @Test
    public void getAllExtensions(){

    }

    @Test
    public void extensionLinkConstraint(){

    }
}
