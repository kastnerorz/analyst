package cn.kastner.analyst.util.crawler;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class Finder {

    public String getString(String doc, String regex, int group) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(doc);
        if (matcher.find()) {
            return matcher.group(group);
        } else {
            return null;
        }
    }

    public String getString(String doc, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(doc);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }

    public Double getDouble(String doc, int group) {
        Pattern pattern = Pattern.compile("([1-9]\\d*\\.?\\d*|0\\.?\\d*[1-9]\\d*)");
        Matcher matcher = pattern.matcher(doc);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(group));
        } else {
            return null;
        }
    }
}
