package com.dori.cypherstracker.vo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MatchInfoProcessor {
	
	// 중복된 MatchInfo 객체를 필터링(중복 제거)
	public List<MatchInfo> filterDuplicateMatchInfo(List<MatchInfo> matchInfoList) {
        Set<String> seenItems = new HashSet<>();
        Set<String> seenAttributes = new HashSet<>();
        List<MatchInfo> filteredList = new ArrayList<>();

        for (MatchInfo matchInfo : matchInfoList) {
            if (!isDuplicate(matchInfo, seenItems, seenAttributes)) {
                filteredList.add(matchInfo);
            }
        }
        
        return filteredList;
    }
	
	// MatchInfo 객체의 중복 체크(개별 아이템 및 속성을 기준으로)
    private boolean isDuplicate(MatchInfo matchInfo, Set<String> seenItems, Set<String> seenAttributes) {
        boolean isDuplicate = true;
        
        for (MatchInfo.ItemInfo item : matchInfo.getItems()) {
            String itemKey = item.getItemId() + item.getSlotName();
            if (!seenItems.contains(itemKey)) {
                isDuplicate = false;
                seenItems.add(itemKey);
            }
        }

        for (MatchInfo.AttributeInfo attribute : matchInfo.getAttributes()) {
            String attributeKey = attribute.getAttributeId() + attribute.getAttributeLevel();
            if (!seenAttributes.contains(attributeKey)) {
                isDuplicate = false;
                seenAttributes.add(attributeKey);
            }
        }
        
        return isDuplicate;
    }
}
