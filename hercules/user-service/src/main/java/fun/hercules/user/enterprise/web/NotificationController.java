package fun.hercules.user.enterprise.web;

import fun.hercules.user.enterprise.domain.Notification;
import fun.hercules.user.enterprise.domain.NotificationType;
import fun.hercules.user.enterprise.repository.NotificationRepository;
import fun.hercules.user.user.security.CurrentUserContext;
import fun.hercules.user.enterprise.domain.Notification;
import fun.hercules.user.enterprise.domain.NotificationType;
import fun.hercules.user.enterprise.repository.NotificationRepository;
import fun.hercules.user.user.security.CurrentUserContext;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController("/notifications")
public class NotificationController {

    private final NotificationRepository repository;

    private final CurrentUserContext userContext;

    @Autowired
    public NotificationController(NotificationRepository repository, CurrentUserContext userContext) {
        this.repository = repository;
        this.userContext = userContext;
    }

    @GetMapping
    @ApiOperation(value = "get notifications")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "get enterprise notifications successfully"),
            @ApiResponse(code = 404, message = "No notifications matches by given enterpriseId")
    })
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('EnterpriseUser')")
    public Page<Notification> getEnterpriseNotifications(@SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                         @RequestParam(name = "notificationType") NotificationType notificationType) {
        return repository.findByEnterpriseIdAndNotificationType(userContext.getUser().getEnterpriseId(), notificationType, pageable);
    }

    @PostMapping
    @ApiOperation(value = "create notification")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "create successfully"),
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('PlatformService')")
    public Notification createNotification(@RequestBody Notification notification) {
        return repository.save(notification);
    }
}
