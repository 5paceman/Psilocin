package me.spaceman.psilocin.utils;

public class TimeHelper {

    private double startTime = 0;
    private double stopTime = 0;

    private final static double NS_TO_MS = 1000000;
    private final static double MS_TO_S = 1000;

    public TimeHelper() {}

    // Start the timer
    public synchronized void start() throws Exception {
        if(startTime != 0)
        {
            throw new Exception("Timer has already started. Use TimeHelper.Clear().");
        } else {
            this.startTime = System.nanoTime();
        }
    }

    // Stop the timer
    public synchronized void stop() throws Exception {
        if(startTime == 0)
        {
            throw new Exception("You must start the timer first. Use TimeHelper.Start().");
        } else {
            this.stopTime = System.nanoTime();
        }
    }

    // Gets the current elapsed time without stopping the timer
    public double getElapsedTime()
    {
        return System.nanoTime() - this.startTime;
    }

    // Gets the time after you've stopped the timer
    public double getTimedTime() throws Exception
    {
        if(startTime == 0 || stopTime == 0)
        {
            throw new Exception("Timer either hasnt been started or hasnt been stopped. See TimeHelper.Start() or TimeHelper.Stop()");
        } else {
            return this.stopTime - this.startTime;
        }
    }

    // Gets the time after you've stopped the timer in milliseconds
    public double getTimedTimeInMilliseconds() throws Exception {
        return getTimedTime() / NS_TO_MS;
    }
    // Gets the time after you've stopped the timer in seconds
    public double getTimedTimeInSeconds() throws Exception {
        return getTimedTimeInMilliseconds() / MS_TO_S;
    }
    // Gets the current elapsed time without stopping the timer in milliseconds
    public double getElapsedTimeInMilliseconds()
    {
        return getElapsedTime() / NS_TO_MS;
    }
    // Gets the current elapsed time without stopping the timer seconds
    public double getElapsedTimeInSeconds()
    {
        return getElapsedTimeInMilliseconds() / MS_TO_S;
    }
    // resets the timer
    public synchronized void clear()
    {
        this.startTime = 0;
        this.stopTime = 0;
    }
}
