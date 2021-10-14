package egg.proyecto.comparaTecno.controlador;

import javax.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class IndexControlador {

    @GetMapping("/")
    public String index() {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
//        auth.getAuthorities().forEach(e->System.out.println("."+e));
//        System.out.println("--"+auth.getCredentials());
        
        return "index.html";

    }

    @GetMapping("/login")
    public String login(HttpSession session, Authentication usuario, ModelMap modelo, @RequestParam(required = false) String error) {

    	if (error != null) {
			modelo.put("error", "E-mail y/o contraseña incorrectos.");
		}
    	return "login.html";
    	
    }

    @GetMapping("/loginsuccess")
    public String loginresolver() {

        return "redirect:/";
    }
    
    @GetMapping("/logout")
    public String logOutResolver() {

        return "redirect:/";
    }
}
