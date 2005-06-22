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
package org.apache.myfaces.custom.rssticker;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.commons.digester.rss.Channel;
import org.apache.commons.digester.rss.Item;
import org.apache.commons.digester.rss.RSSDigester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;


/**
 * @author mwessendorf (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlRssTicker extends HtmlOutputText{

    private static final Log log = LogFactory.getLog(HtmlRssTicker.class);
	public static final String COMPONENT_TYPE = "org.apache.myfaces.RssTicker";
	public static final String COMPONENT_FAMILY = "javax.faces.Output";
	public static final String DEFAULT_RENDERER_TYPE = HtmlRssTickerRenderer.RENDERER_TYPE;

	//private fields
	private String _rssUrl = null;
	private RSSDigester _digester = null;
	private Channel _channel;


	public HtmlRssTicker()
	{
		_digester = new RSSDigester();
		setRendererType(DEFAULT_RENDERER_TYPE);
	}

	public String getFamily()
	{
		return COMPONENT_FAMILY;
	}


	public Object saveState(FacesContext context)
	{
		Object values[] = new Object[5];
		values[0] = super.saveState(context);
		values[1] = _rssUrl;
		return ((Object) (values));
	}

	public void restoreState(FacesContext context, Object state)
	{
		Object values[] = (Object[])state;
		super.restoreState(context, values[0]);
		_rssUrl = (String)values[1];
	}

	public String getRssUrl() {
		if (_rssUrl != null) return _rssUrl;
		ValueBinding vb = getValueBinding("rssUrl");
		return vb != null ? (String)vb.getValue(getFacesContext()) : null;

	}

	public void setRssUrl(String string) {
		_rssUrl = string;
		loadNews(_rssUrl);
	}

	/**
	 * @param _rssUrl
	 */
	private void loadNews(String string) {
		try {

			this._channel = (Channel)_digester.parse(string);
		  } catch(MalformedURLException mue){
			_channel = null;
			log.warn("NO CONNECTION TO THE INTERNET. CAN NOT READ RSS-FEED");
		  }catch (UnknownHostException uhe){
				_channel = null;
				log.warn("NO CONNECTION TO THE INTERNET. CAN NOT READ RSS-FEED");
		  } catch (IOException e1) {
			e1.printStackTrace();
		  } catch (SAXException e) {
			e.printStackTrace();
		  }
	}

	public Channel getChannel() {
	  return _channel;
	}
	public int itemCount(){
	  return _channel.getItems().length;
	}
	public Item[] items(){
	  return _channel.getItems();
	}
}
