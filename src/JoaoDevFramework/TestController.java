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

    @GetMapping(path = "/{id}")
    public Response getTestId(@PathVariable(name = "id") Float id){
        System.out.println(id);
        return Response.status(HttpStatus.OK).body("Ola");
    }

    @GetMapping(path = "/{id}/{productId}")
    public Response getTestIdProductId(@PathVariable(name = "id") Float id, @PathVariable(name = "productId") String idP){
        System.out.println(id);
        System.out.println(idP);

        return Response.status(HttpStatus.OK).body("Ola");
    }

    @GetMapping(path = "/product/{id}")
    public Response getTestSubpath(){

        return Response.status(HttpStatus.OK).body("Ola");
    }

    @PostMapping
    public Response postTest(@Body User user, @Request HttpRequest httpRequest){

        user.setName("Nome Alterado no Server");

        return Response.status(HttpStatus.CREATED).body(user);

    }

}
