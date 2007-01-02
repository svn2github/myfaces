package org.apache.myfaces.examples.conversation;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

public class SpringWizardController
{
	private WizardData wizardData;

	public WizardData getWizardData()
	{
		return wizardData;
	}

	public void setWizardData(WizardData wizardData)
	{
		this.wizardData = wizardData;
	}

	public String ensureConversationAction()
	{
		return "springWizardPage1";
	}

	public String save()
	{
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("data saved"));
		return "success";
	}
}
