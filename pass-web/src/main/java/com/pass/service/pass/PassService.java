package com.pass.service.pass;

import com.pass.repository.pass.PassEntity;
import com.pass.repository.pass.PassRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassService {

    private final PassRepository passRepository;

    public PassService(PassRepository passRepository) {
        this.passRepository = passRepository;
    }

    public List<Pass> getPasses(final String userId) {

        // userId를 조건으로 pass 조회, 이때 packageSeq에 맞는 package 정보도 필요
        final List<PassEntity> passEntities = passRepository.findByUserId(userId);
        return PassModelMapper.INSTANCE.map(passEntities);
    }
}
