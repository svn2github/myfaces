function scrollTo(id){
	var theelement = document.getElementById(id);
	if(theelement.scrollIntoView){
		theelement.scrollIntoView(true); 
	}
}