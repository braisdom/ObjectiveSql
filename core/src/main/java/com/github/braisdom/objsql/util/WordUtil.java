/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.braisdom.objsql.util;

import java.util.*;


/**
 * <p>Conversion between singular and plural form of a noun word.</p>
 *
 * @author (Fei) John Chen
 */
public class WordUtil {

    private static final Map<String, String> resolvedSingle2Plurals = new HashMap<String, String>();
    private static final List<String> resolvedPlurals = new ArrayList<String>();
    private static final Map<String, String> resolvedPlural2Singles = new HashMap<String, String>();
    private static final List<String> resolvedSingles = new ArrayList<String>();

    public static final Map<String, String> single2plurals = new HashMap<String, String>();
    public static final List<String> plurals = new ArrayList<String>();
    public static final Map<String, String> plural2singles = new HashMap<String, String>();
    public static final List<String> singles = new ArrayList<String>();

    static {
        //Irregular plurals:
        single2plurals.put("child", "children");
        single2plurals.put("corpus", "corpora");
        single2plurals.put("foot", "feet");
        single2plurals.put("goose", "geese");
        single2plurals.put("louse", "lice");
        single2plurals.put("man", "men");
        single2plurals.put("mouse", "mice");
        single2plurals.put("ox", "oxen");
        single2plurals.put("person", "people");
        single2plurals.put("tooth", "teeth");
        single2plurals.put("woman", "women");

        //Some nouns do not change at all:
        single2plurals.put("cod", "cod");
        single2plurals.put("deer", "deer");
        single2plurals.put("fish", "fish");
        single2plurals.put("offspring", "offspring");
        single2plurals.put("perch", "perch");
        single2plurals.put("sheep", "sheep");
        single2plurals.put("trout", "trout");
        single2plurals.put("species", "species");
        single2plurals.put("series", "series");

        //Other nouns that do not change:
        single2plurals.put("data", "data");
        single2plurals.put("dice", "dice");
        single2plurals.put("media", "media");

        //Singular ends in -us, plural ends in -i: alumnus/alumni, focus/foci, nucleus/nuclei,
        //octopus/octopi, radius/radii, stimulus/stimuli, virus/viri
        //Exceptions to the above
        single2plurals.put("bus", "buses");

        //Singular ends in -ex, plural ends in -ices: appendix/appendices, index/indices
        single2plurals.put("index", "indices");
        single2plurals.put("vertex", "vertices");

        //These include nouns that are traditionally plural, but are also used for singular forms:
        single2plurals.put("barracks", "barracks");
        single2plurals.put("crossroads", "crossroads");
        single2plurals.put("die", "dice");
        single2plurals.put("gallows", "gallows");
        single2plurals.put("headquarters", "headquarters");
        single2plurals.put("means", "means");
        single2plurals.put("series", "series");
        single2plurals.put("species", "species");

        //Exception to Rule 6: Some nouns ending in f or fe are made plural
        //by changing f or fe to ves. with the following exceptions:
        single2plurals.put("chief", "chiefs");
        single2plurals.put("chef", "chefs");
        single2plurals.put("dwarf", "dwarfs");
        single2plurals.put("hoof", "hoofs");
        single2plurals.put("kerchief", "kerchiefs");
        single2plurals.put("fife", "fifes");
        single2plurals.put("proof", "proofs");//m-w.com
        single2plurals.put("roof", "roofs");
        single2plurals.put("safe", "safes");
        single2plurals.put("mischief", "mischiefs");
        single2plurals.put("grief", "griefs");

        //Rule 7b: All musical terms ending in -o have plurals ending in just -s.
        single2plurals.put("cello", "cellos");
        single2plurals.put("photo", "photos");
        single2plurals.put("solo", "solos");
        single2plurals.put("soprano", "sopranos");
        single2plurals.put("studio", "studios");

        //Exception to Rule 7: Most nouns ending in o preceded by a consonant
        //is formed into a plural by adding es with the following exceptions:
        single2plurals.put("canto", "cantos");
        single2plurals.put("lasso", "lassos");
        single2plurals.put("halo", "halos");
        single2plurals.put("memento", "mementos");
        single2plurals.put("photo", "photos");
        single2plurals.put("sirocco", "siroccos");

        //Rule 7c: Plural forms of words ending in -o (-os):
        single2plurals.put("albino", "albinos");
        single2plurals.put("armadillo", "armadillos");
        single2plurals.put("auto", "autos");
        single2plurals.put("bravo", "bravos");
        single2plurals.put("bronco", "broncos");
        single2plurals.put("canto", "cantos");
        single2plurals.put("casino", "casinos");
        single2plurals.put("combo", "combos");
        single2plurals.put("gazebo", "gazebos");
        single2plurals.put("inferno", "infernos");
        single2plurals.put("kangaroo", "kangaroos");
        single2plurals.put("kilo", "kilos");
        single2plurals.put("kimono", "kimonos");
        single2plurals.put("logo", "logos");
        single2plurals.put("maraschino", "maraschinos");
        single2plurals.put("memo", "memos");
        single2plurals.put("photo", "photos");
        single2plurals.put("pimento", "pimentos");
        single2plurals.put("poncho", "ponchos");
        single2plurals.put("pro", "pros");
        single2plurals.put("sombrero", "sombreros");
        single2plurals.put("taco", "tacos");
        single2plurals.put("tattoo", "tattoos");
        single2plurals.put("torso", "torsos");
        single2plurals.put("tobacco", "tobaccos");
        single2plurals.put("typo", "typos");

        //Rule 7c: Plural forms of words ending in -o (-oes):
        single2plurals.put("echo", "echoes");
        single2plurals.put("embargo", "embargoes");
        single2plurals.put("hero", "heroes");
        single2plurals.put("potato", "potatoes");
        single2plurals.put("tomato", "tomatoes");
        single2plurals.put("torpedo", "torpedoes");
        single2plurals.put("veto", "vetoes");

        //Rule 7c: Plural forms of words ending in -o (-os or -oes):
        single2plurals.put("avocado", "avocados");
        single2plurals.put("buffalo", "buffaloes");
        single2plurals.put("cargo", "cargoes");
        single2plurals.put("desperado", "desperadoes");
        single2plurals.put("dodo", "dodoes");
        single2plurals.put("domino", "dominoes");
        single2plurals.put("ghetto", "ghettos");
        single2plurals.put("grotto", "grottoes");
        single2plurals.put("hobo", "hoboes");
        single2plurals.put("innuendo", "innuendoes");
        single2plurals.put("lasso", "lassos");
        single2plurals.put("mango", "mangoes");
        single2plurals.put("mosquito", "mosquitoes");
        single2plurals.put("motto", "mottoes");
        single2plurals.put("mulatto", "mulattos");
        single2plurals.put("no", "noes");
        single2plurals.put("peccadillo", "peccadilloes");
        single2plurals.put("tornado", "tornadoes");
        single2plurals.put("volcano", "volcanoes");
        single2plurals.put("zero", "zeros");

        //others
        single2plurals.put("forum", "forums");

        //Things that come in pairs
        plurals.add("binoculars");
        plurals.add("forceps");
        plurals.add("jeans");
        plurals.add("glasses");
        plurals.add("pajamas");
        plurals.add("pants");
        plurals.add("scissors");
        plurals.add("shorts");
        plurals.add("tongs");
        plurals.add("trousers");
        plurals.add("tweezers");

        //Nouns that end in -s but have no singular (aggregate nouns)
        plurals.add("accommodations");
        plurals.add("amends");
        plurals.add("archives");
        plurals.add("arms");
        plurals.add("bellows");
        plurals.add("bowels");
        plurals.add("brains");
        plurals.add("clothes");
        plurals.add("communications");
        plurals.add("congratulations");
        plurals.add("contents");
        plurals.add("dregs");
        plurals.add("goods");
        plurals.add("measles");
        plurals.add("mumps");
        plurals.add("oats");
        plurals.add("pinchers");
        plurals.add("shears");
        plurals.add("snuffers");
        plurals.add("stairs");
        plurals.add("thanks");
        plurals.add("vespers");
        plurals.add("victuals");

        //Nouns that are plural but do not end in -s
        plurals.add("children");
        plurals.add("cattle");
        plurals.add("corpora");
        plurals.add("data");
        plurals.add("men");
        plurals.add("people");
        plurals.add("police");
        plurals.add("women");

        //Nouns that are always singular -- uncountable
        singles.add("cooper");
        singles.add("corn");
        singles.add("cotton");
        singles.add("gold");
        singles.add("information");
        singles.add("money");
        singles.add("news");
        singles.add("rice");
        singles.add("silver");
        singles.add("sugar");
        singles.add("wheat");

        //plural2singles.put("data", "data");
        //plural2singles.put("media", "media");
        plural2singles.put("dice", "dice");
        plural2singles.put("indices", "index");
        plural2singles.put("vertices", "vertex");
        plural2singles.put("movies", "movie");
        plural2singles.put("viri", "virus");

        plural2singles.put("axes", "axis");
        plural2singles.put("crises", "crisis");
        plural2singles.put("analyses", "analysis");
        plural2singles.put("diagnoses", "diagnosis");
        plural2singles.put("synopses", "synopsis");
        plural2singles.put("theses", "thesis");
        plural2singles.put("moves", "move");
        plural2singles.put("caves", "cave");
        plural2singles.put("toes", "toe");

        //merge plural2singles with single2plurals
        for (Map.Entry<String, String> entry : single2plurals.entrySet()) {
            String sk = entry.getKey();
            String sv = entry.getValue();
            String pv = plural2singles.get(sv);
            if (pv == null) {
                plural2singles.put(sv, sk);
            }
        }

        //merge single2plurals with plural2singles
        for (Map.Entry<String, String> entry : plural2singles.entrySet()) {
            String pk = entry.getKey();
            String pv = entry.getValue();
            String sv = single2plurals.get(pv);
            if (sv == null) {
                single2plurals.put(pv, pk);
            }
        }
    }

