package com.emiCalculator.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmiResponseTest {

    @Test
    void exposesEmiAmountAndMessageWhenCreated() {
        EmiResponse response = new EmiResponse(1234.56, "ok");

        assertEquals(1234.56, response.getEmiAmount());
        assertEquals("ok", response.getMessage());
    }

    @Test
    void allowsNullValues() {
        EmiResponse response = new EmiResponse(null, null);

        assertNull(response.getEmiAmount());
        assertNull(response.getMessage());
    }

    @Test
    void allowsUpdatingValues() {
        EmiResponse response = new EmiResponse(1.0, "a");

        response.setEmiAmount(2.5);
        response.setMessage("b");

        assertEquals(2.5, response.getEmiAmount());
        assertEquals("b", response.getMessage());
    }

    @Test
    void considersTwoResponsesEqualWhenAllFieldsMatch() {
        EmiResponse a = new EmiResponse(100.0, "ok");
        EmiResponse b = new EmiResponse(100.0, "ok");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void treatsResponsesAsDifferentWhenAnyFieldDiffers() {
        EmiResponse a = new EmiResponse(100.0, "ok");

        assertNotEquals(new EmiResponse(101.0, "ok"), a);
        assertNotEquals(new EmiResponse(100.0, "not ok"), a);
        assertNotEquals(new EmiResponse(null, "ok"), a);
    }
}