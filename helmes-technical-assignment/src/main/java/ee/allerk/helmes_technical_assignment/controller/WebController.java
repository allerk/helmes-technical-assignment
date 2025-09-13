package ee.allerk.helmes_technical_assignment.controller;

import ee.allerk.helmes_technical_assignment.dto.form.UserDto;
import ee.allerk.helmes_technical_assignment.service.form.SectorService;
import ee.allerk.helmes_technical_assignment.service.form.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class WebController {
    private final SectorService sectorService;
    private final UserService userService;

    @GetMapping(value = "/")
    public String index(Model model) {
        model.addAttribute("sectors", sectorService.findAll());
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserDto());
        }
        return "index";
    }

    @PostMapping(value = "/")
    public String createUser(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("sectors", sectorService.findAll());
            return "index";
        }

        userService.create(userDto);
        return "redirect:/index";
    }
}
