package com.apap.tutorial3.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tutorial3.model.PilotModel;
import com.apap.tutorial3.service.PilotService;

@Controller
public class PilotController {
	@Autowired
	private PilotService pilotService;
	
	@RequestMapping("pilot/add")
	public String add(
			@RequestParam(value="id", required=true) String id,
			@RequestParam(value="licenseNumber", required=true) String licenseNumber,
			@RequestParam(value="name", required=true) String name,
			@RequestParam(value="flyHour", required=true) int flyHour) {

	PilotModel pilot = new PilotModel (id,licenseNumber,name,flyHour);
	pilotService.addPilot(pilot);
	return "add";
	}
	
	@RequestMapping("pilot/view")
	public String add(
			@RequestParam("licenseNumber") String licenseNumber, Model model) {
		PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
		
		model.addAttribute("pilot",archive);
		return "view-pilot";
	}
	
	@RequestMapping("pilot/viewall")
	public String viewall(Model model) {
		List<PilotModel> archive = pilotService.getPilotList();
		
		model.addAttribute("listPilot", archive);
		return "viewall-pilot";
	}
	
	@RequestMapping(value= {"/pilot/view/license-number", "/pilot/view/license-number/{licenseNumber}"})
	public String viewLicenseNumber(@PathVariable (value="licenseNumber",required=false) String licenseNumber,Model model) {
		PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
		
		if (archive == null) {
			return "pilotNotFound";
		}
		else {
			model.addAttribute("pilot", archive);
			return "view-pilot";
		}
	}
	
	@RequestMapping(value= {"/pilot/update/license-number/{licenseNumber}/fly-hour/{flyHour}", "/pilot/update/license-number/"})
	public String updateflyHourLN(@PathVariable (value= "licenseNumber",required=false) String licenseNumber,
			@PathVariable (value="flyHour",required=false) Integer flyHour,Model model) {
		PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
		
		if (archive == null) {
			return "idNotFound";
		}
		else {
			archive.setFlyHour(flyHour);
			return "changeFlyHour";
		}
	}
	
	@RequestMapping(value= {"/pilot/delete/id/{id}", "/pilot/delete/id/"})
	public String deletePilot(@PathVariable (value="id",required=false) String id,Model model) {
		
		if (id != null) {
			boolean deleted = pilotService.deletePilot(id);
			if (deleted == true) {
				return "deleteSuccess";
			}
			else {
				return "deleteFailed";
			}
		}
		return "deleteFailed";
	}
}