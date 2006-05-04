function reloadChildComboBox(url, componentId, parentValue) {
    	
    dojo.io.bind({
        url: url,
        content: {
	    	affectedAjaxComponent: componentId,
                parentValue: parentValue
	    },
        load: function(type, data, evt){ 
            
            var childCombo = document.getElementsByName(componentId)[0];
            childCombo.options.length = 0;
	    var data = data.getElementsByTagName('option');
            for (i = 0; i < data.length; i++) {
            	var label = data[i].childNodes[0].firstChild.nodeValue;
                var value = data[i].childNodes[1].firstChild.nodeValue;
                var option = new Option(label, value);    	       
                try {
                	childCombo.add(option,null);
		} catch(e) {        
			childCombo.add(option, -1);
		}
            }
        },
        mimetype: "text/xml"
    });

}
