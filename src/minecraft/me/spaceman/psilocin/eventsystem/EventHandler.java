package me.spaceman.psilocin.eventsystem;

import me.spaceman.psilocin.Psilocin;
import me.spaceman.psilocin.eventsystem.events.Event;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventHandler {

    public Map<Method, Object> eventMethodsSwitched = new ConcurrentHashMap<Method, Object>();

    /**
     * This loops through the methods of a class and checks to see if they
     * have a @EventHandler and if so registers them to the ConcurrentHashMap
     * @param The class that is listening for events
     *
     */
    public void addEventListener(Object listener)
    {
        for(Method method : listener.getClass().getMethods())
        {
            EventSubscriber eventHandlerAnnotation = method.getAnnotation(EventSubscriber.class);
            if(eventHandlerAnnotation != null)
            {
                eventMethodsSwitched.put(method, listener);
            }
        }

    }

    /**
     * This removes the class from the ConcurrentHashMap
     * @param The class that is being removed from the list of listeners
     */
    public void removeEventListener(Object listener)
    {
        Iterator iterator = eventMethodsSwitched.entrySet().iterator();
        while(iterator.hasNext())
        {
            Map.Entry<Method, Object> entry = (Map.Entry<Method, Object>) iterator.next();
            if(entry.getValue() == listener)
            {
                iterator.remove();
            }
        }
    }

    public int eventsParsed = 0;

    /**
     * This will call the specified eventsystem and invoke a method if it is listening for the parsed eventsystem
     * @param The eventsystem to be parsed and handled
     */
    public void callEvent(Event event)
    {
        for(Method method : eventMethodsSwitched.keySet())
        {
            for(Class c : method.getParameterTypes())
            {
                if(c.getName().equals(event.getClass().getName()))
                {
                    try {
                        method.invoke(this.eventMethodsSwitched.get(method), event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    eventsParsed++;
                }
            }
        }
    }

}
