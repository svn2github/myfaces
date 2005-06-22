//
// Overrides the original JSCookMenu function to work with MyFaces
//
function cmItemMouseUp (obj, index)
{
	var item = _cmItemList[index];

	var link = null, target = '_self';

	if (item.length > 2)
		link = item[2];
	if (item.length > 3 && item[3])
		target = item[3];

	if (link != null)
	{
         // changes by royalts (Thomas Spiegl)
         var action = link;
         var form = target;
         var dummyForm = document.forms[ form ];
         dummyForm.elements['jscook_action'].value = action;
         dummyForm.submit();
         //window.open (link, target);
	}

	var prefix = obj.cmPrefix;
	var thisMenu = cmGetThisMenu (obj, prefix);

	var hasChild = (item.length > 5);
	if (!hasChild)
	{
		if (cmIsDefaultItem (item))
		{
			if (obj.cmIsMain)
				obj.className = prefix + 'MainItem';
			else
				obj.className = prefix + 'MenuItem';
		}
		cmHideMenu (thisMenu, null, prefix);
	}
	else
	{
		if (cmIsDefaultItem (item))
		{
			if (obj.cmIsMain)
				obj.className = prefix + 'MainItemHover';
			else
				obj.className = prefix + 'MenuItemHover';
		}
	}
}