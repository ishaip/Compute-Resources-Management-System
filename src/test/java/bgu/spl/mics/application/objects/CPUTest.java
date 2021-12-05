package bgu.spl.mics.application.objects;

import bgu.spl.mics.Future;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CPUTest {

    private CPU cpu;

    @BeforeEach
    public void setUp() throws Exception {
        cpu = new CPU(10);
    }

    @Test
    public void testGetNumOfCPUs() {
        assertEquals(10, cpu.getNumOfCPUs());
    }

    @Test
    public void testProcess() {
        ArrayList<DataBatch> someData = new ArrayList<>();
        assertFalse(cpu.isDone());
        cpu.process(someData);
        assertTrue(cpu.isDone());
    }

    @Test
    public void testIsDone() {
        assertFalse(cpu.isDone());
        ArrayList<DataBatch> someData = new ArrayList<>();
        cpu.process(someData);
        assertTrue(cpu.isDone());
    }
}