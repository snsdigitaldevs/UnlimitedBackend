package com.simonschuster.pimsleur.unlimited.data.dto.promotions;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.simonschuster.pimsleur.unlimited.constants.CommonConstants.USA;

@JsonInclude(NON_EMPTY)
public class UpsellDto {
    private UpsellItem nextLevel;
    private UpsellItem nextSubscription;
    private UpsellItem nextVersion;
    private Map<String, UpsellItem> subscriptionMap;

    public UpsellDto() {
    }

    public static UpsellDto build(UpsellItem nextLevel, UpsellItem nextVersion,
        Map<String, UpsellItem> subscriptionMap) {
        UpsellDto upsellDto = new UpsellDto();
        upsellDto.setNextLevel(nextLevel);
        upsellDto.setNextVersion(nextVersion);
        if (subscriptionMap != null) {
            upsellDto.setNextSubscription(subscriptionMap.get(USA));
            upsellDto.setSubscriptionMap(subscriptionMap);
        }
        return upsellDto;
    }

    public UpsellItem getNextLevel() {
        return nextLevel;
    }

    public UpsellItem getNextVersion() {
        return nextVersion;
    }

    public UpsellItem getNextSubscription() {
        return nextSubscription;
    }

    public void setNextLevel(UpsellItem nextLevel) {
        this.nextLevel = nextLevel;
    }

    public void setNextSubscription(
        UpsellItem nextSubscription) {
        this.nextSubscription = nextSubscription;
    }

    public void setNextVersion(
        UpsellItem nextVersion) {
        this.nextVersion = nextVersion;
    }

    public Map<String, UpsellItem> getSubscriptionMap() {
        return subscriptionMap;
    }

    public void setSubscriptionMap(
        Map<String, UpsellItem> subscriptionMap) {
        this.subscriptionMap = subscriptionMap;
    }
}
