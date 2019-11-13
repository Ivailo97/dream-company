package dreamcompany.web.controller;

import org.springframework.web.servlet.ModelAndView;

public abstract class BaseController {

    protected ModelAndView view(String viewName, ModelAndView modelAndView) {
        modelAndView.setViewName(viewName);
        return modelAndView;
    }

    protected ModelAndView view(String viewName) {
        return view(viewName, new ModelAndView());
    }

    protected ModelAndView redirect(String url) {
        return view("redirect:" + url);
    }

}
