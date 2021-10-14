package egg.proyecto.comparaTecno.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class VistasControlador {

    @GetMapping("/faq")
    public String faq() {  
        return "faq.html";
        
    }
    
    @GetMapping("/info")
    public String info() {  
        return "faq.html";
        
    }

	
}
