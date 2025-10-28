package com.recordroom.recordroom.callbook.controller;

import com.recordroom.recordroom.callbook.entity.CallBook;
import com.recordroom.recordroom.callbook.service.CallBookService;
import com.recordroom.recordroom.dto.DataTableRequest;
import com.recordroom.recordroom.dto.DataTableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/callbook")
public class CallBookController {

    @Autowired
    private CallBookService callBookService;


    @GetMapping("/call_book_in_entry")
    public String fileInEntryLoadForm(Model model) {
        return "fragments/file_callbook/in_entry :: searchrecord";
    }

    @GetMapping("/call_book_out_entry")
    public String fileOutEntryLoadForm(Model model) {
        return "fragments/file_callbook/out_entry :: searchrecord";
    }



    @GetMapping("/call_book_view")
    public String fileViewMaster(Model model) {

        return "fragments/file_callbook/out_entry_report :: tabler";
    }


    @PostMapping("/load_callbook_data")
    @ResponseBody
    public DataTableResponse<CallBook> loadCallBookData(@RequestBody DataTableRequest request) {

        Page<CallBook> page = callBookService.findByFilters(request);

        DataTableResponse<CallBook> response = new DataTableResponse<>();
        response.setDraw(request.getDraw());
        response.setRecordsTotal(page.getTotalElements());
        response.setRecordsFiltered(page.getTotalElements());
        response.setData(page.getContent());

        return response;
    }




    @GetMapping("/searchForInWardEntry")
    public String searchForFileAvailabilityAlready(@RequestParam String new_drSerialNo, @RequestParam String new_dr_year, Model model) {

        Optional<CallBook> callBook = callBookService.findBydrSerialNoAndYearInward(Integer.parseInt(new_drSerialNo),Integer.parseInt(new_dr_year));
        if(callBook.isPresent()){
            model.addAttribute("callbook",callBook.get());
            model.addAttribute("successMsg", "✅ File is not New!. Update for Receving!");
            return "fragments/file_callbook/in_entry :: updateform";
        }
        else {
            model.addAttribute("errorMessage", "✅ File is Not Available!. You Can't Receive this File!");
            model.addAttribute("callbook",new CallBook());
            return "fragments/file_callbook/in_entry :: insertform";
        }
    }

    @PostMapping("/saveInWardEntry")
    public String saveCallBookInWardEntryIfNotFound(@ModelAttribute CallBook callBook, Model model) {

        callBook.setIs_current(true);
        callBook.setClosed_status(false);
        callBook.setIn_out_status(0);
        callBook.setUnique_key(Integer.parseInt(String.valueOf(callBook.getNew_drSerialNo()) + String.valueOf(callBook.getNew_dr_year())));
        callBook.setOld_drSerialNo(callBook.getNew_drSerialNo());
        callBook.setOld_dr_year(callBook.getNew_dr_year());
        callBook.setIn_coming_date(new Date());
        Optional<CallBook> callBook_check = callBookService.findForDuplicate(Integer.parseInt( String.valueOf(callBook.getNew_drSerialNo()) ),Integer.parseInt(String.valueOf(callBook.getNew_dr_year())));
        if(callBook_check.isPresent()){
            model.addAttribute("errorMessage", "This DR Serial No .Already Used");
            model.addAttribute("callbook",new CallBook());
            return "fragments/file_callbook/in_entry :: insertform";
        }
        else {
            callBookService.saveRecord(callBook);
            model.addAttribute("successMessage", "✅ Record saved successfully!");
            return "fragments/file_callbook/in_entry :: searchrecord";
        }
    }

    @PostMapping("/updateInWardEntry")
    public String updateCallBookInWardEntryIfFound(@ModelAttribute CallBook callBook, Model model) {

        callBook.setIs_current(true);
        callBook.setClosed_status(false);
        callBook.setIn_out_status(0);
        callBook.setIn_coming_date(new Date());
        callBookService.updateRecord(callBook);
        model.addAttribute("successMessage", "✅ Record Updated and Received successfully!");
        return "fragments/file_callbook/in_entry :: searchrecord";
    }

