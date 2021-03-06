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
tomahawk.MessageBundle = new function() {
	this.messages = new Array();
	this.messages["javax.faces.component.UIInput.CONVERSION"] = "\u5909\u63db\u30a8\u30e9\u30fc";
	this.messages["javax.faces.component.UIInput.CONVERSION_detail"] = "\"{0}\": \u5909\u63db\u30a8\u30e9\u30fc\u304c\u767a\u751f\u3057\u307e\u3057\u305f.";
	this.messages["javax.faces.component.UIInput.REQUIRED"] = "\u30d0\u30ea\u30c7\u30fc\u30b7\u30e7\u30f3\u30a8\u30e9\u30fc";
	this.messages["javax.faces.component.UIInput.REQUIRED_detail"] = "\"{0}\": \u5024\u3092\u5165\u529b\u3057\u3066\u4e0b\u3055\u3044.";
	this.messages["javax.faces.component.UISelectOne.INVALID"] = "\u30d0\u30ea\u30c7\u30fc\u30b7\u30e7\u30f3\u30a8\u30e9\u30fc";
	this.messages["javax.faces.component.UISelectOne.INVALID_detail"] = "\"{0}\": \u5024\u306f\u7701\u7565\u3067\u304d\u307e\u305b\u3093.";
	this.messages["javax.faces.component.UISelectMany.INVALID"] = "\u30d0\u30ea\u30c7\u30fc\u30b7\u30e7\u30f3\u30a8\u30e9\u30fc";
	this.messages["javax.faces.component.UISelectMany.INVALID_detail"] = "\"{0}\": \u5024\u306f\u7701\u7565\u3067\u304d\u307e\u305b\u3093.";
	this.messages["javax.faces.validator.NOT_IN_RANGE"] = "\u30d0\u30ea\u30c7\u30fc\u30b7\u30e7\u30f3\u30a8\u30e9\u30fc";
	this.messages["javax.faces.validator.NOT_IN_RANGE_detail"] = "\"{2}\": {0}\u304b\u3089{1}\u306e\u9593\u306e\u5024\u3092\u5165\u529b\u3057\u3066\u4e0b\u3055\u3044.";
	this.messages["javax.faces.validator.DoubleRangeValidator.LIMIT"] = "\u30d0\u30ea\u30c7\u30fc\u30b7\u30e7\u30f3\u30a8\u30e9\u30fc";
	this.messages["javax.faces.validator.DoubleRangeValidator.LIMIT_detail"] = "\u30d7\u30ed\u30c8\u30bf\u30a4\u30d7\u3078\u5909\u63db\u3067\u304d\u307e\u305b\u3093";
	this.messages["javax.faces.validator.DoubleRangeValidator.MAXIMUM"] = "\u30d0\u30ea\u30c7\u30fc\u30b7\u30e7\u30f3\u30a8\u30e9\u30fc";
	this.messages["javax.faces.validator.DoubleRangeValidator.MAXIMUM_detail"] = "\"{1}\": \u5165\u529b\u5024\u304c\u6700\u5927\u5024\u3092\u8d85\u3048\u3066\u3044\u307e\u3059.''{0}''\u3088\u308a\u5c0f\u3055\u3044\u5024\u3092\u5165\u529b\u3057\u3066\u4e0b\u3055\u3044.";
	this.messages["javax.faces.validator.DoubleRangeValidator.MINIMUM"] = "\u30d0\u30ea\u30c7\u30fc\u30b7\u30e7\u30f3\u30a8\u30e9\u30fc";
	this.messages["javax.faces.validator.DoubleRangeValidator.MINIMUM_detail"] = "\"{1}\": \u5165\u529b\u5024\u304c\u6700\u5c0f\u5024\u3088\u308a\u5c0f\u3055\u3044\u3067\u3059. ''{0}''\u3088\u308a\u5927\u304d\u3044\u5024\u3092\u5165\u529b\u300c\u3057\u3066\u4e0b\u3055\u3044.";
	this.messages["javax.faces.validator.DoubleRangeValidator.TYPE"] = "\u30d0\u30ea\u30c7\u30fc\u30b7\u30e7\u30f3\u30a8\u30e9\u30fc";
	this.messages["javax.faces.validator.DoubleRangeValidator.TYPE_detail"] = "\"{0}\": \u6b63\u3057\u3044\u30bf\u30a4\u30d7\u3092\u5165\u529b\u3057\u3066\u4e0b\u3055\u3044.";
	this.messages["javax.faces.validator.LengthValidator.LIMIT"] = "\u30d0\u30ea\u30c7\u30fc\u30b7\u30e7\u30f3\u30a8\u30e9\u30fc";
	this.messages["javax.faces.validator.LengthValidator.LIMIT_detail"] = "\u5024\u304c\u30d7\u30ed\u30c8\u30bf\u30a4\u30d7\u3078\u5909\u63db\u3067\u304d\u307e\u305b\u3093.\u6b63\u3057\u3044\u5024\u3092\u5165\u529b\u3057\u3066\u4e0b\u3055\u3044.";
	this.messages["javax.faces.validator.LengthValidator.MAXIMUM"] = "\u30d0\u30ea\u30c7\u30fc\u30b7\u30e7\u30f3\u30a8\u30e9\u30fc";
	this.messages["javax.faces.validator.LengthValidator.MAXIMUM_detail"] = "\"{1}\": \u5165\u529b\u5024\u304c\u9577\u904e\u304e\u307e\u3059. {0}\u6587\u5b57\u4ee5\u4e0b\u306b\u3057\u3066\u304f\u3060\u3055\u3044.";
	this.messages["javax.faces.validator.LengthValidator.MINIMUM"] = "\u30d0\u30ea\u30c7\u30fc\u30b7\u30e7\u30f3\u30a8\u30e9\u30fc";
	this.messages["javax.faces.validator.LengthValidator.MINIMUM_detail"] = "\"{1}\": \u5165\u529b\u5024\u304c\u77ed\u904e\u304e\u307e\u3059. {0} \u6587\u5b57\u4ee5\u4e0a\u306b\u3057\u3066\u304f\u3060\u3055\u3044.";
	this.messages["javax.faces.validator.LongRangeValidator.LIMIT"] = "\u30d0\u30ea\u30c7\u30fc\u30b7\u30e7\u30f3\u30a8\u30e9\u30fc";
	this.messages["javax.faces.validator.LongRangeValidator.LIMIT_detail"] = "\u5024\u304c\u30d7\u30ed\u30c8\u30bf\u30a4\u30d7\u3078\u5909\u63db\u3067\u304d\u307e\u305b\u3093.\u6b63\u3057\u3044\u5024\u3092\u5165\u529b\u3057\u3066\u4e0b\u3055\u3044.";
	this.messages["javax.faces.validator.LongRangeValidator.MAXIMUM"] = "\u30d0\u30ea\u30c7\u30fc\u30b7\u30e7\u30f3\u30a8\u30e9\u30fc";
	this.messages["javax.faces.validator.LongRangeValidator.MAXIMUM_detail"] = "\"{1}\": \u5165\u529b\u5024\u304c\u6700\u5927\u5024\u3092\u8d85\u3048\u3066\u3044\u307e\u3059.''{0}''\u3088\u308a\u5c0f\u3055\u3044\u5024\u3092\u5165\u529b\u3057\u3066\u4e0b\u3055\u3044.";
	this.messages["javax.faces.validator.LongRangeValidator.MINIMUM"] = "\u30d0\u30ea\u30c7\u30fc\u30b7\u30e7\u30f3\u30a8\u30e9\u30fc";
	this.messages["javax.faces.validator.LongRangeValidator.MINIMUM_detail"] = "\"{1}\": \u5165\u529b\u5024\u304c\u6700\u5c0f\u5024\u3088\u308a\u5c0f\u3055\u3044\u3067\u3059. ''{0}''\u3088\u308a\u5927\u304d\u3044\u5024\u3092\u5165\u529b\u300c\u3057\u3066\u4e0b\u3055\u3044.";
	this.messages["javax.faces.validator.LongRangeValidator.TYPE"] = "\u30d0\u30ea\u30c7\u30fc\u30b7\u30e7\u30f3\u30a8\u30e9\u30fc";
	this.messages["javax.faces.validator.LongRangeValidator.TYPE_detail"] = "\"{0}\": \u6b63\u3057\u3044\u30bf\u30a4\u30d7\u3092\u5165\u529b\u3057\u3066\u4e0b\u3055\u3044.";
	this.messages["javax.faces.convert.BigDecimalConverter.CONVERSION"] = "\u5909\u63db\u30a8\u30e9\u30fc";
	this.messages["javax.faces.convert.BigDecimalConverter.CONVERSION_detail"] = "\"{0}\": \u6b63\u3057\u3044\u5024\u3092\u5165\u529b\u3057\u3066\u4e0b\u3055\u3044.";
	this.messages["javax.faces.convert.BigIntegerConverter.CONVERSION"] = "\u5909\u63db\u30a8\u30e9\u30fc";
	this.messages["javax.faces.convert.BigIntegerConverter.CONVERSION_detail"] = "\"{0}\": \u6b63\u3057\u3044\u5024\u3092\u5165\u529b\u3057\u3066\u4e0b\u3055\u3044.";
	this.messages["javax.faces.convert.BooleanConverter.CONVERSION"] = "\u5909\u63db\u30a8\u30e9\u30fc";
	this.messages["javax.faces.convert.BooleanConverter.CONVERSION_detail"] = "\"{1}\": '{0}' \u306f\u30d6\u30fc\u30ea\u30a2\u30f3\u578b\u3078\u5909\u63db\u3067\u304d\u307e\u305b\u3093.";
	this.messages["javax.faces.convert.ByteConverter.CONVERSION"] = "\u5909\u63db\u30a8\u30e9\u30fc";
	this.messages["javax.faces.convert.ByteConverter.CONVERSION_detail"] = "\"{1}\": '{0}' \u3092\u30d0\u30a4\u30c8\u578b\u3078\u5909\u63db\u3067\u304d\u307e\u305b\u3093.";
	this.messages["javax.faces.convert.CharacterConverter.CONVERSION"] = "\u5909\u63db\u30a8\u30e9\u30fc";
	this.messages["javax.faces.convert.CharacterConverter.CONVERSION_detail"] = "'{0}' \u3092\u30ad\u30e3\u30e9\u30af\u30bf\u578b\u3078\u5909\u63db\u3067\u304d\u307e\u305b\u3093.";
	this.messages["javax.faces.convert.DateTimeConverter.CONVERSION"] = "\u5909\u63db\u30a8\u30e9\u30fc";
	this.messages["javax.faces.convert.DateTimeConverter.CONVERSION_detail"] = "\"{1}\": \u6b63\u3057\u3044\u65e5\u4ed8/\u6642\u9593\u3092\u5165\u529b\u3057\u3066\u4e0b\u3055\u3044.";
	this.messages["javax.faces.convert.DoubleConverter.CONVERSION"] = "\u5909\u63db\u30a8\u30e9\u30fc";
	this.messages["javax.faces.convert.DoubleConverter.CONVERSION_detail"] = "\"{0}\": \u6b63\u3057\u3044\u5024\u3092\u5165\u529b\u3057\u3066\u4e0b\u3055\u3044.";
	this.messages["javax.faces.convert.FloatConverter.CONVERSION"] = "\u5909\u63db\u30a8\u30e9\u30fc";
	this.messages["javax.faces.convert.FloatConverter.CONVERSION_detail"] = "\"{0}\": \u6b63\u3057\u3044\u5024\u3092\u5165\u529b\u3057\u3066\u4e0b\u3055\u3044.";
	this.messages["javax.faces.convert.IntegerConverter.CONVERSION"] = "\u5909\u63db\u30a8\u30e9\u30fc";
	this.messages["javax.faces.convert.IntegerConverter.CONVERSION_detail"] = "\"{0}\": \u6b63\u3057\u3044\u5024\u3092\u5165\u529b\u3057\u3066\u4e0b\u3055\u3044.";
	this.messages["javax.faces.convert.LongConverter.CONVERSION"] = "\u5909\u63db\u30a8\u30e9\u30fc";
	this.messages["javax.faces.convert.LongConverter.CONVERSION_detail"] = "\"{0}\": \u6b63\u3057\u3044\u5024\u3092\u5165\u529b\u3057\u3066\u4e0b\u3055\u3044.";
	this.messages["javax.faces.convert.NumberConverter.CONVERSION"] = "\u5909\u63db\u30a8\u30e9\u30fc";
	this.messages["javax.faces.convert.NumberConverter.CONVERSION_detail"] = "\"{0}\": \u6b63\u3057\u3044\u5024\u3092\u5165\u529b\u3057\u3066\u4e0b\u3055\u3044.";
	this.messages["javax.faces.convert.ShortConverter.CONVERSION"] = "\u5909\u63db\u30a8\u30e9\u30fc";
	this.messages["javax.faces.convert.ShortConverter.CONVERSION_detail"] = "\"{0}\": \u6b63\u3057\u3044\u5024\u3092\u5165\u529b\u3057\u3066\u4e0b\u3055\u3044.";
	this.messages["org.apache.myfaces.renderkit.html.HtmlMessagesRenderer.IN_FIELD"] = "\u0020in {0}";
	this.messages["org.apache.myfaces.Email.INVALID"] = "\u30d0\u30ea\u30c7\u30fc\u30b7\u30e7\u30f3\u30a8\u30e9\u30fc";
	this.messages["org.apache.myfaces.Email.INVALID_detail"] = "{0})\u306f\u6b63\u3057\u3044\u30e1\u30fc\u30eb\u30a2\u30c9\u30ec\u30b9\u3067\u306f\u3042\u308a\u307e\u305b\u3093";
	this.messages["org.apache.myfaces.Equal.INVALID"] = "\u30d0\u30ea\u30c7\u30fc\u30b7\u30e7\u30f3\u30a8\u30e9\u30fc";
	this.messages["org.apache.myfaces.Equal.INVALID_detail"] = "u5165\u529b\u3055\u308c\u305f\u5024({0})\u306f\"{1}\"\u3068\u7b49\u3057\u304f\u3042\u308a\u307e\u305b\u3093";
	this.messages["org.apache.myfaces.Creditcard.INVALID"] = "\u30d0\u30ea\u30c7\u30fc\u30b7\u30e7\u30f3\u30a8\u30e9\u30fc";
	this.messages["org.apache.myfaces.Creditcard.INVALID_detail"] = "u5165\u529b\u3055\u308c\u305f\u5024({0})\u306f\u6b63\u3057\u3044\u30af\u30ec\u30b8\u30c3\u30c8\u30ab\u30fc\u30c9\u756a\u53f7\u3067\u306f\u3042\u308a\u307e\u305b\u3093";
	this.messages["org.apache.myfaces.Regexpr.INVALID"] = "u30d0\u30ea\u30c7\u30fc\u30b7\u30e7\u30f3\u30a8\u30e9\u30fc";
	this.messages["org.apache.myfaces.Regexpr.INVALID_detail"] = "u5165\u529b\u3055\u308c\u305f\u5024({0})\u306f\u4e0d\u6b63\u3067\u3059";
	this.messages["org.apache.myfaces.Date.INVALID"] = "\u30d0\u30ea\u30c7\u30fc\u30b7\u30e7\u30f3\u30a8\u30e9\u30fc";
	this.messages["org.apache.myfaces.Date.INVALID_detail"] = "u5165\u529b\u3055\u308c\u305f\u5024({0})\u306f\u6b63\u3057\u3044\u65e5\u4ed8\u3067\u306f\u3042\u308a\u307e\u305b\u3093";
	this.messages["org.apache.myfaces.ticker.NOCONNECTION"] = "\u63a5\u7d9a\u3067\u304d\u307e\u305b\u3093.";
	this.messages["org.apache.myfaces.ticker.NOCONNECTION_detail"] = "\u30d5\u30a1\u30a4\u3084\u30a6\u30a9\u30fc\u30eb\u306e\u5185\u5074\u304b\u3089\u30a2\u30af\u30bb\u30b9\u3057\u3066\u3044\u307e\u305b\u3093\u304b?";
	this.messages["org.apache.myfaces.ISBN.INVALID"] = "\u30d0\u30ea\u30c7\u30fc\u30b7\u30e7\u30f3\u30a8\u30e9\u30fc";
	this.messages["org.apache.myfaces.ISBN.INVALID_detail"] = "u5165\u529b\u3055\u308c\u305f\u5024({0})\u306f\u6b63\u3057\u3044isbn\u30b3\u30fc\u30c9\u3067\u306f\u3042\u308a\u307e\u305b\u3093";

	this.getString = function(key) {
		return this.messages[key];
	}
}
