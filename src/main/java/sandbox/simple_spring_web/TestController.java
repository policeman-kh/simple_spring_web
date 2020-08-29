package sandbox.simple_spring_web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController {
    @GetMapping
    public ModelAndView index() {
        final List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i + ": AAAA BBBB CCCC");
        }
        return new ModelAndView("index")
                .addObject("list", list);
    }

    @GetMapping("moreList")
    public ModelAndView moreList(int start){
        final List<String> list = new ArrayList<>();
        for (int i = start; i < 100 + start; i++) {
            list.add(i + ": AAAA BBBB CCCC");
        }
        return new ModelAndView("index :: FnMoreList")
                .addObject("list", list);
    }
}
