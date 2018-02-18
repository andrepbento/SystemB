package com.softwarearchitecture;

public class Plumber {

    public static void main(String argv[]) {

        SourceFilter sourceFilter = new SourceFilter();
        TemperatureFilter temperatureFilter = new TemperatureFilter();
        AltitudeFilter altitudeFilter = new AltitudeFilter();
        SinkFilter sinkFilter = new SinkFilter();
        WildPointsSinkFilter wildPointsSinkFilter = new WildPointsSinkFilter();

        wildPointsSinkFilter.Connect(sinkFilter);
        sinkFilter.Connect(altitudeFilter);
        altitudeFilter.Connect(temperatureFilter);
        temperatureFilter.Connect(sourceFilter);

        sourceFilter.start();
        temperatureFilter.start();
        altitudeFilter.start();
        sinkFilter.start();
        wildPointsSinkFilter.start();
    } // main
} // Plumber
