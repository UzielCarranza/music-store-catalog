package com.company.musicstorecatalog.controller;

import com.company.musicstorecatalog.model.Artist;
import com.company.musicstorecatalog.service.ArtistService;
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
@WebMvcTest(ArtistController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ArtistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // The aim of this unit test is to test the controller and NOT the service layer.
    // Therefore mock the service layer.
    @MockBean
    private ArtistService service;

    @Autowired
    //used to move between Objects and JSON
    private ObjectMapper mapper;

    private Artist artistInput;
    private Artist artistOut;
    private Artist artistOut2;
    private Artist artistBadInput;

    private List<Artist> artistList;

    @Before
    public void setUp() {
//      input album
        artistInput = new Artist();
        artistInput.setName("test");
        artistInput.setInstagram("test@instagram");
        artistInput.setTwitter("test@twitter");

//output album
        artistOut = new Artist();
        artistOut.setName("test");
        artistOut.setInstagram("test@instagram");
        artistOut.setTwitter("test@twitter");
        artistOut.setId(1);

        artistOut2 = new Artist();
        artistOut2.setName("test 2");
        artistOut2.setInstagram("test2@instagram");
        artistOut2.setTwitter("test2@twitter");
        artistOut2.setId(2);

//        list of albums
        artistList = new ArrayList<>();
        artistList.add(artistOut);
        artistList.add(artistOut2);


//bad input album
        artistBadInput = new Artist();
        artistBadInput.setInstagram("badInput@Instagram");
        artistBadInput.setTwitter("badInput@Twitter");

        when(service.createArtist(artistInput)).thenReturn(artistOut);
        when(service.getArtistById(1)).thenReturn(artistOut);
        when(service.getArtistById(15)).thenThrow(new IllegalArgumentException("Artist not found"));
        when(service.getAllArtists()).thenReturn(artistList);

    }

    @Test
    public void shouldReturnNewArtistOnPostRequest() throws Exception {

        mockMvc.perform(
                        post("/artist")
                                .content(mapper.writeValueAsString(artistInput)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isCreated()) //Expected response status code.
                .andExpect(content().json(mapper.writeValueAsString(artistOut))); //matches the output of the Controller with the mock output.
    }

    @Test
    public void shouldReturn422StatusCodeOnMissingNameAttribute() throws Exception {

        mockMvc.perform(
                        post("/artist")
                                .content(mapper.writeValueAsString(artistBadInput)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.
    }

    @Test
    public void shouldReturnArtistById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/artist/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk())
                // see https://www.baeldung.com/guide-to-jayway-jsonpath for more details on jsonPath
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void shouldReturn422OnArtistIdThatDoesntExist() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/artist/{id}", 15)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturnAllArtist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/artist/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(artistList)));
    }

    @Test
    public void shouldReturnEmptyArrayOfArtists() throws Exception {
//        create a new list, thus emptying the current list
        artistList = new ArrayList<>();
//        need to reset the mocking service to catch the emptying of the list
        when(service.getAllArtists()).thenReturn(artistList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/artist/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn422StatusWithBadNameUpdateRequest() throws Exception {

//        ARRANGE
        artistInput.setName(null); //<--pretend this is a bad title, we are passing a null property
        doThrow(new IllegalArgumentException("Artist not found")).when(service).updateArtistById(artistInput);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/artist")
                                .content(mapper.writeValueAsString(artistBadInput)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.
    }

    @Test
    public void shouldReturn204OnSuccessfulPutRequest() throws Exception {
//        ARRANGE
        artistOut.setName("new name");
        doNothing().when(service).updateArtistById(artistInput);

        mockMvc.perform(

                        MockMvcRequestBuilders.put("/artist")
                                .content(mapper.writeValueAsString(artistOut)) //converts object to JSON and places into RequestBody

                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isNoContent()); //Expected response status code.
    }

    @Test
    public void shouldDeleteArtistReturnNoContent() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/artist/{id}", 1))
                .andDo(print())
                .andExpect(status().isNoContent()); //Expected response status code.
    }

    @Test
    public void shouldSendStatus404WhenDeletingAnArtistThatDoesntExist() throws Exception {
        doThrow(new EmptyResultDataAccessException("not found", 1)).when(service).deleteArtistById(15);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/artist/{id}", 15))
                .andDo(print())
                .andExpect(status().isNotFound()); //Expected response status code.
    }

}