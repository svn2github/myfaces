package org.apache.myfaces.validator;

import java.io.Serializable;
import java.util.List;

/**
 * @author Manfred Geiler (latest modification by $Author: bdudney $)
 * @version $Revision: 225333 $ $Date: 2005-07-26 17:49:19 +0200 (Di, 26 Jul 2005) $
 */
class AttachedListStateWrapper
        implements Serializable
{
    private static final long serialVersionUID = -3958718149793179776L;
    private List _wrappedStateList;

    public AttachedListStateWrapper(List wrappedStateList)
    {
        _wrappedStateList = wrappedStateList;
    }

    public List getWrappedStateList()
    {
        return _wrappedStateList;
    }
}
