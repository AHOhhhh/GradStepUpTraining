package fun.hercules.user.enterprise.web;

import fun.hercules.user.common.constants.Status;
import fun.hercules.user.common.errors.ErrorCode;
import fun.hercules.user.common.exceptions.BadRequestException;
import fun.hercules.user.contact.domain.Contact;
import fun.hercules.user.contact.service.ContactService;
import fun.hercules.user.enterprise.domain.Enterprise;
import fun.hercules.user.enterprise.domain.EnterpriseQualificationHistory;
import fun.hercules.user.enterprise.dto.PayMethodConfigDTO;
import fun.hercules.user.enterprise.service.EnterpriseService;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.service.UserService;
import fun.hercules.user.enterprise.domain.Enterprise;
import fun.hercules.user.enterprise.domain.EnterpriseQualificationHistory;
import fun.hercules.user.enterprise.service.EnterpriseService;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/enterprises", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(value = "enterprises", description = "Access to enterprise")
public class EnterpriseController {
    private final EnterpriseService enterpriseService;

    private final UserService userService;

    private final ContactService contactService;

    @Autowired
    public EnterpriseController(EnterpriseService service, UserService userService, ContactService contactService) {
        this.enterpriseService = service;
        this.userService = userService;
        this.contactService = contactService;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create enterprise")
    @ApiResponses(value = {
            @ApiResponse(code = 201, response = String.class,
                    message = "Create enterprise successfully and return enterpriseId")
    })
    public String createEnterprise(@RequestBody @Valid Enterprise enterprise) {
        return enterpriseService.createEnterprise(enterprise);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{enterpriseId}", consumes = MediaType
            .APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update enterprise qualification")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = String.class,
                    message = "resubmit enterprise qualification materials successfully and return enterpriseId")
    })
    public String updateEnterpriseQualification(@PathVariable("enterpriseId") String enterpriseId,
                                                @RequestBody @Valid Enterprise enterprise) {
        return enterpriseService.updateEnterpriseQualification(enterpriseId, enterprise);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ApiOperation(value = "Get latest enterprise info")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = String.class, message = "Get latest enterprise successfully"),
            @ApiResponse(code = 404, message = "No enterprise matches given enterpriseId")
    })
    public Enterprise getEnterprise(@PathVariable("id") String id) {
        return enterpriseService.getEnterprise(id);
    }


    @GetMapping(value = "/histories/{id}")
    @ApiOperation(value = "Get enterprise qualification histories by enterprise id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = List.class, message = "Get enterprise enterprise qualification histories successfully"),
            @ApiResponse(code = 404, message = "No enterprise matches given enterpriseId")
    })
    public List<EnterpriseQualificationHistory> getEnterpriseQualificationHistories(@PathVariable("id") String id) {
        return enterpriseService.getEnterpriseQualificationHistories(id);
    }


    @RequestMapping(path = "/{id}/users", method = RequestMethod.GET)
    @ApiOperation(value = "Get user by enterprise id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = String.class, message = "Get users successfully"),
            @ApiResponse(code = 404, message = "No user matches given id")
    })
    public Page<User> getUsers(@PathVariable("id") String enterpriseId,
                               @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return userService.getByEnterpriseId(enterpriseId, pageable);
    }

    @RequestMapping(path = "/{id}/contacts", method = RequestMethod.GET)
    @ApiOperation(value = "Get contacts by enterprise id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = String.class, message = "Get users successfully"),
            @ApiResponse(code = 404, message = "No user matches given id")
    })
    public List<Contact> getContacts(@PathVariable("id") String enterpriseId,
                                     @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return contactService.readContacts(enterpriseId, pageable);
    }

    @GetMapping()
    @ApiOperation(value = "Get enterprise list with users' info")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = String.class, message = "Get enterprise list successfully"),
    })
    public Page<Map> getEnterpriseWithUsers(@SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                            @RequestParam(value = "status", required = false) String status,
                                            @RequestParam(value = "validationStatus", required = false) String validationStatus,
                                            @RequestParam(value = "name", required = false) String name) {
        return enterpriseService.findAllByParams(name, status, validationStatus, pageable);
    }

    @GetMapping(params = "ids")
    @ApiOperation(value = "Get enterprises")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = String.class, message = "Get enterprises successfully"),
    })
    public Page<Enterprise> listEnterprise(@RequestParam(value = "ids") List<String> ids,
                                           @PageableDefault(size = Integer.MAX_VALUE,
                                                   sort = "updatedAt",
                                                   direction = Sort.Direction.DESC) Pageable pageable) {
        return enterpriseService.listByIds(ids, pageable);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/admin")
    @ApiOperation(value = "Get enterprise admin by enterprise id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = String.class, message = "Get enterprise admin successfully"),
            @ApiResponse(code = 404, message = "No enterprise matches by given id")
    })
    public User getEnterpriseAdmin(@PathVariable("id") String enterpriseId) {
        return enterpriseService.getEnterpriseAdmin(enterpriseId);
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, path = "/{id}/approve")
    @ApiOperation(value = "Update enterprise info")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Update enterprise info successfully"),
            @ApiResponse(code = 404, message = "No enterprise matches by given id")
    })
    public Enterprise updateEnterpriseQualificationStatus(@PathVariable("id") String enterpriseId,
                                                          @RequestBody Map<String, String> enterpriseInfo) {
        return enterpriseService.updateEnterpriseQualificationStatus(enterpriseId, enterpriseInfo);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, path = "/{id}/status")
    @ApiOperation(value = "Update enterprise status")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Update enterprise status successfully"),
            @ApiResponse(code = 404, message = "No enterprise matches by given id")
    })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void enableOrDisableEnterprise(@PathVariable("id") String enterpriseId, @RequestBody Map<String, String> statusMap) {
        String statusKey = "status";
        if (!statusMap.containsKey(statusKey)) {
            throw new BadRequestException(ErrorCode.INVALID_PARAMETER);
        }
        Status statusEnum = Status.get(statusMap.get(statusKey).toUpperCase());
        enterpriseService.updateEnterpriseStatus(statusEnum, enterpriseId);
    }

    @GetMapping("/{id}/pay-methods")
    @ApiOperation(value = "Update enterprise pay methods")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Update enterprise pay methods successfully"),
            @ApiResponse(code = 404, message = "No enterprise matches by given id")
    })
    @ResponseStatus(value = HttpStatus.OK)
    public List<PayMethodConfigDTO> getEnterprisePayMethods(@PathVariable("id") String enterpriseId) {
        return enterpriseService.getEnterprisePayMethods(enterpriseId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, path = "/{id}/pay-methods")
    @ApiOperation(value = "Update enterprise pay methods")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Update enterprise pay methods successfully"),
            @ApiResponse(code = 403, message = "Not allowed to update enterprise pay methods with invalid rule"),
            @ApiResponse(code = 404, message = "No enterprise matches by given id")
    })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateEnterprisePayMethods(@PathVariable("id") String enterpriseId,
                                           @RequestBody Map<String, List<String>> configurations) {
        enterpriseService.updateEnterprisePayMethods(enterpriseId, configurations);
    }
}
