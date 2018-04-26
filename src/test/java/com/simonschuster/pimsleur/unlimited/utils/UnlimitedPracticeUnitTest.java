package com.simonschuster.pimsleur.unlimited.utils;

import com.simonschuster.pimsleur.unlimited.data.dto.practices.AvailablePractices;
import com.simonschuster.pimsleur.unlimited.services.practices.PracticesCsvLocations;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UnlimitedPracticeUnitTest {
    @Test
    public void shouldGetAvailablePractices() throws IOException {
        PracticesCsvLocations paths = new PracticesCsvLocations(
                "https://install.pimsleurunlimited.com/staging_n/mobile/mandarinchinese/Mandarin Chinese I/metadata/timecode/9781442394872_Mandarin_1_FC.csv",
                "https://install.pimsleurunlimited.com/staging_n/mobile/mandarinchinese/Mandarin Chinese I/metadata/timecode/9781442394872_Mandarin_1_QZ.csv",
                "https://install.pimsleurunlimited.com/staging_n/mobile/mandarinchinese/Mandarin Chinese I/metadata/timecode/9781442394872_Mandarin_1_RL.csv",
                "https://install.pimsleurunlimited.com/staging_n/mobile/mandarinchinese/Mandarin Chinese I/metadata/timecode/9781442394872_Mandarin_1_VC.csv"
        );
        AvailablePractices a = UnlimitedPracticeUtil.getAvailablePractices(paths);
        assertThat( UnlimitedPracticeUtil.getAvailablePractices(paths).getPracticesInUnits().size(), is(30));
    }

}
