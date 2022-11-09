package com.company.musicstorecatalog.controller;

import com.company.musicstorecatalog.model.Track;
import com.company.musicstorecatalog.service.TrackService;
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
@WebMvcTest(TrackController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TrackControllerTest {



    @Autowired
    private MockMvc mockMvc;

    // The aim of this unit test is to test the controller and NOT the service layer.
    // Therefore mock the service layer.
    @MockBean
    private TrackService service;

    @Autowired
    //used to move between Objects and JSON
    private ObjectMapper mapper;

    private Track trackInput;
    private Track trackOut;
    private Track trackOut2;
    private Track trackBadInput;

    private List<Track> trackList;

    @Before
    public void setUp() {
//      input album
        trackInput = new Track();
        trackInput.setAlbumId(1);
        trackInput.setRunTime(10);
        trackInput.setTitle("title test");

//output album
        trackOut = new Track();
        trackOut.setAlbumId(1);
        trackOut.setRunTime(10);
        trackOut.setTitle("title test");
        trackOut.setId(1);

        trackOut2 = new Track();
        trackOut2.setAlbumId(1);
        trackOut2.setRunTime(10);
        trackOut2.setTitle("title test 2");
        trackOut2.setId(2);

//        list of albums
        trackList = new ArrayList<>();
        trackList.add(trackOut);
        trackList.add(trackOut2);


//bad input album
        trackBadInput = new Track();
        trackBadInput.setAlbumId(1);
        trackBadInput.setRunTime(10);


        when(service.createTrack(trackInput)).thenReturn(trackOut);
        when(service.getTrackById(1)).thenReturn(trackOut);
        when(service.getTrackById(15)).thenThrow(new IllegalArgumentException("Track not found"));
        when(service.getAllTracks()).thenReturn(trackList);

    }

    @Test
    public void shouldReturnNewTrackOnPostRequest() throws Exception {

        mockMvc.perform(
                        post("/track")
                                .content(mapper.writeValueAsString(trackInput)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isCreated()) //Expected response status code.
                .andExpect(content().json(mapper.writeValueAsString(trackOut))); //matches the output of the Controller with the mock output.
    }

    @Test
    public void shouldReturn422StatusCodeOnMissingTitleAttribute() throws Exception {

        mockMvc.perform(
                        post("/track")
                                .content(mapper.writeValueAsString(trackBadInput)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.
    }

    @Test
    public void shouldReturnTrackById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/track/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk())
                // see https://www.baeldung.com/guide-to-jayway-jsonpath for more details on jsonPath
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void shouldReturn422OnTrackIdThatDoesntExist() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/track/{id}", 15)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturnAllTracks() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/track/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(trackList)));
    }

    @Test
    public void shouldReturnEmptyArrayOfTracks() throws Exception {
//        create a new list, thus emptying the current list
        trackList = new ArrayList<>();
//        need to reset the mocking service to catch the emptying of the list
        when(service.getAllTracks()).thenReturn(trackList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/track/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn422StatusWithBadTitleUpdateRequest() throws Exception {

//        ARRANGE
        trackInput.setTitle(null); //<--pretend this is a bad name, we are passing a null property
        doThrow(new IllegalArgumentException("Track not found")).when(service).updateTrackById(trackInput);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/track")
                                .content(mapper.writeValueAsString(trackBadInput)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.
    }

    @Test
    public void shouldReturn204OnSuccessfulPutRequest() throws Exception {
//        ARRANGE
        trackOut.setTitle("new name");
        doNothing().when(service).updateTrackById(trackInput);

        mockMvc.perform(

                        MockMvcRequestBuilders.put("/track")
                                .content(mapper.writeValueAsString(trackOut)) //converts object to JSON and places into RequestBody

                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isNoContent()); //Expected response status code.
    }

    @Test
    public void shouldDeleteTrackReturnNoContent() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/track/{id}", 1))
                .andDo(print())
                .andExpect(status().isNoContent()); //Expected response status code.
    }

    @Test
    public void shouldSendStatus404WhenDeletingATrackThatDoesntExist() throws Exception {
        doThrow(new EmptyResultDataAccessException("not found", 1)).when(service).deleteTrackById(15);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/track/{id}", 15))
                .andDo(print())
                .andExpect(status().isNotFound()); //Expected response status code.
    }
}