package JoaoDevFramework;

import JoaoDevFramework.annotations.Controller;
import JoaoDevFramework.annotations.GetMapping;
import JoaoDevFramework.entities.HttpStatus;
import JoaoDevFramework.entities.Response;

@Controller(path = "/user")
public class Controller2 {

    @GetMapping()
    public Response testeController(){
        return Response.status(HttpStatus.INTERNAL_SERVER_ERROR).body("OLALALAL");
    }

}
