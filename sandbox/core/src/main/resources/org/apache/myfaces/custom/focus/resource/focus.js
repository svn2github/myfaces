
org_apache_myfaces_registerTagsWithFocus(hiddenClientId)
{
    dojo.event.connect(window, "onload", function(evt) {
      var elementsArr = new dojo.collections.ArrayList();
      elementsArr.addRange(document.getElementsByTagName("INPUT"));
      elementsArr.addRange(document.getElementsByTagName("SELECT"));
      elementsArr.addRange(document.getElementsByTagName("TEXTAREA"));
      
      for(var i=0;i<elementsArr.count;i++)
      {
        var elem = elementsArr.item(i);
        dojo.event.connect(elem,"onfocus",function(evt)
          {
            document.getElementById(hiddenClientId).value=evt.target.getAttribute('id');
          }
        );
      }
    });
}