    @GetMapping("/searchForOutWardEntry")
    public String searchForFileAvailabilityAlreadyOutWard(@RequestParam String new_drSerialNo, @RequestParam String new_dr_year, Model model) {

        Optional<CallBook> callBook = callBookService.findBydrSerialNoAndYear(Integer.parseInt(new_drSerialNo),Integer.parseInt(new_dr_year));

        if(callBook.isPresent()){
            CallBookFileOutgoingDTO callBookFileOutgoingDTO = new CallBookFileOutgoingDTO();
            callBookFileOutgoingDTO.setId(callBook.get().getId());
            callBookFileOutgoingDTO.setNew_drSerialNo_d(callBook.get().getNew_drSerialNo());
            callBookFileOutgoingDTO.setNew_dr_year_d(callBook.get().getNew_dr_year());
            callBookFileOutgoingDTO.setCall_book_no(callBook.get().getCall_book_no());
            callBookFileOutgoingDTO.setUnique_key(callBook.get().getUnique_key());
            callBookFileOutgoingDTO.setPossible_out_date(callBook.get().getPossible_out_date());
            callBookFileOutgoingDTO.setCall_book_no(callBook.get().getCall_book_no());

            List<CallBook> records = callBookService.findByUniqueKey(callBook.get().getUnique_key());
            model.addAttribute("records", records);
            model.addAttribute("callbookdto",callBookFileOutgoingDTO);
            return "fragments/file_callbook/out_entry :: updateform";
        }
        else {
            model.addAttribute("successMessage", "✅ SomeThing Went Wrong!");
            return "fragments/file_callbook/out_entry :: searchrecord";
        }
    }

    @PostMapping("/saveAndUpdateOutWardEntry")
    public String saveCallBookOutWardEntry(@ModelAttribute CallBookFileOutgoingDTO callBookFileOutgoingDTO, Model model) {

        Optional<CallBook> callBook_current = callBookService.findById(callBookFileOutgoingDTO.getId());
        CallBook callBook_old= callBook_current.get();
        callBook_old.setIs_current(false);
        callBook_old.setClosed_status(true);
        callBook_old.setIn_out_status(1);
        callBook_old.setOut_going_date(new Date());
        callBookService.updateRecord(callBook_old);

        CallBook callBook = new CallBook();
        callBook.setPossible_out_date(callBookFileOutgoingDTO.getPossible_out_date());
        callBook.setNew_drSerialNo(callBookFileOutgoingDTO.getNew_drSerialNo());
        callBook.setNew_dr_year(callBookFileOutgoingDTO.getNew_dr_year());
        callBook.setOld_drSerialNo(callBook_old.getNew_drSerialNo());
        callBook.setOld_dr_year(callBookFileOutgoingDTO.getNew_dr_year());
        callBook.setCall_book_no(callBookFileOutgoingDTO.getCall_book_no());
        callBook.setSectionDealingHandName(callBookFileOutgoingDTO.getSectionDealingHandName());
        callBook.setSectionDealingHandPhoneNo(callBookFileOutgoingDTO.getSectionDealingHandPhoneNo());
        callBook.setRecordRoomDealingHandName(callBookFileOutgoingDTO.getRecordRoomDealingHandName());
        callBook.setUnique_key(callBook_old.getUnique_key());
        callBook.setIs_current(true);
        callBook.setClosed_status(false);
        callBook.setIn_out_status(1);
        callBook.setPurpose_Of_Outgoing_Fle(callBookFileOutgoingDTO.getPurpose_Of_Outgoing_Fle());
        callBook.setRack_no(callBookFileOutgoingDTO.getRack_no());
        callBook.setOut_going_date(new Date());

        callBookService.saveRecord(callBook);
        model.addAttribute("successMessage", "✅ Record saved successfully!.Take File");
        return "fragments/file_callbook/in_entry :: searchrecord";
    }
}