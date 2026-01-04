package JoaoDevFramework;

import JoaoDevFramework.annotations.Body;
import JoaoDevFramework.annotations.Controller;
import JoaoDevFramework.annotations.GetMapping;
import JoaoDevFramework.annotations.PostMapping;

@Controller(path = "/teste")
public class TestController {

    @GetMapping
    public void getTest(){
        System.out.println("Achouuu");
    }

    @PostMapping
    public void postTest(@Body String body){


        System.out.println("Achouuu Post" + body);
    }

}
