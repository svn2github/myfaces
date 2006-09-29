package org.apache.myfaces.custom.conversation;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;

public class ConversationServletFilter implements Filter
{
	public final static String CONVERSATION_FILTER_CALLED = "org.apache.myfaces.conversation.ConversationServletFilter.CALLED";

	private final static ThreadLocal externalContextTL = new ThreadLocal();
	private final static ThreadLocal conversationManagerTL = new ThreadLocal();

	private ServletContext servletContext;

	public void init(FilterConfig arg0) throws ServletException
	{
		servletContext = arg0.getServletContext();
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		ConversationManager conversationManager = null;

		if (!Boolean.TRUE.equals(request.getAttribute(CONVERSATION_FILTER_CALLED)))
		{
			if (request instanceof HttpServletRequest)
			{
				HttpServletRequest httpRequest = (HttpServletRequest) request;
				HttpSession httpSession = httpRequest.getSession(false);
				if (request != null)
				{
					conversationManager = ConversationManager.getInstance(httpSession);
					if (conversationManager != null)
					{
						conversationManagerTL.set(conversationManager);
						externalContextTL.set(ConversationExternalContext.create(servletContext, httpRequest));

						conversationManager.attachPersistence();
					}
				}
			}

			request.setAttribute(CONVERSATION_FILTER_CALLED, Boolean.TRUE);
		}

		boolean ok = false;
		try
		{
			chain.doFilter(request, response);
			ok = true;
		}
		finally
		{
			if (conversationManager != null)
			{
				try
				{
					if (!ok)
					{
						conversationManager.purgePersistence();
					}
					else
					{
						conversationManager.detachPersistence();
					}
				}
				finally
				{
					externalContextTL.set(null);
					conversationManagerTL.set(null);
				}
			}
		}
	}

	public void destroy()
	{
	}

	static ConversationExternalContext getConversationExternalContext()
	{
		return (ConversationExternalContext) externalContextTL.get();
	}

	static ConversationManager getConversationManager()
	{
		return (ConversationManager) conversationManagerTL.get();
	}
}
