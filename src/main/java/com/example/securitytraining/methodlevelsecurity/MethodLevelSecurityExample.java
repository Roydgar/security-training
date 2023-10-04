package com.example.securitytraining.methodlevelsecurity;

import lombok.Data;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MethodLevelSecurityExample {

    // @PreAuthorize is preferable since it allows to use Spring's SPeL
    @PreAuthorize("hasRole('VIEW_ACCOUNT')")
//    @PreAuthorize("hasAnyRole('VIEW_ACCOUNT', 'ADMIN')")
//    @PreAuthorize("hasAuthority('VIEW_ACCOUNT')")
//    @PreAuthorize("hasAnyAuthority('VIEW_ACCOUNT')")
//    @PreAuthorize("# username == authentication.principal.username")
//    @Secured("ROLE_VIEW_ACCOUNT")
//    @RolesAllowed("ROLE_VIEW_ACCOUNT")
    public void preAuthorize(String username) {
    }

    @PostAuthorize("returnObject.propertyName == authentication.principal.username")
    public Object postAuthorize(String username) {
        return null;
    }

    @PreFilter("!filterObject.property.contains('test')")
    public void preFilter(List<TestModel> values) {
    }

    @PostFilter("!filterObject.property.contains('admin')")
    public List<TestModel> postFilter() {
        return null;
    }

    @Data
    private static class TestModel {
        private String property;
    }
}
