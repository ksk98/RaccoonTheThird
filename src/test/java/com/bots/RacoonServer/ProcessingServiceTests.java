package com.bots.RacoonServer;

import com.bots.RacoonServer.Services.ProcessingService;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProcessingServiceTests {
    @Test
    public void commandAndArgumentsSeparationRegexTestBetweenText() {
        Pair<String, List<String>> pair = ProcessingService.getCommandAndArgumentsFrom(
                "hehehehehe fsef ! test $helpdecide \"kran złośliwy\" \"trociny blade suche\" 'koniczyn dwieście' kranówka"
        );

        assertEquals("helpdecide", pair.getFirst());
        assertEquals("\"kran złośliwy\"", pair.getSecond().get(0));
        assertEquals("\"trociny blade suche\"", pair.getSecond().get(1));
        assertEquals("'koniczyn dwieście'", pair.getSecond().get(2));
        assertEquals("kranówka", pair.getSecond().get(3));
    }

    @Test
    public void commandAndArgumentsSeparationRegexTestPrefixes() {
        Pair<String, List<String>> pair = ProcessingService.getCommandAndArgumentsFrom(
                "Bardzo lubię $$$bardzo! Gdyby kózka kwiecień plecień!!1 !!e"
        );

        assertEquals("bardzo!", pair.getFirst());
        assertEquals(5, pair.getSecond().size());

        pair = ProcessingService.getCommandAndArgumentsFrom(
                "Gdyby kózka kwiecień plecień!!1 !!e"
        );

        assertEquals("1", pair.getFirst());
        assertEquals(1, pair.getSecond().size());
    }
}
