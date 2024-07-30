package com.enspd.mindyback.controllers.api;

import com.enspd.mindyback.dto.RewardDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.enspd.mindyback.config.Utils.REWARD_ENDPOINT;

public interface RewardApi {

    @GetMapping(REWARD_ENDPOINT + "/rewards")
    public RewardDto getRewards(@RequestHeader(name = "Authorization") String jwt);
}
