package com.project.zoopiter.web;

import com.project.zoopiter.domain.entity.PetInfo;
import com.project.zoopiter.domain.member.svc.PetInfoSVC;
import com.project.zoopiter.web.form.pet.PetDetailForm;
import com.project.zoopiter.web.form.pet.PetSaveForm;
import com.project.zoopiter.web.form.pet.PetUpdateForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class PetInfoController {
  private final PetInfoSVC petInfoSVC;
// 등록 pet_reg
  // 등록양식
  @GetMapping("/petreg")
  public String saveInfo(Model model){
    PetSaveForm petSaveForm = new PetSaveForm();
    model.addAttribute("petSaveForm", petSaveForm);
//    String save = petInfoSVC.saveInfo(petInfo);

    return "mypage/mypage_pet_reg";
  }
  // 등록처리
//  PetInfo saveInfo(PetInfo petInfo);
  @PostMapping("/petreg")
  public String save(
      @Valid @ModelAttribute PetSaveForm petSaveForm,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes
      ){
    log.info("petSaveForm={}",petSaveForm);
    // 데이터 검증
    if (bindingResult.hasErrors()){
      log.info("bindingResult={}",bindingResult);
      return "mypage/mypage_pet_reg";
    }

    PetInfo petInfo = new PetInfo();
    petInfo.setPetImg(petSaveForm.getPetImg());
    petInfo.setPetType(petSaveForm.getPetType());
    petInfo.setPetBirth(petSaveForm.getPetBirth());
    petInfo.setPetGender(petSaveForm.getPetGender());
    petInfo.setPetName(petSaveForm.getPetName());
    petInfo.setPetYn(petSaveForm.getPetYn());
    petInfo.setPetDate(petSaveForm.getPetDate());
    petInfo.setPetVac(petSaveForm.getPetVac());
    petInfo.setPetInfo(petSaveForm.getPetInfo());

    String savedPetInfo = petInfoSVC.saveInfo(petInfo);
    redirectAttributes.addAttribute("id", savedPetInfo);
    return "redirect:/mypage/pet/{id}/detail";

  }

  // 조회
  @GetMapping("/{id}/detail")
  public String findInfo(
    @PathVariable("id") Long id,
    Model model
  ){
    Optional<PetInfo> findPetInfo = petInfoSVC.findInfo(id);
    PetInfo petInfo = findPetInfo.orElseThrow();

    PetDetailForm detailForm = new PetDetailForm();
    detailForm.setUserId(petInfo.getUserId());
    detailForm.setPetNum(petInfo.getPetNum());
    detailForm.setPetImg(petInfo.getPetImg());
    detailForm.setPetType(petInfo.getPetType());
    detailForm.setPetBirth(petInfo.getPetBirth());
    detailForm.setPetGender(petInfo.getPetGender());
    detailForm.setPetName(petInfo.getPetName());
    detailForm.setPetYn(petInfo.getPetYn());
    detailForm.setPetDate(petInfo.getPetDate());
    detailForm.setPetVac(petInfo.getPetVac());
    detailForm.setPetInfo(petInfo.getPetInfo());

    model.addAttribute("detailForm",detailForm);

    return "/mypage/pet_modify";
  }

// 수정 pet_modify > 메인으로 이동(보호자정보페이지)
//  int updateInfo (Long PetNum, PetInfo petInfo);
  // 수정양식
  @GetMapping("/{id}/edit")
  public String updateInfo(
      @PathVariable("id") Long id,
      Model model
  ){
    Optional<PetInfo> findPetInfo = petInfoSVC.findInfo(id);
    PetInfo petInfo = findPetInfo.orElseThrow();

    PetUpdateForm updateForm = new PetUpdateForm();
    updateForm.setUserId(petInfo.getUserId());
    updateForm.setPetNum(petInfo.getPetNum());
    updateForm.setPetImg(petInfo.getPetImg());
    updateForm.setPetType(petInfo.getPetType());
    updateForm.setPetBirth(petInfo.getPetBirth());
    updateForm.setPetGender(petInfo.getPetGender());
    updateForm.setPetName(petInfo.getPetName());
    updateForm.setPetYn(petInfo.getPetYn());
    updateForm.setPetDate(petInfo.getPetDate());
    updateForm.setPetVac(petInfo.getPetVac());
    updateForm.setPetInfo(petInfo.getPetInfo());

    model.addAttribute("updateForm",updateForm);

    return "../static/html/pet_modify";
  }

  // 수정
  @PostMapping("/{id}/edit")
  public String update(
      @PathVariable("id") Long petNum,
      @Valid @ModelAttribute PetUpdateForm petUpdateForm,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes
  ){
    // 데이터 검증
    if(bindingResult.hasErrors()){
      log.info("bindingResult={}",bindingResult);
      return "mypage/mypage_pet_reg";
    }

    PetInfo petInfo = new PetInfo();
    petInfo.setPetNum(petNum);

    petInfo.setPetImg(petUpdateForm.getPetImg());
    petInfo.setPetType(petUpdateForm.getPetType());
    petInfo.setPetBirth(petUpdateForm.getPetBirth());
    petInfo.setPetGender(petUpdateForm.getPetGender());
    petInfo.setPetName(petUpdateForm.getPetName());
    petInfo.setPetYn(petUpdateForm.getPetYn());
    petInfo.setPetDate(petUpdateForm.getPetDate());
    petInfo.setPetVac(petUpdateForm.getPetVac());
    petInfo.setPetInfo(petUpdateForm.getPetInfo());

    petInfoSVC.updateInfo(petNum, petInfo);

    redirectAttributes.addAttribute("id", petNum);
    return "redirect:/mypage/pet/{id}/detail";
  }

  // 삭제 > 메인으로 이동(보호자정보페이지)
//  int deleteInfo(Long PetNum);
  @GetMapping("{id}/del")
  public String deleteInfo(@PathVariable("id") Long PetNum){
    petInfoSVC.deleteInfo(PetNum);

    return "redirect:/mypage_main";
  }

  // 목록 > ?
//  List<PetInfo> findInfo();
  @GetMapping
  public String findAll(Model model){
    List<PetInfo> petInfo = petInfoSVC.findAll();
    model.addAttribute("petInfo",petInfo);

    return "/mypage/mypage_main";
  }
}
