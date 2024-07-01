package com.project.manager.convert;

import com.project.manager.constant.StatusEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StatusConvertTest {

    @InjectMocks
    private StatusConvert statusConvert;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        statusConvert = new StatusConvert();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void should_convert_to_databaseColumn_when_status_is_open() {
        String response = statusConvert.convertToDatabaseColumn(StatusEnum.OPEN);
        assertTrue("O".equals(response));
    }

    @Test
    public void should_convert_to_databaseColumn_when_status_is_finished() {
        String response = statusConvert.convertToDatabaseColumn(StatusEnum.FINISHED);
        assertTrue("F".equals(response));
    }

    @Test
    public void convert_to_Entity_attribute_when_status_is_open() {
        StatusEnum response = statusConvert.convertToEntityAttribute("O");
        assertTrue(StatusEnum.OPEN.equals(response));
    }

    @Test
    public void convert_to_Entity_attribute_when_status_is_finished() {
        StatusEnum response = statusConvert.convertToEntityAttribute("F");
        assertTrue(StatusEnum.FINISHED.equals(response));
    }
}