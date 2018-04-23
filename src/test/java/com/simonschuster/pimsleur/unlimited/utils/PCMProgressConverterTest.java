package com.simonschuster.pimsleur.unlimited.utils;

import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.ProgressDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.UserAppStateDatum;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class PCMProgressConverterTest {

    @Test
    public void shouldGroupByIdAndConvertToOneDTO() {
        List<UserAppStateDatum> edtProgresses = asList(
                new UserAppStateDatum("com.edt.models::MediaItemHistory_124302978144231987541916#audioTrackComplete", true, ""),
                new UserAppStateDatum("com.edt.models::MediaItemHistory_124302978144231987541916#lastAudioPosMillis", 123.234, ""),
                new UserAppStateDatum("com.edt.models::MediaItemHistory_124302978144231987541916#lastAccessDate", 1524056020178L, "")
        );

        List<ProgressDTO> progressDTOS = PCMProgressConverter.pcmProgressToDto(edtProgresses);
        ProgressDTO progressDTO = progressDTOS.get(0);

        assertThat(progressDTO.getCompleted(), is(true));
        assertThat(progressDTO.getLastPlayHeadLocation(), is(123.234));
        assertThat(progressDTO.getLastPlayedDate(), is(1524056020178L));

        assertThat(progressDTO.getProductCode(), is("9781442319875"));
        assertThat(progressDTO.getMediaItemId(), is(41916));

        assertThat(progressDTO.getCurrent(), is(false));
    }

    @Test
    public void shouldFindCurrentItem() {
        List<UserAppStateDatum> edtProgresses = asList(
                new UserAppStateDatum("com.edt.models::MediaItemHistory_124302978144231987541916#audioTrackComplete", true, ""),
                new UserAppStateDatum("com.edt.models::Customer_124302#currentMediaSetHistoryId", "1243029781442314931", ""),
                new UserAppStateDatum("com.edt.models::MediaSetHistory_1243029781442314931#currentMediaItemHistoryId", "124302978144231987541916", "")
        );

        List<ProgressDTO> progressDTOS = PCMProgressConverter.pcmProgressToDto(edtProgresses);
        ProgressDTO progressDTO = progressDTOS.get(0);

        assertThat(progressDTO.getCurrent(), is(true));
    }

}