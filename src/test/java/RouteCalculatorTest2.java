import core.Line;
import core.Station;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RouteCalculatorTest2 extends TestCase {

    private RouteCalculator calculator; //это наш калькулятор
    private StationIndex stationIndex; //это основной объект в котором храним ветки, станции и пересадки

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        stationIndex = new StationIndex(); //создаете свою схему метро

//создаете ветки
        stationIndex.addLine(new Line(0, "red"));
        stationIndex.addLine(new Line(1, "blue"));
        stationIndex.addLine(new Line(2, "green"));
        stationIndex.addLine(new Line(3, "orange"));
//        System.out.println("Здесь");
//        for (Station station : stationIndex.stations) System.out.println(station);

//заполняете станциями

        stationIndex.addStation(new Station("1ПерваяRed", stationIndex.getLine(0)));
        stationIndex.addStation(new Station("2ВтораяRed", stationIndex.getLine(0)));
        stationIndex.addStation(new Station("3ТретьяRed", stationIndex.getLine(0)));
        stationIndex.addStation(new Station("4ЧетвёртаяRed", stationIndex.getLine(0)));
        stationIndex.addStation(new Station("5ПятаяRed", stationIndex.getLine(0)));

        stationIndex.addStation(new Station("1ПерваяBlue", stationIndex.getLine(1)));
        stationIndex.addStation(new Station("2ВтораяBlue", stationIndex.getLine(1)));
        stationIndex.addStation(new Station("3ТретьяBlue", stationIndex.getLine(1)));
        stationIndex.addStation(new Station("4ЧетвёртаяBlue", stationIndex.getLine(1)));
        stationIndex.addStation(new Station("5ПятаяBlue", stationIndex.getLine(1)));

        stationIndex.addStation(new Station("1ПерваяGreen", stationIndex.getLine(2)));
        stationIndex.addStation(new Station("2ВтораяGreen", stationIndex.getLine(2)));
        stationIndex.addStation(new Station("3ТретьяGreen", stationIndex.getLine(2)));
        stationIndex.addStation(new Station("4ЧетвёртаяGreen", stationIndex.getLine(2)));
        stationIndex.addStation(new Station("5ПятаяGreen", stationIndex.getLine(2)));

        stationIndex.addStation(new Station("1ПерваяOrange", stationIndex.getLine(3)));
        stationIndex.addStation(new Station("2ВтораяOrange", stationIndex.getLine(3)));
        stationIndex.addStation(new Station("3ТретьяOrange", stationIndex.getLine(3)));

//        for (Station station : stationIndex.stations) System.out.println(station);

//остальные станции
//заполняете станциями линии, можно в функциональном стиле
        stationIndex.stations.forEach(station -> station.getLine().addStation(station));

//        System.out.println();
//        for (Station station : stationIndex.stations) System.out.println(station);
//создаете пересадки, список станций передается

        stationIndex.addConnection(new ArrayList<>(Arrays.asList(
                stationIndex.getStation("2ВтораяRed"), stationIndex.getStation("2ВтораяBlue"))));

        stationIndex.addConnection(new ArrayList<>(Arrays.asList(
                stationIndex.getStation("2ВтораяGreen"), stationIndex.getStation("4ЧетвёртаяRed"))));

        stationIndex.addConnection(new ArrayList<>(Arrays.asList(
                stationIndex.getStation("4ЧетвёртаяBlue"), stationIndex.getStation("4ЧетвёртаяGreen"))));

        stationIndex.addConnection(new ArrayList<>(Arrays.asList(
                stationIndex.getStation("3ТретьяOrange"), stationIndex.getStation("3ТретьяRed"))));

        //создаете объект калькулятора, его методы и будем вызывать

        calculator = new RouteCalculator(stationIndex);

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testGetShortestRoute_one_transfer() {
        List<Station> routExp = new ArrayList<Station>();
        routExp.add(stationIndex.getStation("1ПерваяRed"));
        routExp.add(stationIndex.getStation("2ВтораяRed"));
        routExp.add(stationIndex.getStation("3ТретьяRed"));
        routExp.add(stationIndex.getStation("4ЧетвёртаяRed"));
        routExp.add(stationIndex.getStation("2ВтораяGreen"));
        routExp.add(stationIndex.getStation("3ТретьяGreen"));
        routExp.add(stationIndex.getStation("4ЧетвёртаяGreen"));
        routExp.add(stationIndex.getStation("5ПятаяGreen"));
        Assert.assertEquals(routExp, calculator.getShortestRoute(stationIndex.getStation("1ПерваяRed"), stationIndex.getStation("5ПятаяGreen")));
    }
    @Test
    public void testGetShortestRouteTime_one_transfer() {
        Assert.assertEquals(18.5, calculator.
                calculateDuration(calculator.getShortestRoute(stationIndex.getStation("1ПерваяRed"),
                        stationIndex.getStation("5ПятаяGreen"))),  0);
    }
    @Test
    public void testGetShortestRoute_one_line() {
        List<Station> routExp = new ArrayList<Station>();
        routExp.add(stationIndex.getStation("1ПерваяGreen"));
        routExp.add(stationIndex.getStation("2ВтораяGreen"));
        routExp.add(stationIndex.getStation("3ТретьяGreen"));
        routExp.add(stationIndex.getStation("4ЧетвёртаяGreen"));
        routExp.add(stationIndex.getStation("5ПятаяGreen"));
        Assert.assertEquals(routExp, calculator.getShortestRoute(
                stationIndex.getStation("1ПерваяGreen"), stationIndex.getStation("5ПятаяGreen")));
    }
    @Test
    public void testGetShortestRouteTime_one_line() {
        Assert.assertEquals(10, calculator.calculateDuration(
                calculator.getShortestRoute(stationIndex.getStation("1ПерваяGreen"), stationIndex.getStation("5ПятаяGreen"))),  0);
    }

    @Test
    public void testGetShortestRoute_two_transfer() {
        List<Station> routExp = new ArrayList<Station>();
        routExp.add(stationIndex.getStation("1ПерваяOrange"));
        routExp.add(stationIndex.getStation("2ВтораяOrange"));
        routExp.add(stationIndex.getStation("3ТретьяOrange"));
        routExp.add(stationIndex.getStation("3ТретьяRed"));
        routExp.add(stationIndex.getStation("2ВтораяRed"));
        routExp.add(stationIndex.getStation("2ВтораяBlue"));
        routExp.add(stationIndex.getStation("1ПерваяBlue"));
        Assert.assertEquals(routExp, calculator.getShortestRoute(stationIndex.getStation("1ПерваяOrange"), stationIndex.getStation("1ПерваяBlue")));
    }
    @Test
    public void testGetShortestRouteTime_two_transfer() {
        Assert.assertEquals(17, calculator.calculateDuration(
                calculator.getShortestRoute(stationIndex.getStation("1ПерваяOrange"), stationIndex.getStation("1ПерваяBlue"))),  0);
    }

}

