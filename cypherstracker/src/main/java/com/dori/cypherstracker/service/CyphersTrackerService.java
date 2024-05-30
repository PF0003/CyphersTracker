package com.dori.cypherstracker.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dori.cypherstracker.vo.MatchInfo;
import com.dori.cypherstracker.vo.PlayerInfo;

@Service
public class CyphersTrackerService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${external.api.key}")
    private String apiKey;
	
	// 통합 랭킹 조회
	public List<PlayerInfo> getOverallRanking() {
		List<PlayerInfo> playerList = new ArrayList<>();
		String offset = "0";
        String limit = "30";
		String apiUrl = "https://api.neople.co.kr/cy/ranking/ratingpoint?offset=" + offset + "&limit=" + limit + "&apikey=" + apiKey;
        
        // API 호출
        String rankingData = restTemplate.getForObject(apiUrl, String.class);

        // JSON 데이터 파싱
        JSONObject json = new JSONObject(rankingData);
        JSONArray rows = json.getJSONArray("rows");

        // 각 객체에서 필요한 값 추출하여 리스트에 추가
        for (int i = 0; i < rows.length(); i++) {
            JSONObject row = rows.getJSONObject(i);
            int rank = row.getInt("rank");
            String playerId = row.getString("playerId");
            String nickname = row.getString("nickname");
            int grade = row.getInt("grade");
            int ratingPoint = row.getInt("ratingPoint");
            String clanName = row.isNull("clanName") ? null : row.getString("clanName");
            String characterId = row.getJSONObject("represent").getString("characterId");
            String characterName = row.getJSONObject("represent").getString("characterName");

            PlayerInfo player = new PlayerInfo(rank, playerId, nickname, grade, ratingPoint, clanName, characterId, characterName);
            playerList.add(player);
        }

        return playerList;
    }
	
	//캐릭터 랭킹 조회
	public List<PlayerInfo> getCharacterRanking(String characterId) {
		List<PlayerInfo> charaPlayerList = new ArrayList<>();
		String rankingType = "winCount";
		String limit = "30";
		String apiUrl = "https://api.neople.co.kr/cy/ranking/characters/" + characterId + "/" + rankingType + "?limit=" + limit + "&apikey=" + apiKey;
				
		// API호출
		String rankingData = restTemplate.getForObject(apiUrl, String.class);
		
		// JSON 데이터 파싱
	    JSONObject json = new JSONObject(rankingData);
		JSONArray rows = json.getJSONArray("rows");
		
		// 각 객체에서 필요한 값 추출하여 리스트에 추가
		for (int i = 0; i < rows.length(); i++) {
			JSONObject row = rows.getJSONObject(i);
			int winCount = row.getInt("winCount");
		    int loseCount = row.getInt("loseCount");
		    int totalCount = winCount + loseCount;
		    // 최소 플레이 횟수
			if(totalCount >= 35) {
			    int rank = row.getInt("rank");
			    String playerId = row.getString("playerId");
			    String nickname = row.getString("nickname");
			    int grade = row.getInt("grade");
			    int winRate = calculateWinRatio(winCount, loseCount);
			    String clanName = row.isNull("clanName") ? null : row.getString("clanName");
			 
			    PlayerInfo player = new PlayerInfo(rank, playerId, nickname, grade, winCount, loseCount, winRate, clanName);
			    charaPlayerList.add(player);
			}
		 }
        return charaPlayerList;
    }
	
	// 매치ID 조회
    public List<String> getMatchsID(String characterId) {
    	
    	List<String> topPlayerId = new ArrayList<>();
    	List<String> matchIdList = new ArrayList<>();
    	List<PlayerInfo> charaPlayerList = getCharacterRanking(characterId);
    	sortPlayersByWinRate(charaPlayerList);
    	
    	//charaPlayerList에서 랭킹 상위 5명의 playerId를 가져와 topPlayerId에 추가(인원수가 부족한 경우 예외 추가 예정)
		for (int i = 0; i < Math.min(charaPlayerList.size(), 5); i++) {
	        String playerId = charaPlayerList.get(i).getPlayerId();
	        topPlayerId.add(playerId);
	    }
		
		for (String playerId : topPlayerId) {
			String gameTypeId = "rating";
	    	String startDate = "0";
	    	String endDate = "0";
	    	String next = "next";
	    	String matchesLimit = "100";
	    	String matchesUrl = "https://api.neople.co.kr/cy/players/" + playerId + "/matches?gameTypeId=" + gameTypeId + "&limit=" + matchesLimit + "&apikey=" + apiKey;
	    	
	        //String matchesUrl = "https://api.neople.co.kr/cy/players/" + playerId + "/matches?gameTypeId=" + gameTypeId +"&startDate=" + startDate + "&endDate=" + endDate + "&limit=" + limit + "&next=" + next + "&apikey=" + apikey;
	    	
	    	// Api 연동
	    	String matchesData = restTemplate.getForObject(matchesUrl, String.class);
	    	
	    	// JSON 데이터 파싱
	    	JSONObject matchesJson = new JSONObject(matchesData).getJSONObject("matches");
			JSONArray matchesIdRows = matchesJson.getJSONArray("rows");
			
			// 캐릭터ID가 같은 경우의 매치ID를 검색
			for (int i = 0; i < matchesIdRows.length(); i++) {
				JSONObject matchesRow = matchesIdRows.getJSONObject(i);
				String matchesCharacterId = matchesRow.getJSONObject("playInfo").getString("characterId");
			
				if(characterId.equals(matchesCharacterId)) {
					String matchId = matchesRow.getString("matchId");
					matchIdList.add(matchId);
				}
			 }
		}
		return matchIdList;
    }
    
    // 매칭 상세 정보 조회(아이템, 특성), (matchsID()의 값을 매개변수로 가져옴), (최근1달 기준)
    public List<MatchInfo> getBuild(List<String> matchIdList, String characterId) {
		
    	List<MatchInfo> matchInfoList = new ArrayList<>();
    	
    	for (String matchId : matchIdList) {
			String apiUrl = "https://api.neople.co.kr/cy/matches/" + matchId + "?apikey=" + apiKey;
			
			// API 연동
			String matchesData = restTemplate.getForObject(apiUrl, String.class);
			
			// JSON 데이터 파싱
		    JSONObject matchJson = new JSONObject(matchesData);
			JSONArray matchPlayers = matchJson.getJSONArray("players");
			
			for (int i = 0; i < matchPlayers.length(); i++) {
				JSONObject row = matchPlayers.getJSONObject(i);
				
				MatchInfo matchInfo = new MatchInfo();
	            String matchCharacterId = row.getJSONObject("playInfo").getString("characterId");
	            
	            if(characterId.equals(matchCharacterId)) {
	            	String characterName = row.getJSONObject("playInfo").getString("characterName");
	            	matchInfo.setCharacterName(characterName);
		            // items 파싱
		            JSONArray itemsArray = row.getJSONArray("items");
		            List<MatchInfo.ItemInfo> itemsList = new ArrayList<>();
		            for (int j = 0; j < itemsArray.length(); j++) {
		                JSONObject item = itemsArray.getJSONObject(j);
		                String itemId = item.getString("itemId");
		                String itemName = item.getString("itemName");
		                String slotName = item.getString("slotName");
		                String rarityName = item.getString("rarityName");
		
		                MatchInfo.ItemInfo itemInfo = new MatchInfo.ItemInfo();
		                itemInfo.setItemId(itemImg(itemId));
		                itemInfo.setItemName(itemName);
		                itemInfo.setSlotName(slotName);
		                itemInfo.setRarityName(rarityName);
		
		                itemsList.add(itemInfo);
		            }
		            matchInfo.setItems(itemsList);
		            
		            // attributes 파싱
		            JSONObject position = row.getJSONObject("position");
		            JSONArray attributesArray = position.getJSONArray("attribute");
		            List<MatchInfo.AttributeInfo> attributesList = new ArrayList<>();
		            for (int j = 0; j < attributesArray.length(); j++) {
		                JSONObject attribute = attributesArray.getJSONObject(j);
		                String attributeId = attribute.getString("id");
		                int attributeLevel = attribute.getInt("level");
		                String attributeName = attribute.getString("name");
		         
		                MatchInfo.AttributeInfo attributeInfo = new MatchInfo.AttributeInfo();
		                attributeInfo.setAttributeId(attributeImg(attributeId));
		                attributeInfo.setAttributeLevel(attributeLevel);
		                attributeInfo.setAttributeName(attributeName);
		
		                attributesList.add(attributeInfo);
		            }
		            matchInfo.setAttributes(attributesList);
		
		            matchInfoList.add(matchInfo);
		            
		            break;
				}
			}
    	}
		return matchInfoList;
    }
    // 캐릭터 이름 코드로 변환
    public String characterCode(String name) {
    	String characterCode = null;
    	String apiUrl = "https://api.neople.co.kr/cy/characters?apikey=" + apiKey;
		
		// API 연동
		String codeData = restTemplate.getForObject(apiUrl, String.class);
		
		// JSON 데이터 파싱
        JSONObject json = new JSONObject(codeData);
        JSONArray rows = json.getJSONArray("rows");
        
        for(int i=0;i < rows.length(); i++) {
	        JSONObject row = rows.getJSONObject(i);
	        String characterId = row.getString("characterId");
	        String characterName = row.getString("characterName");
	        if(name.equals(characterName)) {
	        	characterCode = characterId;
	        	break;
	        }
        }
          
    	return characterCode;
    }
    //아이템 이미지 변환
    public String itemImg(String itemId) {
    	String apiUrl = "https://img-api.neople.co.kr/cy/items/" + itemId;
    	return apiUrl;
    }
    
    //특성 이미지 변환
    public String attributeImg(String attributeId) {
    	String apiUrl = "https://img-api.neople.co.kr/cy/position-attributes/" + attributeId;
    	return apiUrl;
    }
    
	// 승률 계산 메소드
	public int calculateWinRatio(int winCount, int loseCount){
		// 0으로 나누는 것을 방지하기 위한 예외 처리
	    if (winCount + loseCount == 0)
	        return 0;
	    
	    double ratio = (double)winCount / (winCount + loseCount);
	    int percentage = (int)(ratio * 100);
	    return percentage;
	}
	
	// 승률 기준 재정렬
    public void sortPlayersByWinRate(List<PlayerInfo> playerList) {
        playerList.sort(Comparator.comparingInt(PlayerInfo::getWinRate).reversed());
        for (int i = 0; i < playerList.size(); i++) {
            playerList.get(i).setRank(i + 1);
        }
    }
}