    /**
     * Returns a pluralized word.
     *
     * @param word          the word to be converted to plural form
     * @return pluralized string
     */
    public static String pluralize(String word) {
        if (word == null || "".equals(word)) {
            return word;
        }

        String plform = resolvedSingle2Plurals.get(word);
        if (plform == null && (resolvedPlurals.contains(word) || resolvedPlural2Singles.containsKey(word))) {
            plform = word;
        }
        if (plform != null) {
            return plform;
        }

        String tmp = word.toLowerCase();
        plform = single2plurals.get(tmp);
        if (plform == null && (plurals.contains(tmp) || singles.contains(tmp) || plural2singles.containsKey(tmp))) {
            plform = tmp;
        }

        if (plform != null) {
            ;
        }
        //Rule #5: For words that end in -is, change the -is to -es to make the plural form
        else if (tmp.endsWith("is")) {
            plform = replaceLast(tmp, "is", "es");
        }
        //Singular ends in -ix, plural ends in -ices: appendix/appendices, index/indices
        else if (tmp.endsWith("ix")) {
            plform = replaceLast(tmp, "ix", "ices");
        }
        //Singular ends in -us, plural ends in -i: alumnus/alumni, focus/foci, nucleus/nuclei,
        //octopus/octopi, radius/radii, stimulus/stimuli, virus/viri
        else if (tmp.endsWith("us")) {
            plform = replaceLast(tmp, "us", "i");
        }
        //Rule #2: For words that end in a "hissing" sound (-s, -z, -x, -ch, -sh), add an -es to form the plural.
        //Note: I removed tmp.endsWith("s") || as this cause "posts"->"postses".
        else if (!tmp.endsWith("es") && (tmp.endsWith("z") ||
                tmp.endsWith("x") || tmp.endsWith("ch") || tmp.endsWith("sh"))) {
            plform = tmp + "es";
        }
        else if (tmp.endsWith("y")) {
            //Rule #3: If the word ends in a vowel plus -y (-ay, -ey, -iy, -oy, -uy), add an -s to the word.
            if (tmp.endsWith("ay") || tmp.endsWith("ey") || tmp.endsWith("iy") ||
                    tmp.endsWith("oy") || tmp.endsWith("uy")) {
                plform = word + "s";
            }
            //Rule #4: If the word ends in a consonant plus -y, change the -y into -ie and add an -s to form the plural.
            else {
                plform = replaceLast(tmp, "y", "ies");
            }
        }
        //Rule #6: Some words that end in -f or -fe have plurals that end in -ves.
        else if (tmp.endsWith("f")) {
            plform = replaceLast(tmp, "f", "ves");
        }
        else if (tmp.endsWith("fe")) {
            plform = replaceLast(tmp, "fe", "ves");
        }
        //Rule #7: The plurals of words ending in -o are formed by either adding -s or by adding -es
        else if (tmp.endsWith("o")) {
            //All words that end in a vowel plus -o (-ao, -eo, -io, -oo, -uo) have plurals that end in just -s:
            if (tmp.endsWith("ao") || tmp.endsWith("eo") || tmp.endsWith("io") ||
                    tmp.endsWith("javaoo") || tmp.endsWith("uo")) {
                plform = word + "s";
            }
            //All musical terms ending in -o have plurals ending in just -s.
            //Most others by adding -es with exceptions
            else {
                plform = word + "es";
            }
        }
        //Singular ends in -um, plural ends in -a: datum/data, curriculum/curricula
        else if (tmp.endsWith("um")) {
            plform = replaceLast(tmp, "um", "a");
        }
        //Singular ends in -on, plural ends in -a: criterion/criteria, phenomenon/phenomena
        else if (tmp.endsWith("on") && !tmp.endsWith("ation")) {
            plform = replaceLast(tmp, "on", "a");
        }
        //Singular ends in -a, plural ends in -ae: alumna/alumnae, formula/formulae, antenna/antennae
        else if (tmp.endsWith("a")) {
            plform = replaceLast(tmp, "a", "ae");
        }
        //Singular ends in -eau, plural ends in -eaux: bureau/bureaux, beau/beaux
        else if (tmp.endsWith("eau")) {
            plform = replaceLast(tmp, "eau", "eaux");
        }
        //special
        else if (tmp.endsWith("man")) {
            plform = replaceLast(tmp, "man", "men");
        }
        //Rule #1: Add an -s to form the plural of most words.
        else if (!tmp.endsWith("s")){
            plform = word + "s";
        }
        //Rule #8: The plurals of single capital letters, acronyms, and Arabic numerals
        //(1,2,3,...) take an -s WITHOUT an apostrophe:
        else if (word.toUpperCase().equals(word)) {
            plform = word + "s";
        }
        else {
            plform = tmp;
            resolvedPlurals.add(word);
            return word;
        }

        //check cases
        boolean caseChanged = false;
        int wl = word.length();
        int pl = plform.length();
        char[] pChars = plform.toCharArray();
        int length = (wl < pl)?wl:pl;
        for (int i = 0; i < length; i++) {
            char wChar = word.charAt(i);
            char pChar = plform.charAt(i);
            if (((int)wChar - (int)pChar) == -32) {
                pChars[i] = wChar;
                caseChanged = true;
            }
        }

        if (caseChanged) {
            plform = new String(pChars);
        }
        if (!plform.equalsIgnoreCase(word)) {
            resolvedSingle2Plurals.put(word, plform);
            resolvedPlural2Singles.put(plform, word);
        }

        return plform;
    }



