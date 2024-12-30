package com.Projet_E2425G3_1.GestionProjets.testDto;

import com.Projet_E2425G3_1.GestionProjets.dto.PageResponse;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PageResponseTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        List<String> content = Arrays.asList("item1", "item2");
        int number = 1;
        int size = 2;
        long totalElements = 10L;
        int totalPages = 5;
        boolean first = false;
        boolean last = false;

        // Act
        PageResponse<String> pageResponse = new PageResponse<>(content, number, size,
                totalElements, totalPages, first, last);

        // Assert
        assertEquals(content, pageResponse.getContent());
        assertEquals(number, pageResponse.getNumber());
        assertEquals(size, pageResponse.getSize());
        assertEquals(totalElements, pageResponse.getTotalElements());
        assertEquals(totalPages, pageResponse.getTotalPages());
        assertEquals(first, pageResponse.isFirst());
        assertEquals(last, pageResponse.isLast());
    }

    @Test
    void testSetters() {
        // Arrange
        PageResponse<String> pageResponse = new PageResponse<>();
        List<String> content = Arrays.asList("item1", "item2");

        // Act
        pageResponse.setContent(content);
        pageResponse.setNumber(1);
        pageResponse.setSize(2);
        pageResponse.setTotalElements(10L);
        pageResponse.setTotalPages(5);
        pageResponse.setFirst(false);
        pageResponse.setLast(false);

        // Assert
        assertEquals(content, pageResponse.getContent());
        assertEquals(1, pageResponse.getNumber());
        assertEquals(2, pageResponse.getSize());
        assertEquals(10L, pageResponse.getTotalElements());
        assertEquals(5, pageResponse.getTotalPages());
        assertFalse(pageResponse.isFirst());
        assertFalse(pageResponse.isLast());
    }

    @Test
    void testBuilder() {
        // Arrange
        List<String> content = Arrays.asList("item1", "item2");

        // Act
        PageResponse<String> pageResponse = PageResponse.<String>builder()
                .content(content)
                .number(1)
                .size(2)
                .totalElements(10L)
                .totalPages(5)
                .first(false)
                .last(false)
                .build();

        // Assert
        assertEquals(content, pageResponse.getContent());
        assertEquals(1, pageResponse.getNumber());
        assertEquals(2, pageResponse.getSize());
        assertEquals(10L, pageResponse.getTotalElements());
        assertEquals(5, pageResponse.getTotalPages());
        assertFalse(pageResponse.isFirst());
        assertFalse(pageResponse.isLast());
    }

    @Test
    void testEmptyConstructor() {
        // Act
        PageResponse<String> pageResponse = new PageResponse<>();

        // Assert
        assertNull(pageResponse.getContent());
        assertEquals(0, pageResponse.getNumber());
        assertEquals(0, pageResponse.getSize());
        assertEquals(0L, pageResponse.getTotalElements());
        assertEquals(0, pageResponse.getTotalPages());
        assertFalse(pageResponse.isFirst());
        assertFalse(pageResponse.isLast());
    }

    @Test
    void testBuilderToString() {
        // Arrange
        List<String> content = Arrays.asList("item1", "item2");
        PageResponse.PageResponseBuilder<String> builder = PageResponse.<String>builder()
                .content(content)
                .number(1)
                .size(2)
                .totalElements(10L)
                .totalPages(5)
                .first(false)
                .last(false);

        // Act
        String builderString = builder.toString();

        // Assert
        assertTrue(builderString.contains("content=[item1, item2]"));
        assertTrue(builderString.contains("number=1"));
        assertTrue(builderString.contains("size=2"));
        assertTrue(builderString.contains("totalElements=10"));
        assertTrue(builderString.contains("totalPages=5"));
        assertTrue(builderString.contains("first=false"));
        assertTrue(builderString.contains("last=false"));
    }
}
