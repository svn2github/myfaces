dojo.provide("extensions.FacesIO");

dojo.io.FacesTransport = new function() {
    this.canHandle = function(kwArgs) {
        return this.isClientStateSaving() && dojo.io.XMLHTTPTransport.canHandle(kwArgs);
    }

    this.bind = function(request) {
        if (this.isClientStateSaving()) {
            request.method = "post";
            this.addJsfState(request);
        }
        return dojo.io.XMLHTTPTransport.bind(request);
    }

    this.isClientStateSaving = function() {
        return dojo.byId("jsf_state") || dojo.byId("jsf_state_64");
    }

    this.addJsfState = function(request) {
        request.content = request.content || {};
        this.addInputValue(request.content, "jsf_state");
        this.addInputValue(request.content, "jsf_state_64");
        this.addInputValue(request.content, "jsf_tree");
        this.addInputValue(request.content, "jsf_tree_64");
        this.addInputValue(request.content, "jsf_viewid");
    }

    this.addInputValue = function (content, inputName) {
        var control = dojo.byId(inputName);
        if (!control || !control.value)
            return;
        content[inputName] = control.value;
    }

    dojo.io.transports.addTransport("FacesTransport");
}