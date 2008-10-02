dojo.provide("extensions.FacesIO");

dojo.io.FacesTransport = new function() {
    this.canHandle = function(kwArgs) {
        return this.isClientStateSaving(kwArgs) && dojo.io.XMLHTTPTransport.canHandle(kwArgs);
    }

    this.bind = function(request) {
        if (this.isClientStateSaving(request)) {
            request.method = "post";
            this.addJsfState(request);
        }
        return dojo.io.XMLHTTPTransport.bind(request);
    }

    this.isClientStateSaving = function(kwArgs) {
        //First check if we have a component where we can retrieve it
        //without using its id.
        var affectedAjaxComponent = this.getParamValueFromUrl(kwArgs.url,"affectedAjaxComponent");
        if (affectedAjaxComponent)
        {
            var formElement = dojo.dom.getAncestorsByTag(document.getElementById(affectedAjaxComponent), "form", true);            
            return formElement["javax.faces.ViewState"];
        }    
        return dojo.byId("javax.faces.ViewState");
    }

    this.addJsfState = function(request) {
        request.content = request.content || {};
        
        //This is a hack to make work s:inputSuggestAjax and s:tableSuggestAjax
        // with trinidad. The idea is obtain the request param affectedAjaxComponent
        //encoded in the url, and with this id
        
        //previously the way to do this was:
        //request.content = request.content || {};
        //this.addInputValue(request.content, "javax.faces.ViewState");
        //but trinidad does not render the id on this hidden field, which causes
        //problems on this code.
        
        var affectedAjaxComponent = this.getParamValueFromUrl(request.url,"affectedAjaxComponent");
        
        if (affectedAjaxComponent)
        {
            //Find the affectedAjaxComponent using its id, then its parent form
            //and finally the tag with name javax.faces.ViewState
            //Remember that each form has its own child with            
            var formElement = dojo.dom.getAncestorsByTag(document.getElementById(affectedAjaxComponent), "form", true);

            //request.content["javax.faces.ViewState"] = formElement["javax.faces.ViewState"].value;
            
            //Here we have to handle correctly the encoding of the state value
            var enc = /utf/i.test(request.encoding||"") ? encodeURIComponent : dojo.string.encodeAscii;
            if (request.postContent){
                request.postContent = request.postContent + '&javax.faces.ViewState='+ enc(formElement["javax.faces.ViewState"].value);
            }else{
                request.postContent = 'javax.faces.ViewState='+ enc(formElement["javax.faces.ViewState"].value);
            }            
        }
        else
        {
            this.addInputValue(request.content, "javax.faces.ViewState");        
        }
    }
    
    this.getParamValueFromUrl = function(url,param) {
        var startIndexParams = url.indexOf("?"); 
        if ( startIndexParams > -1 ){
            var paramList = url.substr(startIndexParams);
            var aQueryString = paramList.split("&");
            for ( var iParam = 0; iParam < aQueryString.length; iParam++ ){
                if (aQueryString[iParam].indexOf(param + "=") > -1 ){
                    var aParam = aQueryString[iParam].split("=");
                    return aParam[1];
                }
            }
         }
         return false;
    } 

    this.addInputValue = function (content, inputName) {
        var control = dojo.byId(inputName);
        if (!control || !control.value)
            return;
        content[inputName] = control.value;
    }

    dojo.io.transports.addTransport("FacesTransport");
    dojo.io.transports.reverse();
}