package com.dori.cypherstracker.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.dori.cypherstracker.service.CyphersTrackerService;
import com.dori.cypherstracker.vo.MatchInfo;
import com.dori.cypherstracker.vo.MatchInfoProcessor;
import com.dori.cypherstracker.vo.PlayerInfo;

@Controller
public class CyphersTrackerController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private CyphersTrackerService cyphersTrackerService;
	
	// 메인
	@RequestMapping(value = "/main", method = RequestMethod.GET)
    public String main(Model model) {
		return "index";
    }
	
	// 통합 랭킹 조회
	@RequestMapping(value = "/overallRanking", method = RequestMethod.GET)
    public String overallRanking(Model model) {
		List<PlayerInfo> playerList = cyphersTrackerService.getOverallRanking();
		model.addAttribute("playerList", playerList);
		return "overallRanking";
    }
	
	// 캐릭터 선택
	@RequestMapping(value = "/characterSelect", method = RequestMethod.GET)
    public String characterSelect(Model model) {
		return "characterSelect";
    }
	
	// 캐릭터 랭킹 조회(순위, 승률 기준)
	@RequestMapping(value = "/characterRanking", method = RequestMethod.GET)
    public String characterRanking(Model model, @RequestParam(value = "characterName") String characterName) {
		String characterId = cyphersTrackerService.characterCode(characterName);
		List<PlayerInfo> playerList = cyphersTrackerService.getCharacterRanking(characterId);
		if(!playerList.isEmpty()) {
		cyphersTrackerService.sortPlayersByWinRate(playerList);
		model.addAttribute("playerList", playerList);
		model.addAttribute("characterName", characterName);
		return "characterRanking";
		} else {
			model.addAttribute("characterName", characterName);
			return "error";
		}
    }
	
	// 캐릭터 아이템 특성 조회
	@RequestMapping(value = "/characterBulid", method = RequestMethod.GET)
	public String characterBulid(Model model, @RequestParam(value = "characterName") String characterName) {
		MatchInfoProcessor processor = new MatchInfoProcessor();
		String characterId = cyphersTrackerService.characterCode(characterName);
		List<String> matchsIdList = cyphersTrackerService.getMatchsID(characterId);
		if(!matchsIdList.isEmpty()) {
			List<MatchInfo> matchInfoList = cyphersTrackerService.getBuild(matchsIdList, characterId);
			List<MatchInfo> filteredMatchInfoList = processor.filterDuplicateMatchInfo(matchInfoList);
			model.addAttribute("matchInfoList", filteredMatchInfoList);
			model.addAttribute("characterName", characterName);
			return "characterBulid";
		} else {
			model.addAttribute("characterName", characterName);
			return "error";
		}
	} 
}
