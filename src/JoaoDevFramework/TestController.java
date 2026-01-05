package JoaoDevFramework;

import JoaoDevFramework.annotations.*;
import JoaoDevFramework.entities.HttpStatus;
import JoaoDevFramework.entities.Response;
import JoaoDevFramework.entities.User;
import JoaoDevFramework.entities.HttpRequest;

//This is not Spring boot
@Controller(path = "/teste")
public class TestController {

    @GetMapping
    public Response getTest(){

        return Response.status(HttpStatus.OK).body("Ola");
    }

    @GetMapping(path = "/caminho")
    public void getTestParams(){
        System.out.println("GetResponse caminho adicional");
    }

    @PostMapping
    public Response postTest(@Body User user, @Request HttpRequest httpRequest){

        return Response.status(HttpStatus.CREATED).body(user);

    }

}
