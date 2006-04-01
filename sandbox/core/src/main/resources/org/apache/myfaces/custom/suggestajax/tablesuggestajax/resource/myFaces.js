Ajax.MyFacesAutocompleter = Class.create();
Ajax.MyFacesAutocompleter.prototype = Object.extend(new Autocompleter.Base(),
Object.extend(new Ajax.Base(), {
  initialize: function(element, update, url, options) {
	  this.baseInitialize(element, update, options);
    this.options.asynchronous = true;
    this.options.onComplete   = this.onComplete.bind(this)
    this.options.method       = 'post';
    this.url                  = url;
    this.options.onShow       =
        function(element, update){
          if(!update.style.position || update.style.position=='absolute') {
            update.style.position = 'absolute';
            var offsets = Position.cumulativeOffset(element);
            update.style.top    = (offsets[1] + element.offsetHeight) + 'px';
            update.style.left   = offsets[0] + 'px';
//            Position.clone(element, update, {setHeight: false, offsetTop: element.offsetHeight});
          }
          Effect.Appear(update,{duration:0.15});
        };    
  },

  getUpdatedChoices: function() {
  	Element.addClassName(this.element, "myFacesInputSuggestAjaxThrobbing");
    entry = encodeURIComponent(this.element.name) + '=' +
      encodeURIComponent(this.getToken());

      this.options.parameters = this.options.callback ?
        this.options.callback(this.element, entry) : entry;

    new Ajax.Request(this.url, this.options);
  },

  onComplete: function(request) {
	Element.removeClassName(this.element, "myFacesInputSuggestAjaxThrobbing");
    this.updateChoices(request.responseText);
    this.resetWidth();
  },

  resetWidth: function() {
    this.update.style.width = this.element.offsetWidth + 'px';
    var offset = this.iefix ? 2 : -8;
    if ((this.update.scrollWidth + offset) > this.element.offsetWidth) {
      this.update.style.width = (this.update.scrollWidth + offset) + 'px';
    }
  }

}));