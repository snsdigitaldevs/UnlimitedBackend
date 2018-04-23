package com.simonschuster.pimsleur.unlimited.utils;

import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.ProgressDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.UserAppStateDatum;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PCMProgressConverterTest {

    @Test
    public void shouldGroupByIdAndConvertToOneDTO() {
        List<UserAppStateDatum> edtProgresses = asList(
                new UserAppStateDatum("com.edt.models::MediaItemHistory_124302978144231987541916#audioTrackComplete", true, ""),
                new UserAppStateDatum("com.edt.models::MediaItemHistory_124302978144231987541916#lastAudioPosMillis", 123.234, ""),
                new UserAppStateDatum("com.edt.models::MediaItemHistory_124302978144231987541916#lastAccessDate", 1524056020178L, "")
        );

        List<ProgressDTO> progressDTOS = PCMProgressConverter.edtProgressToProgressDto(edtProgresses);

        assertThat(progressDTOS.get(0).getCompleted(), is(true));
        assertThat(progressDTOS.get(0).getLastPlayHeadLocation(), is(123.234));
        assertThat(progressDTOS.get(0).getLastPlayedDate(), is(1524056020178L));
    }

}