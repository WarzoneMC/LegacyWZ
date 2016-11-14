package com.minehut.warzone.util;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * Created by luke on 4/10/16.
 */
public class Countdown {
    // ==================================================================== \\

    public interface CountdownHandler {
        public void onStart();
        public void onFinish();
        public void onTick();
    }

    // ==================================================================== \\
    JavaPlugin plugin;
    int time;	// how long the countdown will run for(In Seconds).
    BukkitTask task;	// task variable
    CountdownHandler handler; // handler interface implemented
    boolean finished; // if the countdown is finished

    // ==================================================================== \\

    /**
     * @param plugin - Replace Goblet with the name of your class that extends JavaPlugin
     * @param length - How long the plugin will run for
     */
    public Countdown(JavaPlugin plugin, int length)
    {
        this.plugin=plugin;
        this.time = length;
    }

    // ==================================================================== \\

    public void start()
    {
        // lets all implementing interfaces know it started
        handler.onStart();

        // schedule runnable
        task = new BukkitRunnable(){
            @Override
            public void run(){
                if(time <1){
                    finished = true;
                    handler.onFinish();
                    task.cancel();
                    return;
                }
                handler.onTick();
                time--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public void stop(){
        task.cancel();
    }

    public boolean isFinished(){
        return finished;
    }

    // ==================================================================== \\
    /*
     *
     * When you declare your countdown variable, use the setHandler method
     * so your plugin can "listen" to the onStart, onTick, and onFinish "events"
     *
     */

    public void setHandler(CountdownHandler handler){
        this.handler=handler;
    }

    // ==================================================================== \\
}
