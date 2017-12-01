package constraintanalyzer;

import constraintanalyzer.analysis.Analyzer;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, XmlPullParserException {
        new Analyzer().run();
    }
}
