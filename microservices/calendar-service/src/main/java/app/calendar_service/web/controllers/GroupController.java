package app.calendar_service.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import app.calendar_service.application.security.services.JwtService;
import app.calendar_service.application.services.GroupServiceImpl;
import app.calendar_service.application.services.MailServiceImpl;
import app.calendar_service.application.usecases.GroupService;
import app.calendar_service.application.usecases.MailService;
import app.calendar_service.web.dtos.*;
import app.calendar_service.web.security.config.JwtEnv;
import app.calendar_service.web.security.dao.AuthenticationResponse;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
@CrossOrigin(("http://localhost:5173"))
public class GroupController {

    private final GroupServiceImpl groupService;
    private final MailServiceImpl mailService;

    private ResponseEntity<?> responseWithErrors(BindingResult bindingResult){
        Map<String,String> errors =  new HashMap<>();

        bindingResult.getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(), "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<?> show(@PathVariable Long groupId){
        Optional<Pair<GroupDto, UserPermissionsDto>> groupOptional = groupService.findOneWithDetails(groupId);

        if(groupOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> jsonResponse = Map.of(
                "grupo", groupOptional.orElseThrow().a,
                "can", groupOptional.orElseThrow().b
        );

        return ResponseEntity.ok()
                .body(jsonResponse);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody GroupDto newGroupDto, BindingResult bindingResult, @AuthenticationPrincipal String username){

        if(bindingResult.hasErrors()){
            return this.responseWithErrors(bindingResult);
        }

        Optional<GroupDto> groupOptional = groupService.save(username, newGroupDto);

        if(groupOptional.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(groupOptional.orElseThrow());
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<?> update(@PathVariable Long groupId, @Valid @RequestBody GroupDto groupDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return this.responseWithErrors(bindingResult);
        }

        Optional<GroupDto> groupOptional = groupService.update(groupId, groupDto);

        if(groupOptional.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok()
                .body(groupOptional.orElseThrow());
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<?> delete(@PathVariable Long groupId){
        Optional<GroupDto> groupOptional = groupService.delete(groupId);

        if (groupOptional.isPresent()){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{groupId}/add-member")
    public ResponseEntity<?> addMember(@PathVariable Long groupId, @RequestParam(name = "newMember") Long memberId){
        Optional<UserDto> userOptional = groupService.addMember(groupId, memberId);

        if(userOptional.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(userOptional.orElseThrow());
    }

    @PostMapping("/{groupId}/add-many-members")
    public ResponseEntity<?> addManyMembers(@PathVariable Long groupId, @RequestParam List<Long> memberIds){
        System.out.println(memberIds);
        List<UserDto> userDtos = groupService.addManyMembers(groupId, memberIds);

        if(userDtos.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok()
                .body(userDtos);
    }

    @PostMapping("/{groupId}/kick-member")
    public ResponseEntity<?> kickMember(@PathVariable Long groupId, @RequestParam(name = "memberId") Long memberId){
        Optional<MembershipDto> optionalMembership = groupService.kickMember(groupId, memberId);

        if(optionalMembership.isPresent()){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{groupId}/add-role/{memberId}")
    public ResponseEntity<?> addRole(@PathVariable Long groupId, @PathVariable Long memberId, @RequestParam("roleId") Long roleId){
        List<MemberRoleDto> memberRoles = groupService.addRole(groupId, memberId, roleId);

        if(memberRoles.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(memberRoles);
    }

    @PostMapping("/{groupId}/delete-role/{memberId}")
    public ResponseEntity<?> deleteRole(@PathVariable Long groupId, @PathVariable Long memberId, @RequestParam("roleId") Long roleId){
        List<MemberRoleDto> memberRoles = groupService.deleteRole(groupId, memberId, roleId);

        if(!memberRoles.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{groupId}/manage-members")
    public ResponseEntity<?> manageMembers(@PathVariable Long groupId){

        Map<String, Object> json =  groupService.manageUsers(groupId);

        if (! (Boolean) json.get("success")){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(json);
    }
}
