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

package org.apache.myfaces.test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;
import net.sf.maventaglib.checker.Tag;
import net.sf.maventaglib.checker.TagAttribute;
import net.sf.maventaglib.checker.Tld;
import net.sf.maventaglib.checker.TldParser;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

/**
 * Insures the following ...
 * <ul>
 * <li> all tag handlers are in the classpath</li>
 * <li> tag handlers do not appear in the TLD more than once</li>
 * <li> tag handler attributes do not occur mare than once</li>
 * <li> tag handlers have setters for all tag attributes</li>
 * </ul>
 * TODO get this in Shale test jar ?
 * 
 * @author Dennis C. Byrne
 * @see http://maven-taglib.sourceforge.net/ for dependency download
 */

public abstract class AbstractTagLibTestCase extends TestCase {
	private static Log log = LogFactory.getLog(AbstractTagLibTestCase.class);

	protected Tld[] tlds;

	protected String[] tldPaths;

	private static final String WARNING = " This test can fail when "
			+ "validating TLDs produced by any project, not necessarily "
			+ "the project it is run from.  This is a temporary "
			+ "problem, allowing us to avoid some undesired "
			+ "dependencies or copy and pasting this class in "
			+ "multiple projects.";

	/**
	 * Unmarshall TLDs to an object model. TLDs are supplied by a subclass.
	 */

	protected void setUp() throws Exception {

		if (tldPaths == null)
			throw new NullPointerException(
					"tldPaths cannot point to null before setUp() is called");

		for (int t = 0; t < tldPaths.length; t++) {
			String name = tldPaths[t];
			InputStream stream = getClass().getClassLoader()
					.getResourceAsStream(name);

			if (stream == null)
				throw new NullPointerException(
						"couldn't get an input stream for " + name + WARNING);

			tlds[t] = TldTestUtils.getTld(name, stream);
			stream.close();
		}
	}

	public void testUniqueTagTestCase() throws Exception {

		for (int lib = 0; lib < tlds.length; lib++) {
			Tld tld = tlds[lib];
			List tagNames = new ArrayList();
			Tag[] tags = tld.getTags();

			for (int t = 0; t < tags.length; t++) {
				Tag tag = tags[t];

				if (tag == null)
					throw new NullPointerException("tag");

				String name = tag.getName();
				String msg = name + " found more than once in " + tldPaths[lib]
						+ WARNING;
				assertFalse(msg, tagNames.contains(name));
				tagNames.add(name);
			} // end t
		} // end lib
	}

	public void testUniqueTagAttributes() throws Exception {

		for (int lib = 0; lib < tlds.length; lib++) {
			Tld tld = tlds[lib];
			Tag[] tags = tld.getTags();

			for (int t = 0; t < tags.length; t++) {

				Tag tag = tags[t];
				assertUniqueTagAttributes(tag, tldPaths[lib]);

			} // end t
		} // end lib
	}

	private void assertUniqueTagAttributes(final Tag tag, final String path) {

		List attributeNames = new ArrayList();
		TagAttribute[] atts = tag.getAttributes();
		String tagName = tag.getName();

		for (int a = 0; a < atts.length;) {

			TagAttribute att = atts[a++];
			String name = att.getAttributeName();
			String msg = " @" + name + " of " + path + ":" + tagName
					+ " is duplicated." + WARNING;
			assertFalse(msg, attributeNames.contains(name));
			attributeNames.add(name);

		} // end a

	}

	/**
	 * Make sure the class exists. Make sure there is a setter for each
	 * attribute.
	 */

	public void testSetters() throws Exception {

		for (int t = 0; t < tlds.length; t++) {
			Tld tld = tlds[t];
			Tag[] tags = tld.getTags();

			for (int s = 0; s < tags.length; s++) {
				Tag tag = tags[s];
				String filename = tld.getFilename();
				Object object = TldTestUtils.getTagClassInstance(tag, filename,
						getClass().getClassLoader());

				assertSetters(tag, filename, object);

			} // end for tag
		} // end for lib

	} // end method

	private void assertSetters(final Tag tag, final String path,
			final Object object) {

		TagAttribute[] attributes = tag.getAttributes();

		for (int a = 0; a < attributes.length; a++) {
			TagAttribute attribute = attributes[a];
			String name = attribute.getAttributeName();
			String msg = path + ":" + tag.getName() + "@" + name
					+ " exists, but " + object.getClass().getName()
					+ " has no setter." + WARNING;
			assertTrue(msg, PropertyUtils.isWriteable(object, name));
		} // end for attributes

	}

	public static class TldTestUtils {
		private static Log log = LogFactory.getLog(TldTestUtils.class);

		private static DocumentBuilderFactory dbf = DocumentBuilderFactory
				.newInstance();

		public static Tld getTld(String name, InputStream stream)
				throws Exception {
			if (stream == null)
				log.error(" input stream is null ");

			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(stream);

			return TldParser.parse(doc, name);
		}

		public static TagAttribute getTagAttributeByName(String name,
				TagAttribute[] tagAttributes) {

			for (int a = 0; a < tagAttributes.length; a++) {
				TagAttribute tagAttribute = tagAttributes[a];
				if (tagAttribute.getAttributeName().equals(name))
					return tagAttribute;
			}
			return null;
		}

		public static Tag getTagByName(String name, Tag[] tags) {

			for (int t = 0; t < tags.length; t++) {
				Tag tag = tags[t];
				if (tag.getName().equals(name))
					return tag;
			}
			return null;
		}

		public static Object getTagClassInstance(Tag tag, String filename,
				ClassLoader classLoader) throws Exception {

			String clazzName = tag.getTagClass();

			if (clazzName == null || "".equals(clazzName.trim()))
				throw new NullPointerException(tag.getName()
						+ " is missing a tag class.");

			try {

				Class clazz = classLoader.loadClass(clazzName);
				return clazz.newInstance();

			} catch (ClassNotFoundException e) {
				throw new ClassNotFoundException(clazzName);
			} catch (IllegalAccessException ie) {
				throw new IllegalAccessException(clazzName);
			} catch (InstantiationException iste) {
				throw new InstantiationException(clazzName);
			}

		}

	}

}