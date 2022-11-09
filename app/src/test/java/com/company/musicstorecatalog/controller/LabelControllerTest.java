package com.company.musicstorecatalog.controller;

import com.company.musicstorecatalog.model.Label;
import com.company.musicstorecatalog.service.LabelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(LabelController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LabelControllerTest {


    @Autowired
    private MockMvc mockMvc;

    // The aim of this unit test is to test the controller and NOT the service layer.
    // Therefore mock the service layer.
    @MockBean
    private LabelService service;

    @Autowired
    //used to move between Objects and JSON
    private ObjectMapper mapper;

    private Label labelInput;
    private Label labelOut;
    private Label labelOut2;
    private Label labelBadInput;

    private List<Label> labelList;

    @Before
    public void setUp() {
//      input album
        labelInput = new Label();
        labelInput.setName("label 1");
        labelInput.setWebsite("label1.website.com");

//output album
        labelOut = new Label();
        labelOut.setName("label 1");
        labelOut.setWebsite("label1.website.com");
        labelOut.setId(1);

        labelOut2 = new Label();
        labelOut2.setName("test 2");
        labelOut2.setWebsite("label2.website.com");
        labelOut2.setId(2);

//        list of albums
        labelList = new ArrayList<>();
        labelList.add(labelOut);
        labelList.add(labelOut2);


//bad input album
        labelBadInput = new Label();
//        didnt include a name for the label... validation will send an error back
        labelBadInput.setWebsite("badInput@Instagram");


        when(service.createLabel(labelInput)).thenReturn(labelOut);
        when(service.getLabelById(1)).thenReturn(labelOut);
        when(service.getLabelById(15)).thenThrow(new IllegalArgumentException("Label not found"));
        when(service.getAllLabels()).thenReturn(labelList);

    }

    @Test
    public void shouldReturnNewLabelOnPostRequest() throws Exception {

        mockMvc.perform(
                        post("/label")
                                .content(mapper.writeValueAsString(labelInput)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isCreated()) //Expected response status code.
                .andExpect(content().json(mapper.writeValueAsString(labelOut))); //matches the output of the Controller with the mock output.
    }

    @Test
    public void shouldReturn422StatusCodeOnMissingNameAttribute() throws Exception {

        mockMvc.perform(
                        post("/label")
                                .content(mapper.writeValueAsString(labelBadInput)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.
    }

    @Test
    public void shouldReturnLabelById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/label/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk())
                // see https://www.baeldung.com/guide-to-jayway-jsonpath for more details on jsonPath
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void shouldReturn422OnLabelIdThatDoesntExist() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/label/{id}", 15)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturnAllLabels() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/label/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(labelList)));
    }

    @Test
    public void shouldReturnEmptyArrayOfLabels() throws Exception {
//        create a new list, thus emptying the current list
        labelList = new ArrayList<>();
//        need to reset the mocking service to catch the emptying of the list
        when(service.getAllLabels()).thenReturn(labelList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/label/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn422StatusWithBadNameUpdateRequest() throws Exception {

//        ARRANGE
        labelInput.setName(null); //<--pretend this is a bad name, we are passing a null property
        doThrow(new IllegalArgumentException("label not found")).when(service).updateLabelById(labelInput);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/label")
                                .content(mapper.writeValueAsString(labelBadInput)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.
    }

    @Test
    public void shouldReturn204OnSuccessfulPutRequest() throws Exception {
//        ARRANGE
        labelOut.setName("new name");
        doNothing().when(service).updateLabelById(labelInput);

        mockMvc.perform(

                        MockMvcRequestBuilders.put("/label")
                                .content(mapper.writeValueAsString(labelOut)) //converts object to JSON and places into RequestBody

                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isNoContent()); //Expected response status code.
    }

    @Test
    public void shouldDeleteLabelReturnNoContent() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/label/{id}", 1))
                .andDo(print())
                .andExpect(status().isNoContent()); //Expected response status code.
    }

    @Test
    public void shouldSendStatus404WhenDeletingALabelThatDoesntExist() throws Exception {
        doThrow(new EmptyResultDataAccessException("not found", 1)).when(service).deleteLabelById(15);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/label/{id}", 15))
                .andDo(print())
                .andExpect(status().isNotFound()); //Expected response status code.
    }

}