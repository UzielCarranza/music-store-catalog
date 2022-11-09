package com.company.musicstorecatalog.service;


import com.company.musicstorecatalog.model.Artist;
import com.company.musicstorecatalog.model.Label;
import com.company.musicstorecatalog.repository.LabelRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
public class LabelServiceTest {

    private LabelRepository labelRepository;

    private LabelService service;


    private Label labelSetUp;
    private Label actualLabel;
    private Label editedLabel;

    private List<Label> labelsList;

    @Before
    public void setUp() throws Exception {

        setUpLabelRepository();

        service = new LabelService(labelRepository);


    }

    @Test
    public void shouldReturnLabelById() throws Exception {

//        ACT
        actualLabel = service.getLabelById(labelSetUp.getId());

        assertEquals(actualLabel, labelSetUp);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNoIdFound() throws Exception {

//        ACT
        actualLabel = service.getLabelById(10);

        assertEquals(actualLabel, labelSetUp);
    }

    @Test
    public void shouldReturnNewLabel() throws Exception {

//        ACT
        // get it back out of the database
        Label labelPersistentOnDatabase = service.getLabelById(labelSetUp.getId());

//        ASSERT
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(labelSetUp, labelPersistentOnDatabase);

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenLabelIsNull() throws Exception {

//        ARRANGE
        labelSetUp = null;

//        ACT
        actualLabel = service.createLabel(labelSetUp);

        assertEquals(actualLabel, labelSetUp);
    }

    @Test
    public void shouldReturnAllLabels() throws Exception {

//        ACT
        // get it back out of the database
        List<Label> labelsPersistentOnDatabase = service.getAllLabels();

//        ASSERT
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(labelsList, labelsPersistentOnDatabase);

    }

    @Test
    public void shouldReturnEmptyListOfLabels() throws Exception {

//        ARRANGE

        labelsList = new ArrayList<>();

        doReturn(labelsList).when(labelRepository).findAll();
//        ACT
        // get it back out of the database
        List<Label> labelPersistentOnDatabase = service.getAllLabels();


//        ASSERT
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(labelsList, labelPersistentOnDatabase);


    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenUpdatingALabelThatIsNull() throws Exception {

//        ARRANGE
        labelSetUp = null;
//        ACT
        service.updateLabelById(labelSetUp);

        assertEquals(actualLabel, labelSetUp);
    }

    @Test
    public void shouldUpdateALabel() throws Exception {

//        ACT
        service.updateLabelById(labelSetUp);

//        ACT
        // get it back out of the database
        Label labelPersistentOnDatabase = service.getLabelById(editedLabel.getId());

        assertEquals(labelSetUp, labelPersistentOnDatabase);
    }

    public void setUpLabelRepository() {
        labelRepository = mock(LabelRepository.class);
        labelSetUp = new Label();
        labelSetUp.setName("label test");
        labelSetUp.setWebsite("test.com");
        labelSetUp.setId(1);

        editedLabel = new Label();

        editedLabel.setName("label updated");
        editedLabel.setWebsite("test.com");
        editedLabel.setId(1);

        doReturn(labelSetUp).when(labelRepository).save(labelSetUp);
        doReturn(editedLabel).when(labelRepository).save(editedLabel);
        doReturn(Optional.of(labelSetUp)).when(labelRepository).findById(1L);

        //        GET ALL
        Label label1 = new Label();

        label1.setName("label test 2");
        label1.setWebsite("test2.com");
        label1.setId(2);

        labelsList = new ArrayList<>();
        labelsList.add(labelSetUp);
        labelsList.add(label1);
        doReturn(labelsList).when(labelRepository).findAll();
    }
}
