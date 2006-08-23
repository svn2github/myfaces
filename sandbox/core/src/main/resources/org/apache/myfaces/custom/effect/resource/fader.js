/**
 * Copyright 2006 The Apache Software Foundation.
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

org_apache_myfaces_effects_fader = function (theid, thetime) {
	this.fadeid = theid;
	this.time = thetime;
};
org_apache_myfaces_effects_fader.prototype.hide = function () {
	dojo.style.setOpacity(dojo.byId(this.fadeid), 0);
};
org_apache_myfaces_effects_fader.prototype.fadeIn = function () {
	dojo.lfx.html.fadeIn(dojo.byId(this.fadeid), this.time).play();
};
org_apache_myfaces_effects_fader.prototype.show = function () {
	dojo.style.setOpacity(dojo.byId(this.fadeid), 100);
};
org_apache_myfaces_effects_fader.prototype.fadeOut = function () {
	dojo.lfx.html.fadeOut(dojo.byId(this.fadeid), this.time).play();
};
