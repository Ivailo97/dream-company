package dreamcompany.web.controller;

import dreamcompany.domain.model.binding.OfficeCreateBindingModel;
import dreamcompany.domain.model.binding.OfficeEditBindingModel;
import dreamcompany.domain.model.service.OfficeServiceModel;
import dreamcompany.domain.model.view.OfficeAllViewModel;
import dreamcompany.domain.model.view.OfficeDeleteViewModel;
import dreamcompany.domain.model.view.OfficeDetailsViewModel;
import dreamcompany.domain.model.view.OfficeFetchViewModel;
import dreamcompany.service.interfaces.OfficeService;
import dreamcompany.validation.binding.office.OfficeCreateValidator;
import dreamcompany.validation.binding.office.OfficeEditValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/offices")
@PreAuthorize("isAuthenticated()")
public class OfficeController extends BaseController {

    private final OfficeService officeService;

    private final OfficeCreateValidator createValidator;

    private final OfficeEditValidator editValidator;

    private final ModelMapper modelMapper;

    @Autowired
    public OfficeController(OfficeService officeService, OfficeCreateValidator createValidator, OfficeEditValidator editValidator, ModelMapper modelMapper) {
        this.officeService = officeService;
        this.createValidator = createValidator;
        this.editValidator = editValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView create(@ModelAttribute("model") OfficeCreateBindingModel model) {
        return view("/office/create");
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView createConfirm(@Valid @ModelAttribute("model") OfficeCreateBindingModel model,
                                      BindingResult bindingResult) {

        createValidator.validate(model, bindingResult);

        if (bindingResult.hasErrors()) {
            return view("/office/create");
        }

        OfficeServiceModel officeServiceModel = modelMapper.map(model, OfficeServiceModel.class);
        officeService.create(officeServiceModel);
        return redirect("/home");
    }

    @GetMapping("/our")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView listOur(ModelAndView modelAndView) {

        List<OfficeAllViewModel> viewModels = officeService.findAll()
                .stream()
                .map(x -> modelMapper.map(x, OfficeAllViewModel.class))
                .collect(Collectors.toList());

        modelAndView.addObject("models", viewModels);

        return view("/office/our", modelAndView);
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView listAll(ModelAndView modelAndView) {

        List<OfficeAllViewModel> viewModels = officeService.findAll()
                .stream()
                .map(x -> modelMapper.map(x, OfficeAllViewModel.class))
                .collect(Collectors.toList());

        modelAndView.addObject("models", viewModels);

        return view("/office/all", modelAndView);
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView details(@PathVariable String id, ModelAndView modelAndView) {

        OfficeDetailsViewModel officeDetailsViewModel = modelMapper
                .map(officeService.findById(id), OfficeDetailsViewModel.class);
        modelAndView.addObject("model", officeDetailsViewModel);

        return view("/office/details", modelAndView);
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView delete(@PathVariable String id, ModelAndView modelAndView) {

        OfficeDeleteViewModel officeDetailsViewModel = modelMapper
                .map(officeService.findById(id), OfficeDeleteViewModel.class);
        modelAndView.addObject("model", officeDetailsViewModel);

        return view("/office/delete", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView deleteConfirm(@PathVariable String id) {

        officeService.delete(id);
        return redirect("/offices/all");
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView edit(@PathVariable String id, ModelAndView modelAndView) {

        OfficeEditBindingModel officeEditBindingModel = modelMapper
                .map(officeService.findById(id), OfficeEditBindingModel.class);

        modelAndView.addObject("model", officeEditBindingModel);
        return view("/office/edit", modelAndView);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView editConfirm(@PathVariable String id,
                                    @Valid @ModelAttribute(name = "model") OfficeEditBindingModel model,
                                    BindingResult bindingResult) {

        editValidator.validate(model,bindingResult);

        if (bindingResult.hasErrors()) {
            return view("/office/edit");
        }

        OfficeServiceModel officeServiceModel = modelMapper.map(model, OfficeServiceModel.class);

        officeService.edit(id, officeServiceModel);

        return redirect("/offices/all");
    }

    @GetMapping("/fetch")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @ResponseBody
    public List<OfficeFetchViewModel> fetch() {

        return officeService.findAll().stream()
                .map(o -> modelMapper.map(o, OfficeFetchViewModel.class))
                .collect(Collectors.toList());
    }
}
