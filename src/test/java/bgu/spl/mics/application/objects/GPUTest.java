package bgu.spl.mics.application.objects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GPUTest {
    private GPU g;

    @BeforeEach
    void setUp() {
        g = new GPU(GPU.Type.TEST);
    }

    @Test
    void testTrainModelEvent() {
        assertTrue(g.isAvailable());
        g.trainModelEvent(null);
        assertFalse(g.isAvailable());
    }

    @Test
    void testStartModelTraining() {
        assertTrue(g.isAvailable());
        g.startModelTraining(null);
        assertFalse(g.isAvailable());
    }

    @Test
    void testtestModel() {
        assertTrue(g.isAvailable());
        g.testModel(null);
        assertFalse(g.isAvailable());
    }
}