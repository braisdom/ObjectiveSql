/**
 * Copyright © 2007 Chu Yeow Cheah
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * Copied verbatim from http://dzone.com/snippets/java-inflections, used
 * and licensed with express permission from the author Chu Yeow Cheah.
 */

package com.github.braisdom.objsql.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Transforms words (from singular to plural, from camelCase to under_score,
 * etc.). I got bored of doing Real Work...
 *
 * @author chuyeow
 */
public class Inflector {

    private final List<RuleAndReplacement> plurals;
    private final List<RuleAndReplacement> singulars;
    private final List<String> uncountables;

    private static Inflector instance = createDefaultBuilder().build();

    private Inflector(Builder builder) {
        plurals = Collections.unmodifiableList(builder.plurals);
        singulars = Collections.unmodifiableList(builder.singulars);
        uncountables = Collections.unmodifiableList(builder.uncountables);
    }

    public static Inflector.Builder createDefaultBuilder() {
        Builder builder = builder();

        builder.plural("$", "s")
                .plural("s$", "s")
                .plural("(ax|test)is$", "$1es")
                .plural("(octop|vir)us$", "$1i")
                .plural("(alias|status)$", "$1es")
                .plural("(bu)s$", "$1es")
                .plural("(buffal|tomat)o$", "$1oes")
                .plural("([ti])um$", "$1a")
                .plural("sis$", "ses")
                .plural("(?:([^f])fe|([lr])f)$", "$1$2ves")
                .plural("(database|hive)$", "$1s")
                .plural("([^aeiouy]|qu)y$", "$1ies")
                .plural("([^aeiouy]|qu)ies$", "$1y")
                .plural("(x|ch|ss|sh)$", "$1es")
                .plural("(matr|vert|ind)ix|ex$", "$1ices")
                .plural("([m|l])ouse$", "$1ice")
                .plural("(ox)$", "$1en")
                .plural("man$", "men")
                .plural("(quiz)$", "$1zes")
                .plural("specimen", "specimens");

        builder.singular("s$", "")
                .singular("(n)ews$", "$1ews")
                .singular("([ti])a$", "$1um")
                .singular("((a)naly|(b)a|(d)iagno|(p)arenthe|(p)rogno|(s)ynop|(t)he)ses$", "$1$2sis")
                .singular("(^analy)ses$", "$1sis")
                .singular("([^f])ves$", "$1fe")
                .singular("(database|hive)s$", "$1")
                .singular("(tive)s$", "$1")
                .singular("([lr])ves$", "$1f")
                .singular("([^aeiouy]|qu)ies$", "$1y")
                .singular("(s)eries$", "$1eries")
                .singular("(m)ovies$", "$1ovie")
                .singular("(x|ch|ss|sh)es$", "$1")
                .singular("([m|l])ice$", "$1ouse")
                .singular("(bus)es$", "$1")
                .singular("(o)es$", "$1")
                .singular("(shoe)s$", "$1")
                .singular("(cris|ax|test)es$", "$1is")
                .singular("(tax)es$", "$1")
                .singular("([octop|vir])i$", "$1us")
                .singular("(alias|status)es$", "$1")
                .singular("^(ox)en", "$1")
                .singular("(vert|ind)ices$", "$1ex")
                .singular("(matr)ices$", "$1ix")
                .singular("(quiz)zes$", "$1")
                .singular("(ess)$", "$1")
                .singular("men$", "man")
                .singular("(.+)list$", "$1")
                .singular("specimen", "specimen")
                .singular("status$", "status")
                .singular("(slave)s$", "$1");

        builder.irregular("curve", "curves")
                .irregular("leaf", "leaves")
                .irregular("roof", "rooves")
                .irregular("person", "people")
                .irregular("child", "children")
                .irregular("sex", "sexes")
                .irregular("move", "moves");

        builder.uncountable(new String[]{"equipment", "information", "rice", "money", "species", "series", "fish", "sheep", "s"});

        return builder;
    }

