package com.ptglue.basic.interceptor;

import com.ptglue.basic.model.dto.UserContext;
import com.ptglue.common.user.enums.UserRoleEnum;
import com.ptglue.basic.service.PermissionService;
import com.ptglue.branch.model.dto.BranchUserDto;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class PermissionInterceptor implements HandlerInterceptor {

  private final PermissionService permissionService;

  public PermissionInterceptor(PermissionService permissionService) {
    this.permissionService = permissionService;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    // 고객의 계정 정보를 얻어온다 (userId, branchId, role)

    UserContext userContext = extractUserContextFromRequest(request);

    // 고객의 권한 정보를 DB 에서 조회한다
    List<BranchUserDto.ResponseBranchUserPermissionDto> userPermissions = permissionService.getUserPermissions(userContext);

    // 요청에 해당하는 URL 과 메소드를 추출해서 권한 검사를 한다.
    if (!hasPermission(userPermissions, request.getRequestURI(), request.getMethod())) {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      return false;
    }

    return true;
  }

  private boolean hasPermission(List<BranchUserDto.ResponseBranchUserPermissionDto> userPermissions, String url, String method) {
    // DB 에서 조회한 사용자의 권한 정보와 요청 URL, 메소드를 비교하여 권한 검사를 수행한다
    String requestPermissionType = method.equals("GET") ? "VIEW" : "EDIT";
    // url 패턴 준수 필요
    String requestFunctionType = url.split("/")[2];
    return userPermissions.stream()
            .filter(permission -> permission.getFunctionType().toString().equalsIgnoreCase(requestFunctionType))
            .anyMatch(permission -> isPermitted(permission.getPermissionType().toString(), requestPermissionType));
  }

  private boolean isPermitted(String userPermissionType, String requestPermissionType) {
    try {
      if (userPermissionType.equalsIgnoreCase("EDIT")) {
        return true;
      }
      return requestPermissionType.equalsIgnoreCase("VIEW");
    }
    catch (NullPointerException e){
        throw new AuthorizationServiceException("접속 정보가 정확하지 않습니다.[3] 새로고침을 해주세요.");
    }
  }

  private UserContext extractUserContextFromRequest(HttpServletRequest request) {
    Long userId = getAttributeAsLong(request, "userId");
    Long branchId = getAttributeAsLong(request, "branchId");
    UserRoleEnum role = getAttributeAsRole(request);
    return UserContext.builder()
            .userId(userId)
            .branchId(branchId)
            .role(role)
            .build();
  }

  private Long getAttributeAsLong(HttpServletRequest request, String attributeName){
    Object attributeValue = request.getAttribute(attributeName);
    try {
      return Long.valueOf(String.valueOf(attributeValue));
    }
    catch (NumberFormatException e) {
      throw new AuthorizationServiceException("접속 정보가 정확하지 않습니다.[1] 새로고침을 해주세요.");
    }
  }

  private UserRoleEnum getAttributeAsRole(HttpServletRequest request){
    Object attributeValue = request.getAttribute("role");
    try {
      return UserRoleEnum.valueOf(String.valueOf(attributeValue));
    }
    catch (IllegalArgumentException e) {
      throw new AuthorizationServiceException("접속 정보가 정확하지 않습니다.[2] 새로고침을 해주세요.");
    }
  }

}
