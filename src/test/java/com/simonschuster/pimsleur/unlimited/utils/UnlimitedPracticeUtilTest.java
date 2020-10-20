package com.simonschuster.pimsleur.unlimited.utils;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class UnlimitedPracticeUtilTest {

    @Test
    public void should_move_period_to_left() {
        String sentence = "אני מבינה קצת עברית.";
        String result = UnlimitedPracticeUtil.movePunctuatorToLeftForHebrew(sentence);

        assertThat(result, not(sentence));
        assertThat(result.startsWith(UnlimitedPracticeUtil.PERIOD), is(true));
    }

    @Test
    public void should_move_quote_to_left() {
        String sentence = "את מבינה?";
        String result = UnlimitedPracticeUtil.movePunctuatorToLeftForHebrew(sentence);

        assertThat(result, not(sentence));
        assertThat(result.startsWith(UnlimitedPracticeUtil.QUOTE), is(true));
    }

    @Test
    public void should_move_exclamation_to_left() {
        String sentence = "זה כן יקר!";
        String result = UnlimitedPracticeUtil.movePunctuatorToLeftForHebrew(sentence);

        assertThat(result, not(sentence));
        assertThat(result.startsWith(UnlimitedPracticeUtil.EXCLAMATION), is(true));
    }

    @Test
    public void should_move_ellipses_three_to_left() {
        String sentence ="אחרי הסרט ...";
        String result = UnlimitedPracticeUtil.movePunctuatorToLeftForHebrew(sentence);

        assertThat(result, not(sentence));
        assertThat(result.startsWith("... "), is(true));
    }
}