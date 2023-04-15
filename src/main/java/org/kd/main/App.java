package org.kd.main;

import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    public static void main(String[] args) {
        Logger.getLogger(App.class.getSimpleName()).log(Level.INFO, "It's just a lib, bro");
        Logger.getLogger(App.class.getSimpleName()).log(Level.INFO, "Try running some tests with:");
        Logger.getLogger(App.class.getSimpleName()).log(Level.INFO, "\njava -cp junit.jar:math2-0.0.1-SNAPSHOT-jar-with-dependencies.jar org.junit.runner.JUnitCore org.kd.LineAgTest");
        Logger.getLogger(App.class.getSimpleName()).log(Level.INFO, "\njava -cp junit.jar:math2-0.0.1-SNAPSHOT-jar-with-dependencies.jar org.junit.runner.JUnitCore org.kd.LineSectionTest");
        Logger.getLogger(App.class.getSimpleName()).log(Level.INFO, "\njava -cp junit.jar:math2-0.0.1-SNAPSHOT-jar-with-dependencies.jar org.junit.runner.JUnitCore org.kd.PolygonAgTest");
    }
}
