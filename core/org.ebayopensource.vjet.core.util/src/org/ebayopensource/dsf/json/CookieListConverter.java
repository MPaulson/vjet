/*******************************************************************************
 * Copyright (c) 2005-2011 eBay Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.ebayopensource.dsf.json;

import java.text.ParseException;
import java.util.Iterator;

/**
 * Convert a web browser cookie list string to a JSONObject and back.
 * .org
 * @version 0.1
 */
public class CookieListConverter {

    /**
     * Convert a cookie list into a JSONObject. A cookie list is a sequence
     * of name/value pairs. The names are separated from the values by '='.
     * The pairs are separated by ';'. The names and the values
     * will be unescaped, possibly converting '+' and '%' sequences.
     *
     * To add a cookie to a cooklist,
     * cookielistJSONObject.put(cookieJSONObject.getString("name"),
     *     cookieJSONObject.getString("value"));
     * @param string  A cookie list string
     * @return A JSONObject
     * @throws ParseException
     */
    public static JsonObject toJSONObject(String string)
            throws ParseException {
        JsonObject o = new JsonObject();
        JsonTokener x = new JsonTokener(string);
        while (x.more()) {
            String name = JsonTokener.unescape(x.nextTo('='));
            x.next('=');
            o.put(name, JsonTokener.unescape(x.nextTo(';')));
            x.next();
        }
        return o;
    }


    /**
     * Convert a JSONObject into a cookie list. A cookie list is a sequence
     * of name/value pairs. The names are separated from the values by '='.
     * The pairs are separated by ';'. The characters '%', '+', '=', and ';'
     * in the names and values are replaced by "%hh".
     * @param o A JSONObject
     * @return A cookie list string
     */
    public static String toString(JsonObject o) {
        boolean      b = false;
        Iterator     keys = o.keys();
        String       s;
        StringBuffer sb = new StringBuffer();
        while (keys.hasNext()) {
            s = keys.next().toString();
            if (!o.isNull(s)) {
                if (b) {
                    sb.append(';');
                }
                sb.append(CookieConvertor.escape(s));
                sb.append("=");
                sb.append(CookieConvertor.escape(o.getString(s)));
                b = true;
            }
        }
        return sb.toString();
    }
}
