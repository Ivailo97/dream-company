package dreamcompany.web.controller;

import dreamcompany.domain.model.binding.OfficeCreateBindingModel;
import dreamcompany.domain.model.binding.OfficeEditBindingModel;
import dreamcompany.domain.model.service.OfficeServiceModel;
import dreamcompany.domain.model.view.OfficeDeleteViewModel;
import dreamcompany.domain.model.view.OfficeDetailsViewModel;
import dreamcompany.domain.model.view.OfficeFetchViewModel;
import dreamcompany.service.interfaces.OfficeService;
import dreamcompany.util.MappingConverter;
import dreamcompany.validation.office.binding.OfficeCreateValidator;
import dreamcompany.validation.office.binding.OfficeEditValidator;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/offices")
public class OfficeController extends BaseController {

    private final OfficeService officeService;

    private final MappingConverter mappingConverter;

    private final OfficeCreateValidator createValidator;

    private final OfficeEditValidator editValidator;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView createConfirm(@Valid @ModelAttribute("model") OfficeCreateBindingModel model,
                                      BindingResult bindingResult) {
        createValidator.validate(model, bindingResult);
        if (bindingResult.hasErrors()) {
            return view("validation/invalid-office-form");
        }

        OfficeServiceModel officeServiceModel = mappingConverter.map(model, OfficeServiceModel.class);
        officeService.create(officeServiceModel);
        return redirect("/home");
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView details(@PathVariable String id, ModelAndView modelAndView) {
        OfficeDetailsViewModel officeDetailsViewModel = mappingConverter
                .map(officeService.findById(id), OfficeDetailsViewModel.class);
        modelAndView.addObject("model", officeDetailsViewModel);
        return view("office/details", modelAndView);
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView delete(@PathVariable String id, ModelAndView modelAndView) {
        OfficeDeleteViewModel officeDetailsViewModel = mappingConverter
                .map(officeService.findById(id), OfficeDeleteViewModel.class);
        modelAndView.addObject("model", officeDetailsViewModel);
        return view("office/delete", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView deleteConfirm(@PathVariable String id, Principal principal) throws IOException {
        officeService.delete(id,principal.getName());
        return redirect("/show");
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView edit(@PathVariable String id, ModelAndView modelAndView) {
        OfficeEditBindingModel officeEditBindingModel = mappingConverter
                .map(officeService.findById(id), OfficeEditBindingModel.class);
        modelAndView.addObject("model", officeEditBindingModel);
        return view("office/edit", modelAndView);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView editConfirm(@PathVariable String id,
                                    @Valid @ModelAttribute(name = "model") OfficeEditBindingModel model,
                                    BindingResult bindingResult) {
        editValidator.validate(model,bindingResult);
        if (bindingResult.hasErrors()) {
            return view("office/edit");
        }

        OfficeServiceModel officeServiceModel = mappingConverter.map(model, OfficeServiceModel.class);
        officeService.edit(id, officeServiceModel);
        return redirect("/show");
    }

    @GetMapping("/fetch")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @ResponseBody
    public List<OfficeFetchViewModel> fetch() {
        return mappingConverter.convertCollection(officeService.findAll(),OfficeFetchViewModel.class);
    }
}
