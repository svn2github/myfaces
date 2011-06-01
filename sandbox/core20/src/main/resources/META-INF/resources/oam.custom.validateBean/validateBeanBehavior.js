/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
var org;
if ("undefined" == typeof org) org = {};
if ("undefined" == typeof (org.jkva)) org.jkva = {};
if ("undefined" == typeof (org.jkva.validateBean)) {

    org.jkva.validateBean = {

        validateForm: function(formId, messagesId, validations) {
            var errors = [];
            var form = document.getElementById(formId);
            var elems = form.elements;
            for (var i = 0; i < validations.length; i++) {
                var val = validations[i];
                var field = elems[val.fieldId];
                var error = org.jkva.validateBean.validateField(field, val);
                if (error) {
                    if (val.fieldId) {
                        var label = org.jkva.validateBean.findLabelForField(field);
                        if (!label) {
                            label = val.fieldId;
                        }
                    } else {
                        var label = val.fieldId;
                    }
                    errors.push(label + ":" + error);
                }
            }

            if (errors.length) {
                var messages = document.getElementById(messagesId);
                if (!messages) {
                    messages = document.createElement("span");
                    messages.id = messagesId;
                    form.insertBefore(messages, form.firstChild);
                }

                messages.innerHTML = org.jkva.validateBean.arrayToUnorderedList(errors);

                return false;
            } else {
                return true;
            }
        },

        findLabelForField: function(field) {
            var checkFor = function(elem, fieldId) {
                return elem.tagName == "label"
                    && elem.getAttribute("for") == fieldId;
            };

            var previousSibling = field.previousSibling;
            if (checkFor(previousSibling, field.id)) {
                return previousSibling;
            }

            var nextSibling = field.nextSibling;
            if (checkFor(nextSibling, field.id)) {
                return nextSibling;
            }
        },

        printObject: function(obj) {
            var str = '{\n';
            for (var key in obj) {
                str += '  ' + key + ': ' + obj[key] + '\n';
            }
            str += '}\n';
            alert(str);
        },

        arrayToUnorderedList: function(array) {
            var html = "";
            html += "<ul>";
            for (var i = 0; i < array.length; i++) {
                html += "<li>";
                html += array[i];
                html += "</li>";
            }
            html += "</ul>";
            return html;
        },

        validateField: function(field, options) {
            var val = field.value;
            var error;

            if (!org.jkva.validateBean.validateNotEmpty(val)) { // Required check
                if (options.required) {
                    error = "This field is required";
                } else {
                    return;
                }
            }

            if (!error) { // Type formatting/validation
                field.value = val = val.trim();

                if (options.type == 'text') {
                    // Do nothing
                } else if (options.type == 'numeric') {
                    if (!org.jkva.validateBean.isNumeric(val)) {
                        error = "Not a valid number";
                    }
                } else if (options.type == 'date') {
                    if (!Date.isValid(val, options.dateFormat)) {
                        error = "Not a valid date";
                    }
                } else throw new Error("unknown field type " + options.type)
            }

            if (!error) { // Extra checks
                if (options.min && !org.jkva.validateBean.validateMin(val, options.minValue, options.type)) {
                    error = "The minimum value is " + options.minValue;
                } else if (options.max && !org.jkva.validateBean.validateMax(val, options.maxValue, options.type)) {
                    error = "The maximum value is " + options.maxValue;
                } else if (options.future && !org.jkva.validateBean.validateFuture(val, options.nowStr, options.dateFormat)) {
                    error = "A future date is required";
                }
            }
            return error;
        },

        isNumeric: function(val) {
            return (val - 0) == val && val.length > 0;
        },

        validateNotEmpty: function(value) {
            return value && value.trim() != "";
        },

        validateMin: function(value, min, type) {
            if (value == "") return;
            if (type == 'numeric') {
                return value >= min;
            } else if (type == "text") {
                return value.length >= min;
            } else {
                throw new Error("Unsupported field type: " + type);
            }
        },

        validateMax: function(value, max, type) {
            if (value == "") return;
            if (type == 'numeric') {
                return value <= max;
            } else if (type == "text") {
                return value.length <= max;
            } else {
                throw new Error("Unsupported field type: " + type);
            }
        },

        validateFuture: function(valueStr, nowStr, format) {
            if (!valueStr || !nowStr || !format) return;
            var date = org.jkva.validateBean.truncateToDate(Date.fromFormattedString(valueStr, format));
            var now = org.jkva.validateBean.truncateToDate(Date.fromFormattedString(nowStr, format));

            return date > now;
        },

        truncateToDate: function(date) {
            var newDate = new Date();
            newDate.setYear(date.getYear());
            newDate.setMonth(date.getMonth());
            newDate.setDate(date.getDate());
            newDate.setHours(0);
            newDate.setMinutes(0);
            newDate.setSeconds(0);
            newDate.setMilliseconds(0);
            return newDate;
        }
    };

    Date.prototype.format = function(format) {
        var ret = '';
        for (var i = 0; i < format.length; i++) {
            switch (format[i]) {
                case 'd':
                    var date = this.getDate();
                    if (_findPattern(format, i, 'd', 1)) {
                        if (date < 10) ret += "0";
                        i++;
                    }
                    ret += date;
                    break;
                case 'M':
                    var month = this.getMonth();
                    if (_findPattern(format, i, 'M', 1)) {
                        if (month < 10) ret += "0";
                        i++;
                    }
                    ret += month;
                    break;
                case 'y':
                    if (_findPattern(format, i, 'y', 3)) {
                        ret += this.getFullYear();
                        i += 3;
                    } else if (_findPattern(format, i, 'y', 1)) {
                        ret += ('' + this.getFullYear()).substr(2);
                        i++;
                    } else {
                        ret += 'y';
                    }
                    break;
                default:
                    ret += format[i];
            }
        }
        return ret;
    };

    Date.isValid = function(string, format) {
        if (string.length != format.length) return false;
        for (var i = 0; i < format.length; i++) {
            switch (format[i]) {
                case 'd':
                    if (Date._findPattern(format, i, 'd', 1)) {
                        if (string[i] < 0 || string[i] > 9) return false;
                        if (string[i + 1] < 0 || string[i + 1] > 9) return false;
                        i++;
                    } else {
                        if (string[i] < 0 || string[i] > 9) return false;
                    }
                    break;
                case 'M':
                    if (Date._findPattern(format, i, 'M', 1)) {
                        if (string[i] < 0 || string[i] > 9) return false;
                        if (string[i + 1] < 0 || string[i + 1] > 9) return false;
                        i++;
                    } else {
                        if (string[i] < 0 || string[i] > 9) return false;
                    }
                    break;
                case 'y':
                    if (Date._findPattern(format, i, 'y', 3)) {
                        if (string[i] < 0 || string[i] > 9) return false;
                        if (string[i + 1] < 0 || string[i + 1] > 9) return false;
                        if (string[i + 2] < 0 || string[i + 2] > 9) return false;
                        if (string[i + 3] < 0 || string[i + 3] > 9) return false;
                        i += 3;
                    } else if (Date._findPattern(format, i, 'y', 1)) {
                        if (string[i] < 0 || string[i] > 9) return false;
                        if (string[i + 1] < 0 || string[i + 1] > 9) return false;
                        i++;
                    }
                    break;
                default:
                    if (format[i] != string[i]) return false;
            }
        }
        return true;
    };

    Date.fromFormattedString = function(string, format) {
        var ret = new Date();
        for (var i = 0; i < format.length; i++) {
            switch (format[i]) {
                case 'd':
                    if (Date._findPattern(format, i, 'd', 1)) {
                        ret.setDate('' + (string[i] + string[i + 1]));
                        i++;
                    } else {
                        ret.setDate('' + string[i]);
                    }
                    break;
                case 'M':
                    if (Date._findPattern(format, i, 'M', 1)) {
                        ret.setMonth(('' + string[i] + string[i + 1]) - 1);
                        i++;
                    } else {
                        ret.setMonth(('' + string[i]) - 1);
                    }
                    break;
                case 'y':
                    if (Date._findPattern(format, i, 'y', 3)) {
                        ret.setFullYear('' + string[i] + string[i + 1] + string[i + 2] + string[i + 3]);
                        i += 3;
                    } else if (Date._findPattern(format, i, 'y', 1)) {
                        var prefix = ('' + ret.getFullYear()).substr(2);
                        ret.setFullYear(prefix + string[i] + string[i + 1]);
                        i++;
                    }
                    break;
            }
        }
        return ret;
    };

    Date._findPattern = function(format, index, lookForChar, lookAhead) {
        var ret = true;
        if (format[index] != lookForChar) ret = false;
        else if (format.length < index + lookAhead + 1) ret = false;
        else for (var i = 1; i <= lookAhead; i++) {
                if (format[index + i] != lookForChar) {
                    ret = false;
                }
            }

        return ret;
    };
}
