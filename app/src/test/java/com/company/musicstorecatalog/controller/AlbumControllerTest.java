package com.company.musicstorecatalog.controller;

import com.company.musicstorecatalog.model.Album;
import com.company.musicstorecatalog.service.AlbumService;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(AlbumController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AlbumControllerTest {


    @Autowired
    private MockMvc mockMvc;

    // The aim of this unit test is to test the controller and NOT the service layer.
    // Therefore mock the service layer.
    @MockBean
    private AlbumService service;

    @Autowired
    //used to move between Objects and JSON
    private ObjectMapper mapper;

    private Album albumInput;
    private Album albumOut;
    private Album albumOut2;
    private Album albumBadInput;

    private List<Album> albumList;

    @Before
    public void setUp() {
//      input album
        albumInput = new Album();
        albumInput.setLabelId(1);
        albumInput.setArtistId(1);
        albumInput.setListPrice(15.99);
        albumInput.setReleaseDate(LocalDate.parse("2022-01-02"));
        albumInput.setTitle("new title");

//output album
        albumOut = new Album();
        albumOut.setLabelId(1);
        albumOut.setArtistId(1);
        albumOut.setListPrice(15.99);
        albumOut.setReleaseDate(LocalDate.parse("2022-01-02"));
        albumOut.setTitle("new title");
        albumOut.setId(1);

        albumOut2 = new Album();
        albumOut2.setLabelId(2);
        albumOut2.setArtistId(2);
        albumOut2.setListPrice(19.99);
        albumOut2.setReleaseDate(LocalDate.parse("2022-02-02"));
        albumOut2.setTitle("new title 2");
        albumOut2.setId(2);

//        list of albums
        albumList = new ArrayList<>();
        albumList.add(albumOut);
        albumList.add(albumOut2);


//bad input album
        albumBadInput = new Album();
        albumBadInput.setLabelId(1);
        albumBadInput.setArtistId(1);
        albumBadInput.setListPrice(15.99);
        albumBadInput.setReleaseDate(LocalDate.parse("2022-01-02"));

        when(service.createAlbum(albumInput)).thenReturn(albumOut);
        when(service.getAlbumById(1)).thenReturn(albumOut);
        when(service.getAlbumById(15)).thenThrow(new IllegalArgumentException("Album not found"));
        when(service.getAllAlbums()).thenReturn(albumList);

    }

    @Test
    public void shouldReturnNewAlbumOnPostRequest() throws Exception {

        mockMvc.perform(
                        post("/album")
                                .content(mapper.writeValueAsString(albumInput)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isCreated()) //Expected response status code.
                .andExpect(content().json(mapper.writeValueAsString(albumOut))); //matches the output of the Controller with the mock output.
    }

    @Test
    public void shouldReturn422StatusCodeOnMissingTitleAttribute() throws Exception {

        mockMvc.perform(
                        post("/album")
                                .content(mapper.writeValueAsString(albumBadInput)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.
    }

    @Test
    public void shouldReturnAlbumById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/album/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk())
                // see https://www.baeldung.com/guide-to-jayway-jsonpath for more details on jsonPath
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void shouldReturn422OnAlbumIdThatDoesntExist() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/album/{id}", 15)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturnAllAlbums() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/album/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(albumList)));
    }

    @Test
    public void shouldReturnEmptyArrayOfAlbums() throws Exception {
//        create a new list, thus emptying the current list
        albumList = new ArrayList<>();
//        need to reset the mocking service to catch the emptying of the list
        when(service.getAllAlbums()).thenReturn(albumList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/album/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn422StatusWithBadTitleUpdateRequest() throws Exception {

//        ARRANGE
        albumInput.setTitle(null); //<--pretend this is a bad title, we are passing a null property
        doThrow(new IllegalArgumentException("Album not found")).when(service).updateAlbumById(albumInput);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/album")
                                .content(mapper.writeValueAsString(albumBadInput)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.
    }
    @Test
    public void shouldReturn204OnSuccessfulPutRequest() throws Exception {
//        ARRANGE
        albumOut.setTitle("new title");
        doNothing().when(service).updateAlbumById(albumInput);

        mockMvc.perform(

                        MockMvcRequestBuilders.put("/album")
                                .content(mapper.writeValueAsString(albumOut)) //converts object to JSON and places into RequestBody

                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isNoContent()); //Expected response status code.
    }

    @Test
    public void shouldDeleteAlbumReturnNoContent() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/album/{id}", 1))
                .andDo(print())
                .andExpect(status().isNoContent()); //Expected response status code.
    }
    @Test
    public void shouldSendStatus404WhenDeletingAnAlbumThatDoesntExist() throws Exception {
        doThrow(new EmptyResultDataAccessException("not found",1)).when(service).deleteAlbumById(15);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/album/{id}", 15))
                .andDo(print())
                .andExpect(status().isNotFound()); //Expected response status code.
    }
}