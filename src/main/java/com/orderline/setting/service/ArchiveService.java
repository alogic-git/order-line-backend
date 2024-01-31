package com.orderline.setting.service;

import com.orderline.basic.exception.NotFoundException;
import com.orderline.branch.enums.FunctionTypeEnum;
import com.orderline.branch.model.entity.BranchUserRole;
import com.orderline.branch.repository.BranchUserRoleRepository;
import com.orderline.klass.model.entity.Klass;
import com.orderline.klass.repository.KlassRepository;
import com.orderline.product.model.entity.Product;
import com.orderline.product.repository.ProductRepository;
import com.orderline.setting.model.dto.ArchiveDto;
import com.orderline.setting.model.entity.ArchiveList;
import com.orderline.setting.repository.ArchiveListRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class ArchiveService {

    @Resource(name = "archiveListRepository")
    ArchiveListRepository archiveListRepository;

    @Resource(name = "branchUserRoleRepository")
    BranchUserRoleRepository branchUserRoleRepository;

    @Resource(name = "productRepository")
    ProductRepository productRepository;

    @Resource(name = "klassRepository")
    KlassRepository klassRepository;

    public Page<ArchiveDto.ResponseArchiveDto> getArchiveList(Long branchId, Pageable pageable){

        Page<ArchiveList> archiveList = archiveListRepository.findByBranchId(branchId, pageable);

        return archiveList.map(ArchiveDto.ResponseArchiveDto::toDto);

    }

    public Page<ArchiveDto.ResponseArchiveDto> getArchiveListWithCategory(Long branchId, FunctionTypeEnum category, Pageable pageable){

        Page<ArchiveList> archiveList = archiveListRepository.findByBranchIdAndCategory(branchId, category, pageable);

        return archiveList.map(ArchiveDto.ResponseArchiveDto::toDto);

    }

    public void recovery(FunctionTypeEnum category, Long categoryId){

        if (category.equals(FunctionTypeEnum.MANAGER) || category.equals(FunctionTypeEnum.TUTOR) || category.equals(FunctionTypeEnum.TUTEE)) {
            Optional<BranchUserRole> branchUserRoleOptional = branchUserRoleRepository.findById(categoryId);
            if (!branchUserRoleOptional.isPresent()) {
                throw new NotFoundException( category.getText() + "가 존재하지 않습니다.");
            }
            BranchUserRole branchUserRole = branchUserRoleOptional.get();
            branchUserRole.updateArchive(false);
            branchUserRoleRepository.save(branchUserRole);
        } else if (category.equals(FunctionTypeEnum.PRODUCT)) {
            Optional<Product> productOptional = productRepository.findById(categoryId);
            if (!productOptional.isPresent()) {
                throw new NotFoundException( category.getText() + "가 존재하지 않습니다.");
            }
            Product product = productOptional.get();
            product.updateArchive(false);
            productRepository.save(product);
        } else if (category.equals(FunctionTypeEnum.KLASS)) {
            Optional<Klass> klassOptional = klassRepository.findById(categoryId);
            if (!klassOptional.isPresent()) {
                throw new NotFoundException( category.getText() + "가 존재하지 않습니다.");
            }
            Klass klass = klassOptional.get();
            klass.updateArchive(false);
            klassRepository.save(klass);
        } else {
            throw new NotFoundException( category.getText() + "는 복구할 수 없습니다.");
        }
    }

    public void delete(FunctionTypeEnum category, Long categoryId){

        if (category.equals(FunctionTypeEnum.MANAGER) || category.equals(FunctionTypeEnum.TUTOR) || category.equals(FunctionTypeEnum.TUTEE)) {
            Optional<BranchUserRole> branchUserRoleOptional = branchUserRoleRepository.findById(categoryId);
            if (!branchUserRoleOptional.isPresent()) {
                throw new NotFoundException( category.getText() + "가 존재하지 않습니다.");
            }
            BranchUserRole branchUserRole = branchUserRoleOptional.get();
            branchUserRole.delete();
            branchUserRoleRepository.save(branchUserRole);
        } else if (category.equals(FunctionTypeEnum.PRODUCT)) {
            Optional<Product> productOptional = productRepository.findById(categoryId);
            if (!productOptional.isPresent()) {
                throw new NotFoundException( category.getText() + "가 존재하지 않습니다.");
            }
            Product product = productOptional.get();
            product.delete();
            productRepository.save(product);
        } else if (category.equals(FunctionTypeEnum.KLASS)) {
            Optional<Klass> klassOptional = klassRepository.findById(categoryId);
            if (!klassOptional.isPresent()) {
                throw new NotFoundException( category.getText() + "가 존재하지 않습니다.");
            }
            Klass klass = klassOptional.get();
            klass.delete();
            klassRepository.save(klass);
        } else {
            throw new NotFoundException( category.getText() + "는 삭제할 수 없습니다.");
        }
    }
}
