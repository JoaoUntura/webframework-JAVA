package JoaoDevFramework;

import JoaoDevFramework.annotations.*;
import entities.HttpRequest;

//This is not Spring boot
@Controller(path = "/teste")
public class TestController {

    @GetMapping
    public void getTest(){
        System.out.println("GetResponse normal");
    }

    @GetMapping(path = "/caminho")
    public void getTestParams(){
        System.out.println("GetResponse caminho adicional");
    }

    @PostMapping
    public String postTest(@Body String body, @Request HttpRequest httpRequest){

        return body;

    }

}
