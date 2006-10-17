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
package org.apache.myfaces.examples.dynaForm.lib;

import java.util.ArrayList;
import java.util.List;

public class PersonProvider
{
	public List<Person> persons = new ArrayList<Person>();

	public PersonProvider()
	{
		for (int i=0;i<20; i++)
		{
			persons.add(createPersonFake("dummy", i));
		}
	}

	public List<Person> getSearchPersons(String searchString)
	{
		return persons;
	}

	private Person createPersonFake(String search, long count)
	{
		Person p = new Person();
		p.setId(count);
		p.setUserName(search + " " + count);
		p.setAge(count);
		p.setMartialStatus(MartialStatus.MARRIED);
		return p;
	}

	public String getPersonDescription(Person person)
	{
		if (person == null)
		{
			return "#null?";
		}

		return person.getUserName() + ", " + person.getAge();
	}
}
