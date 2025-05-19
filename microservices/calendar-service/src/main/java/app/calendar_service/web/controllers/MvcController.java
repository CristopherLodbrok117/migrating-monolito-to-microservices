package app.calendar_service.web.controllers;

import lombok.RequiredArgsConstructor;
import app.calendar_service.application.usecases.GroupService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@CrossOrigin(("http://localhost:5173"))
public class MvcController {

    private final GroupService groupService;

    @Value("${sinaloa.frontend.url}")
    private String frontendUrl;

    /**
     * Ejemplo de url compatible: http://localhost:8080/api/groups/2/accept-invitation?membershipId=26&invitationToken=0gkYyieIwHC3QDqWRnm3Cwd3N7nQWw4byksLA8v4bsbW6Ci5vw
     *
     * @param membershipId
     * @param invitationToken
     * @return
     */
    @GetMapping("/api/groups/{groupId}/accept-invitation")
    public String manageMembers(@RequestParam("membershipId") Long membershipId,
                                @RequestParam("invitationToken") String invitationToken,
                                Model model){
        Map<String,Object> membershipData = groupService.acceptMembership(membershipId, invitationToken)
                .orElseThrow();

        model.addAttribute("user", membershipData.get("user"));
        model.addAttribute("group", membershipData.get("group"));
        model.addAttribute("frontendUrl", frontendUrl);

        return "invitation-accepted";
    }
}