    public static Inflector getInstance() {
        return instance;
    }

    /**
     * Returns a entityClass class tableName corresponding to the input database
     * table tableName.
     *
     * <pre>
     * Examples:
     *   classify("people")     "Person"
     *   classify("line_items")   "LineItem"
     * </pre>
     *
     * @param tableName java class tableName of the entityClass
     * @return a java entityClass class tableName
     */
    public String classify(String tableName) {
        return camelize(singularize(tableName));
    }

    /**
     * <tt>underscore</tt> is the reverse of <tt>camelize</tt> method.
     *
     * <pre>
     * Examples:
     *   underscore("Hello world")    "hello world"
     *   underscore("ActiveRecord")   "active_record"
     *   underscore("The RedCross")   "the red_cross"
     *   underscore("ABCD")           "abcd"
     * </pre>
     *
     * @param phase             the original string
     * @return an underscored string
     */
    public String underscore(String phase) {
        if (phase == null || "".equals(phase)) {
            return phase;
        }

        phase = phase.replace('-', '_');
        StringBuilder sb = new StringBuilder();
        int total = phase.length();
        for (int i = 0; i < total; i++) {
            char c = phase.charAt(i);
            if (i == 0) {
                if (isInA2Z(c)) {
                    sb.append(("" + c).toLowerCase());
                } else {
                    sb.append(c);
                }
            } else {
                if (isInA2Z(c)) {
                    if (isIna2z(phase.charAt(i - 1))) {
                        sb.append(("_" + c).toLowerCase());
                    } else {
                        sb.append(("" + c).toLowerCase());
                    }
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    /**
     * Replaces all dashes and underscores by spaces and capitalizes all the words.
     *
     * <pre>
     * Examples:
     *   titleize("ch 1:  Java-ActiveRecordIsFun")   "Ch 1:  Java Active Record Is Fun"
     * </pre>
     *
     * @param phase             the original string
     * @return a titleized string
     */
    public String titleize(String phase) {
        if (phase == null || "".equals(phase)) {
            return phase;
        }

        phase = humanize(phase);
        StringBuilder sb = new StringBuilder();
        int total = phase.length();
        for (int i = 0; i < total; i++)	{
            char c = phase.charAt(i);
            if (i == 0) {
                if (isIna2z(c)) {
                    sb.append(("" + c).toUpperCase());
                }
                else {
                    sb.append(c);
                }
            }
            else {
                if (isIna2z(c) && ' ' == phase.charAt(i-1)) {
                    sb.append(("" + c).toUpperCase());
                }
                else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    /**
     * Returns a database table tableName corresponding to the input entityClass class
     * tableName.
     *
     * <pre>
     * Examples:
     *   tableize("Person")     "people"
     *   tableize("LineItem")   "line_items"
     * </pre>
     *
     * @param modelClassName
     * @return the table tableName of the java entityClass class tableName
     */
    public String tableize(String modelClassName) {
        return pluralize(underscore(modelClassName));
    }

    /**
     * Replaces all dashes and underscores by spaces and capitalizes the first
     * word. Also removes
     *
     * <pre>
     * Examples:
     *   humanize("active_record")   "Active record"
     *   humanize("post_id")         "Post"
     * </pre>
     *
     * @param phase             the original string
     * @return a humanized string
     */
    public String humanize(String phase) {
        if (phase == null || "".equals(phase)) {
            return phase;
        }
        phase = underscore(phase);
        if (phase.endsWith("_id")) {
            phase += " ";
        }
        return camelize(phase.replaceAll("_id ", " ").replace('_', ' ').trim());
    }

    /**
     * Converts string to Camel case.
     *
     * @param word          the word to be converted to camelized form
     * @return a camelized string
     */
    public String camelize(String word) {
        return camelize(word, false);
    }

    /**
     * Converts string to Camel case. If <tt>firstLetterInLowerCase</tt>
     * is true, then the first letter of the result string is in lower case.
     *
     * <pre>
     * Examples:
     *   camelize("hello")               "Hello"
     *   camelize("hello world")         "Hello world"
     *   camelize("active_record")       "ActiveRecord"
     *   camelize("active_record", true) "activeRecord"
     * </pre>
     *
     * @param word                      the word to be converted to camelized form
     * @param firstLetterInLowerCase    true if the first character should be in lower case
     * @return a camelized string
     */
    public String camelize(String word, boolean firstLetterInLowerCase) {
        if (word == null || "".equals(word)) {
            return word;
        }

        String result = "";
        if (word.indexOf('_') != -1) {
            StringBuilder sb = new StringBuilder();
            int count = 0;
            StringTokenizer st = new StringTokenizer(word, "_");
            while(st.hasMoreTokens()) {
                String token = st.nextToken();
                count++;
                if (count == 1) {
                    sb.append(camelizeOneWord(token, firstLetterInLowerCase));
                }
                else {
                    sb.append(camelizeOneWord(token, false));
                }
            }
            result = sb.toString();
        }
        else {
            result = camelizeOneWord(word, firstLetterInLowerCase);
        }
        return result;
    }

    private static String camelizeOneWord(String word, boolean firstLetterInLowerCase) {
        if (word == null || "".equals(word)) {
            return word;
        }

        String firstChar = word.substring(0,1);
        String result = (firstLetterInLowerCase)?firstChar.toLowerCase():firstChar.toUpperCase();
        if (word.length() > 1) {
            result += word.substring(1);
        }
        return result;
    }

    public String pluralize(String word) {
        if (uncountables.contains(word.toLowerCase())) {
            return word;
        }
        return replaceWithFirstRule(word, plurals);
    }

    public String singularize(String word) {
        if (uncountables.contains(word.toLowerCase())) {
            return word;
        }
        return replaceWithFirstRule(word, singulars);
    }

    private static String replaceWithFirstRule(String word, List<RuleAndReplacement> ruleAndReplacements) {

        for (RuleAndReplacement rar : ruleAndReplacements) {
            String replacement = rar.getReplacement();

            // Return if we find a match.
            Matcher matcher = rar.getPattern().matcher(word);
            if (matcher.find()) {
                return matcher.replaceAll(replacement);
            }
        }
        return word;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Ugh, no open structs in Java (not-natively at least).
    private static class RuleAndReplacement {
        private final String replacement;
        private final Pattern pattern;

        public RuleAndReplacement(String rule, String replacement) {
            this.replacement = replacement;
            this.pattern = Pattern.compile(rule, Pattern.CASE_INSENSITIVE);
        }

        public String getReplacement() {
            return replacement;
        }

        public Pattern getPattern() {
            return pattern;
        }
    }

    public static class Builder {
        private List<RuleAndReplacement> plurals = new ArrayList<>();
        private List<RuleAndReplacement> singulars = new ArrayList<>();
        private List<String> uncountables = new ArrayList<>();

        public Builder plural(String rule, String replacement) {
            plurals.add(0, new RuleAndReplacement(rule, replacement));
            return this;
        }

        public Builder singular(String rule, String replacement) {
            singulars.add(0, new RuleAndReplacement(rule, replacement));
            return this;
        }

        public Builder irregular(String singular, String plural) {
            plural(singular, plural);
            singular(plural, singular);
            return this;
        }

        public Builder uncountable(String... words) {
            for (String word : words) {
                uncountables.add(word);
            }
            return this;
        }

        public Inflector build() {
            return new Inflector(this);
        }
    }

    private static String A2Z = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String a2z = "abcdefghijklmnopqrstuvwxyz";

    private static boolean isInA2Z(char c) {
        return (A2Z.indexOf(c) != -1) ? true : false;
    }

    private static boolean isIna2z(char c) {
        return (a2z.indexOf(c) != -1)?true:false;
    }
}
