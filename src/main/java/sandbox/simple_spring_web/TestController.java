package sandbox.simple_spring_web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;
import sandbox.simple_spring_web.client.ZipAddressClient;
import sandbox.simple_spring_web.client.ZipAddressResponse;

@AllArgsConstructor
@Controller
public class TestController {
    private final ZipAddressClient zipAddressClient;

    @GetMapping
    public ModelAndView index() {
        ZipAddressResponse zipAddressResponse = null;
        try {
            zipAddressResponse = zipAddressClient.search("8130011").execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i + ": AAAA BBBB CCCC");
        }
        return new ModelAndView("index")
                .addObject("list", list)
                .addObject("address", zipAddressResponse.getResults());
    }

    @GetMapping("moreList")
    public ModelAndView moreList(int start) {
        final List<String> list = new ArrayList<>();
        for (int i = start; i < 100 + start; i++) {
            list.add(i + ": AAAA BBBB CCCC");
        }
        return new ModelAndView("index :: FnMoreList")
                .addObject("list", list);
    }
}
