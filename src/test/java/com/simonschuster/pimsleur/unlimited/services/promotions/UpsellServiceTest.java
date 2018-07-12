package com.simonschuster.pimsleur.unlimited.services.promotions;

import com.simonschuster.pimsleur.unlimited.data.dto.promotions.UpsellDto;
import com.simonschuster.pimsleur.unlimited.services.customer.EDTCustomerInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UpsellServiceTest {

    @Mock
    private EDTCustomerInfoService customerInfoService;

    @Autowired
    @Spy
    private PurchaseMappingService purchaseMappingService;

    @Autowired
    @Spy
    private BundleIsbnService bundleIsbnService;

    @Autowired
    @Spy
    private IsbnNameDescriptionService isbnNameDescriptionService;

    @InjectMocks
    private UpsellService upsellService;
    private String storeDomain = "android_inapp";

    @Test
    public void shouldGetUpsellInfoForIsbn() {
        // mock: user has not bought anything yet
        when(customerInfoService.getBoughtIsbns("123", "email", storeDomain)).thenReturn(emptyList());

        UpsellDto upsellInfo = upsellService.getUpsellInfoFor(
                "9781442316034",// pcm chinese level 1
                "123",
                "email", storeDomain);

        assertThat(upsellInfo.getNextLevel().getName(),
                containsString("Chinese (Mandarin) Level 2"));

        assertThat(upsellInfo.getNextVersion().getName(),
                containsString("Chinese (Mandarin) Level 1"));
    }

    @Test
    public void shouldGetUpsellInfoForOtherFormatIsbn() {
        // mock: user has not bought anything yet
        when(customerInfoService.getBoughtIsbns("123", "email", storeDomain)).thenReturn(emptyList());

        UpsellDto upsellInfo = upsellService.getUpsellInfoFor(
                "9781508273790",// pcm chinese level 2
                "123",
                "email", storeDomain);

        assertThat(upsellInfo.getNextLevel().getName(),
                containsString("Chinese (Mandarin) Level 3"));

        assertThat(upsellInfo.getNextVersion().getName(),
                containsString("Chinese (Mandarin) Level 2"));
    }

    @Test
    public void shouldNotGetNextLevelIfAlreadyBought() {
        // mock: user has already bought next level
        when(customerInfoService.getBoughtIsbns("123", "email", storeDomain)).thenReturn(asList("9781508273790"));

        UpsellDto upsellInfo = upsellService.getUpsellInfoFor(
                "9781442316034",// pcm chinese level 1
                "123",
                "email", storeDomain);

        assertThat(upsellInfo.getNextLevel(), nullValue());

        assertThat(upsellInfo.getNextVersion().getName(),
                containsString("Chinese (Mandarin) Level 1"));
    }

    @Test
    public void shouldNotGetNextVersionIfAlreadyBoughtOtherFormatOfNextVersion() {
        // mock: user has already bought next version's other format
        when(customerInfoService.getBoughtIsbns("123", "email", storeDomain)).thenReturn(asList("9781442394889"));

        UpsellDto upsellInfo = upsellService.getUpsellInfoFor(
                "9781508273790",// pcm chinese level 2
                "123",
                "email", storeDomain);

        assertThat(upsellInfo.getNextLevel().getName(),
                containsString("Chinese (Mandarin) Level 3"));

        assertThat(upsellInfo.getNextVersion(), nullValue());
    }

    @Test
    public void shouldNotGetNextLevelIfAlreadyBoughtBundleThatCoversNextLevel() {
        // mock: user has bought chinese 1-4 pcm
        when(customerInfoService.getBoughtIsbns("123", "email", storeDomain)).thenReturn(asList("9781442369351"));

        UpsellDto upsellInfo = upsellService.getUpsellInfoFor(
                "9781442316034",// pcm chinese level 1
                "123",
                "email", storeDomain);

        assertThat(upsellInfo.getNextLevel(), nullValue());

        assertThat(upsellInfo.getNextVersion().getName(),
                containsString("Chinese (Mandarin) Level 1"));
    }

    @Test
    public void shouldNotGetNextVersionIfAlreadyBoughtOtherFormatOfBundleThatCoversNextVersion() {
        // mock: user has already bought a other format of chinese 1-5 PU
        when(customerInfoService.getBoughtIsbns("123", "email", storeDomain)).thenReturn(asList("9781508276197"));

        UpsellDto upsellInfo = upsellService.getUpsellInfoFor(
                "9781508273790",// pcm chinese level 2
                "123",
                "email", storeDomain);

        assertThat(upsellInfo.getNextLevel().getName(),
                containsString("Chinese (Mandarin) Level 3"));

        assertThat(upsellInfo.getNextVersion(), nullValue());
    }

    @Test
    public void shouldGetUpsellInfoForFreeLessonWithoutSub() {
        UpsellDto upsellInfo = upsellService.getUpsellInfoFor(
                "9781508243328",// Chinese (Mandarin) Level 1 Lesson 1 Demo Unlimited
                "",
                "email", storeDomain);

        assertThat(upsellInfo.getNextLevel().getName(),
                containsString("Chinese (Mandarin) Level 1"));

        assertThat(upsellInfo.getNextVersion(), nullValue());
    }
}