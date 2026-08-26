package dev.phoenixofforce.tea.tracker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ApplicationIT extends BaseIT {

    @Test
    void testStartUp() {
        Integer count = super.template.queryForObject("Select count(*) from tea_type", Integer.class);
        assertEquals(8, count);
    }

}
