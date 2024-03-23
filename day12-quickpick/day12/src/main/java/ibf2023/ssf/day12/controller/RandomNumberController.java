package ibf2023.ssf.day12.controller;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ibf2023.ssf.day12.models.QuickPick;

@Controller
@RequestMapping(path = "/rnd", produces = MediaType.TEXT_HTML_VALUE)
public class RandomNumberController {

    // GET /rnd
    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String getRandom(Model model) {
        Random rand = new SecureRandom();
        int value = rand.nextInt(1, 11);
        boolean odd = (value % 2) > 0;
        model.addAttribute("rndNum", value);
        model.addAttribute("odd", odd);
        return "numbers";
    }

    // GET /rnd/list
    // special version of GET /rnd -- returns multiple random numbers
    // IRL, can do it like GET /rnd?start=1&end=10&count=10
    // default query would be GET /rnd?count=1
    // query string provides info to resource, but is NOT part of resource
    @GetMapping(path = "/list")
    public ModelAndView getRandomList( // note: returning ModelAndView
            @RequestParam(defaultValue = "1") int startNum,
            @RequestParam(defaultValue = "100") int endNum,
            @RequestParam(required = true) int count) {

        ModelAndView mav = new ModelAndView();

        // initial check
        if (startNum > endNum) {
            mav.setViewName("error");
            mav.addObject("message", "Start number %d is greater than end number %d".formatted(startNum, endNum));
            mav.setStatus(HttpStatusCode.valueOf(400));
            return mav;
        }

        Random rand = new SecureRandom();
        List<Integer> nums = new LinkedList<>();

        for (int i = 0; i < count; i++) {
            nums.add(rand.nextInt(startNum, endNum + 1));
        }

        mav.setViewName("numlist"); // set the html view to use as template
        mav.addObject("randNums", nums); // model inserted into view HERE in controller, instead of dynamic HTML
        mav.setStatus(HttpStatusCode.valueOf(200)); // ModelAndView allows you to set the status code that server will
                                                    // return to client

        return mav;
    }

    // GET /rnd/{count}
    @GetMapping(path = "{count}")
    public ModelAndView getRandom( // note: returning ModelAndView
            @PathVariable int count,
            @RequestParam MultiValueMap<String, String> queryParams) {

        int startNum = Integer.parseInt(queryParams.getFirst("startNum"));
        int endNum = Integer.parseInt(queryParams.getFirst("endNum"));

        // queryParams.get(abc); // this is for CHECKBOXES, returns a list of values
        // selection

        return getRandomList(startNum, endNum, count);
    }

    // <a href="/rnd/1?startNum=1&endNum=10">ABC</a>
    @GetMapping(path = "/picks")
    public ModelAndView getPicks(
            @RequestParam(defaultValue = "10") int pickCount) {

        ModelAndView mav = new ModelAndView();
        List<QuickPick> picks = new LinkedList<>();
        for (int i = 0; i < pickCount; i++) {
            picks.add(new QuickPick(-100, 100, i + 1));
        }

        mav.setViewName("quick_picks");
        mav.addObject("picks", picks);
        mav.setStatus(HttpStatusCode.valueOf(200));
        return mav;
    }
}
