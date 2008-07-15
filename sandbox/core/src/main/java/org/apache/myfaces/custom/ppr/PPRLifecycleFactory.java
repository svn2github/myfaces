/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.myfaces.custom.ppr;

import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;
import java.util.Iterator;

/**
 * A LifecycleFactory which just decorates the lifecycle passed in to the constructor to
 * allow interception on ppr requests.
 */
public class PPRLifecycleFactory extends LifecycleFactory
{
    private final LifecycleFactory delegate;

    private final PPRLifecycleWrapper pprLifecycle;

    public PPRLifecycleFactory(LifecycleFactory delegate)
    {
        this.delegate = delegate;

        pprLifecycle = new PPRLifecycleWrapper(this.delegate.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE));
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
