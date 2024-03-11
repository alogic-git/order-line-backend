package com.orderline.site.service;

import com.orderline.basic.exception.NotFoundException;
import com.orderline.basic.exception.UnauthorizedException;
import com.orderline.order.model.dto.OrderDto;
import com.orderline.order.model.entity.Order;
import com.orderline.order.model.entity.OrderHistory;
import com.orderline.order.model.entity.OrderMaterial;
import com.orderline.order.repository.OrderHistoryRepository;
import com.orderline.order.repository.OrderMaterialRepository;
import com.orderline.order.repository.OrderRepository;
import com.orderline.site.model.dto.SiteDto;
import com.orderline.site.model.entity.ConstructionCompany;
import com.orderline.site.model.entity.Site;
import com.orderline.site.repository.ConstructionCompanyRepository;
import com.orderline.site.repository.SiteRepository;
import com.orderline.user.model.entity.User;
import com.orderline.user.model.entity.UserSite;
import com.orderline.user.repository.UserRepository;
import com.orderline.user.repository.UserSiteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.time.ZonedDateTime;
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
    public SiteDto.ResponseSiteDto selectSite(Long userId, Long siteId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
        Site site = siteRepository.findById(siteId).orElseThrow(() -> new NotFoundException("해당 현장을 찾을 수 없습니다."));

        UserSite usersite = userSiteRepository.findByUserAndSite(user, site)
                .orElseThrow(() -> new NotFoundException("사용자의 현장을 찾을 수 없습니다."));

        user.setSite(site);
        userRepository.save(user);

        return SiteDto.ResponseSiteDto.toDto(site);
    }

}
