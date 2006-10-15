/*
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.myfaces.examples.dynaForm;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class SimpleBeanBacking
{
    private SimpleBean simpleBean;
    private List<SimpleBean> simpleBeans;

    public SimpleBeanBacking()
    {
        initBean();
    }

    protected void initBean()
    {
        simpleBean = new SimpleBean();

        SimpleBean s1 = new SimpleBean();
        s1.setAnyBoolean(true);
        s1.setAnyDateTime(new Date());
        s1.setAnyLong(1L);
        s1.setAnyString("bean 1");
        SimpleBean s2 = new SimpleBean();
        s2.setAnyBoolean(true);
        s2.setAnyDateTime(new Date());
        s2.setAnyLong(2L);
        s2.setAnyString("bean 2");

        simpleBeans = new ArrayList<SimpleBean>();
        simpleBeans.add(s1);
        simpleBeans.add(s2);
    }

    public SimpleBean getSimpleBean()
    {
        return simpleBean;
    }

    public void setSimpleBean(SimpleBean simpleBean)
    {
        this.simpleBean = simpleBean;
    }

    public List<SimpleBean> getSimpleBeans()
    {
        return simpleBeans;
    }

    public void setSimpleBeans(List<SimpleBean> simpleBeans)
    {
        this.simpleBeans = simpleBeans;
    }
}