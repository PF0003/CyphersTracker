package com.dori.cypherstracker.vo;

import java.util.List;

public class MatchInfo {
    private String characterId;
    private String characterName;
    private List<ItemInfo> items;
    private List<AttributeInfo> attributes;
	
    public String getCharacterId() {
        return characterId;
    }
    public void setCharacterId(String characterId) {
        this.characterId = characterId;
    }
    public String getCharacterName() {
        return characterName;
    }
    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }
    public List<ItemInfo> getItems() {
        return items;
    }
    public void setItems(List<ItemInfo> items) {
        this.items = items;
    }
	public List<AttributeInfo> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<AttributeInfo> attributes) {
		this.attributes = attributes;
	}



	// 정적 내부 클래스 ItemInfo
    public static class ItemInfo {
        private String itemId;
        private String itemName;
        private String slotName;
        private String rarityName;

        public String getItemId() {
            return itemId;
        }
        public void setItemId(String itemId) {
            this.itemId = itemId;
        }
        public String getItemName() {
            return itemName;
        }
        public void setItemName(String itemName) {
            this.itemName = itemName;
        }
        public String getSlotName() {
            return slotName;
        }
        public void setSlotName(String slotName) {
            this.slotName = slotName;
        }
        public String getRarityName() {
            return rarityName;
        }
        public void setRarityName(String rarityName) {
            this.rarityName = rarityName;
        }
    }
    // 정적 내부 클래스 AttributeInfo
    public static class AttributeInfo {
    	private String attributeId;
    	private int attributeLevel;
        private String attributeName;
		
        public String getAttributeId() {
			return attributeId;
		}
		public void setAttributeId(String attributeId) {
			this.attributeId = attributeId;
		}
		public int getAttributeLevel() {
			return attributeLevel;
		}
		public void setAttributeLevel(int attributeLevel) {
			this.attributeLevel = attributeLevel;
		}
		public String getAttributeName() {
			return attributeName;
		}
		public void setAttributeName(String attributeName) {
			this.attributeName = attributeName;
		}
    }
}
