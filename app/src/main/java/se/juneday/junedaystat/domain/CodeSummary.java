package se.juneday.junedaystat.domain;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CodeSummary implements Iterable<CodeSummary.Stat> {

    public final static String LANG_JAVA = "Java";
    public final static String LANG_BASH = "Bash";
    public final static String LANG_C = "C";
    public static final String LANG_BUILD = "Build";

  @NonNull
    @Override
    public Iterator iterator() {
        return langStat.entrySet().iterator();
    }

    public static class Stat {
        String lang;
        int loc;

        public String getLang() {
            return lang;
        }

        public int getLoc() {
            return loc;
        }

        public int getFiles() {
            return files;
        }

        int files;

        public Stat(String lang, int loc, int files) {
            this.loc = loc;
            this.files = files;
            this.lang = lang;
        }

        @Override
        public String toString() {
            return "Stat{" +
                    ", lang=" + lang +
                    ", loc=" + loc +
                    ", files=" + files +
                    '}';
        }
    }

    private Map<String, Stat> langStat;

    public Stat stat(String lang) {
        return langStat.get(lang);
    }

    public CodeSummary() {
        langStat = new HashMap<>();
    }

    public void addLanguage(String lang, Stat stat) {
        langStat.put(lang, stat);
    }

    @Override
    public String toString() {
        return "CodeSummary{" +
                "langStat=" + langStat +
                '}';
    }

/*        {\n"+
            "            \"lines-of-code\": \"62352\",\n"+
            "            \"number-of-files\": \"1220\",\n"+
            "            \"type\": \"Java\"\n"+
            "        },\n"+
            "        {\n"+
            "            \"lines-of-code\": \"31770\",\n"+
            "            \"number-of-files\": \"464\",\n"+
            "            \"type\": \"C\"\n"+
            "        },\n"+
            "        {\n"+
            "            \"lines-of-code\": \"12169\",\n"+
            "            \"number-of-files\": \"292\",\n"+
            "            \"type\": \"Bash\"\n"+
            "        },\n"+
            "        {\n"+
            "            \"lines-of-code\": \"8018\",\n"+
            "            \"number-of-files\": \"131\",\n"+
            "            \"type\": \"Doc\"\n"+
            "        },\n"+
            "        {\n"+
            "            \"lines-of-code\": \"7684\",\n"+
            "            \"number-of-files\": \"240\",\n"+
            "            \"type\": \"Build\"\n"+
            "        }\n"+
            "    ],"*""
*/
}
