/**
 *    Copyright 2009-2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.guice;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @version $Id$
 */
public class AddressConverter {

    public Address convert(String input) throws ParseException {
        Pattern pattern = Pattern.compile("(\\d+)\\s([\\w\\s]+)");
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            Address address = new Address();
            address.setNumber(new Integer(matcher.group(1)));
            address.setStreet(matcher.group(2));
            return address;
        } else {
            throw new ParseException("Address did not match expected pattern: " + pattern.pattern(), 0);
        }
    }

    public String convert(Address address) {
        StringBuilder builder = new StringBuilder();
        builder.append(address.getNumber().toString());
        builder.append(" ");
        builder.append(address.getStreet());
        return builder.toString();
    }

}
