package com.simonschuster.pimsleur.unlimited.integration;

import com.simonschuster.pimsleur.unlimited.data.dto.practices.AvailablePractices;
import com.simonschuster.pimsleur.unlimited.services.practices.PracticesCsvLocations;
import com.simonschuster.pimsleur.unlimited.utils.UnlimitedPracticeUtil;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UnlimitedAvailablePracticesIntegrationTest {
    @Test
    public void shouldGetAvailablePracticesByUrls() throws Exception {
        PracticesCsvLocations practiceCsvLocations  =new PracticesCsvLocations(
                "https://install.pimsleurunlimited.com/staging_n/mobile/mandarinchinese/Mandarin Chinese I/metadata/timecode/9781442394872_Mandarin_1_FC.csv",
                "https://install.pimsleurunlimited.com/staging_n/mobile/mandarinchinese/Mandarin Chinese I/metadata/timecode/9781442394872_Mandarin_1_QZ.csv",
                "https://install.pimsleurunlimited.com/staging_n/mobile/mandarinchinese/Mandarin Chinese I/metadata/timecode/9781442394872_Mandarin_1_RL.csv",
                "https://install.pimsleurunlimited.com/staging_n/mobile/mandarinchinese/Mandarin Chinese I/metadata/timecode/9781442394872_Mandarin_1_VC.csv"
        );
        AvailablePractices availablePractices = UnlimitedPracticeUtil.getAvailablePractices(practiceCsvLocations);
        assertThat(availablePractices.getPracticesInUnits().size(), is(30));
    }
}
