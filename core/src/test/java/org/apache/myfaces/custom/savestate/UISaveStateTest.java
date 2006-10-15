package org.apache.myfaces.custom.savestate;

/*
 * Copyright 2004-2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.util.LinkedList;

import javax.faces.el.ValueBinding;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.shale.test.base.AbstractJsfTestCase;

/**
 * @author cagatay
 * Tests for UISaveState
 */
public class UISaveStateTest extends AbstractJsfTestCase{
	
	private UISaveState saveState;

	public UISaveStateTest(String name) {
		super(name);	
	}
	
	public static Test suite() {
		return new TestSuite(UISaveStateTest.class);
	}
	
	public void setUp() throws Exception{
		super.setUp();
		saveState = new UISaveState();
		facesContext.getExternalContext().getRequestMap().put("testBean", new SaveStateTestBean());
	}
	
	public void tearDown() throws Exception{
		saveState = null;
		super.tearDown();
	}
	
	public void testWorksWithLinkedList() {
		ValueBinding valueBinding = facesContext.getApplication().createValueBinding("#{testBean.linkedList}");
		saveState.setValueBinding("value", valueBinding);
		
		LinkedList linkedList = (LinkedList) valueBinding.getValue(facesContext);
		
		try {
			Object state = saveState.saveState(facesContext);
			Object values[] = (Object[])state;
			assertEquals(values[1], linkedList);
			
			saveState.restoreState(facesContext, state);
		}catch(Exception exception) {
			fail();
		}
	}
	
	public void testWorksWithStateHolder() {
		ValueBinding valueBinding = facesContext.getApplication().createValueBinding("#{testBean}");
		saveState.setValueBinding("value", valueBinding);
		
		SaveStateTestBean testBean = (SaveStateTestBean) valueBinding.getValue(facesContext);
		
		try {
			Object state = saveState.saveState(facesContext);
			Object values[] = (Object[])state;
			assertNotSame(values[1], testBean); //values[1] should be a type of "javax.faces.component._AttachedStateWrapper"
			
			saveState.restoreState(facesContext, state);
		}catch(Exception exception) {
			fail();
		}
	}

}
