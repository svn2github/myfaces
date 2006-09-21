package org.apache.myfaces.validator;

import java.io.Serializable;

/**
 * @author Manfred Geiler (latest modification by $Author: bdudney $)
 * @version $Revision: 225333 $ $Date: 2005-07-26 17:49:19 +0200 (Di, 26 Jul 2005) $
 */
class AttachedStateWrapper
        implements Serializable
{
    private static final long serialVersionUID = 4948301780259917764L;
    private Class _class;
    private Object _wrappedStateObject;

    /**
     * @param clazz null means wrappedStateObject is a List of state objects
     * @param wrappedStateObject
     */
    public AttachedStateWrapper(Class clazz, Object wrappedStateObject)
    {
        if (wrappedStateObject != null && !(wrappedStateObject instanceof Serializable))
        {
            throw new IllegalArgumentException("Attached state for Object of type " + clazz + " (Class " + wrappedStateObject.getClass().getName() + ") is not serializable");
        }
        _class = clazz;
        _wrappedStateObject = wrappedStateObject;
    }

    public Class getClazz()
    {
        return _class;
    }

    public Object getWrappedStateObject()
    {
        return _wrappedStateObject;
    }
}
