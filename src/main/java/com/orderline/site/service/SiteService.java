package com.orderline.site.service;

import com.orderline.basic.exception.NotFoundException;
import com.orderline.basic.service.AuthService;
import com.orderline.site.model.dto.SiteDto;
import com.orderline.site.model.entity.ConstructionCompany;
import com.orderline.site.model.entity.Site;
import com.orderline.site.repository.ConstructionCompanyRepository;
import com.orderline.site.repository.SiteRepository;
import com.orderline.user.model.dto.UserDto;
import com.orderline.user.model.entity.User;
import com.orderline.user.model.entity.UserSite;
import com.orderline.user.repository.UserRepository;
import com.orderline.user.repository.UserSiteRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SiteService {
    @Resource(name = "userRepository")
    UserRepository userRepository;

    @Resource(name = "siteRepository")
    SiteRepository siteRepository;
    @Resource(name = "userSiteRepository")
    UserSiteRepository userSiteRepository;

    @Resource(name = "constructionCompanyRepository")
    ConstructionCompanyRepository constructionCompanyRepository;

    @Resource(name = "authService")
    AuthService authService;

    @Transactional
    public SiteDto.ResponseSiteDto createSite(Long userId, SiteDto.RequestCreateSiteDto requestCreateSiteDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
        ConstructionCompany constructionCompany = constructionCompanyRepository.findById(requestCreateSiteDto.getCompanyId())
                .orElseThrow(() -> new NotFoundException("회사를 찾을 수 없습니다."));

        Site site = requestCreateSiteDto.toEntity(constructionCompany);

        UserSite userSite = UserSite.builder()
                .user(user)
                .site(site)
                .build();

        userSiteRepository.save(userSite);
        siteRepository.save(site);

        return SiteDto.ResponseSiteDto.toDto(site);
    }

    public SiteDto.ResponseSiteListDto getSiteList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        List<UserSite> userSites = userSiteRepository.findByUser(user);

        List<Site> sites = userSites.stream()
                .map(UserSite::getSite)
                .collect(Collectors.toList());

        return SiteDto.ResponseSiteListDto.toDto(sites);
    }

    @Transactional
    public UserDto.UserInfoDto selectSite(Long userId, Long siteId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
        Site site = siteRepository.findById(siteId).orElseThrow(() -> new NotFoundException("해당 현장을 찾을 수 없습니다."));

        userSiteRepository.findByUserAndSite(user, site).orElseThrow(() -> new NotFoundException("해당 현장에 대한 권한이 없습니다."));

        user.updateSite(site);

        UserDto.UserInfoDto userInfoDto = UserDto.UserInfoDto.toDto(user);
        authService.createToken(userInfoDto);

        return userInfoDto;
    }

}
