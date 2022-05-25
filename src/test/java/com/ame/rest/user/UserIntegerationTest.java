//https://github.com/eugenp/tutorials/blob/master/spring-boot-modules/spring-boot-testing/src/test/java/com/baeldung/boot/testing/EmployeeRestControllerIntegrationTest.java

package com.ame.rest.user;

import net.minidev.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegerationTest {

  @Autowired
  private MockMvc mvc;

  @Test
  public void createUserTest() throws Exception {
    JSONObject chris = new JSONObject();

    chris.put("email", "chris@gmail.com");
    chris.put("password", "JnKing23%%");
    chris.put("type", "writer");

    mvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON).content(chris.toJSONString()))
        .andExpect(status().isOk());
  }

  @Test
  public void userExistsTest() throws Exception {
    JSONObject peter = new JSONObject();

    peter.put("email", "peter@gmail.com");
    peter.put("password", "JnKing23%%");
    peter.put("type", "writer");

    mvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON).content(peter.toJSONString()))
        .andExpect(status().isOk());

    MvcResult mvcResult = mvc
        .perform(post("/user/register").contentType(MediaType.APPLICATION_JSON).content(peter.toJSONString()))
        .andExpect(status().isNotAcceptable()).andReturn();

    assertTrue(mvcResult.getResponse().getContentAsString().contains("User already exists"));
  }

  @Test
  public void badUserType() throws Exception{
    JSONObject ed = new JSONObject();

    ed.put("email", "ed@asdasd");
    ed.put("password", "JnKing23%%");
    ed.put("type", "good");

    MvcResult mvcResult = mvc
        .perform(post("/user/register").contentType(MediaType.APPLICATION_JSON).content(ed.toJSONString()))
        .andExpect(status().isBadRequest()).andReturn();

    assertTrue(mvcResult.getResolvedException().toString().contains("Could not resolve type id 'good' as a subtype of `com.ame.rest.user.User"));
  }

  @Test
  public void badEmailTest() throws Exception {
    JSONObject ed = new JSONObject();

    ed.put("email", "ed@asdasd");
    ed.put("password", "JnKing23%%");
    ed.put("type", "writer");

    MvcResult mvcResult = mvc
        .perform(post("/user/register").contentType(MediaType.APPLICATION_JSON).content(ed.toJSONString()))
        .andExpect(status().isBadRequest()).andReturn();

    assertTrue(mvcResult.getResponse().getContentAsString().contains("password or email doesn't meet requirements"));
  }

  @Test
  public void badPasswordTest() throws Exception {
    JSONObject ed = new JSONObject();

    ed.put("email", "ed@gmail.com");
    ed.put("password", "123");
    ed.put("type", "writer");

    MvcResult mvcResult = mvc
        .perform(post("/user/register").contentType(MediaType.APPLICATION_JSON).content(ed.toJSONString()))
        .andExpect(status().isBadRequest()).andReturn();

    assertTrue(mvcResult.getResponse().getContentAsString().contains("password or email doesn't meet requirements"));
  }

  @Test
  public void badLogin() throws Exception {
    JSONObject dave = new JSONObject();

    dave.put("email", "dave@gmail.com");
    dave.put("password", "JnKing23%%");
    dave.put("type", "developer");

    mvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON).content(dave.toJSONString()))
        .andExpect(status().isOk());

    dave.put("password", "asd");

    MvcResult mvcResult = mvc
        .perform(post("/user/login").contentType(MediaType.APPLICATION_JSON).content(dave.toJSONString()))
        .andExpect(status().isNotAcceptable()).andReturn();

    assertTrue(mvcResult.getResponse().getContentAsString().contains("Invalid credentials"));
  }

  @Test
  public void loginTest() throws Exception {
    JSONObject john = new JSONObject();

    john.put("email", "johnsmit@gmail.com");
    john.put("password", "JnKing23%%");
    john.put("type", "writer");

    mvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON).content(john.toJSONString()))
        .andExpect(status().isOk());

    MvcResult mvcResult = mvc
        .perform(post("/user/login").contentType(MediaType.APPLICATION_JSON).content(john.toJSONString()))
        .andExpect(status().isOk()).andReturn();

    assertTrue(mvcResult.getResponse().getContentAsString().contains("Bearer "));
  }
}
