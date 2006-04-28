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
package org.apache.myfaces.examples.collapsiblemenu;

import javax.faces.event.ActionEvent;

import org.apache.myfaces.custom.navmenu.NavigationMenuItem;

/**
 * Adapted from the original component developed by Kevin Le (http://pragmaticobjects.com)
 * @author Sharath Reddy
 * @version $Revision$ $Date$
 */
public class CollapsibleMenuBean {

    private boolean _panelFrequentTasksExpanded = true;
    private boolean _panelAdministrationExpanded = false;
    private boolean _panelReportsExpanded = false;
    
    public boolean isPanelFrequentTasksExpanded() {
        return _panelFrequentTasksExpanded;
    }
    
    public boolean isPanelAdministrationExpanded() {
        return _panelAdministrationExpanded;
    }
    
    public boolean isPanelReportsExpanded() {
        return _panelReportsExpanded;
    }
        
    public NavigationMenuItem [] getDynamicCollapsibleMenu() {
        
        NavigationMenuItem [] panels = new NavigationMenuItem[3];
        
        //'Frequent Tasks' panel
        String title = "#{example_messages.panel_frequent_tasks}";
        String isExpanded = "#{collapsibleMenuBean.panelFrequentTasksExpanded}";
        
        NavigationMenuItem panel = 
            new NavigationMenuItem(title, isExpanded);
        
        //add icons to panel
        NavigationMenuItem collapsibleIcon = 
            new NavigationMenuItem("#{example_messages.icon_users}", 
                    "#{collapsibleMenuBean.viewUsers}");
        collapsibleIcon.setIcon("../images/users.gif");
        collapsibleIcon.setActionListener("#{collapsibleMenuBean.actionListener}");
        panel.add(collapsibleIcon);
        
        collapsibleIcon = 
            new NavigationMenuItem("#{example_messages.icon_mail}", 
                    "mail");
        collapsibleIcon.setIcon("../images/mail.gif");
        panel.add(collapsibleIcon);
        
        panels[0] = panel;
        
        //'Administration' panel
        title = "#{example_messages.panel_administration}";
        isExpanded = "#{collapsibleMenuBean.panelAdministrationExpanded}";
        panel = new NavigationMenuItem(title, isExpanded);
                
        //add icons to panel
        collapsibleIcon = new NavigationMenuItem("#{example_messages.icon_databases}", 
            "#{collapsibleMenuBean.viewDatabases}");
        collapsibleIcon.setIcon("../images/databases.gif");
        panel.add(collapsibleIcon);
        
        collapsibleIcon = 
            new NavigationMenuItem("#{example_messages.icon_servers}",
                    "servers");
        collapsibleIcon.setIcon("../images/servers.gif");
        panel.add(collapsibleIcon);

        panels[1] = panel;
        
        //'Reports' panel
        title = "#{example_messages.panel_reports}";
        isExpanded = "#{collapsibleMenuBean.panelReportsExpanded}";
        panel = new NavigationMenuItem(title, isExpanded);
        
        collapsibleIcon = 
            new NavigationMenuItem("#{example_messages.icon_statistics}",
                    "#{collapsibleMenuBean.viewStatistics}");
        collapsibleIcon.setIcon("../images/statistics.gif");
        panel.add(collapsibleIcon);
        
        collapsibleIcon = 
            new NavigationMenuItem("#{example_messages.icon_charts}",
                    "charts");
        collapsibleIcon.setIcon("../images/charts.gif");
        panel.add(collapsibleIcon);
                
        panels[2] = panel;
        
        return panels;
    }
    
    public String viewUsers() {
        _panelFrequentTasksExpanded = true;
        _panelAdministrationExpanded = false;
        _panelReportsExpanded = false;
        return "users";
    }
    
    public String viewDatabases() {
        _panelFrequentTasksExpanded = false;
        _panelAdministrationExpanded = true;
        _panelReportsExpanded = false;
        return "databases";
    }
    
    public String viewStatistics() { 
        _panelFrequentTasksExpanded = false;
        _panelAdministrationExpanded = false;
        _panelReportsExpanded = true;
        return "statistics";
    }
        
    public void actionListener(ActionEvent event) 
    {
        System.out.println("executing method 'actionListener'");
    }
}
