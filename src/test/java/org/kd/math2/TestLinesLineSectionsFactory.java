package org.kd.math2;

import java.util.Arrays;
import java.util.List;

class TestLinesLineSectionsFactory {

    static LineAG createDiagonalLine()
    {
        return new LineAG(1, 0);
    }

    static List<LineSection> createExpectedLineSectionDivs0033() {

        return Arrays.asList(
                new LineSection(2, 2, 3, 3),
                new LineSection(1, 1, 2, 2),
                new LineSection(0, 0, 1, 1)
        );
    }

    static List<LineSection> createExpectedLineSectionDivs121212212() {

        return Arrays.asList(
                new LineSection(12, 212, 12, 211),
                new LineSection(12, 211, 12, 210),
                new LineSection(12, 210, 12, 209)
        );
    }
    static List<LineSection> createExpectedLineSectionDivs1154(){

    return Arrays.asList(
            new LineSection(5,4, 3,2.5f),
            new LineSection(3,2.5f, 1,1)
    );}

    static List<LineSection> createExpectedLineSectionDivs5411() {
        return Arrays.asList(
                new LineSection(3, 2.5f, 1, 1),
                new LineSection(5, 4, 3, 2.5f)
        );
    }

    static List<LineSection> createExpectedLineSectionDivs14301010() {

     return    Arrays.asList(
                new LineSection(10, 10, 11, 15),
                new LineSection(11, 15, 12, 20)
        );
    }
}
