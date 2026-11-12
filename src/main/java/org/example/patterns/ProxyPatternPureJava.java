package org.example.patterns;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

enum Role {
    ADMIN,
    USER
}

@RequiredArgsConstructor
@Getter
class User {
    private final Long id;
    private final String name;
    private final Set<Role> roles;
}

interface FileService { byte[] download( String fileId); }

class RemoteFileService implements FileService{

    @Override
    public byte[] download(String fileId) {
        return new byte[0];
    }
}

class SecuredCashingFileServiceProxy implements FileService{

    private RemoteFileService remoteFileService;
    private final User user;
    private final Role requiredRole;
    private List<String> cashes = new ArrayList<>();

    SecuredCashingFileServiceProxy(User user, Role requiredRole) {
        this.remoteFileService = new RemoteFileService();
        this.user = user;
        this.requiredRole = requiredRole;
    }

    @Override
    public byte[] download(String fileId) {
        if(!user.getRoles().contains(requiredRole)){
            throw new RuntimeException("User has not permission for download file");
        }
        this.cashes.add(fileId);
        return remoteFileService.download(fileId);
    }
}

public class ProxyPatternPureJava {
    public static void main(String[] args) {
        SecuredCashingFileServiceProxy securedCashingFileServiceProxy =
                new SecuredCashingFileServiceProxy(new User(1L, "Israyil", Set.of(Role.USER)), Role.ADMIN);

        securedCashingFileServiceProxy.download("12");

    }
}
