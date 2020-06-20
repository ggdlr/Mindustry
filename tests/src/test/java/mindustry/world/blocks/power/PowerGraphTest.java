package mindustry.world.blocks.power;

import arc.Core;
import arc.math.*;
import arc.math.WindowedMean;
import arc.util.Time;
import mindustry.world.Tile;
import org.junit.jupiter.api.Test;
import power.PowerTestFixture;

import static org.junit.jupiter.api.Assertions.*;

class PowerGraphTest extends PowerTestFixture {
    /*
     * purpose: Check whether getID function works correctly.
     * Input: None
     * Expected: True
     * */
    @Test
    public void getIDTest() {
        PowerGraph powerGraph = new PowerGraph();
        int expectedGraphID = powerGraph.getLastGraphID();

        assertEquals(expectedGraphID, powerGraph.getID());
    }
    /*
     * purpose: Check if getPowerBalance returns expected value.
     * Input: None
     * Expected: True
     * */
    @Test
    public void getPowerBalanceTest() {
        PowerGraph powerGraph = new PowerGraph();
        WindowedMean powerBalance = new WindowedMean(60);

        assertEquals(powerBalance.mean(), powerGraph.getPowerBalance());
    }
    /*
     * purpose: When powerGraph calls update function, check whether getLastPowerNeeded returns proper value.
     * Input: None
     * Expected: If getFrameId() is same as lastFrameUpdated, a second assert returns false, If not, that returns true.
     * */
    @Test
    public void getLastPowerNeededTest() {
        PowerGraph powerGraph = new PowerGraph();
        float expectedOutput = 1f;
        long lastFrameUpdated = -1;

        assertEquals(lastFrameUpdated, Core.graphics.getFrameId());

        powerGraph.update();

        assertEquals(expectedOutput, powerGraph.getLastPowerNeeded());
    }
    /*
     * purpose: When powerGraph calls update function, check whether getLastPowerProduced returns proper value.
     * Input: None
     * Expected: If getFrameId() is same as lastFrameUpdated, a second assert returns false, If not, that returns true.
     * */
    @Test
    public void getLastPowerProduced() {
        PowerGraph powerGraph = new PowerGraph();
        float expectedOutput = 1f;
        long lastFrameUpdated = -1;

        assertEquals(lastFrameUpdated, Core.graphics.getFrameId());

        powerGraph.update();

        assertEquals(expectedOutput, powerGraph.getLastPowerProduced());
    }
    /*
     * purpose: Check whether getSatisfaction returns properly when every conditional situation.
     * Input: lastPowerProduced = 1.0e-6f (zero = true)
     *        lastPowerProduced = 1.0e-5f (zero = false)
     *        lastPowerNeeded = 1.0e-6f (zero = true)
     *        lastPowerNeeded = 1.0e-5f (zero = false)
     * Expected: All return values are true
     * */
    @Test
    public void getSatisfactionTest() {
        PowerGraph powerGraph = new PowerGraph();
        powerGraph.changeLastPowerProduced(1.0e-6f);
        assertEquals(0f, powerGraph.getSatisfaction());

        powerGraph.changeLastPowerProduced(1.0e-5f);
        powerGraph.changeLastPowerNeeded(1.0e-6f);
        assertEquals(1f, powerGraph.getSatisfaction());

        powerGraph.changeLastPowerProduced(1.0e-5f);
        powerGraph.changeLastPowerNeeded(1.0e-5f);
        assertEquals(Mathf.clamp(powerGraph.getLastPowerProduced() / powerGraph.getLastPowerNeeded()), powerGraph.getSatisfaction());
    }
    /*
     * purpose: Check whether we could get the power which is produced
     * Input: producedPower = 0.0f
     *        producerTile = createFakeTile(0, 0, 0f)
     * Expected: True
     * */
    @Test
    public void getPowerProducedTest() {
        PowerGraph powerGraph = new PowerGraph();
        float producedPower = 0.0f;
        Tile producerTile = createFakeTile(0, 0, createFakeProducerBlock(producedPower));
        producerTile.<PowerGenerator.GeneratorEntity>ent().productionEfficiency = 1f;
        powerGraph.add(producerTile.entity);

        assertEquals(producedPower * Time.delta(), powerGraph.getPowerProduced());
    }
    /*
     * purpose: Check whether we are able to get the capacity of power that is needed
     * Input: requiredPower = 1.0f
     *        consumerTile = createFakeTile(0, 1, 1f)
     * Expected: True
     * */
    @Test
    public void getPowerNeededTest() {
        PowerGraph powerGraph = new PowerGraph();
        float requiredPower = 1.0f;
        Tile consumerTile = createFakeTile(0, 1, createFakeDirectConsumer(requiredPower));
        powerGraph.add(consumerTile.entity);

        assertEquals(requiredPower * Time.delta(), powerGraph.getPowerNeeded());
    }
    /*
     * purpose: Check whether batteries are stored precisely.
     * Input: batteryCapacity = 100.0f
     *        maxCapacity = 100f;
     * Expected: True
     * */
    @Test
    public void getBatteryStoredTest() {
        PowerGraph powerGraph = new PowerGraph();
        float batteryCapacity = 100.0f;
        //float maxCapacity = 100f;
        //float expectedBatteryCapacity = 100.0f;
        //Tile batteryTile = createFakeTile(0, 2, createFakeBattery(maxCapacity));
        //batteryTile.entity.power().status = batteryCapacity / maxCapacity;

        //powerGraph.add(batteryTile.entity);

        //powerGraph.update();
        //assertEquals(expectedBatteryCapacity / maxCapacity, batteryTile.entity.power().status);

        Tile batteryTile = createFakeTile(0, 2, createFakeBattery(batteryCapacity));
        powerGraph.add(batteryTile.entity);
        powerGraph.update();
        assertEquals(batteryTile.entity.power().status*batteryCapacity, powerGraph.getBatteryStored());
    }
    /*
     * purpose: Check if getBatteryCapacity returns capacity of battery that we expected
     * Input: batteryCapacity = 100.0f
     * Expected: True
     * */
    @Test
    public void getBatteryCapacityTest() {
        PowerGraph powerGraph = new PowerGraph();
        float batteryCapacity = 100.0f;
        Tile batteryTile = createFakeTile(0, 2, createFakeBattery(batteryCapacity));
        powerGraph.add(batteryTile.entity);
        powerGraph.update();

        assertEquals((1f-batteryTile.entity.power().status)*batteryCapacity, powerGraph.getBatteryCapacity());
    }
    /*
     * purpose: Check a below function returns total capacity of battery correctly.
     * Input: batteryCapacity = 100.0f
     * Expected: True
     * */
    @Test
    public void getTotalBatteryCapacity() {
        PowerGraph powerGraph = new PowerGraph();
        float batteryCapacity = 100.0f;
        Tile batteryTile = createFakeTile(0, 2, createFakeBattery(batteryCapacity));
        powerGraph.add(batteryTile.entity);
        powerGraph.update();

        assertEquals(batteryTile.entity.block().consumes.getPower().capacity, powerGraph.getTotalBatteryCapacity());
    }
    /*
     * purpose: Check whether producers, consumers, batteries and all are removed when remove function is conducted.
     * Input: None
     * Expected: True
     * */
    @Test
    public void removeTest() {
        PowerGraph powerGraph = new PowerGraph();

        assertNull(powerGraph.getProducers());
        assertNull(powerGraph.getConsumers());
        assertNull(powerGraph.getBatteries());
        assertNull(powerGraph.getAll());
    }
    /*
     * purpose: Check the output of toString function comparing with string that we expected.
     * Input: expectedString
     * Expected: True
     * */
    @Test
    public void toStringTest() {
        PowerGraph powerGraph = new PowerGraph();
        String expectedString = "PowerGraph{" +
                "producers=" + powerGraph.getProducers() +
                ", consumers=" + powerGraph.getConsumers() +
                ", batteries=" + powerGraph.getBatteries() +
                ", all=" + powerGraph.getAll() +
                ", lastFrameUpdated=" + powerGraph.getLastFrameUpdated() +
                ", graphID=" + powerGraph.getID() +
                '}';
        assertSame(expectedString, powerGraph.toString());
    }
}