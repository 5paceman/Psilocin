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
     * have a @EventHandler and if so registers them to the listenerslist
     * @param listener object that contains @EventHandler methods
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
     * This removes the listener object from the ConcurrentHashMap
     * @param listener listener object that is being removed from the list of listeners
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
     * This will call the specified eventsystem and invoke a listener method if it is listening for the parsed event
     * @param event to be parsed and handled
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
