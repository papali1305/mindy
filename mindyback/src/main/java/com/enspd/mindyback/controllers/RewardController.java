package com.enspd.mindyback.controllers;

import com.enspd.mindyback.controllers.api.RewardApi;
import com.enspd.mindyback.dto.RewardDto;
import com.enspd.mindyback.dto.UserDto;
import com.enspd.mindyback.services.RewardService;
import com.enspd.mindyback.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class RewardController implements RewardApi {

    @Autowired
    private UserService userService;

    @Autowired
    private RewardService rewardService;

    @Override
    public RewardDto getRewards(String jwt) {
        UserDto user = userService.findUserByJwt(jwt);
        return rewardService.findRewardByUserId(user.id());
    }
}
