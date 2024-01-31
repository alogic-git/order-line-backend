package com.ptglue.klass.service;

import com.ptglue.basic.exception.DuplicateException;
import com.ptglue.basic.exception.NotFoundException;
import com.ptglue.basic.model.dto.CommonDto;
import com.ptglue.branch.model.dto.BranchTuteeDto;
import com.ptglue.branch.model.dto.BranchUserDto;
import com.ptglue.branch.model.entity.Branch;
import com.ptglue.branch.model.entity.BranchUserOngoingListGroupedKlass;
import com.ptglue.branch.model.entity.BranchUserRole;
import com.ptglue.branch.repository.BranchRepository;
import com.ptglue.branch.repository.BranchUserOngoingListGroupedKlassRepository;
import com.ptglue.branch.repository.BranchUserRoleRepository;
import com.ptglue.common.user.enums.UserRoleEnum;
import com.ptglue.klass.repository.KlassRepository;
import com.ptglue.product.model.dto.ProductDto;
import com.ptglue.product.model.entity.Product;
import com.ptglue.product.model.entity.ProductKlass;
import com.ptglue.product.repository.ProductKlassRepository;
import com.ptglue.product.repository.ProductRepository;
import com.ptglue.klass.enums.KlassColorTypeEnum;
import com.ptglue.klass.model.dto.KlassDto;
import com.ptglue.klass.model.entity.Klass;
import com.ptglue.schedule.model.dto.RepeatScheduleDto;
import com.ptglue.schedule.model.dto.ScheduleDto;
import com.ptglue.schedule.model.entity.RepeatSchedule;
import com.ptglue.schedule.model.entity.Schedule;
import com.ptglue.schedule.repository.RepeatScheduleRepository;
import com.ptglue.schedule.repository.ScheduleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class KlassService {

    @Resource(name = "klassRepository")
    private KlassRepository klassRepository;

    @Resource(name = "branchRepository")
    private BranchRepository branchRepository;

    @Resource(name = "branchUserRoleRepository")
    BranchUserRoleRepository branchUserRoleRepository;

    @Resource(name = "productRepository")
    ProductRepository productRepository;

    @Resource(name = "productKlassRepository")
    ProductKlassRepository productKlassRepository;

    @Resource(name = "branchUserOngoingListGroupedKlassRepository")
    BranchUserOngoingListGroupedKlassRepository branchUserOngoingListGroupedKlassRepository;

    @Resource(name = "repeatScheduleRepository")
    RepeatScheduleRepository repeatScheduleRepository;

    @Resource(name = "scheduleRepository")
    ScheduleRepository scheduleRepository;

    public KlassDto.ResponseKlassDto get(Long klassId) {
        Klass klass = klassRepository.findById(klassId)
                .orElseThrow(() -> new NotFoundException("해당 클래스가 존재하지 않습니다."));
        return KlassDto.ResponseKlassDto.toDto(klass);
    }

    public KlassDto.ResponseKlassDto delete(Long klassId) {
        Klass klass = klassRepository.findById(klassId)
                .orElseThrow(() -> new NotFoundException("해당 클래스가 존재하지 않습니다."));
        klass.delete();
        klassRepository.save(klass);
        return KlassDto.ResponseKlassDto.toDto(klass);
    }

    public List<ProductDto.ResponseProductDto> getKlassProduct(Long klassId) {

        klassRepository.findById(klassId).orElseThrow(() -> new NotFoundException("해당 클래스가 존재하지 않습니다."));

        List<ProductKlass> productKlassList = productKlassRepository.findByKlassId(klassId);
        return productKlassList.stream().map(productKlass -> ProductDto.ResponseProductDto.toDto(productKlass.getProduct())).collect(Collectors.toList());
    }

    public Page<KlassDto.ResponseKlassDto> getList(Long branchId, String searchWord, Pageable pageable) {

        branchRepository.findById(branchId).orElseThrow(() -> new NotFoundException("지점이 존재하지 않습니다."));

        Page<Klass> klasses = klassRepository.findKlassNativeQuery(branchId, searchWord, pageable);
        return klasses.map(KlassDto.ResponseKlassDto::toDto);
    }

    @Transactional
    public KlassDto.ResponseKlassDto createFreeKlass(Long branchId, KlassDto.RequestFreeKlassDto requestKlassDto) {

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new NotFoundException("지점이 존재하지 않습니다."));

        BranchUserRole mainTutor = branchUserRoleRepository.findById(requestKlassDto.getMainTutorId()).orElseThrow(() -> new NotFoundException("메인 강사가 존재하지 않습니다."));

        BranchUserRole subTutor = null;
        if(requestKlassDto.getSubTutorId() != null){
            subTutor = branchUserRoleRepository.findById(requestKlassDto.getSubTutorId()).orElseThrow(() -> new NotFoundException("서브 강사가 존재하지 않습니다."));
        }

        Klass klass = requestKlassDto.toEntity(branch, mainTutor, subTutor);
        klassRepository.save(klass);
        return KlassDto.ResponseKlassDto.toDto(klass);
    }

    @Transactional
    public KlassDto.ResponseKlassDto createLimitedKlass(Long branchId, KlassDto.RequestLimitedKlassDto requestKlassDto) {

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new NotFoundException("지점이 존재하지 않습니다."));

        BranchUserRole mainTutor = branchUserRoleRepository.findById(requestKlassDto.getMainTutorId()).orElseThrow(() -> new NotFoundException("메인 강사가 존재하지 않습니다."));

        BranchUserRole subTutor = null;
        if(requestKlassDto.getSubTutorId() != null){
            subTutor = branchUserRoleRepository.findById(requestKlassDto.getSubTutorId()).orElseThrow(() -> new NotFoundException("서브 강사가 존재하지 않습니다."));
        }

        Klass klass = requestKlassDto.toEntity(branch, mainTutor, subTutor);
        klassRepository.save(klass);
        return KlassDto.ResponseKlassDto.toDto(klass);
    }

    public List<CommonDto.ColorCodeDto> getKlassColor(){

        List<CommonDto.ColorCodeDto> colorCodeDtoList = new ArrayList<>();
        for(KlassColorTypeEnum klassColorEnum : KlassColorTypeEnum.values()){
            colorCodeDtoList.add(
                    CommonDto.ColorCodeDto.builder()
                            .colorCode(klassColorEnum)
                            .colorName(klassColorEnum.getText())
                            .mainColor(klassColorEnum.getMainColor())
                            .textColor(klassColorEnum.getTextColor())
                            .build()
            );
        }
        return colorCodeDtoList;

    }

    public Page<BranchUserDto.ResponseBranchTutorDto> getTutorList(Long branchId, Pageable pageable){

        branchRepository.findById(branchId).orElseThrow(() -> new NotFoundException("지점이 존재하지 않습니다."));

        Page<BranchUserRole> branchUserRolePage = branchUserRoleRepository.findByBranchIdAndRoleTypeAndArchiveYn(branchId, UserRoleEnum.TUTOR, false, pageable);
        return branchUserRolePage.map(BranchUserDto.ResponseBranchTutorDto::toDto);
    }

    public Page<ProductDto.ResponseProductDto> getProductList(Long branchId, Pageable pageable){

        branchRepository.findById(branchId).orElseThrow(() -> new NotFoundException("지점이 존재하지 않습니다."));

        Page<Product> products = productRepository.findByBranchIdAndArchiveYn(branchId, false, pageable);
        return products.map(ProductDto.ResponseProductDto::toDto);
    }

    @Transactional
    public List<ProductDto.ResponseProductDto> createKlassProduct(Long klassId, List<Long> requestProductIdList){

        Klass klass = klassRepository.findById(klassId)
                .orElseThrow(() -> new NotFoundException("해당 클래스가 존재하지 않습니다."));

        List<Product> productList = productRepository.findByIdIn(requestProductIdList);
        List<ProductKlass> productKlassList = new ArrayList<>();
        for(Product product : productList){
            if (!productKlassRepository.findByProductIdAndKlassId(product.getId(), klassId).isEmpty()){
                throw new DuplicateException("이미 등록된 수강권입니다.");
            }
            productKlassList.add(ProductKlass.builder()
                    .branch(product.getBranch())
                    .product(product)
                    .klass(klass)
                    .build());
        }
        productKlassRepository.saveAll(productKlassList);
        return productKlassList.stream().map(productKlass -> ProductDto.ResponseProductDto.toDto(productKlass.getProduct())).collect(Collectors.toList());
    }

    @Transactional
    public KlassDto.ResponseKlassDto archive(Long klassId) {

        Klass klass = klassRepository.findById(klassId)
                .orElseThrow(() -> new NotFoundException("해당 클래스가 존재하지 않습니다."));

        klass.archiveKlass();
        klassRepository.save(klass);
        return KlassDto.ResponseKlassDto.toDto(klass);
    }

    @Transactional
    public KlassDto.ResponseKlassDto updateFreeKlass(Long klassId, KlassDto.RequestFreeKlassDto requestKlassDto) {

        Klass klass = klassRepository.findById(klassId)
                .orElseThrow(() -> new NotFoundException("해당 클래스가 존재하지 않습니다."));

        BranchUserRole mainTutor = branchUserRoleRepository.findById(requestKlassDto.getMainTutorId()).orElseThrow(() -> new NotFoundException("메인 강사가 존재하지 않습니다."));

        BranchUserRole subTutor = null;
        if(requestKlassDto.getSubTutorId() != null){
            subTutor = branchUserRoleRepository.findById(requestKlassDto.getSubTutorId()).orElseThrow(() -> new NotFoundException("서브 강사가 존재하지 않습니다."));
        }

        klass.updateFreeKlass(requestKlassDto, mainTutor, subTutor);
        klassRepository.save(klass);
        return KlassDto.ResponseKlassDto.toDto(klass);
    }

    public KlassDto.ResponseKlassDto updateFreeKlassSetting(Long klassId, KlassDto.RequestFreeKlassSettingDto requestKlassDto) {

            Klass klass = klassRepository.findById(klassId)
                    .orElseThrow(() -> new NotFoundException("해당 클래스가 존재하지 않습니다."));

            klass.updateFreeKlassSetting(requestKlassDto);
            klassRepository.save(klass);
            return KlassDto.ResponseKlassDto.toDto(klass);
    }

    @Transactional
    public KlassDto.ResponseKlassDto updateLimitedKlass(Long klassId, KlassDto.RequestLimitedKlassDto requestKlassDto) {

        Klass klass = klassRepository.findById(klassId)
                .orElseThrow(() -> new NotFoundException("해당 클래스가 존재하지 않습니다."));

        BranchUserRole mainTutor = branchUserRoleRepository.findById(requestKlassDto.getMainTutorId()).orElseThrow(() -> new NotFoundException("메인 강사가 존재하지 않습니다."));

        BranchUserRole subTutor = null;
        if(requestKlassDto.getSubTutorId() != null){
            subTutor = branchUserRoleRepository.findById(requestKlassDto.getSubTutorId()).orElseThrow(() -> new NotFoundException("서브 강사가 존재하지 않습니다."));
        }

        klass.updateLimitedKlass(requestKlassDto, mainTutor, subTutor);
        klassRepository.save(klass);
        return KlassDto.ResponseKlassDto.toDto(klass);
    }

    public KlassDto.ResponseKlassDto updateLimitedKlassSetting(Long klassId, KlassDto.RequestLimitedKlassSettingDto requestKlassDto) {

        Klass klass = klassRepository.findById(klassId)
                .orElseThrow(() -> new NotFoundException("해당 클래스가 존재하지 않습니다."));

        klass.updateLimitedKlassSetting(requestKlassDto);
        klassRepository.save(klass);
        return KlassDto.ResponseKlassDto.toDto(klass);
    }

    public Page<BranchTuteeDto.ResponseBranchTuteeDto> getKlassTutee(Long klassId, Pageable pageable) {

        klassRepository.findById(klassId).orElseThrow(() -> new NotFoundException("해당 클래스가 존재하지 않습니다."));

        Page<BranchUserOngoingListGroupedKlass> branchUserOngoingListGroupedKlassPage = branchUserOngoingListGroupedKlassRepository.findByKlassId(klassId, pageable);
        return branchUserOngoingListGroupedKlassPage.map(BranchTuteeDto.ResponseBranchTuteeDto::toDto);
    }

    public Page<RepeatScheduleDto.ResponseRepeatScheduleDto> getKlassRepeatSchedule(Long klassId, Pageable pageable) {

        klassRepository.findById(klassId).orElseThrow(() -> new NotFoundException("해당 클래스가 존재하지 않습니다."));

        Page<RepeatSchedule> repeatSchedule = repeatScheduleRepository.findByKlassId(klassId, pageable);
        return repeatSchedule.map(RepeatScheduleDto.ResponseRepeatScheduleDto::toDto);
    }

    public Page<ScheduleDto.ResponseScheduleDto> getKlassSchedule(Long klassId, Pageable pageable){

        klassRepository.findById(klassId).orElseThrow(() -> new NotFoundException("해당 클래스가 존재하지 않습니다."));

        Page<Schedule> schedule = scheduleRepository.findByKlassId(klassId, pageable);
        return schedule.map(ScheduleDto.ResponseScheduleDto::toDto);
    }
}