    /**
     * Returns a singularized word from a plural word.
     *
     * @param word          the word to be converted to singular form
     * @return singularized string
     */
    public static String singularize(String word) {
        if (word == null || "".equals(word)) {
            return word;
        }

        String sgform = resolvedPlural2Singles.get(word);
        if (sgform == null && (resolvedSingles.contains(word) || resolvedSingle2Plurals.containsKey(word))) {
            sgform = word;
        }
        if (sgform != null) {
            return sgform;
        }

        String tmp = word.toLowerCase();
        sgform = plural2singles.get(tmp);
        if (sgform == null && (plurals.contains(tmp) || singles.contains(tmp) || single2plurals.containsKey(tmp))) {
            sgform = tmp;
        }

        if (sgform != null) {
            ;
        }
        else if (tmp.endsWith("ices")) {
            sgform = replaceLast(tmp, "ices", "ix");
        }
        else if (tmp.endsWith("i")) {
            sgform = replaceLast(tmp, "i", "us");
        }
        else if (tmp.endsWith("ses") && !tmp.endsWith("bases") ||
                tmp.endsWith("zes") || tmp.endsWith("xes") ||
                tmp.endsWith("ches") || tmp.endsWith("shes")) {
            sgform = replaceLast(tmp, "es", "");
        }
        else if (tmp.endsWith("ays") || tmp.endsWith("eys") || tmp.endsWith("iys") ||
                tmp.endsWith("oys") || tmp.endsWith("uys")) {
            sgform = replaceLast(tmp, "ys", "y");
        }
        else if (tmp.endsWith("ies")) {
            sgform = replaceLast(tmp, "ies", "y");
        }
        //Rule #7
        else if (tmp.endsWith("aos") || tmp.endsWith("eos") || tmp.endsWith("ios") ||
                tmp.endsWith("oos") || tmp.endsWith("uos")) {
            sgform = replaceLast(tmp, "os", "o");
        }
        //Rule #7
        else if (tmp.endsWith("oes")) {
            sgform = replaceLast(tmp, "oes", "o");
        }
        else if (tmp.endsWith("ives")) {
            sgform = replaceLast(tmp, "ves", "fe");
        }
        else if (tmp.endsWith("lves") || tmp.endsWith("rves") || tmp.endsWith("aves")) {
            sgform = replaceLast(tmp, "ves", "f");
        }
        else if (tmp.endsWith("ae")) {
            sgform = replaceLast(tmp, "ae", "a");
        }
        else if (tmp.endsWith("eaux")) {
            sgform = replaceLast(tmp, "eaux", "eau");
        }
        else if (tmp.endsWith("men")) {
            sgform = replaceLast(tmp, "men", "man");
        }
        else if (tmp.endsWith("s")) {
            sgform = replaceLast(tmp, "s", "");
        }
        else {
            sgform = tmp;
            resolvedSingles.add(word);
            return word;
        }

        //check cases
        boolean caseChanged = false;
        int wl = word.length();
        int pl = sgform.length();
        char[] sChars = sgform.toCharArray();
        int length = (wl < pl)?wl:pl;
        for (int i = 0; i < length; i++) {
            char wChar = word.charAt(i);
            char pChar = sgform.charAt(i);
            if (((int)wChar - (int)pChar) == -32) {
                sChars[i] = wChar;
                caseChanged = true;
            }
        }

        if (caseChanged) {
            sgform = new String(sChars);
        }
        if (!sgform.equalsIgnoreCase(word)) {
            resolvedPlural2Singles.put(word, sgform);
            resolvedSingle2Plurals.put(sgform, word);
        }

        return sgform;
    }

