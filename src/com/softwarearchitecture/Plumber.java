package com.softwarearchitecture;

public class Plumber {

    public static void main(String argv[]) {

        SourceFilter sourceFilter = new SourceFilter("FlightData.dat");

        TemperatureFilter temperatureFilter = new TemperatureFilter();
        AltitudeFilter altitudeFilter = new AltitudeFilter();
        WildPointsFilter wildPointsFilter = new WildPointsFilter();
        RejectedPointsFilter rejectedPointsFilter = new RejectedPointsFilter();

        SinkFilter pressureDataSink = new SinkFilter("OutputB.dat");
        WildPointsDataSink wildPointsDataSink = new WildPointsDataSink("WildPoints.dat");

        wildPointsDataSink.Connect(rejectedPointsFilter);
        pressureDataSink.Connect(wildPointsFilter);
        rejectedPointsFilter.Connect(altitudeFilter);
        wildPointsFilter.Connect(altitudeFilter);
        altitudeFilter.Connect(temperatureFilter);
        temperatureFilter.Connect(sourceFilter);

        sourceFilter.start();
        temperatureFilter.start();
        altitudeFilter.start();
        wildPointsFilter.start();
        rejectedPointsFilter.start();
        pressureDataSink.start();
        wildPointsDataSink.start();

        try {
            pressureDataSink.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
