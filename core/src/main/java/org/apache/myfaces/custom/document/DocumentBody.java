/*
 * Copyright 2004 The Apache Software Foundation.
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
package org.apache.myfaces.custom.document;


/**
 * Document to enclose the document body. If not otherwise possible you can use
 * state="start|end" to demarkate the document boundaries
 * 
 * @author Mario Ivankovits (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class DocumentBody extends AbstractDocument
{
	public static final String COMPONENT_TYPE = "org.apache.myfaces.DocumentBody";
	private static final String DEFAULT_RENDERER_TYPE = DocumentBodyRenderer.RENDERER_TYPE;

	public DocumentBody()
	{
		super(DEFAULT_RENDERER_TYPE);
	}
}