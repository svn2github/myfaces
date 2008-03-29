package org.apache.myfaces.custom.ppr;

import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;
import java.util.Iterator;

public class PPRLifecycleFactory extends LifecycleFactory
{
    private final LifecycleFactory delegate;

    private final PPRLifecycle pprLifecycle;

    public PPRLifecycleFactory(LifecycleFactory delegate)
    {
        this.delegate = delegate;

        pprLifecycle = new PPRLifecycle(this.delegate.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE));
    }
    
    public void addLifecycle(String lifecycleId, Lifecycle lifecycle)
    {
        this.delegate.addLifecycle(lifecycleId, lifecycle);
    }

    public Lifecycle getLifecycle(String lifecycleId)
    {
        if (LifecycleFactory.DEFAULT_LIFECYCLE.equals(lifecycleId))
        {
            return pprLifecycle;
        }

        return this.delegate.getLifecycle(lifecycleId);
    }

    public Iterator getLifecycleIds()
    {
        return this.delegate.getLifecycleIds();
    }
}