    /**
     * Replaces the last occurance of an old symbol with a new symbol.
     *
     * @param data              the original string
     * @param oldSymbol         the old symbols to be replaced
     * @param newSymbol         the corresponding new symbol
     * @return a new string
     */
    public static String replaceLast(String data, String oldSymbol, String newSymbol) {
        if (data == null || data.indexOf(oldSymbol) == -1) {
            return data;
        }

        int lastIndex = data.lastIndexOf(oldSymbol);
        int oldLength = oldSymbol.length();
        String result = data.substring(0, lastIndex) + newSymbol +
                data.substring(lastIndex + oldLength);

        return result;
    }

    /**
     * Adds more pairs of single and plural words.
     *
     * @param single singular form of the word
     * @param plural plural form of the word
     */
    public static void addPlural(String single, String plural) {
        resolvedSingle2Plurals.put(single, plural);
        resolvedPlural2Singles.put(plural, single);
    }

    /**
     * Converts string to Camel case.
     *
     * @param word          the word to be converted to camelized form
     * @return a camelized string
     */
    public static String camelize(String word) {
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
    public static String camelize(String word, boolean firstLetterInLowerCase) {
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
    public static String underscore(String phase) {
        if (phase == null || "".equals(phase)) {
            return phase;
        }

        phase = phase.replace('-', '_');
        StringBuilder sb = new StringBuilder();
        int total = phase.length();
        for (int i = 0; i < total; i++)	{
            char c = phase.charAt(i);
            if (i == 0) {
                if (isInA2Z(c)) {
                    sb.append(("" + c).toLowerCase());
                }
                else {
                    sb.append(c);
                }
            }
            else {
                if (isInA2Z(c)) {
                    if (isIna2z(phase.charAt(i-1))) {
                        sb.append(("_" + c).toLowerCase());
                    }
                    else {
                        sb.append(("" + c).toLowerCase());
                    }
                }
                else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    private static String A2Z = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String a2z = "abcdefghijklmnopqrstuvwxyz";

    private static boolean isInA2Z(char c) {
        return (A2Z.indexOf(c) != -1)?true:false;
    }

    private static boolean isIna2z(char c) {
        return (a2z.indexOf(c) != -1)?true:false;
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
    public static String titleize(String phase) {
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
    public static String humanize(String phase) {
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
    public static String tableize(String modelClassName) {
        return pluralize(underscore(modelClassName));
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
    public static String classify(String tableName) {
        return camelize(singularize(tableName));
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String removeEnd(String str, String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        if (str.endsWith(remove)) {
            return str.substring(0, str.length() - remove.length());
        }
        return str;
    }

    /**
     * Returns an ordinalized string.
     *
     * <pre>
     * Examples:
     *   ordinalize(100)    "100th"
     *   ordinalize(1003)   "1003rd"
     * </pre>
     *
     * @param number            the number
     * @return an ordinalized string for the number
     */
    public static String ordinalize(int number) {
        String result = "" + number;
        if (result.endsWith("1")) {
            result = result + "st";
        } else if (result.endsWith("2")) {
            result = result + "nd";
        } else if (result.endsWith("3")) {
            result = result + "rd";
        } else {
            result = result + "th";
        }
        return result;
    }
}