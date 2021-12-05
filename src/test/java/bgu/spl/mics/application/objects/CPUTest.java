package bgu.spl.mics.application.objects;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CPUTest {

    private CPU cpu;

    @BeforeEach
    public void setUp() throws Exception {
        cpu = new CPU(4);
    }

    @Test
    public void testGetNumOfCPUs() {
        assertEquals(4, cpu.getNumOfCPUs());
    }

    @Test
    public void testProcess() throws Exception{
        assertFalse(cpu.isDone());
        ArrayList<DataBatch> db1 = new ArrayList<>();
        ArrayList<DataBatch> db2 = new ArrayList<>();

        for (int i = 0; i <= 1000; i++)
            db1.add(new DataBatch());
        for (int i = 0; i < 1000; i++)
            db1.add(new DataBatch());

        try{
            cpu.process(db1);
        }
        catch(Exception e){
            assertFalse(cpu.isDone());
        }
        cpu.process(db2);
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