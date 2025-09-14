package ee.allerk.helmes_technical_assignment.controller;

import ee.allerk.helmes_technical_assignment.dto.form.UserDto;
import ee.allerk.helmes_technical_assignment.exceptions.AppException;
import ee.allerk.helmes_technical_assignment.service.form.SectorService;
import ee.allerk.helmes_technical_assignment.service.form.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
public class WebController {
    private final SectorService sectorService;
    private final UserService userService;

    @GetMapping(value = "/")
    public String createForm(Model model) {
        model.addAttribute("sectors", sectorService.findAll());
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserDto());
        }
        model.addAttribute("isEdit", false);
        return "index";
    }

    @PostMapping(value = "/")
    public String createUser(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("sectors", sectorService.findAll());
            model.addAttribute("isEdit", false);
            return "index";
        }

        UserDto dto;
        try {
            dto = userService.create(userDto);
        } catch (AppException e){
            result.rejectValue("sectorIds", "invalid.sector", e.getMessage());
            model.addAttribute("sectors", sectorService.findAll());
            return "index";
        }
        model.addAttribute("user", dto);
        model.addAttribute("sectors", sectorService.findAll());
        return "redirect:/edit/" + dto.getId();
    }

    @GetMapping(value = "/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        UserDto user;
        try {
            user = userService.findById(id);
            model.addAttribute("sectors", sectorService.findAll());
            model.addAttribute("user", user);
            model.addAttribute("isEdit", true);
            return "index";
        } catch (AppException e) {
            model.addAttribute("userError", e.getMessage());
            model.addAttribute("isEdit", false);
        }
        return "index";
    }

    @PostMapping(value = "/edit")
    public String editUser(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("sectors", sectorService.findAll());
            model.addAttribute("isEdit", true);
            return "index";
        }

        UserDto dto;
        try {
            dto = userService.partialUpdate(userDto);
        } catch (AppException e){
            result.rejectValue("sectorIds", "invalid.sector", e.getMessage());
            model.addAttribute("sectors", sectorService.findAll());
            return "index";
        }
        model.addAttribute("user", dto);
        model.addAttribute("sectors", sectorService.findAll());
        return "redirect:/edit/" + dto.getId();
    }
}
