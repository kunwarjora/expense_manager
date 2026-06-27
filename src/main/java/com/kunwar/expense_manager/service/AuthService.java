package com.kunwar.expense_manager.service;

import jakarta.annotation.PostConstruct;
import org.checkerframework.checker.units.qual.K;
import org.openapitools.sdk.KindeClientSDK;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private KindeClientSDK kindeClientSDK;

    @PostConstruct
    public void updateKindeClientSDK(){

        this.kindeClientSDK=new KindeClientSDK(
        );
    }
}
